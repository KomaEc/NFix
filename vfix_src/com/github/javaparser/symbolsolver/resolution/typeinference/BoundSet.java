package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.typeinference.bounds.CapturesBound;
import com.github.javaparser.symbolsolver.resolution.typeinference.bounds.FalseBound;
import com.github.javaparser.symbolsolver.resolution.typeinference.bounds.SameAsBound;
import com.github.javaparser.symbolsolver.resolution.typeinference.bounds.SubtypeOfBound;
import com.github.javaparser.symbolsolver.resolution.typeinference.constraintformulas.TypeSameAsType;
import com.github.javaparser.symbolsolver.resolution.typeinference.constraintformulas.TypeSubtypeOfType;
import com.github.javaparser.utils.Pair;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BoundSet {
   private List<Bound> bounds = new LinkedList();
   private static final BoundSet EMPTY = new BoundSet();

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BoundSet boundSet = (BoundSet)o;
         return (new HashSet(this.bounds)).equals(new HashSet(boundSet.bounds));
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.bounds.hashCode();
   }

   public String toString() {
      return "BoundSet{bounds=" + this.bounds + '}';
   }

   public boolean isTrue() {
      return this.bounds.isEmpty();
   }

   public static BoundSet empty() {
      return EMPTY;
   }

   public BoundSet withBound(Bound bound) {
      if (this.bounds.contains(bound)) {
         return this;
      } else {
         BoundSet boundSet = new BoundSet();
         boundSet.bounds.addAll(this.bounds);
         boundSet.bounds.add(bound);
         return boundSet;
      }
   }

   private Optional<Pair<SameAsBound, SameAsBound>> findPairSameAs(Predicate<Pair<SameAsBound, SameAsBound>> condition) {
      for(int i = 0; i < this.bounds.size(); ++i) {
         Bound bi = (Bound)this.bounds.get(i);
         if (bi instanceof SameAsBound) {
            SameAsBound si = (SameAsBound)bi;

            for(int j = i + 1; j < this.bounds.size(); ++j) {
               Bound bj = (Bound)this.bounds.get(j);
               if (bj instanceof SameAsBound) {
                  SameAsBound sj = (SameAsBound)bj;
                  Pair<SameAsBound, SameAsBound> pair = new Pair(si, sj);
                  if (condition.test(pair)) {
                     return Optional.of(pair);
                  }
               }
            }
         }
      }

      return Optional.empty();
   }

   public boolean isEmpty() {
      return this.bounds.isEmpty();
   }

   private <T> T forEachPairSameAs(BoundSet.Processor<SameAsBound, SameAsBound, T> processor, T initialValue) {
      T currentValue = initialValue;

      for(int i = 0; i < this.bounds.size(); ++i) {
         Bound bi = (Bound)this.bounds.get(i);
         if (bi instanceof SameAsBound) {
            SameAsBound si = (SameAsBound)bi;

            for(int j = i + 1; j < this.bounds.size(); ++j) {
               Bound bj = (Bound)this.bounds.get(j);
               if (bj instanceof SameAsBound) {
                  SameAsBound sj = (SameAsBound)bj;
                  currentValue = processor.process(si, sj, currentValue);
               }
            }
         }
      }

      return currentValue;
   }

   private <T> T forEachPairSameAndSubtype(BoundSet.Processor<SameAsBound, SubtypeOfBound, T> processor, T initialValue) {
      T currentValue = initialValue;

      for(int i = 0; i < this.bounds.size(); ++i) {
         Bound bi = (Bound)this.bounds.get(i);
         if (bi instanceof SameAsBound) {
            SameAsBound si = (SameAsBound)bi;

            for(int j = i + 1; j < this.bounds.size(); ++j) {
               Bound bj = (Bound)this.bounds.get(j);
               if (bj instanceof SubtypeOfBound) {
                  SubtypeOfBound sj = (SubtypeOfBound)bj;
                  currentValue = processor.process(si, sj, currentValue);
               }
            }
         }
      }

      return currentValue;
   }

   private <T> T forEachPairSubtypeAndSubtype(BoundSet.Processor<SubtypeOfBound, SubtypeOfBound, T> processor, T initialValue) {
      T currentValue = initialValue;

      for(int i = 0; i < this.bounds.size(); ++i) {
         Bound bi = (Bound)this.bounds.get(i);
         if (bi instanceof SubtypeOfBound) {
            SubtypeOfBound si = (SubtypeOfBound)bi;

            for(int j = i + 1; j < this.bounds.size(); ++j) {
               Bound bj = (Bound)this.bounds.get(j);
               if (bj instanceof SubtypeOfBound) {
                  SubtypeOfBound sj = (SubtypeOfBound)bj;
                  currentValue = processor.process(si, sj, currentValue);
               }
            }
         }
      }

      return currentValue;
   }

   private boolean areSameTypeInference(ResolvedType a, ResolvedType b) {
      return TypeHelper.isInferenceVariable(a) && TypeHelper.isInferenceVariable(b) && a.equals(b);
   }

   private List<Pair<ResolvedReferenceType, ResolvedReferenceType>> findPairsOfCommonAncestors(ResolvedReferenceType r1, ResolvedReferenceType r2) {
      List<ResolvedReferenceType> set1 = new LinkedList();
      set1.add(r1);
      set1.addAll(r1.getAllAncestors());
      List<ResolvedReferenceType> set2 = new LinkedList();
      set2.add(r2);
      set2.addAll(r2.getAllAncestors());
      List<Pair<ResolvedReferenceType, ResolvedReferenceType>> pairs = new LinkedList();
      Iterator var6 = set1.iterator();

      while(var6.hasNext()) {
         ResolvedReferenceType rtFrom1 = (ResolvedReferenceType)var6.next();
         Iterator var8 = set2.iterator();

         while(var8.hasNext()) {
            ResolvedReferenceType rtFrom2 = (ResolvedReferenceType)var8.next();
            if (rtFrom1.getTypeDeclaration().equals(rtFrom2.getTypeDeclaration())) {
               pairs.add(new Pair(rtFrom1, rtFrom2));
            }
         }
      }

      return pairs;
   }

   public BoundSet incorporate(BoundSet otherBounds, TypeSolver typeSolver) {
      BoundSet newBoundSet = this;

      Bound b;
      for(Iterator var4 = otherBounds.bounds.iterator(); var4.hasNext(); newBoundSet = newBoundSet.withBound(b)) {
         b = (Bound)var4.next();
      }

      return newBoundSet.deriveImpliedBounds(typeSolver);
   }

   public BoundSet deriveImpliedBounds(TypeSolver typeSolver) {
      ConstraintFormulaSet newConstraintsSet = ConstraintFormulaSet.empty();
      newConstraintsSet = (ConstraintFormulaSet)this.forEachPairSameAs((a, bx, currentConstraintSet) -> {
         if (this.areSameTypeInference(a.getS(), bx.getS())) {
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSameAsType(a.getT(), bx.getT()));
         }

         if (this.areSameTypeInference(a.getS(), bx.getT())) {
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSameAsType(a.getS(), bx.getT()));
         }

         if (this.areSameTypeInference(a.getT(), bx.getS())) {
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSameAsType(a.getT(), bx.getS()));
         }

         if (this.areSameTypeInference(a.getT(), bx.getT())) {
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSameAsType(a.getS(), bx.getS()));
         }

         return currentConstraintSet;
      }, newConstraintsSet);
      newConstraintsSet = (ConstraintFormulaSet)this.forEachPairSameAndSubtype((a, bx, currentConstraintSet) -> {
         if (this.areSameTypeInference(a.getS(), bx.getS())) {
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSubtypeOfType(typeSolver, a.getT(), bx.getT()));
         }

         if (this.areSameTypeInference(a.getT(), bx.getS())) {
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSubtypeOfType(typeSolver, a.getS(), bx.getT()));
         }

         return currentConstraintSet;
      }, newConstraintsSet);
      newConstraintsSet = (ConstraintFormulaSet)this.forEachPairSameAndSubtype((a, bx, currentConstraintSet) -> {
         if (this.areSameTypeInference(a.getS(), bx.getT())) {
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSubtypeOfType(typeSolver, bx.getS(), a.getT()));
         }

         if (this.areSameTypeInference(a.getT(), bx.getT())) {
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSubtypeOfType(typeSolver, bx.getS(), a.getS()));
         }

         return currentConstraintSet;
      }, newConstraintsSet);
      newConstraintsSet = (ConstraintFormulaSet)this.forEachPairSubtypeAndSubtype((a, bx, currentConstraintSet) -> {
         if (this.areSameTypeInference(a.getT(), bx.getS())) {
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSubtypeOfType(typeSolver, bx.getS(), a.getT()));
         }

         return currentConstraintSet;
      }, newConstraintsSet);
      newConstraintsSet = (ConstraintFormulaSet)this.forEachPairSameAs((a, bx, currentConstraintSet) -> {
         InferenceVariable alpha;
         ResolvedType U;
         ResolvedType S;
         ResolvedType T;
         Substitution sub;
         if (TypeHelper.isInferenceVariable(a.getS()) && TypeHelper.isProperType(a.getT())) {
            alpha = (InferenceVariable)a.getS();
            U = a.getT();
            S = bx.getS();
            T = bx.getT();
            sub = Substitution.empty().withPair(alpha.getTypeParameterDeclaration(), U);
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSameAsType(sub.apply(S), sub.apply(T)));
         }

         if (TypeHelper.isInferenceVariable(a.getT()) && TypeHelper.isProperType(a.getS())) {
            alpha = (InferenceVariable)a.getT();
            U = a.getS();
            S = bx.getS();
            T = bx.getT();
            sub = Substitution.empty().withPair(alpha.getTypeParameterDeclaration(), U);
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSameAsType(sub.apply(S), sub.apply(T)));
         }

         if (TypeHelper.isInferenceVariable(bx.getS()) && TypeHelper.isProperType(bx.getT())) {
            alpha = (InferenceVariable)bx.getS();
            U = bx.getT();
            S = a.getS();
            T = a.getT();
            sub = Substitution.empty().withPair(alpha.getTypeParameterDeclaration(), U);
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSameAsType(sub.apply(S), sub.apply(T)));
         }

         if (TypeHelper.isInferenceVariable(bx.getT()) && TypeHelper.isProperType(bx.getS())) {
            alpha = (InferenceVariable)bx.getT();
            U = bx.getS();
            S = a.getS();
            T = a.getT();
            sub = Substitution.empty().withPair(alpha.getTypeParameterDeclaration(), U);
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSameAsType(sub.apply(S), sub.apply(T)));
         }

         return currentConstraintSet;
      }, newConstraintsSet);
      newConstraintsSet = (ConstraintFormulaSet)this.forEachPairSameAndSubtype((a, bx, currentConstraintSet) -> {
         InferenceVariable alpha;
         ResolvedType U;
         ResolvedType S;
         ResolvedType T;
         Substitution sub;
         if (TypeHelper.isInferenceVariable(a.getS()) && TypeHelper.isProperType(a.getT())) {
            alpha = (InferenceVariable)a.getS();
            U = a.getT();
            S = bx.getS();
            T = bx.getT();
            sub = Substitution.empty().withPair(alpha.getTypeParameterDeclaration(), U);
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSubtypeOfType(typeSolver, sub.apply(S), sub.apply(T)));
         }

         if (TypeHelper.isInferenceVariable(a.getT()) && TypeHelper.isProperType(a.getS())) {
            alpha = (InferenceVariable)a.getT();
            U = a.getS();
            S = bx.getS();
            T = bx.getT();
            sub = Substitution.empty().withPair(alpha.getTypeParameterDeclaration(), U);
            currentConstraintSet = currentConstraintSet.withConstraint(new TypeSubtypeOfType(typeSolver, sub.apply(S), sub.apply(T)));
         }

         return currentConstraintSet;
      }, newConstraintsSet);
      newConstraintsSet = (ConstraintFormulaSet)this.forEachPairSubtypeAndSubtype((a, bx, currentConstraintSet) -> {
         if (TypeHelper.isInferenceVariable(a.getS()) && TypeHelper.isInferenceVariable(bx.getS()) && a.getT().isReferenceType() && bx.getT().isReferenceType()) {
            ResolvedReferenceType S = a.getT().asReferenceType();
            ResolvedReferenceType T = bx.getT().asReferenceType();
            List<Pair<ResolvedReferenceType, ResolvedReferenceType>> pairs = this.findPairsOfCommonAncestors(S, T);
            Iterator var7 = pairs.iterator();

            while(var7.hasNext()) {
               Pair<ResolvedReferenceType, ResolvedReferenceType> pair = (Pair)var7.next();

               for(int i = 0; i < Math.min(((ResolvedReferenceType)pair.a).typeParametersValues().size(), ((ResolvedReferenceType)pair.b).typeParametersValues().size()); ++i) {
                  ResolvedType si = (ResolvedType)((ResolvedReferenceType)pair.a).typeParametersValues().get(i);
                  ResolvedType ti = (ResolvedType)((ResolvedReferenceType)pair.b).typeParametersValues().get(i);
                  if (!si.isWildcard() && !ti.isWildcard()) {
                     currentConstraintSet = currentConstraintSet.withConstraint(new TypeSameAsType(si, ti));
                  }
               }
            }
         }

         return currentConstraintSet;
      }, newConstraintsSet);
      Iterator var3 = ((List)this.bounds.stream().filter((bx) -> {
         return bx instanceof CapturesBound;
      }).collect(Collectors.toList())).iterator();
      if (var3.hasNext()) {
         Bound b = (Bound)var3.next();
         CapturesBound capturesBound = (CapturesBound)b;
         throw new UnsupportedOperationException();
      } else if (newConstraintsSet.isEmpty()) {
         return this;
      } else {
         BoundSet newBounds = newConstraintsSet.reduce(typeSolver);
         return newBounds.isEmpty() ? this : this.incorporate(newBounds, typeSolver);
      }
   }

   public boolean containsFalse() {
      return this.bounds.stream().anyMatch((it) -> {
         return it instanceof FalseBound;
      });
   }

   private Set<InferenceVariable> allInferenceVariables() {
      Set<InferenceVariable> variables = new HashSet();
      Iterator var2 = this.bounds.iterator();

      while(var2.hasNext()) {
         Bound b = (Bound)var2.next();
         variables.addAll(b.usedInferenceVariables());
      }

      return variables;
   }

   private boolean hasInstantiationFor(InferenceVariable v) {
      Iterator var2 = this.bounds.iterator();

      Bound b;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         b = (Bound)var2.next();
      } while(!b.isAnInstantiationFor(v));

      return true;
   }

   private Instantiation getInstantiationFor(InferenceVariable v) {
      Iterator var2 = this.bounds.iterator();

      Bound b;
      do {
         if (!var2.hasNext()) {
            throw new IllegalArgumentException();
         }

         b = (Bound)var2.next();
      } while(!b.isAnInstantiationFor(v));

      return (Instantiation)b.isAnInstantiation().get();
   }

   private boolean thereIsSomeJSuchThatβequalAlphaJ(Set<InferenceVariable> alphas, InferenceVariable beta) {
      Iterator var3 = alphas.iterator();

      while(var3.hasNext()) {
         InferenceVariable alphaJ = (InferenceVariable)var3.next();
         Iterator var5 = this.bounds.iterator();

         while(var5.hasNext()) {
            Bound b = (Bound)var5.next();
            if (b instanceof SameAsBound) {
               SameAsBound sameAsBound = (SameAsBound)b;
               if (sameAsBound.getS().equals(alphaJ) && sameAsBound.getT().equals(beta)) {
                  return true;
               }

               if (sameAsBound.getT().equals(alphaJ) && sameAsBound.getS().equals(beta)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private <T> List<Set<T>> buildAllSubsetsOfSize(Set<T> allElements, int desiredSize) {
      if (desiredSize == allElements.size()) {
         return Arrays.asList(allElements);
      } else {
         List<Set<T>> res = new LinkedList();
         Iterator var4 = allElements.iterator();

         while(var4.hasNext()) {
            T element = var4.next();
            Set<T> subset = this.allButOne(allElements, element);
            res.addAll(this.buildAllSubsetsOfSize(subset, desiredSize));
         }

         return res;
      }
   }

   private <T> Set<T> allButOne(Set<T> elements, T element) {
      Set<T> set = new HashSet(elements);
      set.remove(element);
      return set;
   }

   private Optional<Set<InferenceVariable>> smallestSetWithProperty(Set<InferenceVariable> uninstantiatedVariables, List<BoundSet.VariableDependency> dependencies) {
      for(int i = 1; i <= uninstantiatedVariables.size(); ++i) {
         Iterator var4 = this.buildAllSubsetsOfSize(uninstantiatedVariables, i).iterator();

         while(var4.hasNext()) {
            Set<InferenceVariable> aSubSet = (Set)var4.next();
            if (this.hasProperty(aSubSet, dependencies)) {
               return Optional.of(aSubSet);
            }
         }
      }

      return Optional.empty();
   }

   private boolean hasProperty(Set<InferenceVariable> alphas, List<BoundSet.VariableDependency> dependencies) {
      Iterator var3 = alphas.iterator();

      while(var3.hasNext()) {
         InferenceVariable alphaI = (InferenceVariable)var3.next();
         Iterator var5 = ((List)dependencies.stream().filter((d) -> {
            return d.depending.equals(alphaI);
         }).filter((d) -> {
            return !d.isReflexive();
         }).map((d) -> {
            return d.dependedOn;
         }).collect(Collectors.toList())).iterator();

         while(var5.hasNext()) {
            InferenceVariable beta = (InferenceVariable)var5.next();
            if (!this.hasInstantiationFor(beta) && !this.thereIsSomeJSuchThatβequalAlphaJ(alphas, beta)) {
               return false;
            }
         }
      }

      return true;
   }

   public Optional<InstantiationSet> performResolution(List<InferenceVariable> variablesToResolve, TypeSolver typeSolver) {
      if (this.containsFalse()) {
         return Optional.empty();
      } else {
         List<BoundSet.VariableDependency> dependencies = new LinkedList();
         Iterator var4 = this.bounds.iterator();

         Bound b;
         while(var4.hasNext()) {
            b = (Bound)var4.next();
            if (b instanceof CapturesBound) {
               throw new UnsupportedOperationException();
            }
         }

         var4 = this.bounds.iterator();

         while(var4.hasNext()) {
            b = (Bound)var4.next();
            if (b instanceof CapturesBound) {
               throw new UnsupportedOperationException();
            }
         }

         for(int i = 0; i < dependencies.size(); ++i) {
            BoundSet.VariableDependency di = (BoundSet.VariableDependency)dependencies.get(i);

            for(int j = i + 1; j < dependencies.size(); ++j) {
               BoundSet.VariableDependency dj = (BoundSet.VariableDependency)dependencies.get(j);
               if (di.dependedOn.equals(dj.depending)) {
                  dependencies.add(new BoundSet.VariableDependency(di.getDepending(), dj.getDependedOn()));
               }
            }
         }

         var4 = this.allInferenceVariables().iterator();

         while(var4.hasNext()) {
            InferenceVariable v = (InferenceVariable)var4.next();
            dependencies.add(new BoundSet.VariableDependency(v, v));
         }

         Set<InferenceVariable> V = new HashSet();
         V.addAll(variablesToResolve);
         Iterator var21 = dependencies.iterator();

         while(var21.hasNext()) {
            BoundSet.VariableDependency dependency = (BoundSet.VariableDependency)var21.next();
            if (variablesToResolve.contains(dependency.depending)) {
               V.add(dependency.dependedOn);
            }
         }

         boolean ok = true;
         Iterator var24 = V.iterator();

         while(var24.hasNext()) {
            InferenceVariable v = (InferenceVariable)var24.next();
            if (!this.hasInstantiationFor(v)) {
               ok = false;
            }
         }

         InferenceVariable v;
         Iterator var27;
         if (!ok) {
            Set<InferenceVariable> uninstantiatedPortionOfV = new HashSet();
            var27 = V.iterator();

            while(var27.hasNext()) {
               v = (InferenceVariable)var27.next();
               if (!this.hasInstantiationFor(v)) {
                  uninstantiatedPortionOfV.add(v);
               }
            }

            var27 = this.allSetsWithProperty(uninstantiatedPortionOfV, dependencies).iterator();
            if (var27.hasNext()) {
               Set<InferenceVariable> alphas = (Set)var27.next();
               boolean hasSomeCaptureForAlphas = alphas.stream().anyMatch((alphaIx) -> {
                  return this.appearInLeftPartOfCapture(alphaIx);
               });
               if (!hasSomeCaptureForAlphas) {
                  BoundSet newBounds = empty();

                  InferenceVariable alphaI;
                  Object Ti;
                  for(Iterator var11 = alphas.iterator(); var11.hasNext(); newBounds = newBounds.withBound(new SameAsBound(alphaI, (ResolvedType)Ti))) {
                     alphaI = (InferenceVariable)var11.next();
                     Set<ResolvedType> properLowerBounds = (Set)this.bounds.stream().filter((bx) -> {
                        return bx.isProperLowerBoundFor(alphaI).isPresent();
                     }).map((bx) -> {
                        return ((ProperLowerBound)bx.isProperLowerBoundFor(alphaI).get()).getProperType();
                     }).collect(Collectors.toSet());
                     Ti = null;
                     if (properLowerBounds.size() > 0) {
                        Ti = TypeHelper.leastUpperBound(properLowerBounds);
                     }

                     boolean throwsBound = this.bounds.stream().anyMatch((bx) -> {
                        return bx.isThrowsBoundOn(alphaI);
                     });
                     if (Ti == null && throwsBound && this.properUpperBoundsAreAtMostExceptionThrowableAndObject(alphaI)) {
                        Ti = new ReferenceTypeImpl(typeSolver.solveType(RuntimeException.class.getCanonicalName()), typeSolver);
                     }

                     if (Ti == null) {
                        Set<ResolvedType> properUpperBounds = (Set)this.bounds.stream().filter((bx) -> {
                           return bx.isProperUpperBoundFor(alphaI).isPresent();
                        }).map((bx) -> {
                           return ((ProperUpperBound)bx.isProperUpperBoundFor(alphaI).get()).getProperType();
                        }).collect(Collectors.toSet());
                        if (properUpperBounds.size() == 0) {
                           throw new IllegalStateException();
                        }

                        Ti = TypeHelper.glb(properUpperBounds);
                     }
                  }

                  BoundSet incorporatedBoundSet = this.incorporate(newBounds, typeSolver);
                  if (!incorporatedBoundSet.containsFalse()) {
                     return incorporatedBoundSet.performResolution(variablesToResolve, typeSolver);
                  } else {
                     throw new UnsupportedOperationException();
                  }
               } else {
                  throw new UnsupportedOperationException();
               }
            } else {
               return Optional.empty();
            }
         } else {
            InstantiationSet instantiationSet = InstantiationSet.empty();

            for(var27 = V.iterator(); var27.hasNext(); instantiationSet = instantiationSet.withInstantiation(this.getInstantiationFor(v))) {
               v = (InferenceVariable)var27.next();
            }

            return Optional.of(instantiationSet);
         }
      }
   }

   private Set<Set<InferenceVariable>> allPossibleSetsWithProperty(Set<InferenceVariable> allElements, List<BoundSet.VariableDependency> dependencies) {
      Set<Set<InferenceVariable>> result = new HashSet();

      for(int i = 1; i <= allElements.size(); ++i) {
         Iterator var5 = this.buildAllSubsetsOfSize(allElements, i).iterator();

         while(var5.hasNext()) {
            Set<InferenceVariable> aSubSet = (Set)var5.next();
            if (this.hasProperty(aSubSet, dependencies)) {
               result.add(aSubSet);
            }
         }
      }

      return result;
   }

   private boolean thereAreProperSubsets(Set<InferenceVariable> aSet, Set<Set<InferenceVariable>> allPossibleSets) {
      Iterator var3 = allPossibleSets.iterator();

      Set anotherSet;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         anotherSet = (Set)var3.next();
      } while(anotherSet.equals(aSet) || !this.isTheFirstAProperSubsetOfTheSecond(anotherSet, aSet));

      return true;
   }

   private boolean isTheFirstAProperSubsetOfTheSecond(Set<InferenceVariable> subset, Set<InferenceVariable> originalSet) {
      return originalSet.containsAll(subset) && originalSet.size() > subset.size();
   }

   private Set<Set<InferenceVariable>> allSetsWithProperty(Set<InferenceVariable> allElements, List<BoundSet.VariableDependency> dependencies) {
      Set<Set<InferenceVariable>> allPossibleSets = this.allPossibleSetsWithProperty(allElements, dependencies);
      Set<Set<InferenceVariable>> selected = new HashSet();
      Iterator var5 = allPossibleSets.iterator();

      while(var5.hasNext()) {
         Set<InferenceVariable> aSet = (Set)var5.next();
         if (!this.thereAreProperSubsets(aSet, allPossibleSets)) {
            selected.add(aSet);
         }
      }

      return selected;
   }

   private boolean properUpperBoundsAreAtMostExceptionThrowableAndObject(InferenceVariable inferenceVariable) {
      throw new UnsupportedOperationException();
   }

   private boolean appearInLeftPartOfCapture(InferenceVariable inferenceVariable) {
      Iterator var2 = this.bounds.iterator();

      while(var2.hasNext()) {
         Bound b = (Bound)var2.next();
         if (b instanceof CapturesBound) {
            CapturesBound capturesBound = (CapturesBound)b;
            if (capturesBound.getInferenceVariables().contains(inferenceVariable)) {
               return true;
            }
         }
      }

      return false;
   }

   public List<Bound> getProperUpperBoundsFor(InferenceVariable inferenceVariable) {
      return (List)this.bounds.stream().filter((b) -> {
         return b.isProperUpperBoundFor(inferenceVariable).isPresent();
      }).collect(Collectors.toList());
   }

   private class VariableDependency {
      private InferenceVariable depending;
      private InferenceVariable dependedOn;

      public VariableDependency(InferenceVariable depending, InferenceVariable dependedOn) {
         this.depending = depending;
         this.dependedOn = dependedOn;
      }

      public InferenceVariable getDepending() {
         return this.depending;
      }

      public InferenceVariable getDependedOn() {
         return this.dependedOn;
      }

      public boolean isReflexive() {
         return this.dependedOn.equals(this.depending);
      }
   }

   interface Processor<B1 extends Bound, B2 extends Bound, R> {
      R process(B1 var1, B2 var2, R var3);
   }
}

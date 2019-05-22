package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.typeinference.bounds.SubtypeOfBound;
import com.github.javaparser.symbolsolver.resolution.typeinference.bounds.ThrowsBound;
import com.github.javaparser.symbolsolver.resolution.typeinference.constraintformulas.ExpressionCompatibleWithType;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TypeInference {
   private final ResolvedType object;
   private TypeSolver typeSolver;

   public TypeInference(TypeSolver typeSolver) {
      if (typeSolver == null) {
         throw new NullPointerException();
      } else {
         this.typeSolver = typeSolver;
         this.object = new ReferenceTypeImpl(typeSolver.solveType(Object.class.getCanonicalName()), typeSolver);
      }
   }

   public static MethodUsage toMethodUsage(MethodCallExpr call, ResolvedMethodDeclaration methodDeclaration, TypeSolver typeSolver) {
      TypeInference typeInference = new TypeInference(typeSolver);
      Optional<InstantiationSet> instantiationSetOpt = typeInference.instantiationInference(call, methodDeclaration);
      if (instantiationSetOpt.isPresent()) {
         return instantiationSetToMethodUsage(methodDeclaration, (InstantiationSet)instantiationSetOpt.get());
      } else {
         throw new IllegalArgumentException();
      }
   }

   public Optional<InstantiationSet> instantiationInference(MethodCallExpr methodCallExpr, ResolvedMethodDeclaration methodDeclaration) {
      return this.instantiationInference((List)methodCallExpr.getArguments(), methodDeclaration);
   }

   public Optional<InstantiationSet> instantiationInference(List<Expression> argumentExpressions, ResolvedMethodDeclaration methodDeclaration) {
      List<ResolvedTypeParameterDeclaration> Ps = methodDeclaration.getTypeParameters();
      List<InferenceVariable> alphas = InferenceVariable.instantiate(Ps);
      Substitution theta = Substitution.empty();

      for(int i = 0; i < Ps.size(); ++i) {
         theta = theta.withPair((ResolvedTypeParameterDeclaration)Ps.get(0), (ResolvedType)alphas.get(0));
      }

      BoundSet B0 = this.boundSetup(Ps, alphas);
      BoundSet B1 = B0;

      for(int i = 0; i < Ps.size(); ++i) {
         ResolvedTypeParameterDeclaration Pi = (ResolvedTypeParameterDeclaration)Ps.get(i);
         if (this.appearInThrowsClause(Pi, methodDeclaration)) {
            B1 = B1.withBound(new ThrowsBound((InferenceVariable)alphas.get(i)));
         }
      }

      List<ResolvedType> Fs = this.formalParameterTypes(methodDeclaration);
      Optional<ConstraintFormulaSet> C = Optional.empty();
      if (!C.isPresent()) {
         C = this.testForApplicabilityByStrictInvocation(Fs, argumentExpressions, theta);
      }

      if (!C.isPresent()) {
         C = this.testForApplicabilityByLooseInvocation(Fs, argumentExpressions, theta);
      }

      if (!C.isPresent()) {
         C = this.testForApplicabilityByVariableArityInvocation(Fs, argumentExpressions, theta);
      }

      if (!C.isPresent()) {
         return Optional.empty();
      } else {
         BoundSet resultingBounds = ((ConstraintFormulaSet)C.get()).reduce(this.typeSolver);
         BoundSet B2 = B1.incorporate(resultingBounds, this.typeSolver);
         if (B2.containsFalse()) {
            return Optional.empty();
         } else {
            Optional<InstantiationSet> instantiation = B2.performResolution(alphas, this.typeSolver);
            return instantiation;
         }
      }
   }

   public boolean invocationApplicabilityInference(MethodCallExpr methodCallExpr, ResolvedMethodDeclaration methodDeclaration) {
      if (!methodCallExpr.getNameAsString().equals(methodDeclaration.getName())) {
         throw new IllegalArgumentException();
      } else {
         Optional<InstantiationSet> partial = this.instantiationInference(methodCallExpr, methodDeclaration);
         if (!partial.isPresent()) {
            return false;
         } else {
            int nActualParams = methodCallExpr.getArguments().size();
            int nFormalParams = methodDeclaration.getNumberOfParams();
            if (nActualParams != nFormalParams) {
               if (!methodDeclaration.hasVariadicParameter()) {
                  return false;
               }

               if (nActualParams < nFormalParams - 1) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public BoundSet invocationTypeInferenceBoundsSetB3() {
      throw new UnsupportedOperationException();
   }

   public void invocationTypeInference() {
      BoundSet B3 = this.invocationTypeInferenceBoundsSetB3();
      throw new UnsupportedOperationException();
   }

   public void functionalInterfaceParameterizationInference(LambdaExpr lambdaExpr, ResolvedInterfaceDeclaration interfaceDeclaration) {
      int n = lambdaExpr.getParameters().size();
      if (interfaceDeclaration.getTypeParameters().isEmpty()) {
         throw new IllegalArgumentException("Functional Interface without type arguments");
      } else {
         int k = interfaceDeclaration.getTypeParameters().size();
         List<InferenceVariable> alphas = InferenceVariable.instantiate(interfaceDeclaration.getTypeParameters());
         TypeInferenceCache.recordInferenceVariables(this.typeSolver, lambdaExpr, alphas);
         if (n != k) {
            throw new IllegalArgumentException("No valida parameterization can exist has n= and k=" + k);
         } else {
            ConstraintFormulaSet constraintFormulaSet = ConstraintFormulaSet.empty();
            int i = 0;
            if (i < n) {
               throw new UnsupportedOperationException();
            } else {
               constraintFormulaSet.reduce(this.typeSolver);
               throw new UnsupportedOperationException();
            }
         }
      }
   }

   public boolean moreSpecificMethodInference(MethodCallExpr methodCall, ResolvedMethodDeclaration m1, ResolvedMethodDeclaration m2) {
      if (!m2.isGeneric()) {
         throw new IllegalArgumentException("M2 is not generic (m2: " + m2 + ")");
      } else {
         throw new UnsupportedOperationException();
      }
   }

   private static MethodUsage instantiationSetToMethodUsage(ResolvedMethodDeclaration methodDeclaration, InstantiationSet instantiationSet) {
      if (instantiationSet.isEmpty()) {
         return new MethodUsage(methodDeclaration);
      } else {
         List<ResolvedType> paramTypes = new LinkedList();

         for(int i = 0; i < methodDeclaration.getNumberOfParams(); ++i) {
            paramTypes.add(instantiationSet.apply(methodDeclaration.getParam(i).getType()));
         }

         ResolvedType returnType = instantiationSet.apply(methodDeclaration.getReturnType());
         return new MethodUsage(methodDeclaration, paramTypes, returnType);
      }
   }

   private BoundSet boundSetup(List<ResolvedTypeParameterDeclaration> typeParameterDeclarations, List<InferenceVariable> inferenceVariables) {
      if (typeParameterDeclarations.size() != inferenceVariables.size()) {
         throw new IllegalArgumentException();
      } else {
         BoundSet boundSet = BoundSet.empty();

         for(int l = 0; l < typeParameterDeclarations.size(); ++l) {
            ResolvedTypeParameterDeclaration Pl = (ResolvedTypeParameterDeclaration)typeParameterDeclarations.get(l);
            InferenceVariable alphaL = (InferenceVariable)inferenceVariables.get(l);
            if (Pl.getBounds().isEmpty()) {
               boundSet = boundSet.withBound(new SubtypeOfBound(alphaL, this.object));
            } else {
               Iterator var7 = Pl.getBounds().iterator();

               while(var7.hasNext()) {
                  ResolvedTypeParameterDeclaration.Bound bound = (ResolvedTypeParameterDeclaration.Bound)var7.next();
                  ResolvedType T = bound.getType();
                  Substitution substitution = Substitution.empty();

                  for(int j = 0; j < typeParameterDeclarations.size(); ++j) {
                     substitution = substitution.withPair((ResolvedTypeParameterDeclaration)typeParameterDeclarations.get(j), (ResolvedType)inferenceVariables.get(j));
                  }

                  ResolvedType TWithSubstitutions = substitution.apply(T);
                  boundSet = boundSet.withBound(new SubtypeOfBound(alphaL, TWithSubstitutions));
                  if (boundSet.getProperUpperBoundsFor(alphaL).isEmpty()) {
                     boundSet = boundSet.withBound(new SubtypeOfBound(alphaL, this.object));
                  }
               }
            }
         }

         return boundSet;
      }
   }

   private boolean appearInThrowsClause(ResolvedTypeParameterDeclaration p, ResolvedMethodDeclaration methodDeclaration) {
      for(int j = 0; j < methodDeclaration.getNumberOfSpecifiedExceptions(); ++j) {
         ResolvedType thrownType = methodDeclaration.getSpecifiedException(j);
         if (thrownType.isTypeVariable() && thrownType.asTypeVariable().asTypeParameter().equals(p)) {
            return true;
         }
      }

      return false;
   }

   private List<ResolvedType> formalParameterTypes(ResolvedMethodDeclaration methodDeclaration) {
      List<ResolvedType> types = new LinkedList();

      for(int i = 0; i < methodDeclaration.getNumberOfParams(); ++i) {
         types.add(methodDeclaration.getParam(i).getType());
      }

      return types;
   }

   private boolean isImplicitlyTyped(LambdaExpr lambdaExpr) {
      return lambdaExpr.getParameters().stream().anyMatch((p) -> {
         return p.getType() instanceof UnknownType;
      });
   }

   private boolean isInexact(MethodReferenceExpr methodReferenceExpr) {
      throw new UnsupportedOperationException();
   }

   private boolean isPertinentToApplicability(Expression argument) {
      if (argument instanceof LambdaExpr) {
         LambdaExpr lambdaExpr = (LambdaExpr)argument;
         if (this.isImplicitlyTyped(lambdaExpr)) {
            return false;
         }
      }

      if (argument instanceof MethodReferenceExpr) {
         MethodReferenceExpr methodReferenceExpr = (MethodReferenceExpr)argument;
         if (this.isInexact(methodReferenceExpr)) {
            return false;
         }
      }

      if (argument instanceof LambdaExpr) {
         throw new UnsupportedOperationException();
      } else if (argument instanceof MethodReferenceExpr) {
         throw new UnsupportedOperationException();
      } else if (argument instanceof LambdaExpr) {
         throw new UnsupportedOperationException();
      } else if (argument instanceof LambdaExpr) {
         throw new UnsupportedOperationException();
      } else if (argument instanceof EnclosedExpr) {
         EnclosedExpr enclosedExpr = (EnclosedExpr)argument;
         return this.isPertinentToApplicability(enclosedExpr.getInner());
      } else if (!(argument instanceof ConditionalExpr)) {
         return true;
      } else {
         ConditionalExpr conditionalExpr = (ConditionalExpr)argument;
         return this.isPertinentToApplicability(conditionalExpr.getThenExpr()) && this.isPertinentToApplicability(conditionalExpr.getElseExpr());
      }
   }

   private Optional<ConstraintFormulaSet> testForApplicabilityByStrictInvocation(List<ResolvedType> Fs, List<Expression> es, Substitution theta) {
      int n = Fs.size();
      int k = es.size();
      if (k != n) {
         return Optional.empty();
      } else {
         for(int i = 0; i < n; ++i) {
            Expression ei = (Expression)es.get(i);
            ResolvedType fi = (ResolvedType)Fs.get(i);
            if (this.isPertinentToApplicability(ei)) {
               if (ExpressionHelper.isStandaloneExpression(ei) && JavaParserFacade.get(this.typeSolver).getType(ei).isPrimitive() && fi.isReferenceType()) {
                  return Optional.empty();
               }

               if (fi.isPrimitive() && (!ExpressionHelper.isStandaloneExpression(ei) || !JavaParserFacade.get(this.typeSolver).getType(ei).isPrimitive())) {
                  return Optional.empty();
               }
            }
         }

         return Optional.of(this.constraintSetFromArgumentsSubstitution(Fs, es, theta, k));
      }
   }

   private ResolvedType typeWithSubstitution(ResolvedType originalType, Substitution substitution) {
      return substitution.apply(originalType);
   }

   private Optional<ConstraintFormulaSet> testForApplicabilityByLooseInvocation(List<ResolvedType> Fs, List<Expression> es, Substitution theta) {
      int n = Fs.size();
      int k = es.size();
      return k != n ? Optional.empty() : Optional.of(this.constraintSetFromArgumentsSubstitution(Fs, es, theta, k));
   }

   private ConstraintFormulaSet constraintSetFromArgumentsSubstitution(List<ResolvedType> Fs, List<Expression> es, Substitution theta, int k) {
      ConstraintFormulaSet constraintFormulaSet = ConstraintFormulaSet.empty();

      for(int i = 0; i < k; ++i) {
         Expression ei = (Expression)es.get(i);
         ResolvedType fi = (ResolvedType)Fs.get(i);
         ResolvedType fiTheta = this.typeWithSubstitution(fi, theta);
         constraintFormulaSet = constraintFormulaSet.withConstraint(new ExpressionCompatibleWithType(this.typeSolver, ei, fiTheta));
      }

      return constraintFormulaSet;
   }

   private Optional<ConstraintFormulaSet> testForApplicabilityByVariableArityInvocation(List<ResolvedType> Fs, List<Expression> es, Substitution theta) {
      int k = es.size();
      List<ResolvedType> FsFirst = new LinkedList();

      for(int i = 0; i < k; ++i) {
         ResolvedType FFirstI = i < Fs.size() ? (ResolvedType)Fs.get(i) : (ResolvedType)Fs.get(Fs.size() - 1);
         FsFirst.add(FFirstI);
      }

      return Optional.of(this.constraintSetFromArgumentsSubstitution(FsFirst, es, theta, k));
   }
}

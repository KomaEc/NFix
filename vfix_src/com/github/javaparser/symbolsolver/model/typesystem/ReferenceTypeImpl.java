package com.github.javaparser.symbolsolver.model.typesystem;

import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeTransformer;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.resolution.types.parametrization.ResolvedTypeParametersMap;
import com.github.javaparser.symbolsolver.javaparsermodel.LambdaArgumentTypePlaceholder;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserTypeVariableDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReferenceTypeImpl extends ResolvedReferenceType {
   private TypeSolver typeSolver;

   public static ResolvedReferenceType undeterminedParameters(ResolvedReferenceTypeDeclaration typeDeclaration, TypeSolver typeSolver) {
      return new ReferenceTypeImpl(typeDeclaration, (List)typeDeclaration.getTypeParameters().stream().map(ResolvedTypeVariable::new).collect(Collectors.toList()), typeSolver);
   }

   protected ResolvedReferenceType create(ResolvedReferenceTypeDeclaration typeDeclaration, List<ResolvedType> typeParametersCorrected) {
      return new ReferenceTypeImpl(typeDeclaration, typeParametersCorrected, this.typeSolver);
   }

   protected ResolvedReferenceType create(ResolvedReferenceTypeDeclaration typeDeclaration) {
      return new ReferenceTypeImpl(typeDeclaration, this.typeSolver);
   }

   public ReferenceTypeImpl(ResolvedReferenceTypeDeclaration typeDeclaration, TypeSolver typeSolver) {
      super(typeDeclaration);
      this.typeSolver = typeSolver;
   }

   public ReferenceTypeImpl(ResolvedReferenceTypeDeclaration typeDeclaration, List<ResolvedType> typeArguments, TypeSolver typeSolver) {
      super(typeDeclaration, typeArguments);
      this.typeSolver = typeSolver;
   }

   public ResolvedTypeParameterDeclaration asTypeParameter() {
      if (this.typeDeclaration instanceof JavaParserTypeVariableDeclaration) {
         JavaParserTypeVariableDeclaration javaParserTypeVariableDeclaration = (JavaParserTypeVariableDeclaration)this.typeDeclaration;
         return javaParserTypeVariableDeclaration.asTypeParameter();
      } else {
         throw new UnsupportedOperationException(this.typeDeclaration.getClass().getCanonicalName());
      }
   }

   public boolean isAssignableBy(ResolvedType other) {
      if (other instanceof NullType) {
         return !this.isPrimitive();
      } else if (!other.isVoid() && this.getQualifiedName().equals(Object.class.getCanonicalName())) {
         return true;
      } else if (other.isPrimitive()) {
         if (this.getQualifiedName().equals(Object.class.getCanonicalName())) {
            return true;
         } else if (this.isCorrespondingBoxingType(other.describe())) {
            return true;
         } else {
            SymbolReference<ResolvedReferenceTypeDeclaration> type = this.typeSolver.tryToSolveType(other.asPrimitive().getBoxTypeQName());
            return ((ResolvedReferenceTypeDeclaration)type.getCorrespondingDeclaration()).canBeAssignedTo(super.typeDeclaration);
         }
      } else if (other instanceof LambdaArgumentTypePlaceholder) {
         return this.getTypeDeclaration().hasAnnotation(FunctionalInterface.class.getCanonicalName());
      } else if (other instanceof ReferenceTypeImpl) {
         ReferenceTypeImpl otherRef = (ReferenceTypeImpl)other;
         if (this.compareConsideringTypeParameters(otherRef)) {
            return true;
         } else {
            Iterator var7 = otherRef.getAllAncestors().iterator();

            ResolvedReferenceType otherAncestor;
            do {
               if (!var7.hasNext()) {
                  return false;
               }

               otherAncestor = (ResolvedReferenceType)var7.next();
            } while(!this.compareConsideringTypeParameters(otherAncestor));

            return true;
         }
      } else if (other.isTypeVariable()) {
         Iterator var2 = other.asTypeVariable().asTypeParameter().getBounds().iterator();

         ResolvedTypeParameterDeclaration.Bound bound;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            bound = (ResolvedTypeParameterDeclaration.Bound)var2.next();
         } while(!bound.isExtends() || !this.isAssignableBy(bound.getType()));

         return true;
      } else if (other.isConstraint()) {
         return this.isAssignableBy(other.asConstraintType().getBound());
      } else if (other.isWildcard()) {
         if (this.getQualifiedName().equals(Object.class.getCanonicalName())) {
            return true;
         } else {
            return other.asWildcard().isExtends() ? this.isAssignableBy(other.asWildcard().getBoundedType()) : false;
         }
      } else {
         return false;
      }
   }

   public Set<MethodUsage> getDeclaredMethods() {
      Set<MethodUsage> methods = new HashSet();
      Iterator var2 = this.getTypeDeclaration().getDeclaredMethods().iterator();

      while(var2.hasNext()) {
         ResolvedMethodDeclaration methodDeclaration = (ResolvedMethodDeclaration)var2.next();
         MethodUsage methodUsage = new MethodUsage(methodDeclaration);
         methods.add(methodUsage);
      }

      return methods;
   }

   public ResolvedType toRawType() {
      return this.isRawType() ? this : new ReferenceTypeImpl(this.typeDeclaration, this.typeSolver);
   }

   public boolean mention(List<ResolvedTypeParameterDeclaration> typeParameters) {
      return this.typeParametersValues().stream().anyMatch((tp) -> {
         return tp.mention(typeParameters);
      });
   }

   public ResolvedType transformTypeParameters(ResolvedTypeTransformer transformer) {
      ResolvedType result = this;
      int i = 0;

      for(Iterator var4 = this.typeParametersValues().iterator(); var4.hasNext(); ++i) {
         ResolvedType tp = (ResolvedType)var4.next();
         ResolvedType transformedTp = transformer.transform(tp);
         if (transformedTp != tp) {
            List<ResolvedType> typeParametersCorrected = ((ResolvedType)result).asReferenceType().typeParametersValues();
            typeParametersCorrected.set(i, transformedTp);
            result = this.create(this.typeDeclaration, typeParametersCorrected);
         }
      }

      return (ResolvedType)result;
   }

   public List<ResolvedReferenceType> getAllAncestors() {
      List<ResolvedReferenceType> ancestors = this.typeDeclaration.getAllAncestors();
      ancestors = (List)ancestors.stream().map((a) -> {
         return this.typeParametersMap().replaceAll(a).asReferenceType();
      }).collect(Collectors.toList());
      ancestors.removeIf((a) -> {
         return a.getQualifiedName().equals(Object.class.getCanonicalName());
      });
      ResolvedReferenceTypeDeclaration objectType = this.typeSolver.solveType(Object.class.getCanonicalName());
      ResolvedReferenceType objectRef = this.create(objectType);
      ancestors.add(objectRef);
      return ancestors;
   }

   public List<ResolvedReferenceType> getDirectAncestors() {
      List<ResolvedReferenceType> ancestors = this.typeDeclaration.getAncestors();
      ancestors = (List)ancestors.stream().map((a) -> {
         return this.typeParametersMap().replaceAll(a).asReferenceType();
      }).collect(Collectors.toList());
      ancestors.removeIf((a) -> {
         return a.getQualifiedName().equals(Object.class.getCanonicalName());
      });
      boolean isClassWithSuperClassOrObject = this.getTypeDeclaration().isClass() && (this.getTypeDeclaration().asClass().getSuperClass() == null || !this.getTypeDeclaration().asClass().getSuperClass().getQualifiedName().equals(Object.class.getCanonicalName()));
      if (!isClassWithSuperClassOrObject) {
         ResolvedReferenceTypeDeclaration objectType = this.typeSolver.solveType(Object.class.getCanonicalName());
         ResolvedReferenceType objectRef = this.create(objectType);
         ancestors.add(objectRef);
      }

      return ancestors;
   }

   public ResolvedReferenceType deriveTypeParameters(ResolvedTypeParametersMap typeParametersMap) {
      return this.create(this.typeDeclaration, typeParametersMap);
   }

   public Set<ResolvedFieldDeclaration> getDeclaredFields() {
      Set<ResolvedFieldDeclaration> res = new HashSet();
      res.addAll(this.getTypeDeclaration().getDeclaredFields());
      return res;
   }
}

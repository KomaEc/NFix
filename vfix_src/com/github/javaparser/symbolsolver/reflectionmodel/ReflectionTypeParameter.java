package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.resolution.declarations.ResolvedMethodLikeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParametrizable;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReflectionTypeParameter implements ResolvedTypeParameterDeclaration {
   private TypeVariable typeVariable;
   private TypeSolver typeSolver;
   private ResolvedTypeParametrizable container;

   public ReflectionTypeParameter(TypeVariable typeVariable, boolean declaredOnClass, TypeSolver typeSolver) {
      GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
      if (genericDeclaration instanceof Class) {
         this.container = ReflectionFactory.typeDeclarationFor((Class)genericDeclaration, typeSolver);
      } else if (genericDeclaration instanceof Method) {
         this.container = new ReflectionMethodDeclaration((Method)genericDeclaration, typeSolver);
      } else if (genericDeclaration instanceof Constructor) {
         this.container = new ReflectionConstructorDeclaration((Constructor)genericDeclaration, typeSolver);
      }

      this.typeVariable = typeVariable;
      this.typeSolver = typeSolver;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof ResolvedTypeParameterDeclaration)) {
         return false;
      } else {
         ResolvedTypeParameterDeclaration that = (ResolvedTypeParameterDeclaration)o;
         if (!this.getQualifiedName().equals(that.getQualifiedName())) {
            return false;
         } else if (this.declaredOnType() != that.declaredOnType()) {
            return false;
         } else {
            return this.declaredOnMethod() == that.declaredOnMethod();
         }
      }
   }

   public int hashCode() {
      int result = this.typeVariable.hashCode();
      result = 31 * result + this.container.hashCode();
      return result;
   }

   public String getName() {
      return this.typeVariable.getName();
   }

   public String getContainerQualifiedName() {
      return this.container instanceof ResolvedReferenceTypeDeclaration ? ((ResolvedReferenceTypeDeclaration)this.container).getQualifiedName() : ((ResolvedMethodLikeDeclaration)this.container).getQualifiedSignature();
   }

   public String getContainerId() {
      return this.container instanceof ResolvedReferenceTypeDeclaration ? ((ResolvedReferenceTypeDeclaration)this.container).getId() : ((ResolvedMethodLikeDeclaration)this.container).getQualifiedSignature();
   }

   public ResolvedTypeParametrizable getContainer() {
      return this.container;
   }

   public List<ResolvedTypeParameterDeclaration.Bound> getBounds() {
      return (List)Arrays.stream(this.typeVariable.getBounds()).map((refB) -> {
         return ResolvedTypeParameterDeclaration.Bound.extendsBound(ReflectionFactory.typeUsageFor(refB, this.typeSolver));
      }).collect(Collectors.toList());
   }

   public String toString() {
      return "ReflectionTypeParameter{typeVariable=" + this.typeVariable + '}';
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      return this.container instanceof ResolvedReferenceTypeDeclaration ? Optional.of((ResolvedReferenceTypeDeclaration)this.container) : Optional.empty();
   }
}

package com.github.javaparser.resolution.types;

import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import java.util.List;
import java.util.Map;

public class ResolvedTypeVariable implements ResolvedType {
   private ResolvedTypeParameterDeclaration typeParameter;

   public ResolvedTypeVariable(ResolvedTypeParameterDeclaration typeParameter) {
      this.typeParameter = typeParameter;
   }

   public String toString() {
      return "TypeVariable {" + this.typeParameter.getQualifiedName() + "}";
   }

   public String qualifiedName() {
      return this.typeParameter.getQualifiedName();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ResolvedTypeVariable that = (ResolvedTypeVariable)o;
         if (!this.typeParameter.getName().equals(that.typeParameter.getName())) {
            return false;
         } else if (this.typeParameter.declaredOnType() != that.typeParameter.declaredOnType()) {
            return false;
         } else {
            return this.typeParameter.declaredOnMethod() == that.typeParameter.declaredOnMethod();
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.typeParameter.hashCode();
   }

   public boolean isArray() {
      return false;
   }

   public boolean isPrimitive() {
      return false;
   }

   public ResolvedType replaceTypeVariables(ResolvedTypeParameterDeclaration tpToBeReplaced, ResolvedType replaced, Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes) {
      if (tpToBeReplaced.getName().equals(this.typeParameter.getName())) {
         inferredTypes.put(this.asTypeParameter(), replaced);
         return replaced;
      } else {
         return this;
      }
   }

   public boolean isReferenceType() {
      return false;
   }

   public String describe() {
      return this.typeParameter.getName();
   }

   public ResolvedTypeParameterDeclaration asTypeParameter() {
      return this.typeParameter;
   }

   public ResolvedTypeVariable asTypeVariable() {
      return this;
   }

   public boolean isTypeVariable() {
      return true;
   }

   public boolean isAssignableBy(ResolvedType other) {
      return other.isTypeVariable() ? this.describe().equals(other.describe()) : true;
   }

   public boolean mention(List<ResolvedTypeParameterDeclaration> typeParameters) {
      return typeParameters.contains(this.typeParameter);
   }
}

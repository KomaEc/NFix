package com.github.javaparser.resolution.types;

import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import java.util.Map;

public class ResolvedArrayType implements ResolvedType {
   private ResolvedType baseType;

   public ResolvedArrayType(ResolvedType baseType) {
      this.baseType = baseType;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ResolvedArrayType that = (ResolvedArrayType)o;
         return this.baseType.equals(that.baseType);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.baseType.hashCode();
   }

   public String toString() {
      return "ResolvedArrayType{" + this.baseType + "}";
   }

   public ResolvedArrayType asArrayType() {
      return this;
   }

   public boolean isArray() {
      return true;
   }

   public String describe() {
      return this.baseType.describe() + "[]";
   }

   public ResolvedType getComponentType() {
      return this.baseType;
   }

   public boolean isAssignableBy(ResolvedType other) {
      if (other.isArray()) {
         return this.baseType.isPrimitive() && other.asArrayType().getComponentType().isPrimitive() ? this.baseType.equals(other.asArrayType().getComponentType()) : this.baseType.isAssignableBy(other.asArrayType().getComponentType());
      } else {
         return other.isNull();
      }
   }

   public ResolvedType replaceTypeVariables(ResolvedTypeParameterDeclaration tpToReplace, ResolvedType replaced, Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes) {
      ResolvedType baseTypeReplaced = this.baseType.replaceTypeVariables(tpToReplace, replaced, inferredTypes);
      return baseTypeReplaced == this.baseType ? this : new ResolvedArrayType(baseTypeReplaced);
   }
}

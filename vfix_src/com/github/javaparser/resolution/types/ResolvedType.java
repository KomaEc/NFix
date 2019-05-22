package com.github.javaparser.resolution.types;

import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ResolvedType {
   default boolean isArray() {
      return false;
   }

   default int arrayLevel() {
      return this.isArray() ? 1 + this.asArrayType().getComponentType().arrayLevel() : 0;
   }

   default boolean isPrimitive() {
      return false;
   }

   default boolean isNull() {
      return false;
   }

   default boolean isUnionType() {
      return false;
   }

   default boolean isReference() {
      return this.isReferenceType() || this.isArray() || this.isTypeVariable() || this.isNull() || this.isWildcard() || this.isUnionType();
   }

   default boolean isConstraint() {
      return false;
   }

   default boolean isReferenceType() {
      return false;
   }

   default boolean isVoid() {
      return false;
   }

   default boolean isTypeVariable() {
      return false;
   }

   default boolean isWildcard() {
      return false;
   }

   default ResolvedArrayType asArrayType() {
      throw new UnsupportedOperationException(String.format("%s is not an Array", this));
   }

   default ResolvedReferenceType asReferenceType() {
      throw new UnsupportedOperationException(String.format("%s is not a Reference Type", this));
   }

   default ResolvedTypeParameterDeclaration asTypeParameter() {
      throw new UnsupportedOperationException(String.format("%s is not a Type parameter", this));
   }

   default ResolvedTypeVariable asTypeVariable() {
      throw new UnsupportedOperationException(String.format("%s is not a Type variable", this));
   }

   default ResolvedPrimitiveType asPrimitive() {
      throw new UnsupportedOperationException(String.format("%s is not a Primitive type", this));
   }

   default ResolvedWildcard asWildcard() {
      throw new UnsupportedOperationException(String.format("%s is not a Wildcard", this));
   }

   default ResolvedLambdaConstraintType asConstraintType() {
      throw new UnsupportedOperationException(String.format("%s is not a constraint type", this));
   }

   default ResolvedUnionType asUnionType() {
      throw new UnsupportedOperationException(String.format("%s is not a union type", this));
   }

   String describe();

   default ResolvedType replaceTypeVariables(ResolvedTypeParameterDeclaration tp, ResolvedType replaced, Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes) {
      return this;
   }

   default ResolvedType replaceTypeVariables(ResolvedTypeParameterDeclaration tp, ResolvedType replaced) {
      return this.replaceTypeVariables(tp, replaced, new HashMap());
   }

   default boolean mention(List<ResolvedTypeParameterDeclaration> typeParameters) {
      throw new UnsupportedOperationException(this.getClass().getCanonicalName());
   }

   boolean isAssignableBy(ResolvedType other);
}

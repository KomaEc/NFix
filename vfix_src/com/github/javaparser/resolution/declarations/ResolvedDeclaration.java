package com.github.javaparser.resolution.declarations;

public interface ResolvedDeclaration {
   default boolean hasName() {
      return true;
   }

   String getName();

   default boolean isField() {
      return false;
   }

   default boolean isVariable() {
      return false;
   }

   default boolean isEnumConstant() {
      return false;
   }

   default boolean isParameter() {
      return false;
   }

   default boolean isType() {
      return false;
   }

   default boolean isMethod() {
      return false;
   }

   default ResolvedFieldDeclaration asField() {
      throw new UnsupportedOperationException(String.format("%s is not a FieldDeclaration", this));
   }

   default ResolvedParameterDeclaration asParameter() {
      throw new UnsupportedOperationException(String.format("%s is not a ParameterDeclaration", this));
   }

   default ResolvedTypeDeclaration asType() {
      throw new UnsupportedOperationException(String.format("%s is not a TypeDeclaration", this));
   }

   default ResolvedMethodDeclaration asMethod() {
      throw new UnsupportedOperationException(String.format("%s is not a MethodDeclaration", this));
   }

   default ResolvedEnumConstantDeclaration asEnumConstant() {
      throw new UnsupportedOperationException(String.format("%s is not an EnumConstantDeclaration", this));
   }
}

package com.github.javaparser.resolution.declarations;

public interface ResolvedFieldDeclaration extends ResolvedValueDeclaration, HasAccessSpecifier {
   boolean isStatic();

   default boolean isField() {
      return true;
   }

   default ResolvedFieldDeclaration asField() {
      return this;
   }

   ResolvedTypeDeclaration declaringType();
}

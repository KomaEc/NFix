package com.github.javaparser.resolution.declarations;

public interface ResolvedEnumConstantDeclaration extends ResolvedValueDeclaration {
   String getName();

   default boolean isEnumConstant() {
      return true;
   }

   default ResolvedEnumConstantDeclaration asEnumConstant() {
      return this;
   }
}

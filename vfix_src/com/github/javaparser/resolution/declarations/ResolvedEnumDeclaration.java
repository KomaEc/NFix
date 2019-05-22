package com.github.javaparser.resolution.declarations;

import java.util.List;

public interface ResolvedEnumDeclaration extends ResolvedReferenceTypeDeclaration, HasAccessSpecifier {
   default boolean isEnum() {
      return true;
   }

   default ResolvedEnumDeclaration asEnum() {
      return this;
   }

   List<ResolvedEnumConstantDeclaration> getEnumConstants();

   default boolean hasEnumConstant(String name) {
      return this.getEnumConstants().stream().anyMatch((c) -> {
         return c.getName().equals(name);
      });
   }

   default ResolvedEnumConstantDeclaration getEnumConstant(final String name) {
      return (ResolvedEnumConstantDeclaration)this.getEnumConstants().stream().filter((c) -> {
         return c.getName().equals(name);
      }).findFirst().orElseThrow(() -> {
         return new IllegalArgumentException("No constant named " + name);
      });
   }
}

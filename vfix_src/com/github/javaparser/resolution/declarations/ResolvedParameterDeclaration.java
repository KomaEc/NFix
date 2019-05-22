package com.github.javaparser.resolution.declarations;

public interface ResolvedParameterDeclaration extends ResolvedValueDeclaration {
   default boolean isParameter() {
      return true;
   }

   default boolean hasName() {
      return true;
   }

   default ResolvedParameterDeclaration asParameter() {
      return this;
   }

   boolean isVariadic();

   default String describeType() {
      return this.isVariadic() ? this.getType().asArrayType().getComponentType().describe() + "..." : this.getType().describe();
   }
}

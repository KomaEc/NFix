package com.github.javaparser.resolution.types;

public class ResolvedVoidType implements ResolvedType {
   public static final ResolvedType INSTANCE = new ResolvedVoidType();

   private ResolvedVoidType() {
   }

   public String describe() {
      return "void";
   }

   public boolean isAssignableBy(ResolvedType other) {
      throw new UnsupportedOperationException();
   }

   public boolean isVoid() {
      return true;
   }
}

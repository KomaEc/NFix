package com.github.javaparser.symbolsolver.model.typesystem;

import com.github.javaparser.resolution.types.ResolvedType;

public class NullType implements ResolvedType {
   public static final NullType INSTANCE = new NullType();

   private NullType() {
   }

   public boolean isArray() {
      return false;
   }

   public boolean isPrimitive() {
      return false;
   }

   public boolean isNull() {
      return true;
   }

   public boolean isReferenceType() {
      return false;
   }

   public String describe() {
      return "null";
   }

   public boolean isTypeVariable() {
      return false;
   }

   public boolean isAssignableBy(ResolvedType other) {
      throw new UnsupportedOperationException("It does not make sense to assign a value to null, it can only be assigned");
   }
}

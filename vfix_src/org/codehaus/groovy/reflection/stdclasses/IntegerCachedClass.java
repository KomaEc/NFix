package org.codehaus.groovy.reflection.stdclasses;

import java.math.BigInteger;
import org.codehaus.groovy.reflection.ClassInfo;

public class IntegerCachedClass extends NumberCachedClass {
   private boolean allowNull;

   public IntegerCachedClass(Class klazz, ClassInfo classInfo, boolean allowNull) {
      super(klazz, classInfo);
      this.allowNull = allowNull;
   }

   public Object coerceArgument(Object argument) {
      if (argument instanceof Integer) {
         return argument;
      } else {
         return argument instanceof Number ? ((Number)argument).intValue() : argument;
      }
   }

   public boolean isDirectlyAssignable(Object argument) {
      return this.allowNull && argument == null || argument instanceof Integer;
   }

   public boolean isAssignableFrom(Class classToTransformFrom) {
      return this.allowNull && classToTransformFrom == null || classToTransformFrom == Integer.class || classToTransformFrom == Short.class || classToTransformFrom == Byte.class || classToTransformFrom == BigInteger.class || classToTransformFrom == Integer.TYPE || classToTransformFrom == Short.TYPE || classToTransformFrom == Byte.TYPE;
   }
}

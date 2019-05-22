package org.codehaus.groovy.reflection.stdclasses;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.groovy.reflection.ClassInfo;

public class FloatCachedClass extends NumberCachedClass {
   private boolean allowNull;

   public FloatCachedClass(Class klazz, ClassInfo classInfo, boolean allowNull) {
      super(klazz, classInfo);
      this.allowNull = allowNull;
   }

   public Object coerceArgument(Object argument) {
      if (argument instanceof Float) {
         return argument;
      } else if (argument instanceof Number) {
         Float res = ((Number)argument).floatValue();
         if (argument instanceof BigDecimal && res.isInfinite()) {
            throw new IllegalArgumentException(Float.class + " out of range while converting from BigDecimal");
         } else {
            return res;
         }
      } else {
         return argument;
      }
   }

   public boolean isDirectlyAssignable(Object argument) {
      return this.allowNull && argument == null || argument instanceof Float;
   }

   public boolean isAssignableFrom(Class classToTransformFrom) {
      return this.allowNull && classToTransformFrom == null || classToTransformFrom == Float.class || classToTransformFrom == Integer.class || classToTransformFrom == Long.class || classToTransformFrom == Short.class || classToTransformFrom == Byte.class || classToTransformFrom == Float.TYPE || classToTransformFrom == Integer.TYPE || classToTransformFrom == Long.TYPE || classToTransformFrom == Short.TYPE || classToTransformFrom == Byte.TYPE || classToTransformFrom == BigDecimal.class || classToTransformFrom == BigInteger.class;
   }
}

package org.codehaus.groovy.reflection.stdclasses;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.groovy.reflection.ClassInfo;

public class DoubleCachedClass extends NumberCachedClass {
   private boolean allowNull;

   public DoubleCachedClass(Class klazz, ClassInfo classInfo, boolean allowNull) {
      super(klazz, classInfo);
      this.allowNull = allowNull;
   }

   public boolean isDirectlyAssignable(Object argument) {
      return this.allowNull && argument == null || argument instanceof Double;
   }

   public Object coerceArgument(Object argument) {
      if (argument instanceof Double) {
         return argument;
      } else if (argument instanceof Number) {
         Double res = ((Number)argument).doubleValue();
         if (argument instanceof BigDecimal && res.isInfinite()) {
            throw new IllegalArgumentException(Double.class + " out of range while converting from BigDecimal");
         } else {
            return res;
         }
      } else {
         return argument;
      }
   }

   public boolean isAssignableFrom(Class classToTransformFrom) {
      return this.allowNull && classToTransformFrom == null || classToTransformFrom == Double.class || classToTransformFrom == Integer.class || classToTransformFrom == Long.class || classToTransformFrom == Short.class || classToTransformFrom == Byte.class || classToTransformFrom == Float.class || classToTransformFrom == Double.TYPE || classToTransformFrom == Integer.TYPE || classToTransformFrom == Long.TYPE || classToTransformFrom == Short.TYPE || classToTransformFrom == Byte.TYPE || classToTransformFrom == Float.TYPE || classToTransformFrom == BigDecimal.class || classToTransformFrom == BigInteger.class;
   }
}

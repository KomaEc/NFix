package org.codehaus.groovy.reflection.stdclasses;

import java.math.BigInteger;
import org.codehaus.groovy.reflection.ClassInfo;

public class BigIntegerCachedClass extends NumberCachedClass {
   public BigIntegerCachedClass(Class klazz, ClassInfo classInfo) {
      super(klazz, classInfo);
   }

   public boolean isDirectlyAssignable(Object argument) {
      return argument instanceof BigInteger;
   }

   public boolean isAssignableFrom(Class classToTransformFrom) {
      return classToTransformFrom == null || classToTransformFrom == Integer.class || classToTransformFrom == Short.class || classToTransformFrom == Byte.class || classToTransformFrom == BigInteger.class || classToTransformFrom == Long.class || classToTransformFrom == Integer.TYPE || classToTransformFrom == Short.TYPE || classToTransformFrom == Byte.TYPE || classToTransformFrom == Long.TYPE;
   }
}

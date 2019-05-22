package org.codehaus.groovy.reflection.stdclasses;

import org.codehaus.groovy.reflection.ClassInfo;

public class ByteCachedClass extends NumberCachedClass {
   private boolean allowNull;

   public ByteCachedClass(Class klazz, ClassInfo classInfo, boolean allowNull) {
      super(klazz, classInfo);
      this.allowNull = allowNull;
   }

   public Object coerceArgument(Object argument) {
      if (argument instanceof Byte) {
         return argument;
      } else {
         return argument instanceof Number ? ((Number)argument).byteValue() : argument;
      }
   }

   public boolean isDirectlyAssignable(Object argument) {
      return this.allowNull && argument == null || argument instanceof Byte;
   }

   public boolean isAssignableFrom(Class classToTransformFrom) {
      return this.allowNull && classToTransformFrom == null || classToTransformFrom == Byte.class || classToTransformFrom == Byte.TYPE;
   }
}

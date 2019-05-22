package org.codehaus.groovy.reflection.stdclasses;

import org.codehaus.groovy.reflection.ClassInfo;

public class ShortCachedClass extends NumberCachedClass {
   private boolean allowNull;

   public ShortCachedClass(Class klazz, ClassInfo classInfo, boolean allowNull) {
      super(klazz, classInfo);
      this.allowNull = allowNull;
   }

   public Object coerceArgument(Object argument) {
      if (argument instanceof Short) {
         return argument;
      } else {
         return argument instanceof Number ? ((Number)argument).shortValue() : argument;
      }
   }

   public boolean isDirectlyAssignable(Object argument) {
      return this.allowNull && argument == null || argument instanceof Short;
   }

   public boolean isAssignableFrom(Class classToTransformFrom) {
      return this.allowNull && classToTransformFrom == null || classToTransformFrom == Short.class || classToTransformFrom == Byte.class || classToTransformFrom == Short.TYPE || classToTransformFrom == Byte.TYPE;
   }
}

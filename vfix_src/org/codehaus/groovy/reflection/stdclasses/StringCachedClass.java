package org.codehaus.groovy.reflection.stdclasses;

import groovy.lang.GString;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.reflection.ReflectionCache;

public class StringCachedClass extends CachedClass {
   private static final Class STRING_CLASS = String.class;
   private static final Class GSTRING_CLASS = GString.class;

   public StringCachedClass(ClassInfo classInfo) {
      super(STRING_CLASS, classInfo);
   }

   public boolean isDirectlyAssignable(Object argument) {
      return argument instanceof String;
   }

   public boolean isAssignableFrom(Class classToTransformFrom) {
      return classToTransformFrom == null || classToTransformFrom == STRING_CLASS || ReflectionCache.isAssignableFrom(GSTRING_CLASS, classToTransformFrom);
   }

   public Object coerceArgument(Object argument) {
      return argument instanceof GString ? argument.toString() : argument;
   }
}

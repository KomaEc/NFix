package org.codehaus.groovy.reflection.stdclasses;

import java.math.BigDecimal;
import org.codehaus.groovy.reflection.ClassInfo;

public class BigDecimalCachedClass extends DoubleCachedClass {
   public BigDecimalCachedClass(Class klazz, ClassInfo classInfo) {
      super(klazz, classInfo, true);
   }

   public boolean isDirectlyAssignable(Object argument) {
      return argument instanceof BigDecimal;
   }

   public Object coerceArgument(Object argument) {
      if (argument instanceof BigDecimal) {
         return argument;
      } else {
         return argument instanceof Number ? new BigDecimal(((Number)argument).doubleValue()) : argument;
      }
   }
}

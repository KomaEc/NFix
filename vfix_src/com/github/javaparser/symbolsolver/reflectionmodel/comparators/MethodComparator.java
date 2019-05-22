package com.github.javaparser.symbolsolver.reflectionmodel.comparators;

import java.lang.reflect.Method;
import java.util.Comparator;

public class MethodComparator implements Comparator<Method> {
   public int compare(Method o1, Method o2) {
      int compareName = o1.getName().compareTo(o2.getName());
      if (compareName != 0) {
         return compareName;
      } else {
         int compareNParams = o1.getParameterCount() - o2.getParameterCount();
         if (compareNParams != 0) {
            return compareNParams;
         } else {
            int compareResult;
            for(compareResult = 0; compareResult < o1.getParameterCount(); ++compareResult) {
               int compareParam = (new ParameterComparator()).compare(o1.getParameters()[compareResult], o2.getParameters()[compareResult]);
               if (compareParam != 0) {
                  return compareParam;
               }
            }

            compareResult = (new ClassComparator()).compare(o1.getReturnType(), o2.getReturnType());
            if (compareResult != 0) {
               return compareResult;
            } else {
               return 0;
            }
         }
      }
   }
}

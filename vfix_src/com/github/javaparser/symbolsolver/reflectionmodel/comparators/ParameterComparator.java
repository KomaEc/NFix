package com.github.javaparser.symbolsolver.reflectionmodel.comparators;

import java.lang.reflect.Parameter;
import java.util.Comparator;

public class ParameterComparator implements Comparator<Parameter> {
   public int compare(Parameter o1, Parameter o2) {
      int compareName = o1.getName().compareTo(o2.getName());
      if (compareName != 0) {
         return compareName;
      } else {
         int compareType = (new ClassComparator()).compare(o1.getType(), o2.getType());
         return compareType != 0 ? compareType : 0;
      }
   }
}

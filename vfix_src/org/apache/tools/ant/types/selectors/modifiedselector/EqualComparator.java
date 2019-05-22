package org.apache.tools.ant.types.selectors.modifiedselector;

import java.util.Comparator;

public class EqualComparator implements Comparator {
   public int compare(Object o1, Object o2) {
      if (o1 == null) {
         return o2 == null ? 1 : 0;
      } else {
         return o1.equals(o2) ? 0 : 1;
      }
   }

   public String toString() {
      return "EqualComparator";
   }
}

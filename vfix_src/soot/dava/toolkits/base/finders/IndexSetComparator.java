package soot.dava.toolkits.base.finders;

import java.util.Comparator;
import java.util.TreeSet;

class IndexSetComparator implements Comparator {
   public int compare(Object o1, Object o2) {
      if (o1 == o2) {
         return 0;
      } else {
         o1 = ((TreeSet)o1).last();
         o2 = ((TreeSet)o2).last();
         if (o1 instanceof String) {
            return 1;
         } else {
            return o2 instanceof String ? -1 : (Integer)o1 - (Integer)o2;
         }
      }
   }

   public boolean equals(Object o) {
      return o instanceof IndexSetComparator;
   }
}

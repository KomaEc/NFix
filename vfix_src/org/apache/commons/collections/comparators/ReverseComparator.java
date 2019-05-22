package org.apache.commons.collections.comparators;

import java.io.Serializable;
import java.util.Comparator;

public class ReverseComparator implements Comparator, Serializable {
   private static final long serialVersionUID = 2858887242028539265L;
   private Comparator comparator;

   public ReverseComparator() {
      this((Comparator)null);
   }

   public ReverseComparator(Comparator comparator) {
      if (comparator != null) {
         this.comparator = comparator;
      } else {
         this.comparator = ComparableComparator.getInstance();
      }

   }

   public int compare(Object obj1, Object obj2) {
      return this.comparator.compare(obj2, obj1);
   }

   public int hashCode() {
      return "ReverseComparator".hashCode() ^ this.comparator.hashCode();
   }

   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (null == object) {
         return false;
      } else if (object.getClass().equals(this.getClass())) {
         ReverseComparator thatrc = (ReverseComparator)object;
         return this.comparator.equals(thatrc.comparator);
      } else {
         return false;
      }
   }
}

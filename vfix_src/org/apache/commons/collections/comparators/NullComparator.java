package org.apache.commons.collections.comparators;

import java.io.Serializable;
import java.util.Comparator;

public class NullComparator implements Comparator, Serializable {
   private static final long serialVersionUID = -5820772575483504339L;
   private Comparator nonNullComparator;
   private boolean nullsAreHigh;

   public NullComparator() {
      this(ComparableComparator.getInstance(), true);
   }

   public NullComparator(Comparator nonNullComparator) {
      this(nonNullComparator, true);
   }

   public NullComparator(boolean nullsAreHigh) {
      this(ComparableComparator.getInstance(), nullsAreHigh);
   }

   public NullComparator(Comparator nonNullComparator, boolean nullsAreHigh) {
      this.nonNullComparator = nonNullComparator;
      this.nullsAreHigh = nullsAreHigh;
      if (nonNullComparator == null) {
         throw new NullPointerException("null nonNullComparator");
      }
   }

   public int compare(Object o1, Object o2) {
      if (o1 == o2) {
         return 0;
      } else if (o1 == null) {
         return this.nullsAreHigh ? 1 : -1;
      } else if (o2 == null) {
         return this.nullsAreHigh ? -1 : 1;
      } else {
         return this.nonNullComparator.compare(o1, o2);
      }
   }

   public int hashCode() {
      return (this.nullsAreHigh ? -1 : 1) * this.nonNullComparator.hashCode();
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (obj == this) {
         return true;
      } else if (!obj.getClass().equals(this.getClass())) {
         return false;
      } else {
         NullComparator other = (NullComparator)obj;
         return this.nullsAreHigh == other.nullsAreHigh && this.nonNullComparator.equals(other.nonNullComparator);
      }
   }
}

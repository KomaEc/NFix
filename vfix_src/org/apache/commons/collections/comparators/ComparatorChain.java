package org.apache.commons.collections.comparators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ComparatorChain implements Comparator, Serializable {
   private static final long serialVersionUID = -721644942746081630L;
   protected List comparatorChain;
   protected BitSet orderingBits;
   protected boolean isLocked;

   public ComparatorChain() {
      this(new ArrayList(), new BitSet());
   }

   public ComparatorChain(Comparator comparator) {
      this(comparator, false);
   }

   public ComparatorChain(Comparator comparator, boolean reverse) {
      this.comparatorChain = null;
      this.orderingBits = null;
      this.isLocked = false;
      this.comparatorChain = new ArrayList();
      this.comparatorChain.add(comparator);
      this.orderingBits = new BitSet(1);
      if (reverse) {
         this.orderingBits.set(0);
      }

   }

   public ComparatorChain(List list) {
      this(list, new BitSet(list.size()));
   }

   public ComparatorChain(List list, BitSet bits) {
      this.comparatorChain = null;
      this.orderingBits = null;
      this.isLocked = false;
      this.comparatorChain = list;
      this.orderingBits = bits;
   }

   public void addComparator(Comparator comparator) {
      this.addComparator(comparator, false);
   }

   public void addComparator(Comparator comparator, boolean reverse) {
      this.checkLocked();
      this.comparatorChain.add(comparator);
      if (reverse) {
         this.orderingBits.set(this.comparatorChain.size() - 1);
      }

   }

   public void setComparator(int index, Comparator comparator) throws IndexOutOfBoundsException {
      this.setComparator(index, comparator, false);
   }

   public void setComparator(int index, Comparator comparator, boolean reverse) {
      this.checkLocked();
      this.comparatorChain.set(index, comparator);
      if (reverse) {
         this.orderingBits.set(index);
      } else {
         this.orderingBits.clear(index);
      }

   }

   public void setForwardSort(int index) {
      this.checkLocked();
      this.orderingBits.clear(index);
   }

   public void setReverseSort(int index) {
      this.checkLocked();
      this.orderingBits.set(index);
   }

   public int size() {
      return this.comparatorChain.size();
   }

   public boolean isLocked() {
      return this.isLocked;
   }

   private void checkLocked() {
      if (this.isLocked) {
         throw new UnsupportedOperationException("Comparator ordering cannot be changed after the first comparison is performed");
      }
   }

   private void checkChainIntegrity() {
      if (this.comparatorChain.size() == 0) {
         throw new UnsupportedOperationException("ComparatorChains must contain at least one Comparator");
      }
   }

   public int compare(Object o1, Object o2) throws UnsupportedOperationException {
      if (!this.isLocked) {
         this.checkChainIntegrity();
         this.isLocked = true;
      }

      Iterator comparators = this.comparatorChain.iterator();

      for(int comparatorIndex = 0; comparators.hasNext(); ++comparatorIndex) {
         Comparator comparator = (Comparator)comparators.next();
         int retval = comparator.compare(o1, o2);
         if (retval != 0) {
            if (this.orderingBits.get(comparatorIndex)) {
               if (Integer.MIN_VALUE == retval) {
                  retval = Integer.MAX_VALUE;
               } else {
                  retval *= -1;
               }
            }

            return retval;
         }
      }

      return 0;
   }

   public int hashCode() {
      int hash = 0;
      if (null != this.comparatorChain) {
         hash ^= this.comparatorChain.hashCode();
      }

      if (null != this.orderingBits) {
         hash ^= this.orderingBits.hashCode();
      }

      return hash;
   }

   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (null == object) {
         return false;
      } else if (object.getClass().equals(this.getClass())) {
         ComparatorChain chain = (ComparatorChain)object;
         return (null == this.orderingBits ? null == chain.orderingBits : this.orderingBits.equals(chain.orderingBits)) && (null == this.comparatorChain ? null == chain.comparatorChain : this.comparatorChain.equals(chain.comparatorChain));
      } else {
         return false;
      }
   }
}

package org.apache.commons.collections.bag;

import java.util.Comparator;
import org.apache.commons.collections.Bag;
import org.apache.commons.collections.SortedBag;

public class SynchronizedSortedBag extends SynchronizedBag implements SortedBag {
   private static final long serialVersionUID = 722374056718497858L;

   public static SortedBag decorate(SortedBag bag) {
      return new SynchronizedSortedBag(bag);
   }

   protected SynchronizedSortedBag(SortedBag bag) {
      super(bag);
   }

   protected SynchronizedSortedBag(Bag bag, Object lock) {
      super(bag, lock);
   }

   protected SortedBag getSortedBag() {
      return (SortedBag)super.collection;
   }

   public synchronized Object first() {
      Object var1 = super.lock;
      synchronized(var1) {
         Object var2 = this.getSortedBag().first();
         return var2;
      }
   }

   public synchronized Object last() {
      Object var1 = super.lock;
      synchronized(var1) {
         Object var2 = this.getSortedBag().last();
         return var2;
      }
   }

   public synchronized Comparator comparator() {
      Object var1 = super.lock;
      synchronized(var1) {
         Comparator var2 = this.getSortedBag().comparator();
         return var2;
      }
   }
}

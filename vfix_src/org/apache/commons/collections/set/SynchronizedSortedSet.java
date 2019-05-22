package org.apache.commons.collections.set;

import java.util.Comparator;
import java.util.SortedSet;
import org.apache.commons.collections.collection.SynchronizedCollection;

public class SynchronizedSortedSet extends SynchronizedCollection implements SortedSet {
   private static final long serialVersionUID = 2775582861954500111L;

   public static SortedSet decorate(SortedSet set) {
      return new SynchronizedSortedSet(set);
   }

   protected SynchronizedSortedSet(SortedSet set) {
      super(set);
   }

   protected SynchronizedSortedSet(SortedSet set, Object lock) {
      super(set, lock);
   }

   protected SortedSet getSortedSet() {
      return (SortedSet)super.collection;
   }

   public SortedSet subSet(Object fromElement, Object toElement) {
      Object var3 = super.lock;
      synchronized(var3) {
         SortedSet set = this.getSortedSet().subSet(fromElement, toElement);
         SynchronizedSortedSet var5 = new SynchronizedSortedSet(set, super.lock);
         return var5;
      }
   }

   public SortedSet headSet(Object toElement) {
      Object var2 = super.lock;
      synchronized(var2) {
         SortedSet set = this.getSortedSet().headSet(toElement);
         SynchronizedSortedSet var4 = new SynchronizedSortedSet(set, super.lock);
         return var4;
      }
   }

   public SortedSet tailSet(Object fromElement) {
      Object var2 = super.lock;
      synchronized(var2) {
         SortedSet set = this.getSortedSet().tailSet(fromElement);
         SynchronizedSortedSet var4 = new SynchronizedSortedSet(set, super.lock);
         return var4;
      }
   }

   public Object first() {
      Object var1 = super.lock;
      synchronized(var1) {
         Object var2 = this.getSortedSet().first();
         return var2;
      }
   }

   public Object last() {
      Object var1 = super.lock;
      synchronized(var1) {
         Object var2 = this.getSortedSet().last();
         return var2;
      }
   }

   public Comparator comparator() {
      Object var1 = super.lock;
      synchronized(var1) {
         Comparator var2 = this.getSortedSet().comparator();
         return var2;
      }
   }
}

package soot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class SmallPriorityQueue<E> extends PriorityQueue<E> {
   static final int MAX_CAPACITY = 64;
   private long queue = 0L;

   void addAll() {
      if (this.N != 0) {
         this.queue = -1L >>> -this.N;
         this.min = 0;
      }
   }

   SmallPriorityQueue(List<? extends E> universe, Map<E, Integer> ordinalMap) {
      super(universe, ordinalMap);

      assert universe.size() <= 64;

   }

   public void clear() {
      this.queue = 0L;
      this.min = Integer.MAX_VALUE;
   }

   public Iterator<E> iterator() {
      return new PriorityQueue<E>.Itr() {
         long getExpected() {
            return SmallPriorityQueue.this.queue;
         }
      };
   }

   public int size() {
      return Long.bitCount(this.queue);
   }

   int nextSetBit(int fromIndex) {
      assert fromIndex >= 0;

      if (fromIndex > this.N) {
         return fromIndex;
      } else {
         long m0 = -1L << fromIndex;
         long t0 = this.queue & m0;
         return (t0 & -m0) != 0L ? fromIndex : Long.numberOfTrailingZeros(t0);
      }
   }

   boolean add(int ordinal) {
      long old = this.queue;
      this.queue |= 1L << ordinal;
      if (old == this.queue) {
         return false;
      } else {
         this.min = Math.min(this.min, ordinal);
         return true;
      }
   }

   boolean contains(int ordinal) {
      assert ordinal >= 0;

      assert ordinal < this.N;

      return (this.queue >>> ordinal & 1L) == 1L;
   }

   boolean remove(int index) {
      assert index >= 0;

      assert index < this.N;

      long old = this.queue;
      this.queue &= ~(1L << index);
      if (old == this.queue) {
         return false;
      } else {
         if (this.min == index) {
            this.min = this.nextSetBit(this.min + 1);
         }

         return true;
      }
   }

   public boolean removeAll(Collection<?> c) {
      long mask = 0L;

      Object o;
      for(Iterator var4 = c.iterator(); var4.hasNext(); mask |= 1L << this.getOrdinal(o)) {
         o = var4.next();
      }

      long old = this.queue;
      this.queue &= ~mask;
      this.min = this.nextSetBit(this.min);
      return old != this.queue;
   }

   public boolean retainAll(Collection<?> c) {
      long mask = 0L;

      Object o;
      for(Iterator var4 = c.iterator(); var4.hasNext(); mask |= 1L << this.getOrdinal(o)) {
         o = var4.next();
      }

      long old = this.queue;
      this.queue &= mask;
      this.min = this.nextSetBit(this.min);
      return old != this.queue;
   }

   public boolean containsAll(Collection<?> c) {
      long mask = 0L;

      Object o;
      for(Iterator var4 = c.iterator(); var4.hasNext(); mask |= 1L << this.getOrdinal(o)) {
         o = var4.next();
      }

      return (mask & ~this.queue) == 0L;
   }

   public boolean addAll(Collection<? extends E> c) {
      long mask = 0L;

      Object o;
      for(Iterator var4 = c.iterator(); var4.hasNext(); mask |= 1L << this.getOrdinal(o)) {
         o = var4.next();
      }

      long old = this.queue;
      this.queue |= mask;
      if (old == this.queue) {
         return false;
      } else {
         this.min = this.nextSetBit(0);
         return true;
      }
   }
}

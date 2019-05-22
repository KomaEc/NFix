package soot.util;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class LargePriorityQueue<E> extends PriorityQueue<E> {
   BitSet queue;
   private long modCount = 0L;

   LargePriorityQueue(List<? extends E> universe, Map<E, Integer> ordinalMap) {
      super(universe, ordinalMap);
      this.queue = new BitSet(this.N);
   }

   boolean add(int ordinal) {
      if (this.contains(ordinal)) {
         return false;
      } else {
         this.queue.set(ordinal);
         this.min = Math.min(this.min, ordinal);
         ++this.modCount;
         return true;
      }
   }

   void addAll() {
      this.queue.set(0, this.N);
      this.min = 0;
      ++this.modCount;
   }

   int nextSetBit(int fromIndex) {
      int i = this.queue.nextSetBit(fromIndex);
      return i < 0 ? Integer.MAX_VALUE : i;
   }

   boolean remove(int ordinal) {
      if (!this.contains(ordinal)) {
         return false;
      } else {
         this.queue.clear(ordinal);
         if (this.min == ordinal) {
            this.min = this.nextSetBit(this.min + 1);
         }

         ++this.modCount;
         return true;
      }
   }

   boolean contains(int ordinal) {
      return this.queue.get(ordinal);
   }

   public Iterator<E> iterator() {
      return new PriorityQueue<E>.Itr() {
         long getExpected() {
            return LargePriorityQueue.this.modCount;
         }
      };
   }

   public int size() {
      return this.queue.cardinality();
   }
}

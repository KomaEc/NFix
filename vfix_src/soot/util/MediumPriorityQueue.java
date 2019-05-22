package soot.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class MediumPriorityQueue<E> extends PriorityQueue<E> {
   static final int MAX_CAPACITY = 4096;
   private int size = 0;
   private long modCount = 0L;
   private long[] data;
   private long lookup = 0L;

   void addAll() {
      this.size = this.N;
      Arrays.fill(this.data, -1L);
      this.data[this.data.length - 1] = -1L >>> -this.size;
      this.lookup = -1L >>> -this.data.length;
      this.min = 0;
      ++this.modCount;
   }

   MediumPriorityQueue(List<? extends E> universe, Map<E, Integer> ordinalMap) {
      super(universe, ordinalMap);
      this.data = new long[this.N + 64 - 1 >>> 6];

      assert this.N > 64;

      assert this.N <= 4096;

   }

   public void clear() {
      this.size = 0;
      Arrays.fill(this.data, 0L);
      this.lookup = 0L;
      this.min = Integer.MAX_VALUE;
      ++this.modCount;
   }

   int nextSetBit(int fromIndex) {
      assert fromIndex >= 0;

      for(int bb = fromIndex >>> 6; fromIndex < this.N; fromIndex = bb << 6) {
         long m1 = -1L << fromIndex;
         long t1 = this.data[bb] & m1;
         if ((t1 & -m1) != 0L) {
            return fromIndex;
         }

         if (t1 != 0L) {
            return (bb << 6) + Long.numberOfTrailingZeros(t1);
         }

         ++bb;
         long m0 = -1L << bb;
         long t0 = this.lookup & m0;
         if ((t0 & -m0) == 0L) {
            bb = Long.numberOfTrailingZeros(t0);
         }
      }

      return fromIndex;
   }

   boolean add(int ordinal) {
      int bucket = ordinal >>> 6;
      long prv = this.data[bucket];
      long now = prv | 1L << ordinal;
      if (prv == now) {
         return false;
      } else {
         this.data[bucket] = now;
         this.lookup |= 1L << bucket;
         ++this.size;
         ++this.modCount;
         this.min = Math.min(this.min, ordinal);
         return true;
      }
   }

   boolean contains(int ordinal) {
      assert ordinal >= 0;

      assert ordinal < this.N;

      return (this.data[ordinal >>> 6] >>> ordinal & 1L) == 1L;
   }

   boolean remove(int index) {
      assert index >= 0;

      assert index < this.N;

      int bucket = index >>> 6;
      long old = this.data[bucket];
      long now = old & ~(1L << index);
      if (old == now) {
         return false;
      } else {
         if (0L == now) {
            this.lookup &= ~(1L << bucket);
         }

         --this.size;
         ++this.modCount;
         this.data[bucket] = now;
         if (this.min == index) {
            this.min = this.nextSetBit(this.min + 1);
         }

         return true;
      }
   }

   public Iterator<E> iterator() {
      return new PriorityQueue<E>.Itr() {
         long getExpected() {
            return MediumPriorityQueue.this.modCount;
         }
      };
   }

   public int size() {
      return this.size;
   }
}

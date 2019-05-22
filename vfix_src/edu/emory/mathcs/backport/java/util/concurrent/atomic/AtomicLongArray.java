package edu.emory.mathcs.backport.java.util.concurrent.atomic;

import java.io.Serializable;

public class AtomicLongArray implements Serializable {
   private static final long serialVersionUID = -2308431214976778248L;
   private final long[] array;

   public AtomicLongArray(int length) {
      this.array = new long[length];
   }

   public AtomicLongArray(long[] array) {
      if (array == null) {
         throw new NullPointerException();
      } else {
         int length = array.length;
         this.array = new long[length];
         System.arraycopy(array, 0, this.array, 0, array.length);
      }
   }

   public final int length() {
      return this.array.length;
   }

   public final synchronized long get(int i) {
      return this.array[i];
   }

   public final synchronized void set(int i, long newValue) {
      this.array[i] = newValue;
   }

   public final synchronized void lazySet(int i, long newValue) {
      this.array[i] = newValue;
   }

   public final synchronized long getAndSet(int i, long newValue) {
      long old = this.array[i];
      this.array[i] = newValue;
      return old;
   }

   public final synchronized boolean compareAndSet(int i, long expect, long update) {
      if (this.array[i] == expect) {
         this.array[i] = update;
         return true;
      } else {
         return false;
      }
   }

   public final synchronized boolean weakCompareAndSet(int i, long expect, long update) {
      if (this.array[i] == expect) {
         this.array[i] = update;
         return true;
      } else {
         return false;
      }
   }

   public final synchronized long getAndIncrement(int i) {
      return (long)(this.array[i]++);
   }

   public final synchronized long getAndDecrement(int i) {
      return (long)(this.array[i]--);
   }

   public final synchronized long getAndAdd(int i, long delta) {
      long old = this.array[i];
      long[] var10000 = this.array;
      var10000[i] += delta;
      return old;
   }

   public final synchronized long incrementAndGet(int i) {
      return ++this.array[i];
   }

   public final synchronized long decrementAndGet(int i) {
      return --this.array[i];
   }

   public synchronized long addAndGet(int i, long delta) {
      long[] var10000 = this.array;
      return var10000[i] += delta;
   }

   public synchronized String toString() {
      if (this.array.length == 0) {
         return "[]";
      } else {
         StringBuffer buf = new StringBuffer();
         buf.append('[');
         buf.append(this.array[0]);

         for(int i = 1; i < this.array.length; ++i) {
            buf.append(", ");
            buf.append(this.array[i]);
         }

         buf.append("]");
         return buf.toString();
      }
   }
}

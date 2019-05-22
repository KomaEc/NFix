package edu.emory.mathcs.backport.java.util.concurrent.atomic;

import java.io.Serializable;

public class AtomicIntegerArray implements Serializable {
   private static final long serialVersionUID = 2862133569453604235L;
   private final int[] array;

   public AtomicIntegerArray(int length) {
      this.array = new int[length];
   }

   public AtomicIntegerArray(int[] array) {
      if (array == null) {
         throw new NullPointerException();
      } else {
         int length = array.length;
         this.array = new int[length];
         System.arraycopy(array, 0, this.array, 0, array.length);
      }
   }

   public final int length() {
      return this.array.length;
   }

   public final synchronized int get(int i) {
      return this.array[i];
   }

   public final synchronized void set(int i, int newValue) {
      this.array[i] = newValue;
   }

   public final synchronized void lazySet(int i, int newValue) {
      this.array[i] = newValue;
   }

   public final synchronized int getAndSet(int i, int newValue) {
      int old = this.array[i];
      this.array[i] = newValue;
      return old;
   }

   public final synchronized boolean compareAndSet(int i, int expect, int update) {
      if (this.array[i] == expect) {
         this.array[i] = update;
         return true;
      } else {
         return false;
      }
   }

   public final synchronized boolean weakCompareAndSet(int i, int expect, int update) {
      if (this.array[i] == expect) {
         this.array[i] = update;
         return true;
      } else {
         return false;
      }
   }

   public final synchronized int getAndIncrement(int i) {
      return this.array[i]++;
   }

   public final synchronized int getAndDecrement(int i) {
      return this.array[i]--;
   }

   public final synchronized int getAndAdd(int i, int delta) {
      int old = this.array[i];
      int[] var10000 = this.array;
      var10000[i] += delta;
      return old;
   }

   public final synchronized int incrementAndGet(int i) {
      return ++this.array[i];
   }

   public final synchronized int decrementAndGet(int i) {
      return --this.array[i];
   }

   public final synchronized int addAndGet(int i, int delta) {
      int[] var10000 = this.array;
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

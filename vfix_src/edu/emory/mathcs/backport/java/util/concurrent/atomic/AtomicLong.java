package edu.emory.mathcs.backport.java.util.concurrent.atomic;

import java.io.Serializable;

public class AtomicLong extends Number implements Serializable {
   private static final long serialVersionUID = 1927816293512124184L;
   private volatile long value;

   public AtomicLong(long initialValue) {
      this.value = initialValue;
   }

   public AtomicLong() {
   }

   public final long get() {
      return this.value;
   }

   public final synchronized void set(long newValue) {
      this.value = newValue;
   }

   public final synchronized void lazySet(long newValue) {
      this.value = newValue;
   }

   public final synchronized long getAndSet(long newValue) {
      long old = this.value;
      this.value = newValue;
      return old;
   }

   public final synchronized boolean compareAndSet(long expect, long update) {
      if (this.value == expect) {
         this.value = update;
         return true;
      } else {
         return false;
      }
   }

   public final synchronized boolean weakCompareAndSet(long expect, long update) {
      if (this.value == expect) {
         this.value = update;
         return true;
      } else {
         return false;
      }
   }

   public final synchronized long getAndIncrement() {
      return (long)(this.value++);
   }

   public final synchronized long getAndDecrement() {
      return (long)(this.value--);
   }

   public final synchronized long getAndAdd(long delta) {
      long old = this.value;
      this.value += delta;
      return old;
   }

   public final synchronized long incrementAndGet() {
      return ++this.value;
   }

   public final synchronized long decrementAndGet() {
      return --this.value;
   }

   public final synchronized long addAndGet(long delta) {
      return this.value += delta;
   }

   public String toString() {
      return Long.toString(this.get());
   }

   public int intValue() {
      return (int)this.get();
   }

   public long longValue() {
      return this.get();
   }

   public float floatValue() {
      return (float)this.get();
   }

   public double doubleValue() {
      return (double)this.get();
   }
}

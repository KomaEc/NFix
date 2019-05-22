package edu.emory.mathcs.backport.java.util.concurrent.atomic;

import java.io.Serializable;

public class AtomicInteger extends Number implements Serializable {
   private static final long serialVersionUID = 6214790243416807050L;
   private volatile int value;

   public AtomicInteger(int initialValue) {
      this.value = initialValue;
   }

   public AtomicInteger() {
   }

   public final int get() {
      return this.value;
   }

   public final synchronized void set(int newValue) {
      this.value = newValue;
   }

   public final synchronized void lazySet(int newValue) {
      this.value = newValue;
   }

   public final synchronized int getAndSet(int newValue) {
      int old = this.value;
      this.value = newValue;
      return old;
   }

   public final synchronized boolean compareAndSet(int expect, int update) {
      if (this.value == expect) {
         this.value = update;
         return true;
      } else {
         return false;
      }
   }

   public final synchronized boolean weakCompareAndSet(int expect, int update) {
      if (this.value == expect) {
         this.value = update;
         return true;
      } else {
         return false;
      }
   }

   public final synchronized int getAndIncrement() {
      return this.value++;
   }

   public final synchronized int getAndDecrement() {
      return this.value--;
   }

   public final synchronized int getAndAdd(int delta) {
      int old = this.value;
      this.value += delta;
      return old;
   }

   public final synchronized int incrementAndGet() {
      return ++this.value;
   }

   public final synchronized int decrementAndGet() {
      return --this.value;
   }

   public final synchronized int addAndGet(int delta) {
      return this.value += delta;
   }

   public String toString() {
      return Integer.toString(this.get());
   }

   public int intValue() {
      return this.get();
   }

   public long longValue() {
      return (long)this.get();
   }

   public float floatValue() {
      return (float)this.get();
   }

   public double doubleValue() {
      return (double)this.get();
   }
}

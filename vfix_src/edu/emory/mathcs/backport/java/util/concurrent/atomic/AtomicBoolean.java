package edu.emory.mathcs.backport.java.util.concurrent.atomic;

import java.io.Serializable;

public class AtomicBoolean implements Serializable {
   private static final long serialVersionUID = 4654671469794556979L;
   private volatile int value;

   public AtomicBoolean(boolean initialValue) {
      this.value = initialValue ? 1 : 0;
   }

   public AtomicBoolean() {
   }

   public final boolean get() {
      return this.value != 0;
   }

   public final synchronized boolean compareAndSet(boolean expect, boolean update) {
      if (expect == (this.value != 0)) {
         this.value = update ? 1 : 0;
         return true;
      } else {
         return false;
      }
   }

   public synchronized boolean weakCompareAndSet(boolean expect, boolean update) {
      if (expect == (this.value != 0)) {
         this.value = update ? 1 : 0;
         return true;
      } else {
         return false;
      }
   }

   public final synchronized void set(boolean newValue) {
      this.value = newValue ? 1 : 0;
   }

   public final synchronized void lazySet(boolean newValue) {
      this.value = newValue ? 1 : 0;
   }

   public final synchronized boolean getAndSet(boolean newValue) {
      int old = this.value;
      this.value = newValue ? 1 : 0;
      return old != 0;
   }

   public String toString() {
      return Boolean.toString(this.get());
   }
}

package edu.emory.mathcs.backport.java.util.concurrent.atomic;

import java.io.Serializable;

public class AtomicReference implements Serializable {
   private static final long serialVersionUID = -1848883965231344442L;
   private volatile Object value;

   public AtomicReference(Object initialValue) {
      this.value = initialValue;
   }

   public AtomicReference() {
   }

   public final Object get() {
      return this.value;
   }

   public final synchronized void set(Object newValue) {
      this.value = newValue;
   }

   public final synchronized void lazySet(Object newValue) {
      this.value = newValue;
   }

   public final synchronized boolean compareAndSet(Object expect, Object update) {
      if (this.value == expect) {
         this.value = update;
         return true;
      } else {
         return false;
      }
   }

   public final synchronized boolean weakCompareAndSet(Object expect, Object update) {
      if (this.value == expect) {
         this.value = update;
         return true;
      } else {
         return false;
      }
   }

   public final synchronized Object getAndSet(Object newValue) {
      Object old = this.value;
      this.value = newValue;
      return old;
   }

   public String toString() {
      return String.valueOf(this.get());
   }
}

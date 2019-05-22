package edu.emory.mathcs.backport.java.util.concurrent.atomic;

import java.io.Serializable;

public class AtomicReferenceArray implements Serializable {
   private static final long serialVersionUID = -6209656149925076980L;
   private final Object[] array;

   public AtomicReferenceArray(int length) {
      this.array = new Object[length];
   }

   public AtomicReferenceArray(Object[] array) {
      if (array == null) {
         throw new NullPointerException();
      } else {
         int length = array.length;
         this.array = new Object[length];
         System.arraycopy(array, 0, this.array, 0, array.length);
      }
   }

   public final int length() {
      return this.array.length;
   }

   public final synchronized Object get(int i) {
      return this.array[i];
   }

   public final synchronized void set(int i, Object newValue) {
      this.array[i] = newValue;
   }

   public final synchronized void lazySet(int i, Object newValue) {
      this.array[i] = newValue;
   }

   public final synchronized Object getAndSet(int i, Object newValue) {
      Object old = this.array[i];
      this.array[i] = newValue;
      return old;
   }

   public final synchronized boolean compareAndSet(int i, Object expect, Object update) {
      if (this.array[i] == expect) {
         this.array[i] = update;
         return true;
      } else {
         return false;
      }
   }

   public final synchronized boolean weakCompareAndSet(int i, Object expect, Object update) {
      if (this.array[i] == expect) {
         this.array[i] = update;
         return true;
      } else {
         return false;
      }
   }

   public synchronized String toString() {
      if (this.array.length == 0) {
         return "[]";
      } else {
         StringBuffer buf = new StringBuffer();

         for(int i = 0; i < this.array.length; ++i) {
            if (i == 0) {
               buf.append('[');
            } else {
               buf.append(", ");
            }

            buf.append(String.valueOf(this.array[i]));
         }

         buf.append("]");
         return buf.toString();
      }
   }
}

package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;

@VisibleForTesting
class Suppliers$NonSerializableMemoizingSupplier<T> implements Supplier<T> {
   volatile Supplier<T> delegate;
   volatile boolean initialized;
   T value;

   Suppliers$NonSerializableMemoizingSupplier(Supplier<T> delegate) {
      this.delegate = (Supplier)Preconditions.checkNotNull(delegate);
   }

   public T get() {
      if (!this.initialized) {
         synchronized(this) {
            if (!this.initialized) {
               T t = this.delegate.get();
               this.value = t;
               this.initialized = true;
               this.delegate = null;
               return t;
            }
         }
      }

      return this.value;
   }

   public String toString() {
      return "Suppliers.memoize(" + this.delegate + ")";
   }
}

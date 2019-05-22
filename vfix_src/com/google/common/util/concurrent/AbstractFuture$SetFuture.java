package com.google.common.util.concurrent;

final class AbstractFuture$SetFuture<V> implements Runnable {
   final AbstractFuture<V> owner;
   final ListenableFuture<? extends V> future;

   AbstractFuture$SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
      this.owner = owner;
      this.future = future;
   }

   public void run() {
      if (AbstractFuture.access$400(this.owner) == this) {
         Object valueToSet = AbstractFuture.access$500(this.future);
         if (AbstractFuture.access$200().casValue(this.owner, this, valueToSet)) {
            AbstractFuture.access$600(this.owner);
         }

      }
   }
}

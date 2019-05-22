package com.google.common.util.concurrent;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

final class AbstractFuture$SafeAtomicHelper extends AbstractFuture$AtomicHelper {
   final AtomicReferenceFieldUpdater<AbstractFuture$Waiter, Thread> waiterThreadUpdater;
   final AtomicReferenceFieldUpdater<AbstractFuture$Waiter, AbstractFuture$Waiter> waiterNextUpdater;
   final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture$Waiter> waitersUpdater;
   final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture$Listener> listenersUpdater;
   final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;

   AbstractFuture$SafeAtomicHelper(AtomicReferenceFieldUpdater<AbstractFuture$Waiter, Thread> waiterThreadUpdater, AtomicReferenceFieldUpdater<AbstractFuture$Waiter, AbstractFuture$Waiter> waiterNextUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture$Waiter> waitersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture$Listener> listenersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
      super((AbstractFuture$1)null);
      this.waiterThreadUpdater = waiterThreadUpdater;
      this.waiterNextUpdater = waiterNextUpdater;
      this.waitersUpdater = waitersUpdater;
      this.listenersUpdater = listenersUpdater;
      this.valueUpdater = valueUpdater;
   }

   void putThread(AbstractFuture$Waiter waiter, Thread newValue) {
      this.waiterThreadUpdater.lazySet(waiter, newValue);
   }

   void putNext(AbstractFuture$Waiter waiter, AbstractFuture$Waiter newValue) {
      this.waiterNextUpdater.lazySet(waiter, newValue);
   }

   boolean casWaiters(AbstractFuture<?> future, AbstractFuture$Waiter expect, AbstractFuture$Waiter update) {
      return this.waitersUpdater.compareAndSet(future, expect, update);
   }

   boolean casListeners(AbstractFuture<?> future, AbstractFuture$Listener expect, AbstractFuture$Listener update) {
      return this.listenersUpdater.compareAndSet(future, expect, update);
   }

   boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
      return this.valueUpdater.compareAndSet(future, expect, update);
   }
}

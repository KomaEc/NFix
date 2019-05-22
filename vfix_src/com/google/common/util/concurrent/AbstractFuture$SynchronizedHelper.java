package com.google.common.util.concurrent;

final class AbstractFuture$SynchronizedHelper extends AbstractFuture$AtomicHelper {
   private AbstractFuture$SynchronizedHelper() {
      super((AbstractFuture$1)null);
   }

   void putThread(AbstractFuture$Waiter waiter, Thread newValue) {
      waiter.thread = newValue;
   }

   void putNext(AbstractFuture$Waiter waiter, AbstractFuture$Waiter newValue) {
      waiter.next = newValue;
   }

   boolean casWaiters(AbstractFuture<?> future, AbstractFuture$Waiter expect, AbstractFuture$Waiter update) {
      synchronized(future) {
         if (AbstractFuture.access$800(future) == expect) {
            AbstractFuture.access$802(future, update);
            return true;
         } else {
            return false;
         }
      }
   }

   boolean casListeners(AbstractFuture<?> future, AbstractFuture$Listener expect, AbstractFuture$Listener update) {
      synchronized(future) {
         if (AbstractFuture.access$900(future) == expect) {
            AbstractFuture.access$902(future, update);
            return true;
         } else {
            return false;
         }
      }
   }

   boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
      synchronized(future) {
         if (AbstractFuture.access$400(future) == expect) {
            AbstractFuture.access$402(future, update);
            return true;
         } else {
            return false;
         }
      }
   }

   // $FF: synthetic method
   AbstractFuture$SynchronizedHelper(AbstractFuture$1 x0) {
      this();
   }
}

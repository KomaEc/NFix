package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;

public class Exchanger {
   private final Object lock = new Object();
   private Object item;
   private int arrivalCount;

   private Object doExchange(Object x, boolean timed, long nanos) throws InterruptedException, TimeoutException {
      synchronized(this.lock) {
         long deadline = timed ? Utils.nanoTime() + nanos : 0L;

         while(this.arrivalCount == 2) {
            if (!timed) {
               this.lock.wait();
            } else {
               if (nanos <= 0L) {
                  throw new TimeoutException();
               }

               TimeUnit.NANOSECONDS.timedWait(this.lock, nanos);
               nanos = deadline - Utils.nanoTime();
            }
         }

         int count = ++this.arrivalCount;
         Object other;
         if (count == 2) {
            other = this.item;
            this.item = x;
            this.lock.notifyAll();
            return other;
         } else {
            this.item = x;
            InterruptedException interrupted = null;

            try {
               while(this.arrivalCount != 2) {
                  if (!timed) {
                     this.lock.wait();
                  } else {
                     if (nanos <= 0L) {
                        break;
                     }

                     TimeUnit.NANOSECONDS.timedWait(this.lock, nanos);
                     nanos = deadline - Utils.nanoTime();
                  }
               }
            } catch (InterruptedException var13) {
               interrupted = var13;
            }

            other = this.item;
            this.item = null;
            count = this.arrivalCount;
            this.arrivalCount = 0;
            this.lock.notifyAll();
            if (count == 2) {
               if (interrupted != null) {
                  Thread.currentThread().interrupt();
               }

               return other;
            } else if (interrupted != null) {
               throw interrupted;
            } else {
               throw new TimeoutException();
            }
         }
      }
   }

   public Object exchange(Object x) throws InterruptedException {
      try {
         return this.doExchange(x, false, 0L);
      } catch (TimeoutException var3) {
         throw new Error(var3);
      }
   }

   public Object exchange(Object x, long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
      return this.doExchange(x, true, unit.toNanos(timeout));
   }
}

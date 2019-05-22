package edu.emory.mathcs.backport.java.util.concurrent.helpers;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import java.util.Collection;

public abstract class WaitQueue {
   public abstract void insert(WaitQueue.WaitNode var1);

   public abstract WaitQueue.WaitNode extract();

   public abstract void putBack(WaitQueue.WaitNode var1);

   public abstract boolean hasNodes();

   public abstract int getLength();

   public abstract Collection getWaitingThreads();

   public abstract boolean isWaiting(Thread var1);

   public static class WaitNode {
      boolean waiting = true;
      WaitQueue.WaitNode next = null;
      final Thread owner = Thread.currentThread();

      public Thread getOwner() {
         return this.owner;
      }

      public synchronized boolean signal(WaitQueue.QueuedSync sync) {
         boolean signalled = this.waiting;
         if (signalled) {
            this.waiting = false;
            this.notify();
            sync.takeOver(this);
         }

         return signalled;
      }

      public synchronized boolean doTimedWait(WaitQueue.QueuedSync sync, long nanos) throws InterruptedException {
         if (!sync.recheck(this) && this.waiting) {
            if (nanos <= 0L) {
               this.waiting = false;
               return false;
            } else {
               long deadline = Utils.nanoTime() + nanos;

               try {
                  do {
                     TimeUnit.NANOSECONDS.timedWait(this, nanos);
                     if (!this.waiting) {
                        return true;
                     }

                     nanos = deadline - Utils.nanoTime();
                  } while(nanos > 0L);

                  this.waiting = false;
                  return false;
               } catch (InterruptedException var7) {
                  if (this.waiting) {
                     this.waiting = false;
                     throw var7;
                  } else {
                     Thread.currentThread().interrupt();
                     return true;
                  }
               }
            }
         } else {
            return true;
         }
      }

      public synchronized void doWait(WaitQueue.QueuedSync sync) throws InterruptedException {
         if (!sync.recheck(this)) {
            try {
               while(this.waiting) {
                  this.wait();
               }
            } catch (InterruptedException var3) {
               if (this.waiting) {
                  this.waiting = false;
                  throw var3;
               }

               Thread.currentThread().interrupt();
               return;
            }
         }

      }

      public synchronized void doWaitUninterruptibly(WaitQueue.QueuedSync sync) {
         if (!sync.recheck(this)) {
            boolean wasInterrupted = Thread.interrupted();

            try {
               while(this.waiting) {
                  try {
                     this.wait();
                  } catch (InterruptedException var7) {
                     wasInterrupted = true;
                  }
               }
            } finally {
               if (wasInterrupted) {
                  Thread.currentThread().interrupt();
               }

            }
         }

      }
   }

   public interface QueuedSync {
      boolean recheck(WaitQueue.WaitNode var1);

      void takeOver(WaitQueue.WaitNode var1);
   }
}

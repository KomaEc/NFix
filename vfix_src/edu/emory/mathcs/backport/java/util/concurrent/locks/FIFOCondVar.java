package edu.emory.mathcs.backport.java.util.concurrent.locks;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.FIFOWaitQueue;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.WaitQueue;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

class FIFOCondVar extends CondVar implements Condition, Serializable {
   private static final WaitQueue.QueuedSync sync = new WaitQueue.QueuedSync() {
      public boolean recheck(WaitQueue.WaitNode node) {
         return false;
      }

      public void takeOver(WaitQueue.WaitNode node) {
      }
   };
   private final WaitQueue wq = new FIFOWaitQueue();

   FIFOCondVar(CondVar.ExclusiveLock lock) {
      super(lock);
   }

   public void awaitUninterruptibly() {
      int holdCount = this.lock.getHoldCount();
      if (holdCount == 0) {
         throw new IllegalMonitorStateException();
      } else {
         WaitQueue.WaitNode n = new WaitQueue.WaitNode();
         this.wq.insert(n);

         for(int i = holdCount; i > 0; --i) {
            this.lock.unlock();
         }

         try {
            n.doWaitUninterruptibly(sync);
         } finally {
            for(int i = holdCount; i > 0; --i) {
               this.lock.lock();
            }

         }

      }
   }

   public void await() throws InterruptedException {
      int holdCount = this.lock.getHoldCount();
      if (holdCount == 0) {
         throw new IllegalMonitorStateException();
      } else if (Thread.interrupted()) {
         throw new InterruptedException();
      } else {
         WaitQueue.WaitNode n = new WaitQueue.WaitNode();
         this.wq.insert(n);

         for(int i = holdCount; i > 0; --i) {
            this.lock.unlock();
         }

         try {
            n.doWait(sync);
         } finally {
            for(int i = holdCount; i > 0; --i) {
               this.lock.lock();
            }

         }

      }
   }

   public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
      int holdCount = this.lock.getHoldCount();
      if (holdCount == 0) {
         throw new IllegalMonitorStateException();
      } else if (Thread.interrupted()) {
         throw new InterruptedException();
      } else {
         long nanos = unit.toNanos(timeout);
         WaitQueue.WaitNode n = new WaitQueue.WaitNode();
         this.wq.insert(n);
         boolean success = false;

         for(int i = holdCount; i > 0; --i) {
            this.lock.unlock();
         }

         try {
            success = n.doTimedWait(sync, nanos);
         } finally {
            for(int i = holdCount; i > 0; --i) {
               this.lock.lock();
            }

         }

         return success;
      }
   }

   public boolean awaitUntil(Date deadline) throws InterruptedException {
      if (deadline == null) {
         throw new NullPointerException();
      } else {
         long abstime = deadline.getTime();
         long start = System.currentTimeMillis();
         long msecs = abstime - start;
         return this.await(msecs, TimeUnit.MILLISECONDS);
      }
   }

   public void signal() {
      if (!this.lock.isHeldByCurrentThread()) {
         throw new IllegalMonitorStateException();
      } else {
         WaitQueue.WaitNode w;
         do {
            w = this.wq.extract();
            if (w == null) {
               return;
            }
         } while(!w.signal(sync));

      }
   }

   public void signalAll() {
      if (!this.lock.isHeldByCurrentThread()) {
         throw new IllegalMonitorStateException();
      } else {
         while(true) {
            WaitQueue.WaitNode w = this.wq.extract();
            if (w == null) {
               return;
            }

            w.signal(sync);
         }
      }
   }

   protected boolean hasWaiters() {
      if (!this.lock.isHeldByCurrentThread()) {
         throw new IllegalMonitorStateException();
      } else {
         return this.wq.hasNodes();
      }
   }

   protected int getWaitQueueLength() {
      if (!this.lock.isHeldByCurrentThread()) {
         throw new IllegalMonitorStateException();
      } else {
         return this.wq.getLength();
      }
   }

   protected Collection getWaitingThreads() {
      if (!this.lock.isHeldByCurrentThread()) {
         throw new IllegalMonitorStateException();
      } else {
         return this.wq.getWaitingThreads();
      }
   }
}

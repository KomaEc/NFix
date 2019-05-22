package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.concurrent.helpers.FIFOWaitQueue;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.WaitQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;

public class Semaphore implements Serializable {
   private static final long serialVersionUID = -3222578661600680210L;
   private final Semaphore.Sync sync;

   public Semaphore(int permits) {
      this.sync = new Semaphore.NonfairSync(permits);
   }

   public Semaphore(int permits, boolean fair) {
      this.sync = (Semaphore.Sync)(fair ? new Semaphore.FairSync(permits) : new Semaphore.NonfairSync(permits));
   }

   public void acquire() throws InterruptedException {
      this.sync.acquire(1);
   }

   public void acquireUninterruptibly() {
      this.sync.acquireUninterruptibly(1);
   }

   public boolean tryAcquire() {
      return this.sync.attempt(1);
   }

   public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
      return this.sync.attempt(1, unit.toNanos(timeout));
   }

   public void release() {
      this.sync.release(1);
   }

   public void acquire(int permits) throws InterruptedException {
      if (permits < 0) {
         throw new IllegalArgumentException();
      } else {
         this.sync.acquire(permits);
      }
   }

   public void acquireUninterruptibly(int permits) {
      this.sync.acquireUninterruptibly(permits);
   }

   public boolean tryAcquire(int permits) {
      if (permits < 0) {
         throw new IllegalArgumentException();
      } else {
         return this.sync.attempt(permits);
      }
   }

   public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException {
      if (permits < 0) {
         throw new IllegalArgumentException();
      } else {
         return this.sync.attempt(permits, unit.toNanos(timeout));
      }
   }

   public void release(int permits) {
      if (permits < 0) {
         throw new IllegalArgumentException();
      } else {
         this.sync.release(permits);
      }
   }

   public int availablePermits() {
      return this.sync.getPermits();
   }

   public int drainPermits() {
      return this.sync.drain();
   }

   protected void reducePermits(int reduction) {
      if (reduction < 0) {
         throw new IllegalArgumentException();
      } else {
         this.sync.reduce(reduction);
      }
   }

   public boolean isFair() {
      return this.sync instanceof Semaphore.FairSync;
   }

   public final boolean hasQueuedThreads() {
      return this.sync.hasQueuedThreads();
   }

   public final int getQueueLength() {
      return this.sync.getQueueLength();
   }

   protected Collection getQueuedThreads() {
      return this.sync.getQueuedThreads();
   }

   public String toString() {
      return super.toString() + "[Permits = " + this.sync.getPermits() + "]";
   }

   static final class FairSync extends Semaphore.Sync implements WaitQueue.QueuedSync {
      private static final long serialVersionUID = 2014338818796000944L;
      private transient WaitQueue wq_ = new FIFOWaitQueue();

      FairSync(int initialPermits) {
         super(initialPermits);
      }

      public void acquireUninterruptibly(int n) {
         if (!this.precheck(n)) {
            WaitQueue.WaitNode w = new Semaphore.FairSync.Node(n);
            w.doWaitUninterruptibly(this);
         }
      }

      public void acquire(int n) throws InterruptedException {
         if (Thread.interrupted()) {
            throw new InterruptedException();
         } else if (!this.precheck(n)) {
            WaitQueue.WaitNode w = new Semaphore.FairSync.Node(n);
            w.doWait(this);
         }
      }

      public boolean attempt(int n, long nanos) throws InterruptedException {
         if (Thread.interrupted()) {
            throw new InterruptedException();
         } else if (this.precheck(n)) {
            return true;
         } else if (nanos <= 0L) {
            return false;
         } else {
            WaitQueue.WaitNode w = new Semaphore.FairSync.Node(n);
            return w.doTimedWait(this, nanos);
         }
      }

      protected synchronized boolean precheck(int n) {
         boolean pass = this.permits_ >= n;
         if (pass) {
            this.permits_ -= n;
         }

         return pass;
      }

      public synchronized boolean recheck(WaitQueue.WaitNode w) {
         Semaphore.FairSync.Node node = (Semaphore.FairSync.Node)w;
         boolean pass = this.permits_ >= node.requests;
         if (pass) {
            this.permits_ -= node.requests;
         } else {
            this.wq_.insert(w);
         }

         return pass;
      }

      public void takeOver(WaitQueue.WaitNode n) {
      }

      protected synchronized Semaphore.FairSync.Node getSignallee(int n) {
         Semaphore.FairSync.Node w = (Semaphore.FairSync.Node)this.wq_.extract();
         this.permits_ += n;
         if (w == null) {
            return null;
         } else if (w.requests > this.permits_) {
            this.wq_.putBack(w);
            return null;
         } else {
            this.permits_ -= w.requests;
            return w;
         }
      }

      public void release(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Negative argument");
         } else {
            while(true) {
               Semaphore.FairSync.Node w = this.getSignallee(n);
               if (w == null) {
                  return;
               }

               if (w.signal(this)) {
                  return;
               }

               n = w.requests;
            }
         }
      }

      public synchronized boolean hasQueuedThreads() {
         return this.wq_.hasNodes();
      }

      public synchronized int getQueueLength() {
         return this.wq_.getLength();
      }

      public synchronized Collection getQueuedThreads() {
         return this.wq_.getWaitingThreads();
      }

      private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
         in.defaultReadObject();
         synchronized(this) {
            this.wq_ = new FIFOWaitQueue();
         }
      }

      static final class Node extends WaitQueue.WaitNode {
         final int requests;

         Node(int requests) {
            this.requests = requests;
         }
      }
   }

   static final class NonfairSync extends Semaphore.Sync {
      private static final long serialVersionUID = -2694183684443567898L;

      protected NonfairSync(int initialPermits) {
         super(initialPermits);
      }

      private static void checkAgainstMultiacquire(int n) {
         if (n != 1) {
            throw new UnsupportedOperationException("Atomic multi-acquire supported only in FAIR semaphores");
         }
      }

      public void acquireUninterruptibly(int n) {
         if (n != 0) {
            checkAgainstMultiacquire(n);
            synchronized(this) {
               if (this.permits_ > 0) {
                  --this.permits_;
               } else {
                  boolean wasInterrupted = Thread.interrupted();

                  try {
                     do {
                        try {
                           this.wait();
                        } catch (InterruptedException var10) {
                           wasInterrupted = true;
                        }
                     } while(this.permits_ <= 0);

                     --this.permits_;
                  } finally {
                     if (wasInterrupted) {
                        Thread.currentThread().interrupt();
                     }

                  }

               }
            }
         }
      }

      public void acquire(int n) throws InterruptedException {
         if (Thread.interrupted()) {
            throw new InterruptedException();
         } else if (n != 0) {
            checkAgainstMultiacquire(n);
            synchronized(this) {
               while(this.permits_ <= 0) {
                  try {
                     this.wait();
                  } catch (InterruptedException var5) {
                     this.notify();
                     throw var5;
                  }
               }

               --this.permits_;
            }
         }
      }

      public boolean attempt(int n, long nanos) throws InterruptedException {
         if (Thread.interrupted()) {
            throw new InterruptedException();
         } else if (n == 0) {
            return true;
         } else {
            checkAgainstMultiacquire(n);
            synchronized(this) {
               if (this.permits_ > 0) {
                  --this.permits_;
                  return true;
               } else if (nanos <= 0L) {
                  return false;
               } else {
                  try {
                     long deadline = Utils.nanoTime() + nanos;

                     boolean var10000;
                     do {
                        TimeUnit.NANOSECONDS.timedWait(this, nanos);
                        if (this.permits_ > 0) {
                           --this.permits_;
                           var10000 = true;
                           return var10000;
                        }

                        nanos = deadline - Utils.nanoTime();
                     } while(nanos > 0L);

                     var10000 = false;
                     return var10000;
                  } catch (InterruptedException var9) {
                     this.notify();
                     throw var9;
                  }
               }
            }
         }
      }

      public synchronized void release(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Negative argument");
         } else {
            this.permits_ += n;

            for(int i = 0; i < n; ++i) {
               this.notify();
            }

         }
      }

      public boolean hasQueuedThreads() {
         throw new UnsupportedOperationException("Use FAIR version");
      }

      public int getQueueLength() {
         throw new UnsupportedOperationException("Use FAIR version");
      }

      public Collection getQueuedThreads() {
         throw new UnsupportedOperationException("Use FAIR version");
      }
   }

   abstract static class Sync implements Serializable {
      private static final long serialVersionUID = 1192457210091910933L;
      int permits_;

      protected Sync(int permits) {
         this.permits_ = permits;
      }

      abstract void acquireUninterruptibly(int var1);

      abstract void acquire(int var1) throws InterruptedException;

      public boolean attempt(int n) {
         synchronized(this) {
            if (this.permits_ >= n) {
               this.permits_ -= n;
               return true;
            } else {
               return false;
            }
         }
      }

      abstract boolean attempt(int var1, long var2) throws InterruptedException;

      abstract void release(int var1);

      public synchronized int getPermits() {
         return this.permits_;
      }

      public synchronized int drain() {
         int acquired = this.permits_;
         this.permits_ = 0;
         return acquired;
      }

      public synchronized void reduce(int reduction) {
         this.permits_ -= reduction;
      }

      abstract boolean hasQueuedThreads();

      abstract int getQueueLength();

      abstract Collection getQueuedThreads();
   }
}

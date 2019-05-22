package edu.emory.mathcs.backport.java.util.concurrent.locks;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.FIFOWaitQueue;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.WaitQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;

public class ReentrantLock implements Lock, Serializable, CondVar.ExclusiveLock {
   private static final long serialVersionUID = 7373984872572414699L;
   private final ReentrantLock.Sync sync;

   public ReentrantLock() {
      this.sync = new ReentrantLock.NonfairSync();
   }

   public ReentrantLock(boolean fair) {
      this.sync = (ReentrantLock.Sync)(fair ? new ReentrantLock.FairSync() : new ReentrantLock.NonfairSync());
   }

   public void lock() {
      this.sync.lock();
   }

   public void lockInterruptibly() throws InterruptedException {
      this.sync.lockInterruptibly();
   }

   public boolean tryLock() {
      return this.sync.tryLock();
   }

   public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
      return this.sync.tryLock(unit.toNanos(timeout));
   }

   public void unlock() {
      this.sync.unlock();
   }

   public Condition newCondition() {
      return (Condition)(this.isFair() ? new FIFOCondVar(this) : new CondVar(this));
   }

   public int getHoldCount() {
      return this.sync.getHoldCount();
   }

   public boolean isHeldByCurrentThread() {
      return this.sync.isHeldByCurrentThread();
   }

   public boolean isLocked() {
      return this.sync.isLocked();
   }

   public final boolean isFair() {
      return this.sync.isFair();
   }

   protected Thread getOwner() {
      return this.sync.getOwner();
   }

   public final boolean hasQueuedThreads() {
      return this.sync.hasQueuedThreads();
   }

   public final boolean hasQueuedThread(Thread thread) {
      return this.sync.isQueued(thread);
   }

   public final int getQueueLength() {
      return this.sync.getQueueLength();
   }

   protected Collection getQueuedThreads() {
      return this.sync.getQueuedThreads();
   }

   public boolean hasWaiters(Condition condition) {
      return this.asCondVar(condition).hasWaiters();
   }

   public int getWaitQueueLength(Condition condition) {
      return this.asCondVar(condition).getWaitQueueLength();
   }

   protected Collection getWaitingThreads(Condition condition) {
      return this.asCondVar(condition).getWaitingThreads();
   }

   public String toString() {
      Thread o = this.getOwner();
      return super.toString() + (o == null ? "[Unlocked]" : "[Locked by thread " + o.getName() + "]");
   }

   private CondVar asCondVar(Condition condition) {
      if (condition == null) {
         throw new NullPointerException();
      } else if (!(condition instanceof CondVar)) {
         throw new IllegalArgumentException("not owner");
      } else {
         CondVar condVar = (CondVar)condition;
         if (condVar.lock != this) {
            throw new IllegalArgumentException("not owner");
         } else {
            return condVar;
         }
      }
   }

   static final class FairSync extends ReentrantLock.Sync implements WaitQueue.QueuedSync {
      private static final long serialVersionUID = -3000897897090466540L;
      private transient WaitQueue wq_ = new FIFOWaitQueue();

      public synchronized boolean recheck(WaitQueue.WaitNode node) {
         Thread caller = Thread.currentThread();
         if (this.owner_ == null) {
            this.owner_ = caller;
            this.holds_ = 1;
            return true;
         } else if (caller == this.owner_) {
            this.incHolds();
            return true;
         } else {
            this.wq_.insert(node);
            return false;
         }
      }

      public synchronized void takeOver(WaitQueue.WaitNode node) {
         this.owner_ = node.getOwner();
      }

      public void lock() {
         Thread caller = Thread.currentThread();
         synchronized(this) {
            if (this.owner_ == null) {
               this.owner_ = caller;
               this.holds_ = 1;
               return;
            }

            if (caller == this.owner_) {
               this.incHolds();
               return;
            }
         }

         WaitQueue.WaitNode n = new WaitQueue.WaitNode();
         n.doWaitUninterruptibly(this);
      }

      public void lockInterruptibly() throws InterruptedException {
         if (Thread.interrupted()) {
            throw new InterruptedException();
         } else {
            Thread caller = Thread.currentThread();
            synchronized(this) {
               if (this.owner_ == null) {
                  this.owner_ = caller;
                  this.holds_ = 1;
                  return;
               }

               if (caller == this.owner_) {
                  this.incHolds();
                  return;
               }
            }

            WaitQueue.WaitNode n = new WaitQueue.WaitNode();
            n.doWait(this);
         }
      }

      public boolean tryLock(long nanos) throws InterruptedException {
         if (Thread.interrupted()) {
            throw new InterruptedException();
         } else {
            Thread caller = Thread.currentThread();
            synchronized(this) {
               if (this.owner_ == null) {
                  this.owner_ = caller;
                  this.holds_ = 1;
                  return true;
               }

               if (caller == this.owner_) {
                  this.incHolds();
                  return true;
               }
            }

            WaitQueue.WaitNode n = new WaitQueue.WaitNode();
            return n.doTimedWait(this, nanos);
         }
      }

      protected synchronized WaitQueue.WaitNode getSignallee(Thread caller) {
         if (caller != this.owner_) {
            throw new IllegalMonitorStateException("Not owner");
         } else if (this.holds_ >= 2) {
            --this.holds_;
            return null;
         } else {
            WaitQueue.WaitNode w = this.wq_.extract();
            if (w == null) {
               this.owner_ = null;
               this.holds_ = 0;
            }

            return w;
         }
      }

      public void unlock() {
         Thread caller = Thread.currentThread();

         WaitQueue.WaitNode w;
         do {
            w = this.getSignallee(caller);
            if (w == null) {
               return;
            }
         } while(!w.signal(this));

      }

      public final boolean isFair() {
         return true;
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

      public synchronized boolean isQueued(Thread thread) {
         return this.wq_.isWaiting(thread);
      }

      private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
         in.defaultReadObject();
         synchronized(this) {
            this.wq_ = new FIFOWaitQueue();
         }
      }
   }

   static final class NonfairSync extends ReentrantLock.Sync {
      private static final long serialVersionUID = 7316153563782823691L;

      public void lock() {
         Thread caller = Thread.currentThread();
         synchronized(this) {
            if (this.owner_ == null) {
               this.owner_ = caller;
               this.holds_ = 1;
            } else if (caller == this.owner_) {
               this.incHolds();
            } else {
               boolean wasInterrupted = Thread.interrupted();

               try {
                  do {
                     try {
                        this.wait();
                     } catch (InterruptedException var10) {
                        wasInterrupted = true;
                     }
                  } while(this.owner_ != null);

                  this.owner_ = caller;
                  this.holds_ = 1;
               } finally {
                  if (wasInterrupted) {
                     Thread.currentThread().interrupt();
                  }

               }

            }
         }
      }

      public void lockInterruptibly() throws InterruptedException {
         if (Thread.interrupted()) {
            throw new InterruptedException();
         } else {
            Thread caller = Thread.currentThread();
            synchronized(this) {
               if (this.owner_ == null) {
                  this.owner_ = caller;
                  this.holds_ = 1;
               } else if (caller == this.owner_) {
                  this.incHolds();
               } else {
                  try {
                     do {
                        this.wait();
                     } while(this.owner_ != null);

                     this.owner_ = caller;
                     this.holds_ = 1;
                  } catch (InterruptedException var5) {
                     if (this.owner_ == null) {
                        this.notify();
                     }

                     throw var5;
                  }

               }
            }
         }
      }

      public boolean tryLock(long nanos) throws InterruptedException {
         if (Thread.interrupted()) {
            throw new InterruptedException();
         } else {
            Thread caller = Thread.currentThread();
            synchronized(this) {
               if (this.owner_ == null) {
                  this.owner_ = caller;
                  this.holds_ = 1;
                  return true;
               } else if (caller == this.owner_) {
                  this.incHolds();
                  return true;
               } else if (nanos <= 0L) {
                  return false;
               } else {
                  long deadline = Utils.nanoTime() + nanos;

                  try {
                     boolean var10000;
                     do {
                        TimeUnit.NANOSECONDS.timedWait(this, nanos);
                        if (caller == this.owner_) {
                           this.incHolds();
                           var10000 = true;
                           return var10000;
                        }

                        if (this.owner_ == null) {
                           this.owner_ = caller;
                           this.holds_ = 1;
                           var10000 = true;
                           return var10000;
                        }

                        nanos = deadline - Utils.nanoTime();
                     } while(nanos > 0L);

                     var10000 = false;
                     return var10000;
                  } catch (InterruptedException var9) {
                     if (this.owner_ == null) {
                        this.notify();
                     }

                     throw var9;
                  }
               }
            }
         }
      }

      public synchronized void unlock() {
         if (Thread.currentThread() != this.owner_) {
            throw new IllegalMonitorStateException("Not owner");
         } else {
            if (--this.holds_ == 0) {
               this.owner_ = null;
               this.notify();
            }

         }
      }

      public final boolean isFair() {
         return false;
      }
   }

   abstract static class Sync implements Serializable {
      private static final long serialVersionUID = -5179523762034025860L;
      protected transient Thread owner_ = null;
      protected transient int holds_ = 0;

      protected Sync() {
      }

      public abstract void lock();

      public abstract void lockInterruptibly() throws InterruptedException;

      final void incHolds() {
         int nextHolds = ++this.holds_;
         if (nextHolds < 0) {
            throw new Error("Maximum lock count exceeded");
         } else {
            this.holds_ = nextHolds;
         }
      }

      public boolean tryLock() {
         Thread caller = Thread.currentThread();
         synchronized(this) {
            if (this.owner_ == null) {
               this.owner_ = caller;
               this.holds_ = 1;
               return true;
            } else if (caller == this.owner_) {
               this.incHolds();
               return true;
            } else {
               return false;
            }
         }
      }

      public abstract boolean tryLock(long var1) throws InterruptedException;

      public abstract void unlock();

      public synchronized int getHoldCount() {
         return this.isHeldByCurrentThread() ? this.holds_ : 0;
      }

      public synchronized boolean isHeldByCurrentThread() {
         return this.holds_ > 0 && Thread.currentThread() == this.owner_;
      }

      public synchronized boolean isLocked() {
         return this.owner_ != null;
      }

      public abstract boolean isFair();

      protected synchronized Thread getOwner() {
         return this.owner_;
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

      public boolean isQueued(Thread thread) {
         throw new UnsupportedOperationException("Use FAIR version");
      }
   }
}

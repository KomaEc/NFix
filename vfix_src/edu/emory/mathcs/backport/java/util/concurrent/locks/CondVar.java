package edu.emory.mathcs.backport.java.util.concurrent.locks;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

class CondVar implements Condition, Serializable {
   protected final CondVar.ExclusiveLock lock;

   CondVar(CondVar.ExclusiveLock lock) {
      this.lock = lock;
   }

   public void awaitUninterruptibly() {
      int holdCount = this.lock.getHoldCount();
      if (holdCount == 0) {
         throw new IllegalMonitorStateException();
      } else {
         boolean wasInterrupted = Thread.interrupted();

         try {
            synchronized(this) {
               for(int i = holdCount; i > 0; --i) {
                  this.lock.unlock();
               }

               try {
                  this.wait();
               } catch (InterruptedException var12) {
                  wasInterrupted = true;
               }
            }
         } finally {
            for(int i = holdCount; i > 0; --i) {
               this.lock.lock();
            }

            if (wasInterrupted) {
               Thread.currentThread().interrupt();
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
         try {
            synchronized(this) {
               for(int i = holdCount; i > 0; --i) {
                  this.lock.unlock();
               }

               try {
                  this.wait();
               } catch (InterruptedException var11) {
                  this.notify();
                  throw var11;
               }
            }
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
         boolean success = false;

         try {
            synchronized(this) {
               for(int i = holdCount; i > 0; --i) {
                  this.lock.unlock();
               }

               try {
                  if (nanos > 0L) {
                     long start = Utils.nanoTime();
                     TimeUnit.NANOSECONDS.timedWait(this, nanos);
                     success = Utils.nanoTime() - start < nanos;
                  }
               } catch (InterruptedException var19) {
                  this.notify();
                  throw var19;
               }
            }
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
         int holdCount = this.lock.getHoldCount();
         if (holdCount == 0) {
            throw new IllegalMonitorStateException();
         } else {
            long abstime = deadline.getTime();
            if (Thread.interrupted()) {
               throw new InterruptedException();
            } else {
               boolean success = false;

               try {
                  synchronized(this) {
                     for(int i = holdCount; i > 0; --i) {
                        this.lock.unlock();
                     }

                     try {
                        long start = System.currentTimeMillis();
                        long msecs = abstime - start;
                        if (msecs > 0L) {
                           this.wait(msecs);
                           success = System.currentTimeMillis() - start < msecs;
                        }
                     } catch (InterruptedException var19) {
                        this.notify();
                        throw var19;
                     }
                  }
               } finally {
                  for(int i = holdCount; i > 0; --i) {
                     this.lock.lock();
                  }

               }

               return success;
            }
         }
      }
   }

   public synchronized void signal() {
      if (!this.lock.isHeldByCurrentThread()) {
         throw new IllegalMonitorStateException();
      } else {
         this.notify();
      }
   }

   public synchronized void signalAll() {
      if (!this.lock.isHeldByCurrentThread()) {
         throw new IllegalMonitorStateException();
      } else {
         this.notifyAll();
      }
   }

   protected CondVar.ExclusiveLock getLock() {
      return this.lock;
   }

   protected boolean hasWaiters() {
      throw new UnsupportedOperationException("Use FAIR version");
   }

   protected int getWaitQueueLength() {
      throw new UnsupportedOperationException("Use FAIR version");
   }

   protected Collection getWaitingThreads() {
      throw new UnsupportedOperationException("Use FAIR version");
   }

   interface ExclusiveLock extends Lock {
      boolean isHeldByCurrentThread();

      int getHoldCount();
   }
}

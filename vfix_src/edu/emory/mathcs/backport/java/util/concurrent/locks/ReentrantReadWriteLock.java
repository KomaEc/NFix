package edu.emory.mathcs.backport.java.util.concurrent.locks;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;

public class ReentrantReadWriteLock implements ReadWriteLock, Serializable {
   private static final long serialVersionUID = -3463448656717690166L;
   final ReentrantReadWriteLock.ReadLock readerLock_ = new ReentrantReadWriteLock.ReadLock(this);
   final ReentrantReadWriteLock.WriteLock writerLock_ = new ReentrantReadWriteLock.WriteLock(this);
   final ReentrantReadWriteLock.Sync sync = new ReentrantReadWriteLock.NonfairSync();

   public Lock writeLock() {
      return this.writerLock_;
   }

   public Lock readLock() {
      return this.readerLock_;
   }

   public final boolean isFair() {
      return false;
   }

   protected Thread getOwner() {
      return this.sync.getOwner();
   }

   public int getReadLockCount() {
      return this.sync.getReadLockCount();
   }

   public boolean isWriteLocked() {
      return this.sync.isWriteLocked();
   }

   public boolean isWriteLockedByCurrentThread() {
      return this.sync.isWriteLockedByCurrentThread();
   }

   public int getWriteHoldCount() {
      return this.sync.getWriteHoldCount();
   }

   public int getReadHoldCount() {
      return this.sync.getReadHoldCount();
   }

   public final boolean hasQueuedThreads() {
      return this.sync.hasQueuedThreads();
   }

   public final int getQueueLength() {
      return this.sync.getQueueLength();
   }

   public String toString() {
      return super.toString() + "[Write locks = " + this.getWriteHoldCount() + ", Read locks = " + this.getReadLockCount() + "]";
   }

   public static class WriteLock implements Lock, CondVar.ExclusiveLock, Serializable {
      private static final long serialVersionUID = -4992448646407690164L;
      final ReentrantReadWriteLock lock;

      protected WriteLock(ReentrantReadWriteLock lock) {
         if (lock == null) {
            throw new NullPointerException();
         } else {
            this.lock = lock;
         }
      }

      public void lock() {
         synchronized(this) {
            if (!this.lock.sync.startWriteFromNewWriter()) {
               boolean wasInterrupted = Thread.interrupted();

               try {
                  do {
                     try {
                        this.wait();
                     } catch (InterruptedException var9) {
                        wasInterrupted = true;
                     }
                  } while(!this.lock.sync.startWriteFromWaitingWriter());
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
            InterruptedException ie = null;
            synchronized(this) {
               if (!this.lock.sync.startWriteFromNewWriter()) {
                  while(true) {
                     try {
                        this.wait();
                        if (this.lock.sync.startWriteFromWaitingWriter()) {
                           return;
                        }
                     } catch (InterruptedException var5) {
                        this.lock.sync.cancelledWaitingWriter();
                        this.notify();
                        ie = var5;
                        break;
                     }
                  }
               }
            }

            if (ie != null) {
               this.lock.readerLock_.signalWaiters();
               throw ie;
            }
         }
      }

      public boolean tryLock() {
         return this.lock.sync.startWrite();
      }

      public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
         if (Thread.interrupted()) {
            throw new InterruptedException();
         } else {
            InterruptedException ie = null;
            long nanos = unit.toNanos(timeout);
            synchronized(this) {
               if (nanos <= 0L) {
                  return this.lock.sync.startWrite();
               }

               if (this.lock.sync.startWriteFromNewWriter()) {
                  return true;
               }

               long deadline = Utils.nanoTime() + nanos;

               while(true) {
                  try {
                     TimeUnit.NANOSECONDS.timedWait(this, nanos);
                  } catch (InterruptedException var12) {
                     this.lock.sync.cancelledWaitingWriter();
                     this.notify();
                     ie = var12;
                     break;
                  }

                  if (this.lock.sync.startWriteFromWaitingWriter()) {
                     return true;
                  }

                  nanos = deadline - Utils.nanoTime();
                  if (nanos <= 0L) {
                     this.lock.sync.cancelledWaitingWriter();
                     this.notify();
                     break;
                  }
               }
            }

            this.lock.readerLock_.signalWaiters();
            if (ie != null) {
               throw ie;
            } else {
               return false;
            }
         }
      }

      public void unlock() {
         switch(this.lock.sync.endWrite()) {
         case 0:
            return;
         case 1:
            this.lock.readerLock_.signalWaiters();
            return;
         case 2:
            this.lock.writerLock_.signalWaiters();
            return;
         default:
         }
      }

      public Condition newCondition() {
         return new CondVar(this);
      }

      synchronized void signalWaiters() {
         this.notify();
      }

      public String toString() {
         Thread o = this.lock.getOwner();
         return super.toString() + (o == null ? "[Unlocked]" : "[Locked by thread " + o.getName() + "]");
      }

      public boolean isHeldByCurrentThread() {
         return this.lock.sync.isWriteLockedByCurrentThread();
      }

      public int getHoldCount() {
         return this.lock.sync.getWriteHoldCount();
      }
   }

   public static class ReadLock implements Lock, Serializable {
      private static final long serialVersionUID = -5992448646407690164L;
      final ReentrantReadWriteLock lock;

      protected ReadLock(ReentrantReadWriteLock lock) {
         if (lock == null) {
            throw new NullPointerException();
         } else {
            this.lock = lock;
         }
      }

      public void lock() {
         synchronized(this) {
            if (!this.lock.sync.startReadFromNewReader()) {
               boolean wasInterrupted = Thread.interrupted();

               try {
                  do {
                     try {
                        this.wait();
                     } catch (InterruptedException var9) {
                        wasInterrupted = true;
                     }
                  } while(!this.lock.sync.startReadFromWaitingReader());
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
            InterruptedException ie = null;
            synchronized(this) {
               if (!this.lock.sync.startReadFromNewReader()) {
                  while(true) {
                     try {
                        this.wait();
                        if (this.lock.sync.startReadFromWaitingReader()) {
                           return;
                        }
                     } catch (InterruptedException var5) {
                        this.lock.sync.cancelledWaitingReader();
                        ie = var5;
                        break;
                     }
                  }
               }
            }

            if (ie != null) {
               this.lock.writerLock_.signalWaiters();
               throw ie;
            }
         }
      }

      public boolean tryLock() {
         return this.lock.sync.startRead();
      }

      public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
         if (Thread.interrupted()) {
            throw new InterruptedException();
         } else {
            InterruptedException ie = null;
            long nanos = unit.toNanos(timeout);
            synchronized(this) {
               if (nanos <= 0L) {
                  return this.lock.sync.startRead();
               }

               if (this.lock.sync.startReadFromNewReader()) {
                  return true;
               }

               long deadline = Utils.nanoTime() + nanos;

               while(true) {
                  try {
                     TimeUnit.NANOSECONDS.timedWait(this, nanos);
                  } catch (InterruptedException var12) {
                     this.lock.sync.cancelledWaitingReader();
                     ie = var12;
                     break;
                  }

                  if (this.lock.sync.startReadFromWaitingReader()) {
                     return true;
                  }

                  nanos = deadline - Utils.nanoTime();
                  if (nanos <= 0L) {
                     this.lock.sync.cancelledWaitingReader();
                     break;
                  }
               }
            }

            this.lock.writerLock_.signalWaiters();
            if (ie != null) {
               throw ie;
            } else {
               return false;
            }
         }
      }

      public void unlock() {
         switch(this.lock.sync.endRead()) {
         case 0:
            return;
         case 1:
            this.lock.readerLock_.signalWaiters();
            return;
         case 2:
            this.lock.writerLock_.signalWaiters();
            return;
         default:
         }
      }

      public Condition newCondition() {
         throw new UnsupportedOperationException();
      }

      synchronized void signalWaiters() {
         this.notifyAll();
      }

      public String toString() {
         int r = this.lock.getReadLockCount();
         return super.toString() + "[Read locks = " + r + "]";
      }
   }

   private static class NonfairSync extends ReentrantReadWriteLock.Sync {
      NonfairSync() {
      }
   }

   private abstract static class Sync implements Serializable {
      private static final int NONE = 0;
      private static final int READER = 1;
      private static final int WRITER = 2;
      transient int activeReaders_ = 0;
      transient Thread activeWriter_ = null;
      transient int waitingReaders_ = 0;
      transient int waitingWriters_ = 0;
      transient int writeHolds_ = 0;
      transient HashMap readers_ = new HashMap();
      static final Integer IONE = new Integer(1);

      Sync() {
      }

      synchronized boolean startReadFromNewReader() {
         boolean pass = this.startRead();
         if (!pass) {
            ++this.waitingReaders_;
         }

         return pass;
      }

      synchronized boolean startWriteFromNewWriter() {
         boolean pass = this.startWrite();
         if (!pass) {
            ++this.waitingWriters_;
         }

         return pass;
      }

      synchronized boolean startReadFromWaitingReader() {
         boolean pass = this.startRead();
         if (pass) {
            --this.waitingReaders_;
         }

         return pass;
      }

      synchronized boolean startWriteFromWaitingWriter() {
         boolean pass = this.startWrite();
         if (pass) {
            --this.waitingWriters_;
         }

         return pass;
      }

      synchronized void cancelledWaitingReader() {
         --this.waitingReaders_;
      }

      synchronized void cancelledWaitingWriter() {
         --this.waitingWriters_;
      }

      boolean allowReader() {
         return this.activeWriter_ == null && this.waitingWriters_ == 0 || this.activeWriter_ == Thread.currentThread();
      }

      synchronized boolean startRead() {
         Thread t = Thread.currentThread();
         Object c = this.readers_.get(t);
         if (c != null) {
            this.readers_.put(t, new Integer((Integer)c + 1));
            ++this.activeReaders_;
            return true;
         } else if (this.allowReader()) {
            this.readers_.put(t, IONE);
            ++this.activeReaders_;
            return true;
         } else {
            return false;
         }
      }

      synchronized boolean startWrite() {
         if (this.activeWriter_ == Thread.currentThread()) {
            ++this.writeHolds_;
            return true;
         } else if (this.writeHolds_ != 0) {
            return false;
         } else if (this.activeReaders_ != 0 && (this.readers_.size() != 1 || this.readers_.get(Thread.currentThread()) == null)) {
            return false;
         } else {
            this.activeWriter_ = Thread.currentThread();
            this.writeHolds_ = 1;
            return true;
         }
      }

      synchronized int endRead() {
         Thread t = Thread.currentThread();
         Object c = this.readers_.get(t);
         if (c == null) {
            throw new IllegalMonitorStateException();
         } else {
            --this.activeReaders_;
            if (c != IONE) {
               int h = (Integer)c - 1;
               Integer ih = h == 1 ? IONE : new Integer(h);
               this.readers_.put(t, ih);
               return 0;
            } else {
               this.readers_.remove(t);
               if (this.writeHolds_ > 0) {
                  return 0;
               } else {
                  return this.activeReaders_ == 0 && this.waitingWriters_ > 0 ? 2 : 0;
               }
            }
         }
      }

      synchronized int endWrite() {
         if (this.activeWriter_ != Thread.currentThread()) {
            throw new IllegalMonitorStateException();
         } else {
            --this.writeHolds_;
            if (this.writeHolds_ > 0) {
               return 0;
            } else {
               this.activeWriter_ = null;
               if (this.waitingReaders_ > 0 && this.allowReader()) {
                  return 1;
               } else {
                  return this.waitingWriters_ > 0 ? 2 : 0;
               }
            }
         }
      }

      synchronized Thread getOwner() {
         return this.activeWriter_;
      }

      synchronized int getReadLockCount() {
         return this.activeReaders_;
      }

      synchronized boolean isWriteLocked() {
         return this.activeWriter_ != null;
      }

      synchronized boolean isWriteLockedByCurrentThread() {
         return this.activeWriter_ == Thread.currentThread();
      }

      synchronized int getWriteHoldCount() {
         return this.isWriteLockedByCurrentThread() ? this.writeHolds_ : 0;
      }

      synchronized int getReadHoldCount() {
         if (this.activeReaders_ == 0) {
            return 0;
         } else {
            Thread t = Thread.currentThread();
            Integer i = (Integer)this.readers_.get(t);
            return i == null ? 0 : i;
         }
      }

      final synchronized boolean hasQueuedThreads() {
         return this.waitingWriters_ > 0 || this.waitingReaders_ > 0;
      }

      final synchronized int getQueueLength() {
         return this.waitingWriters_ + this.waitingReaders_;
      }

      private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
         in.defaultReadObject();
         synchronized(this) {
            this.readers_ = new HashMap();
         }
      }
   }
}

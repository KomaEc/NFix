package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.AbstractQueue;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SynchronousQueue extends AbstractQueue implements BlockingQueue, Serializable {
   private static final long serialVersionUID = -3223113410248163686L;
   private final ReentrantLock qlock;
   private final SynchronousQueue.WaitQueue waitingProducers;
   private final SynchronousQueue.WaitQueue waitingConsumers;

   public SynchronousQueue() {
      this(false);
   }

   public SynchronousQueue(boolean fair) {
      if (fair) {
         this.qlock = new ReentrantLock(true);
         this.waitingProducers = new SynchronousQueue.FifoWaitQueue();
         this.waitingConsumers = new SynchronousQueue.FifoWaitQueue();
      } else {
         this.qlock = new ReentrantLock();
         this.waitingProducers = new SynchronousQueue.LifoWaitQueue();
         this.waitingConsumers = new SynchronousQueue.LifoWaitQueue();
      }

   }

   private void unlinkCancelledConsumer(SynchronousQueue.Node node) {
      if (this.waitingConsumers.shouldUnlink(node)) {
         this.qlock.lock();

         try {
            if (this.waitingConsumers.shouldUnlink(node)) {
               this.waitingConsumers.unlink(node);
            }
         } finally {
            this.qlock.unlock();
         }
      }

   }

   private void unlinkCancelledProducer(SynchronousQueue.Node node) {
      if (this.waitingProducers.shouldUnlink(node)) {
         this.qlock.lock();

         try {
            if (this.waitingProducers.shouldUnlink(node)) {
               this.waitingProducers.unlink(node);
            }
         } finally {
            this.qlock.unlock();
         }
      }

   }

   public void put(Object e) throws InterruptedException {
      if (e == null) {
         throw new NullPointerException();
      } else {
         ReentrantLock qlock = this.qlock;

         while(!Thread.interrupted()) {
            qlock.lock();

            SynchronousQueue.Node node;
            boolean mustWait;
            try {
               node = this.waitingConsumers.deq();
               if (mustWait = node == null) {
                  node = this.waitingProducers.enq(e);
               }
            } finally {
               qlock.unlock();
            }

            if (mustWait) {
               try {
                  node.waitForTake();
                  return;
               } catch (InterruptedException var8) {
                  this.unlinkCancelledProducer(node);
                  throw var8;
               }
            }

            if (node.setItem(e)) {
               return;
            }
         }

         throw new InterruptedException();
      }
   }

   public boolean offer(Object e, long timeout, TimeUnit unit) throws InterruptedException {
      if (e == null) {
         throw new NullPointerException();
      } else {
         long nanos = unit.toNanos(timeout);
         ReentrantLock qlock = this.qlock;

         while(!Thread.interrupted()) {
            qlock.lock();

            SynchronousQueue.Node node;
            boolean mustWait;
            try {
               node = this.waitingConsumers.deq();
               if (mustWait = node == null) {
                  node = this.waitingProducers.enq(e);
               }
            } finally {
               qlock.unlock();
            }

            if (mustWait) {
               try {
                  boolean x = node.waitForTake(nanos);
                  if (!x) {
                     this.unlinkCancelledProducer(node);
                  }

                  return x;
               } catch (InterruptedException var13) {
                  this.unlinkCancelledProducer(node);
                  throw var13;
               }
            }

            if (node.setItem(e)) {
               return true;
            }
         }

         throw new InterruptedException();
      }
   }

   public Object take() throws InterruptedException {
      ReentrantLock qlock = this.qlock;

      while(!Thread.interrupted()) {
         qlock.lock();

         SynchronousQueue.Node node;
         boolean mustWait;
         try {
            node = this.waitingProducers.deq();
            if (mustWait = node == null) {
               node = this.waitingConsumers.enq((Object)null);
            }
         } finally {
            qlock.unlock();
         }

         Object x;
         if (mustWait) {
            try {
               x = node.waitForPut();
               return x;
            } catch (InterruptedException var7) {
               this.unlinkCancelledConsumer(node);
               throw var7;
            }
         }

         x = node.getItem();
         if (x != null) {
            return x;
         }
      }

      throw new InterruptedException();
   }

   public Object poll(long timeout, TimeUnit unit) throws InterruptedException {
      long nanos = unit.toNanos(timeout);
      ReentrantLock qlock = this.qlock;

      while(!Thread.interrupted()) {
         qlock.lock();

         SynchronousQueue.Node node;
         boolean mustWait;
         try {
            node = this.waitingProducers.deq();
            if (mustWait = node == null) {
               node = this.waitingConsumers.enq((Object)null);
            }
         } finally {
            qlock.unlock();
         }

         Object x;
         if (mustWait) {
            try {
               x = node.waitForPut(nanos);
               if (x == null) {
                  this.unlinkCancelledConsumer(node);
               }

               return x;
            } catch (InterruptedException var12) {
               this.unlinkCancelledConsumer(node);
               throw var12;
            }
         }

         x = node.getItem();
         if (x != null) {
            return x;
         }
      }

      throw new InterruptedException();
   }

   public boolean offer(Object e) {
      if (e == null) {
         throw new NullPointerException();
      } else {
         ReentrantLock qlock = this.qlock;

         SynchronousQueue.Node node;
         do {
            qlock.lock();

            try {
               node = this.waitingConsumers.deq();
            } finally {
               qlock.unlock();
            }

            if (node == null) {
               return false;
            }
         } while(!node.setItem(e));

         return true;
      }
   }

   public Object poll() {
      ReentrantLock qlock = this.qlock;

      Object x;
      do {
         qlock.lock();

         SynchronousQueue.Node node;
         try {
            node = this.waitingProducers.deq();
         } finally {
            qlock.unlock();
         }

         if (node == null) {
            return null;
         }

         x = node.getItem();
      } while(x == null);

      return x;
   }

   public boolean isEmpty() {
      return true;
   }

   public int size() {
      return 0;
   }

   public int remainingCapacity() {
      return 0;
   }

   public void clear() {
   }

   public boolean contains(Object o) {
      return false;
   }

   public boolean remove(Object o) {
      return false;
   }

   public boolean containsAll(Collection c) {
      return c.isEmpty();
   }

   public boolean removeAll(Collection c) {
      return false;
   }

   public boolean retainAll(Collection c) {
      return false;
   }

   public Object peek() {
      return null;
   }

   public Iterator iterator() {
      return new SynchronousQueue.EmptyIterator();
   }

   public Object[] toArray() {
      return new Object[0];
   }

   public Object[] toArray(Object[] a) {
      if (a.length > 0) {
         a[0] = null;
      }

      return a;
   }

   public int drainTo(Collection c) {
      if (c == null) {
         throw new NullPointerException();
      } else if (c == this) {
         throw new IllegalArgumentException();
      } else {
         int n;
         Object e;
         for(n = 0; (e = this.poll()) != null; ++n) {
            c.add(e);
         }

         return n;
      }
   }

   public int drainTo(Collection c, int maxElements) {
      if (c == null) {
         throw new NullPointerException();
      } else if (c == this) {
         throw new IllegalArgumentException();
      } else {
         int n;
         Object e;
         for(n = 0; n < maxElements && (e = this.poll()) != null; ++n) {
            c.add(e);
         }

         return n;
      }
   }

   static class EmptyIterator implements Iterator {
      public boolean hasNext() {
         return false;
      }

      public Object next() {
         throw new NoSuchElementException();
      }

      public void remove() {
         throw new IllegalStateException();
      }
   }

   static final class Node implements Serializable {
      private static final long serialVersionUID = -3223113410248163686L;
      private static final int ACK = 1;
      private static final int CANCEL = -1;
      int state = 0;
      Object item;
      SynchronousQueue.Node next;

      Node(Object x) {
         this.item = x;
      }

      Node(Object x, SynchronousQueue.Node n) {
         this.item = x;
         this.next = n;
      }

      private Object extract() {
         Object x = this.item;
         this.item = null;
         return x;
      }

      private void checkCancellationOnInterrupt(InterruptedException ie) throws InterruptedException {
         if (this.state == 0) {
            this.state = -1;
            this.notify();
            throw ie;
         } else {
            Thread.currentThread().interrupt();
         }
      }

      synchronized boolean setItem(Object x) {
         if (this.state != 0) {
            return false;
         } else {
            this.item = x;
            this.state = 1;
            this.notify();
            return true;
         }
      }

      synchronized Object getItem() {
         if (this.state != 0) {
            return null;
         } else {
            this.state = 1;
            this.notify();
            return this.extract();
         }
      }

      synchronized void waitForTake() throws InterruptedException {
         while(true) {
            try {
               if (this.state == 0) {
                  this.wait();
                  continue;
               }
            } catch (InterruptedException var2) {
               this.checkCancellationOnInterrupt(var2);
            }

            return;
         }
      }

      synchronized Object waitForPut() throws InterruptedException {
         while(true) {
            try {
               if (this.state == 0) {
                  this.wait();
                  continue;
               }
            } catch (InterruptedException var2) {
               this.checkCancellationOnInterrupt(var2);
            }

            return this.extract();
         }
      }

      private boolean attempt(long nanos) throws InterruptedException {
         if (this.state != 0) {
            return true;
         } else if (nanos <= 0L) {
            this.state = -1;
            this.notify();
            return false;
         } else {
            long deadline = Utils.nanoTime() + nanos;

            do {
               TimeUnit.NANOSECONDS.timedWait(this, nanos);
               if (this.state != 0) {
                  return true;
               }

               nanos = deadline - Utils.nanoTime();
            } while(nanos > 0L);

            this.state = -1;
            this.notify();
            return false;
         }
      }

      synchronized boolean waitForTake(long nanos) throws InterruptedException {
         try {
            if (!this.attempt(nanos)) {
               return false;
            }
         } catch (InterruptedException var4) {
            this.checkCancellationOnInterrupt(var4);
         }

         return true;
      }

      synchronized Object waitForPut(long nanos) throws InterruptedException {
         try {
            if (!this.attempt(nanos)) {
               return null;
            }
         } catch (InterruptedException var4) {
            this.checkCancellationOnInterrupt(var4);
         }

         return this.extract();
      }
   }

   static final class LifoWaitQueue extends SynchronousQueue.WaitQueue implements Serializable {
      private static final long serialVersionUID = -3633113410248163686L;
      private transient SynchronousQueue.Node head;

      SynchronousQueue.Node enq(Object x) {
         return this.head = new SynchronousQueue.Node(x, this.head);
      }

      SynchronousQueue.Node deq() {
         SynchronousQueue.Node p = this.head;
         if (p != null) {
            this.head = p.next;
            p.next = null;
         }

         return p;
      }

      boolean shouldUnlink(SynchronousQueue.Node node) {
         return node == this.head || node.next != null;
      }

      void unlink(SynchronousQueue.Node node) {
         SynchronousQueue.Node p = this.head;

         for(SynchronousQueue.Node trail = null; p != null; p = p.next) {
            if (p == node) {
               SynchronousQueue.Node next = p.next;
               if (trail == null) {
                  this.head = next;
               } else {
                  trail.next = next;
               }
               break;
            }

            trail = p;
         }

      }
   }

   static final class FifoWaitQueue extends SynchronousQueue.WaitQueue implements Serializable {
      private static final long serialVersionUID = -3623113410248163686L;
      private transient SynchronousQueue.Node head;
      private transient SynchronousQueue.Node last;

      SynchronousQueue.Node enq(Object x) {
         SynchronousQueue.Node p = new SynchronousQueue.Node(x);
         if (this.last == null) {
            this.last = this.head = p;
         } else {
            this.last = this.last.next = p;
         }

         return p;
      }

      SynchronousQueue.Node deq() {
         SynchronousQueue.Node p = this.head;
         if (p != null) {
            if ((this.head = p.next) == null) {
               this.last = null;
            }

            p.next = null;
         }

         return p;
      }

      boolean shouldUnlink(SynchronousQueue.Node node) {
         return node == this.last || node.next != null;
      }

      void unlink(SynchronousQueue.Node node) {
         SynchronousQueue.Node p = this.head;

         for(SynchronousQueue.Node trail = null; p != null; p = p.next) {
            if (p == node) {
               SynchronousQueue.Node next = p.next;
               if (trail == null) {
                  this.head = next;
               } else {
                  trail.next = next;
               }

               if (this.last == node) {
                  this.last = trail;
               }
               break;
            }

            trail = p;
         }

      }
   }

   abstract static class WaitQueue implements Serializable {
      abstract SynchronousQueue.Node enq(Object var1);

      abstract SynchronousQueue.Node deq();

      abstract void unlink(SynchronousQueue.Node var1);

      abstract boolean shouldUnlink(SynchronousQueue.Node var1);
   }
}

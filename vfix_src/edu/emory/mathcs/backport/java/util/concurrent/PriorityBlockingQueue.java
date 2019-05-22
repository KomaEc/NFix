package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.AbstractQueue;
import edu.emory.mathcs.backport.java.util.PriorityQueue;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Condition;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PriorityBlockingQueue extends AbstractQueue implements BlockingQueue, Serializable {
   private static final long serialVersionUID = 5595510919245408276L;
   private final PriorityQueue q;
   private final ReentrantLock lock = new ReentrantLock(true);
   private final Condition notEmpty;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public PriorityBlockingQueue() {
      this.notEmpty = this.lock.newCondition();
      this.q = new PriorityQueue();
   }

   public PriorityBlockingQueue(int initialCapacity) {
      this.notEmpty = this.lock.newCondition();
      this.q = new PriorityQueue(initialCapacity, (Comparator)null);
   }

   public PriorityBlockingQueue(int initialCapacity, Comparator comparator) {
      this.notEmpty = this.lock.newCondition();
      this.q = new PriorityQueue(initialCapacity, comparator);
   }

   public PriorityBlockingQueue(Collection c) {
      this.notEmpty = this.lock.newCondition();
      this.q = new PriorityQueue(c);
   }

   public boolean add(Object e) {
      return this.offer(e);
   }

   public boolean offer(Object e) {
      ReentrantLock lock = this.lock;
      lock.lock();

      boolean var4;
      try {
         boolean ok = this.q.offer(e);
         if (!$assertionsDisabled && !ok) {
            throw new AssertionError();
         }

         this.notEmpty.signal();
         var4 = true;
      } finally {
         lock.unlock();
      }

      return var4;
   }

   public void put(Object e) {
      this.offer(e);
   }

   public boolean offer(Object e, long timeout, TimeUnit unit) {
      return this.offer(e);
   }

   public Object poll() {
      ReentrantLock lock = this.lock;
      lock.lock();

      Object var2;
      try {
         var2 = this.q.poll();
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public Object take() throws InterruptedException {
      ReentrantLock lock = this.lock;
      lock.lockInterruptibly();

      Object var3;
      try {
         try {
            while(this.q.size() == 0) {
               this.notEmpty.await();
            }
         } catch (InterruptedException var7) {
            this.notEmpty.signal();
            throw var7;
         }

         Object x = this.q.poll();
         if (!$assertionsDisabled && x == null) {
            throw new AssertionError();
         }

         var3 = x;
      } finally {
         lock.unlock();
      }

      return var3;
   }

   public Object poll(long timeout, TimeUnit unit) throws InterruptedException {
      long nanos = unit.toNanos(timeout);
      ReentrantLock lock = this.lock;
      lock.lockInterruptibly();

      try {
         long deadline = Utils.nanoTime() + nanos;

         while(true) {
            Object x = this.q.poll();
            Object var10;
            if (x != null) {
               var10 = x;
               return var10;
            }

            if (nanos <= 0L) {
               var10 = null;
               return var10;
            }

            try {
               this.notEmpty.await(nanos, TimeUnit.NANOSECONDS);
               nanos = deadline - Utils.nanoTime();
            } catch (InterruptedException var14) {
               this.notEmpty.signal();
               throw var14;
            }
         }
      } finally {
         lock.unlock();
      }
   }

   public Object peek() {
      ReentrantLock lock = this.lock;
      lock.lock();

      Object var2;
      try {
         var2 = this.q.peek();
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public Comparator comparator() {
      return this.q.comparator();
   }

   public int size() {
      ReentrantLock lock = this.lock;
      lock.lock();

      int var2;
      try {
         var2 = this.q.size();
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public int remainingCapacity() {
      return Integer.MAX_VALUE;
   }

   public boolean remove(Object o) {
      ReentrantLock lock = this.lock;
      lock.lock();

      boolean var3;
      try {
         var3 = this.q.remove(o);
      } finally {
         lock.unlock();
      }

      return var3;
   }

   public boolean contains(Object o) {
      ReentrantLock lock = this.lock;
      lock.lock();

      boolean var3;
      try {
         var3 = this.q.contains(o);
      } finally {
         lock.unlock();
      }

      return var3;
   }

   public Object[] toArray() {
      ReentrantLock lock = this.lock;
      lock.lock();

      Object[] var2;
      try {
         var2 = this.q.toArray();
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public String toString() {
      ReentrantLock lock = this.lock;
      lock.lock();

      String var2;
      try {
         var2 = this.q.toString();
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public int drainTo(Collection c) {
      if (c == null) {
         throw new NullPointerException();
      } else if (c == this) {
         throw new IllegalArgumentException();
      } else {
         ReentrantLock lock = this.lock;
         lock.lock();

         try {
            int n;
            Object e;
            for(n = 0; (e = this.q.poll()) != null; ++n) {
               c.add(e);
            }

            int var5 = n;
            return var5;
         } finally {
            lock.unlock();
         }
      }
   }

   public int drainTo(Collection c, int maxElements) {
      if (c == null) {
         throw new NullPointerException();
      } else if (c == this) {
         throw new IllegalArgumentException();
      } else if (maxElements <= 0) {
         return 0;
      } else {
         ReentrantLock lock = this.lock;
         lock.lock();

         try {
            int n;
            Object e;
            for(n = 0; n < maxElements && (e = this.q.poll()) != null; ++n) {
               c.add(e);
            }

            int var6 = n;
            return var6;
         } finally {
            lock.unlock();
         }
      }
   }

   public void clear() {
      ReentrantLock lock = this.lock;
      lock.lock();

      try {
         this.q.clear();
      } finally {
         lock.unlock();
      }

   }

   public Object[] toArray(Object[] a) {
      ReentrantLock lock = this.lock;
      lock.lock();

      Object[] var3;
      try {
         var3 = this.q.toArray(a);
      } finally {
         lock.unlock();
      }

      return var3;
   }

   public Iterator iterator() {
      return new PriorityBlockingQueue.Itr(this.toArray());
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      this.lock.lock();

      try {
         s.defaultWriteObject();
      } finally {
         this.lock.unlock();
      }

   }

   static {
      $assertionsDisabled = !PriorityBlockingQueue.class.desiredAssertionStatus();
   }

   private class Itr implements Iterator {
      final Object[] array;
      int cursor;
      int lastRet = -1;

      Itr(Object[] array) {
         this.array = array;
      }

      public boolean hasNext() {
         return this.cursor < this.array.length;
      }

      public Object next() {
         if (this.cursor >= this.array.length) {
            throw new NoSuchElementException();
         } else {
            this.lastRet = this.cursor;
            return this.array[this.cursor++];
         }
      }

      public void remove() {
         if (this.lastRet < 0) {
            throw new IllegalStateException();
         } else {
            Object x = this.array[this.lastRet];
            this.lastRet = -1;
            PriorityBlockingQueue.this.lock.lock();

            try {
               Iterator it = PriorityBlockingQueue.this.q.iterator();

               do {
                  if (!it.hasNext()) {
                     return;
                  }
               } while(it.next() != x);

               it.remove();
            } finally {
               PriorityBlockingQueue.this.lock.unlock();
            }

         }
      }
   }
}

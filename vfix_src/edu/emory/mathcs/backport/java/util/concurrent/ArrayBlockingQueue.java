package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.AbstractQueue;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Condition;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayBlockingQueue extends AbstractQueue implements BlockingQueue, Serializable {
   private static final long serialVersionUID = -817911632652898426L;
   private final Object[] items;
   private int takeIndex;
   private int putIndex;
   private int count;
   private final ReentrantLock lock;
   private final Condition notEmpty;
   private final Condition notFull;

   final int inc(int i) {
      ++i;
      return i == this.items.length ? 0 : i;
   }

   private void insert(Object x) {
      this.items[this.putIndex] = x;
      this.putIndex = this.inc(this.putIndex);
      ++this.count;
      this.notEmpty.signal();
   }

   private Object extract() {
      Object[] items = this.items;
      Object x = items[this.takeIndex];
      items[this.takeIndex] = null;
      this.takeIndex = this.inc(this.takeIndex);
      --this.count;
      this.notFull.signal();
      return x;
   }

   void removeAt(int i) {
      Object[] items = this.items;
      if (i == this.takeIndex) {
         items[this.takeIndex] = null;
         this.takeIndex = this.inc(this.takeIndex);
      } else {
         while(true) {
            int nexti = this.inc(i);
            if (nexti == this.putIndex) {
               items[i] = null;
               this.putIndex = i;
               break;
            }

            items[i] = items[nexti];
            i = nexti;
         }
      }

      --this.count;
      this.notFull.signal();
   }

   public ArrayBlockingQueue(int capacity) {
      this(capacity, false);
   }

   public ArrayBlockingQueue(int capacity, boolean fair) {
      if (capacity <= 0) {
         throw new IllegalArgumentException();
      } else {
         this.items = new Object[capacity];
         this.lock = new ReentrantLock(fair);
         this.notEmpty = this.lock.newCondition();
         this.notFull = this.lock.newCondition();
      }
   }

   public ArrayBlockingQueue(int capacity, boolean fair, Collection c) {
      this(capacity, fair);
      if (capacity < c.size()) {
         throw new IllegalArgumentException();
      } else {
         Iterator it = c.iterator();

         while(it.hasNext()) {
            this.add(it.next());
         }

      }
   }

   public boolean add(Object e) {
      return super.add(e);
   }

   public boolean offer(Object e) {
      if (e == null) {
         throw new NullPointerException();
      } else {
         ReentrantLock lock = this.lock;
         lock.lock();

         boolean var3;
         try {
            if (this.count == this.items.length) {
               var3 = false;
               return var3;
            }

            this.insert(e);
            var3 = true;
         } finally {
            lock.unlock();
         }

         return var3;
      }
   }

   public void put(Object e) throws InterruptedException {
      if (e == null) {
         throw new NullPointerException();
      } else {
         Object[] items = this.items;
         ReentrantLock lock = this.lock;
         lock.lockInterruptibly();

         try {
            try {
               while(this.count == items.length) {
                  this.notFull.await();
               }
            } catch (InterruptedException var8) {
               this.notFull.signal();
               throw var8;
            }

            this.insert(e);
         } finally {
            lock.unlock();
         }

      }
   }

   public boolean offer(Object e, long timeout, TimeUnit unit) throws InterruptedException {
      if (e == null) {
         throw new NullPointerException();
      } else {
         long nanos = unit.toNanos(timeout);
         ReentrantLock lock = this.lock;
         lock.lockInterruptibly();

         try {
            long deadline = Utils.nanoTime() + nanos;

            boolean var10;
            while(this.count == this.items.length) {
               if (nanos <= 0L) {
                  var10 = false;
                  return var10;
               }

               try {
                  this.notFull.await(nanos, TimeUnit.NANOSECONDS);
                  nanos = deadline - Utils.nanoTime();
               } catch (InterruptedException var14) {
                  this.notFull.signal();
                  throw var14;
               }
            }

            this.insert(e);
            var10 = true;
            return var10;
         } finally {
            lock.unlock();
         }
      }
   }

   public Object poll() {
      ReentrantLock lock = this.lock;
      lock.lock();

      Object var3;
      try {
         Object x;
         if (this.count == 0) {
            x = null;
            return x;
         }

         x = this.extract();
         var3 = x;
      } finally {
         lock.unlock();
      }

      return var3;
   }

   public Object take() throws InterruptedException {
      ReentrantLock lock = this.lock;
      lock.lockInterruptibly();

      Object var3;
      try {
         try {
            while(this.count == 0) {
               this.notEmpty.await();
            }
         } catch (InterruptedException var7) {
            this.notEmpty.signal();
            throw var7;
         }

         Object x = this.extract();
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

         Object x;
         while(this.count == 0) {
            if (nanos <= 0L) {
               x = null;
               return x;
            }

            try {
               this.notEmpty.await(nanos, TimeUnit.NANOSECONDS);
               nanos = deadline - Utils.nanoTime();
            } catch (InterruptedException var14) {
               this.notEmpty.signal();
               throw var14;
            }
         }

         x = this.extract();
         Object var10 = x;
         return var10;
      } finally {
         lock.unlock();
      }
   }

   public Object peek() {
      ReentrantLock lock = this.lock;
      lock.lock();

      Object var2;
      try {
         var2 = this.count == 0 ? null : this.items[this.takeIndex];
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public int size() {
      ReentrantLock lock = this.lock;
      lock.lock();

      int var2;
      try {
         var2 = this.count;
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public int remainingCapacity() {
      ReentrantLock lock = this.lock;
      lock.lock();

      int var2;
      try {
         var2 = this.items.length - this.count;
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public boolean remove(Object o) {
      if (o == null) {
         return false;
      } else {
         Object[] items = this.items;
         ReentrantLock lock = this.lock;
         lock.lock();

         boolean var6;
         try {
            int i = this.takeIndex;

            for(int var5 = 0; var5++ < this.count; i = this.inc(i)) {
               if (o.equals(items[i])) {
                  this.removeAt(i);
                  var6 = true;
                  return var6;
               }
            }

            var6 = false;
         } finally {
            lock.unlock();
         }

         return var6;
      }
   }

   public boolean contains(Object o) {
      if (o == null) {
         return false;
      } else {
         Object[] items = this.items;
         ReentrantLock lock = this.lock;
         lock.lock();

         boolean var6;
         try {
            int i = this.takeIndex;

            for(int var5 = 0; var5++ < this.count; i = this.inc(i)) {
               if (o.equals(items[i])) {
                  var6 = true;
                  return var6;
               }
            }

            var6 = false;
         } finally {
            lock.unlock();
         }

         return var6;
      }
   }

   public Object[] toArray() {
      Object[] items = this.items;
      ReentrantLock lock = this.lock;
      lock.lock();

      try {
         Object[] a = new Object[this.count];
         int k = 0;

         for(int i = this.takeIndex; k < this.count; i = this.inc(i)) {
            a[k++] = items[i];
         }

         Object[] var6 = a;
         return var6;
      } finally {
         lock.unlock();
      }
   }

   public Object[] toArray(Object[] a) {
      Object[] items = this.items;
      ReentrantLock lock = this.lock;
      lock.lock();

      Object[] var6;
      try {
         if (a.length < this.count) {
            a = (Object[])Array.newInstance(a.getClass().getComponentType(), this.count);
         }

         int k = 0;

         for(int i = this.takeIndex; k < this.count; i = this.inc(i)) {
            a[k++] = items[i];
         }

         if (a.length > this.count) {
            a[this.count] = null;
         }

         var6 = a;
      } finally {
         lock.unlock();
      }

      return var6;
   }

   public String toString() {
      ReentrantLock lock = this.lock;
      lock.lock();

      String var2;
      try {
         var2 = super.toString();
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public void clear() {
      Object[] items = this.items;
      ReentrantLock lock = this.lock;
      lock.lock();

      try {
         int i = this.takeIndex;

         for(int k = this.count; k-- > 0; i = this.inc(i)) {
            items[i] = null;
         }

         this.count = 0;
         this.putIndex = 0;
         this.takeIndex = 0;
         this.notFull.signalAll();
      } finally {
         lock.unlock();
      }
   }

   public int drainTo(Collection c) {
      if (c == null) {
         throw new NullPointerException();
      } else if (c == this) {
         throw new IllegalArgumentException();
      } else {
         Object[] items = this.items;
         ReentrantLock lock = this.lock;
         lock.lock();

         try {
            int i = this.takeIndex;
            int n = 0;

            for(int max = this.count; n < max; ++n) {
               c.add(items[i]);
               items[i] = null;
               i = this.inc(i);
            }

            if (n > 0) {
               this.count = 0;
               this.putIndex = 0;
               this.takeIndex = 0;
               this.notFull.signalAll();
            }

            int var7 = n;
            return var7;
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
         Object[] items = this.items;
         ReentrantLock lock = this.lock;
         lock.lock();

         int var9;
         try {
            int i = this.takeIndex;
            int n = 0;
            int sz = this.count;

            for(int max = maxElements < this.count ? maxElements : this.count; n < max; ++n) {
               c.add(items[i]);
               items[i] = null;
               i = this.inc(i);
            }

            if (n > 0) {
               this.count -= n;
               this.takeIndex = i;
               this.notFull.signalAll();
            }

            var9 = n;
         } finally {
            lock.unlock();
         }

         return var9;
      }
   }

   public Iterator iterator() {
      ReentrantLock lock = this.lock;
      lock.lock();

      ArrayBlockingQueue.Itr var2;
      try {
         var2 = new ArrayBlockingQueue.Itr();
      } finally {
         lock.unlock();
      }

      return var2;
   }

   private class Itr implements Iterator {
      private int nextIndex;
      private Object nextItem;
      private int lastRet = -1;

      Itr() {
         if (ArrayBlockingQueue.this.count == 0) {
            this.nextIndex = -1;
         } else {
            this.nextIndex = ArrayBlockingQueue.this.takeIndex;
            this.nextItem = ArrayBlockingQueue.this.items[ArrayBlockingQueue.this.takeIndex];
         }

      }

      public boolean hasNext() {
         return this.nextIndex >= 0;
      }

      private void checkNext() {
         if (this.nextIndex == ArrayBlockingQueue.this.putIndex) {
            this.nextIndex = -1;
            this.nextItem = null;
         } else {
            this.nextItem = ArrayBlockingQueue.this.items[this.nextIndex];
            if (this.nextItem == null) {
               this.nextIndex = -1;
            }
         }

      }

      public Object next() {
         ReentrantLock lock = ArrayBlockingQueue.this.lock;
         lock.lock();

         Object var3;
         try {
            if (this.nextIndex < 0) {
               throw new NoSuchElementException();
            }

            this.lastRet = this.nextIndex;
            Object x = this.nextItem;
            this.nextIndex = ArrayBlockingQueue.this.inc(this.nextIndex);
            this.checkNext();
            var3 = x;
         } finally {
            lock.unlock();
         }

         return var3;
      }

      public void remove() {
         ReentrantLock lock = ArrayBlockingQueue.this.lock;
         lock.lock();

         try {
            int i = this.lastRet;
            if (i == -1) {
               throw new IllegalStateException();
            }

            this.lastRet = -1;
            int ti = ArrayBlockingQueue.this.takeIndex;
            ArrayBlockingQueue.this.removeAt(i);
            this.nextIndex = i == ti ? ArrayBlockingQueue.this.takeIndex : i;
            this.checkNext();
         } finally {
            lock.unlock();
         }

      }
   }
}

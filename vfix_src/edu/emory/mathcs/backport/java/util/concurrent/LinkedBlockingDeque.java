package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.AbstractQueue;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Condition;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedBlockingDeque extends AbstractQueue implements BlockingDeque, Serializable {
   private static final long serialVersionUID = -387911632671998426L;
   private transient LinkedBlockingDeque.Node first;
   private transient LinkedBlockingDeque.Node last;
   private transient int count;
   private final int capacity;
   private final ReentrantLock lock;
   private final Condition notEmpty;
   private final Condition notFull;

   public LinkedBlockingDeque() {
      this(Integer.MAX_VALUE);
   }

   public LinkedBlockingDeque(int capacity) {
      this.lock = new ReentrantLock();
      this.notEmpty = this.lock.newCondition();
      this.notFull = this.lock.newCondition();
      if (capacity <= 0) {
         throw new IllegalArgumentException();
      } else {
         this.capacity = capacity;
      }
   }

   public LinkedBlockingDeque(Collection c) {
      this(Integer.MAX_VALUE);
      Iterator itr = c.iterator();

      while(itr.hasNext()) {
         Object e = itr.next();
         this.add(e);
      }

   }

   private boolean linkFirst(Object e) {
      if (this.count >= this.capacity) {
         return false;
      } else {
         ++this.count;
         LinkedBlockingDeque.Node f = this.first;
         LinkedBlockingDeque.Node x = new LinkedBlockingDeque.Node(e, (LinkedBlockingDeque.Node)null, f);
         this.first = x;
         if (this.last == null) {
            this.last = x;
         } else {
            f.prev = x;
         }

         this.notEmpty.signal();
         return true;
      }
   }

   private boolean linkLast(Object e) {
      if (this.count >= this.capacity) {
         return false;
      } else {
         ++this.count;
         LinkedBlockingDeque.Node l = this.last;
         LinkedBlockingDeque.Node x = new LinkedBlockingDeque.Node(e, l, (LinkedBlockingDeque.Node)null);
         this.last = x;
         if (this.first == null) {
            this.first = x;
         } else {
            l.next = x;
         }

         this.notEmpty.signal();
         return true;
      }
   }

   private Object unlinkFirst() {
      LinkedBlockingDeque.Node f = this.first;
      if (f == null) {
         return null;
      } else {
         LinkedBlockingDeque.Node n = f.next;
         this.first = n;
         if (n == null) {
            this.last = null;
         } else {
            n.prev = null;
         }

         --this.count;
         this.notFull.signal();
         return f.item;
      }
   }

   private Object unlinkLast() {
      LinkedBlockingDeque.Node l = this.last;
      if (l == null) {
         return null;
      } else {
         LinkedBlockingDeque.Node p = l.prev;
         this.last = p;
         if (p == null) {
            this.first = null;
         } else {
            p.next = null;
         }

         --this.count;
         this.notFull.signal();
         return l.item;
      }
   }

   private void unlink(LinkedBlockingDeque.Node x) {
      LinkedBlockingDeque.Node p = x.prev;
      LinkedBlockingDeque.Node n = x.next;
      if (p == null) {
         if (n == null) {
            this.first = this.last = null;
         } else {
            n.prev = null;
            this.first = n;
         }
      } else if (n == null) {
         p.next = null;
         this.last = p;
      } else {
         p.next = n;
         n.prev = p;
      }

      --this.count;
      this.notFull.signalAll();
   }

   public void addFirst(Object e) {
      if (!this.offerFirst(e)) {
         throw new IllegalStateException("Deque full");
      }
   }

   public void addLast(Object e) {
      if (!this.offerLast(e)) {
         throw new IllegalStateException("Deque full");
      }
   }

   public boolean offerFirst(Object e) {
      if (e == null) {
         throw new NullPointerException();
      } else {
         this.lock.lock();

         boolean var2;
         try {
            var2 = this.linkFirst(e);
         } finally {
            this.lock.unlock();
         }

         return var2;
      }
   }

   public boolean offerLast(Object e) {
      if (e == null) {
         throw new NullPointerException();
      } else {
         this.lock.lock();

         boolean var2;
         try {
            var2 = this.linkLast(e);
         } finally {
            this.lock.unlock();
         }

         return var2;
      }
   }

   public void putFirst(Object e) throws InterruptedException {
      if (e == null) {
         throw new NullPointerException();
      } else {
         this.lock.lock();

         try {
            while(!this.linkFirst(e)) {
               this.notFull.await();
            }
         } finally {
            this.lock.unlock();
         }

      }
   }

   public void putLast(Object e) throws InterruptedException {
      if (e == null) {
         throw new NullPointerException();
      } else {
         this.lock.lock();

         try {
            while(!this.linkLast(e)) {
               this.notFull.await();
            }
         } finally {
            this.lock.unlock();
         }

      }
   }

   public boolean offerFirst(Object e, long timeout, TimeUnit unit) throws InterruptedException {
      if (e == null) {
         throw new NullPointerException();
      } else {
         long nanos = unit.toNanos(timeout);
         long deadline = Utils.nanoTime() + nanos;
         this.lock.lockInterruptibly();

         try {
            boolean var9;
            while(!this.linkFirst(e)) {
               if (nanos <= 0L) {
                  var9 = false;
                  return var9;
               }

               this.notFull.await(nanos, TimeUnit.NANOSECONDS);
               nanos = deadline - Utils.nanoTime();
            }

            var9 = true;
            return var9;
         } finally {
            this.lock.unlock();
         }
      }
   }

   public boolean offerLast(Object e, long timeout, TimeUnit unit) throws InterruptedException {
      if (e == null) {
         throw new NullPointerException();
      } else {
         long nanos = unit.toNanos(timeout);
         long deadline = Utils.nanoTime() + nanos;
         this.lock.lockInterruptibly();

         try {
            boolean var9;
            while(!this.linkLast(e)) {
               if (nanos <= 0L) {
                  var9 = false;
                  return var9;
               }

               this.notFull.await(nanos, TimeUnit.NANOSECONDS);
               nanos = deadline - Utils.nanoTime();
            }

            var9 = true;
            return var9;
         } finally {
            this.lock.unlock();
         }
      }
   }

   public Object removeFirst() {
      Object x = this.pollFirst();
      if (x == null) {
         throw new NoSuchElementException();
      } else {
         return x;
      }
   }

   public Object removeLast() {
      Object x = this.pollLast();
      if (x == null) {
         throw new NoSuchElementException();
      } else {
         return x;
      }
   }

   public Object pollFirst() {
      this.lock.lock();

      Object var1;
      try {
         var1 = this.unlinkFirst();
      } finally {
         this.lock.unlock();
      }

      return var1;
   }

   public Object pollLast() {
      this.lock.lock();

      Object var1;
      try {
         var1 = this.unlinkLast();
      } finally {
         this.lock.unlock();
      }

      return var1;
   }

   public Object takeFirst() throws InterruptedException {
      this.lock.lock();

      Object var2;
      try {
         Object x;
         while((x = this.unlinkFirst()) == null) {
            this.notEmpty.await();
         }

         var2 = x;
      } finally {
         this.lock.unlock();
      }

      return var2;
   }

   public Object takeLast() throws InterruptedException {
      this.lock.lock();

      Object var2;
      try {
         Object x;
         while((x = this.unlinkLast()) == null) {
            this.notEmpty.await();
         }

         var2 = x;
      } finally {
         this.lock.unlock();
      }

      return var2;
   }

   public Object pollFirst(long timeout, TimeUnit unit) throws InterruptedException {
      long nanos = unit.toNanos(timeout);
      long deadline = Utils.nanoTime() + nanos;
      this.lock.lockInterruptibly();

      try {
         while(true) {
            Object x = this.unlinkFirst();
            Object var9;
            if (x != null) {
               var9 = x;
               return var9;
            }

            if (nanos <= 0L) {
               var9 = null;
               return var9;
            }

            this.notEmpty.await(nanos, TimeUnit.NANOSECONDS);
            nanos = deadline - Utils.nanoTime();
         }
      } finally {
         this.lock.unlock();
      }
   }

   public Object pollLast(long timeout, TimeUnit unit) throws InterruptedException {
      long nanos = unit.toNanos(timeout);
      long deadline = Utils.nanoTime() + nanos;
      this.lock.lockInterruptibly();

      try {
         while(true) {
            Object x = this.unlinkLast();
            Object var9;
            if (x != null) {
               var9 = x;
               return var9;
            }

            if (nanos <= 0L) {
               var9 = null;
               return var9;
            }

            this.notEmpty.await(nanos, TimeUnit.NANOSECONDS);
            nanos = deadline - Utils.nanoTime();
         }
      } finally {
         this.lock.unlock();
      }
   }

   public Object getFirst() {
      Object x = this.peekFirst();
      if (x == null) {
         throw new NoSuchElementException();
      } else {
         return x;
      }
   }

   public Object getLast() {
      Object x = this.peekLast();
      if (x == null) {
         throw new NoSuchElementException();
      } else {
         return x;
      }
   }

   public Object peekFirst() {
      this.lock.lock();

      Object var1;
      try {
         var1 = this.first == null ? null : this.first.item;
      } finally {
         this.lock.unlock();
      }

      return var1;
   }

   public Object peekLast() {
      this.lock.lock();

      Object var1;
      try {
         var1 = this.last == null ? null : this.last.item;
      } finally {
         this.lock.unlock();
      }

      return var1;
   }

   public boolean removeFirstOccurrence(Object o) {
      if (o == null) {
         return false;
      } else {
         this.lock.lock();

         boolean var7;
         try {
            for(LinkedBlockingDeque.Node p = this.first; p != null; p = p.next) {
               if (o.equals(p.item)) {
                  this.unlink(p);
                  boolean var3 = true;
                  return var3;
               }
            }

            var7 = false;
         } finally {
            this.lock.unlock();
         }

         return var7;
      }
   }

   public boolean removeLastOccurrence(Object o) {
      if (o == null) {
         return false;
      } else {
         this.lock.lock();

         boolean var7;
         try {
            for(LinkedBlockingDeque.Node p = this.last; p != null; p = p.prev) {
               if (o.equals(p.item)) {
                  this.unlink(p);
                  boolean var3 = true;
                  return var3;
               }
            }

            var7 = false;
         } finally {
            this.lock.unlock();
         }

         return var7;
      }
   }

   public boolean add(Object e) {
      this.addLast(e);
      return true;
   }

   public boolean offer(Object e) {
      return this.offerLast(e);
   }

   public void put(Object e) throws InterruptedException {
      this.putLast(e);
   }

   public boolean offer(Object e, long timeout, TimeUnit unit) throws InterruptedException {
      return this.offerLast(e, timeout, unit);
   }

   public Object remove() {
      return this.removeFirst();
   }

   public Object poll() {
      return this.pollFirst();
   }

   public Object take() throws InterruptedException {
      return this.takeFirst();
   }

   public Object poll(long timeout, TimeUnit unit) throws InterruptedException {
      return this.pollFirst(timeout, unit);
   }

   public Object element() {
      return this.getFirst();
   }

   public Object peek() {
      return this.peekFirst();
   }

   public int remainingCapacity() {
      this.lock.lock();

      int var1;
      try {
         var1 = this.capacity - this.count;
      } finally {
         this.lock.unlock();
      }

      return var1;
   }

   public int drainTo(Collection c) {
      if (c == null) {
         throw new NullPointerException();
      } else if (c == this) {
         throw new IllegalArgumentException();
      } else {
         this.lock.lock();

         try {
            for(LinkedBlockingDeque.Node p = this.first; p != null; p = p.next) {
               c.add(p.item);
            }

            int n = this.count;
            this.count = 0;
            this.first = this.last = null;
            this.notFull.signalAll();
            int var3 = n;
            return var3;
         } finally {
            this.lock.unlock();
         }
      }
   }

   public int drainTo(Collection c, int maxElements) {
      if (c == null) {
         throw new NullPointerException();
      } else if (c == this) {
         throw new IllegalArgumentException();
      } else {
         this.lock.lock();

         try {
            int n;
            for(n = 0; n < maxElements && this.first != null; ++n) {
               c.add(this.first.item);
               this.first.prev = null;
               this.first = this.first.next;
               --this.count;
            }

            if (this.first == null) {
               this.last = null;
            }

            this.notFull.signalAll();
            int var4 = n;
            return var4;
         } finally {
            this.lock.unlock();
         }
      }
   }

   public void push(Object e) {
      this.addFirst(e);
   }

   public Object pop() {
      return this.removeFirst();
   }

   public boolean remove(Object o) {
      return this.removeFirstOccurrence(o);
   }

   public int size() {
      this.lock.lock();

      int var1;
      try {
         var1 = this.count;
      } finally {
         this.lock.unlock();
      }

      return var1;
   }

   public boolean contains(Object o) {
      if (o == null) {
         return false;
      } else {
         this.lock.lock();

         try {
            for(LinkedBlockingDeque.Node p = this.first; p != null; p = p.next) {
               if (o.equals(p.item)) {
                  boolean var3 = true;
                  return var3;
               }
            }

            boolean var7 = false;
            return var7;
         } finally {
            this.lock.unlock();
         }
      }
   }

   boolean removeNode(LinkedBlockingDeque.Node e) {
      this.lock.lock();

      try {
         for(LinkedBlockingDeque.Node p = this.first; p != null; p = p.next) {
            if (p == e) {
               this.unlink(p);
               boolean var3 = true;
               return var3;
            }
         }

         boolean var7 = false;
         return var7;
      } finally {
         this.lock.unlock();
      }
   }

   public Object[] toArray() {
      this.lock.lock();

      try {
         Object[] a = new Object[this.count];
         int k = 0;

         for(LinkedBlockingDeque.Node p = this.first; p != null; p = p.next) {
            a[k++] = p.item;
         }

         Object[] var7 = a;
         return var7;
      } finally {
         this.lock.unlock();
      }
   }

   public Object[] toArray(Object[] a) {
      this.lock.lock();

      Object[] var7;
      try {
         if (a.length < this.count) {
            a = (Object[])Array.newInstance(a.getClass().getComponentType(), this.count);
         }

         int k = 0;

         for(LinkedBlockingDeque.Node p = this.first; p != null; p = p.next) {
            a[k++] = p.item;
         }

         if (a.length > k) {
            a[k] = null;
         }

         var7 = a;
      } finally {
         this.lock.unlock();
      }

      return var7;
   }

   public String toString() {
      this.lock.lock();

      String var1;
      try {
         var1 = super.toString();
      } finally {
         this.lock.unlock();
      }

      return var1;
   }

   public void clear() {
      this.lock.lock();

      try {
         this.first = this.last = null;
         this.count = 0;
         this.notFull.signalAll();
      } finally {
         this.lock.unlock();
      }

   }

   public Iterator iterator() {
      return new LinkedBlockingDeque.Itr();
   }

   public Iterator descendingIterator() {
      return new LinkedBlockingDeque.DescendingItr();
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      this.lock.lock();

      try {
         s.defaultWriteObject();

         for(LinkedBlockingDeque.Node p = this.first; p != null; p = p.next) {
            s.writeObject(p.item);
         }

         s.writeObject((Object)null);
      } finally {
         this.lock.unlock();
      }
   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      this.count = 0;
      this.first = null;
      this.last = null;

      while(true) {
         Object item = s.readObject();
         if (item == null) {
            return;
         }

         this.add(item);
      }
   }

   private class DescendingItr extends LinkedBlockingDeque.AbstractItr {
      private DescendingItr() {
         super();
      }

      void advance() {
         ReentrantLock lock = LinkedBlockingDeque.this.lock;
         lock.lock();

         try {
            this.next = this.next == null ? LinkedBlockingDeque.this.last : this.next.prev;
            this.nextItem = this.next == null ? null : this.next.item;
         } finally {
            lock.unlock();
         }

      }

      // $FF: synthetic method
      DescendingItr(Object x1) {
         this();
      }
   }

   private class Itr extends LinkedBlockingDeque.AbstractItr {
      private Itr() {
         super();
      }

      void advance() {
         ReentrantLock lock = LinkedBlockingDeque.this.lock;
         lock.lock();

         try {
            this.next = this.next == null ? LinkedBlockingDeque.this.first : this.next.next;
            this.nextItem = this.next == null ? null : this.next.item;
         } finally {
            lock.unlock();
         }

      }

      // $FF: synthetic method
      Itr(Object x1) {
         this();
      }
   }

   private abstract class AbstractItr implements Iterator {
      LinkedBlockingDeque.Node next;
      Object nextItem;
      private LinkedBlockingDeque.Node lastRet;

      AbstractItr() {
         this.advance();
      }

      abstract void advance();

      public boolean hasNext() {
         return this.next != null;
      }

      public Object next() {
         if (this.next == null) {
            throw new NoSuchElementException();
         } else {
            this.lastRet = this.next;
            Object x = this.nextItem;
            this.advance();
            return x;
         }
      }

      public void remove() {
         LinkedBlockingDeque.Node n = this.lastRet;
         if (n == null) {
            throw new IllegalStateException();
         } else {
            this.lastRet = null;
            LinkedBlockingDeque.this.removeNode(n);
         }
      }
   }

   static final class Node {
      Object item;
      LinkedBlockingDeque.Node prev;
      LinkedBlockingDeque.Node next;

      Node(Object x, LinkedBlockingDeque.Node p, LinkedBlockingDeque.Node n) {
         this.item = x;
         this.prev = p;
         this.next = n;
      }
   }
}

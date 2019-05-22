package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.AbstractQueue;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedBlockingQueue extends AbstractQueue implements BlockingQueue, Serializable {
   private static final long serialVersionUID = -6903933977591709194L;
   private final int capacity;
   private volatile int count;
   private transient LinkedBlockingQueue.Node head;
   private transient LinkedBlockingQueue.Node last;
   private final Object takeLock;
   private final Object putLock;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   private void signalNotEmpty() {
      synchronized(this.takeLock) {
         this.takeLock.notify();
      }
   }

   private void signalNotFull() {
      synchronized(this.putLock) {
         this.putLock.notify();
      }
   }

   private void insert(Object x) {
      this.last = this.last.next = new LinkedBlockingQueue.Node(x);
   }

   private Object extract() {
      LinkedBlockingQueue.Node first = this.head.next;
      this.head = first;
      Object x = first.item;
      first.item = null;
      return x;
   }

   public LinkedBlockingQueue() {
      this(Integer.MAX_VALUE);
   }

   public LinkedBlockingQueue(int capacity) {
      this.count = 0;
      this.takeLock = new LinkedBlockingQueue.SerializableLock();
      this.putLock = new LinkedBlockingQueue.SerializableLock();
      if (capacity <= 0) {
         throw new IllegalArgumentException();
      } else {
         this.capacity = capacity;
         this.last = this.head = new LinkedBlockingQueue.Node((Object)null);
      }
   }

   public LinkedBlockingQueue(Collection c) {
      this(Integer.MAX_VALUE);
      Iterator itr = c.iterator();

      while(itr.hasNext()) {
         Object e = itr.next();
         this.add(e);
      }

   }

   public int size() {
      return this.count;
   }

   public int remainingCapacity() {
      return this.capacity - this.count;
   }

   public void put(Object e) throws InterruptedException {
      if (e == null) {
         throw new NullPointerException();
      } else {
         int c = true;
         int c;
         synchronized(this.putLock) {
            try {
               while(this.count == this.capacity) {
                  this.putLock.wait();
               }
            } catch (InterruptedException var8) {
               this.putLock.notify();
               throw var8;
            }

            this.insert(e);
            synchronized(this) {
               c = this.count++;
            }

            if (c + 1 < this.capacity) {
               this.putLock.notify();
            }
         }

         if (c == 0) {
            this.signalNotEmpty();
         }

      }
   }

   public boolean offer(Object e, long timeout, TimeUnit unit) throws InterruptedException {
      if (e == null) {
         throw new NullPointerException();
      } else {
         long nanos = unit.toNanos(timeout);
         int c = true;
         int c;
         synchronized(this.putLock) {
            long deadline = Utils.nanoTime() + nanos;

            while(this.count >= this.capacity) {
               if (nanos <= 0L) {
                  return false;
               }

               try {
                  TimeUnit.NANOSECONDS.timedWait(this.putLock, nanos);
                  nanos = deadline - Utils.nanoTime();
               } catch (InterruptedException var15) {
                  this.putLock.notify();
                  throw var15;
               }
            }

            this.insert(e);
            synchronized(this) {
               c = this.count++;
            }

            if (c + 1 < this.capacity) {
               this.putLock.notify();
            }
         }

         if (c == 0) {
            this.signalNotEmpty();
         }

         return true;
      }
   }

   public boolean offer(Object e) {
      if (e == null) {
         throw new NullPointerException();
      } else if (this.count == this.capacity) {
         return false;
      } else {
         int c = -1;
         synchronized(this.putLock) {
            if (this.count < this.capacity) {
               this.insert(e);
               synchronized(this) {
                  c = this.count++;
               }

               if (c + 1 < this.capacity) {
                  this.putLock.notify();
               }
            }
         }

         if (c == 0) {
            this.signalNotEmpty();
         }

         return c >= 0;
      }
   }

   public Object take() throws InterruptedException {
      int c = true;
      Object x;
      int c;
      synchronized(this.takeLock) {
         try {
            while(this.count == 0) {
               this.takeLock.wait();
            }
         } catch (InterruptedException var8) {
            this.takeLock.notify();
            throw var8;
         }

         x = this.extract();
         synchronized(this) {
            c = this.count--;
         }

         if (c > 1) {
            this.takeLock.notify();
         }
      }

      if (c == this.capacity) {
         this.signalNotFull();
      }

      return x;
   }

   public Object poll(long timeout, TimeUnit unit) throws InterruptedException {
      Object x = null;
      int c = true;
      long nanos = unit.toNanos(timeout);
      int c;
      synchronized(this.takeLock) {
         long deadline = Utils.nanoTime() + nanos;

         while(this.count <= 0) {
            if (nanos <= 0L) {
               return null;
            }

            try {
               TimeUnit.NANOSECONDS.timedWait(this.takeLock, nanos);
               nanos = deadline - Utils.nanoTime();
            } catch (InterruptedException var15) {
               this.takeLock.notify();
               throw var15;
            }
         }

         x = this.extract();
         synchronized(this) {
            c = this.count--;
         }

         if (c > 1) {
            this.takeLock.notify();
         }
      }

      if (c == this.capacity) {
         this.signalNotFull();
      }

      return x;
   }

   public Object poll() {
      if (this.count == 0) {
         return null;
      } else {
         Object x = null;
         int c = -1;
         synchronized(this.takeLock) {
            if (this.count > 0) {
               x = this.extract();
               synchronized(this) {
                  c = this.count--;
               }

               if (c > 1) {
                  this.takeLock.notify();
               }
            }
         }

         if (c == this.capacity) {
            this.signalNotFull();
         }

         return x;
      }
   }

   public Object peek() {
      if (this.count == 0) {
         return null;
      } else {
         synchronized(this.takeLock) {
            LinkedBlockingQueue.Node first = this.head.next;
            return first == null ? null : first.item;
         }
      }
   }

   public boolean remove(Object o) {
      if (o == null) {
         return false;
      } else {
         boolean removed = false;
         synchronized(this.putLock) {
            synchronized(this.takeLock) {
               LinkedBlockingQueue.Node trail = this.head;

               LinkedBlockingQueue.Node p;
               for(p = this.head.next; p != null; p = p.next) {
                  if (o.equals(p.item)) {
                     removed = true;
                     break;
                  }

                  trail = p;
               }

               if (removed) {
                  p.item = null;
                  trail.next = p.next;
                  if (this.last == p) {
                     this.last = trail;
                  }

                  synchronized(this) {
                     if (this.count-- == this.capacity) {
                        this.putLock.notifyAll();
                     }
                  }
               }
            }

            return removed;
         }
      }
   }

   public Object[] toArray() {
      synchronized(this.putLock) {
         synchronized(this.takeLock) {
            int size = this.count;
            Object[] a = new Object[size];
            int k = 0;

            for(LinkedBlockingQueue.Node p = this.head.next; p != null; p = p.next) {
               a[k++] = p.item;
            }

            Object[] var10000 = a;
            return var10000;
         }
      }
   }

   public Object[] toArray(Object[] a) {
      synchronized(this.putLock) {
         synchronized(this.takeLock) {
            int size = this.count;
            if (a.length < size) {
               a = (Object[])Array.newInstance(a.getClass().getComponentType(), size);
            }

            int k = 0;

            for(LinkedBlockingQueue.Node p = this.head.next; p != null; p = p.next) {
               a[k++] = p.item;
            }

            if (a.length > k) {
               a[k] = null;
            }

            Object[] var10000 = a;
            return var10000;
         }
      }
   }

   public String toString() {
      synchronized(this.putLock) {
         String var10000;
         synchronized(this.takeLock) {
            var10000 = super.toString();
         }

         return var10000;
      }
   }

   public void clear() {
      synchronized(this.putLock) {
         synchronized(this.takeLock) {
            this.head.next = null;
            if (!$assertionsDisabled && this.head.item != null) {
               throw new AssertionError();
            }

            this.last = this.head;
            int c;
            synchronized(this) {
               c = this.count;
               this.count = 0;
            }

            if (c == this.capacity) {
               this.putLock.notifyAll();
            }
         }

      }
   }

   public int drainTo(Collection c) {
      if (c == null) {
         throw new NullPointerException();
      } else if (c == this) {
         throw new IllegalArgumentException();
      } else {
         LinkedBlockingQueue.Node first;
         synchronized(this.putLock) {
            synchronized(this.takeLock) {
               first = this.head.next;
               this.head.next = null;
               if (!$assertionsDisabled && this.head.item != null) {
                  throw new AssertionError();
               }

               this.last = this.head;
               int cold;
               synchronized(this) {
                  cold = this.count;
                  this.count = 0;
               }

               if (cold == this.capacity) {
                  this.putLock.notifyAll();
               }
            }
         }

         int n = 0;

         for(LinkedBlockingQueue.Node p = first; p != null; p = p.next) {
            c.add(p.item);
            p.item = null;
            ++n;
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
         synchronized(this.putLock) {
            int var10000;
            synchronized(this.takeLock) {
               int n = 0;

               LinkedBlockingQueue.Node p;
               for(p = this.head.next; p != null && n < maxElements; ++n) {
                  c.add(p.item);
                  p.item = null;
                  p = p.next;
               }

               if (n != 0) {
                  this.head.next = p;
                  if (!$assertionsDisabled && this.head.item != null) {
                     throw new AssertionError();
                  }

                  if (p == null) {
                     this.last = this.head;
                  }

                  int cold;
                  synchronized(this) {
                     cold = this.count;
                     this.count -= n;
                  }

                  if (cold == this.capacity) {
                     this.putLock.notifyAll();
                  }
               }

               var10000 = n;
            }

            return var10000;
         }
      }
   }

   public Iterator iterator() {
      return new LinkedBlockingQueue.Itr();
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      synchronized(this.putLock) {
         synchronized(this.takeLock) {
            s.defaultWriteObject();

            for(LinkedBlockingQueue.Node p = this.head.next; p != null; p = p.next) {
               s.writeObject(p.item);
            }

            s.writeObject((Object)null);
         }
      }
   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      synchronized(this) {
         this.count = 0;
      }

      this.last = this.head = new LinkedBlockingQueue.Node((Object)null);

      while(true) {
         Object item = s.readObject();
         if (item == null) {
            return;
         }

         this.add(item);
      }
   }

   static {
      $assertionsDisabled = !LinkedBlockingQueue.class.desiredAssertionStatus();
   }

   private static class SerializableLock implements Serializable {
      private static final long serialVersionUID = -8856990691138858668L;

      private SerializableLock() {
      }

      // $FF: synthetic method
      SerializableLock(Object x0) {
         this();
      }
   }

   private class Itr implements Iterator {
      private LinkedBlockingQueue.Node current;
      private LinkedBlockingQueue.Node lastRet;
      private Object currentElement;

      Itr() {
         synchronized(LinkedBlockingQueue.this.putLock) {
            synchronized(LinkedBlockingQueue.this.takeLock) {
               this.current = LinkedBlockingQueue.this.head.next;
               if (this.current != null) {
                  this.currentElement = this.current.item;
               }
            }

         }
      }

      public boolean hasNext() {
         return this.current != null;
      }

      public Object next() {
         synchronized(LinkedBlockingQueue.this.putLock) {
            Object var10000;
            synchronized(LinkedBlockingQueue.this.takeLock) {
               if (this.current == null) {
                  throw new NoSuchElementException();
               }

               Object x = this.currentElement;
               this.lastRet = this.current;
               this.current = this.current.next;
               if (this.current != null) {
                  this.currentElement = this.current.item;
               }

               var10000 = x;
            }

            return var10000;
         }
      }

      public void remove() {
         if (this.lastRet == null) {
            throw new IllegalStateException();
         } else {
            synchronized(LinkedBlockingQueue.this.putLock) {
               synchronized(LinkedBlockingQueue.this.takeLock) {
                  LinkedBlockingQueue.Node node = this.lastRet;
                  this.lastRet = null;
                  LinkedBlockingQueue.Node trail = LinkedBlockingQueue.this.head;

                  LinkedBlockingQueue.Node p;
                  for(p = LinkedBlockingQueue.this.head.next; p != null && p != node; p = p.next) {
                     trail = p;
                  }

                  if (p == node) {
                     p.item = null;
                     trail.next = p.next;
                     if (LinkedBlockingQueue.this.last == p) {
                        LinkedBlockingQueue.this.last = trail;
                     }

                     int c;
                     synchronized(this) {
                        c = LinkedBlockingQueue.this.count--;
                     }

                     if (c == LinkedBlockingQueue.this.capacity) {
                        LinkedBlockingQueue.this.putLock.notifyAll();
                     }
                  }
               }

            }
         }
      }
   }

   static class Node {
      volatile Object item;
      LinkedBlockingQueue.Node next;

      Node(Object x) {
         this.item = x;
      }
   }
}

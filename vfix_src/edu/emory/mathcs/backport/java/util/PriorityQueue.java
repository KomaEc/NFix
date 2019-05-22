package edu.emory.mathcs.backport.java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedSet;

public class PriorityQueue extends AbstractQueue implements Serializable {
   private static final long serialVersionUID = -7720805057305804111L;
   private static final int DEFAULT_INIT_CAPACITY = 11;
   private transient Object[] buffer;
   private int size;
   private final Comparator comparator;
   private transient int modCount;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public PriorityQueue() {
      this(11, (Comparator)null);
   }

   public PriorityQueue(int initialCapacity) {
      this(initialCapacity, (Comparator)null);
   }

   public PriorityQueue(Comparator comparator) {
      this(11, comparator);
   }

   public PriorityQueue(int initialCapacity, Comparator comparator) {
      if (initialCapacity < 1) {
         throw new IllegalArgumentException();
      } else {
         this.buffer = new Object[initialCapacity];
         this.comparator = comparator;
      }
   }

   public PriorityQueue(PriorityQueue q) {
      this((Collection)q);
   }

   public PriorityQueue(SortedSet s) {
      this((Collection)s);
   }

   public PriorityQueue(Collection c) {
      int capacity = c.size();
      capacity += this.size / 10;
      if (capacity < 0) {
         capacity = Integer.MAX_VALUE;
      } else if (capacity == 0) {
         capacity = 1;
      }

      this.buffer = new Object[capacity];
      if (c instanceof PriorityQueue) {
         PriorityQueue that = (PriorityQueue)c;
         this.comparator = that.comparator;
         this.size = that.size;
         System.arraycopy(that.buffer, 0, this.buffer, 0, this.size);
      } else if (c instanceof SortedSet) {
         SortedSet s = (SortedSet)c;
         this.comparator = s.comparator();

         for(Iterator itr = s.iterator(); itr.hasNext(); this.buffer[this.size++] = itr.next()) {
         }
      } else {
         this.comparator = null;

         for(Iterator itr = c.iterator(); itr.hasNext(); this.buffer[this.size++] = itr.next()) {
         }

         for(int i = this.size / 2; i >= 0; --i) {
            this.percolateDown(i, this.buffer[i]);
         }
      }

   }

   public Iterator iterator() {
      return new PriorityQueue.Itr();
   }

   public Comparator comparator() {
      return this.comparator;
   }

   public boolean offer(Object o) {
      if (o == null) {
         throw new NullPointerException();
      } else {
         if (this.size == this.buffer.length) {
            int newlen = this.buffer.length * 2;
            if (newlen < this.buffer.length) {
               if (this.buffer.length == Integer.MAX_VALUE) {
                  throw new OutOfMemoryError();
               }

               newlen = Integer.MAX_VALUE;
            }

            Object[] newbuffer = new Object[newlen];
            System.arraycopy(this.buffer, 0, newbuffer, 0, this.size);
            this.buffer = newbuffer;
         }

         ++this.modCount;
         this.percolateUp(this.size++, o);
         return true;
      }
   }

   public Object peek() {
      return this.size == 0 ? null : this.buffer[0];
   }

   public Object poll() {
      if (this.size == 0) {
         return null;
      } else {
         ++this.modCount;
         Object head = this.buffer[0];
         --this.size;
         this.percolateDown(0, this.buffer[this.size]);
         this.buffer[this.size] = null;
         return head;
      }
   }

   public int size() {
      return this.size;
   }

   private int percolateDown(int idx, Object e) {
      int c;
      try {
         if (this.comparator != null) {
            while(true) {
               c = (idx << 1) + 1;
               if (c >= this.size) {
                  break;
               }

               if (c + 1 < this.size && this.comparator.compare(this.buffer[c], this.buffer[c + 1]) > 0) {
                  ++c;
               }

               if (this.comparator.compare(e, this.buffer[c]) <= 0) {
                  break;
               }

               this.buffer[idx] = this.buffer[c];
               idx = c;
            }
         } else {
            Comparable ec = (Comparable)e;

            while(true) {
               int c = (idx << 1) + 1;
               if (c >= this.size) {
                  break;
               }

               if (c + 1 < this.size && ((Comparable)this.buffer[c]).compareTo(this.buffer[c + 1]) > 0) {
                  ++c;
               }

               if (ec.compareTo(this.buffer[c]) <= 0) {
                  break;
               }

               this.buffer[idx] = this.buffer[c];
               idx = c;
            }
         }

         c = idx;
      } finally {
         this.buffer[idx] = e;
      }

      return c;
   }

   private int percolateUp(int idx, Object e) {
      try {
         if (this.comparator != null) {
            while(true) {
               int c;
               if (idx > 0) {
                  c = idx - 1 >>> 1;
                  if (this.comparator.compare(e, this.buffer[c]) < 0) {
                     this.buffer[idx] = this.buffer[c];
                     idx = c;
                     continue;
                  }
               }

               c = idx;
               return c;
            }
         } else {
            Comparable ce = (Comparable)e;

            while(true) {
               int c;
               if (idx > 0) {
                  c = idx - 1 >>> 1;
                  if (ce.compareTo(this.buffer[c]) < 0) {
                     this.buffer[idx] = this.buffer[c];
                     idx = c;
                     continue;
                  }
               }

               c = idx;
               return c;
            }
         }
      } finally {
         this.buffer[idx] = e;
      }
   }

   public boolean add(Object o) {
      return this.offer(o);
   }

   public Object remove() {
      if (this.size == 0) {
         throw new NoSuchElementException();
      } else {
         Object head = this.buffer[0];
         ++this.modCount;
         --this.size;
         this.percolateDown(0, this.buffer[this.size]);
         this.buffer[this.size] = null;
         return head;
      }
   }

   public Object element() {
      if (this.size == 0) {
         throw new NoSuchElementException();
      } else {
         return this.buffer[0];
      }
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public boolean contains(Object o) {
      for(int i = 0; i < this.size; ++i) {
         if (o.equals(this.buffer[i])) {
            return true;
         }
      }

      return false;
   }

   public Object[] toArray() {
      return Arrays.copyOf(this.buffer, this.size, Object[].class);
   }

   public Object[] toArray(Object[] a) {
      if (a.length < this.size) {
         return Arrays.copyOf(this.buffer, this.size, a.getClass());
      } else {
         System.arraycopy(this.buffer, 0, a, 0, this.size);
         if (a.length > this.size) {
            a[this.size] = null;
         }

         return a;
      }
   }

   public boolean remove(Object o) {
      if (o == null) {
         return false;
      } else {
         int i;
         if (this.comparator != null) {
            for(i = 0; i < this.size; ++i) {
               if (this.comparator.compare(this.buffer[i], o) == 0) {
                  this.removeAt(i);
                  return true;
               }
            }
         } else {
            for(i = 0; i < this.size; ++i) {
               if (((Comparable)this.buffer[i]).compareTo(o) == 0) {
                  this.removeAt(i);
                  return true;
               }
            }
         }

         return false;
      }
   }

   private Object removeAt(int i) {
      if (!$assertionsDisabled && i >= this.size) {
         throw new AssertionError();
      } else {
         ++this.modCount;
         --this.size;
         Object e = this.buffer[this.size];
         this.buffer[this.size] = null;
         int newpos = this.percolateDown(i, e);
         if (newpos != i) {
            return null;
         } else {
            newpos = this.percolateUp(i, e);
            return newpos < i ? e : null;
         }
      }
   }

   public void clear() {
      ++this.modCount;
      Arrays.fill(this.buffer, 0, this.size, (Object)null);
      this.size = 0;
   }

   private void writeObject(ObjectOutputStream os) throws IOException {
      os.defaultWriteObject();
      os.writeInt(this.buffer.length);

      for(int i = 0; i < this.size; ++i) {
         os.writeObject(this.buffer[i]);
      }

   }

   private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
      is.defaultReadObject();
      this.buffer = new Object[is.readInt()];

      for(int i = 0; i < this.size; ++i) {
         this.buffer[i] = is.readObject();
      }

   }

   static {
      $assertionsDisabled = !PriorityQueue.class.desiredAssertionStatus();
   }

   private class Itr implements Iterator {
      int cursor = 0;
      List percolatedElems;
      int cursorPercolated = 0;
      int expectedModCount;
      int lastRet;
      Object lastRetPercolated;

      Itr() {
         this.expectedModCount = PriorityQueue.this.modCount;
      }

      public boolean hasNext() {
         return this.cursor < PriorityQueue.this.size || this.percolatedElems != null;
      }

      public Object next() {
         this.checkForComodification();
         if (this.cursor < PriorityQueue.this.size) {
            this.lastRet = this.cursor++;
            return PriorityQueue.this.buffer[this.lastRet];
         } else if (this.percolatedElems != null) {
            this.lastRet = -1;
            this.lastRetPercolated = this.percolatedElems.remove(this.percolatedElems.size() - 1);
            if (this.percolatedElems.isEmpty()) {
               this.percolatedElems = null;
            }

            return this.lastRetPercolated;
         } else {
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         if (this.lastRet >= 0) {
            Object percolatedElem = PriorityQueue.this.removeAt(this.lastRet);
            this.lastRet = -1;
            if (percolatedElem == null) {
               --this.cursor;
            } else {
               if (this.percolatedElems == null) {
                  this.percolatedElems = new ArrayList();
               }

               this.percolatedElems.add(percolatedElem);
            }
         } else {
            if (this.lastRetPercolated == null) {
               throw new IllegalStateException();
            }

            PriorityQueue.this.remove(this.lastRetPercolated);
            this.lastRetPercolated = null;
         }

         this.expectedModCount = PriorityQueue.this.modCount;
      }

      private void checkForComodification() {
         if (this.expectedModCount != PriorityQueue.this.modCount) {
            throw new ConcurrentModificationException();
         }
      }
   }
}

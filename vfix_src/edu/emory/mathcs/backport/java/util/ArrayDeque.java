package edu.emory.mathcs.backport.java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque extends AbstractCollection implements Deque, Cloneable, Serializable {
   private transient Object[] elements;
   private transient int head;
   private transient int tail;
   private static final int MIN_INITIAL_CAPACITY = 8;
   private static final long serialVersionUID = 2340985798034038923L;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   private void allocateElements(int numElements) {
      int initialCapacity = 8;
      if (numElements >= initialCapacity) {
         initialCapacity = numElements | numElements >>> 1;
         initialCapacity |= initialCapacity >>> 2;
         initialCapacity |= initialCapacity >>> 4;
         initialCapacity |= initialCapacity >>> 8;
         initialCapacity |= initialCapacity >>> 16;
         ++initialCapacity;
         if (initialCapacity < 0) {
            initialCapacity >>>= 1;
         }
      }

      this.elements = (Object[])(new Object[initialCapacity]);
   }

   private void doubleCapacity() {
      if (!$assertionsDisabled && this.head != this.tail) {
         throw new AssertionError();
      } else {
         int p = this.head;
         int n = this.elements.length;
         int r = n - p;
         int newCapacity = n << 1;
         if (newCapacity < 0) {
            throw new IllegalStateException("Sorry, deque too big");
         } else {
            Object[] a = new Object[newCapacity];
            System.arraycopy(this.elements, p, a, 0, r);
            System.arraycopy(this.elements, 0, a, r, p);
            this.elements = (Object[])a;
            this.head = 0;
            this.tail = n;
         }
      }
   }

   private Object[] copyElements(Object[] a) {
      if (this.head < this.tail) {
         System.arraycopy(this.elements, this.head, a, 0, this.size());
      } else if (this.head > this.tail) {
         int headPortionLen = this.elements.length - this.head;
         System.arraycopy(this.elements, this.head, a, 0, headPortionLen);
         System.arraycopy(this.elements, 0, a, headPortionLen, this.tail);
      }

      return a;
   }

   public ArrayDeque() {
      this.elements = (Object[])(new Object[16]);
   }

   public ArrayDeque(int numElements) {
      this.allocateElements(numElements);
   }

   public ArrayDeque(Collection c) {
      this.allocateElements(c.size());
      this.addAll(c);
   }

   public void addFirst(Object e) {
      if (e == null) {
         throw new NullPointerException();
      } else {
         this.elements[this.head = this.head - 1 & this.elements.length - 1] = e;
         if (this.head == this.tail) {
            this.doubleCapacity();
         }

      }
   }

   public void addLast(Object e) {
      if (e == null) {
         throw new NullPointerException();
      } else {
         this.elements[this.tail] = e;
         if ((this.tail = this.tail + 1 & this.elements.length - 1) == this.head) {
            this.doubleCapacity();
         }

      }
   }

   public boolean offerFirst(Object e) {
      this.addFirst(e);
      return true;
   }

   public boolean offerLast(Object e) {
      this.addLast(e);
      return true;
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
      int h = this.head;
      Object result = this.elements[h];
      if (result == null) {
         return null;
      } else {
         this.elements[h] = null;
         this.head = h + 1 & this.elements.length - 1;
         return result;
      }
   }

   public Object pollLast() {
      int t = this.tail - 1 & this.elements.length - 1;
      Object result = this.elements[t];
      if (result == null) {
         return null;
      } else {
         this.elements[t] = null;
         this.tail = t;
         return result;
      }
   }

   public Object getFirst() {
      Object x = this.elements[this.head];
      if (x == null) {
         throw new NoSuchElementException();
      } else {
         return x;
      }
   }

   public Object getLast() {
      Object x = this.elements[this.tail - 1 & this.elements.length - 1];
      if (x == null) {
         throw new NoSuchElementException();
      } else {
         return x;
      }
   }

   public Object peekFirst() {
      return this.elements[this.head];
   }

   public Object peekLast() {
      return this.elements[this.tail - 1 & this.elements.length - 1];
   }

   public boolean removeFirstOccurrence(Object o) {
      if (o == null) {
         return false;
      } else {
         int mask = this.elements.length - 1;

         Object x;
         for(int i = this.head; (x = this.elements[i]) != null; i = i + 1 & mask) {
            if (o.equals(x)) {
               this.delete(i);
               return true;
            }
         }

         return false;
      }
   }

   public boolean removeLastOccurrence(Object o) {
      if (o == null) {
         return false;
      } else {
         int mask = this.elements.length - 1;

         Object x;
         for(int i = this.tail - 1 & mask; (x = this.elements[i]) != null; i = i - 1 & mask) {
            if (o.equals(x)) {
               this.delete(i);
               return true;
            }
         }

         return false;
      }
   }

   public boolean add(Object e) {
      this.addLast(e);
      return true;
   }

   public boolean offer(Object e) {
      return this.offerLast(e);
   }

   public Object remove() {
      return this.removeFirst();
   }

   public Object poll() {
      return this.pollFirst();
   }

   public Object element() {
      return this.getFirst();
   }

   public Object peek() {
      return this.peekFirst();
   }

   public void push(Object e) {
      this.addFirst(e);
   }

   public Object pop() {
      return this.removeFirst();
   }

   private void checkInvariants() {
      if (!$assertionsDisabled && this.elements[this.tail] != null) {
         throw new AssertionError();
      } else {
         if (!$assertionsDisabled) {
            label36: {
               if (this.head == this.tail) {
                  if (this.elements[this.head] == null) {
                     break label36;
                  }
               } else if (this.elements[this.head] != null && this.elements[this.tail - 1 & this.elements.length - 1] != null) {
                  break label36;
               }

               throw new AssertionError();
            }
         }

         if (!$assertionsDisabled && this.elements[this.head - 1 & this.elements.length - 1] != null) {
            throw new AssertionError();
         }
      }
   }

   private boolean delete(int i) {
      this.checkInvariants();
      Object[] elements = this.elements;
      int mask = elements.length - 1;
      int h = this.head;
      int t = this.tail;
      int front = i - h & mask;
      int back = t - i & mask;
      if (front >= (t - h & mask)) {
         throw new ConcurrentModificationException();
      } else if (front < back) {
         if (h <= i) {
            System.arraycopy(elements, h, elements, h + 1, front);
         } else {
            System.arraycopy(elements, 0, elements, 1, i);
            elements[0] = elements[mask];
            System.arraycopy(elements, h, elements, h + 1, mask - h);
         }

         elements[h] = null;
         this.head = h + 1 & mask;
         return false;
      } else {
         if (i < t) {
            System.arraycopy(elements, i + 1, elements, i, back);
            this.tail = t - 1;
         } else {
            System.arraycopy(elements, i + 1, elements, i, mask - i);
            elements[mask] = elements[0];
            System.arraycopy(elements, 1, elements, 0, t);
            this.tail = t - 1 & mask;
         }

         return true;
      }
   }

   public int size() {
      return this.tail - this.head & this.elements.length - 1;
   }

   public boolean isEmpty() {
      return this.head == this.tail;
   }

   public Iterator iterator() {
      return new ArrayDeque.DeqIterator();
   }

   public Iterator descendingIterator() {
      return new ArrayDeque.DescendingIterator();
   }

   public boolean contains(Object o) {
      if (o == null) {
         return false;
      } else {
         int mask = this.elements.length - 1;

         Object x;
         for(int i = this.head; (x = this.elements[i]) != null; i = i + 1 & mask) {
            if (o.equals(x)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean remove(Object o) {
      return this.removeFirstOccurrence(o);
   }

   public void clear() {
      int h = this.head;
      int t = this.tail;
      if (h != t) {
         this.head = this.tail = 0;
         int i = h;
         int mask = this.elements.length - 1;

         do {
            this.elements[i] = null;
            i = i + 1 & mask;
         } while(i != t);
      }

   }

   public Object[] toArray() {
      return this.copyElements(new Object[this.size()]);
   }

   public Object[] toArray(Object[] a) {
      int size = this.size();
      if (a.length < size) {
         a = (Object[])Array.newInstance(a.getClass().getComponentType(), size);
      }

      this.copyElements(a);
      if (a.length > size) {
         a[size] = null;
      }

      return a;
   }

   public Object clone() {
      try {
         ArrayDeque result = (ArrayDeque)super.clone();
         result.elements = Arrays.copyOf(this.elements, this.elements.length);
         return result;
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError();
      }
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();
      s.writeInt(this.size());
      int mask = this.elements.length - 1;

      for(int i = this.head; i != this.tail; i = i + 1 & mask) {
         s.writeObject(this.elements[i]);
      }

   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      int size = s.readInt();
      this.allocateElements(size);
      this.head = 0;
      this.tail = size;

      for(int i = 0; i < size; ++i) {
         this.elements[i] = s.readObject();
      }

   }

   static {
      $assertionsDisabled = !ArrayDeque.class.desiredAssertionStatus();
   }

   private class DescendingIterator implements Iterator {
      private int cursor;
      private int fence;
      private int lastRet;

      private DescendingIterator() {
         this.cursor = ArrayDeque.this.tail;
         this.fence = ArrayDeque.this.head;
         this.lastRet = -1;
      }

      public boolean hasNext() {
         return this.cursor != this.fence;
      }

      public Object next() {
         if (this.cursor == this.fence) {
            throw new NoSuchElementException();
         } else {
            this.cursor = this.cursor - 1 & ArrayDeque.this.elements.length - 1;
            Object result = ArrayDeque.this.elements[this.cursor];
            if (ArrayDeque.this.head == this.fence && result != null) {
               this.lastRet = this.cursor;
               return result;
            } else {
               throw new ConcurrentModificationException();
            }
         }
      }

      public void remove() {
         if (this.lastRet < 0) {
            throw new IllegalStateException();
         } else {
            if (!ArrayDeque.this.delete(this.lastRet)) {
               this.cursor = this.cursor + 1 & ArrayDeque.this.elements.length - 1;
               this.fence = ArrayDeque.this.head;
            }

            this.lastRet = -1;
         }
      }

      // $FF: synthetic method
      DescendingIterator(Object x1) {
         this();
      }
   }

   private class DeqIterator implements Iterator {
      private int cursor;
      private int fence;
      private int lastRet;

      private DeqIterator() {
         this.cursor = ArrayDeque.this.head;
         this.fence = ArrayDeque.this.tail;
         this.lastRet = -1;
      }

      public boolean hasNext() {
         return this.cursor != this.fence;
      }

      public Object next() {
         if (this.cursor == this.fence) {
            throw new NoSuchElementException();
         } else {
            Object result = ArrayDeque.this.elements[this.cursor];
            if (ArrayDeque.this.tail == this.fence && result != null) {
               this.lastRet = this.cursor;
               this.cursor = this.cursor + 1 & ArrayDeque.this.elements.length - 1;
               return result;
            } else {
               throw new ConcurrentModificationException();
            }
         }
      }

      public void remove() {
         if (this.lastRet < 0) {
            throw new IllegalStateException();
         } else {
            if (ArrayDeque.this.delete(this.lastRet)) {
               this.cursor = this.cursor - 1 & ArrayDeque.this.elements.length - 1;
               this.fence = ArrayDeque.this.tail;
            }

            this.lastRet = -1;
         }
      }

      // $FF: synthetic method
      DeqIterator(Object x1) {
         this();
      }
   }
}

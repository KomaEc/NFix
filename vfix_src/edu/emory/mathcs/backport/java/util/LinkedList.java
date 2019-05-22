package edu.emory.mathcs.backport.java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LinkedList extends java.util.AbstractSequentialList implements List, Deque, Cloneable, Serializable {
   private static final long serialVersionUID = 876323262645176354L;
   private transient int size;
   private transient int modCount;
   private transient LinkedList.Entry head;

   public LinkedList() {
      this.size = 0;
      LinkedList.Entry sentinel = new LinkedList.Entry((Object)null);
      sentinel.next = sentinel.prev = sentinel;
      this.head = sentinel;
   }

   public LinkedList(Collection c) {
      this();
      this.addAll(c);
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public boolean contains(Object o) {
      return this.findFirst(o) != null;
   }

   private LinkedList.Entry getAt(int idx) {
      int size = this.size;
      if (idx >= 0 && idx < size) {
         LinkedList.Entry e;
         if (idx < size >> 1) {
            for(e = this.head.next; idx > 0; --idx) {
               e = e.next;
            }

            return e;
         } else {
            idx = size - idx - 1;

            for(e = this.head.prev; idx > 0; --idx) {
               e = e.prev;
            }

            return e;
         }
      } else {
         throw new ArrayIndexOutOfBoundsException("Index: " + idx + "; Size: " + size);
      }
   }

   private LinkedList.Entry findFirst(Object o) {
      LinkedList.Entry e;
      if (o == null) {
         for(e = this.head.next; e != this.head; e = e.next) {
            if (e.val == null) {
               return e;
            }
         }
      } else {
         for(e = this.head.next; e != this.head; e = e.next) {
            if (o.equals(e.val)) {
               return e;
            }
         }
      }

      return null;
   }

   private LinkedList.Entry findLast(Object o) {
      LinkedList.Entry e;
      if (o == null) {
         for(e = this.head.prev; e != this.head; e = e.prev) {
            if (e.val == null) {
               return e;
            }
         }
      } else {
         for(e = this.head.prev; e != this.head; e = e.prev) {
            if (o.equals(e.val)) {
               return e;
            }
         }
      }

      return null;
   }

   public int indexOf(Object o) {
      int idx = 0;
      LinkedList.Entry e;
      if (o == null) {
         for(e = this.head.next; e != this.head; ++idx) {
            if (e.val == null) {
               return idx;
            }

            e = e.next;
         }
      } else {
         for(e = this.head.next; e != this.head; ++idx) {
            if (o.equals(e.val)) {
               return idx;
            }

            e = e.next;
         }
      }

      return -1;
   }

   public int lastIndexOf(Object o) {
      int idx = this.size - 1;
      LinkedList.Entry e;
      if (o == null) {
         for(e = this.head.prev; e != this.head; --idx) {
            if (e.val == null) {
               return idx;
            }

            e = e.prev;
         }
      } else {
         for(e = this.head.prev; e != this.head; --idx) {
            if (o.equals(e.val)) {
               return idx;
            }

            e = e.prev;
         }
      }

      return -1;
   }

   public Object[] toArray() {
      Object[] a = new Object[this.size];
      int i = 0;

      for(LinkedList.Entry e = this.head.next; e != this.head; e = e.next) {
         a[i++] = e.val;
      }

      return a;
   }

   public Object[] toArray(Object[] a) {
      int size = this.size;
      if (a.length < size) {
         a = (Object[])Array.newInstance(a.getClass().getComponentType(), size);
      }

      int i = 0;

      for(LinkedList.Entry e = this.head.next; e != this.head; e = e.next) {
         a[i++] = e.val;
      }

      if (i < a.length) {
         a[i++] = null;
      }

      return a;
   }

   public boolean add(Object o) {
      this.insertBefore(this.head, o);
      return true;
   }

   private void insertAfter(LinkedList.Entry e, Object val) {
      ++this.modCount;
      LinkedList.Entry succ = e.next;
      LinkedList.Entry newe = new LinkedList.Entry(val);
      newe.prev = e;
      newe.next = succ;
      e.next = newe;
      succ.prev = newe;
      ++this.size;
   }

   private void insertBefore(LinkedList.Entry e, Object val) {
      ++this.modCount;
      LinkedList.Entry pred = e.prev;
      LinkedList.Entry newe = new LinkedList.Entry(val);
      newe.prev = pred;
      newe.next = e;
      pred.next = newe;
      e.prev = newe;
      ++this.size;
   }

   private Object remove(LinkedList.Entry e) {
      if (e == this.head) {
         throw new NoSuchElementException();
      } else {
         ++this.modCount;
         LinkedList.Entry succ = e.next;
         LinkedList.Entry pred = e.prev;
         pred.next = succ;
         succ.prev = pred;
         --this.size;
         return e.val;
      }
   }

   public boolean remove(Object o) {
      LinkedList.Entry e = this.findFirst(o);
      if (e == null) {
         return false;
      } else {
         this.remove(e);
         return true;
      }
   }

   public boolean addAll(Collection c) {
      return this.insertAllBefore(this.head, c);
   }

   public boolean addAll(int index, Collection c) {
      return this.insertAllBefore(index == this.size ? this.head : this.getAt(index), c);
   }

   private boolean insertAllBefore(LinkedList.Entry succ, Collection c) {
      Iterator itr = c.iterator();
      if (!itr.hasNext()) {
         return false;
      } else {
         ++this.modCount;
         LinkedList.Entry first = new LinkedList.Entry(itr.next());
         LinkedList.Entry prev = first;
         LinkedList.Entry curr = first;

         int added;
         for(added = 1; itr.hasNext(); ++added) {
            curr = new LinkedList.Entry(itr.next());
            prev.next = curr;
            curr.prev = prev;
            prev = curr;
         }

         LinkedList.Entry pred = succ.prev;
         first.prev = pred;
         curr.next = succ;
         pred.next = first;
         succ.prev = curr;
         this.size += added;
         return true;
      }
   }

   public void clear() {
      ++this.modCount;
      this.head.next = this.head.prev = this.head;
      this.size = 0;
   }

   public Object get(int index) {
      return this.getAt(index).val;
   }

   public Object set(int index, Object element) {
      LinkedList.Entry e = this.getAt(index);
      Object old = e.val;
      e.val = element;
      return old;
   }

   public void add(int index, Object element) {
      if (index == this.size) {
         this.insertBefore(this.head, element);
      } else {
         this.insertBefore(index == this.size ? this.head : this.getAt(index), element);
      }

   }

   public Object remove(int index) {
      return this.remove(this.getAt(index));
   }

   public ListIterator listIterator() {
      return new LinkedList.Itr();
   }

   public ListIterator listIterator(int index) {
      return new LinkedList.Itr(index == this.size ? this.head : this.getAt(index), index);
   }

   public void addFirst(Object e) {
      this.insertAfter(this.head, e);
   }

   public void addLast(Object e) {
      this.insertBefore(this.head, e);
   }

   public boolean offerFirst(Object e) {
      this.insertAfter(this.head, e);
      return true;
   }

   public boolean offerLast(Object e) {
      this.insertBefore(this.head, e);
      return true;
   }

   public Object removeFirst() {
      return this.remove(this.head.next);
   }

   public Object removeLast() {
      return this.remove(this.head.prev);
   }

   public Object pollFirst() {
      return this.size == 0 ? null : this.remove(this.head.next);
   }

   public Object pollLast() {
      return this.size == 0 ? null : this.remove(this.head.prev);
   }

   public Object getFirst() {
      if (this.size == 0) {
         throw new NoSuchElementException();
      } else {
         return this.head.next.val;
      }
   }

   public Object getLast() {
      if (this.size == 0) {
         throw new NoSuchElementException();
      } else {
         return this.head.prev.val;
      }
   }

   public Object peekFirst() {
      return this.size == 0 ? null : this.head.next.val;
   }

   public Object peekLast() {
      return this.size == 0 ? null : this.head.prev.val;
   }

   public boolean removeFirstOccurrence(Object o) {
      LinkedList.Entry e = this.findFirst(o);
      if (e == null) {
         return false;
      } else {
         this.remove(e);
         return true;
      }
   }

   public boolean removeLastOccurrence(Object o) {
      LinkedList.Entry e = this.findLast(o);
      if (e == null) {
         return false;
      } else {
         this.remove(e);
         return true;
      }
   }

   public boolean offer(Object e) {
      return this.add(e);
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

   public Iterator descendingIterator() {
      return new LinkedList.DescItr();
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeInt(this.size);

      for(LinkedList.Entry e = this.head.next; e != this.head; e = e.next) {
         out.writeObject(e.val);
      }

   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      int size = in.readInt();
      LinkedList.Entry head = new LinkedList.Entry((Object)null);
      head.next = head.prev = head;

      for(int i = 0; i < size; ++i) {
         this.insertBefore(head, in.readObject());
      }

      this.size = size;
      this.head = head;
   }

   public Object clone() {
      LinkedList clone = null;

      try {
         clone = (LinkedList)super.clone();
      } catch (CloneNotSupportedException var3) {
         throw new InternalError();
      }

      LinkedList.Entry head = new LinkedList.Entry((Object)null);
      head.next = head.prev = head;
      clone.head = head;
      clone.addAll(this);
      return clone;
   }

   private class DescItr implements ListIterator {
      int expectedModCount;
      int idx;
      LinkedList.Entry cursor;
      LinkedList.Entry lastRet;

      DescItr(LinkedList.Entry cursor, int idx) {
         this.cursor = cursor;
         this.idx = idx;
         this.expectedModCount = LinkedList.this.modCount;
      }

      DescItr() {
         this(LinkedList.this.head.prev, 0);
      }

      public boolean hasNext() {
         return this.cursor != LinkedList.this.head;
      }

      public int nextIndex() {
         return this.idx;
      }

      public boolean hasPrevious() {
         return this.cursor.next != LinkedList.this.head;
      }

      public int previousIndex() {
         return this.idx - 1;
      }

      public Object next() {
         if (this.expectedModCount != LinkedList.this.modCount) {
            throw new ConcurrentModificationException();
         } else if (this.cursor == LinkedList.this.head) {
            throw new NoSuchElementException();
         } else {
            this.lastRet = this.cursor;
            this.cursor = this.cursor.prev;
            ++this.idx;
            return this.lastRet.val;
         }
      }

      public Object previous() {
         if (this.expectedModCount != LinkedList.this.modCount) {
            throw new ConcurrentModificationException();
         } else if (this.cursor.next == LinkedList.this.head) {
            throw new NoSuchElementException();
         } else {
            this.lastRet = this.cursor = this.cursor.next;
            --this.idx;
            return this.lastRet;
         }
      }

      public void add(Object val) {
         if (this.expectedModCount != LinkedList.this.modCount) {
            throw new ConcurrentModificationException();
         } else {
            LinkedList.this.insertAfter(this.cursor, val);
            this.lastRet = null;
            ++this.idx;
            ++this.expectedModCount;
         }
      }

      public void set(Object newVal) {
         if (this.lastRet == null) {
            throw new IllegalStateException();
         } else {
            this.lastRet.val = newVal;
         }
      }

      public void remove() {
         if (this.expectedModCount != LinkedList.this.modCount) {
            throw new ConcurrentModificationException();
         } else if (this.lastRet == null) {
            throw new IllegalStateException();
         } else {
            if (this.lastRet.next == this.cursor) {
               --this.idx;
            } else {
               this.cursor = this.lastRet.next;
            }

            LinkedList.this.remove(this.lastRet);
            this.lastRet = null;
            ++this.expectedModCount;
         }
      }
   }

   private class Itr implements ListIterator {
      int expectedModCount;
      int idx;
      LinkedList.Entry cursor;
      LinkedList.Entry lastRet;

      Itr(LinkedList.Entry cursor, int idx) {
         this.cursor = cursor;
         this.idx = idx;
         this.expectedModCount = LinkedList.this.modCount;
      }

      Itr() {
         this(LinkedList.this.head.next, 0);
      }

      public boolean hasNext() {
         return this.cursor != LinkedList.this.head;
      }

      public int nextIndex() {
         return this.idx;
      }

      public boolean hasPrevious() {
         return this.cursor.prev != LinkedList.this.head;
      }

      public int previousIndex() {
         return this.idx - 1;
      }

      public Object next() {
         if (this.expectedModCount != LinkedList.this.modCount) {
            throw new ConcurrentModificationException();
         } else if (this.cursor == LinkedList.this.head) {
            throw new NoSuchElementException();
         } else {
            this.lastRet = this.cursor;
            this.cursor = this.cursor.next;
            ++this.idx;
            return this.lastRet.val;
         }
      }

      public Object previous() {
         if (this.expectedModCount != LinkedList.this.modCount) {
            throw new ConcurrentModificationException();
         } else if (this.cursor.prev == LinkedList.this.head) {
            throw new NoSuchElementException();
         } else {
            this.lastRet = this.cursor = this.cursor.prev;
            --this.idx;
            return this.lastRet.val;
         }
      }

      public void add(Object val) {
         if (this.expectedModCount != LinkedList.this.modCount) {
            throw new ConcurrentModificationException();
         } else {
            LinkedList.this.insertBefore(this.cursor, val);
            this.lastRet = null;
            ++this.idx;
            ++this.expectedModCount;
         }
      }

      public void set(Object newVal) {
         if (this.lastRet == null) {
            throw new IllegalStateException();
         } else {
            this.lastRet.val = newVal;
         }
      }

      public void remove() {
         if (this.expectedModCount != LinkedList.this.modCount) {
            throw new ConcurrentModificationException();
         } else if (this.lastRet == null) {
            throw new IllegalStateException();
         } else {
            if (this.lastRet.next == this.cursor) {
               --this.idx;
            } else {
               this.cursor = this.lastRet.next;
            }

            LinkedList.this.remove(this.lastRet);
            this.lastRet = null;
            ++this.expectedModCount;
         }
      }
   }

   private static class Entry {
      LinkedList.Entry prev;
      LinkedList.Entry next;
      Object val;

      Entry(Object val) {
         this.val = val;
      }
   }
}

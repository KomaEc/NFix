package soot.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArraySet<E> extends AbstractSet<E> {
   private static final int DEFAULT_SIZE = 8;
   private int numElements;
   private int maxElements;
   private Object[] elements;

   public ArraySet(int size) {
      this.maxElements = size;
      this.elements = new Object[size];
      this.numElements = 0;
   }

   public ArraySet() {
      this(8);
   }

   public ArraySet(E[] elements) {
      this();
      Object[] var2 = elements;
      int var3 = elements.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         E element = var2[var4];
         this.add(element);
      }

   }

   public final void clear() {
      this.numElements = 0;
   }

   public final boolean contains(Object obj) {
      for(int i = 0; i < this.numElements; ++i) {
         if (this.elements[i].equals(obj)) {
            return true;
         }
      }

      return false;
   }

   public final boolean addElement(E e) {
      if (e == null) {
         throw new RuntimeException("oops");
      } else {
         if (this.numElements == this.maxElements) {
            this.doubleCapacity();
         }

         this.elements[this.numElements++] = e;
         return true;
      }
   }

   public final boolean add(E e) {
      if (e == null) {
         throw new RuntimeException("oops");
      } else if (this.contains(e)) {
         return false;
      } else {
         if (this.numElements == this.maxElements) {
            this.doubleCapacity();
         }

         this.elements[this.numElements++] = e;
         return true;
      }
   }

   public final boolean addAll(Collection<? extends E> s) {
      boolean ret = false;
      if (!(s instanceof ArraySet)) {
         return super.addAll(s);
      } else {
         ArraySet<?> as = (ArraySet)s;
         int asSize = as.size();
         Object[] asElements = as.elements;

         for(int i = 0; i < asSize; ++i) {
            ret |= this.add(asElements[i]);
         }

         return ret;
      }
   }

   public final int size() {
      return this.numElements;
   }

   public final Iterator<E> iterator() {
      return new ArraySet.ArrayIterator();
   }

   private final void removeElementAt(int index) {
      if (index == this.numElements - 1) {
         --this.numElements;
      } else {
         System.arraycopy(this.elements, index + 1, this.elements, index, this.numElements - (index + 1));
         --this.numElements;
      }
   }

   private final void doubleCapacity() {
      int newSize = this.maxElements * 2;
      Object[] newElements = new Object[newSize];
      System.arraycopy(this.elements, 0, newElements, 0, this.numElements);
      this.elements = newElements;
      this.maxElements = newSize;
   }

   public final Object[] toArray() {
      Object[] array = new Object[this.numElements];
      System.arraycopy(this.elements, 0, array, 0, this.numElements);
      return array;
   }

   public final <T> T[] toArray(T[] array) {
      System.arraycopy(this.elements, 0, array, 0, this.numElements);
      return array;
   }

   public final Object[] getUnderlyingArray() {
      return this.elements;
   }

   class Array {
      private final int DEFAULT_SIZE = 8;
      private int numElements = 0;
      private int maxElements = 8;
      private Object[] elements = new Object[8];

      public final void clear() {
         this.numElements = 0;
      }

      public Array() {
      }

      private final void doubleCapacity() {
         int newSize = this.maxElements * 2;
         Object[] newElements = new Object[newSize];
         System.arraycopy(this.elements, 0, newElements, 0, this.numElements);
         this.elements = newElements;
         this.maxElements = newSize;
      }

      public final void addElement(Object e) {
         if (this.numElements == this.maxElements) {
            this.doubleCapacity();
         }

         this.elements[this.numElements++] = e;
      }

      public final void insertElementAt(Object e, int index) {
         if (this.numElements == this.maxElements) {
            this.doubleCapacity();
         }

         if (index == this.numElements) {
            this.elements[this.numElements++] = e;
         } else {
            System.arraycopy(this.elements, index, this.elements, index + 1, this.numElements - index);
            this.elements[index] = e;
            ++this.numElements;
         }
      }

      public final boolean contains(Object e) {
         for(int i = 0; i < this.numElements; ++i) {
            if (this.elements[i].equals(e)) {
               return true;
            }
         }

         return false;
      }

      public final int size() {
         return this.numElements;
      }

      public final Object elementAt(int index) {
         return this.elements[index];
      }

      public final void removeElementAt(int index) {
         if (index == this.numElements - 1) {
            --this.numElements;
         } else {
            System.arraycopy(this.elements, index + 1, this.elements, index, this.numElements - (index + 1));
            --this.numElements;
         }
      }
   }

   private class ArrayIterator<V> implements Iterator<V> {
      int nextIndex = 0;

      ArrayIterator() {
      }

      public final boolean hasNext() {
         return this.nextIndex < ArraySet.this.numElements;
      }

      public final V next() throws NoSuchElementException {
         if (this.nextIndex >= ArraySet.this.numElements) {
            throw new NoSuchElementException();
         } else {
            return ArraySet.this.elements[this.nextIndex++];
         }
      }

      public final void remove() throws NoSuchElementException {
         if (this.nextIndex == 0) {
            throw new NoSuchElementException();
         } else {
            ArraySet.this.removeElementAt(this.nextIndex - 1);
            --this.nextIndex;
         }
      }
   }
}

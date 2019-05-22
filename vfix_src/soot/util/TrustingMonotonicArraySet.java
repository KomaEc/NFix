package soot.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

class TrustingMonotonicArraySet<T> extends AbstractSet<T> {
   private static final int DEFAULT_SIZE = 8;
   private int numElements;
   private int maxElements;
   private T[] elements;

   public TrustingMonotonicArraySet() {
      this.maxElements = 8;
      this.elements = (Object[])(new Object[8]);
      this.numElements = 0;
   }

   public TrustingMonotonicArraySet(T[] elements) {
      this();
      Object[] var2 = elements;
      int var3 = elements.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         T element = var2[var4];
         this.add(element);
      }

   }

   public void clear() {
      this.numElements = 0;
   }

   public boolean contains(Object obj) {
      for(int i = 0; i < this.numElements; ++i) {
         if (this.elements[i].equals(obj)) {
            return true;
         }
      }

      return false;
   }

   public boolean add(T e) {
      if (this.numElements == this.maxElements) {
         this.doubleCapacity();
      }

      this.elements[this.numElements++] = e;
      return true;
   }

   public int size() {
      return this.numElements;
   }

   public Iterator<T> iterator() {
      return new TrustingMonotonicArraySet.ArrayIterator();
   }

   private void removeElementAt(int index) {
      throw new UnsupportedOperationException();
   }

   private void doubleCapacity() {
      int newSize = this.maxElements * 2;
      T[] newElements = (Object[])(new Object[newSize]);
      System.arraycopy(this.elements, 0, newElements, 0, this.numElements);
      this.elements = newElements;
      this.maxElements = newSize;
   }

   public T[] toArray() {
      T[] array = (Object[])(new Object[this.numElements]);
      System.arraycopy(this.elements, 0, array, 0, this.numElements);
      return array;
   }

   private class ArrayIterator implements Iterator<T> {
      int nextIndex = 0;

      ArrayIterator() {
      }

      public boolean hasNext() {
         return this.nextIndex < TrustingMonotonicArraySet.this.numElements;
      }

      public T next() throws NoSuchElementException {
         if (this.nextIndex >= TrustingMonotonicArraySet.this.numElements) {
            throw new NoSuchElementException();
         } else {
            return TrustingMonotonicArraySet.this.elements[this.nextIndex++];
         }
      }

      public void remove() throws NoSuchElementException {
         if (this.nextIndex == 0) {
            throw new NoSuchElementException();
         } else {
            TrustingMonotonicArraySet.this.removeElementAt(this.nextIndex - 1);
            --this.nextIndex;
         }
      }
   }
}

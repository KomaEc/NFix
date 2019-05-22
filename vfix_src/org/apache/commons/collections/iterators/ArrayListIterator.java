package org.apache.commons.collections.iterators;

import java.lang.reflect.Array;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.commons.collections.ResettableListIterator;

public class ArrayListIterator extends ArrayIterator implements ListIterator, ResettableListIterator {
   protected int lastItemIndex = -1;

   public ArrayListIterator() {
   }

   public ArrayListIterator(Object array) {
      super(array);
   }

   public ArrayListIterator(Object array, int startIndex) {
      super(array, startIndex);
      super.startIndex = startIndex;
   }

   public ArrayListIterator(Object array, int startIndex, int endIndex) {
      super(array, startIndex, endIndex);
      super.startIndex = startIndex;
   }

   public boolean hasPrevious() {
      return super.index > super.startIndex;
   }

   public Object previous() {
      if (!this.hasPrevious()) {
         throw new NoSuchElementException();
      } else {
         this.lastItemIndex = --super.index;
         return Array.get(super.array, super.index);
      }
   }

   public Object next() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         this.lastItemIndex = super.index;
         return Array.get(super.array, super.index++);
      }
   }

   public int nextIndex() {
      return super.index - super.startIndex;
   }

   public int previousIndex() {
      return super.index - super.startIndex - 1;
   }

   public void add(Object o) {
      throw new UnsupportedOperationException("add() method is not supported");
   }

   public void set(Object o) {
      if (this.lastItemIndex == -1) {
         throw new IllegalStateException("must call next() or previous() before a call to set()");
      } else {
         Array.set(super.array, this.lastItemIndex, o);
      }
   }

   public void reset() {
      super.reset();
      this.lastItemIndex = -1;
   }
}

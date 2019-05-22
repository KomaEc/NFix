package org.jf.util;

import java.util.ListIterator;

public abstract class AbstractListIterator<T> implements ListIterator<T> {
   public boolean hasNext() {
      throw new UnsupportedOperationException();
   }

   public T next() {
      throw new UnsupportedOperationException();
   }

   public boolean hasPrevious() {
      throw new UnsupportedOperationException();
   }

   public T previous() {
      throw new UnsupportedOperationException();
   }

   public int nextIndex() {
      throw new UnsupportedOperationException();
   }

   public int previousIndex() {
      throw new UnsupportedOperationException();
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   public void set(T t) {
      throw new UnsupportedOperationException();
   }

   public void add(T t) {
      throw new UnsupportedOperationException();
   }
}

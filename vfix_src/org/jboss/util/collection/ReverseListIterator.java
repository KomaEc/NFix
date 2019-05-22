package org.jboss.util.collection;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ReverseListIterator<E> implements Iterator<E> {
   protected final List<E> list;
   protected int current;

   public ReverseListIterator(List<E> list) {
      this.list = list;
      this.current = list.size() - 1;
   }

   public boolean hasNext() {
      return this.current > 0;
   }

   public E next() {
      if (this.current <= 0) {
         throw new NoSuchElementException();
      } else {
         return this.list.get(this.current--);
      }
   }

   public void remove() {
      this.list.remove(this.current);
   }
}

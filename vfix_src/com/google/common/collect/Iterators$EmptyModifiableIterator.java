package com.google.common.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;

enum Iterators$EmptyModifiableIterator implements Iterator<Object> {
   INSTANCE;

   public boolean hasNext() {
      return false;
   }

   public Object next() {
      throw new NoSuchElementException();
   }

   public void remove() {
      CollectPreconditions.checkRemove(false);
   }
}

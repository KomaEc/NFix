package org.jboss.util.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CompoundIterator implements Iterator {
   protected final Iterator[] iters;
   protected int index;

   public CompoundIterator(Iterator[] iters) {
      if (iters != null && iters.length != 0) {
         this.iters = iters;
      } else {
         throw new IllegalArgumentException("array is null or empty");
      }
   }

   public boolean hasNext() {
      while(this.index < this.iters.length) {
         if (this.iters[this.index] != null && this.iters[this.index].hasNext()) {
            return true;
         }

         ++this.index;
      }

      return false;
   }

   public Object next() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         return this.iters[this.index].next();
      }
   }

   public void remove() {
      this.iters[this.index].remove();
   }
}

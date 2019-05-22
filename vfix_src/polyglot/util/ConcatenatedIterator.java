package polyglot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class ConcatenatedIterator implements Iterator {
   Object next_item;
   Iterator[] backing_iterators;
   int index;

   public ConcatenatedIterator(Iterator iter1, Iterator iter2) {
      this(new Iterator[]{iter1, iter2});
   }

   public ConcatenatedIterator(Iterator[] iters) {
      this.backing_iterators = (Iterator[])iters.clone();
      this.findNextItem();
   }

   public ConcatenatedIterator(Collection iters) {
      this.backing_iterators = (Iterator[])iters.toArray(new Iterator[0]);
      this.findNextItem();
   }

   public Object next() {
      Object res = this.next_item;
      if (res == null) {
         throw new NoSuchElementException();
      } else {
         this.findNextItem();
         return res;
      }
   }

   public boolean hasNext() {
      return this.next_item != null;
   }

   public void remove() {
      throw new UnsupportedOperationException("ConcatenatedIterator.remove");
   }

   private void findNextItem() {
      while(this.index < this.backing_iterators.length) {
         Iterator it = this.backing_iterators[this.index];
         if (it.hasNext()) {
            this.next_item = it.next();
            return;
         }

         ++this.index;
      }

      this.next_item = null;
   }
}

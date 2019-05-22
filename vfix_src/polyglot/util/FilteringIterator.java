package polyglot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class FilteringIterator implements Iterator {
   Object next_item;
   Iterator backing_iterator;
   Predicate predicate;

   public FilteringIterator(Collection coll, Predicate pred) {
      this(coll.iterator(), pred);
   }

   public FilteringIterator(Iterator iter, Predicate pred) {
      this.backing_iterator = iter;
      this.predicate = pred;
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
      throw new UnsupportedOperationException("FilteringIterator.remove");
   }

   private void findNextItem() {
      while(true) {
         if (this.backing_iterator.hasNext()) {
            Object o = this.backing_iterator.next();
            if (!this.predicate.isTrue(o)) {
               continue;
            }

            this.next_item = o;
            return;
         }

         this.next_item = null;
         return;
      }
   }
}

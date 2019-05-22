package polyglot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class TransformingIterator implements Iterator {
   Object next_item;
   Iterator current_iter;
   int index;
   Iterator[] backing_iterators;
   Transformation transformation;

   public TransformingIterator(Iterator iter, Transformation trans) {
      this(new Iterator[]{iter}, trans);
   }

   public TransformingIterator(Collection iters, Transformation trans) {
      this.index = 0;
      this.backing_iterators = (Iterator[])iters.toArray(new Iterator[0]);
      this.transformation = trans;
      if (this.backing_iterators.length > 0) {
         this.current_iter = this.backing_iterators[0];
      }

      this.findNextItem();
   }

   public TransformingIterator(Iterator[] iters, Transformation trans) {
      this.index = 0;
      this.backing_iterators = (Iterator[])iters.clone();
      this.transformation = trans;
      if (iters.length > 0) {
         this.current_iter = iters[0];
      }

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
      throw new UnsupportedOperationException("TransformingIterator.remove");
   }

   private void findNextItem() {
      label21:
      while(true) {
         if (this.current_iter != null) {
            Object res;
            do {
               if (!this.current_iter.hasNext()) {
                  ++this.index;
                  if (this.index < this.backing_iterators.length) {
                     this.current_iter = this.backing_iterators[this.index];
                     continue label21;
                  }

                  this.current_iter = null;
                  continue label21;
               }

               Object o = this.current_iter.next();
               res = this.transformation.transform(o);
            } while(res == Transformation.NOTHING);

            this.next_item = res;
            return;
         }

         this.next_item = null;
         return;
      }
   }
}

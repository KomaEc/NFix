package org.jf.util;

import java.util.AbstractSequentialList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractForwardSequentialList<T> extends AbstractSequentialList<T> {
   @Nonnull
   private Iterator<T> iterator(int index) {
      if (index < 0) {
         throw new NoSuchElementException();
      } else {
         Iterator<T> it = this.iterator();

         for(int i = 0; i < index; ++i) {
            it.next();
         }

         return it;
      }
   }

   @Nonnull
   public abstract Iterator<T> iterator();

   @Nonnull
   public ListIterator<T> listIterator(final int initialIndex) {
      final Iterator initialIterator;
      try {
         initialIterator = this.iterator(initialIndex);
      } catch (NoSuchElementException var4) {
         throw new IndexOutOfBoundsException();
      }

      return new AbstractListIterator<T>() {
         private int index = initialIndex - 1;
         @Nullable
         private Iterator<T> forwardIterator = initialIterator;

         @Nonnull
         private Iterator<T> getForwardIterator() {
            if (this.forwardIterator == null) {
               try {
                  this.forwardIterator = AbstractForwardSequentialList.this.iterator(this.index + 1);
               } catch (IndexOutOfBoundsException var2) {
                  throw new NoSuchElementException();
               }
            }

            return this.forwardIterator;
         }

         public boolean hasNext() {
            return this.getForwardIterator().hasNext();
         }

         public boolean hasPrevious() {
            return this.index >= 0;
         }

         public T next() {
            T ret = this.getForwardIterator().next();
            ++this.index;
            return ret;
         }

         public int nextIndex() {
            return this.index + 1;
         }

         public T previous() {
            this.forwardIterator = null;

            try {
               return AbstractForwardSequentialList.this.iterator(this.index--).next();
            } catch (IndexOutOfBoundsException var2) {
               throw new NoSuchElementException();
            }
         }

         public int previousIndex() {
            return this.index;
         }
      };
   }

   @Nonnull
   public ListIterator<T> listIterator() {
      return this.listIterator(0);
   }
}

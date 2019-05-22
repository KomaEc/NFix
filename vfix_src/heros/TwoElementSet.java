package heros;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TwoElementSet<E> extends AbstractSet<E> {
   protected final E first;
   protected final E second;

   public TwoElementSet(E first, E second) {
      this.first = first;
      this.second = second;
   }

   public static <E> TwoElementSet<E> twoElementSet(E first, E second) {
      return new TwoElementSet(first, second);
   }

   public Iterator<E> iterator() {
      return new Iterator<E>() {
         int elementsRead = 0;

         public boolean hasNext() {
            return this.elementsRead < 2;
         }

         public E next() {
            switch(this.elementsRead) {
            case 0:
               ++this.elementsRead;
               return TwoElementSet.this.first;
            case 1:
               ++this.elementsRead;
               return TwoElementSet.this.second;
            default:
               throw new NoSuchElementException();
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public int size() {
      return 2;
   }
}

package soot.util;

import java.util.AbstractList;

/** @deprecated */
@Deprecated
public class SingletonList<E> extends AbstractList<E> {
   private E o;

   public SingletonList(E o) {
      this.o = o;
   }

   public int size() {
      return 1;
   }

   public boolean contains(Object other) {
      return other.equals(this.o);
   }

   public E get(int index) {
      if (index != 0) {
         throw new IndexOutOfBoundsException("Singleton list; index = " + index);
      } else {
         return this.o;
      }
   }
}

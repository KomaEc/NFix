package soot.jimple.toolkits.typing.fast;

import java.util.AbstractList;

/** @deprecated */
@Deprecated
public class SingletonList<E> extends AbstractList<E> {
   private E e;

   public SingletonList(E e) {
      this.e = e;
   }

   public E get(int index) {
      if (index != 0) {
         throw new IndexOutOfBoundsException();
      } else {
         return this.e;
      }
   }

   public int size() {
      return 1;
   }
}

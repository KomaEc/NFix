package soot.jimple.toolkits.typing.fast;

import java.util.AbstractList;

/** @deprecated */
@Deprecated
public class EmptyList<E> extends AbstractList<E> {
   public E get(int index) {
      throw new IndexOutOfBoundsException();
   }

   public int size() {
      return 0;
   }
}

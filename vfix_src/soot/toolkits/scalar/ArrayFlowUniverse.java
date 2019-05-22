package soot.toolkits.scalar;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayFlowUniverse<E> implements FlowUniverse<E> {
   E[] elements;

   public ArrayFlowUniverse(E[] elements) {
      this.elements = elements;
   }

   public int size() {
      return this.elements.length;
   }

   public Iterator<E> iterator() {
      return Arrays.asList(this.elements).iterator();
   }

   public E[] toArray() {
      return this.elements;
   }
}

package soot.toolkits.scalar;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CollectionFlowUniverse<E> implements FlowUniverse<E> {
   Set<E> elements;

   public CollectionFlowUniverse(Collection<? extends E> elements) {
      this.elements = new HashSet(elements);
   }

   public int size() {
      return this.elements.size();
   }

   public Iterator<E> iterator() {
      return this.elements.iterator();
   }

   public E[] toArray() {
      return (Object[])this.elements.toArray();
   }
}

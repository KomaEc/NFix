package soot.toolkits.scalar;

import java.util.Iterator;

public interface FlowUniverse<E> extends Iterable<E> {
   int size();

   Iterator<E> iterator();

   E[] toArray();
}

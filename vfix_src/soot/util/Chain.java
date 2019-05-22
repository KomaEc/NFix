package soot.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface Chain<E> extends Collection<E>, Serializable {
   void insertBefore(List<E> var1, E var2);

   void insertAfter(List<E> var1, E var2);

   void insertAfter(E var1, E var2);

   void insertBefore(E var1, E var2);

   void insertBefore(Chain<E> var1, E var2);

   void insertAfter(Chain<E> var1, E var2);

   void insertOnEdge(E var1, E var2, E var3);

   void insertOnEdge(List<E> var1, E var2, E var3);

   void insertOnEdge(Chain<E> var1, E var2, E var3);

   void swapWith(E var1, E var2);

   boolean remove(Object var1);

   void addFirst(E var1);

   void addLast(E var1);

   void removeFirst();

   void removeLast();

   boolean follows(E var1, E var2);

   E getFirst();

   E getLast();

   E getSuccOf(E var1);

   E getPredOf(E var1);

   Iterator<E> snapshotIterator();

   Iterator<E> iterator();

   Iterator<E> iterator(E var1);

   Iterator<E> iterator(E var1, E var2);

   int size();

   long getModificationCount();

   Collection<E> getElementsUnsorted();
}

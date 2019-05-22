package org.jboss.util.collection;

import java.util.Collection;

public interface Queue<E> extends Collection<E> {
   int UNLIMITED_MAXIMUM_SIZE = -1;

   int getMaximumSize();

   void setMaximumSize(int var1) throws IllegalArgumentException;

   boolean isFull();

   boolean isEmpty();

   boolean add(E var1) throws FullCollectionException;

   E remove() throws EmptyCollectionException;

   E getFront() throws EmptyCollectionException;

   E getBack() throws EmptyCollectionException;
}

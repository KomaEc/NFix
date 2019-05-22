package edu.emory.mathcs.backport.java.util;

import java.util.Iterator;

public interface Deque extends Queue {
   void addFirst(Object var1);

   void addLast(Object var1);

   boolean offerFirst(Object var1);

   boolean offerLast(Object var1);

   Object removeFirst();

   Object removeLast();

   Object pollFirst();

   Object pollLast();

   Object getFirst();

   Object getLast();

   Object peekFirst();

   Object peekLast();

   boolean removeFirstOccurrence(Object var1);

   boolean removeLastOccurrence(Object var1);

   boolean add(Object var1);

   boolean offer(Object var1);

   Object remove();

   Object poll();

   Object element();

   Object peek();

   void push(Object var1);

   Object pop();

   boolean remove(Object var1);

   boolean contains(Object var1);

   int size();

   Iterator iterator();

   Iterator descendingIterator();
}

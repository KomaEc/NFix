package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.Deque;
import java.util.Iterator;

public interface BlockingDeque extends BlockingQueue, Deque {
   void addFirst(Object var1);

   void addLast(Object var1);

   boolean offerFirst(Object var1);

   boolean offerLast(Object var1);

   void putFirst(Object var1) throws InterruptedException;

   void putLast(Object var1) throws InterruptedException;

   boolean offerFirst(Object var1, long var2, TimeUnit var4) throws InterruptedException;

   boolean offerLast(Object var1, long var2, TimeUnit var4) throws InterruptedException;

   Object takeFirst() throws InterruptedException;

   Object takeLast() throws InterruptedException;

   Object pollFirst(long var1, TimeUnit var3) throws InterruptedException;

   Object pollLast(long var1, TimeUnit var3) throws InterruptedException;

   boolean removeFirstOccurrence(Object var1);

   boolean removeLastOccurrence(Object var1);

   boolean add(Object var1);

   boolean offer(Object var1);

   void put(Object var1) throws InterruptedException;

   boolean offer(Object var1, long var2, TimeUnit var4) throws InterruptedException;

   Object remove();

   Object poll();

   Object take() throws InterruptedException;

   Object poll(long var1, TimeUnit var3) throws InterruptedException;

   Object element();

   Object peek();

   boolean remove(Object var1);

   boolean contains(Object var1);

   int size();

   Iterator iterator();

   void push(Object var1);
}

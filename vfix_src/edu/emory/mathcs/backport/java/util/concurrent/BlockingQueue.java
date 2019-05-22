package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.Queue;
import java.util.Collection;

public interface BlockingQueue extends Queue {
   boolean add(Object var1);

   boolean offer(Object var1);

   void put(Object var1) throws InterruptedException;

   boolean offer(Object var1, long var2, TimeUnit var4) throws InterruptedException;

   Object take() throws InterruptedException;

   Object poll(long var1, TimeUnit var3) throws InterruptedException;

   int remainingCapacity();

   boolean remove(Object var1);

   boolean contains(Object var1);

   int drainTo(Collection var1);

   int drainTo(Collection var1, int var2);
}

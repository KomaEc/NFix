package org.jboss.util.timeout;

public interface TimeoutPriorityQueue {
   TimeoutExt offer(long var1, TimeoutTarget var3);

   TimeoutExt take();

   TimeoutExt poll();

   TimeoutExt poll(long var1);

   TimeoutExt peek();

   boolean remove(TimeoutExt var1);

   void clear();

   void cancel();

   int size();
}

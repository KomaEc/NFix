package soot.dava.toolkits.base.DavaMonitor;

import java.util.LinkedList;

class Lock {
   public Thread owner = null;
   public int level = 0;
   private final LinkedList<Thread> q = new LinkedList();

   public Thread nextThread() {
      return (Thread)this.q.getFirst();
   }

   public Thread deQ(Thread t) throws IllegalMonitorStateException {
      if (t != this.q.getFirst()) {
         throw new IllegalMonitorStateException();
      } else {
         return (Thread)this.q.removeFirst();
      }
   }

   public void enQ(Thread t) {
      this.q.add(t);
   }
}

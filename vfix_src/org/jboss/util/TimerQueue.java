package org.jboss.util;

public class TimerQueue extends WorkerQueue {
   private Heap m_heap;

   public TimerQueue() {
      this("TimerTask Thread");
   }

   public TimerQueue(String threadName) {
      super(threadName);
      this.m_heap = new Heap();
   }

   public void schedule(TimerTask t) {
      this.schedule(t, 0L);
   }

   public void schedule(TimerTask t, long delay) {
      if (t == null) {
         throw new IllegalArgumentException("Can't schedule a null TimerTask");
      } else {
         if (delay < 0L) {
            delay = 0L;
         }

         t.setNextExecutionTime(System.currentTimeMillis() + delay);
         this.putJob(t);
      }
   }

   protected void putJobImpl(Executable task) {
      this.m_heap.insert(task);
      ((TimerTask)task).setState(2);
      this.notifyAll();
   }

   protected Executable getJobImpl() throws InterruptedException {
      while(this.m_heap.peek() == null) {
         this.wait();
      }

      TimerTask task = (TimerTask)this.m_heap.extract();
      switch(task.getState()) {
      case 1:
      case 2:
         return task;
      case 3:
      case 4:
         task = null;
         return this.getJobImpl();
      default:
         throw new IllegalStateException("TimerTask has an illegal state");
      }
   }

   protected Runnable createQueueLoop() {
      return new TimerQueue.TimerTaskLoop();
   }

   protected void clear() {
      super.clear();
      synchronized(this) {
         this.m_heap.clear();
      }
   }

   protected class TimerTaskLoop implements Runnable {
      public void run() {
         try {
            while(true) {
               try {
                  TimerTask task = (TimerTask)TimerQueue.this.getJob();
                  long now = System.currentTimeMillis();
                  long executionTime = task.getNextExecutionTime();
                  long timeToWait = executionTime - now;
                  boolean runTask = timeToWait <= 0L;
                  if (!runTask) {
                     TimerQueue.this.putJob(task);
                     Object mutex = TimerQueue.this;
                     synchronized(mutex) {
                        mutex.wait(timeToWait);
                     }
                  } else {
                     if (task.isPeriodic()) {
                        task.setNextExecutionTime(executionTime + task.getPeriod());
                        TimerQueue.this.putJob(task);
                     } else {
                        task.setState(3);
                     }

                     task.execute();
                  }
               } catch (InterruptedException var18) {
                  return;
               } catch (Exception var19) {
                  ThrowableHandler.add(1, var19);
               }
            }
         } finally {
            TimerQueue.this.clear();
         }
      }
   }
}

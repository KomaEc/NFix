package org.jboss.util.threadpool;

public interface ThreadPool {
   void stop(boolean var1);

   void waitForTasks() throws InterruptedException;

   void waitForTasks(long var1) throws InterruptedException;

   void runTaskWrapper(TaskWrapper var1);

   void runTask(Task var1);

   void run(Runnable var1);

   void run(Runnable var1, long var2, long var4);
}

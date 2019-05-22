package org.jboss.util.threadpool;

public interface TaskWrapper extends Runnable {
   int getTaskWaitType();

   int getTaskPriority();

   long getTaskStartTimeout();

   long getTaskCompletionTimeout();

   void waitForTask();

   void stopTask();

   void acceptTask();

   void rejectTask(RuntimeException var1);

   boolean isComplete();
}

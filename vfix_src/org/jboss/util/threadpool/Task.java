package org.jboss.util.threadpool;

public interface Task {
   int WAIT_NONE = 0;
   int WAIT_FOR_START = 1;
   int WAIT_FOR_COMPLETE = 2;

   int getWaitType();

   int getPriority();

   long getStartTimeout();

   long getCompletionTimeout();

   void execute();

   void stop();

   void accepted(long var1);

   void rejected(long var1, Throwable var3);

   void started(long var1);

   void completed(long var1, Throwable var3);
}

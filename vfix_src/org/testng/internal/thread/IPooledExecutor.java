package org.testng.internal.thread;

public interface IPooledExecutor {
   void execute(Runnable var1);

   void shutdown();

   void awaitTermination(long var1) throws InterruptedException;

   boolean isTerminated();
}

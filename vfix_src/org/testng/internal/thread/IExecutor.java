package org.testng.internal.thread;

public interface IExecutor {
   IFutureResult submitRunnable(Runnable var1);

   void shutdown();

   boolean awaitTermination(long var1);

   void stopNow();

   StackTraceElement[][] getStackTraces();
}

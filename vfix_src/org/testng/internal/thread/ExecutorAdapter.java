package org.testng.internal.thread;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorAdapter extends ThreadPoolExecutor implements IExecutor {
   private IThreadFactory m_threadFactory;

   public ExecutorAdapter(int threadCount, IThreadFactory tf) {
      super(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), (ThreadFactory)tf.getThreadFactory());
      this.m_threadFactory = tf;
   }

   public IFutureResult submitRunnable(Runnable runnable) {
      return new FutureResultAdapter(super.submit(runnable));
   }

   public void stopNow() {
      super.shutdownNow();
   }

   public boolean awaitTermination(long timeout) {
      boolean result = false;

      try {
         result = super.awaitTermination(timeout, TimeUnit.MILLISECONDS);
      } catch (InterruptedException var5) {
         System.out.println("[WARN] ThreadPoolExecutor has been interrupted while awaiting termination");
         Thread.currentThread().interrupt();
      }

      return result;
   }

   public StackTraceElement[][] getStackTraces() {
      List<Thread> threads = this.m_threadFactory.getThreads();
      int threadCount = threads.size();
      StackTraceElement[][] result = new StackTraceElement[threadCount][];

      for(int i = 0; i < result.length; ++i) {
         result[i] = ((Thread)threads.get(i)).getStackTrace();
      }

      return result;
   }
}

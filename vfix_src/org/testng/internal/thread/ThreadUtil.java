package org.testng.internal.thread;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.testng.collections.Lists;
import org.testng.internal.Utils;

public class ThreadUtil {
   private static final String THREAD_NAME = "TestNG";

   public static boolean isTestNGThread() {
      return Thread.currentThread().getName().contains("TestNG");
   }

   public static final void execute(List<? extends Runnable> tasks, int threadPoolSize, long timeout, boolean triggerAtOnce) {
      new CountDownLatch(1);
      new CountDownLatch(tasks.size());
      Utils.log("ThreadUtil", 2, "Starting executor timeOut:" + timeout + "ms" + " workers:" + tasks.size() + " threadPoolSize:" + threadPoolSize);
      ExecutorService pooledExecutor = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, timeout, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
         public Thread newThread(Runnable r) {
            Thread result = new Thread(r);
            result.setName("TestNG");
            return result;
         }
      });
      List<Callable<Object>> callables = Lists.newArrayList();
      Iterator i$ = tasks.iterator();

      while(i$.hasNext()) {
         final Runnable task = (Runnable)i$.next();
         callables.add(new Callable<Object>() {
            public Object call() throws Exception {
               task.run();
               return null;
            }
         });
      }

      try {
         if (timeout != 0L) {
            pooledExecutor.invokeAll(callables, timeout, TimeUnit.MILLISECONDS);
         } else {
            pooledExecutor.invokeAll(callables);
         }
      } catch (InterruptedException var14) {
         var14.printStackTrace();
         Thread.currentThread().interrupt();
      } finally {
         pooledExecutor.shutdown();
      }

   }

   public static final String currentThreadInfo() {
      Thread thread = Thread.currentThread();
      return String.valueOf(thread.getName() + "@" + thread.hashCode());
   }

   public static final IExecutor createExecutor(int threadCount, String threadFactoryName) {
      return new ExecutorAdapter(threadCount, createFactory(threadFactoryName));
   }

   public static final IAtomicInteger createAtomicInteger(int initialValue) {
      return new AtomicIntegerAdapter(initialValue);
   }

   private static final IThreadFactory createFactory(String name) {
      return new ThreadUtil.ThreadFactoryImpl(name);
   }

   private static void log(int level, String msg) {
      Utils.log("ThreadUtil:" + currentThreadInfo(), level, msg);
   }

   private static class CountDownLatchedRunnable implements Runnable {
      private final Runnable m_task;
      private final CountDownLatch m_startGate;
      private final CountDownLatch m_endGate;

      public CountDownLatchedRunnable(Runnable task, CountDownLatch endGate, CountDownLatch startGate) {
         this.m_task = task;
         this.m_startGate = startGate;
         this.m_endGate = endGate;
      }

      public void run() {
         if (null != this.m_startGate) {
            try {
               this.m_startGate.await();
            } catch (InterruptedException var6) {
               ThreadUtil.log(2, "Cannot wait for startup gate when executing " + this.m_task + "; thread was already interrupted " + var6.getMessage());
               Thread.currentThread().interrupt();
               return;
            }
         }

         try {
            this.m_task.run();
         } finally {
            this.m_endGate.countDown();
         }

      }
   }

   public static class ThreadFactoryImpl implements IThreadFactory, ThreadFactory {
      private String m_methodName;
      private List<Thread> m_threads = Lists.newArrayList();

      public ThreadFactoryImpl(String name) {
         this.m_methodName = name;
      }

      public Thread newThread(Runnable run) {
         Thread result = new TestNGThread(run, this.m_methodName);
         this.m_threads.add(result);
         return result;
      }

      public Object getThreadFactory() {
         return this;
      }

      public List<Thread> getThreads() {
         return this.m_threads;
      }
   }
}

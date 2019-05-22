package org.jboss.util.timeout;

import java.util.concurrent.atomic.AtomicBoolean;
import org.jboss.util.NestedRuntimeException;
import org.jboss.util.ThrowableHandler;
import org.jboss.util.threadpool.BasicThreadPool;
import org.jboss.util.threadpool.BlockingMode;
import org.jboss.util.threadpool.ThreadPool;

public class TimeoutFactory {
   private static final String priorityQueueProperty = TimeoutPriorityQueue.class.getName();
   private static final String priorityQueueName = TimeoutPriorityQueueImpl.class.getName();
   private static TimeoutFactory singleton;
   private static int timeoutFactoriesCount = 0;
   private static Class<?> priorityQueueClass;
   private static BasicThreadPool DEFAULT_TP = new BasicThreadPool("Timeouts");
   private AtomicBoolean cancelled;
   private Thread workerThread;
   private ThreadPool threadPool;
   private TimeoutPriorityQueue queue;

   public static synchronized TimeoutFactory getSingleton() {
      if (singleton == null) {
         singleton = new TimeoutFactory(DEFAULT_TP);
      }

      return singleton;
   }

   public static Timeout createTimeout(long time, TimeoutTarget target) {
      return getSingleton().schedule(time, target);
   }

   public TimeoutFactory(ThreadPool threadPool) {
      this.cancelled = new AtomicBoolean(false);
      this.threadPool = threadPool;

      try {
         this.queue = (TimeoutPriorityQueue)priorityQueueClass.newInstance();
      } catch (Exception var3) {
         throw new RuntimeException("Cannot instantiate " + priorityQueueClass, var3);
      }

      this.workerThread = new Thread("TimeoutFactory-" + timeoutFactoriesCount++) {
         public void run() {
            TimeoutFactory.this.doWork();
         }
      };
      this.workerThread.setDaemon(true);
      this.workerThread.start();
   }

   public TimeoutFactory() {
      this(DEFAULT_TP);
   }

   public Timeout schedule(long time, TimeoutTarget target) {
      if (this.cancelled.get()) {
         throw new IllegalStateException("TimeoutFactory has been cancelled");
      } else if (time < 0L) {
         throw new IllegalArgumentException("Negative time");
      } else if (target == null) {
         throw new IllegalArgumentException("Null timeout target");
      } else {
         return this.queue.offer(time, target);
      }
   }

   public Timeout schedule(long time, Runnable run) {
      return this.schedule(time, (TimeoutTarget)(new TimeoutFactory.TimeoutTargetImpl(run)));
   }

   public void cancel() {
      if (!this.cancelled.getAndSet(true)) {
         this.queue.cancel();
      }

   }

   public boolean isCancelled() {
      return this.cancelled.get();
   }

   private void doWork() {
      while(!this.cancelled.get()) {
         TimeoutExt work = this.queue.take();
         if (work != null) {
            TimeoutFactory.TimeoutWorker worker = new TimeoutFactory.TimeoutWorker(work);

            try {
               this.threadPool.run(worker);
            } catch (Throwable var6) {
               ThrowableHandler.add(1, var6);
            }

            synchronized(work) {
               work.done();
            }
         }
      }

      this.queue.cancel();
   }

   static {
      DEFAULT_TP.setBlockingMode(BlockingMode.RUN);
      String priorityQueueClassName = priorityQueueName;
      ClassLoader cl = TimeoutFactory.class.getClassLoader();

      try {
         priorityQueueClassName = System.getProperty(priorityQueueProperty, priorityQueueName);
         cl = Thread.currentThread().getContextClassLoader();
      } catch (Exception var4) {
      }

      try {
         priorityQueueClass = cl.loadClass(priorityQueueClassName);
      } catch (Exception var3) {
         throw new NestedRuntimeException(var3.toString(), var3);
      }
   }

   private static class TimeoutTargetImpl implements TimeoutTarget {
      Runnable runnable;

      TimeoutTargetImpl(Runnable runnable) {
         this.runnable = runnable;
      }

      public void timedOut(Timeout ignored) {
         this.runnable.run();
      }
   }

   private static class TimeoutWorker implements Runnable {
      private TimeoutExt work;

      TimeoutWorker(TimeoutExt work) {
         this.work = work;
      }

      public void run() {
         try {
            this.work.getTimeoutTarget().timedOut(this.work);
         } catch (Throwable var4) {
            ThrowableHandler.add(1, var4);
         }

         synchronized(this.work) {
            this.work.done();
         }
      }
   }
}

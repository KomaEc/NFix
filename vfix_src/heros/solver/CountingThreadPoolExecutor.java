package heros.solver;

import heros.util.SootThreadGroup;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountingThreadPoolExecutor extends ThreadPoolExecutor {
   protected static final Logger logger = LoggerFactory.getLogger(CountingThreadPoolExecutor.class);
   protected final CountLatch numRunningTasks = new CountLatch(0);
   protected volatile Throwable exception = null;

   public CountingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
      super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadFactory() {
         public Thread newThread(Runnable r) {
            return new Thread(new SootThreadGroup(), r);
         }
      });
   }

   public void execute(Runnable command) {
      try {
         this.numRunningTasks.increment();
         super.execute(command);
      } catch (RejectedExecutionException var3) {
         this.numRunningTasks.decrement();
         throw var3;
      }
   }

   protected void afterExecute(Runnable r, Throwable t) {
      if (t != null) {
         this.exception = t;
         logger.error("Worker thread execution failed: " + t.getMessage(), t);
         this.shutdownNow();
         this.numRunningTasks.resetAndInterrupt();
      } else {
         this.numRunningTasks.decrement();
      }

      super.afterExecute(r, t);
   }

   public void awaitCompletion() throws InterruptedException {
      this.numRunningTasks.awaitZero();
   }

   public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException {
      this.numRunningTasks.awaitZero(timeout, unit);
   }

   public Throwable getException() {
      return this.exception;
   }
}

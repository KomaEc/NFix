package org.junit.internal.runners.statements;

import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestTimedOutException;

public class FailOnTimeout extends Statement {
   private final Statement originalStatement;
   private final TimeUnit timeUnit;
   private final long timeout;
   private final boolean lookForStuckThread;
   private volatile ThreadGroup threadGroup;

   public static FailOnTimeout.Builder builder() {
      return new FailOnTimeout.Builder();
   }

   /** @deprecated */
   @Deprecated
   public FailOnTimeout(Statement statement, long timeoutMillis) {
      this(builder().withTimeout(timeoutMillis, TimeUnit.MILLISECONDS), statement);
   }

   private FailOnTimeout(FailOnTimeout.Builder builder, Statement statement) {
      this.threadGroup = null;
      this.originalStatement = statement;
      this.timeout = builder.timeout;
      this.timeUnit = builder.unit;
      this.lookForStuckThread = builder.lookForStuckThread;
   }

   public void evaluate() throws Throwable {
      FailOnTimeout.CallableStatement callable = new FailOnTimeout.CallableStatement();
      FutureTask<Throwable> task = new FutureTask(callable);
      this.threadGroup = new ThreadGroup("FailOnTimeoutGroup");
      Thread thread = new Thread(this.threadGroup, task, "Time-limited test");
      thread.setDaemon(true);
      thread.start();
      callable.awaitStarted();
      Throwable throwable = this.getResult(task, thread);
      if (throwable != null) {
         throw throwable;
      }
   }

   private Throwable getResult(FutureTask<Throwable> task, Thread thread) {
      try {
         return this.timeout > 0L ? (Throwable)task.get(this.timeout, this.timeUnit) : (Throwable)task.get();
      } catch (InterruptedException var4) {
         return var4;
      } catch (ExecutionException var5) {
         return var5.getCause();
      } catch (TimeoutException var6) {
         return this.createTimeoutException(thread);
      }
   }

   private Exception createTimeoutException(Thread thread) {
      StackTraceElement[] stackTrace = thread.getStackTrace();
      Thread stuckThread = this.lookForStuckThread ? this.getStuckThread(thread) : null;
      Exception currThreadException = new TestTimedOutException(this.timeout, this.timeUnit);
      if (stackTrace != null) {
         currThreadException.setStackTrace(stackTrace);
         thread.interrupt();
      }

      if (stuckThread != null) {
         Exception stuckThreadException = new Exception("Appears to be stuck in thread " + stuckThread.getName());
         stuckThreadException.setStackTrace(this.getStackTrace(stuckThread));
         return new MultipleFailureException(Arrays.asList(currThreadException, stuckThreadException));
      } else {
         return currThreadException;
      }
   }

   private StackTraceElement[] getStackTrace(Thread thread) {
      try {
         return thread.getStackTrace();
      } catch (SecurityException var3) {
         return new StackTraceElement[0];
      }
   }

   private Thread getStuckThread(Thread mainThread) {
      if (this.threadGroup == null) {
         return null;
      } else {
         Thread[] threadsInGroup = this.getThreadArray(this.threadGroup);
         if (threadsInGroup == null) {
            return null;
         } else {
            Thread stuckThread = null;
            long maxCpuTime = 0L;
            Thread[] arr$ = threadsInGroup;
            int len$ = threadsInGroup.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Thread thread = arr$[i$];
               if (thread.getState() == State.RUNNABLE) {
                  long threadCpuTime = this.cpuTime(thread);
                  if (stuckThread == null || threadCpuTime > maxCpuTime) {
                     stuckThread = thread;
                     maxCpuTime = threadCpuTime;
                  }
               }
            }

            return stuckThread == mainThread ? null : stuckThread;
         }
      }
   }

   private Thread[] getThreadArray(ThreadGroup group) {
      int count = group.activeCount();
      int enumSize = Math.max(count * 2, 100);
      int loopCount = 0;

      do {
         Thread[] threads = new Thread[enumSize];
         int enumCount = group.enumerate(threads);
         if (enumCount < enumSize) {
            return this.copyThreads(threads, enumCount);
         }

         enumSize += 100;
         ++loopCount;
      } while(loopCount < 5);

      return null;
   }

   private Thread[] copyThreads(Thread[] threads, int count) {
      int length = Math.min(count, threads.length);
      Thread[] result = new Thread[length];

      for(int i = 0; i < length; ++i) {
         result[i] = threads[i];
      }

      return result;
   }

   private long cpuTime(Thread thr) {
      ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
      if (mxBean.isThreadCpuTimeSupported()) {
         try {
            return mxBean.getThreadCpuTime(thr.getId());
         } catch (UnsupportedOperationException var4) {
         }
      }

      return 0L;
   }

   // $FF: synthetic method
   FailOnTimeout(FailOnTimeout.Builder x0, Statement x1, Object x2) {
      this(x0, x1);
   }

   private class CallableStatement implements Callable<Throwable> {
      private final CountDownLatch startLatch;

      private CallableStatement() {
         this.startLatch = new CountDownLatch(1);
      }

      public Throwable call() throws Exception {
         try {
            this.startLatch.countDown();
            FailOnTimeout.this.originalStatement.evaluate();
            return null;
         } catch (Exception var2) {
            throw var2;
         } catch (Throwable var3) {
            return var3;
         }
      }

      public void awaitStarted() throws InterruptedException {
         this.startLatch.await();
      }

      // $FF: synthetic method
      CallableStatement(Object x1) {
         this();
      }
   }

   public static class Builder {
      private boolean lookForStuckThread;
      private long timeout;
      private TimeUnit unit;

      private Builder() {
         this.lookForStuckThread = false;
         this.timeout = 0L;
         this.unit = TimeUnit.SECONDS;
      }

      public FailOnTimeout.Builder withTimeout(long timeout, TimeUnit unit) {
         if (timeout < 0L) {
            throw new IllegalArgumentException("timeout must be non-negative");
         } else if (unit == null) {
            throw new NullPointerException("TimeUnit cannot be null");
         } else {
            this.timeout = timeout;
            this.unit = unit;
            return this;
         }
      }

      public FailOnTimeout.Builder withLookingForStuckThread(boolean enable) {
         this.lookForStuckThread = enable;
         return this;
      }

      public FailOnTimeout build(Statement statement) {
         if (statement == null) {
            throw new NullPointerException("statement cannot be null");
         } else {
            return new FailOnTimeout(this, statement);
         }
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}

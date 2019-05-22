package org.jboss.util.threadpool;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.jboss.logging.Logger;
import org.jboss.util.collection.WeakValueHashMap;
import org.jboss.util.loading.ClassLoaderSource;
import org.jboss.util.loading.ContextClassLoaderSwitcher;

public class BasicThreadPool implements ThreadPool, BasicThreadPoolMBean {
   private static final ThreadGroup JBOSS_THREAD_GROUP = new ThreadGroup("JBoss Pooled Threads");
   private static final Map threadGroups = Collections.synchronizedMap(new WeakValueHashMap());
   private static final AtomicInteger lastPoolNumber = new AtomicInteger(0);
   private static Logger log = Logger.getLogger(BasicThreadPool.class);
   private String name;
   private int poolNumber;
   private BlockingMode blockingMode;
   private ThreadPoolExecutor executor;
   private LinkedBlockingQueue queue;
   private ThreadGroup threadGroup;
   private ClassLoaderSource classLoaderSource;
   private ContextClassLoaderSwitcher classLoaderSwitcher;
   private AtomicInteger lastThreadNumber;
   private AtomicBoolean stopped;
   private PriorityBlockingQueue<BasicThreadPool.TimeoutInfo> tasksWithTimeouts;
   private BasicThreadPool.TimeoutMonitor timeoutTask;
   private boolean trace;

   public BasicThreadPool() {
      this("ThreadPool");
   }

   public BasicThreadPool(String name) {
      this(name, JBOSS_THREAD_GROUP);
   }

   public BasicThreadPool(String name, ThreadGroup threadGroup) {
      this.blockingMode = BlockingMode.ABORT;
      this.lastThreadNumber = new AtomicInteger(0);
      this.stopped = new AtomicBoolean(false);
      this.tasksWithTimeouts = new PriorityBlockingQueue(13);
      this.trace = log.isTraceEnabled();
      ThreadFactory factory = new BasicThreadPool.ThreadPoolThreadFactory();
      this.queue = new LinkedBlockingQueue(1024);
      this.executor = new BasicThreadPool.RestoreTCCLThreadPoolExecutor(4, 4, 60L, TimeUnit.SECONDS, this.queue);
      this.executor.setThreadFactory(factory);
      this.executor.setRejectedExecutionHandler(new AbortPolicy());
      this.poolNumber = lastPoolNumber.incrementAndGet();
      this.setName(name);
      this.threadGroup = threadGroup;
   }

   public void stop(boolean immediate) {
      log.debug("stop, immediate=" + immediate);
      this.stopped.set(true);
      if (immediate) {
         this.executor.shutdownNow();
      } else {
         this.executor.shutdown();
      }

   }

   public void waitForTasks() throws InterruptedException {
      this.executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
   }

   public void waitForTasks(long maxWaitTime) throws InterruptedException {
      this.executor.awaitTermination(maxWaitTime, TimeUnit.MILLISECONDS);
   }

   public void runTaskWrapper(TaskWrapper wrapper) {
      if (this.trace) {
         log.trace("runTaskWrapper, wrapper=" + wrapper);
      }

      if (this.stopped.get()) {
         wrapper.rejectTask(new ThreadPoolStoppedException("Thread pool has been stopped"));
      } else {
         wrapper.acceptTask();
         long completionTimeout = wrapper.getTaskCompletionTimeout();
         if (completionTimeout > 0L) {
            this.checkTimeoutMonitor();
            BasicThreadPool.TimeoutInfo info = new BasicThreadPool.TimeoutInfo(wrapper, completionTimeout);
            this.tasksWithTimeouts.add(info);
         }

         int waitType = wrapper.getTaskWaitType();
         switch(waitType) {
         case 2:
            this.executeOnThread(wrapper);
            break;
         default:
            this.execute(wrapper);
         }

         this.waitForTask(wrapper);
      }
   }

   public void runTask(Task task) {
      BasicTaskWrapper wrapper = new BasicTaskWrapper(task);
      this.runTaskWrapper(wrapper);
   }

   public void run(Runnable runnable) {
      this.run(runnable, 0L, 0L);
   }

   public void run(Runnable runnable, long startTimeout, long completeTimeout) {
      RunnableTaskWrapper wrapper = new RunnableTaskWrapper(runnable, startTimeout, completeTimeout);
      this.runTaskWrapper(wrapper);
   }

   public ThreadGroup getThreadGroup() {
      return this.threadGroup;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getPoolNumber() {
      return this.poolNumber;
   }

   public String getThreadGroupName() {
      return this.threadGroup.getName();
   }

   public void setThreadGroupName(String threadGroupName) {
      ThreadGroup group;
      synchronized(threadGroups) {
         group = (ThreadGroup)threadGroups.get(threadGroupName);
         if (group == null) {
            group = new ThreadGroup(JBOSS_THREAD_GROUP, threadGroupName);
            threadGroups.put(threadGroupName, group);
         }
      }

      this.threadGroup = group;
   }

   public int getQueueSize() {
      return this.queue.size();
   }

   public int getMaximumQueueSize() {
      return this.queue.remainingCapacity() + this.queue.size();
   }

   public void setMaximumQueueSize(int size) {
      ArrayList tmp = new ArrayList();
      this.queue.drainTo(tmp);
      this.queue = new LinkedBlockingQueue(size);
      this.queue.addAll(tmp);
      ThreadFactory tf = this.executor.getThreadFactory();
      RejectedExecutionHandler handler = this.executor.getRejectedExecutionHandler();
      long keepAlive = this.executor.getKeepAliveTime(TimeUnit.SECONDS);
      int cs = this.executor.getCorePoolSize();
      int mcs = this.executor.getMaximumPoolSize();
      this.executor = new ThreadPoolExecutor(cs, mcs, keepAlive, TimeUnit.SECONDS, this.queue);
      this.executor.setThreadFactory(tf);
      this.executor.setRejectedExecutionHandler(handler);
   }

   public int getPoolSize() {
      return this.executor.getPoolSize();
   }

   public int getMinimumPoolSize() {
      return this.executor.getCorePoolSize();
   }

   public void setMinimumPoolSize(int size) {
      synchronized(this.executor) {
         if (this.executor.getMaximumPoolSize() < size) {
            this.executor.setCorePoolSize(size);
            this.executor.setMaximumPoolSize(size);
         }

      }
   }

   public int getMaximumPoolSize() {
      return this.executor.getMaximumPoolSize();
   }

   public void setMaximumPoolSize(int size) {
      synchronized(this.executor) {
         this.executor.setCorePoolSize(size);
         this.executor.setMaximumPoolSize(size);
      }
   }

   public long getKeepAliveTime() {
      return this.executor.getKeepAliveTime(TimeUnit.MILLISECONDS);
   }

   public void setKeepAliveTime(long time) {
      this.executor.setKeepAliveTime(time, TimeUnit.MILLISECONDS);
   }

   public BlockingMode getBlockingMode() {
      return this.blockingMode;
   }

   public void setBlockingMode(BlockingMode mode) {
      this.blockingMode = mode;
      if (this.blockingMode == BlockingMode.RUN) {
         this.executor.setRejectedExecutionHandler(new CallerRunsPolicy());
      } else if (this.blockingMode == BlockingMode.WAIT) {
         this.executor.setRejectedExecutionHandler(new CallerRunsPolicy());
      } else if (this.blockingMode == BlockingMode.DISCARD) {
         this.executor.setRejectedExecutionHandler(new DiscardPolicy());
      } else if (this.blockingMode == BlockingMode.DISCARD_OLDEST) {
         this.executor.setRejectedExecutionHandler(new DiscardOldestPolicy());
      } else {
         if (this.blockingMode != BlockingMode.ABORT) {
            throw new IllegalArgumentException("Failed to recognize mode: " + mode);
         }

         this.executor.setRejectedExecutionHandler(new AbortPolicy());
      }

   }

   public void setBlockingMode(String name) {
      this.blockingMode = BlockingMode.toBlockingMode(name);
      if (this.blockingMode == null) {
         this.blockingMode = BlockingMode.ABORT;
      }

   }

   public void setBlockingModeString(String name) {
      this.blockingMode = BlockingMode.toBlockingMode(name);
      if (this.blockingMode == null) {
         this.blockingMode = BlockingMode.ABORT;
      }

   }

   public ClassLoaderSource getClassLoaderSource() {
      return this.classLoaderSource;
   }

   public void setClassLoaderSource(ClassLoaderSource classLoaderSource) {
      if (classLoaderSource == null) {
         this.classLoaderSource = null;
         this.classLoaderSwitcher = null;
      } else if (this.classLoaderSwitcher == null) {
         try {
            this.classLoaderSwitcher = (ContextClassLoaderSwitcher)AccessController.doPrivileged(ContextClassLoaderSwitcher.INSTANTIATOR);
            this.classLoaderSource = classLoaderSource;
         } catch (SecurityException var3) {
            log.error("Cannot manage context classloader for pool threads; Do not have setContextClassLoader permission");
         }
      } else {
         this.classLoaderSource = classLoaderSource;
      }

   }

   public ThreadPool getInstance() {
      return this;
   }

   public void stop() {
      this.stop(false);
   }

   public String toString() {
      return this.name + '(' + this.poolNumber + ')';
   }

   protected void executeOnThread(TaskWrapper wrapper) {
      if (this.trace) {
         log.trace("executeOnThread, wrapper=" + wrapper);
      }

      wrapper.run();
   }

   protected void execute(TaskWrapper wrapper) {
      if (this.trace) {
         log.trace("execute, wrapper=" + wrapper);
      }

      try {
         this.executor.execute(wrapper);
      } catch (Throwable var3) {
         wrapper.rejectTask(new ThreadPoolFullException("Error scheduling work: " + wrapper, var3));
      }

   }

   protected void waitForTask(TaskWrapper wrapper) {
      wrapper.waitForTask();
   }

   protected synchronized void checkTimeoutMonitor() {
      if (this.timeoutTask == null) {
         this.timeoutTask = new BasicThreadPool.TimeoutMonitor(this.name, log);
      }

   }

   protected BasicThreadPool.TimeoutInfo getNextTimeout() {
      return (BasicThreadPool.TimeoutInfo)this.tasksWithTimeouts.poll();
   }

   protected void setDefaultThreadContextClassLoader(Thread thread) {
      if (this.classLoaderSwitcher != null) {
         ClassLoader cl = this.classLoaderSource == null ? null : this.classLoaderSource.getClassLoader();
         this.classLoaderSwitcher.setContextClassLoader(thread, cl);
      }

   }

   private class TimeoutMonitor implements Runnable {
      final Logger log;

      TimeoutMonitor(String name, Logger log) {
         this.log = log;
         Thread t = new Thread(this, name + " TimeoutMonitor");
         t.setDaemon(true);
         t.start();
      }

      public void run() {
         for(boolean isStopped = BasicThreadPool.this.stopped.get(); !isStopped; isStopped = BasicThreadPool.this.stopped.get()) {
            boolean trace = this.log.isTraceEnabled();

            try {
               BasicThreadPool.TimeoutInfo info = BasicThreadPool.this.getNextTimeout();
               if (info != null) {
                  long now = System.currentTimeMillis();
                  long timeToTimeout = info.getTaskCompletionTimeout(now);
                  if (timeToTimeout > 0L) {
                     if (trace) {
                        this.log.trace("Will check wrapper=" + info.getTaskWrapper() + " after " + timeToTimeout);
                     }

                     Thread.sleep(timeToTimeout);
                  }

                  TaskWrapper wrapper = info.getTaskWrapper();
                  if (!wrapper.isComplete()) {
                     if (trace) {
                        this.log.trace("Failed completion check for wrapper=" + wrapper);
                     }

                     if (info.stopTask()) {
                        info.setTimeout(1000L);
                        BasicThreadPool.this.tasksWithTimeouts.add(info);
                        if (trace) {
                           this.log.trace("Rescheduled completion check for wrapper=" + wrapper);
                        }
                     }
                  }
               } else {
                  Thread.sleep(1000L);
               }
            } catch (InterruptedException var9) {
               this.log.debug("Timeout monitor has been interrupted", var9);
            } catch (Throwable var10) {
               this.log.debug("Timeout monitor saw unexpected error", var10);
            }
         }

      }
   }

   private static class TimeoutInfo implements Comparable {
      long start = System.currentTimeMillis();
      long timeoutMS;
      TaskWrapper wrapper;
      boolean firstStop;

      TimeoutInfo(TaskWrapper wrapper, long timeout) {
         this.timeoutMS = this.start + timeout;
         this.wrapper = wrapper;
      }

      public void setTimeout(long timeout) {
         this.start = System.currentTimeMillis();
         this.timeoutMS = this.start + timeout;
      }

      public int compareTo(Object o) {
         BasicThreadPool.TimeoutInfo ti = (BasicThreadPool.TimeoutInfo)o;
         long to0 = this.timeoutMS;
         long to1 = ti.timeoutMS;
         return (int)(to0 - to1);
      }

      TaskWrapper getTaskWrapper() {
         return this.wrapper;
      }

      public long getTaskCompletionTimeout() {
         return this.wrapper.getTaskCompletionTimeout();
      }

      public long getTaskCompletionTimeout(long now) {
         return this.timeoutMS - now;
      }

      public boolean stopTask() {
         this.wrapper.stopTask();
         boolean wasFirstStop = !this.firstStop;
         this.firstStop = true;
         return wasFirstStop;
      }
   }

   private class RestoreTCCLThreadPoolExecutor extends ThreadPoolExecutor {
      public RestoreTCCLThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
         super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
      }

      protected void afterExecute(Runnable r, Throwable t) {
         try {
            super.afterExecute(r, t);
         } finally {
            BasicThreadPool.this.setDefaultThreadContextClassLoader(Thread.currentThread());
         }

      }
   }

   private class ThreadPoolThreadFactory implements ThreadFactory {
      private ThreadPoolThreadFactory() {
      }

      public Thread newThread(Runnable runnable) {
         String threadName = BasicThreadPool.this.toString() + "-" + BasicThreadPool.this.lastThreadNumber.incrementAndGet();
         Thread thread = new Thread(BasicThreadPool.this.threadGroup, runnable, threadName);
         thread.setDaemon(true);
         BasicThreadPool.this.setDefaultThreadContextClassLoader(thread);
         return thread;
      }

      // $FF: synthetic method
      ThreadPoolThreadFactory(Object x1) {
         this();
      }
   }
}

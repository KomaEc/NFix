package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;
import edu.emory.mathcs.backport.java.util.concurrent.locks.Condition;
import edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ThreadPoolExecutor extends AbstractExecutorService {
   private final AtomicInteger ctl;
   private static final int COUNT_BITS = 29;
   private static final int CAPACITY = 536870911;
   private static final int RUNNING = -536870912;
   private static final int SHUTDOWN = 0;
   private static final int STOP = 536870912;
   private static final int TIDYING = 1073741824;
   private static final int TERMINATED = 1610612736;
   private final BlockingQueue workQueue;
   private final ReentrantLock mainLock;
   private final HashSet workers;
   private final Condition termination;
   private int largestPoolSize;
   private long completedTaskCount;
   private volatile ThreadFactory threadFactory;
   private volatile RejectedExecutionHandler handler;
   private volatile long keepAliveTime;
   private volatile boolean allowCoreThreadTimeOut;
   private volatile int corePoolSize;
   private volatile int maximumPoolSize;
   private static final RejectedExecutionHandler defaultHandler = new ThreadPoolExecutor.AbortPolicy();
   private static final RuntimePermission shutdownPerm = new RuntimePermission("modifyThread");
   private static final boolean ONLY_ONE = true;

   private static int runStateOf(int c) {
      return c & -536870912;
   }

   private static int workerCountOf(int c) {
      return c & 536870911;
   }

   private static int ctlOf(int rs, int wc) {
      return rs | wc;
   }

   private static boolean runStateLessThan(int c, int s) {
      return c < s;
   }

   private static boolean runStateAtLeast(int c, int s) {
      return c >= s;
   }

   private static boolean isRunning(int c) {
      return c < 0;
   }

   private boolean compareAndIncrementWorkerCount(int expect) {
      return this.ctl.compareAndSet(expect, expect + 1);
   }

   private boolean compareAndDecrementWorkerCount(int expect) {
      return this.ctl.compareAndSet(expect, expect - 1);
   }

   private void decrementWorkerCount() {
      while(!this.compareAndDecrementWorkerCount(this.ctl.get())) {
      }

   }

   private void advanceRunState(int targetState) {
      int c;
      do {
         c = this.ctl.get();
      } while(!runStateAtLeast(c, targetState) && !this.ctl.compareAndSet(c, ctlOf(targetState, workerCountOf(c))));

   }

   final void tryTerminate() {
      while(true) {
         int c = this.ctl.get();
         if (isRunning(c) || runStateAtLeast(c, 1073741824) || runStateOf(c) == 0 && !this.workQueue.isEmpty()) {
            return;
         }

         if (workerCountOf(c) != 0) {
            this.interruptIdleWorkers(true);
            return;
         }

         ReentrantLock mainLock = this.mainLock;
         mainLock.lock();

         try {
            if (!this.ctl.compareAndSet(c, ctlOf(1073741824, 0))) {
               continue;
            }

            try {
               this.terminated();
            } finally {
               this.ctl.set(ctlOf(1610612736, 0));
               this.termination.signalAll();
            }
         } finally {
            mainLock.unlock();
         }

         return;
      }
   }

   private void checkShutdownAccess() {
      SecurityManager security = System.getSecurityManager();
      if (security != null) {
         security.checkPermission(shutdownPerm);
         ReentrantLock mainLock = this.mainLock;
         mainLock.lock();

         try {
            Iterator itr = this.workers.iterator();

            while(itr.hasNext()) {
               ThreadPoolExecutor.Worker w = (ThreadPoolExecutor.Worker)itr.next();
               security.checkAccess(w.thread);
            }
         } finally {
            mainLock.unlock();
         }
      }

   }

   private void interruptWorkers() {
      ReentrantLock mainLock = this.mainLock;
      mainLock.lock();

      try {
         Iterator itr = this.workers.iterator();

         while(itr.hasNext()) {
            ThreadPoolExecutor.Worker w = (ThreadPoolExecutor.Worker)itr.next();

            try {
               w.thread.interrupt();
            } catch (SecurityException var8) {
            }
         }
      } finally {
         mainLock.unlock();
      }

   }

   private void interruptIdleWorkers(boolean onlyOne) {
      ReentrantLock mainLock = this.mainLock;
      mainLock.lock();

      try {
         Iterator it = this.workers.iterator();

         while(it.hasNext()) {
            ThreadPoolExecutor.Worker w = (ThreadPoolExecutor.Worker)it.next();
            Thread t = w.thread;
            if (!t.isInterrupted() && w.tryLock()) {
               try {
                  t.interrupt();
               } catch (SecurityException var15) {
               } finally {
                  w.unlock();
               }
            }

            if (onlyOne) {
               break;
            }
         }
      } finally {
         mainLock.unlock();
      }

   }

   private void interruptIdleWorkers() {
      this.interruptIdleWorkers(false);
   }

   private void clearInterruptsForTaskRun() {
      if (runStateLessThan(this.ctl.get(), 536870912) && Thread.interrupted() && runStateAtLeast(this.ctl.get(), 536870912)) {
         Thread.currentThread().interrupt();
      }

   }

   final void reject(Runnable command) {
      this.handler.rejectedExecution(command, this);
   }

   void onShutdown() {
   }

   final boolean isRunningOrShutdown(boolean shutdownOK) {
      int rs = runStateOf(this.ctl.get());
      return rs == -536870912 || rs == 0 && shutdownOK;
   }

   private List drainQueue() {
      BlockingQueue q = this.workQueue;
      List taskList = new ArrayList();
      q.drainTo(taskList);
      if (!q.isEmpty()) {
         Runnable[] arr = (Runnable[])q.toArray(new Runnable[0]);

         for(int i = 0; i < arr.length; ++i) {
            Runnable r = arr[i];
            if (q.remove(r)) {
               taskList.add(r);
            }
         }
      }

      return taskList;
   }

   private boolean addWorker(Runnable firstTask, boolean core) {
      while(true) {
         int c = this.ctl.get();
         int rs = runStateOf(c);
         if (rs >= 0 && (rs != 0 || firstTask != null || this.workQueue.isEmpty())) {
            return false;
         }

         while(true) {
            int wc = workerCountOf(c);
            if (wc >= 536870911 || wc >= (core ? this.corePoolSize : this.maximumPoolSize)) {
               return false;
            }

            if (this.compareAndIncrementWorkerCount(c)) {
               ThreadPoolExecutor.Worker w = new ThreadPoolExecutor.Worker(firstTask);
               Thread t = w.thread;
               ReentrantLock mainLock = this.mainLock;
               mainLock.lock();

               try {
                  int c = this.ctl.get();
                  int rs = runStateOf(c);
                  if (t == null || rs >= 0 && (rs != 0 || firstTask != null)) {
                     this.decrementWorkerCount();
                     this.tryTerminate();
                     boolean var15 = false;
                     return var15;
                  }

                  this.workers.add(w);
                  int s = this.workers.size();
                  if (s > this.largestPoolSize) {
                     this.largestPoolSize = s;
                  }
               } finally {
                  mainLock.unlock();
               }

               t.start();
               if (runStateOf(this.ctl.get()) == 536870912 && !t.isInterrupted()) {
                  t.interrupt();
               }

               return true;
            }

            c = this.ctl.get();
            if (runStateOf(c) != rs) {
               break;
            }
         }
      }
   }

   private void processWorkerExit(ThreadPoolExecutor.Worker w, boolean completedAbruptly) {
      if (completedAbruptly) {
         this.decrementWorkerCount();
      }

      ReentrantLock mainLock = this.mainLock;
      mainLock.lock();

      try {
         this.completedTaskCount += w.completedTasks;
         this.workers.remove(w);
      } finally {
         mainLock.unlock();
      }

      this.tryTerminate();
      int c = this.ctl.get();
      if (runStateLessThan(c, 536870912)) {
         if (!completedAbruptly) {
            int min = this.allowCoreThreadTimeOut ? 0 : this.corePoolSize;
            if (min == 0 && !this.workQueue.isEmpty()) {
               min = 1;
            }

            if (workerCountOf(c) >= min) {
               return;
            }
         }

         this.addWorker((Runnable)null, false);
      }

   }

   private Runnable getTask() {
      boolean timedOut = false;

      while(true) {
         int c = this.ctl.get();
         int rs = runStateOf(c);
         if (rs >= 0 && (rs >= 536870912 || this.workQueue.isEmpty())) {
            this.decrementWorkerCount();
            return null;
         }

         while(true) {
            int wc = workerCountOf(c);
            boolean timed = this.allowCoreThreadTimeOut || wc > this.corePoolSize;
            if (wc <= this.maximumPoolSize && (!timedOut || !timed)) {
               try {
                  Runnable r = timed ? (Runnable)this.workQueue.poll(this.keepAliveTime, TimeUnit.NANOSECONDS) : (Runnable)this.workQueue.take();
                  if (r != null) {
                     return r;
                  }

                  timedOut = true;
               } catch (InterruptedException var6) {
                  timedOut = false;
               }
               break;
            }

            if (this.compareAndDecrementWorkerCount(c)) {
               return null;
            }

            c = this.ctl.get();
            if (runStateOf(c) != rs) {
               break;
            }
         }
      }
   }

   final void runWorker(ThreadPoolExecutor.Worker w) {
      Runnable task = w.firstTask;
      w.firstTask = null;
      boolean completedAbruptly = true;

      try {
         while(task != null || (task = this.getTask()) != null) {
            w.lock();
            this.clearInterruptsForTaskRun();

            try {
               this.beforeExecute(w.thread, task);
               Object thrown = null;

               try {
                  task.run();
               } catch (RuntimeException var27) {
                  thrown = var27;
                  throw var27;
               } catch (Error var28) {
                  thrown = var28;
                  throw var28;
               } catch (Throwable var29) {
                  thrown = var29;
                  throw new Error(var29);
               } finally {
                  this.afterExecute(task, (Throwable)thrown);
               }
            } finally {
               task = null;
               ++w.completedTasks;
               w.unlock();
            }
         }

         completedAbruptly = false;
      } finally {
         this.processWorkerExit(w, completedAbruptly);
      }

   }

   public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue workQueue) {
      this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory(), defaultHandler);
   }

   public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue workQueue, ThreadFactory threadFactory) {
      this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, defaultHandler);
   }

   public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue workQueue, RejectedExecutionHandler handler) {
      this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory(), handler);
   }

   public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
      this.ctl = new AtomicInteger(ctlOf(-536870912, 0));
      this.mainLock = new ReentrantLock();
      this.workers = new HashSet();
      this.termination = this.mainLock.newCondition();
      if (corePoolSize >= 0 && maximumPoolSize > 0 && maximumPoolSize >= corePoolSize && keepAliveTime >= 0L) {
         if (workQueue != null && threadFactory != null && handler != null) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.workQueue = workQueue;
            this.keepAliveTime = unit.toNanos(keepAliveTime);
            this.threadFactory = threadFactory;
            this.handler = handler;
         } else {
            throw new NullPointerException();
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void execute(Runnable command) {
      if (command == null) {
         throw new NullPointerException();
      } else {
         int c = this.ctl.get();
         if (workerCountOf(c) < this.corePoolSize) {
            if (this.addWorker(command, true)) {
               return;
            }

            c = this.ctl.get();
         }

         if (isRunning(c) && this.workQueue.offer(command)) {
            int recheck = this.ctl.get();
            if (!isRunning(recheck) && this.remove(command)) {
               this.reject(command);
            } else if (workerCountOf(recheck) == 0) {
               this.addWorker((Runnable)null, false);
            }
         } else if (!this.addWorker(command, false)) {
            this.reject(command);
         }

      }
   }

   public void shutdown() {
      ReentrantLock mainLock = this.mainLock;
      mainLock.lock();

      try {
         this.checkShutdownAccess();
         this.advanceRunState(0);
         this.interruptIdleWorkers();
         this.onShutdown();
      } finally {
         mainLock.unlock();
      }

      this.tryTerminate();
   }

   public List shutdownNow() {
      ReentrantLock mainLock = this.mainLock;
      mainLock.lock();

      List tasks;
      try {
         this.checkShutdownAccess();
         this.advanceRunState(536870912);
         this.interruptWorkers();
         tasks = this.drainQueue();
      } finally {
         mainLock.unlock();
      }

      this.tryTerminate();
      return tasks;
   }

   public boolean isShutdown() {
      return !isRunning(this.ctl.get());
   }

   public boolean isTerminating() {
      int c = this.ctl.get();
      return !isRunning(c) && runStateLessThan(c, 1610612736);
   }

   public boolean isTerminated() {
      return runStateAtLeast(this.ctl.get(), 1610612736);
   }

   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
      long nanos = unit.toNanos(timeout);
      long deadline = Utils.nanoTime() + nanos;
      ReentrantLock mainLock = this.mainLock;
      mainLock.lock();

      try {
         boolean var9;
         if (runStateAtLeast(this.ctl.get(), 1610612736)) {
            var9 = true;
            return var9;
         } else {
            while(nanos > 0L) {
               this.termination.await(nanos, TimeUnit.NANOSECONDS);
               if (runStateAtLeast(this.ctl.get(), 1610612736)) {
                  var9 = true;
                  return var9;
               }

               nanos = deadline - Utils.nanoTime();
            }

            var9 = false;
            return var9;
         }
      } finally {
         mainLock.unlock();
      }
   }

   protected void finalize() {
      this.shutdown();
   }

   public void setThreadFactory(ThreadFactory threadFactory) {
      if (threadFactory == null) {
         throw new NullPointerException();
      } else {
         this.threadFactory = threadFactory;
      }
   }

   public ThreadFactory getThreadFactory() {
      return this.threadFactory;
   }

   public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {
      if (handler == null) {
         throw new NullPointerException();
      } else {
         this.handler = handler;
      }
   }

   public RejectedExecutionHandler getRejectedExecutionHandler() {
      return this.handler;
   }

   public void setCorePoolSize(int corePoolSize) {
      if (corePoolSize < 0) {
         throw new IllegalArgumentException();
      } else {
         int delta = corePoolSize - this.corePoolSize;
         this.corePoolSize = corePoolSize;
         if (workerCountOf(this.ctl.get()) > corePoolSize) {
            this.interruptIdleWorkers();
         } else if (delta > 0) {
            int k = Math.min(delta, this.workQueue.size());

            while(k-- > 0 && this.addWorker((Runnable)null, true) && !this.workQueue.isEmpty()) {
            }
         }

      }
   }

   public int getCorePoolSize() {
      return this.corePoolSize;
   }

   public boolean prestartCoreThread() {
      return workerCountOf(this.ctl.get()) < this.corePoolSize && this.addWorker((Runnable)null, true);
   }

   public int prestartAllCoreThreads() {
      int n;
      for(n = 0; this.addWorker((Runnable)null, true); ++n) {
      }

      return n;
   }

   public boolean allowsCoreThreadTimeOut() {
      return this.allowCoreThreadTimeOut;
   }

   public void allowCoreThreadTimeOut(boolean value) {
      if (value && this.keepAliveTime <= 0L) {
         throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
      } else {
         if (value != this.allowCoreThreadTimeOut) {
            this.allowCoreThreadTimeOut = value;
            if (value) {
               this.interruptIdleWorkers();
            }
         }

      }
   }

   public void setMaximumPoolSize(int maximumPoolSize) {
      if (maximumPoolSize > 0 && maximumPoolSize >= this.corePoolSize) {
         this.maximumPoolSize = maximumPoolSize;
         if (workerCountOf(this.ctl.get()) > maximumPoolSize) {
            this.interruptIdleWorkers();
         }

      } else {
         throw new IllegalArgumentException();
      }
   }

   public int getMaximumPoolSize() {
      return this.maximumPoolSize;
   }

   public void setKeepAliveTime(long time, TimeUnit unit) {
      if (time < 0L) {
         throw new IllegalArgumentException();
      } else if (time == 0L && this.allowsCoreThreadTimeOut()) {
         throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
      } else {
         long keepAliveTime = unit.toNanos(time);
         long delta = keepAliveTime - this.keepAliveTime;
         this.keepAliveTime = keepAliveTime;
         if (delta < 0L) {
            this.interruptIdleWorkers();
         }

      }
   }

   public long getKeepAliveTime(TimeUnit unit) {
      return unit.convert(this.keepAliveTime, TimeUnit.NANOSECONDS);
   }

   public BlockingQueue getQueue() {
      return this.workQueue;
   }

   public boolean remove(Runnable task) {
      boolean removed = this.workQueue.remove(task);
      this.tryTerminate();
      return removed;
   }

   public void purge() {
      BlockingQueue q = this.workQueue;

      try {
         Iterator it = q.iterator();

         while(it.hasNext()) {
            Runnable r = (Runnable)it.next();
            if (r instanceof Future && ((Future)r).isCancelled()) {
               it.remove();
            }
         }
      } catch (ConcurrentModificationException var6) {
         Object[] arr = q.toArray();

         for(int i = 0; i < arr.length; ++i) {
            Object r = arr[i];
            if (r instanceof Future && ((Future)r).isCancelled()) {
               q.remove(r);
            }
         }
      }

      this.tryTerminate();
   }

   public int getPoolSize() {
      ReentrantLock mainLock = this.mainLock;
      mainLock.lock();

      int var2;
      try {
         var2 = runStateAtLeast(this.ctl.get(), 1073741824) ? 0 : this.workers.size();
      } finally {
         mainLock.unlock();
      }

      return var2;
   }

   public int getActiveCount() {
      ReentrantLock mainLock = this.mainLock;
      mainLock.lock();

      try {
         int n = 0;
         Iterator itr = this.workers.iterator();

         while(itr.hasNext()) {
            ThreadPoolExecutor.Worker w = (ThreadPoolExecutor.Worker)itr.next();
            if (w.isLocked()) {
               ++n;
            }
         }

         int var8 = n;
         return var8;
      } finally {
         mainLock.unlock();
      }
   }

   public int getLargestPoolSize() {
      ReentrantLock mainLock = this.mainLock;
      mainLock.lock();

      int var2;
      try {
         var2 = this.largestPoolSize;
      } finally {
         mainLock.unlock();
      }

      return var2;
   }

   public long getTaskCount() {
      ReentrantLock mainLock = this.mainLock;
      mainLock.lock();

      try {
         long n = this.completedTaskCount;
         Iterator itr = this.workers.iterator();

         while(itr.hasNext()) {
            ThreadPoolExecutor.Worker w = (ThreadPoolExecutor.Worker)itr.next();
            n += w.completedTasks;
            if (w.isLocked()) {
               ++n;
            }
         }

         long var6 = n + (long)this.workQueue.size();
         return var6;
      } finally {
         mainLock.unlock();
      }
   }

   public long getCompletedTaskCount() {
      ReentrantLock mainLock = this.mainLock;
      mainLock.lock();

      try {
         long n = this.completedTaskCount;

         ThreadPoolExecutor.Worker w;
         for(Iterator itr = this.workers.iterator(); itr.hasNext(); n += w.completedTasks) {
            w = (ThreadPoolExecutor.Worker)itr.next();
         }

         long var6 = n;
         return var6;
      } finally {
         mainLock.unlock();
      }
   }

   protected void beforeExecute(Thread t, Runnable r) {
   }

   protected void afterExecute(Runnable r, Throwable t) {
   }

   protected void terminated() {
   }

   public static class DiscardOldestPolicy implements RejectedExecutionHandler {
      public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
         if (!e.isShutdown()) {
            e.getQueue().poll();
            e.execute(r);
         }

      }
   }

   public static class DiscardPolicy implements RejectedExecutionHandler {
      public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
      }
   }

   public static class AbortPolicy implements RejectedExecutionHandler {
      public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
         throw new RejectedExecutionException();
      }
   }

   public static class CallerRunsPolicy implements RejectedExecutionHandler {
      public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
         if (!e.isShutdown()) {
            r.run();
         }

      }
   }

   private final class Worker extends ReentrantLock implements Runnable {
      private static final long serialVersionUID = 6138294804551838833L;
      final Thread thread;
      Runnable firstTask;
      volatile long completedTasks;

      Worker(Runnable firstTask) {
         this.firstTask = firstTask;
         this.thread = ThreadPoolExecutor.this.getThreadFactory().newThread(this);
      }

      public void run() {
         ThreadPoolExecutor.this.runWorker(this);
      }
   }
}

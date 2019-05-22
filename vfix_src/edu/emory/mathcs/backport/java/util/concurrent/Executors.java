package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.List;

public class Executors {
   public static ExecutorService newFixedThreadPool(int nThreads) {
      return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
   }

   public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
      return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), threadFactory);
   }

   public static ExecutorService newSingleThreadExecutor() {
      return new Executors.FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue()));
   }

   public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
      return new Executors.FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), threadFactory));
   }

   public static ExecutorService newCachedThreadPool() {
      return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue());
   }

   public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
      return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), threadFactory);
   }

   public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
      return new Executors.DelegatedScheduledExecutorService(new ScheduledThreadPoolExecutor(1));
   }

   public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
      return new Executors.DelegatedScheduledExecutorService(new ScheduledThreadPoolExecutor(1, threadFactory));
   }

   public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
      return new ScheduledThreadPoolExecutor(corePoolSize);
   }

   public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory) {
      return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
   }

   public static ExecutorService unconfigurableExecutorService(ExecutorService executor) {
      if (executor == null) {
         throw new NullPointerException();
      } else {
         return new Executors.DelegatedExecutorService(executor);
      }
   }

   public static ScheduledExecutorService unconfigurableScheduledExecutorService(ScheduledExecutorService executor) {
      if (executor == null) {
         throw new NullPointerException();
      } else {
         return new Executors.DelegatedScheduledExecutorService(executor);
      }
   }

   public static ThreadFactory defaultThreadFactory() {
      return new Executors.DefaultThreadFactory();
   }

   public static ThreadFactory privilegedThreadFactory() {
      return new Executors.PrivilegedThreadFactory();
   }

   public static Callable callable(Runnable task, Object result) {
      if (task == null) {
         throw new NullPointerException();
      } else {
         return new Executors.RunnableAdapter(task, result);
      }
   }

   public static Callable callable(Runnable task) {
      if (task == null) {
         throw new NullPointerException();
      } else {
         return new Executors.RunnableAdapter(task, (Object)null);
      }
   }

   public static Callable callable(final PrivilegedAction action) {
      if (action == null) {
         throw new NullPointerException();
      } else {
         return new Callable() {
            public Object call() {
               return action.run();
            }
         };
      }
   }

   public static Callable callable(final PrivilegedExceptionAction action) {
      if (action == null) {
         throw new NullPointerException();
      } else {
         return new Callable() {
            public Object call() throws Exception {
               return action.run();
            }
         };
      }
   }

   public static Callable privilegedCallable(Callable callable) {
      if (callable == null) {
         throw new NullPointerException();
      } else {
         return new Executors.PrivilegedCallable(callable);
      }
   }

   public static Callable privilegedCallableUsingCurrentClassLoader(Callable callable) {
      if (callable == null) {
         throw new NullPointerException();
      } else {
         return new Executors.PrivilegedCallableUsingCurrentClassLoader(callable);
      }
   }

   private Executors() {
   }

   static class DelegatedScheduledExecutorService extends Executors.DelegatedExecutorService implements ScheduledExecutorService {
      private final ScheduledExecutorService e;

      DelegatedScheduledExecutorService(ScheduledExecutorService executor) {
         super(executor);
         this.e = executor;
      }

      public ScheduledFuture schedule(Runnable command, long delay, TimeUnit unit) {
         return this.e.schedule(command, delay, unit);
      }

      public ScheduledFuture schedule(Callable callable, long delay, TimeUnit unit) {
         return this.e.schedule(callable, delay, unit);
      }

      public ScheduledFuture scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
         return this.e.scheduleAtFixedRate(command, initialDelay, period, unit);
      }

      public ScheduledFuture scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
         return this.e.scheduleWithFixedDelay(command, initialDelay, delay, unit);
      }
   }

   static class FinalizableDelegatedExecutorService extends Executors.DelegatedExecutorService {
      FinalizableDelegatedExecutorService(ExecutorService executor) {
         super(executor);
      }

      protected void finalize() {
         super.shutdown();
      }
   }

   static class DelegatedExecutorService extends AbstractExecutorService {
      private final ExecutorService e;

      DelegatedExecutorService(ExecutorService executor) {
         this.e = executor;
      }

      public void execute(Runnable command) {
         this.e.execute(command);
      }

      public void shutdown() {
         this.e.shutdown();
      }

      public List shutdownNow() {
         return this.e.shutdownNow();
      }

      public boolean isShutdown() {
         return this.e.isShutdown();
      }

      public boolean isTerminated() {
         return this.e.isTerminated();
      }

      public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
         return this.e.awaitTermination(timeout, unit);
      }

      public Future submit(Runnable task) {
         return this.e.submit(task);
      }

      public Future submit(Callable task) {
         return this.e.submit(task);
      }

      public Future submit(Runnable task, Object result) {
         return this.e.submit(task, result);
      }

      public List invokeAll(Collection tasks) throws InterruptedException {
         return this.e.invokeAll(tasks);
      }

      public List invokeAll(Collection tasks, long timeout, TimeUnit unit) throws InterruptedException {
         return this.e.invokeAll(tasks, timeout, unit);
      }

      public Object invokeAny(Collection tasks) throws InterruptedException, ExecutionException {
         return this.e.invokeAny(tasks);
      }

      public Object invokeAny(Collection tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
         return this.e.invokeAny(tasks, timeout, unit);
      }
   }

   static class PrivilegedThreadFactory extends Executors.DefaultThreadFactory {
      private final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
      private final AccessControlContext acc = AccessController.getContext();

      PrivilegedThreadFactory() {
         this.acc.checkPermission(new RuntimePermission("setContextClassLoader"));
      }

      public Thread newThread(final Runnable r) {
         return super.newThread(new Runnable() {
            public void run() {
               AccessController.doPrivileged(new PrivilegedAction() {
                  public Object run() {
                     Thread.currentThread().setContextClassLoader(PrivilegedThreadFactory.this.ccl);
                     r.run();
                     return null;
                  }
               }, PrivilegedThreadFactory.this.acc);
            }
         });
      }
   }

   static class DefaultThreadFactory implements ThreadFactory {
      static final AtomicInteger poolNumber = new AtomicInteger(1);
      final ThreadGroup group;
      final AtomicInteger threadNumber = new AtomicInteger(1);
      final String namePrefix;

      DefaultThreadFactory() {
         SecurityManager s = System.getSecurityManager();
         this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
         this.namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
      }

      public Thread newThread(Runnable r) {
         Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
         if (t.isDaemon()) {
            t.setDaemon(false);
         }

         if (t.getPriority() != 5) {
            t.setPriority(5);
         }

         return t;
      }
   }

   static final class PrivilegedCallableUsingCurrentClassLoader implements Callable {
      private final ClassLoader ccl;
      private final AccessControlContext acc;
      private final Callable task;
      private Object result;
      private Exception exception;

      PrivilegedCallableUsingCurrentClassLoader(Callable task) {
         this.task = task;
         this.ccl = Thread.currentThread().getContextClassLoader();
         this.acc = AccessController.getContext();
         this.acc.checkPermission(new RuntimePermission("getContextClassLoader"));
         this.acc.checkPermission(new RuntimePermission("setContextClassLoader"));
      }

      public Object call() throws Exception {
         AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               ClassLoader savedcl = null;
               Thread t = Thread.currentThread();

               try {
                  ClassLoader cl = t.getContextClassLoader();
                  if (PrivilegedCallableUsingCurrentClassLoader.this.ccl != cl) {
                     t.setContextClassLoader(PrivilegedCallableUsingCurrentClassLoader.this.ccl);
                     savedcl = cl;
                  }

                  PrivilegedCallableUsingCurrentClassLoader.this.result = PrivilegedCallableUsingCurrentClassLoader.this.task.call();
               } catch (Exception var7) {
                  PrivilegedCallableUsingCurrentClassLoader.this.exception = var7;
               } finally {
                  if (savedcl != null) {
                     t.setContextClassLoader(savedcl);
                  }

               }

               return null;
            }
         }, this.acc);
         if (this.exception != null) {
            throw this.exception;
         } else {
            return this.result;
         }
      }
   }

   static final class PrivilegedCallable implements Callable {
      private final AccessControlContext acc;
      private final Callable task;
      private Object result;
      private Exception exception;

      PrivilegedCallable(Callable task) {
         this.task = task;
         this.acc = AccessController.getContext();
      }

      public Object call() throws Exception {
         AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               try {
                  PrivilegedCallable.this.result = PrivilegedCallable.this.task.call();
               } catch (Exception var2) {
                  PrivilegedCallable.this.exception = var2;
               }

               return null;
            }
         }, this.acc);
         if (this.exception != null) {
            throw this.exception;
         } else {
            return this.result;
         }
      }
   }

   static final class RunnableAdapter implements Callable {
      final Runnable task;
      final Object result;

      RunnableAdapter(Runnable task, Object result) {
         this.task = task;
         this.result = result;
      }

      public Object call() {
         this.task.run();
         return this.result;
      }
   }
}

package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class MoreExecutors {
   private MoreExecutors() {
   }

   @Beta
   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
      return (new MoreExecutors.Application()).getExitingExecutorService(executor, terminationTimeout, timeUnit);
   }

   @Beta
   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
      return (new MoreExecutors.Application()).getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
   }

   @Beta
   public static void addDelayedShutdownHook(ExecutorService service, long terminationTimeout, TimeUnit timeUnit) {
      (new MoreExecutors.Application()).addDelayedShutdownHook(service, terminationTimeout, timeUnit);
   }

   @Beta
   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
      return (new MoreExecutors.Application()).getExitingExecutorService(executor);
   }

   @Beta
   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
      return (new MoreExecutors.Application()).getExitingScheduledExecutorService(executor);
   }

   private static void useDaemonThreadFactory(ThreadPoolExecutor executor) {
      executor.setThreadFactory((new ThreadFactoryBuilder()).setDaemon(true).setThreadFactory(executor.getThreadFactory()).build());
   }

   public static ListeningExecutorService sameThreadExecutor() {
      return new MoreExecutors.SameThreadExecutorService();
   }

   public static ListeningExecutorService listeningDecorator(ExecutorService delegate) {
      return (ListeningExecutorService)(delegate instanceof ListeningExecutorService ? (ListeningExecutorService)delegate : (delegate instanceof ScheduledExecutorService ? new MoreExecutors.ScheduledListeningDecorator((ScheduledExecutorService)delegate) : new MoreExecutors.ListeningDecorator(delegate)));
   }

   public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService delegate) {
      return (ListeningScheduledExecutorService)(delegate instanceof ListeningScheduledExecutorService ? (ListeningScheduledExecutorService)delegate : new MoreExecutors.ScheduledListeningDecorator(delegate));
   }

   static <T> T invokeAnyImpl(ListeningExecutorService executorService, Collection<? extends Callable<T>> tasks, boolean timed, long nanos) throws InterruptedException, ExecutionException, TimeoutException {
      Preconditions.checkNotNull(executorService);
      int ntasks = tasks.size();
      Preconditions.checkArgument(ntasks > 0);
      List<Future<T>> futures = Lists.newArrayListWithCapacity(ntasks);
      LinkedBlockingQueue futureQueue = Queues.newLinkedBlockingQueue();
      boolean var25 = false;

      Object var16;
      try {
         var25 = true;
         ExecutionException ee = null;
         long lastTime = timed ? System.nanoTime() : 0L;
         Iterator<? extends Callable<T>> it = tasks.iterator();
         futures.add(submitAndAddQueueListener(executorService, (Callable)it.next(), futureQueue));
         --ntasks;
         int active = 1;

         while(true) {
            Future<T> f = (Future)futureQueue.poll();
            if (f == null) {
               if (ntasks > 0) {
                  --ntasks;
                  futures.add(submitAndAddQueueListener(executorService, (Callable)it.next(), futureQueue));
                  ++active;
               } else {
                  if (active == 0) {
                     if (ee == null) {
                        ee = new ExecutionException((Throwable)null);
                     }

                     throw ee;
                  }

                  if (timed) {
                     f = (Future)futureQueue.poll(nanos, TimeUnit.NANOSECONDS);
                     if (f == null) {
                        throw new TimeoutException();
                     }

                     long now = System.nanoTime();
                     nanos -= now - lastTime;
                     lastTime = now;
                  } else {
                     f = (Future)futureQueue.take();
                  }
               }
            }

            if (f != null) {
               --active;

               try {
                  var16 = f.get();
                  var25 = false;
                  break;
               } catch (ExecutionException var26) {
                  ee = var26;
               } catch (RuntimeException var27) {
                  ee = new ExecutionException(var27);
               }
            }
         }
      } finally {
         if (var25) {
            Iterator i$ = futures.iterator();

            while(i$.hasNext()) {
               Future<T> f = (Future)i$.next();
               f.cancel(true);
            }

         }
      }

      Iterator i$ = futures.iterator();

      while(i$.hasNext()) {
         Future<T> f = (Future)i$.next();
         f.cancel(true);
      }

      return var16;
   }

   private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService executorService, Callable<T> task, final BlockingQueue<Future<T>> queue) {
      final ListenableFuture<T> future = executorService.submit(task);
      future.addListener(new Runnable() {
         public void run() {
            queue.add(future);
         }
      }, sameThreadExecutor());
      return future;
   }

   @Beta
   public static ThreadFactory platformThreadFactory() {
      if (!isAppEngine()) {
         return Executors.defaultThreadFactory();
      } else {
         try {
            return (ThreadFactory)Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory").invoke((Object)null);
         } catch (IllegalAccessException var1) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", var1);
         } catch (ClassNotFoundException var2) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", var2);
         } catch (NoSuchMethodException var3) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", var3);
         } catch (InvocationTargetException var4) {
            throw Throwables.propagate(var4.getCause());
         }
      }
   }

   private static boolean isAppEngine() {
      if (System.getProperty("com.google.appengine.runtime.environment") == null) {
         return false;
      } else {
         try {
            return Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment").invoke((Object)null) != null;
         } catch (ClassNotFoundException var1) {
            return false;
         } catch (InvocationTargetException var2) {
            return false;
         } catch (IllegalAccessException var3) {
            return false;
         } catch (NoSuchMethodException var4) {
            return false;
         }
      }
   }

   static Thread newThread(String name, Runnable runnable) {
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(runnable);
      Thread result = platformThreadFactory().newThread(runnable);

      try {
         result.setName(name);
      } catch (SecurityException var4) {
      }

      return result;
   }

   static Executor renamingDecorator(final Executor executor, final Supplier<String> nameSupplier) {
      Preconditions.checkNotNull(executor);
      Preconditions.checkNotNull(nameSupplier);
      return isAppEngine() ? executor : new Executor() {
         public void execute(Runnable command) {
            executor.execute(Callables.threadRenaming(command, nameSupplier));
         }
      };
   }

   static ExecutorService renamingDecorator(ExecutorService service, final Supplier<String> nameSupplier) {
      Preconditions.checkNotNull(service);
      Preconditions.checkNotNull(nameSupplier);
      return (ExecutorService)(isAppEngine() ? service : new WrappingExecutorService(service) {
         protected <T> Callable<T> wrapTask(Callable<T> callable) {
            return Callables.threadRenaming(callable, nameSupplier);
         }

         protected Runnable wrapTask(Runnable command) {
            return Callables.threadRenaming(command, nameSupplier);
         }
      });
   }

   static ScheduledExecutorService renamingDecorator(ScheduledExecutorService service, final Supplier<String> nameSupplier) {
      Preconditions.checkNotNull(service);
      Preconditions.checkNotNull(nameSupplier);
      return (ScheduledExecutorService)(isAppEngine() ? service : new WrappingScheduledExecutorService(service) {
         protected <T> Callable<T> wrapTask(Callable<T> callable) {
            return Callables.threadRenaming(callable, nameSupplier);
         }

         protected Runnable wrapTask(Runnable command) {
            return Callables.threadRenaming(command, nameSupplier);
         }
      });
   }

   private static class ScheduledListeningDecorator extends MoreExecutors.ListeningDecorator implements ListeningScheduledExecutorService {
      final ScheduledExecutorService delegate;

      ScheduledListeningDecorator(ScheduledExecutorService delegate) {
         super(delegate);
         this.delegate = (ScheduledExecutorService)Preconditions.checkNotNull(delegate);
      }

      public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
         ListenableFutureTask<Void> task = ListenableFutureTask.create(command, (Object)null);
         ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
         return new MoreExecutors.ScheduledListeningDecorator.ListenableScheduledTask(task, scheduled);
      }

      public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
         ListenableFutureTask<V> task = ListenableFutureTask.create(callable);
         ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
         return new MoreExecutors.ScheduledListeningDecorator.ListenableScheduledTask(task, scheduled);
      }

      public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
         MoreExecutors.ScheduledListeningDecorator.NeverSuccessfulListenableFutureTask task = new MoreExecutors.ScheduledListeningDecorator.NeverSuccessfulListenableFutureTask(command);
         ScheduledFuture<?> scheduled = this.delegate.scheduleAtFixedRate(task, initialDelay, period, unit);
         return new MoreExecutors.ScheduledListeningDecorator.ListenableScheduledTask(task, scheduled);
      }

      public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
         MoreExecutors.ScheduledListeningDecorator.NeverSuccessfulListenableFutureTask task = new MoreExecutors.ScheduledListeningDecorator.NeverSuccessfulListenableFutureTask(command);
         ScheduledFuture<?> scheduled = this.delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit);
         return new MoreExecutors.ScheduledListeningDecorator.ListenableScheduledTask(task, scheduled);
      }

      private static final class NeverSuccessfulListenableFutureTask extends AbstractFuture<Void> implements Runnable {
         private final Runnable delegate;

         public NeverSuccessfulListenableFutureTask(Runnable delegate) {
            this.delegate = (Runnable)Preconditions.checkNotNull(delegate);
         }

         public void run() {
            try {
               this.delegate.run();
            } catch (Throwable var2) {
               this.setException(var2);
               throw Throwables.propagate(var2);
            }
         }
      }

      private static final class ListenableScheduledTask<V> extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V> implements ListenableScheduledFuture<V> {
         private final ScheduledFuture<?> scheduledDelegate;

         public ListenableScheduledTask(ListenableFuture<V> listenableDelegate, ScheduledFuture<?> scheduledDelegate) {
            super(listenableDelegate);
            this.scheduledDelegate = scheduledDelegate;
         }

         public boolean cancel(boolean mayInterruptIfRunning) {
            boolean cancelled = super.cancel(mayInterruptIfRunning);
            if (cancelled) {
               this.scheduledDelegate.cancel(mayInterruptIfRunning);
            }

            return cancelled;
         }

         public long getDelay(TimeUnit unit) {
            return this.scheduledDelegate.getDelay(unit);
         }

         public int compareTo(Delayed other) {
            return this.scheduledDelegate.compareTo(other);
         }
      }
   }

   private static class ListeningDecorator extends AbstractListeningExecutorService {
      private final ExecutorService delegate;

      ListeningDecorator(ExecutorService delegate) {
         this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate);
      }

      public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
         return this.delegate.awaitTermination(timeout, unit);
      }

      public boolean isShutdown() {
         return this.delegate.isShutdown();
      }

      public boolean isTerminated() {
         return this.delegate.isTerminated();
      }

      public void shutdown() {
         this.delegate.shutdown();
      }

      public List<Runnable> shutdownNow() {
         return this.delegate.shutdownNow();
      }

      public void execute(Runnable command) {
         this.delegate.execute(command);
      }
   }

   private static class SameThreadExecutorService extends AbstractListeningExecutorService {
      private final Lock lock;
      private final Condition termination;
      private int runningTasks;
      private boolean shutdown;

      private SameThreadExecutorService() {
         this.lock = new ReentrantLock();
         this.termination = this.lock.newCondition();
         this.runningTasks = 0;
         this.shutdown = false;
      }

      public void execute(Runnable command) {
         this.startTask();

         try {
            command.run();
         } finally {
            this.endTask();
         }

      }

      public boolean isShutdown() {
         this.lock.lock();

         boolean var1;
         try {
            var1 = this.shutdown;
         } finally {
            this.lock.unlock();
         }

         return var1;
      }

      public void shutdown() {
         this.lock.lock();

         try {
            this.shutdown = true;
         } finally {
            this.lock.unlock();
         }

      }

      public List<Runnable> shutdownNow() {
         this.shutdown();
         return Collections.emptyList();
      }

      public boolean isTerminated() {
         this.lock.lock();

         boolean var1;
         try {
            var1 = this.shutdown && this.runningTasks == 0;
         } finally {
            this.lock.unlock();
         }

         return var1;
      }

      public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
         long nanos = unit.toNanos(timeout);
         this.lock.lock();

         boolean var6;
         try {
            while(!this.isTerminated()) {
               if (nanos <= 0L) {
                  var6 = false;
                  return var6;
               }

               nanos = this.termination.awaitNanos(nanos);
            }

            var6 = true;
         } finally {
            this.lock.unlock();
         }

         return var6;
      }

      private void startTask() {
         this.lock.lock();

         try {
            if (this.isShutdown()) {
               throw new RejectedExecutionException("Executor already shutdown");
            }

            ++this.runningTasks;
         } finally {
            this.lock.unlock();
         }

      }

      private void endTask() {
         this.lock.lock();

         try {
            --this.runningTasks;
            if (this.isTerminated()) {
               this.termination.signalAll();
            }
         } finally {
            this.lock.unlock();
         }

      }

      // $FF: synthetic method
      SameThreadExecutorService(Object x0) {
         this();
      }
   }

   @VisibleForTesting
   static class Application {
      final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
         MoreExecutors.useDaemonThreadFactory(executor);
         ExecutorService service = Executors.unconfigurableExecutorService(executor);
         this.addDelayedShutdownHook(service, terminationTimeout, timeUnit);
         return service;
      }

      final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
         MoreExecutors.useDaemonThreadFactory(executor);
         ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
         this.addDelayedShutdownHook(service, terminationTimeout, timeUnit);
         return service;
      }

      final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
         Preconditions.checkNotNull(service);
         Preconditions.checkNotNull(timeUnit);
         this.addShutdownHook(MoreExecutors.newThread("DelayedShutdownHook-for-" + service, new Runnable() {
            public void run() {
               try {
                  service.shutdown();
                  service.awaitTermination(terminationTimeout, timeUnit);
               } catch (InterruptedException var2) {
               }

            }
         }));
      }

      final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
         return this.getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
      }

      final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
         return this.getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
      }

      @VisibleForTesting
      void addShutdownHook(Thread hook) {
         Runtime.getRuntime().addShutdownHook(hook);
      }
   }
}

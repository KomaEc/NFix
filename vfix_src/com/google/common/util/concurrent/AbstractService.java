package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;

@Beta
public abstract class AbstractService implements Service {
   private final Monitor monitor = new Monitor();
   private final AbstractService.Transition startup = new AbstractService.Transition();
   private final AbstractService.Transition shutdown = new AbstractService.Transition();
   private final Monitor.Guard isStartable;
   private final Monitor.Guard isStoppable;
   private final Monitor.Guard hasReachedRunning;
   private final Monitor.Guard isStopped;
   @GuardedBy("monitor")
   private final List<AbstractService.ListenerExecutorPair> listeners;
   private final ExecutionQueue queuedListeners;
   @GuardedBy("monitor")
   private volatile AbstractService.StateSnapshot snapshot;

   protected AbstractService() {
      this.isStartable = new Monitor.Guard(this.monitor) {
         public boolean isSatisfied() {
            return AbstractService.this.state() == Service.State.NEW;
         }
      };
      this.isStoppable = new Monitor.Guard(this.monitor) {
         public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(Service.State.RUNNING) <= 0;
         }
      };
      this.hasReachedRunning = new Monitor.Guard(this.monitor) {
         public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(Service.State.RUNNING) >= 0;
         }
      };
      this.isStopped = new Monitor.Guard(this.monitor) {
         public boolean isSatisfied() {
            return AbstractService.this.state().isTerminal();
         }
      };
      this.listeners = Lists.newArrayList();
      this.queuedListeners = new ExecutionQueue();
      this.snapshot = new AbstractService.StateSnapshot(Service.State.NEW);
      this.addListener(new Service.Listener() {
         public void running() {
            AbstractService.this.startup.set(Service.State.RUNNING);
         }

         public void stopping(Service.State from) {
            if (from == Service.State.STARTING) {
               AbstractService.this.startup.set(Service.State.STOPPING);
            }

         }

         public void terminated(Service.State from) {
            if (from == Service.State.NEW) {
               AbstractService.this.startup.set(Service.State.TERMINATED);
            }

            AbstractService.this.shutdown.set(Service.State.TERMINATED);
         }

         public void failed(Service.State from, Throwable failure) {
            switch(from) {
            case STARTING:
               AbstractService.this.startup.setException(failure);
               AbstractService.this.shutdown.setException(new Exception("Service failed to start.", failure));
               break;
            case RUNNING:
               AbstractService.this.shutdown.setException(new Exception("Service failed while running", failure));
               break;
            case STOPPING:
               AbstractService.this.shutdown.setException(failure);
               break;
            case TERMINATED:
            case FAILED:
            case NEW:
            default:
               throw new AssertionError("Unexpected from state: " + from);
            }

         }
      }, MoreExecutors.sameThreadExecutor());
   }

   protected abstract void doStart();

   protected abstract void doStop();

   public final Service startAsync() {
      if (this.monitor.enterIf(this.isStartable)) {
         try {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.STARTING);
            this.starting();
            this.doStart();
         } catch (Throwable var5) {
            this.notifyFailed(var5);
         } finally {
            this.monitor.leave();
            this.executeListeners();
         }

         return this;
      } else {
         throw new IllegalStateException("Service " + this + " has already been started");
      }
   }

   /** @deprecated */
   @Deprecated
   public final ListenableFuture<Service.State> start() {
      if (this.monitor.enterIf(this.isStartable)) {
         try {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.STARTING);
            this.starting();
            this.doStart();
         } catch (Throwable var5) {
            this.notifyFailed(var5);
         } finally {
            this.monitor.leave();
            this.executeListeners();
         }
      }

      return this.startup;
   }

   public final Service stopAsync() {
      this.stop();
      return this;
   }

   /** @deprecated */
   @Deprecated
   public final ListenableFuture<Service.State> stop() {
      if (this.monitor.enterIf(this.isStoppable)) {
         try {
            Service.State previous = this.state();
            switch(previous) {
            case STARTING:
               this.snapshot = new AbstractService.StateSnapshot(Service.State.STARTING, true, (Throwable)null);
               this.stopping(Service.State.STARTING);
               break;
            case RUNNING:
               this.snapshot = new AbstractService.StateSnapshot(Service.State.STOPPING);
               this.stopping(Service.State.RUNNING);
               this.doStop();
               break;
            case STOPPING:
            case TERMINATED:
            case FAILED:
               throw new AssertionError("isStoppable is incorrectly implemented, saw: " + previous);
            case NEW:
               this.snapshot = new AbstractService.StateSnapshot(Service.State.TERMINATED);
               this.terminated(Service.State.NEW);
               break;
            default:
               throw new AssertionError("Unexpected state: " + previous);
            }
         } catch (Throwable var5) {
            this.notifyFailed(var5);
         } finally {
            this.monitor.leave();
            this.executeListeners();
         }
      }

      return this.shutdown;
   }

   /** @deprecated */
   @Deprecated
   public Service.State startAndWait() {
      return (Service.State)Futures.getUnchecked(this.start());
   }

   /** @deprecated */
   @Deprecated
   public Service.State stopAndWait() {
      return (Service.State)Futures.getUnchecked(this.stop());
   }

   public final void awaitRunning() {
      this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);

      try {
         this.checkCurrentState(Service.State.RUNNING);
      } finally {
         this.monitor.leave();
      }

   }

   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
      if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
         try {
            this.checkCurrentState(Service.State.RUNNING);
         } finally {
            this.monitor.leave();
         }

      } else {
         throw new TimeoutException("Timed out waiting for " + this + " to reach the RUNNING state. " + "Current state: " + this.state());
      }
   }

   public final void awaitTerminated() {
      this.monitor.enterWhenUninterruptibly(this.isStopped);

      try {
         this.checkCurrentState(Service.State.TERMINATED);
      } finally {
         this.monitor.leave();
      }

   }

   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
      if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
         try {
            Service.State state = this.state();
            this.checkCurrentState(Service.State.TERMINATED);
         } finally {
            this.monitor.leave();
         }

      } else {
         throw new TimeoutException("Timed out waiting for " + this + " to reach a terminal state. " + "Current state: " + this.state());
      }
   }

   @GuardedBy("monitor")
   private void checkCurrentState(Service.State expected) {
      Service.State actual = this.state();
      if (actual != expected) {
         if (actual == Service.State.FAILED) {
            throw new IllegalStateException("Expected the service to be " + expected + ", but the service has FAILED", this.failureCause());
         } else {
            throw new IllegalStateException("Expected the service to be " + expected + ", but was " + actual);
         }
      }
   }

   protected final void notifyStarted() {
      this.monitor.enter();

      try {
         if (this.snapshot.state != Service.State.STARTING) {
            IllegalStateException failure = new IllegalStateException("Cannot notifyStarted() when the service is " + this.snapshot.state);
            this.notifyFailed(failure);
            throw failure;
         }

         if (this.snapshot.shutdownWhenStartupFinishes) {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.STOPPING);
            this.doStop();
         } else {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.RUNNING);
            this.running();
         }
      } finally {
         this.monitor.leave();
         this.executeListeners();
      }

   }

   protected final void notifyStopped() {
      this.monitor.enter();

      try {
         Service.State previous = this.snapshot.state;
         if (previous != Service.State.STOPPING && previous != Service.State.RUNNING) {
            IllegalStateException failure = new IllegalStateException("Cannot notifyStopped() when the service is " + previous);
            this.notifyFailed(failure);
            throw failure;
         }

         this.snapshot = new AbstractService.StateSnapshot(Service.State.TERMINATED);
         this.terminated(previous);
      } finally {
         this.monitor.leave();
         this.executeListeners();
      }

   }

   protected final void notifyFailed(Throwable cause) {
      Preconditions.checkNotNull(cause);
      this.monitor.enter();

      try {
         Service.State previous = this.state();
         switch(previous) {
         case STARTING:
         case RUNNING:
         case STOPPING:
            this.snapshot = new AbstractService.StateSnapshot(Service.State.FAILED, false, cause);
            this.failed(previous, cause);
            break;
         case TERMINATED:
         case NEW:
            throw new IllegalStateException("Failed while in state:" + previous, cause);
         case FAILED:
            break;
         default:
            throw new AssertionError("Unexpected state: " + previous);
         }
      } finally {
         this.monitor.leave();
         this.executeListeners();
      }

   }

   public final boolean isRunning() {
      return this.state() == Service.State.RUNNING;
   }

   public final Service.State state() {
      return this.snapshot.externalState();
   }

   public final Throwable failureCause() {
      return this.snapshot.failureCause();
   }

   public final void addListener(Service.Listener listener, Executor executor) {
      Preconditions.checkNotNull(listener, "listener");
      Preconditions.checkNotNull(executor, "executor");
      this.monitor.enter();

      try {
         Service.State currentState = this.state();
         if (currentState != Service.State.TERMINATED && currentState != Service.State.FAILED) {
            this.listeners.add(new AbstractService.ListenerExecutorPair(listener, executor));
         }
      } finally {
         this.monitor.leave();
      }

   }

   public String toString() {
      return this.getClass().getSimpleName() + " [" + this.state() + "]";
   }

   private void executeListeners() {
      if (!this.monitor.isOccupiedByCurrentThread()) {
         this.queuedListeners.execute();
      }

   }

   @GuardedBy("monitor")
   private void starting() {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         final AbstractService.ListenerExecutorPair pair = (AbstractService.ListenerExecutorPair)i$.next();
         this.queuedListeners.add(new Runnable() {
            public void run() {
               pair.listener.starting();
            }
         }, pair.executor);
      }

   }

   @GuardedBy("monitor")
   private void running() {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         final AbstractService.ListenerExecutorPair pair = (AbstractService.ListenerExecutorPair)i$.next();
         this.queuedListeners.add(new Runnable() {
            public void run() {
               pair.listener.running();
            }
         }, pair.executor);
      }

   }

   @GuardedBy("monitor")
   private void stopping(final Service.State from) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         final AbstractService.ListenerExecutorPair pair = (AbstractService.ListenerExecutorPair)i$.next();
         this.queuedListeners.add(new Runnable() {
            public void run() {
               pair.listener.stopping(from);
            }
         }, pair.executor);
      }

   }

   @GuardedBy("monitor")
   private void terminated(final Service.State from) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         final AbstractService.ListenerExecutorPair pair = (AbstractService.ListenerExecutorPair)i$.next();
         this.queuedListeners.add(new Runnable() {
            public void run() {
               pair.listener.terminated(from);
            }
         }, pair.executor);
      }

      this.listeners.clear();
   }

   @GuardedBy("monitor")
   private void failed(final Service.State from, final Throwable cause) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         final AbstractService.ListenerExecutorPair pair = (AbstractService.ListenerExecutorPair)i$.next();
         this.queuedListeners.add(new Runnable() {
            public void run() {
               pair.listener.failed(from, cause);
            }
         }, pair.executor);
      }

      this.listeners.clear();
   }

   @Immutable
   private static final class StateSnapshot {
      final Service.State state;
      final boolean shutdownWhenStartupFinishes;
      @Nullable
      final Throwable failure;

      StateSnapshot(Service.State internalState) {
         this(internalState, false, (Throwable)null);
      }

      StateSnapshot(Service.State internalState, boolean shutdownWhenStartupFinishes, @Nullable Throwable failure) {
         Preconditions.checkArgument(!shutdownWhenStartupFinishes || internalState == Service.State.STARTING, "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", internalState);
         Preconditions.checkArgument(!(failure != null ^ internalState == Service.State.FAILED), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
         this.state = internalState;
         this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
         this.failure = failure;
      }

      Service.State externalState() {
         return this.shutdownWhenStartupFinishes && this.state == Service.State.STARTING ? Service.State.STOPPING : this.state;
      }

      Throwable failureCause() {
         Preconditions.checkState(this.state == Service.State.FAILED, "failureCause() is only valid if the service has failed, service is %s", this.state);
         return this.failure;
      }
   }

   private static class ListenerExecutorPair {
      final Service.Listener listener;
      final Executor executor;

      ListenerExecutorPair(Service.Listener listener, Executor executor) {
         this.listener = listener;
         this.executor = executor;
      }
   }

   private class Transition extends AbstractFuture<Service.State> {
      private Transition() {
      }

      public Service.State get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
         try {
            return (Service.State)super.get(timeout, unit);
         } catch (TimeoutException var5) {
            throw new TimeoutException(AbstractService.this.toString());
         }
      }

      // $FF: synthetic method
      Transition(Object x1) {
         this();
      }
   }
}

package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AggregateFuture<InputT, OutputT> extends AbstractFuture$TrustedFuture<OutputT> {
   private static final Logger logger = Logger.getLogger(AggregateFuture.class.getName());
   private AggregateFuture<InputT, OutputT>.RunningState runningState;

   protected final void afterDone() {
      super.afterDone();
      AggregateFuture<InputT, OutputT>.RunningState localRunningState = this.runningState;
      if (localRunningState != null) {
         this.runningState = null;
         ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures = localRunningState.futures;
         boolean wasInterrupted = this.wasInterrupted();
         if (wasInterrupted) {
            localRunningState.interruptTask();
         }

         if (this.isCancelled() & futures != null) {
            UnmodifiableIterator var4 = futures.iterator();

            while(var4.hasNext()) {
               ListenableFuture<?> future = (ListenableFuture)var4.next();
               future.cancel(wasInterrupted);
            }
         }
      }

   }

   protected String pendingToString() {
      AggregateFuture<InputT, OutputT>.RunningState localRunningState = this.runningState;
      if (localRunningState == null) {
         return null;
      } else {
         ImmutableCollection<? extends ListenableFuture<? extends InputT>> localFutures = localRunningState.futures;
         return localFutures != null ? "futures=[" + localFutures + "]" : null;
      }
   }

   final void init(AggregateFuture<InputT, OutputT>.RunningState runningState) {
      this.runningState = runningState;
      runningState.init();
   }

   private static boolean addCausalChain(Set<Throwable> seen, Throwable t) {
      while(t != null) {
         boolean firstTimeSeen = seen.add(t);
         if (!firstTimeSeen) {
            return false;
         }

         t = t.getCause();
      }

      return true;
   }

   abstract class RunningState extends AggregateFutureState implements Runnable {
      private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;
      private final boolean allMustSucceed;
      private final boolean collectsValues;

      RunningState(ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, boolean allMustSucceed, boolean collectsValues) {
         super(futures.size());
         this.futures = (ImmutableCollection)Preconditions.checkNotNull(futures);
         this.allMustSucceed = allMustSucceed;
         this.collectsValues = collectsValues;
      }

      public final void run() {
         this.decrementCountAndMaybeComplete();
      }

      private void init() {
         if (this.futures.isEmpty()) {
            this.handleAllCompleted();
         } else {
            if (this.allMustSucceed) {
               int i = 0;
               UnmodifiableIterator var2 = this.futures.iterator();

               while(var2.hasNext()) {
                  final ListenableFuture<? extends InputT> listenablex = (ListenableFuture)var2.next();
                  final int index = i++;
                  listenablex.addListener(new Runnable() {
                     public void run() {
                        try {
                           RunningState.this.handleOneInputDone(index, listenablex);
                        } finally {
                           RunningState.this.decrementCountAndMaybeComplete();
                        }

                     }
                  }, MoreExecutors.directExecutor());
               }
            } else {
               UnmodifiableIterator var5 = this.futures.iterator();

               while(var5.hasNext()) {
                  ListenableFuture<? extends InputT> listenable = (ListenableFuture)var5.next();
                  listenable.addListener(this, MoreExecutors.directExecutor());
               }
            }

         }
      }

      private void handleException(Throwable throwable) {
         Preconditions.checkNotNull(throwable);
         boolean completedWithFailure = false;
         boolean firstTimeSeeingThisException = true;
         if (this.allMustSucceed) {
            completedWithFailure = AggregateFuture.this.setException(throwable);
            if (completedWithFailure) {
               this.releaseResourcesAfterFailure();
            } else {
               firstTimeSeeingThisException = AggregateFuture.addCausalChain(this.getOrInitSeenExceptions(), throwable);
            }
         }

         if (throwable instanceof Error | this.allMustSucceed & !completedWithFailure & firstTimeSeeingThisException) {
            String message = throwable instanceof Error ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first";
            AggregateFuture.logger.log(Level.SEVERE, message, throwable);
         }

      }

      final void addInitialException(Set<Throwable> seen) {
         if (!AggregateFuture.this.isCancelled()) {
            AggregateFuture.addCausalChain(seen, AggregateFuture.this.trustedGetException());
         }

      }

      private void handleOneInputDone(int index, Future<? extends InputT> future) {
         Preconditions.checkState(this.allMustSucceed || !AggregateFuture.this.isDone() || AggregateFuture.this.isCancelled(), "Future was done before all dependencies completed");

         try {
            Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");
            if (this.allMustSucceed) {
               if (future.isCancelled()) {
                  AggregateFuture.this.runningState = null;
                  AggregateFuture.this.cancel(false);
               } else {
                  InputT result = Futures.getDone(future);
                  if (this.collectsValues) {
                     this.collectOneValue(this.allMustSucceed, index, result);
                  }
               }
            } else if (this.collectsValues && !future.isCancelled()) {
               this.collectOneValue(this.allMustSucceed, index, Futures.getDone(future));
            }
         } catch (ExecutionException var4) {
            this.handleException(var4.getCause());
         } catch (Throwable var5) {
            this.handleException(var5);
         }

      }

      private void decrementCountAndMaybeComplete() {
         int newRemaining = this.decrementRemainingAndGet();
         Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
         if (newRemaining == 0) {
            this.processCompleted();
         }

      }

      private void processCompleted() {
         if (this.collectsValues & !this.allMustSucceed) {
            int i = 0;
            UnmodifiableIterator var2 = this.futures.iterator();

            while(var2.hasNext()) {
               ListenableFuture<? extends InputT> listenable = (ListenableFuture)var2.next();
               this.handleOneInputDone(i++, listenable);
            }
         }

         this.handleAllCompleted();
      }

      void releaseResourcesAfterFailure() {
         this.futures = null;
      }

      abstract void collectOneValue(boolean var1, int var2, @Nullable InputT var3);

      abstract void handleAllCompleted();

      void interruptTask() {
      }
   }
}

package com.google.common.util.concurrent;

import com.google.common.collect.ImmutableList;
import java.util.concurrent.atomic.AtomicInteger;

final class Futures$InCompletionOrderState<T> {
   private boolean wasCancelled;
   private boolean shouldInterrupt;
   private final AtomicInteger incompleteOutputCount;
   private final ListenableFuture<? extends T>[] inputFutures;
   private volatile int delegateIndex;

   private Futures$InCompletionOrderState(ListenableFuture<? extends T>[] inputFutures) {
      this.wasCancelled = false;
      this.shouldInterrupt = true;
      this.delegateIndex = 0;
      this.inputFutures = inputFutures;
      this.incompleteOutputCount = new AtomicInteger(inputFutures.length);
   }

   private void recordOutputCancellation(boolean interruptIfRunning) {
      this.wasCancelled = true;
      if (!interruptIfRunning) {
         this.shouldInterrupt = false;
      }

      this.recordCompletion();
   }

   private void recordInputCompletion(ImmutableList<AbstractFuture<T>> delegates, int inputFutureIndex) {
      ListenableFuture<? extends T> inputFuture = this.inputFutures[inputFutureIndex];
      this.inputFutures[inputFutureIndex] = null;

      for(int i = this.delegateIndex; i < delegates.size(); ++i) {
         if (((AbstractFuture)delegates.get(i)).setFuture(inputFuture)) {
            this.recordCompletion();
            this.delegateIndex = i + 1;
            return;
         }
      }

      this.delegateIndex = delegates.size();
   }

   private void recordCompletion() {
      if (this.incompleteOutputCount.decrementAndGet() == 0 && this.wasCancelled) {
         ListenableFuture[] var1 = this.inputFutures;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ListenableFuture<?> toCancel = var1[var3];
            if (toCancel != null) {
               toCancel.cancel(this.shouldInterrupt);
            }
         }
      }

   }

   // $FF: synthetic method
   Futures$InCompletionOrderState(ListenableFuture[] x0, Object x1) {
      this(x0);
   }

   // $FF: synthetic method
   static void access$300(Futures$InCompletionOrderState x0, ImmutableList x1, int x2) {
      x0.recordInputCompletion(x1, x2);
   }

   // $FF: synthetic method
   static void access$400(Futures$InCompletionOrderState x0, boolean x1) {
      x0.recordOutputCancellation(x1);
   }

   // $FF: synthetic method
   static ListenableFuture[] access$500(Futures$InCompletionOrderState x0) {
      return x0.inputFutures;
   }

   // $FF: synthetic method
   static AtomicInteger access$600(Futures$InCompletionOrderState x0) {
      return x0.incompleteOutputCount;
   }
}

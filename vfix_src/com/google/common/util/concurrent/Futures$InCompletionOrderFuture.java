package com.google.common.util.concurrent;

final class Futures$InCompletionOrderFuture<T> extends AbstractFuture<T> {
   private Futures$InCompletionOrderState<T> state;

   private Futures$InCompletionOrderFuture(Futures$InCompletionOrderState<T> state) {
      this.state = state;
   }

   public boolean cancel(boolean interruptIfRunning) {
      Futures$InCompletionOrderState<T> localState = this.state;
      if (super.cancel(interruptIfRunning)) {
         Futures$InCompletionOrderState.access$400(localState, interruptIfRunning);
         return true;
      } else {
         return false;
      }
   }

   protected void afterDone() {
      this.state = null;
   }

   protected String pendingToString() {
      Futures$InCompletionOrderState<T> localState = this.state;
      return localState != null ? "inputCount=[" + Futures$InCompletionOrderState.access$500(localState).length + "], remaining=[" + Futures$InCompletionOrderState.access$600(localState).get() + "]" : null;
   }

   // $FF: synthetic method
   Futures$InCompletionOrderFuture(Futures$InCompletionOrderState x0, Object x1) {
      this(x0);
   }
}

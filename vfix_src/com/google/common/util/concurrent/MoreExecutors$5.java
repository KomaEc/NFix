package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

final class MoreExecutors$5 implements Executor {
   boolean thrownFromDelegate;
   // $FF: synthetic field
   final Executor val$delegate;
   // $FF: synthetic field
   final AbstractFuture val$future;

   MoreExecutors$5(Executor var1, AbstractFuture var2) {
      this.val$delegate = var1;
      this.val$future = var2;
      this.thrownFromDelegate = true;
   }

   public void execute(Runnable command) {
      try {
         this.val$delegate.execute(new MoreExecutors$5$1(this, command));
      } catch (RejectedExecutionException var3) {
         if (this.thrownFromDelegate) {
            this.val$future.setException(var3);
         }
      }

   }
}

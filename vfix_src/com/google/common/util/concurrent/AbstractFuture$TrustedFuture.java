package com.google.common.util.concurrent;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

abstract class AbstractFuture$TrustedFuture<V> extends AbstractFuture<V> {
   @CanIgnoreReturnValue
   public final V get() throws InterruptedException, ExecutionException {
      return super.get();
   }

   @CanIgnoreReturnValue
   public final V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      return super.get(timeout, unit);
   }

   public final boolean isDone() {
      return super.isDone();
   }

   public final boolean isCancelled() {
      return super.isCancelled();
   }

   public final void addListener(Runnable listener, Executor executor) {
      super.addListener(listener, executor);
   }

   @CanIgnoreReturnValue
   public final boolean cancel(boolean mayInterruptIfRunning) {
      return super.cancel(mayInterruptIfRunning);
   }
}

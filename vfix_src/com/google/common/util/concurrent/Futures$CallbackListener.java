package com.google.common.util.concurrent;

import com.google.common.base.MoreObjects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

final class Futures$CallbackListener<V> implements Runnable {
   final Future<V> future;
   final FutureCallback<? super V> callback;

   Futures$CallbackListener(Future<V> future, FutureCallback<? super V> callback) {
      this.future = future;
      this.callback = callback;
   }

   public void run() {
      Object value;
      try {
         value = Futures.getDone(this.future);
      } catch (ExecutionException var3) {
         this.callback.onFailure(var3.getCause());
         return;
      } catch (Error | RuntimeException var4) {
         this.callback.onFailure(var4);
         return;
      }

      this.callback.onSuccess(value);
   }

   public String toString() {
      return MoreObjects.toStringHelper((Object)this).addValue(this.callback).toString();
   }
}

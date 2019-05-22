package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import javax.annotation.Nullable;

final class AbstractFuture$Listener {
   static final AbstractFuture$Listener TOMBSTONE = new AbstractFuture$Listener((Runnable)null, (Executor)null);
   final Runnable task;
   final Executor executor;
   @Nullable
   AbstractFuture$Listener next;

   AbstractFuture$Listener(Runnable task, Executor executor) {
      this.task = task;
      this.executor = executor;
   }
}

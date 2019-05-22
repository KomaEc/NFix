package com.google.common.util.concurrent;

import javax.annotation.Nullable;

final class AbstractFuture$Cancellation {
   static final AbstractFuture$Cancellation CAUSELESS_INTERRUPTED;
   static final AbstractFuture$Cancellation CAUSELESS_CANCELLED;
   final boolean wasInterrupted;
   @Nullable
   final Throwable cause;

   AbstractFuture$Cancellation(boolean wasInterrupted, @Nullable Throwable cause) {
      this.wasInterrupted = wasInterrupted;
      this.cause = cause;
   }

   static {
      if (AbstractFuture.access$300()) {
         CAUSELESS_CANCELLED = null;
         CAUSELESS_INTERRUPTED = null;
      } else {
         CAUSELESS_CANCELLED = new AbstractFuture$Cancellation(false, (Throwable)null);
         CAUSELESS_INTERRUPTED = new AbstractFuture$Cancellation(true, (Throwable)null);
      }

   }
}

package com.google.common.util.concurrent;

import java.util.concurrent.ThreadFactory;

class AbstractScheduledService$1ThreadFactoryImpl implements ThreadFactory {
   // $FF: synthetic field
   final AbstractScheduledService this$0;

   AbstractScheduledService$1ThreadFactoryImpl(AbstractScheduledService this$0) {
      this.this$0 = this$0;
   }

   public Thread newThread(Runnable runnable) {
      return MoreExecutors.newThread(this.this$0.serviceName(), runnable);
   }
}

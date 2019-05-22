package com.google.common.util.concurrent;

class AbstractIdleService$DelegateService$2 implements Runnable {
   // $FF: synthetic field
   final AbstractIdleService$DelegateService this$1;

   AbstractIdleService$DelegateService$2(AbstractIdleService$DelegateService this$1) {
      this.this$1 = this$1;
   }

   public void run() {
      try {
         this.this$1.this$0.shutDown();
         this.this$1.notifyStopped();
      } catch (Throwable var2) {
         this.this$1.notifyFailed(var2);
      }

   }
}

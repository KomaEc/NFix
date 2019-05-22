package com.google.common.util.concurrent;

class AbstractIdleService$DelegateService$1 implements Runnable {
   // $FF: synthetic field
   final AbstractIdleService$DelegateService this$1;

   AbstractIdleService$DelegateService$1(AbstractIdleService$DelegateService this$1) {
      this.this$1 = this$1;
   }

   public void run() {
      try {
         this.this$1.this$0.startUp();
         this.this$1.notifyStarted();
      } catch (Throwable var2) {
         this.this$1.notifyFailed(var2);
      }

   }
}

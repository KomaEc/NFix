package com.google.common.util.concurrent;

class AbstractScheduledService$ServiceDelegate$2 implements Runnable {
   // $FF: synthetic field
   final AbstractScheduledService$ServiceDelegate this$1;

   AbstractScheduledService$ServiceDelegate$2(AbstractScheduledService$ServiceDelegate this$1) {
      this.this$1 = this$1;
   }

   public void run() {
      AbstractScheduledService$ServiceDelegate.access$200(this.this$1).lock();

      try {
         this.this$1.this$0.startUp();
         AbstractScheduledService$ServiceDelegate.access$302(this.this$1, this.this$1.this$0.scheduler().schedule(AbstractScheduledService.access$500(this.this$1.this$0), AbstractScheduledService$ServiceDelegate.access$600(this.this$1), AbstractScheduledService$ServiceDelegate.access$700(this.this$1)));
         this.this$1.notifyStarted();
      } catch (Throwable var5) {
         this.this$1.notifyFailed(var5);
         if (AbstractScheduledService$ServiceDelegate.access$300(this.this$1) != null) {
            AbstractScheduledService$ServiceDelegate.access$300(this.this$1).cancel(false);
         }
      } finally {
         AbstractScheduledService$ServiceDelegate.access$200(this.this$1).unlock();
      }

   }
}

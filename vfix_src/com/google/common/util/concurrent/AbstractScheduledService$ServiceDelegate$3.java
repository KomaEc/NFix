package com.google.common.util.concurrent;

class AbstractScheduledService$ServiceDelegate$3 implements Runnable {
   // $FF: synthetic field
   final AbstractScheduledService$ServiceDelegate this$1;

   AbstractScheduledService$ServiceDelegate$3(AbstractScheduledService$ServiceDelegate this$1) {
      this.this$1 = this$1;
   }

   public void run() {
      try {
         AbstractScheduledService$ServiceDelegate.access$200(this.this$1).lock();

         label49: {
            try {
               if (this.this$1.state() == Service.State.STOPPING) {
                  this.this$1.this$0.shutDown();
                  break label49;
               }
            } finally {
               AbstractScheduledService$ServiceDelegate.access$200(this.this$1).unlock();
            }

            return;
         }

         this.this$1.notifyStopped();
      } catch (Throwable var5) {
         this.this$1.notifyFailed(var5);
      }

   }
}

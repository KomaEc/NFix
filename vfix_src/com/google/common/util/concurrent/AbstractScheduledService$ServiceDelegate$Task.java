package com.google.common.util.concurrent;

import java.util.logging.Level;

class AbstractScheduledService$ServiceDelegate$Task implements Runnable {
   // $FF: synthetic field
   final AbstractScheduledService$ServiceDelegate this$1;

   AbstractScheduledService$ServiceDelegate$Task(AbstractScheduledService$ServiceDelegate this$1) {
      this.this$1 = this$1;
   }

   public void run() {
      AbstractScheduledService$ServiceDelegate.access$200(this.this$1).lock();

      try {
         if (AbstractScheduledService$ServiceDelegate.access$300(this.this$1).isCancelled()) {
            return;
         }

         this.this$1.this$0.runOneIteration();
      } catch (Throwable var8) {
         try {
            this.this$1.this$0.shutDown();
         } catch (Exception var7) {
            AbstractScheduledService.access$400().log(Level.WARNING, "Error while attempting to shut down the service after failure.", var7);
         }

         this.this$1.notifyFailed(var8);
         AbstractScheduledService$ServiceDelegate.access$300(this.this$1).cancel(false);
      } finally {
         AbstractScheduledService$ServiceDelegate.access$200(this.this$1).unlock();
      }

   }
}

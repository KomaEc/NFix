package com.google.common.util.concurrent;

class MoreExecutors$5$1 implements Runnable {
   // $FF: synthetic field
   final Runnable val$command;
   // $FF: synthetic field
   final MoreExecutors$5 this$0;

   MoreExecutors$5$1(MoreExecutors$5 this$0, Runnable var2) {
      this.this$0 = this$0;
      this.val$command = var2;
   }

   public void run() {
      this.this$0.thrownFromDelegate = false;
      this.val$command.run();
   }
}

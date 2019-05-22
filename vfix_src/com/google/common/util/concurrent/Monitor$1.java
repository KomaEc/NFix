package com.google.common.util.concurrent;

import java.util.function.BooleanSupplier;

class Monitor$1 extends Monitor.Guard {
   // $FF: synthetic field
   final BooleanSupplier val$isSatisfied;
   // $FF: synthetic field
   final Monitor this$0;

   Monitor$1(Monitor this$0, Monitor monitor, BooleanSupplier var3) {
      super(monitor);
      this.this$0 = this$0;
      this.val$isSatisfied = var3;
   }

   public boolean isSatisfied() {
      return this.val$isSatisfied.getAsBoolean();
   }
}

package com.google.common.util.concurrent;

import com.google.common.base.Supplier;

class AbstractScheduledService$ServiceDelegate$1 implements Supplier<String> {
   // $FF: synthetic field
   final AbstractScheduledService$ServiceDelegate this$1;

   AbstractScheduledService$ServiceDelegate$1(AbstractScheduledService$ServiceDelegate this$1) {
      this.this$1 = this$1;
   }

   public String get() {
      return this.this$1.this$0.serviceName() + " " + this.this$1.state();
   }
}

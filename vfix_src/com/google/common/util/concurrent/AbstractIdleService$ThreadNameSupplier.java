package com.google.common.util.concurrent;

import com.google.common.base.Supplier;

final class AbstractIdleService$ThreadNameSupplier implements Supplier<String> {
   // $FF: synthetic field
   final AbstractIdleService this$0;

   private AbstractIdleService$ThreadNameSupplier(AbstractIdleService var1) {
      this.this$0 = var1;
   }

   public String get() {
      return this.this$0.serviceName() + " " + this.this$0.state();
   }

   // $FF: synthetic method
   AbstractIdleService$ThreadNameSupplier(AbstractIdleService x0, Object x1) {
      this(x0);
   }
}

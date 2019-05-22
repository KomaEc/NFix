package com.google.common.util.concurrent;

final class AbstractService$IsStoppableGuard extends Monitor.Guard {
   // $FF: synthetic field
   final AbstractService this$0;

   AbstractService$IsStoppableGuard(AbstractService var1) {
      super(AbstractService.access$000(var1));
      this.this$0 = var1;
   }

   public boolean isSatisfied() {
      return this.this$0.state().compareTo(Service.State.RUNNING) <= 0;
   }
}

package com.google.common.util.concurrent;

final class AbstractIdleService$DelegateService extends AbstractService {
   // $FF: synthetic field
   final AbstractIdleService this$0;

   private AbstractIdleService$DelegateService(AbstractIdleService var1) {
      this.this$0 = var1;
   }

   protected final void doStart() {
      MoreExecutors.renamingDecorator(this.this$0.executor(), AbstractIdleService.access$200(this.this$0)).execute(new AbstractIdleService$DelegateService$1(this));
   }

   protected final void doStop() {
      MoreExecutors.renamingDecorator(this.this$0.executor(), AbstractIdleService.access$200(this.this$0)).execute(new AbstractIdleService$DelegateService$2(this));
   }

   public String toString() {
      return this.this$0.toString();
   }

   // $FF: synthetic method
   AbstractIdleService$DelegateService(AbstractIdleService x0, Object x1) {
      this(x0);
   }
}

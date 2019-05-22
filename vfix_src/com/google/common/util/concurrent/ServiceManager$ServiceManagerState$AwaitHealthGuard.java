package com.google.common.util.concurrent;

import javax.annotation.concurrent.GuardedBy;

final class ServiceManager$ServiceManagerState$AwaitHealthGuard extends Monitor.Guard {
   // $FF: synthetic field
   final ServiceManager.ServiceManagerState this$0;

   ServiceManager$ServiceManagerState$AwaitHealthGuard(ServiceManager.ServiceManagerState this$0) {
      super(this$0.monitor);
      this.this$0 = this$0;
   }

   @GuardedBy("ServiceManagerState.this.monitor")
   public boolean isSatisfied() {
      return this.this$0.states.count(Service.State.RUNNING) == this.this$0.numberOfServices || this.this$0.states.contains(Service.State.STOPPING) || this.this$0.states.contains(Service.State.TERMINATED) || this.this$0.states.contains(Service.State.FAILED);
   }
}

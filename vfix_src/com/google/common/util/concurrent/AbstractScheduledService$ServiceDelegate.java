package com.google.common.util.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.ReentrantLock;

final class AbstractScheduledService$ServiceDelegate extends AbstractService {
   private volatile Future<?> runningTask;
   private volatile ScheduledExecutorService executorService;
   private final ReentrantLock lock;
   private final Runnable task;
   // $FF: synthetic field
   final AbstractScheduledService this$0;

   private AbstractScheduledService$ServiceDelegate(AbstractScheduledService var1) {
      this.this$0 = var1;
      this.lock = new ReentrantLock();
      this.task = new AbstractScheduledService$ServiceDelegate$Task(this);
   }

   protected final void doStart() {
      this.executorService = MoreExecutors.renamingDecorator((ScheduledExecutorService)this.this$0.executor(), new AbstractScheduledService$ServiceDelegate$1(this));
      this.executorService.execute(new AbstractScheduledService$ServiceDelegate$2(this));
   }

   protected final void doStop() {
      this.runningTask.cancel(false);
      this.executorService.execute(new AbstractScheduledService$ServiceDelegate$3(this));
   }

   public String toString() {
      return this.this$0.toString();
   }

   // $FF: synthetic method
   AbstractScheduledService$ServiceDelegate(AbstractScheduledService x0, Object x1) {
      this(x0);
   }

   // $FF: synthetic method
   static ReentrantLock access$200(AbstractScheduledService$ServiceDelegate x0) {
      return x0.lock;
   }

   // $FF: synthetic method
   static Future access$300(AbstractScheduledService$ServiceDelegate x0) {
      return x0.runningTask;
   }

   // $FF: synthetic method
   static Future access$302(AbstractScheduledService$ServiceDelegate x0, Future x1) {
      return x0.runningTask = x1;
   }

   // $FF: synthetic method
   static ScheduledExecutorService access$600(AbstractScheduledService$ServiceDelegate x0) {
      return x0.executorService;
   }

   // $FF: synthetic method
   static Runnable access$700(AbstractScheduledService$ServiceDelegate x0) {
      return x0.task;
   }
}

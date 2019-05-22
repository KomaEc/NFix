package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
final class SequentialExecutor implements Executor {
   private static final Logger log = Logger.getLogger(SequentialExecutor.class.getName());
   private final Executor executor;
   @GuardedBy("queue")
   private final Queue<Runnable> queue = new ArrayDeque();
   @GuardedBy("queue")
   private boolean isWorkerRunning = false;
   private final SequentialExecutor.QueueWorker worker = new SequentialExecutor.QueueWorker();

   SequentialExecutor(Executor executor) {
      this.executor = (Executor)Preconditions.checkNotNull(executor);
   }

   public void execute(Runnable task) {
      synchronized(this.queue) {
         this.queue.add(task);
         if (this.isWorkerRunning) {
            return;
         }

         this.isWorkerRunning = true;
      }

      this.startQueueWorker();
   }

   private void startQueueWorker() {
      boolean executionRejected = true;
      boolean var10 = false;

      try {
         var10 = true;
         this.executor.execute(this.worker);
         executionRejected = false;
         var10 = false;
      } finally {
         if (var10) {
            if (executionRejected) {
               synchronized(this.queue) {
                  this.isWorkerRunning = false;
               }
            }

         }
      }

      if (executionRejected) {
         synchronized(this.queue) {
            this.isWorkerRunning = false;
         }
      }

   }

   private final class QueueWorker implements Runnable {
      private QueueWorker() {
      }

      public void run() {
         try {
            this.workOnQueue();
         } catch (Error var5) {
            synchronized(SequentialExecutor.this.queue) {
               SequentialExecutor.this.isWorkerRunning = false;
            }

            throw var5;
         }
      }

      private void workOnQueue() {
         boolean interruptedDuringTask = false;

         try {
            while(true) {
               interruptedDuringTask |= Thread.interrupted();
               Runnable task;
               synchronized(SequentialExecutor.this.queue) {
                  task = (Runnable)SequentialExecutor.this.queue.poll();
                  if (task == null) {
                     SequentialExecutor.this.isWorkerRunning = false;
                     return;
                  }
               }

               try {
                  task.run();
               } catch (RuntimeException var9) {
                  SequentialExecutor.log.log(Level.SEVERE, "Exception while executing runnable " + task, var9);
               }
            }
         } finally {
            if (interruptedDuringTask) {
               Thread.currentThread().interrupt();
            }

         }
      }

      // $FF: synthetic method
      QueueWorker(Object x1) {
         this();
      }
   }
}

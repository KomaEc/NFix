package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import javax.annotation.concurrent.GuardedBy;

final class ListenerCallQueue$PerListenerQueue<L> implements Runnable {
   final L listener;
   final Executor executor;
   @GuardedBy("this")
   final Queue<ListenerCallQueue$Event<L>> waitQueue = Queues.newArrayDeque();
   @GuardedBy("this")
   final Queue<Object> labelQueue = Queues.newArrayDeque();
   @GuardedBy("this")
   boolean isThreadScheduled;

   ListenerCallQueue$PerListenerQueue(L listener, Executor executor) {
      this.listener = Preconditions.checkNotNull(listener);
      this.executor = (Executor)Preconditions.checkNotNull(executor);
   }

   synchronized void add(ListenerCallQueue$Event<L> event, Object label) {
      this.waitQueue.add(event);
      this.labelQueue.add(label);
   }

   void dispatch() {
      boolean scheduleEventRunner = false;
      synchronized(this) {
         if (!this.isThreadScheduled) {
            this.isThreadScheduled = true;
            scheduleEventRunner = true;
         }
      }

      if (scheduleEventRunner) {
         try {
            this.executor.execute(this);
         } catch (RuntimeException var6) {
            synchronized(this) {
               this.isThreadScheduled = false;
            }

            ListenerCallQueue.access$000().log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, var6);
            throw var6;
         }
      }

   }

   public void run() {
      boolean stillRunning = true;

      while(true) {
         boolean var15 = false;

         try {
            var15 = true;
            ListenerCallQueue$Event nextToRun;
            Object nextLabel;
            synchronized(this) {
               Preconditions.checkState(this.isThreadScheduled);
               nextToRun = (ListenerCallQueue$Event)this.waitQueue.poll();
               nextLabel = this.labelQueue.poll();
               if (nextToRun == null) {
                  this.isThreadScheduled = false;
                  stillRunning = false;
                  var15 = false;
                  break;
               }
            }

            try {
               nextToRun.call(this.listener);
            } catch (RuntimeException var18) {
               ListenerCallQueue.access$000().log(Level.SEVERE, "Exception while executing callback: " + this.listener + " " + nextLabel, var18);
            }
         } finally {
            if (var15) {
               if (stillRunning) {
                  synchronized(this) {
                     this.isThreadScheduled = false;
                  }
               }

            }
         }
      }

      if (stillRunning) {
         synchronized(this) {
            this.isThreadScheduled = false;
         }
      }

   }
}

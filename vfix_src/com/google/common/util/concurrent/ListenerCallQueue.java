package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

final class ListenerCallQueue<L> implements Runnable {
   private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
   private final L listener;
   private final Executor executor;
   @GuardedBy("this")
   private final Queue<ListenerCallQueue.Callback<L>> waitQueue = Queues.newArrayDeque();
   @GuardedBy("this")
   private boolean isThreadScheduled;

   ListenerCallQueue(L listener, Executor executor) {
      this.listener = Preconditions.checkNotNull(listener);
      this.executor = (Executor)Preconditions.checkNotNull(executor);
   }

   synchronized void add(ListenerCallQueue.Callback<L> callback) {
      this.waitQueue.add(callback);
   }

   void execute() {
      boolean scheduleTaskRunner = false;
      synchronized(this) {
         if (!this.isThreadScheduled) {
            this.isThreadScheduled = true;
            scheduleTaskRunner = true;
         }
      }

      if (scheduleTaskRunner) {
         try {
            this.executor.execute(this);
         } catch (RuntimeException var6) {
            synchronized(this) {
               this.isThreadScheduled = false;
            }

            Logger var10000 = logger;
            Level var10001 = Level.SEVERE;
            String var3 = String.valueOf(String.valueOf(this.listener));
            String var4 = String.valueOf(String.valueOf(this.executor));
            var10000.log(var10001, (new StringBuilder(42 + var3.length() + var4.length())).append("Exception while running callbacks for ").append(var3).append(" on ").append(var4).toString(), var6);
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
            ListenerCallQueue.Callback nextToRun;
            synchronized(this) {
               Preconditions.checkState(this.isThreadScheduled);
               nextToRun = (ListenerCallQueue.Callback)this.waitQueue.poll();
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
               Logger var10000 = logger;
               Level var10001 = Level.SEVERE;
               String var4 = String.valueOf(String.valueOf(this.listener));
               String var5 = String.valueOf(String.valueOf(nextToRun.methodCall));
               var10000.log(var10001, (new StringBuilder(37 + var4.length() + var5.length())).append("Exception while executing callback: ").append(var4).append(".").append(var5).toString(), var18);
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

   abstract static class Callback<L> {
      private final String methodCall;

      Callback(String methodCall) {
         this.methodCall = methodCall;
      }

      abstract void call(L var1);

      void enqueueOn(Iterable<ListenerCallQueue<L>> queues) {
         Iterator i$ = queues.iterator();

         while(i$.hasNext()) {
            ListenerCallQueue<L> queue = (ListenerCallQueue)i$.next();
            queue.add(this);
         }

      }
   }
}

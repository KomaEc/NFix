package org.jboss.util.threadpool;

import org.jboss.logging.Logger;

public class RunnableTaskWrapper implements TaskWrapper {
   private static final Logger log = Logger.getLogger(RunnableTaskWrapper.class);
   private Runnable runnable;
   private boolean started;
   private Thread runThread;
   private long startTimeout;
   private long completionTimeout;

   public RunnableTaskWrapper(Runnable runnable) {
      this(runnable, 0L, 0L);
   }

   public RunnableTaskWrapper(Runnable runnable, long startTimeout, long completeTimeout) {
      if (runnable == null) {
         throw new IllegalArgumentException("Null runnable");
      } else {
         this.runnable = runnable;
         this.startTimeout = startTimeout;
         this.completionTimeout = completeTimeout;
      }
   }

   public int getTaskWaitType() {
      return 0;
   }

   public int getTaskPriority() {
      return 5;
   }

   public long getTaskStartTimeout() {
      return this.startTimeout;
   }

   public long getTaskCompletionTimeout() {
      return this.completionTimeout;
   }

   public void acceptTask() {
   }

   public void rejectTask(RuntimeException t) {
      throw t;
   }

   public void stopTask() {
      boolean trace = log.isTraceEnabled();
      if (this.runThread != null && !this.runThread.isInterrupted()) {
         this.runThread.interrupt();
         if (trace) {
            log.trace("stopTask, interrupted thread=" + this.runThread);
         }
      } else if (this.runThread != null) {
         this.runThread.stop();
         if (trace) {
            log.trace("stopTask, stopped thread=" + this.runThread);
         }
      }

   }

   public void waitForTask() {
   }

   public boolean isComplete() {
      return this.started && this.runThread == null;
   }

   public void run() {
      boolean trace = log.isTraceEnabled();

      try {
         if (trace) {
            log.trace("Begin run, wrapper=" + this);
         }

         this.runThread = Thread.currentThread();
         this.started = true;
         this.runnable.run();
         this.runThread = null;
         if (trace) {
            log.trace("End run, wrapper=" + this);
         }
      } catch (Throwable var3) {
         log.warn("Unhandled throwable for runnable: " + this.runnable, var3);
      }

   }
}

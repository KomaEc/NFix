package org.jboss.util;

public class WorkerQueue {
   protected Thread m_queueThread;
   private WorkerQueue.JobItem m_currentJob;

   public WorkerQueue() {
      this("Worker Thread");
   }

   public WorkerQueue(String threadName) {
      this.m_queueThread = new Thread(this.createQueueLoop(), threadName);
   }

   public WorkerQueue(String threadName, boolean isDaemon) {
      this.m_queueThread = new Thread(this.createQueueLoop(), threadName);
      this.m_queueThread.setDaemon(isDaemon);
   }

   public void start() {
      if (this.m_queueThread != null) {
         this.m_queueThread.start();
      }

   }

   public synchronized void stop() {
      if (this.m_queueThread != null) {
         this.m_queueThread.interrupt();
      }

   }

   public synchronized void putJob(Executable job) {
      if (this.m_queueThread != null && this.m_queueThread.isAlive()) {
         if (this.isInterrupted()) {
            throw new IllegalStateException("Can't put job, thread was interrupted");
         } else {
            this.putJobImpl(job);
         }
      } else {
         throw new IllegalStateException("Can't put job, thread is not alive or not present");
      }
   }

   protected boolean isInterrupted() {
      return this.m_queueThread.isInterrupted();
   }

   protected synchronized Executable getJob() throws InterruptedException {
      if (this.m_queueThread != null && this.m_queueThread.isAlive()) {
         return this.getJobImpl();
      } else {
         throw new IllegalStateException();
      }
   }

   protected Executable getJobImpl() throws InterruptedException {
      while(this.m_currentJob == null) {
         this.wait();
      }

      WorkerQueue.JobItem item = this.m_currentJob;
      this.m_currentJob = this.m_currentJob.m_next;
      return item.m_job;
   }

   protected void putJobImpl(Executable job) {
      WorkerQueue.JobItem posted = new WorkerQueue.JobItem(job);
      if (this.m_currentJob == null) {
         this.m_currentJob = posted;
         this.notifyAll();
      } else {
         WorkerQueue.JobItem item;
         for(item = this.m_currentJob; item.m_next != null; item = item.m_next) {
         }

         item.m_next = posted;
      }

   }

   protected void clear() {
      this.m_queueThread = null;
      this.m_currentJob = null;
   }

   protected Runnable createQueueLoop() {
      return new WorkerQueue.QueueLoop();
   }

   private class JobItem {
      private Executable m_job;
      private WorkerQueue.JobItem m_next;

      private JobItem(Executable job) {
         this.m_job = job;
      }

      // $FF: synthetic method
      JobItem(Executable x1, Object x2) {
         this(x1);
      }
   }

   protected class QueueLoop implements Runnable {
      public void run() {
         try {
            while(true) {
               try {
                  if (WorkerQueue.this.isInterrupted()) {
                     this.flush();
                     break;
                  }

                  WorkerQueue.this.getJob().execute();
               } catch (InterruptedException var9) {
                  try {
                     this.flush();
                  } catch (Exception var8) {
                  }
                  break;
               } catch (Exception var10) {
                  ThrowableHandler.add(1, var10);
               }
            }
         } finally {
            WorkerQueue.this.clear();
         }

      }

      protected void flush() throws Exception {
         while(WorkerQueue.this.m_currentJob != null) {
            WorkerQueue.this.m_currentJob.m_job.execute();
            WorkerQueue.this.m_currentJob = WorkerQueue.this.m_currentJob.m_next;
         }

      }
   }
}

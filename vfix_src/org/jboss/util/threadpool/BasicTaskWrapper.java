package org.jboss.util.threadpool;

import org.jboss.logging.Logger;

public class BasicTaskWrapper implements TaskWrapper {
   private static final Logger log = Logger.getLogger(BasicTaskWrapper.class);
   public static final int TASK_NOT_ACCEPTED = 0;
   public static final int TASK_ACCEPTED = 1;
   public static final int TASK_STARTED = 2;
   public static final int TASK_COMPLETED = 3;
   public static final int TASK_REJECTED = -1;
   public static final int TASK_STOPPED = -2;
   private int state = 0;
   private Object stateLock = new Object();
   private Task task;
   private String taskString;
   private long startTime;
   private long startTimeout;
   private long completionTimeout;
   private int priority;
   private int waitType;
   private Thread runThread;

   protected BasicTaskWrapper() {
   }

   public BasicTaskWrapper(Task task) {
      this.setTask(task);
   }

   public int getTaskWaitType() {
      return this.waitType;
   }

   public int getTaskPriority() {
      return this.priority;
   }

   public long getTaskStartTimeout() {
      return this.startTimeout;
   }

   public long getTaskCompletionTimeout() {
      return this.completionTimeout;
   }

   public void acceptTask() {
      synchronized(this.stateLock) {
         if (this.state != 0) {
            return;
         }
      }

      if (this.taskAccepted()) {
         this.state = 1;
      } else {
         this.state = -1;
      }

      synchronized(this.stateLock) {
         this.stateLock.notifyAll();
      }
   }

   public void rejectTask(RuntimeException e) {
      synchronized(this.stateLock) {
         this.state = -1;
         this.stateLock.notifyAll();
      }

      this.taskRejected(e);
   }

   public boolean isComplete() {
      return this.state == 3;
   }

   public void stopTask() {
      boolean started;
      synchronized(this.stateLock) {
         started = this.state == 2;
         this.state = -2;
      }

      if (started) {
         if (this.runThread != null) {
            this.runThread.interrupt();
         }

         this.taskStop();
      } else if (this.runThread != null && this.runThread.isInterrupted()) {
         this.runThread.stop();
      }

   }

   public void waitForTask() {
      switch(this.waitType) {
      case 1:
         boolean interrupted = false;
         synchronized(this.stateLock) {
            while(this.state == 0 || this.state == 1) {
               try {
                  this.stateLock.wait();
               } catch (InterruptedException var5) {
                  interrupted = true;
               }
            }

            if (interrupted) {
               Thread.currentThread().interrupt();
            }

            return;
         }
      default:
      }
   }

   public void run() {
      this.runThread = Thread.currentThread();
      long runTime = this.getElapsedTime();
      if (this.startTimeout > 0L && runTime >= this.startTimeout) {
         this.taskRejected(new StartTimeoutException("Start Timeout exceeded for task " + this.taskString));
      } else {
         boolean stopped = false;
         synchronized(this.stateLock) {
            if (this.state == -2) {
               stopped = true;
            } else {
               this.state = 2;
               this.taskStarted();
               if (this.waitType == 1) {
                  this.stateLock.notifyAll();
               }
            }
         }

         if (stopped) {
            this.taskRejected(new TaskStoppedException("Task stopped for task " + this.taskString));
         } else {
            Throwable throwable = null;

            try {
               this.task.execute();
            } catch (Throwable var8) {
               throwable = var8;
            }

            this.taskCompleted(throwable);
            synchronized(this.stateLock) {
               this.state = 3;
               if (this.waitType == 2) {
                  this.stateLock.notifyAll();
               }

            }
         }
      }
   }

   protected void setTask(Task task) {
      if (task == null) {
         throw new IllegalArgumentException("Null task");
      } else {
         this.task = task;
         this.taskString = task.toString();
         this.startTime = System.currentTimeMillis();
         this.waitType = task.getWaitType();
         this.priority = task.getPriority();
         this.startTimeout = task.getStartTimeout();
         this.completionTimeout = task.getCompletionTimeout();
      }
   }

   protected boolean taskAccepted() {
      try {
         this.task.accepted(this.getElapsedTime());
         return true;
      } catch (Throwable var2) {
         log.warn("Unexpected error during 'accepted' for task: " + this.taskString, var2);
         return false;
      }
   }

   protected boolean taskRejected(RuntimeException e) {
      try {
         this.task.rejected(this.getElapsedTime(), e);
         return true;
      } catch (Throwable var3) {
         log.warn("Unexpected error during 'rejected' for task: " + this.taskString, var3);
         if (e != null) {
            log.warn("Original reason for rejection of task: " + this.taskString, e);
         }

         return false;
      }
   }

   protected boolean taskStarted() {
      try {
         this.task.started(this.getElapsedTime());
         return true;
      } catch (Throwable var2) {
         log.warn("Unexpected error during 'started' for task: " + this.taskString, var2);
         return false;
      }
   }

   protected boolean taskCompleted(Throwable throwable) {
      try {
         this.task.completed(this.getElapsedTime(), throwable);
         return true;
      } catch (Throwable var3) {
         log.warn("Unexpected error during 'completed' for task: " + this.taskString, var3);
         if (throwable != null) {
            log.warn("Original error during 'run' for task: " + this.taskString, throwable);
         }

         return false;
      }
   }

   protected boolean taskStop() {
      try {
         this.task.stop();
         return true;
      } catch (Throwable var2) {
         log.warn("Unexpected error during 'stop' for task: " + this.taskString, var2);
         return false;
      }
   }

   protected long getElapsedTime() {
      return System.currentTimeMillis() - this.startTime;
   }

   protected String getStateString() {
      switch(this.state) {
      case -2:
         return "STOPPED";
      case -1:
         return "REJECTED";
      case 0:
         return "NOT_ACCEPTED";
      case 1:
         return "ACCEPTED";
      case 2:
         return "STARTED";
      case 3:
         return "COMPLETED";
      default:
         return "???";
      }
   }
}

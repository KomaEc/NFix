package org.apache.tools.ant.taskdefs;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.util.StringUtils;

public class Parallel extends Task implements TaskContainer {
   private Vector nestedTasks = new Vector();
   private final Object semaphore = new Object();
   private int numThreads = 0;
   private int numThreadsPerProcessor = 0;
   private long timeout;
   private volatile boolean stillRunning;
   private boolean timedOut;
   private boolean failOnAny;
   private Parallel.TaskList daemonTasks;
   private StringBuffer exceptionMessage;
   private int numExceptions = 0;
   private Throwable firstException;
   private Location firstLocation;
   // $FF: synthetic field
   static Class class$java$lang$Runtime;

   public void addDaemons(Parallel.TaskList daemonTasks) {
      if (this.daemonTasks != null) {
         throw new BuildException("Only one daemon group is supported");
      } else {
         this.daemonTasks = daemonTasks;
      }
   }

   public void setPollInterval(int pollInterval) {
   }

   public void setFailOnAny(boolean failOnAny) {
      this.failOnAny = failOnAny;
   }

   public void addTask(Task nestedTask) {
      this.nestedTasks.addElement(nestedTask);
   }

   public void setThreadsPerProcessor(int numThreadsPerProcessor) {
      this.numThreadsPerProcessor = numThreadsPerProcessor;
   }

   public void setThreadCount(int numThreads) {
      this.numThreads = numThreads;
   }

   public void setTimeout(long timeout) {
      this.timeout = timeout;
   }

   public void execute() throws BuildException {
      this.updateThreadCounts();
      if (this.numThreads == 0) {
         this.numThreads = this.nestedTasks.size();
      }

      this.spinThreads();
   }

   private void updateThreadCounts() {
      if (this.numThreadsPerProcessor != 0) {
         int numProcessors = this.getNumProcessors();
         if (numProcessors != 0) {
            this.numThreads = numProcessors * this.numThreadsPerProcessor;
         }
      }

   }

   private void processExceptions(Parallel.TaskRunnable[] runnables) {
      if (runnables != null) {
         for(int i = 0; i < runnables.length; ++i) {
            Throwable t = runnables[i].getException();
            if (t != null) {
               ++this.numExceptions;
               if (this.firstException == null) {
                  this.firstException = t;
               }

               if (t instanceof BuildException && this.firstLocation == Location.UNKNOWN_LOCATION) {
                  this.firstLocation = ((BuildException)t).getLocation();
               }

               this.exceptionMessage.append(StringUtils.LINE_SEP);
               this.exceptionMessage.append(t.getMessage());
            }
         }

      }
   }

   private void spinThreads() throws BuildException {
      int numTasks = this.nestedTasks.size();
      Parallel.TaskRunnable[] runnables = new Parallel.TaskRunnable[numTasks];
      this.stillRunning = true;
      this.timedOut = false;
      int threadNumber = 0;

      for(Enumeration e = this.nestedTasks.elements(); e.hasMoreElements(); ++threadNumber) {
         Task nestedTask = (Task)e.nextElement();
         runnables[threadNumber] = new Parallel.TaskRunnable(nestedTask);
      }

      int maxRunning = numTasks < this.numThreads ? numTasks : this.numThreads;
      Parallel.TaskRunnable[] running = new Parallel.TaskRunnable[maxRunning];
      threadNumber = 0;
      ThreadGroup group = new ThreadGroup("parallel");
      Parallel.TaskRunnable[] daemons = null;
      if (this.daemonTasks != null && this.daemonTasks.tasks.size() != 0) {
         daemons = new Parallel.TaskRunnable[this.daemonTasks.tasks.size()];
      }

      synchronized(this.semaphore) {
         ;
      }

      synchronized(this.semaphore) {
         int i;
         Thread thread;
         if (daemons != null) {
            for(i = 0; i < daemons.length; ++i) {
               daemons[i] = new Parallel.TaskRunnable((Task)this.daemonTasks.tasks.get(i));
               thread = new Thread(group, daemons[i]);
               thread.setDaemon(true);
               thread.start();
            }
         }

         i = 0;

         label112:
         while(true) {
            if (i >= maxRunning) {
               if (this.timeout != 0L) {
                  Thread timeoutThread = new Thread() {
                     public synchronized void run() {
                        try {
                           this.wait(Parallel.this.timeout);
                           synchronized(Parallel.this.semaphore) {
                              Parallel.this.stillRunning = false;
                              Parallel.this.timedOut = true;
                              Parallel.this.semaphore.notifyAll();
                           }
                        } catch (InterruptedException var4) {
                        }

                     }
                  };
                  timeoutThread.start();
               }

               label107:
               while(threadNumber < numTasks && this.stillRunning) {
                  for(i = 0; i < maxRunning; ++i) {
                     if (running[i] == null || running[i].isFinished()) {
                        running[i] = runnables[threadNumber++];
                        thread = new Thread(group, running[i]);
                        thread.start();
                        continue label107;
                     }
                  }

                  try {
                     this.semaphore.wait();
                  } catch (InterruptedException var13) {
                  }
               }

               label92:
               while(true) {
                  if (this.stillRunning) {
                     for(i = 0; i < maxRunning; ++i) {
                        if (running[i] != null && !running[i].isFinished()) {
                           try {
                              this.semaphore.wait();
                           } catch (InterruptedException var12) {
                           }
                           continue label92;
                        }
                     }

                     this.stillRunning = false;
                     continue;
                  }
                  break label112;
               }
            }

            running[i] = runnables[threadNumber++];
            thread = new Thread(group, running[i]);
            thread.start();
            ++i;
         }
      }

      if (this.timedOut) {
         throw new BuildException("Parallel execution timed out");
      } else {
         this.exceptionMessage = new StringBuffer();
         this.numExceptions = 0;
         this.firstException = null;
         this.firstLocation = Location.UNKNOWN_LOCATION;
         this.processExceptions(daemons);
         this.processExceptions(runnables);
         if (this.numExceptions == 1) {
            if (this.firstException instanceof BuildException) {
               throw (BuildException)this.firstException;
            } else {
               throw new BuildException(this.firstException);
            }
         } else if (this.numExceptions > 1) {
            throw new BuildException(this.exceptionMessage.toString(), this.firstLocation);
         }
      }
   }

   private int getNumProcessors() {
      try {
         Class[] paramTypes = new Class[0];
         Method availableProcessors = (class$java$lang$Runtime == null ? (class$java$lang$Runtime = class$("java.lang.Runtime")) : class$java$lang$Runtime).getMethod("availableProcessors", paramTypes);
         Object[] args = new Object[0];
         Integer ret = (Integer)availableProcessors.invoke(Runtime.getRuntime(), args);
         return ret;
      } catch (Exception var5) {
         return 0;
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   private class TaskRunnable implements Runnable {
      private Throwable exception;
      private Task task;
      private boolean finished;

      TaskRunnable(Task task) {
         this.task = task;
      }

      public void run() {
         boolean var12 = false;

         label95: {
            try {
               var12 = true;
               this.task.perform();
               var12 = false;
               break label95;
            } catch (Throwable var16) {
               this.exception = var16;
               if (Parallel.this.failOnAny) {
                  Parallel.this.stillRunning = false;
                  var12 = false;
               } else {
                  var12 = false;
               }
            } finally {
               if (var12) {
                  synchronized(Parallel.this.semaphore) {
                     this.finished = true;
                     Parallel.this.semaphore.notifyAll();
                  }
               }
            }

            synchronized(Parallel.this.semaphore) {
               this.finished = true;
               Parallel.this.semaphore.notifyAll();
               return;
            }
         }

         synchronized(Parallel.this.semaphore) {
            this.finished = true;
            Parallel.this.semaphore.notifyAll();
         }

      }

      public Throwable getException() {
         return this.exception;
      }

      boolean isFinished() {
         return this.finished;
      }
   }

   public static class TaskList implements TaskContainer {
      private List tasks = new ArrayList();

      public void addTask(Task nestedTask) {
         this.tasks.add(nestedTask);
      }
   }
}

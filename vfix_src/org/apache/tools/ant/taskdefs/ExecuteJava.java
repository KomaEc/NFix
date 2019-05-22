package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Permissions;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.TimeoutObserver;
import org.apache.tools.ant.util.Watchdog;

public class ExecuteJava implements Runnable, TimeoutObserver {
   private Commandline javaCommand = null;
   private Path classpath = null;
   private CommandlineJava.SysProperties sysProperties = null;
   private Permissions perm = null;
   private Method main = null;
   private Long timeout = null;
   private volatile Throwable caught = null;
   private volatile boolean timedOut = false;
   private Thread thread = null;
   // $FF: synthetic field
   static Class array$Ljava$lang$String;

   public void setJavaCommand(Commandline javaCommand) {
      this.javaCommand = javaCommand;
   }

   public void setClasspath(Path p) {
      this.classpath = p;
   }

   public void setSystemProperties(CommandlineJava.SysProperties s) {
      this.sysProperties = s;
   }

   public void setPermissions(Permissions permissions) {
      this.perm = permissions;
   }

   /** @deprecated */
   public void setOutput(PrintStream out) {
   }

   public void setTimeout(Long timeout) {
      this.timeout = timeout;
   }

   public void execute(Project project) throws BuildException {
      String classname = this.javaCommand.getExecutable();
      AntClassLoader loader = null;

      try {
         if (this.sysProperties != null) {
            this.sysProperties.setSystem();
         }

         Class target = null;

         try {
            if (this.classpath == null) {
               target = Class.forName(classname);
            } else {
               loader = project.createClassLoader(this.classpath);
               loader.setParent(project.getCoreLoader());
               loader.setParentFirst(false);
               loader.addJavaLibraries();
               loader.setIsolated(true);
               loader.setThreadContextLoader();
               loader.forceLoadClass(classname);
               target = Class.forName(classname, true, loader);
            }
         } catch (ClassNotFoundException var21) {
            throw new BuildException("Could not find " + classname + "." + " Make sure you have it in your" + " classpath");
         }

         this.main = target.getMethod("main", array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
         if (this.main == null) {
            throw new BuildException("Could not find main() method in " + classname);
         }

         if ((this.main.getModifiers() & 8) == 0) {
            throw new BuildException("main() method in " + classname + " is not declared static");
         }

         if (this.timeout == null) {
            this.run();
         } else {
            this.thread = new Thread(this, "ExecuteJava");
            Task currentThreadTask = project.getThreadTask(Thread.currentThread());
            project.registerThreadTask(this.thread, currentThreadTask);
            this.thread.setDaemon(true);
            Watchdog w = new Watchdog(this.timeout);
            w.addTimeoutObserver(this);
            synchronized(this) {
               this.thread.start();
               w.start();

               try {
                  this.wait();
               } catch (InterruptedException var19) {
               }

               if (this.timedOut) {
                  project.log("Timeout: sub-process interrupted", 1);
               } else {
                  this.thread = null;
                  w.stop();
               }
            }
         }

         if (this.caught != null) {
            throw this.caught;
         }
      } catch (BuildException var22) {
         throw var22;
      } catch (SecurityException var23) {
         throw var23;
      } catch (ThreadDeath var24) {
         throw var24;
      } catch (Throwable var25) {
         throw new BuildException(var25);
      } finally {
         if (loader != null) {
            loader.resetThreadContextLoader();
            loader.cleanup();
            loader = null;
         }

         if (this.sysProperties != null) {
            this.sysProperties.restoreSystem();
         }

      }

   }

   public void run() {
      Object[] argument = new Object[]{this.javaCommand.getArguments()};
      boolean var16 = false;

      label170: {
         label171: {
            try {
               var16 = true;
               if (this.perm != null) {
                  this.perm.setSecurityManager();
               }

               this.main.invoke((Object)null, argument);
               var16 = false;
               break label170;
            } catch (InvocationTargetException var21) {
               Throwable t = var21.getTargetException();
               if (!(t instanceof InterruptedException)) {
                  this.caught = t;
                  var16 = false;
               } else {
                  var16 = false;
               }
            } catch (Throwable var22) {
               this.caught = var22;
               var16 = false;
               break label171;
            } finally {
               if (var16) {
                  if (this.perm != null) {
                     this.perm.restoreSecurityManager();
                  }

                  synchronized(this) {
                     this.notifyAll();
                  }
               }
            }

            if (this.perm != null) {
               this.perm.restoreSecurityManager();
            }

            synchronized(this) {
               this.notifyAll();
               return;
            }
         }

         if (this.perm != null) {
            this.perm.restoreSecurityManager();
         }

         synchronized(this) {
            this.notifyAll();
            return;
         }
      }

      if (this.perm != null) {
         this.perm.restoreSecurityManager();
      }

      synchronized(this) {
         this.notifyAll();
      }

   }

   public synchronized void timeoutOccured(Watchdog w) {
      if (this.thread != null) {
         this.timedOut = true;
         this.thread.interrupt();
      }

      this.notifyAll();
   }

   public synchronized boolean killedProcess() {
      return this.timedOut;
   }

   public int fork(ProjectComponent pc) throws BuildException {
      CommandlineJava cmdl = new CommandlineJava();
      cmdl.setClassname(this.javaCommand.getExecutable());
      String[] args = this.javaCommand.getArguments();

      for(int i = 0; i < args.length; ++i) {
         cmdl.createArgument().setValue(args[i]);
      }

      if (this.classpath != null) {
         cmdl.createClasspath(pc.getProject()).append(this.classpath);
      }

      if (this.sysProperties != null) {
         cmdl.addSysproperties(this.sysProperties);
      }

      Redirector redirector = new Redirector(pc);
      Execute exe = new Execute(redirector.createHandler(), this.timeout == null ? null : new ExecuteWatchdog(this.timeout));
      exe.setAntRun(pc.getProject());
      if (Os.isFamily("openvms")) {
         setupCommandLineForVMS(exe, cmdl.getCommandline());
      } else {
         exe.setCommandline(cmdl.getCommandline());
      }

      int var7;
      try {
         int rc = exe.execute();
         redirector.complete();
         var7 = rc;
      } catch (IOException var11) {
         throw new BuildException(var11);
      } finally {
         this.timedOut = exe.killedProcess();
      }

      return var7;
   }

   public static void setupCommandLineForVMS(Execute exe, String[] command) {
      exe.setVMLauncher(true);
      File vmsJavaOptionFile = null;

      try {
         String[] args = new String[command.length - 1];
         System.arraycopy(command, 1, args, 0, command.length - 1);
         vmsJavaOptionFile = JavaEnvUtils.createVmsJavaOptionFile(args);
         vmsJavaOptionFile.deleteOnExit();
         String[] vmsCmd = new String[]{command[0], "-V", vmsJavaOptionFile.getPath()};
         exe.setCommandline(vmsCmd);
      } catch (IOException var5) {
         throw new BuildException("Failed to create a temporary file for \"-V\" switch");
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
}

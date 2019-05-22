package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.RedirectorElement;
import org.apache.tools.ant.util.FileUtils;

public class ExecTask extends Task {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private String os;
   private String osFamily;
   private File dir;
   protected boolean failOnError = false;
   protected boolean newEnvironment = false;
   private Long timeout = null;
   private Environment env = new Environment();
   protected Commandline cmdl = new Commandline();
   private String resultProperty;
   private boolean failIfExecFails = true;
   private String executable;
   private boolean resolveExecutable = false;
   private boolean searchPath = false;
   private boolean spawn = false;
   private boolean incompatibleWithSpawn = false;
   private String inputString;
   private File input;
   private File output;
   private File error;
   protected Redirector redirector = new Redirector(this);
   protected RedirectorElement redirectorElement;
   private boolean vmLauncher = true;

   public ExecTask() {
   }

   public ExecTask(Task owner) {
      this.bindToOwner(owner);
   }

   public void setSpawn(boolean spawn) {
      this.spawn = spawn;
   }

   public void setTimeout(Long value) {
      this.timeout = value;
      this.incompatibleWithSpawn = true;
   }

   public void setTimeout(Integer value) {
      this.setTimeout(value == null ? null : new Long((long)value));
   }

   public void setExecutable(String value) {
      this.executable = value;
      this.cmdl.setExecutable(value);
   }

   public void setDir(File d) {
      this.dir = d;
   }

   public void setOs(String os) {
      this.os = os;
   }

   public void setCommand(Commandline cmdl) {
      this.log("The command attribute is deprecated.\nPlease use the executable attribute and nested arg elements.", 1);
      this.cmdl = cmdl;
   }

   public void setOutput(File out) {
      this.output = out;
      this.incompatibleWithSpawn = true;
   }

   public void setInput(File input) {
      if (this.inputString != null) {
         throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
      } else {
         this.input = input;
         this.incompatibleWithSpawn = true;
      }
   }

   public void setInputString(String inputString) {
      if (this.input != null) {
         throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
      } else {
         this.inputString = inputString;
         this.incompatibleWithSpawn = true;
      }
   }

   public void setLogError(boolean logError) {
      this.redirector.setLogError(logError);
      this.incompatibleWithSpawn |= logError;
   }

   public void setError(File error) {
      this.error = error;
      this.incompatibleWithSpawn = true;
   }

   public void setOutputproperty(String outputProp) {
      this.redirector.setOutputProperty(outputProp);
      this.incompatibleWithSpawn = true;
   }

   public void setErrorProperty(String errorProperty) {
      this.redirector.setErrorProperty(errorProperty);
      this.incompatibleWithSpawn = true;
   }

   public void setFailonerror(boolean fail) {
      this.failOnError = fail;
      this.incompatibleWithSpawn |= fail;
   }

   public void setNewenvironment(boolean newenv) {
      this.newEnvironment = newenv;
   }

   public void setResolveExecutable(boolean resolveExecutable) {
      this.resolveExecutable = resolveExecutable;
   }

   public void setSearchPath(boolean searchPath) {
      this.searchPath = searchPath;
   }

   public boolean getResolveExecutable() {
      return this.resolveExecutable;
   }

   public void addEnv(Environment.Variable var) {
      this.env.addVariable(var);
   }

   public Commandline.Argument createArg() {
      return this.cmdl.createArgument();
   }

   public void setResultProperty(String resultProperty) {
      this.resultProperty = resultProperty;
      this.incompatibleWithSpawn = true;
   }

   protected void maybeSetResultPropertyValue(int result) {
      if (this.resultProperty != null) {
         String res = Integer.toString(result);
         this.getProject().setNewProperty(this.resultProperty, res);
      }

   }

   public void setFailIfExecutionFails(boolean flag) {
      this.failIfExecFails = flag;
      this.incompatibleWithSpawn = true;
   }

   public void setAppend(boolean append) {
      this.redirector.setAppend(append);
      this.incompatibleWithSpawn = true;
   }

   public void addConfiguredRedirector(RedirectorElement redirectorElement) {
      if (this.redirectorElement != null) {
         throw new BuildException("cannot have > 1 nested <redirector>s");
      } else {
         this.redirectorElement = redirectorElement;
         this.incompatibleWithSpawn = true;
      }
   }

   public void setOsFamily(String osFamily) {
      this.osFamily = osFamily.toLowerCase(Locale.US);
   }

   protected String resolveExecutable(String exec, boolean mustSearchPath) {
      if (!this.resolveExecutable) {
         return exec;
      } else {
         File executableFile = this.getProject().resolveFile(exec);
         if (executableFile.exists()) {
            return executableFile.getAbsolutePath();
         } else {
            if (this.dir != null) {
               executableFile = FILE_UTILS.resolveFile(this.dir, exec);
               if (executableFile.exists()) {
                  return executableFile.getAbsolutePath();
               }
            }

            if (mustSearchPath) {
               Path p = null;
               String[] environment = this.env.getVariables();
               if (environment != null) {
                  for(int i = 0; i < environment.length; ++i) {
                     if (this.isPath(environment[i])) {
                        p = new Path(this.getProject(), environment[i].substring(5));
                        break;
                     }
                  }
               }

               if (p == null) {
                  Vector envVars = Execute.getProcEnvironment();
                  Enumeration e = envVars.elements();

                  while(e.hasMoreElements()) {
                     String line = (String)e.nextElement();
                     if (this.isPath(line)) {
                        p = new Path(this.getProject(), line.substring(5));
                        break;
                     }
                  }
               }

               if (p != null) {
                  String[] dirs = p.list();

                  for(int i = 0; i < dirs.length; ++i) {
                     executableFile = FILE_UTILS.resolveFile(new File(dirs[i]), exec);
                     if (executableFile.exists()) {
                        return executableFile.getAbsolutePath();
                     }
                  }
               }
            }

            return exec;
         }
      }
   }

   public void execute() throws BuildException {
      if (this.isValidOs()) {
         File savedDir = this.dir;
         this.cmdl.setExecutable(this.resolveExecutable(this.executable, this.searchPath));
         this.checkConfiguration();

         try {
            this.runExec(this.prepareExec());
         } finally {
            this.dir = savedDir;
         }

      }
   }

   protected void checkConfiguration() throws BuildException {
      if (this.cmdl.getExecutable() == null) {
         throw new BuildException("no executable specified", this.getLocation());
      } else if (this.dir != null && !this.dir.exists()) {
         throw new BuildException("The directory " + this.dir + " does not exist");
      } else if (this.dir != null && !this.dir.isDirectory()) {
         throw new BuildException(this.dir + " is not a directory");
      } else if (this.spawn && this.incompatibleWithSpawn) {
         this.getProject().log("spawn does not allow attributes related to input, output, error, result", 0);
         this.getProject().log("spawn also does not allow timeout", 0);
         this.getProject().log("finally, spawn is not compatible with a nested I/O <redirector>", 0);
         throw new BuildException("You have used an attribute or nested element which is not compatible with spawn");
      } else {
         this.setupRedirector();
      }
   }

   protected void setupRedirector() {
      this.redirector.setInput(this.input);
      this.redirector.setInputString(this.inputString);
      this.redirector.setOutput(this.output);
      this.redirector.setError(this.error);
   }

   protected boolean isValidOs() {
      if (this.osFamily != null && !Os.isOs(this.osFamily, (String)null, (String)null, (String)null)) {
         return false;
      } else {
         String myos = System.getProperty("os.name");
         this.log("Current OS is " + myos, 3);
         if (this.os != null && this.os.indexOf(myos) < 0) {
            this.log("This OS, " + myos + " was not found in the specified list of valid OSes: " + this.os, 3);
            return false;
         } else {
            return true;
         }
      }
   }

   public void setVMLauncher(boolean vmLauncher) {
      this.vmLauncher = vmLauncher;
   }

   protected Execute prepareExec() throws BuildException {
      if (this.dir == null) {
         this.dir = this.getProject().getBaseDir();
      }

      if (this.redirectorElement != null) {
         this.redirectorElement.configure(this.redirector);
      }

      Execute exe = new Execute(this.createHandler(), this.createWatchdog());
      exe.setAntRun(this.getProject());
      exe.setWorkingDirectory(this.dir);
      exe.setVMLauncher(this.vmLauncher);
      exe.setSpawn(this.spawn);
      String[] environment = this.env.getVariables();
      if (environment != null) {
         for(int i = 0; i < environment.length; ++i) {
            this.log("Setting environment variable: " + environment[i], 3);
         }
      }

      exe.setNewenvironment(this.newEnvironment);
      exe.setEnvironment(environment);
      return exe;
   }

   protected final void runExecute(Execute exe) throws IOException {
      int returnCode = true;
      if (!this.spawn) {
         int returnCode = exe.execute();
         if (exe.killedProcess()) {
            String msg = "Timeout: killed the sub-process";
            if (this.failOnError) {
               throw new BuildException(msg);
            }

            this.log(msg, 1);
         }

         this.maybeSetResultPropertyValue(returnCode);
         this.redirector.complete();
         if (Execute.isFailure(returnCode)) {
            if (this.failOnError) {
               throw new BuildException(this.getTaskType() + " returned: " + returnCode, this.getLocation());
            }

            this.log("Result: " + returnCode, 0);
         }
      } else {
         exe.spawn();
      }

   }

   protected void runExec(Execute exe) throws BuildException {
      this.log(this.cmdl.describeCommand(), 3);
      exe.setCommandline(this.cmdl.getCommandline());

      try {
         this.runExecute(exe);
      } catch (IOException var6) {
         if (this.failIfExecFails) {
            throw new BuildException("Execute failed: " + var6.toString(), var6, this.getLocation());
         }

         this.log("Execute failed: " + var6.toString(), 0);
      } finally {
         this.logFlush();
      }

   }

   protected ExecuteStreamHandler createHandler() throws BuildException {
      return this.redirector.createHandler();
   }

   protected ExecuteWatchdog createWatchdog() throws BuildException {
      return this.timeout == null ? null : new ExecuteWatchdog(this.timeout);
   }

   protected void logFlush() {
   }

   private boolean isPath(String line) {
      return line.startsWith("PATH=") || line.startsWith("Path=");
   }
}

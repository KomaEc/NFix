package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ExitException;
import org.apache.tools.ant.ExitStatusException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Assertions;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Permissions;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.types.RedirectorElement;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.KeepAliveInputStream;

public class Java extends Task {
   private CommandlineJava cmdl = new CommandlineJava();
   private Environment env = new Environment();
   private boolean fork = false;
   private boolean newEnvironment = false;
   private File dir = null;
   private boolean failOnError = false;
   private Long timeout = null;
   private String inputString;
   private File input;
   private File output;
   private File error;
   protected Redirector redirector = new Redirector(this);
   protected RedirectorElement redirectorElement;
   private String resultProperty;
   private Permissions perm = null;
   private boolean spawn = false;
   private boolean incompatibleWithSpawn = false;

   public Java() {
   }

   public Java(Task owner) {
      this.bindToOwner(owner);
   }

   public void execute() throws BuildException {
      File savedDir = this.dir;
      Permissions savedPermissions = this.perm;
      boolean var3 = true;

      try {
         int err = this.executeJava();
         if (err != 0) {
            if (this.failOnError) {
               throw new ExitStatusException("Java returned: " + err, err, this.getLocation());
            }

            this.log("Java Result: " + err, 0);
         }

         this.maybeSetResultPropertyValue(err);
      } finally {
         this.dir = savedDir;
         this.perm = savedPermissions;
      }

   }

   public int executeJava() throws BuildException {
      String classname = this.getCommandLine().getClassname();
      if (classname == null && this.getCommandLine().getJar() == null) {
         throw new BuildException("Classname must not be null.");
      } else if (!this.fork && this.getCommandLine().getJar() != null) {
         throw new BuildException("Cannot execute a jar in non-forked mode. Please set fork='true'. ");
      } else if (this.spawn && !this.fork) {
         throw new BuildException("Cannot spawn a java process in non-forked mode. Please set fork='true'. ");
      } else {
         if (this.getCommandLine().getClasspath() != null && this.getCommandLine().getJar() != null) {
            this.log("When using 'jar' attribute classpath-settings are ignored. See the manual for more information.", 3);
         }

         if (this.spawn && this.incompatibleWithSpawn) {
            this.getProject().log("spawn does not allow attributes related to input, output, error, result", 0);
            this.getProject().log("spawn also does not allow timeout", 0);
            this.getProject().log("finally, spawn is not compatible with a nested I/O <redirector>", 0);
            throw new BuildException("You have used an attribute or nested element which is not compatible with spawn");
         } else {
            if (this.getCommandLine().getAssertions() != null && !this.fork) {
               this.log("Assertion statements are currently ignored in non-forked mode");
            }

            if (this.fork) {
               if (this.perm != null) {
                  this.log("Permissions can not be set this way in forked mode.", 1);
               }

               this.log(this.getCommandLine().describeCommand(), 3);
            } else {
               if (this.getCommandLine().getVmCommand().size() > 1) {
                  this.log("JVM args ignored when same JVM is used.", 1);
               }

               if (this.dir != null) {
                  this.log("Working directory ignored when same JVM is used.", 1);
               }

               if (this.newEnvironment || null != this.env.getVariables()) {
                  this.log("Changes to environment variables are ignored when same JVM is used.", 1);
               }

               if (this.getCommandLine().getBootclasspath() != null) {
                  this.log("bootclasspath ignored when same JVM is used.", 1);
               }

               if (this.perm == null) {
                  this.perm = new Permissions(true);
                  this.log("running " + this.getCommandLine().getClassname() + " with default permissions (exit forbidden)", 3);
               }

               this.log("Running in same VM " + this.getCommandLine().describeJavaCommand(), 3);
            }

            this.setupRedirector();

            try {
               if (this.fork) {
                  if (!this.spawn) {
                     return this.fork(this.getCommandLine().getCommandline());
                  } else {
                     this.spawn(this.getCommandLine().getCommandline());
                     return 0;
                  }
               } else {
                  try {
                     this.run(this.getCommandLine());
                     return 0;
                  } catch (ExitException var3) {
                     return var3.getStatus();
                  }
               }
            } catch (BuildException var4) {
               if (var4.getLocation() == null && this.getLocation() != null) {
                  var4.setLocation(this.getLocation());
               }

               if (this.failOnError) {
                  throw var4;
               } else {
                  this.log(var4);
                  return 0;
               }
            } catch (ThreadDeath var5) {
               throw var5;
            } catch (Throwable var6) {
               if (this.failOnError) {
                  throw new BuildException(var6, this.getLocation());
               } else {
                  this.log(var6);
                  return 0;
               }
            }
         }
      }
   }

   public void setSpawn(boolean spawn) {
      this.spawn = spawn;
   }

   public void setClasspath(Path s) {
      this.createClasspath().append(s);
   }

   public Path createClasspath() {
      return this.getCommandLine().createClasspath(this.getProject()).createPath();
   }

   public Path createBootclasspath() {
      return this.getCommandLine().createBootclasspath(this.getProject()).createPath();
   }

   public Permissions createPermissions() {
      this.perm = this.perm == null ? new Permissions() : this.perm;
      return this.perm;
   }

   public void setClasspathRef(Reference r) {
      this.createClasspath().setRefid(r);
   }

   public void setJar(File jarfile) throws BuildException {
      if (this.getCommandLine().getClassname() != null) {
         throw new BuildException("Cannot use 'jar' and 'classname' attributes in same command.");
      } else {
         this.getCommandLine().setJar(jarfile.getAbsolutePath());
      }
   }

   public void setClassname(String s) throws BuildException {
      if (this.getCommandLine().getJar() != null) {
         throw new BuildException("Cannot use 'jar' and 'classname' attributes in same command");
      } else {
         this.getCommandLine().setClassname(s);
      }
   }

   public void setArgs(String s) {
      this.log("The args attribute is deprecated. Please use nested arg elements.", 1);
      this.getCommandLine().createArgument().setLine(s);
   }

   public void setCloneVm(boolean cloneVm) {
      this.getCommandLine().setCloneVm(cloneVm);
   }

   public Commandline.Argument createArg() {
      return this.getCommandLine().createArgument();
   }

   public void setResultProperty(String resultProperty) {
      this.resultProperty = resultProperty;
      this.incompatibleWithSpawn = true;
   }

   protected void maybeSetResultPropertyValue(int result) {
      String res = Integer.toString(result);
      if (this.resultProperty != null) {
         this.getProject().setNewProperty(this.resultProperty, res);
      }

   }

   public void setFork(boolean s) {
      this.fork = s;
   }

   public void setJvmargs(String s) {
      this.log("The jvmargs attribute is deprecated. Please use nested jvmarg elements.", 1);
      this.getCommandLine().createVmArgument().setLine(s);
   }

   public Commandline.Argument createJvmarg() {
      return this.getCommandLine().createVmArgument();
   }

   public void setJvm(String s) {
      this.getCommandLine().setVm(s);
   }

   public void addSysproperty(Environment.Variable sysp) {
      this.getCommandLine().addSysproperty(sysp);
   }

   public void addSyspropertyset(PropertySet sysp) {
      this.getCommandLine().addSyspropertyset(sysp);
   }

   public void setFailonerror(boolean fail) {
      this.failOnError = fail;
      this.incompatibleWithSpawn |= fail;
   }

   public void setDir(File d) {
      this.dir = d;
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

   public void setMaxmemory(String max) {
      this.getCommandLine().setMaxmemory(max);
   }

   public void setJVMVersion(String value) {
      this.getCommandLine().setVmversion(value);
   }

   public void addEnv(Environment.Variable var) {
      this.env.addVariable(var);
   }

   public void setNewenvironment(boolean newenv) {
      this.newEnvironment = newenv;
   }

   public void setAppend(boolean append) {
      this.redirector.setAppend(append);
      this.incompatibleWithSpawn = true;
   }

   public void setTimeout(Long value) {
      this.timeout = value;
      this.incompatibleWithSpawn |= this.timeout != null;
   }

   public void addAssertions(Assertions asserts) {
      if (this.getCommandLine().getAssertions() != null) {
         throw new BuildException("Only one assertion declaration is allowed");
      } else {
         this.getCommandLine().setAssertions(asserts);
      }
   }

   public void addConfiguredRedirector(RedirectorElement redirectorElement) {
      if (this.redirectorElement != null) {
         throw new BuildException("cannot have > 1 nested redirectors");
      } else {
         this.redirectorElement = redirectorElement;
         this.incompatibleWithSpawn = true;
      }
   }

   protected void handleOutput(String output) {
      if (this.redirector.getOutputStream() != null) {
         this.redirector.handleOutput(output);
      } else {
         super.handleOutput(output);
      }

   }

   public int handleInput(byte[] buffer, int offset, int length) throws IOException {
      return this.redirector.handleInput(buffer, offset, length);
   }

   protected void handleFlush(String output) {
      if (this.redirector.getOutputStream() != null) {
         this.redirector.handleFlush(output);
      } else {
         super.handleFlush(output);
      }

   }

   protected void handleErrorOutput(String output) {
      if (this.redirector.getErrorStream() != null) {
         this.redirector.handleErrorOutput(output);
      } else {
         super.handleErrorOutput(output);
      }

   }

   protected void handleErrorFlush(String output) {
      if (this.redirector.getErrorStream() != null) {
         this.redirector.handleErrorFlush(output);
      } else {
         super.handleErrorOutput(output);
      }

   }

   protected void setupRedirector() {
      this.redirector.setInput(this.input);
      this.redirector.setInputString(this.inputString);
      this.redirector.setOutput(this.output);
      this.redirector.setError(this.error);
      if (this.redirectorElement != null) {
         this.redirectorElement.configure(this.redirector);
      }

      if (!this.spawn && this.input == null && this.inputString == null) {
         this.redirector.setInputStream(new KeepAliveInputStream(this.getProject().getDefaultInputStream()));
      }

   }

   private void run(CommandlineJava command) throws BuildException {
      try {
         ExecuteJava exe = new ExecuteJava();
         exe.setJavaCommand(command.getJavaCommand());
         exe.setClasspath(command.getClasspath());
         exe.setSystemProperties(command.getSystemProperties());
         exe.setPermissions(this.perm);
         exe.setTimeout(this.timeout);
         this.redirector.createStreams();
         exe.execute(this.getProject());
         this.redirector.complete();
         if (exe.killedProcess()) {
            throw new BuildException("Timeout: killed the sub-process");
         }
      } catch (IOException var3) {
         throw new BuildException(var3);
      }
   }

   private int fork(String[] command) throws BuildException {
      Execute exe = new Execute(this.redirector.createHandler(), this.createWatchdog());
      this.setupExecutable(exe, command);

      try {
         int rc = exe.execute();
         this.redirector.complete();
         if (exe.killedProcess()) {
            throw new BuildException("Timeout: killed the sub-process");
         } else {
            return rc;
         }
      } catch (IOException var4) {
         throw new BuildException(var4, this.getLocation());
      }
   }

   private void spawn(String[] command) throws BuildException {
      Execute exe = new Execute();
      this.setupExecutable(exe, command);

      try {
         exe.spawn();
      } catch (IOException var4) {
         throw new BuildException(var4, this.getLocation());
      }
   }

   private void setupExecutable(Execute exe, String[] command) {
      exe.setAntRun(this.getProject());
      this.setupWorkingDir(exe);
      this.setupEnvironment(exe);
      this.setupCommandLine(exe, command);
   }

   private void setupEnvironment(Execute exe) {
      String[] environment = this.env.getVariables();
      if (environment != null) {
         for(int i = 0; i < environment.length; ++i) {
            this.log("Setting environment variable: " + environment[i], 3);
         }
      }

      exe.setNewenvironment(this.newEnvironment);
      exe.setEnvironment(environment);
   }

   private void setupWorkingDir(Execute exe) {
      if (this.dir == null) {
         this.dir = this.getProject().getBaseDir();
      } else if (!this.dir.exists() || !this.dir.isDirectory()) {
         throw new BuildException(this.dir.getAbsolutePath() + " is not a valid directory", this.getLocation());
      }

      exe.setWorkingDirectory(this.dir);
   }

   private void setupCommandLine(Execute exe, String[] command) {
      if (Os.isFamily("openvms")) {
         this.setupCommandLineForVMS(exe, command);
      } else {
         exe.setCommandline(command);
      }

   }

   private void setupCommandLineForVMS(Execute exe, String[] command) {
      ExecuteJava.setupCommandLineForVMS(exe, command);
   }

   protected void run(String classname, Vector args) throws BuildException {
      CommandlineJava cmdj = new CommandlineJava();
      cmdj.setClassname(classname);

      for(int i = 0; i < args.size(); ++i) {
         cmdj.createArgument().setValue((String)args.elementAt(i));
      }

      this.run(cmdj);
   }

   public void clearArgs() {
      this.getCommandLine().clearJavaArgs();
   }

   protected ExecuteWatchdog createWatchdog() throws BuildException {
      return this.timeout == null ? null : new ExecuteWatchdog(this.timeout);
   }

   private void log(Throwable t) {
      StringWriter sw = new StringWriter();
      PrintWriter w = new PrintWriter(sw);
      t.printStackTrace(w);
      w.close();
      this.log(sw.toString(), 0);
   }

   public CommandlineJava getCommandLine() {
      return this.cmdl;
   }

   public CommandlineJava.SysProperties getSysProperties() {
      return this.getCommandLine().getSystemProperties();
   }
}

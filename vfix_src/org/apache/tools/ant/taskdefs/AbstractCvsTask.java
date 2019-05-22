package org.apache.tools.ant.taskdefs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.StringUtils;

public abstract class AbstractCvsTask extends Task {
   public static final int DEFAULT_COMPRESSION_LEVEL = 3;
   private static final int MAXIMUM_COMRESSION_LEVEL = 9;
   private Commandline cmd = new Commandline();
   private Vector vecCommandlines = new Vector();
   private String cvsRoot;
   private String cvsRsh;
   private String cvsPackage;
   private String tag;
   private static final String DEFAULT_COMMAND = "checkout";
   private String command = null;
   private boolean quiet = false;
   private boolean reallyquiet = false;
   private int compression = 0;
   private boolean noexec = false;
   private int port = 0;
   private File passFile = null;
   private File dest;
   private boolean append = false;
   private File output;
   private File error;
   private boolean failOnError = false;
   private ExecuteStreamHandler executeStreamHandler;
   private OutputStream outputStream;
   private OutputStream errorStream;

   public void setExecuteStreamHandler(ExecuteStreamHandler handler) {
      this.executeStreamHandler = handler;
   }

   protected ExecuteStreamHandler getExecuteStreamHandler() {
      if (this.executeStreamHandler == null) {
         this.setExecuteStreamHandler(new PumpStreamHandler(this.getOutputStream(), this.getErrorStream()));
      }

      return this.executeStreamHandler;
   }

   protected void setOutputStream(OutputStream outputStream) {
      this.outputStream = outputStream;
   }

   protected OutputStream getOutputStream() {
      if (this.outputStream == null) {
         if (this.output != null) {
            try {
               this.setOutputStream(new PrintStream(new BufferedOutputStream(new FileOutputStream(this.output.getPath(), this.append))));
            } catch (IOException var2) {
               throw new BuildException(var2, this.getLocation());
            }
         } else {
            this.setOutputStream(new LogOutputStream(this, 2));
         }
      }

      return this.outputStream;
   }

   protected void setErrorStream(OutputStream errorStream) {
      this.errorStream = errorStream;
   }

   protected OutputStream getErrorStream() {
      if (this.errorStream == null) {
         if (this.error != null) {
            try {
               this.setErrorStream(new PrintStream(new BufferedOutputStream(new FileOutputStream(this.error.getPath(), this.append))));
            } catch (IOException var2) {
               throw new BuildException(var2, this.getLocation());
            }
         } else {
            this.setErrorStream(new LogOutputStream(this, 1));
         }
      }

      return this.errorStream;
   }

   protected void runCommand(Commandline toExecute) throws BuildException {
      Environment env = new Environment();
      Environment.Variable var;
      if (this.port > 0) {
         var = new Environment.Variable();
         var.setKey("CVS_CLIENT_PORT");
         var.setValue(String.valueOf(this.port));
         env.addVariable(var);
      }

      if (this.passFile == null) {
         File defaultPassFile = new File(System.getProperty("cygwin.user.home", System.getProperty("user.home")) + File.separatorChar + ".cvspass");
         if (defaultPassFile.exists()) {
            this.setPassfile(defaultPassFile);
         }
      }

      if (this.passFile != null) {
         if (this.passFile.isFile() && this.passFile.canRead()) {
            var = new Environment.Variable();
            var.setKey("CVS_PASSFILE");
            var.setValue(String.valueOf(this.passFile));
            env.addVariable(var);
            this.log("Using cvs passfile: " + String.valueOf(this.passFile), 3);
         } else if (!this.passFile.canRead()) {
            this.log("cvs passfile: " + String.valueOf(this.passFile) + " ignored as it is not readable", 1);
         } else {
            this.log("cvs passfile: " + String.valueOf(this.passFile) + " ignored as it is not a file", 1);
         }
      }

      if (this.cvsRsh != null) {
         var = new Environment.Variable();
         var.setKey("CVS_RSH");
         var.setValue(String.valueOf(this.cvsRsh));
         env.addVariable(var);
      }

      Execute exe = new Execute(this.getExecuteStreamHandler(), (ExecuteWatchdog)null);
      exe.setAntRun(this.getProject());
      if (this.dest == null) {
         this.dest = this.getProject().getBaseDir();
      }

      if (!this.dest.exists()) {
         this.dest.mkdirs();
      }

      exe.setWorkingDirectory(this.dest);
      exe.setCommandline(toExecute.getCommandline());
      exe.setEnvironment(env.getVariables());

      try {
         String actualCommandLine = this.executeToString(exe);
         this.log(actualCommandLine, 3);
         int retCode = exe.execute();
         this.log("retCode=" + retCode, 4);
         if (this.failOnError && Execute.isFailure(retCode)) {
            throw new BuildException("cvs exited with error code " + retCode + StringUtils.LINE_SEP + "Command line was [" + actualCommandLine + "]", this.getLocation());
         }
      } catch (IOException var6) {
         if (this.failOnError) {
            throw new BuildException(var6, this.getLocation());
         }

         this.log("Caught exception: " + var6.getMessage(), 1);
      } catch (BuildException var7) {
         if (this.failOnError) {
            throw var7;
         }

         Throwable t = var7.getException();
         if (t == null) {
            t = var7;
         }

         this.log("Caught exception: " + ((Throwable)t).getMessage(), 1);
      } catch (Exception var8) {
         if (this.failOnError) {
            throw new BuildException(var8, this.getLocation());
         }

         this.log("Caught exception: " + var8.getMessage(), 1);
      }

   }

   public void execute() throws BuildException {
      String savedCommand = this.getCommand();
      if (this.getCommand() == null && this.vecCommandlines.size() == 0) {
         this.setCommand("checkout");
      }

      String c = this.getCommand();
      Commandline cloned = null;
      if (c != null) {
         cloned = (Commandline)this.cmd.clone();
         cloned.createArgument(true).setLine(c);
         this.addConfiguredCommandline(cloned, true);
      }

      try {
         for(int i = 0; i < this.vecCommandlines.size(); ++i) {
            this.runCommand((Commandline)this.vecCommandlines.elementAt(i));
         }
      } finally {
         if (cloned != null) {
            this.removeCommandline(cloned);
         }

         this.setCommand(savedCommand);
         FileUtils.close(this.outputStream);
         FileUtils.close(this.errorStream);
      }

   }

   private String executeToString(Execute execute) {
      StringBuffer stringBuffer = new StringBuffer(Commandline.describeCommand(execute.getCommandline()));
      String newLine = StringUtils.LINE_SEP;
      String[] variableArray = execute.getEnvironment();
      if (variableArray != null) {
         stringBuffer.append(newLine);
         stringBuffer.append(newLine);
         stringBuffer.append("environment:");
         stringBuffer.append(newLine);

         for(int z = 0; z < variableArray.length; ++z) {
            stringBuffer.append(newLine);
            stringBuffer.append("\t");
            stringBuffer.append(variableArray[z]);
         }
      }

      return stringBuffer.toString();
   }

   public void setCvsRoot(String root) {
      if (root != null && root.trim().equals("")) {
         root = null;
      }

      this.cvsRoot = root;
   }

   public String getCvsRoot() {
      return this.cvsRoot;
   }

   public void setCvsRsh(String rsh) {
      if (rsh != null && rsh.trim().equals("")) {
         rsh = null;
      }

      this.cvsRsh = rsh;
   }

   public String getCvsRsh() {
      return this.cvsRsh;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public int getPort() {
      return this.port;
   }

   public void setPassfile(File passFile) {
      this.passFile = passFile;
   }

   public File getPassFile() {
      return this.passFile;
   }

   public void setDest(File dest) {
      this.dest = dest;
   }

   public File getDest() {
      return this.dest;
   }

   public void setPackage(String p) {
      this.cvsPackage = p;
   }

   public String getPackage() {
      return this.cvsPackage;
   }

   public String getTag() {
      return this.tag;
   }

   public void setTag(String p) {
      if (p != null && p.trim().length() > 0) {
         this.tag = p;
         this.addCommandArgument("-r" + p);
      }

   }

   public void addCommandArgument(String arg) {
      this.addCommandArgument(this.cmd, arg);
   }

   public void addCommandArgument(Commandline c, String arg) {
      c.createArgument().setValue(arg);
   }

   public void setDate(String p) {
      if (p != null && p.trim().length() > 0) {
         this.addCommandArgument("-D");
         this.addCommandArgument(p);
      }

   }

   public void setCommand(String c) {
      this.command = c;
   }

   public String getCommand() {
      return this.command;
   }

   public void setQuiet(boolean q) {
      this.quiet = q;
   }

   public void setReallyquiet(boolean q) {
      this.reallyquiet = q;
   }

   public void setNoexec(boolean ne) {
      this.noexec = ne;
   }

   public void setOutput(File output) {
      this.output = output;
   }

   public void setError(File error) {
      this.error = error;
   }

   public void setAppend(boolean value) {
      this.append = value;
   }

   public void setFailOnError(boolean failOnError) {
      this.failOnError = failOnError;
   }

   protected void configureCommandline(Commandline c) {
      if (c != null) {
         c.setExecutable("cvs");
         if (this.cvsPackage != null) {
            c.createArgument().setLine(this.cvsPackage);
         }

         if (this.compression > 0 && this.compression <= 9) {
            c.createArgument(true).setValue("-z" + this.compression);
         }

         if (this.quiet && !this.reallyquiet) {
            c.createArgument(true).setValue("-q");
         }

         if (this.reallyquiet) {
            c.createArgument(true).setValue("-Q");
         }

         if (this.noexec) {
            c.createArgument(true).setValue("-n");
         }

         if (this.cvsRoot != null) {
            c.createArgument(true).setLine("-d" + this.cvsRoot);
         }

      }
   }

   protected void removeCommandline(Commandline c) {
      this.vecCommandlines.removeElement(c);
   }

   public void addConfiguredCommandline(Commandline c) {
      this.addConfiguredCommandline(c, false);
   }

   public void addConfiguredCommandline(Commandline c, boolean insertAtStart) {
      if (c != null) {
         this.configureCommandline(c);
         if (insertAtStart) {
            this.vecCommandlines.insertElementAt(c, 0);
         } else {
            this.vecCommandlines.addElement(c);
         }

      }
   }

   public void setCompressionLevel(int level) {
      this.compression = level;
   }

   public void setCompression(boolean usecomp) {
      this.setCompressionLevel(usecomp ? 3 : 0);
   }
}

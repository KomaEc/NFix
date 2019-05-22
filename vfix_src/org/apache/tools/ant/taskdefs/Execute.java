package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.StringUtils;

public class Execute {
   public static final int INVALID = Integer.MAX_VALUE;
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private String[] cmdl;
   private String[] env;
   private int exitValue;
   private ExecuteStreamHandler streamHandler;
   private ExecuteWatchdog watchdog;
   private File workingDirectory;
   private Project project;
   private boolean newEnvironment;
   private boolean spawn;
   private boolean useVMLauncher;
   private static String antWorkingDirectory = System.getProperty("user.dir");
   private static Execute.CommandLauncher vmLauncher = null;
   private static Execute.CommandLauncher shellLauncher = null;
   private static Vector procEnvironment = null;
   private static ProcessDestroyer processDestroyer = new ProcessDestroyer();
   private static boolean environmentCaseInSensitive = false;
   // $FF: synthetic field
   static Class array$Ljava$lang$String;
   // $FF: synthetic field
   static Class class$java$io$File;
   // $FF: synthetic field
   static Class class$java$lang$Runtime;

   public void setSpawn(boolean spawn) {
      this.spawn = spawn;
   }

   public static synchronized Vector getProcEnvironment() {
      if (procEnvironment != null) {
         return procEnvironment;
      } else {
         procEnvironment = new Vector();

         try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Execute exe = new Execute(new PumpStreamHandler(out));
            exe.setCommandline(getProcEnvCommand());
            exe.setNewenvironment(true);
            int retval = exe.execute();
            if (retval != 0) {
            }

            BufferedReader in = new BufferedReader(new StringReader(toString(out)));
            if (Os.isFamily("openvms")) {
               procEnvironment = addVMSLogicals(procEnvironment, in);
               return procEnvironment;
            }

            String var = null;
            String lineSep = StringUtils.LINE_SEP;

            String line;
            while((line = in.readLine()) != null) {
               if (line.indexOf(61) == -1) {
                  if (var == null) {
                     var = lineSep + line;
                  } else {
                     var = var + lineSep + line;
                  }
               } else {
                  if (var != null) {
                     procEnvironment.addElement(var);
                  }

                  var = line;
               }
            }

            if (var != null) {
               procEnvironment.addElement(var);
            }
         } catch (IOException var7) {
            var7.printStackTrace();
         }

         return procEnvironment;
      }
   }

   private static String[] getProcEnvCommand() {
      if (Os.isFamily("os/2")) {
         return new String[]{"cmd", "/c", "set"};
      } else if (Os.isFamily("windows")) {
         return Os.isFamily("win9x") ? new String[]{"command.com", "/c", "set"} : new String[]{"cmd", "/c", "set"};
      } else if (!Os.isFamily("z/os") && !Os.isFamily("unix")) {
         if (!Os.isFamily("netware") && !Os.isFamily("os/400")) {
            return Os.isFamily("openvms") ? new String[]{"show", "logical"} : null;
         } else {
            return new String[]{"env"};
         }
      } else {
         String[] cmd = new String[1];
         if ((new File("/bin/env")).canRead()) {
            cmd[0] = "/bin/env";
         } else if ((new File("/usr/bin/env")).canRead()) {
            cmd[0] = "/usr/bin/env";
         } else {
            cmd[0] = "env";
         }

         return cmd;
      }
   }

   public static String toString(ByteArrayOutputStream bos) {
      if (Os.isFamily("z/os")) {
         try {
            return bos.toString("Cp1047");
         } catch (UnsupportedEncodingException var3) {
         }
      } else if (Os.isFamily("os/400")) {
         try {
            return bos.toString("Cp500");
         } catch (UnsupportedEncodingException var2) {
         }
      }

      return bos.toString();
   }

   public Execute() {
      this(new PumpStreamHandler(), (ExecuteWatchdog)null);
   }

   public Execute(ExecuteStreamHandler streamHandler) {
      this(streamHandler, (ExecuteWatchdog)null);
   }

   public Execute(ExecuteStreamHandler streamHandler, ExecuteWatchdog watchdog) {
      this.cmdl = null;
      this.env = null;
      this.exitValue = Integer.MAX_VALUE;
      this.workingDirectory = null;
      this.project = null;
      this.newEnvironment = false;
      this.spawn = false;
      this.useVMLauncher = true;
      this.setStreamHandler(streamHandler);
      this.watchdog = watchdog;
      if (Os.isFamily("openvms")) {
         this.useVMLauncher = false;
      }

   }

   public void setStreamHandler(ExecuteStreamHandler streamHandler) {
      this.streamHandler = streamHandler;
   }

   public String[] getCommandline() {
      return this.cmdl;
   }

   public void setCommandline(String[] commandline) {
      this.cmdl = commandline;
   }

   public void setNewenvironment(boolean newenv) {
      this.newEnvironment = newenv;
   }

   public String[] getEnvironment() {
      return this.env != null && !this.newEnvironment ? this.patchEnvironment() : this.env;
   }

   public void setEnvironment(String[] env) {
      this.env = env;
   }

   public void setWorkingDirectory(File wd) {
      this.workingDirectory = wd != null && !wd.getAbsolutePath().equals(antWorkingDirectory) ? wd : null;
   }

   public File getWorkingDirectory() {
      return this.workingDirectory == null ? new File(antWorkingDirectory) : this.workingDirectory;
   }

   public void setAntRun(Project project) throws BuildException {
      this.project = project;
   }

   public void setVMLauncher(boolean useVMLauncher) {
      this.useVMLauncher = useVMLauncher;
   }

   public static Process launch(Project project, String[] command, String[] env, File dir, boolean useVM) throws IOException {
      if (dir != null && !dir.exists()) {
         throw new BuildException(dir + " doesn't exist.");
      } else {
         Execute.CommandLauncher launcher = useVM && vmLauncher != null ? vmLauncher : shellLauncher;
         return launcher.exec(project, command, env, dir);
      }
   }

   public int execute() throws IOException {
      if (this.workingDirectory != null && !this.workingDirectory.exists()) {
         throw new BuildException(this.workingDirectory + " doesn't exist.");
      } else {
         Process process = launch(this.project, this.getCommandline(), this.getEnvironment(), this.workingDirectory, this.useVMLauncher);

         try {
            this.streamHandler.setProcessInputStream(process.getOutputStream());
            this.streamHandler.setProcessOutputStream(process.getInputStream());
            this.streamHandler.setProcessErrorStream(process.getErrorStream());
         } catch (IOException var7) {
            process.destroy();
            throw var7;
         }

         this.streamHandler.start();

         int var2;
         try {
            processDestroyer.add(process);
            if (this.watchdog != null) {
               this.watchdog.start(process);
            }

            this.waitFor(process);
            if (this.watchdog != null) {
               this.watchdog.stop();
            }

            this.streamHandler.stop();
            closeStreams(process);
            if (this.watchdog != null) {
               this.watchdog.checkException();
            }

            var2 = this.getExitValue();
         } catch (ThreadDeath var8) {
            process.destroy();
            throw var8;
         } finally {
            processDestroyer.remove(process);
         }

         return var2;
      }
   }

   public void spawn() throws IOException {
      if (this.workingDirectory != null && !this.workingDirectory.exists()) {
         throw new BuildException(this.workingDirectory + " doesn't exist.");
      } else {
         Process process = launch(this.project, this.getCommandline(), this.getEnvironment(), this.workingDirectory, this.useVMLauncher);
         if (Os.isFamily("windows")) {
            try {
               Thread.sleep(1000L);
            } catch (InterruptedException var4) {
               this.project.log("interruption in the sleep after having spawned a process", 3);
            }
         }

         OutputStream dummyOut = new OutputStream() {
            public void write(int b) throws IOException {
            }
         };
         ExecuteStreamHandler handler = new PumpStreamHandler(dummyOut);
         handler.setProcessErrorStream(process.getErrorStream());
         handler.setProcessOutputStream(process.getInputStream());
         handler.start();
         process.getOutputStream().close();
         this.project.log("spawned process " + process.toString(), 3);
      }
   }

   protected void waitFor(Process process) {
      try {
         process.waitFor();
         this.setExitValue(process.exitValue());
      } catch (InterruptedException var3) {
         process.destroy();
      }

   }

   protected void setExitValue(int value) {
      this.exitValue = value;
   }

   public int getExitValue() {
      return this.exitValue;
   }

   public static boolean isFailure(int exitValue) {
      return Os.isFamily("openvms") ? exitValue % 2 == 0 : exitValue != 0;
   }

   public boolean isFailure() {
      return isFailure(this.getExitValue());
   }

   public boolean killedProcess() {
      return this.watchdog != null && this.watchdog.killedProcess();
   }

   private String[] patchEnvironment() {
      if (Os.isFamily("openvms")) {
         return this.env;
      } else {
         Vector osEnv = (Vector)getProcEnvironment().clone();

         for(int i = 0; i < this.env.length; ++i) {
            String keyValue = this.env[i];
            String key = keyValue.substring(0, keyValue.indexOf(61) + 1);
            if (environmentCaseInSensitive) {
               key = key.toLowerCase();
            }

            int size = osEnv.size();

            for(int j = 0; j < size; ++j) {
               String osEnvItem = (String)osEnv.elementAt(j);
               String convertedItem = environmentCaseInSensitive ? osEnvItem.toLowerCase() : osEnvItem;
               if (convertedItem.startsWith(key)) {
                  osEnv.removeElementAt(j);
                  if (environmentCaseInSensitive) {
                     keyValue = osEnvItem.substring(0, key.length()) + keyValue.substring(key.length());
                  }
                  break;
               }
            }

            osEnv.addElement(keyValue);
         }

         return (String[])((String[])osEnv.toArray(new String[osEnv.size()]));
      }
   }

   public static void runCommand(Task task, String[] cmdline) throws BuildException {
      try {
         task.log((String)Commandline.describeCommand(cmdline), 3);
         Execute exe = new Execute(new LogStreamHandler(task, 2, 0));
         exe.setAntRun(task.getProject());
         exe.setCommandline(cmdline);
         int retval = exe.execute();
         if (isFailure(retval)) {
            throw new BuildException(cmdline[0] + " failed with return code " + retval, task.getLocation());
         }
      } catch (IOException var4) {
         throw new BuildException("Could not launch " + cmdline[0] + ": " + var4, task.getLocation());
      }
   }

   public static void closeStreams(Process process) {
      FileUtils.close(process.getInputStream());
      FileUtils.close(process.getOutputStream());
      FileUtils.close(process.getErrorStream());
   }

   private static Vector addVMSLogicals(Vector environment, BufferedReader in) throws IOException {
      HashMap logicals = new HashMap();
      String logName = null;
      String logValue = null;
      String line = null;

      while((line = in.readLine()) != null) {
         if (line.startsWith("\t=")) {
            if (logName != null) {
               logValue = logValue + "," + line.substring(4, line.length() - 1);
            }
         } else if (line.startsWith("  \"")) {
            if (logName != null) {
               logicals.put(logName, logValue);
            }

            int eqIndex = line.indexOf(61);
            String newLogName = line.substring(3, eqIndex - 2);
            if (logicals.containsKey(newLogName)) {
               logName = null;
            } else {
               logName = newLogName;
               logValue = line.substring(eqIndex + 3, line.length() - 1);
            }
         }
      }

      if (logName != null) {
         logicals.put(logName, logValue);
      }

      Iterator i = logicals.keySet().iterator();

      while(i.hasNext()) {
         String logical = (String)i.next();
         environment.add(logical + "=" + logicals.get(logical));
      }

      return environment;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      try {
         if (!Os.isFamily("os/2")) {
            vmLauncher = new Execute.Java13CommandLauncher();
         }
      } catch (NoSuchMethodException var2) {
      }

      if (Os.isFamily("mac") && !Os.isFamily("unix")) {
         shellLauncher = new Execute.MacCommandLauncher(new Execute.CommandLauncher());
      } else if (Os.isFamily("os/2")) {
         shellLauncher = new Execute.OS2CommandLauncher(new Execute.CommandLauncher());
      } else {
         Execute.CommandLauncher baseLauncher;
         if (Os.isFamily("windows")) {
            environmentCaseInSensitive = true;
            baseLauncher = new Execute.CommandLauncher();
            if (!Os.isFamily("win9x")) {
               shellLauncher = new Execute.WinNTCommandLauncher(baseLauncher);
            } else {
               shellLauncher = new Execute.ScriptCommandLauncher("bin/antRun.bat", baseLauncher);
            }
         } else if (Os.isFamily("netware")) {
            baseLauncher = new Execute.CommandLauncher();
            shellLauncher = new Execute.PerlScriptCommandLauncher("bin/antRun.pl", baseLauncher);
         } else if (Os.isFamily("openvms")) {
            try {
               shellLauncher = new Execute.VmsCommandLauncher();
            } catch (NoSuchMethodException var1) {
            }
         } else {
            shellLauncher = new Execute.ScriptCommandLauncher("bin/antRun", new Execute.CommandLauncher());
         }
      }

   }

   private static class VmsCommandLauncher extends Execute.Java13CommandLauncher {
      public VmsCommandLauncher() throws NoSuchMethodException {
      }

      public Process exec(Project project, String[] cmd, String[] env) throws IOException {
         File cmdFile = this.createCommandFile(cmd, env);
         Process p = super.exec(project, new String[]{cmdFile.getPath()}, env);
         this.deleteAfter(cmdFile, p);
         return p;
      }

      public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
         File cmdFile = this.createCommandFile(cmd, env);
         Process p = super.exec(project, new String[]{cmdFile.getPath()}, env, workingDir);
         this.deleteAfter(cmdFile, p);
         return p;
      }

      private File createCommandFile(String[] cmd, String[] env) throws IOException {
         File script = Execute.FILE_UTILS.createTempFile("ANT", ".COM", (File)null);
         script.deleteOnExit();
         PrintWriter out = null;

         try {
            out = new PrintWriter(new FileWriter(script));
            int eqIndex;
            if (env != null) {
               for(int i = 0; i < env.length; ++i) {
                  eqIndex = env[i].indexOf(61);
                  if (eqIndex != -1) {
                     out.print("$ DEFINE/NOLOG ");
                     out.print(env[i].substring(0, eqIndex));
                     out.print(" \"");
                     out.print(env[i].substring(eqIndex + 1));
                     out.println('"');
                  }
               }
            }

            out.print("$ " + cmd[0]);

            for(eqIndex = 1; eqIndex < cmd.length; ++eqIndex) {
               out.println(" -");
               out.print(cmd[eqIndex]);
            }
         } finally {
            if (out != null) {
               out.close();
            }

         }

         return script;
      }

      private void deleteAfter(final File f, final Process p) {
         (new Thread() {
            public void run() {
               try {
                  p.waitFor();
               } catch (InterruptedException var2) {
               }

               FileUtils.delete(f);
            }
         }).start();
      }
   }

   private static class PerlScriptCommandLauncher extends Execute.CommandLauncherProxy {
      private String myScript;

      PerlScriptCommandLauncher(String script, Execute.CommandLauncher launcher) {
         super(launcher);
         this.myScript = script;
      }

      public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
         if (project == null) {
            if (workingDir == null) {
               return this.exec(project, cmd, env);
            } else {
               throw new IOException("Cannot locate antRun script: No project provided");
            }
         } else {
            String antHome = project.getProperty("ant.home");
            if (antHome == null) {
               throw new IOException("Cannot locate antRun script: Property 'ant.home' not found");
            } else {
               String antRun = Execute.FILE_UTILS.resolveFile(project.getBaseDir(), antHome + File.separator + this.myScript).toString();
               File commandDir = workingDir;
               if (workingDir == null && project != null) {
                  commandDir = project.getBaseDir();
               }

               String[] newcmd = new String[cmd.length + 3];
               newcmd[0] = "perl";
               newcmd[1] = antRun;
               newcmd[2] = commandDir.getAbsolutePath();
               System.arraycopy(cmd, 0, newcmd, 3, cmd.length);
               return this.exec(project, newcmd, env);
            }
         }
      }
   }

   private static class ScriptCommandLauncher extends Execute.CommandLauncherProxy {
      private String myScript;

      ScriptCommandLauncher(String script, Execute.CommandLauncher launcher) {
         super(launcher);
         this.myScript = script;
      }

      public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
         if (project == null) {
            if (workingDir == null) {
               return this.exec(project, cmd, env);
            } else {
               throw new IOException("Cannot locate antRun script: No project provided");
            }
         } else {
            String antHome = project.getProperty("ant.home");
            if (antHome == null) {
               throw new IOException("Cannot locate antRun script: Property 'ant.home' not found");
            } else {
               String antRun = Execute.FILE_UTILS.resolveFile(project.getBaseDir(), antHome + File.separator + this.myScript).toString();
               File commandDir = workingDir;
               if (workingDir == null && project != null) {
                  commandDir = project.getBaseDir();
               }

               String[] newcmd = new String[cmd.length + 2];
               newcmd[0] = antRun;
               newcmd[1] = commandDir.getAbsolutePath();
               System.arraycopy(cmd, 0, newcmd, 2, cmd.length);
               return this.exec(project, newcmd, env);
            }
         }
      }
   }

   private static class MacCommandLauncher extends Execute.CommandLauncherProxy {
      MacCommandLauncher(Execute.CommandLauncher launcher) {
         super(launcher);
      }

      public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
         if (workingDir == null) {
            return this.exec(project, cmd, env);
         } else {
            System.getProperties().put("user.dir", workingDir.getAbsolutePath());

            Process var5;
            try {
               var5 = this.exec(project, cmd, env);
            } finally {
               System.getProperties().put("user.dir", Execute.antWorkingDirectory);
            }

            return var5;
         }
      }
   }

   private static class WinNTCommandLauncher extends Execute.CommandLauncherProxy {
      WinNTCommandLauncher(Execute.CommandLauncher launcher) {
         super(launcher);
      }

      public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
         File commandDir = workingDir;
         if (workingDir == null) {
            if (project == null) {
               return this.exec(project, cmd, env);
            }

            commandDir = project.getBaseDir();
         }

         int preCmdLength = true;
         String[] newcmd = new String[cmd.length + 6];
         newcmd[0] = "cmd";
         newcmd[1] = "/c";
         newcmd[2] = "cd";
         newcmd[3] = "/d";
         newcmd[4] = commandDir.getAbsolutePath();
         newcmd[5] = "&&";
         System.arraycopy(cmd, 0, newcmd, 6, cmd.length);
         return this.exec(project, newcmd, env);
      }
   }

   private static class OS2CommandLauncher extends Execute.CommandLauncherProxy {
      OS2CommandLauncher(Execute.CommandLauncher launcher) {
         super(launcher);
      }

      public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
         File commandDir = workingDir;
         if (workingDir == null) {
            if (project == null) {
               return this.exec(project, cmd, env);
            }

            commandDir = project.getBaseDir();
         }

         int preCmdLength = true;
         String cmdDir = commandDir.getAbsolutePath();
         String[] newcmd = new String[cmd.length + 7];
         newcmd[0] = "cmd";
         newcmd[1] = "/c";
         newcmd[2] = cmdDir.substring(0, 2);
         newcmd[3] = "&&";
         newcmd[4] = "cd";
         newcmd[5] = cmdDir.substring(2);
         newcmd[6] = "&&";
         System.arraycopy(cmd, 0, newcmd, 7, cmd.length);
         return this.exec(project, newcmd, env);
      }
   }

   private static class CommandLauncherProxy extends Execute.CommandLauncher {
      private Execute.CommandLauncher myLauncher;

      CommandLauncherProxy(Execute.CommandLauncher launcher) {
         super(null);
         this.myLauncher = launcher;
      }

      public Process exec(Project project, String[] cmd, String[] env) throws IOException {
         return this.myLauncher.exec(project, cmd, env);
      }
   }

   private static class Java13CommandLauncher extends Execute.CommandLauncher {
      private Method myExecWithCWD;

      public Java13CommandLauncher() throws NoSuchMethodException {
         super(null);
         this.myExecWithCWD = (Execute.class$java$lang$Runtime == null ? (Execute.class$java$lang$Runtime = Execute.class$("java.lang.Runtime")) : Execute.class$java$lang$Runtime).getMethod("exec", Execute.array$Ljava$lang$String == null ? (Execute.array$Ljava$lang$String = Execute.class$("[Ljava.lang.String;")) : Execute.array$Ljava$lang$String, Execute.array$Ljava$lang$String == null ? (Execute.array$Ljava$lang$String = Execute.class$("[Ljava.lang.String;")) : Execute.array$Ljava$lang$String, Execute.class$java$io$File == null ? (Execute.class$java$io$File = Execute.class$("java.io.File")) : Execute.class$java$io$File);
      }

      public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
         try {
            if (project != null) {
               project.log("Execute:Java13CommandLauncher: " + Commandline.describeCommand(cmd), 4);
            }

            return (Process)this.myExecWithCWD.invoke(Runtime.getRuntime(), cmd, env, workingDir);
         } catch (InvocationTargetException var7) {
            Throwable realexc = var7.getTargetException();
            if (realexc instanceof ThreadDeath) {
               throw (ThreadDeath)realexc;
            } else if (realexc instanceof IOException) {
               throw (IOException)realexc;
            } else {
               throw new BuildException("Unable to execute command", realexc);
            }
         } catch (Exception var8) {
            throw new BuildException("Unable to execute command", var8);
         }
      }
   }

   private static class CommandLauncher {
      private CommandLauncher() {
      }

      public Process exec(Project project, String[] cmd, String[] env) throws IOException {
         if (project != null) {
            project.log("Execute:CommandLauncher: " + Commandline.describeCommand(cmd), 4);
         }

         return Runtime.getRuntime().exec(cmd, env);
      }

      public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
         if (workingDir == null) {
            return this.exec(project, cmd, env);
         } else {
            throw new IOException("Cannot execute a process in different directory under this JVM");
         }
      }

      // $FF: synthetic method
      CommandLauncher(Object x0) {
         this();
      }
   }
}

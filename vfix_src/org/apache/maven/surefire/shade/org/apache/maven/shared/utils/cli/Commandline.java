package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.Os;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.StringUtils;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.shell.BourneShell;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.shell.CmdShell;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.shell.CommandShell;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.shell.Shell;

public class Commandline implements Cloneable {
   private final List<Arg> arguments = new Vector();
   private final Map<String, String> envVars = Collections.synchronizedMap(new LinkedHashMap());
   private Shell shell;

   public Commandline(Shell shell) {
      this.shell = shell;
   }

   public Commandline(String toProcess) {
      this.setDefaultShell();
      String[] tmp = new String[0];

      try {
         tmp = CommandLineUtils.translateCommandline(toProcess);
      } catch (Exception var4) {
         System.err.println("Error translating Commandline.");
      }

      if (tmp != null && tmp.length > 0) {
         this.setExecutable(tmp[0]);

         for(int i = 1; i < tmp.length; ++i) {
            this.createArg().setValue(tmp[i]);
         }
      }

   }

   public Commandline() {
      this.setDefaultShell();
   }

   private void setDefaultShell() {
      if (Os.isFamily("windows")) {
         if (Os.isFamily("win9x")) {
            this.setShell(new CommandShell());
         } else {
            this.setShell(new CmdShell());
         }
      } else {
         this.setShell(new BourneShell());
      }

   }

   public Arg createArg() {
      return this.createArg(false);
   }

   public Arg createArg(boolean insertAtStart) {
      Arg argument = new Commandline.Argument();
      if (insertAtStart) {
         this.arguments.add(0, argument);
      } else {
         this.arguments.add(argument);
      }

      return argument;
   }

   public void setExecutable(String executable) {
      this.shell.setExecutable(executable);
   }

   public String getExecutable() {
      return this.shell.getExecutable();
   }

   public void addArguments(String... line) {
      String[] arr$ = line;
      int len$ = line.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String aLine = arr$[i$];
         this.createArg().setValue(aLine);
      }

   }

   public void addEnvironment(String name, String value) {
      this.envVars.put(name, value);
   }

   public void addSystemEnvironment() {
      Properties systemEnvVars = CommandLineUtils.getSystemEnvVars();
      Iterator i$ = systemEnvVars.keySet().iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         String key = (String)o;
         if (!this.envVars.containsKey(key)) {
            this.addEnvironment(key, systemEnvVars.getProperty(key));
         }
      }

   }

   public String[] getEnvironmentVariables() {
      this.addSystemEnvironment();
      String[] environmentVars = new String[this.envVars.size()];
      int i = 0;

      for(Iterator i$ = this.envVars.keySet().iterator(); i$.hasNext(); ++i) {
         String name = (String)i$.next();
         String value = (String)this.envVars.get(name);
         environmentVars[i] = name + "=" + value;
      }

      return environmentVars;
   }

   public String[] getCommandline() {
      String[] args = this.getArguments();
      String executable = this.getExecutable();
      if (executable == null) {
         return args;
      } else {
         String[] result = new String[args.length + 1];
         result[0] = executable;
         System.arraycopy(args, 0, result, 1, args.length);
         return result;
      }
   }

   private String[] getShellCommandline() {
      List<String> shellCommandLine = this.getShell().getShellCommandLine(this.getArguments());
      return (String[])shellCommandLine.toArray(new String[shellCommandLine.size()]);
   }

   public String[] getArguments() {
      List<String> result = new ArrayList(this.arguments.size() * 2);
      Iterator i$ = this.arguments.iterator();

      while(i$.hasNext()) {
         Arg argument = (Arg)i$.next();
         Commandline.Argument arg = (Commandline.Argument)argument;
         String[] s = arg.getParts();
         if (s != null) {
            Collections.addAll(result, s);
         }
      }

      return (String[])result.toArray(new String[result.size()]);
   }

   public String toString() {
      return StringUtils.join((Object[])this.getShellCommandline(), " ");
   }

   public Object clone() {
      throw new RuntimeException("Do we ever clone a commandline?");
   }

   public void setWorkingDirectory(String path) {
      this.shell.setWorkingDirectory(path);
   }

   public void setWorkingDirectory(File workingDirectory) {
      this.shell.setWorkingDirectory(workingDirectory);
   }

   public File getWorkingDirectory() {
      return this.shell.getWorkingDirectory();
   }

   public void clearArgs() {
      this.arguments.clear();
   }

   public Process execute() throws CommandLineException {
      String[] environment = this.getEnvironmentVariables();
      File workingDir = this.shell.getWorkingDirectory();

      try {
         Process process;
         if (workingDir == null) {
            process = Runtime.getRuntime().exec(this.getShellCommandline(), environment);
         } else {
            if (!workingDir.exists()) {
               throw new CommandLineException("Working directory \"" + workingDir.getPath() + "\" does not exist!");
            }

            if (!workingDir.isDirectory()) {
               throw new CommandLineException("Path \"" + workingDir.getPath() + "\" does not specify a directory.");
            }

            process = Runtime.getRuntime().exec(this.getShellCommandline(), environment, workingDir);
         }

         return process;
      } catch (IOException var5) {
         throw new CommandLineException("Error while executing process.", var5);
      }
   }

   void setShell(Shell shell) {
      this.shell = shell;
   }

   public Shell getShell() {
      return this.shell;
   }

   public static class Argument implements Arg {
      private String[] parts;

      public void setValue(String value) {
         if (value != null) {
            this.parts = new String[]{value};
         }

      }

      public void setLine(String line) {
         if (line != null) {
            try {
               this.parts = CommandLineUtils.translateCommandline(line);
            } catch (Exception var3) {
               System.err.println("Error translating Commandline.");
            }

         }
      }

      public void setFile(File value) {
         this.parts = new String[]{value.getAbsolutePath()};
      }

      private String[] getParts() {
         return this.parts;
      }
   }
}

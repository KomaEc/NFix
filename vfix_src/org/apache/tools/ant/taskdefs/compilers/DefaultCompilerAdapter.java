package org.apache.tools.ant.taskdefs.compilers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.StringUtils;

public abstract class DefaultCompilerAdapter implements CompilerAdapter {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   protected Path src;
   protected File destDir;
   protected String encoding;
   protected boolean debug = false;
   protected boolean optimize = false;
   protected boolean deprecation = false;
   protected boolean depend = false;
   protected boolean verbose = false;
   protected String target;
   protected Path bootclasspath;
   protected Path extdirs;
   protected Path compileClasspath;
   protected Path compileSourcepath;
   protected Project project;
   protected Location location;
   protected boolean includeAntRuntime;
   protected boolean includeJavaRuntime;
   protected String memoryInitialSize;
   protected String memoryMaximumSize;
   protected File[] compileList;
   protected Javac attributes;
   protected static final String lSep;

   public void setJavac(Javac attributes) {
      this.attributes = attributes;
      this.src = attributes.getSrcdir();
      this.destDir = attributes.getDestdir();
      this.encoding = attributes.getEncoding();
      this.debug = attributes.getDebug();
      this.optimize = attributes.getOptimize();
      this.deprecation = attributes.getDeprecation();
      this.depend = attributes.getDepend();
      this.verbose = attributes.getVerbose();
      this.target = attributes.getTarget();
      this.bootclasspath = attributes.getBootclasspath();
      this.extdirs = attributes.getExtdirs();
      this.compileList = attributes.getFileList();
      this.compileClasspath = attributes.getClasspath();
      this.compileSourcepath = attributes.getSourcepath();
      this.project = attributes.getProject();
      this.location = attributes.getLocation();
      this.includeAntRuntime = attributes.getIncludeantruntime();
      this.includeJavaRuntime = attributes.getIncludejavaruntime();
      this.memoryInitialSize = attributes.getMemoryInitialSize();
      this.memoryMaximumSize = attributes.getMemoryMaximumSize();
   }

   public Javac getJavac() {
      return this.attributes;
   }

   protected Project getProject() {
      return this.project;
   }

   protected Path getCompileClasspath() {
      Path classpath = new Path(this.project);
      if (this.destDir != null) {
         classpath.setLocation(this.destDir);
      }

      Path cp = this.compileClasspath;
      if (cp == null) {
         cp = new Path(this.project);
      }

      if (this.includeAntRuntime) {
         classpath.addExisting(cp.concatSystemClasspath("last"));
      } else {
         classpath.addExisting(cp.concatSystemClasspath("ignore"));
      }

      if (this.includeJavaRuntime) {
         classpath.addJavaRuntime();
      }

      return classpath;
   }

   protected Commandline setupJavacCommandlineSwitches(Commandline cmd) {
      return this.setupJavacCommandlineSwitches(cmd, false);
   }

   protected Commandline setupJavacCommandlineSwitches(Commandline cmd, boolean useDebugLevel) {
      Path classpath = this.getCompileClasspath();
      Path sourcepath = null;
      if (this.compileSourcepath != null) {
         sourcepath = this.compileSourcepath;
      } else {
         sourcepath = this.src;
      }

      String memoryParameterPrefix = this.assumeJava11() ? "-J-" : "-J-X";
      if (this.memoryInitialSize != null) {
         if (!this.attributes.isForkedJavac()) {
            this.attributes.log("Since fork is false, ignoring memoryInitialSize setting.", 1);
         } else {
            cmd.createArgument().setValue(memoryParameterPrefix + "ms" + this.memoryInitialSize);
         }
      }

      if (this.memoryMaximumSize != null) {
         if (!this.attributes.isForkedJavac()) {
            this.attributes.log("Since fork is false, ignoring memoryMaximumSize setting.", 1);
         } else {
            cmd.createArgument().setValue(memoryParameterPrefix + "mx" + this.memoryMaximumSize);
         }
      }

      if (this.attributes.getNowarn()) {
         cmd.createArgument().setValue("-nowarn");
      }

      if (this.deprecation) {
         cmd.createArgument().setValue("-deprecation");
      }

      if (this.destDir != null) {
         cmd.createArgument().setValue("-d");
         cmd.createArgument().setFile(this.destDir);
      }

      cmd.createArgument().setValue("-classpath");
      Path bp;
      if (this.assumeJava11()) {
         bp = new Path(this.project);
         Path bp = this.getBootClassPath();
         if (bp.size() > 0) {
            bp.append(bp);
         }

         if (this.extdirs != null) {
            bp.addExtdirs(this.extdirs);
         }

         bp.append(classpath);
         bp.append(sourcepath);
         cmd.createArgument().setPath(bp);
      } else {
         cmd.createArgument().setPath(classpath);
         if (sourcepath.size() > 0) {
            cmd.createArgument().setValue("-sourcepath");
            cmd.createArgument().setPath(sourcepath);
         }

         if (this.target != null) {
            cmd.createArgument().setValue("-target");
            cmd.createArgument().setValue(this.target);
         }

         bp = this.getBootClassPath();
         if (bp.size() > 0) {
            cmd.createArgument().setValue("-bootclasspath");
            cmd.createArgument().setPath(bp);
         }

         if (this.extdirs != null && this.extdirs.size() > 0) {
            cmd.createArgument().setValue("-extdirs");
            cmd.createArgument().setPath(this.extdirs);
         }
      }

      if (this.encoding != null) {
         cmd.createArgument().setValue("-encoding");
         cmd.createArgument().setValue(this.encoding);
      }

      if (this.debug) {
         if (useDebugLevel && !this.assumeJava11()) {
            String debugLevel = this.attributes.getDebugLevel();
            if (debugLevel != null) {
               cmd.createArgument().setValue("-g:" + debugLevel);
            } else {
               cmd.createArgument().setValue("-g");
            }
         } else {
            cmd.createArgument().setValue("-g");
         }
      } else if (this.getNoDebugArgument() != null) {
         cmd.createArgument().setValue(this.getNoDebugArgument());
      }

      if (this.optimize) {
         cmd.createArgument().setValue("-O");
      }

      if (this.depend) {
         if (this.assumeJava11()) {
            cmd.createArgument().setValue("-depend");
         } else if (this.assumeJava12()) {
            cmd.createArgument().setValue("-Xdepend");
         } else {
            this.attributes.log("depend attribute is not supported by the modern compiler", 1);
         }
      }

      if (this.verbose) {
         cmd.createArgument().setValue("-verbose");
      }

      this.addCurrentCompilerArgs(cmd);
      return cmd;
   }

   protected Commandline setupModernJavacCommandlineSwitches(Commandline cmd) {
      this.setupJavacCommandlineSwitches(cmd, true);
      String t;
      if (this.attributes.getSource() != null && !this.assumeJava13()) {
         cmd.createArgument().setValue("-source");
         t = this.attributes.getSource();
         if (!this.assumeJava14() && !this.assumeJava15() || !t.equals("1.1") && !t.equals("1.2")) {
            cmd.createArgument().setValue(t);
         } else {
            cmd.createArgument().setValue("1.3");
         }
      } else if ((this.assumeJava15() || this.assumeJava16()) && this.attributes.getTarget() != null) {
         t = this.attributes.getTarget();
         if (t.equals("1.1") || t.equals("1.2") || t.equals("1.3") || t.equals("1.4")) {
            String s = t;
            if (t.equals("1.1")) {
               s = "1.2";
            }

            this.attributes.log("", 1);
            this.attributes.log("          WARNING", 1);
            this.attributes.log("", 1);
            this.attributes.log("The -source switch defaults to 1.5 in JDK 1.5 and 1.6.", 1);
            this.attributes.log("If you specify -target " + t + " you now must also specify -source " + s + ".", 1);
            this.attributes.log("Ant will implicitly add -source " + s + " for you.  Please change your build file.", 1);
            cmd.createArgument().setValue("-source");
            cmd.createArgument().setValue(s);
         }
      }

      return cmd;
   }

   protected Commandline setupModernJavacCommand() {
      Commandline cmd = new Commandline();
      this.setupModernJavacCommandlineSwitches(cmd);
      this.logAndAddFilesToCompile(cmd);
      return cmd;
   }

   protected Commandline setupJavacCommand() {
      return this.setupJavacCommand(false);
   }

   protected Commandline setupJavacCommand(boolean debugLevelCheck) {
      Commandline cmd = new Commandline();
      this.setupJavacCommandlineSwitches(cmd, debugLevelCheck);
      this.logAndAddFilesToCompile(cmd);
      return cmd;
   }

   protected void logAndAddFilesToCompile(Commandline cmd) {
      this.attributes.log("Compilation " + cmd.describeArguments(), 3);
      StringBuffer niceSourceList = new StringBuffer("File");
      if (this.compileList.length != 1) {
         niceSourceList.append("s");
      }

      niceSourceList.append(" to be compiled:");
      niceSourceList.append(StringUtils.LINE_SEP);

      for(int i = 0; i < this.compileList.length; ++i) {
         String arg = this.compileList[i].getAbsolutePath();
         cmd.createArgument().setValue(arg);
         niceSourceList.append("    ");
         niceSourceList.append(arg);
         niceSourceList.append(StringUtils.LINE_SEP);
      }

      this.attributes.log(niceSourceList.toString(), 3);
   }

   protected int executeExternalCompile(String[] args, int firstFileName) {
      return this.executeExternalCompile(args, firstFileName, true);
   }

   protected int executeExternalCompile(String[] args, int firstFileName, boolean quoteFiles) {
      String[] commandArray = null;
      File tmpFile = null;

      int i;
      try {
         if (Commandline.toString(args).length() > 4096 && firstFileName >= 0) {
            PrintWriter out = null;

            try {
               tmpFile = FILE_UTILS.createTempFile("files", "", this.getJavac().getTempdir());
               tmpFile.deleteOnExit();
               out = new PrintWriter(new FileWriter(tmpFile));
               i = firstFileName;

               while(true) {
                  if (i >= args.length) {
                     out.flush();
                     commandArray = new String[firstFileName + 1];
                     System.arraycopy(args, 0, commandArray, 0, firstFileName);
                     commandArray[firstFileName] = "@" + tmpFile;
                     break;
                  }

                  if (quoteFiles && args[i].indexOf(" ") > -1) {
                     args[i] = args[i].replace(File.separatorChar, '/');
                     out.println("\"" + args[i] + "\"");
                  } else {
                     out.println(args[i]);
                  }

                  ++i;
               }
            } catch (IOException var19) {
               throw new BuildException("Error creating temporary file", var19, this.location);
            } finally {
               FileUtils.close((Writer)out);
            }
         } else {
            commandArray = args;
         }

         try {
            Execute exe = new Execute(new LogStreamHandler(this.attributes, 2, 1));
            if (Os.isFamily("openvms")) {
               exe.setVMLauncher(true);
            }

            exe.setAntRun(this.project);
            exe.setWorkingDirectory(this.project.getBaseDir());
            exe.setCommandline(commandArray);
            exe.execute();
            i = exe.getExitValue();
         } catch (IOException var18) {
            throw new BuildException("Error running " + args[0] + " compiler", var18, this.location);
         }
      } finally {
         if (tmpFile != null) {
            tmpFile.delete();
         }

      }

      return i;
   }

   /** @deprecated */
   protected void addExtdirsToClasspath(Path classpath) {
      classpath.addExtdirs(this.extdirs);
   }

   protected void addCurrentCompilerArgs(Commandline cmd) {
      cmd.addArguments(this.getJavac().getCurrentCompilerArgs());
   }

   protected boolean assumeJava11() {
      return "javac1.1".equals(this.attributes.getCompilerVersion());
   }

   protected boolean assumeJava12() {
      return "javac1.2".equals(this.attributes.getCompilerVersion()) || "classic".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.2") || "extJavac".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.2");
   }

   protected boolean assumeJava13() {
      return "javac1.3".equals(this.attributes.getCompilerVersion()) || "classic".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.3") || "modern".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.3") || "extJavac".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.3");
   }

   protected boolean assumeJava14() {
      return "javac1.4".equals(this.attributes.getCompilerVersion()) || "classic".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.4") || "modern".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.4") || "extJavac".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.4");
   }

   protected boolean assumeJava15() {
      return "javac1.5".equals(this.attributes.getCompilerVersion()) || "classic".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.5") || "modern".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.5") || "extJavac".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.5");
   }

   protected boolean assumeJava16() {
      return "javac1.6".equals(this.attributes.getCompilerVersion()) || "classic".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.6") || "modern".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.6") || "extJavac".equals(this.attributes.getCompilerVersion()) && JavaEnvUtils.isJavaVersion("1.6");
   }

   protected Path getBootClassPath() {
      Path bp = new Path(this.project);
      if (this.bootclasspath != null) {
         bp.append(this.bootclasspath);
      }

      return bp.concatSystemBootClasspath("ignore");
   }

   protected String getNoDebugArgument() {
      return this.assumeJava11() ? null : "-g:none";
   }

   static {
      lSep = StringUtils.LINE_SEP;
   }
}

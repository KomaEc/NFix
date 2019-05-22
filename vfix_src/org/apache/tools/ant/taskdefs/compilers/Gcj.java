package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

public class Gcj extends DefaultCompilerAdapter {
   private static final String[] CONFLICT_WITH_DASH_C = new String[]{"-o", "--main=", "-D", "-fjni", "-L"};

   public boolean execute() throws BuildException {
      this.attributes.log("Using gcj compiler", 3);
      Commandline cmd = this.setupGCJCommand();
      int firstFileName = cmd.size();
      this.logAndAddFilesToCompile(cmd);
      return this.executeExternalCompile(cmd.getCommandline(), firstFileName) == 0;
   }

   protected Commandline setupGCJCommand() {
      Commandline cmd = new Commandline();
      Path classpath = new Path(this.project);
      Path p = this.getBootClassPath();
      if (p.size() > 0) {
         classpath.append(p);
      }

      classpath.addExtdirs(this.extdirs);
      classpath.append(this.getCompileClasspath());
      if (this.compileSourcepath != null) {
         classpath.append(this.compileSourcepath);
      } else {
         classpath.append(this.src);
      }

      String exec = this.getJavac().getExecutable();
      cmd.setExecutable(exec == null ? "gcj" : exec);
      if (this.destDir != null) {
         cmd.createArgument().setValue("-d");
         cmd.createArgument().setFile(this.destDir);
         if (!this.destDir.exists() && !this.destDir.mkdirs()) {
            throw new BuildException("Can't make output directories. Maybe permission is wrong. ");
         }
      }

      cmd.createArgument().setValue("-classpath");
      cmd.createArgument().setPath(classpath);
      if (this.encoding != null) {
         cmd.createArgument().setValue("--encoding=" + this.encoding);
      }

      if (this.debug) {
         cmd.createArgument().setValue("-g1");
      }

      if (this.optimize) {
         cmd.createArgument().setValue("-O");
      }

      if (!this.isNativeBuild()) {
         cmd.createArgument().setValue("-C");
      }

      this.addCurrentCompilerArgs(cmd);
      return cmd;
   }

   public boolean isNativeBuild() {
      boolean nativeBuild = false;
      String[] additionalArguments = this.getJavac().getCurrentCompilerArgs();

      for(int argsLength = 0; !nativeBuild && argsLength < additionalArguments.length; ++argsLength) {
         for(int conflictLength = 0; !nativeBuild && conflictLength < CONFLICT_WITH_DASH_C.length; ++conflictLength) {
            nativeBuild = additionalArguments[argsLength].startsWith(CONFLICT_WITH_DASH_C[conflictLength]);
         }
      }

      return nativeBuild;
   }
}

package org.apache.tools.ant.taskdefs.compilers;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;

public class JavacExternal extends DefaultCompilerAdapter {
   public boolean execute() throws BuildException {
      this.attributes.log("Using external javac compiler", 3);
      Commandline cmd = new Commandline();
      cmd.setExecutable(this.getJavac().getJavacExecutable());
      if (!this.assumeJava11() && !this.assumeJava12()) {
         this.setupModernJavacCommandlineSwitches(cmd);
      } else {
         this.setupJavacCommandlineSwitches(cmd, true);
      }

      int firstFileName = this.assumeJava11() ? -1 : cmd.size();
      this.logAndAddFilesToCompile(cmd);
      if (Os.isFamily("openvms")) {
         return this.execOnVMS(cmd, firstFileName);
      } else {
         return this.executeExternalCompile(cmd.getCommandline(), firstFileName, true) == 0;
      }
   }

   private boolean execOnVMS(Commandline cmd, int firstFileName) {
      File vmsFile = null;

      boolean var5;
      try {
         vmsFile = JavaEnvUtils.createVmsJavaOptionFile(cmd.getArguments());
         String[] commandLine = new String[]{cmd.getExecutable(), "-V", vmsFile.getPath()};
         var5 = 0 == this.executeExternalCompile(commandLine, firstFileName, true);
      } catch (IOException var9) {
         throw new BuildException("Failed to create a temporary file for \"-V\" switch");
      } finally {
         FileUtils.delete(vmsFile);
      }

      return var5;
   }
}

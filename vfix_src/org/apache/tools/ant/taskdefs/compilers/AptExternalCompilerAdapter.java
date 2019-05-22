package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Apt;
import org.apache.tools.ant.types.Commandline;

public class AptExternalCompilerAdapter extends DefaultCompilerAdapter {
   protected Apt getApt() {
      return (Apt)this.getJavac();
   }

   public boolean execute() throws BuildException {
      this.attributes.log("Using external apt compiler", 3);
      Apt apt = this.getApt();
      Commandline cmd = new Commandline();
      cmd.setExecutable(apt.getAptExecutable());
      this.setupModernJavacCommandlineSwitches(cmd);
      AptCompilerAdapter.setAptCommandlineSwitches(apt, cmd);
      int firstFileName = cmd.size();
      this.logAndAddFilesToCompile(cmd);
      return 0 == this.executeExternalCompile(cmd.getCommandline(), firstFileName, true);
   }
}

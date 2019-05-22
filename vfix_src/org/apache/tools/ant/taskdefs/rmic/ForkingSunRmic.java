package org.apache.tools.ant.taskdefs.rmic;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.Rmic;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.JavaEnvUtils;

public class ForkingSunRmic extends DefaultRmicAdapter {
   public static final String COMPILER_NAME = "forking";

   public boolean execute() throws BuildException {
      Rmic owner = this.getRmic();
      Commandline cmd = this.setupRmicCommand();
      Project project = owner.getProject();
      cmd.setExecutable(JavaEnvUtils.getJdkExecutable(this.getExecutableName()));
      String[] args = cmd.getCommandline();

      try {
         Execute exe = new Execute(new LogStreamHandler(owner, 2, 1));
         exe.setAntRun(project);
         exe.setWorkingDirectory(project.getBaseDir());
         exe.setCommandline(args);
         exe.execute();
         return !exe.isFailure();
      } catch (IOException var6) {
         throw new BuildException("Error running " + this.getExecutableName() + " -maybe it is not on the path", var6);
      }
   }

   protected String getExecutableName() {
      return "rmic";
   }
}

package org.apache.maven.scm.provider.cvslib.cvsjava.command.add;

import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.provider.cvslib.command.add.AbstractCvsAddCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsConnection;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsLogListener;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsJavaAddCommand extends AbstractCvsAddCommand {
   protected AddScmResult executeCvsCommand(Commandline cl, List<ScmFile> addedFiles) throws ScmException {
      CvsLogListener logListener = new CvsLogListener();

      try {
         boolean isSuccess = CvsConnection.processCommand(cl.getArguments(), cl.getWorkingDirectory().getAbsolutePath(), logListener, this.getLogger());
         return !isSuccess ? new AddScmResult(cl.toString(), "The cvs command failed.", logListener.getStdout().toString(), false) : new AddScmResult(cl.toString(), addedFiles);
      } catch (Exception var5) {
         var5.printStackTrace();
         return new AddScmResult(cl.toString(), "The cvs command failed.", logListener.getStdout().toString(), false);
      }
   }
}

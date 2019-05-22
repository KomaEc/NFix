package org.apache.maven.scm.provider.cvslib.cvsjava.command.remove;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.provider.cvslib.command.remove.AbstractCvsRemoveCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsConnection;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsLogListener;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsJavaRemoveCommand extends AbstractCvsRemoveCommand {
   protected RemoveScmResult executeCvsCommand(Commandline cl, List<ScmFile> removedFiles) throws ScmException {
      CvsLogListener logListener = new CvsLogListener();

      try {
         boolean isSuccess = CvsConnection.processCommand(cl.getArguments(), cl.getWorkingDirectory().getAbsolutePath(), logListener, this.getLogger());
         if (!isSuccess) {
            return new RemoveScmResult(cl.toString(), "The cvs command failed.", logListener.getStderr().toString(), false);
         }

         BufferedReader stream = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(logListener.getStdout().toString().getBytes())));
         String line;
         if (this.getLogger().isDebugEnabled()) {
            while((line = stream.readLine()) != null) {
               this.getLogger().debug(line);
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
         return new RemoveScmResult(cl.toString(), "The cvs command failed.", logListener.getStderr().toString(), false);
      }

      return new RemoveScmResult(cl.toString(), removedFiles);
   }
}

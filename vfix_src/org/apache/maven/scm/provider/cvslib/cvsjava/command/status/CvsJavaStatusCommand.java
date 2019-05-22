package org.apache.maven.scm.provider.cvslib.cvsjava.command.status;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.cvslib.command.status.AbstractCvsStatusCommand;
import org.apache.maven.scm.provider.cvslib.command.status.CvsStatusConsumer;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsConnection;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsLogListener;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsJavaStatusCommand extends AbstractCvsStatusCommand {
   protected StatusScmResult executeCvsCommand(Commandline cl) throws ScmException {
      CvsLogListener logListener = new CvsLogListener();
      CvsStatusConsumer consumer = new CvsStatusConsumer(this.getLogger(), cl.getWorkingDirectory());

      try {
         boolean isSuccess = CvsConnection.processCommand(cl.getArguments(), cl.getWorkingDirectory().getAbsolutePath(), logListener, this.getLogger());
         if (!isSuccess) {
            return new StatusScmResult(cl.toString(), "The cvs command failed.", logListener.getStderr().toString(), false);
         }

         BufferedReader stream = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(logListener.getStdout().toString().getBytes())));

         String line;
         while((line = stream.readLine()) != null) {
            consumer.consumeLine(line);
         }
      } catch (Exception var7) {
         var7.printStackTrace();
         return new StatusScmResult(cl.toString(), "The cvs command failed.", logListener.getStderr().toString(), false);
      }

      return new StatusScmResult(cl.toString(), consumer.getChangedFiles());
   }
}

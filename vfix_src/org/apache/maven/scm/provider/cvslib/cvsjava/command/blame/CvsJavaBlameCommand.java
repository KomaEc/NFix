package org.apache.maven.scm.provider.cvslib.cvsjava.command.blame;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.provider.cvslib.command.blame.AbstractCvsBlameCommand;
import org.apache.maven.scm.provider.cvslib.command.blame.CvsBlameConsumer;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsConnection;
import org.apache.maven.scm.provider.cvslib.cvsjava.util.CvsLogListener;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsJavaBlameCommand extends AbstractCvsBlameCommand {
   protected BlameScmResult executeCvsCommand(Commandline cl, CvsScmProviderRepository repository) {
      CvsLogListener logListener = new CvsLogListener();
      CvsBlameConsumer consumer = new CvsBlameConsumer(this.getLogger());

      try {
         boolean isSuccess = CvsConnection.processCommand(cl.getArguments(), cl.getWorkingDirectory().getAbsolutePath(), logListener, this.getLogger());
         if (!isSuccess) {
            return new BlameScmResult(cl.toString(), "The cvs command failed.", logListener.getStderr().toString(), false);
         }

         BufferedReader stream = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(logListener.getStdout().toString().getBytes())));

         String line;
         while((line = stream.readLine()) != null) {
            consumer.consumeLine(line);
         }
      } catch (Exception var8) {
         this.getLogger().error((Throwable)var8);
         return new BlameScmResult(cl.toString(), "The cvs command failed.", logListener.getStdout().toString(), false);
      }

      return new BlameScmResult(cl.toString(), consumer.getLines());
   }
}

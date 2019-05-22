package org.apache.maven.scm.provider.cvslib.cvsexe.command.checkin;

import java.io.File;
import java.io.IOException;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.provider.cvslib.command.checkin.AbstractCvsCheckInCommand;
import org.apache.maven.scm.provider.cvslib.command.checkin.CvsCheckInConsumer;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsExeCheckInCommand extends AbstractCvsCheckInCommand {
   protected CheckInScmResult executeCvsCommand(Commandline cl, CvsScmProviderRepository repository, File messageFile) throws ScmException {
      CvsCheckInConsumer consumer = new CvsCheckInConsumer(repository.getPath(), this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var9) {
         throw new ScmException("Error while executing command.", var9);
      }

      try {
         FileUtils.forceDelete(messageFile);
      } catch (IOException var8) {
      }

      return exitCode != 0 ? new CheckInScmResult(cl.toString(), "The cvs command failed.", stderr.getOutput(), false) : new CheckInScmResult(cl.toString(), consumer.getCheckedInFiles());
   }
}

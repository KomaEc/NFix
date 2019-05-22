package org.apache.maven.scm.provider.cvslib.cvsexe.command.blame;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.provider.cvslib.command.blame.AbstractCvsBlameCommand;
import org.apache.maven.scm.provider.cvslib.command.blame.CvsBlameConsumer;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsExeBlameCommand extends AbstractCvsBlameCommand {
   protected BlameScmResult executeCvsCommand(Commandline cl, CvsScmProviderRepository repository) throws ScmException {
      CvsBlameConsumer consumer = new CvsBlameConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var7) {
         throw new ScmException("Error while executing cvs command.", var7);
      }

      return exitCode != 0 ? new BlameScmResult(cl.toString(), "The cvs command failed.", stderr.getOutput(), false) : new BlameScmResult(cl.toString(), consumer.getLines());
   }
}

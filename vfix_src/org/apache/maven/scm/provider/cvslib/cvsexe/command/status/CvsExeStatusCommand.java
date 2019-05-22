package org.apache.maven.scm.provider.cvslib.cvsexe.command.status;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.cvslib.command.status.AbstractCvsStatusCommand;
import org.apache.maven.scm.provider.cvslib.command.status.CvsStatusConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsExeStatusCommand extends AbstractCvsStatusCommand {
   protected StatusScmResult executeCvsCommand(Commandline cl) throws ScmException {
      CvsStatusConsumer consumer = new CvsStatusConsumer(this.getLogger(), cl.getWorkingDirectory());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var6) {
         throw new ScmException("Error while executing command.", var6);
      }

      return exitCode != 0 ? new StatusScmResult(cl.toString(), "The cvs command failed.", stderr.getOutput(), false) : new StatusScmResult(cl.toString(), consumer.getChangedFiles());
   }
}

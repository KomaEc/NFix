package org.apache.maven.scm.provider.cvslib.cvsexe.command.list;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.provider.cvslib.command.list.AbstractCvsListCommand;
import org.apache.maven.scm.provider.cvslib.command.status.CvsStatusConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsExeListCommand extends AbstractCvsListCommand {
   protected ListScmResult executeCvsCommand(Commandline cl) throws ScmException {
      CvsStatusConsumer consumer = new CvsStatusConsumer(this.getLogger(), cl.getWorkingDirectory());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var6) {
         throw new ScmException("Error while executing command.", var6);
      }

      return exitCode != 0 ? new ListScmResult(cl.toString(), "The cvs command failed.", stderr.getOutput(), false) : new ListScmResult(cl.toString(), consumer.getChangedFiles());
   }
}

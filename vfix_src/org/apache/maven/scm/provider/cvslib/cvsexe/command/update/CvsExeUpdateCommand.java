package org.apache.maven.scm.provider.cvslib.cvsexe.command.update;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.cvslib.command.update.AbstractCvsUpdateCommand;
import org.apache.maven.scm.provider.cvslib.command.update.CvsUpdateConsumer;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.changelog.CvsExeChangeLogCommand;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsExeUpdateCommand extends AbstractCvsUpdateCommand {
   protected UpdateScmResult executeCvsCommand(Commandline cl) throws ScmException {
      CvsUpdateConsumer consumer = new CvsUpdateConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var6) {
         throw new ScmException("Error while executing command.", var6);
      }

      return exitCode != 0 ? new UpdateScmResult(cl.toString(), "The cvs command failed.", stderr.getOutput(), false) : new UpdateScmResult(cl.toString(), consumer.getUpdatedFiles());
   }

   protected ChangeLogCommand getChangeLogCommand() {
      CvsExeChangeLogCommand command = new CvsExeChangeLogCommand();
      command.setLogger(this.getLogger());
      return command;
   }
}

package org.apache.maven.scm.provider.cvslib.cvsexe.command.changelog;

import java.util.Date;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.provider.cvslib.command.changelog.AbstractCvsChangeLogCommand;
import org.apache.maven.scm.provider.cvslib.command.changelog.CvsChangeLogConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsExeChangeLogCommand extends AbstractCvsChangeLogCommand {
   protected ChangeLogScmResult executeCvsCommand(Commandline cl, Date startDate, Date endDate, ScmVersion startVersion, ScmVersion endVersion, String datePattern) throws ScmException {
      CvsChangeLogConsumer consumer = new CvsChangeLogConsumer(this.getLogger(), datePattern);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var11) {
         throw new ScmException("Error while executing cvs command.", var11);
      }

      if (exitCode != 0) {
         return new ChangeLogScmResult(cl.toString(), "The cvs command failed.", stderr.getOutput(), false);
      } else {
         ChangeLogSet changeLogSet = new ChangeLogSet(consumer.getModifications(), startDate, endDate);
         changeLogSet.setStartVersion(startVersion);
         changeLogSet.setEndVersion(endVersion);
         return new ChangeLogScmResult(cl.toString(), changeLogSet);
      }
   }
}

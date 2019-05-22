package org.apache.maven.scm.provider.cvslib.cvsexe.command.diff;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.provider.cvslib.command.diff.AbstractCvsDiffCommand;
import org.apache.maven.scm.provider.cvslib.command.diff.CvsDiffConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsExeDiffCommand extends AbstractCvsDiffCommand {
   protected DiffScmResult executeCvsCommand(Commandline cl) throws ScmException {
      CvsDiffConsumer consumer = new CvsDiffConsumer(this.getLogger(), cl.getWorkingDirectory());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      try {
         CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var5) {
         throw new ScmException("Error while executing command.", var5);
      }

      return new DiffScmResult(cl.toString(), consumer.getChangedFiles(), consumer.getDifferences(), consumer.getPatch());
   }
}

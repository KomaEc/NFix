package org.apache.maven.scm.provider.cvslib.cvsexe.command.branch;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.provider.cvslib.command.branch.AbstractCvsBranchCommand;
import org.apache.maven.scm.provider.cvslib.command.branch.CvsBranchConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsExeBranchCommand extends AbstractCvsBranchCommand {
   protected BranchScmResult executeCvsCommand(Commandline cl) throws ScmException {
      CvsBranchConsumer consumer = new CvsBranchConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var6) {
         throw new ScmException("Error while executing command.", var6);
      }

      return exitCode != 0 ? new BranchScmResult(cl.toString(), "The cvs branch command failed.", stderr.getOutput(), false) : new BranchScmResult(cl.toString(), consumer.getTaggedFiles());
   }
}

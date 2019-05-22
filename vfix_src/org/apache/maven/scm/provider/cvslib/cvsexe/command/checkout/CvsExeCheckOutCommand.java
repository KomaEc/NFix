package org.apache.maven.scm.provider.cvslib.cvsexe.command.checkout;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.provider.cvslib.command.checkout.AbstractCvsCheckOutCommand;
import org.apache.maven.scm.provider.cvslib.command.checkout.CvsCheckOutConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsExeCheckOutCommand extends AbstractCvsCheckOutCommand {
   protected CheckOutScmResult executeCvsCommand(Commandline cl) throws ScmException {
      CvsCheckOutConsumer consumer = new CvsCheckOutConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var6) {
         throw new ScmException("Error while executing command.", var6);
      }

      return exitCode != 0 ? new CheckOutScmResult(cl.toString(), "The cvs command failed.", stderr.getOutput(), false) : new CheckOutScmResult(cl.toString(), consumer.getCheckedOutFiles());
   }
}

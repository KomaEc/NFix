package org.apache.maven.scm.provider.cvslib.cvsexe.command.export;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.provider.cvslib.command.export.AbstractCvsExportCommand;
import org.apache.maven.scm.provider.cvslib.command.update.CvsUpdateConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsExeExportCommand extends AbstractCvsExportCommand {
   protected ExportScmResult executeCvsCommand(Commandline cl) throws ScmException {
      CvsUpdateConsumer consumer = new CvsUpdateConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var6) {
         throw new ScmException("Error while executing command.", var6);
      }

      return exitCode != 0 ? new ExportScmResult(cl.toString(), "The cvs command failed.", stderr.getOutput(), false) : new ExportScmResult(cl.toString(), consumer.getUpdatedFiles());
   }
}

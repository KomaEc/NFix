package org.apache.maven.scm.provider.cvslib.cvsexe.command.remove;

import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.provider.cvslib.command.remove.AbstractCvsRemoveCommand;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsExeRemoveCommand extends AbstractCvsRemoveCommand {
   protected RemoveScmResult executeCvsCommand(Commandline cl, List<ScmFile> removedFiles) throws ScmException {
      CommandLineUtils.StringStreamConsumer consumer = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var7) {
         throw new ScmException("Error while executing command.", var7);
      }

      return exitCode != 0 ? new RemoveScmResult(cl.toString(), "The cvs command failed.", stderr.getOutput(), false) : new RemoveScmResult(cl.toString(), removedFiles);
   }
}

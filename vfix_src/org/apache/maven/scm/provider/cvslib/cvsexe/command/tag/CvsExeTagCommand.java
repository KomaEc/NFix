package org.apache.maven.scm.provider.cvslib.cvsexe.command.tag;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.cvslib.command.tag.AbstractCvsTagCommand;
import org.apache.maven.scm.provider.cvslib.command.tag.CvsTagConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class CvsExeTagCommand extends AbstractCvsTagCommand {
   protected TagScmResult executeCvsCommand(Commandline cl) throws ScmException {
      CvsTagConsumer consumer = new CvsTagConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var6) {
         throw new ScmException("Error while executing command.", var6);
      }

      return exitCode != 0 ? new TagScmResult(cl.toString(), "The cvs tag command failed.", stderr.getOutput(), false) : new TagScmResult(cl.toString(), consumer.getTaggedFiles());
   }
}

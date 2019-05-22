package org.apache.maven.scm.provider.tfs.command.blame;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.blame.AbstractBlameCommand;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class TfsBlameCommand extends AbstractBlameCommand {
   public BlameScmResult executeBlameCommand(ScmProviderRepository repo, ScmFileSet workingDirectory, String filename) throws ScmException {
      Commandline cl = createCommandLine(workingDirectory.getBasedir(), filename);
      TfsBlameConsumer consumer = new TfsBlameConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var9) {
         throw new ScmException("Error while executing command.", var9);
      }

      return exitCode != 0 ? new BlameScmResult(cl.toString(), "The tfs command failed.", stderr.getOutput(), false) : new BlameScmResult(cl.toString(), consumer.getLines());
   }

   public static Commandline createCommandLine(File workingDirectory, String filename) {
      Commandline command = new Commandline();
      command.setWorkingDirectory(workingDirectory);
      command.setExecutable("tfpt");
      command.createArg().setValue("annotate");
      command.createArg().setValue("/noprompt");
      command.createArg().setValue(filename);
      return command;
   }
}

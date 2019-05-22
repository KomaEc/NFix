package org.apache.maven.scm.provider.clearcase.command.blame;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.blame.AbstractBlameCommand;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.command.ClearCaseCommand;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class ClearCaseBlameCommand extends AbstractBlameCommand implements ClearCaseCommand {
   public BlameScmResult executeBlameCommand(ScmProviderRepository repo, ScmFileSet workingDirectory, String filename) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing blame command...");
      }

      Commandline cl = createCommandLine(workingDirectory.getBasedir(), filename);
      ClearCaseBlameConsumer consumer = new ClearCaseBlameConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
         }

         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var9) {
         throw new ScmException("Error while executing cvs command.", var9);
      }

      return exitCode != 0 ? new BlameScmResult(cl.toString(), "The cleartool command failed.", stderr.getOutput(), false) : new BlameScmResult(cl.toString(), consumer.getLines());
   }

   public static Commandline createCommandLine(File workingDirectory, String filename) {
      Commandline command = new Commandline();
      command.setExecutable("cleartool");
      command.createArg().setValue("annotate");
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      StringBuilder format = new StringBuilder();
      format.append("VERSION:%Ln@@@");
      format.append("USER:%u@@@");
      format.append("DATE:%Nd@@@");
      command.createArg().setValue("-out");
      command.createArg().setValue("-");
      command.createArg().setValue("-fmt");
      command.createArg().setValue(format.toString());
      command.createArg().setValue("-nheader");
      command.createArg().setValue("-f");
      command.createArg().setValue(filename);
      return command;
   }
}

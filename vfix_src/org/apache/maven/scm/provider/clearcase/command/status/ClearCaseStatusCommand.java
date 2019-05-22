package org.apache.maven.scm.provider.clearcase.command.status;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.command.ClearCaseCommand;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class ClearCaseStatusCommand extends AbstractStatusCommand implements ClearCaseCommand {
   protected StatusScmResult executeStatusCommand(ScmProviderRepository scmProviderRepository, ScmFileSet scmFileSet) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing status command...");
      }

      Commandline cl = createCommandLine(scmFileSet);
      ClearCaseStatusConsumer consumer = new ClearCaseStatusConsumer(this.getLogger(), scmFileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
         }

         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var8) {
         throw new ScmException("Error while executing clearcase command.", var8);
      }

      return exitCode != 0 ? new StatusScmResult(cl.toString(), "The cleartool command failed.", stderr.getOutput(), false) : new StatusScmResult(cl.toString(), consumer.getCheckedOutFiles());
   }

   public static Commandline createCommandLine(ScmFileSet scmFileSet) {
      Commandline command = new Commandline();
      File workingDirectory = scmFileSet.getBasedir();
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("lscheckout");
      command.createArg().setValue("-cview");
      command.createArg().setValue("-r");
      command.createArg().setValue("-fmt");
      command.createArg().setValue("%n\\n");
      return command;
   }
}

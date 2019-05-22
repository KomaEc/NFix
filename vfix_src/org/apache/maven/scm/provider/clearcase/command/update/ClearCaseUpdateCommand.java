package org.apache.maven.scm.provider.clearcase.command.update;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.update.AbstractUpdateCommand;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.command.ClearCaseCommand;
import org.apache.maven.scm.provider.clearcase.command.changelog.ClearCaseChangeLogCommand;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class ClearCaseUpdateCommand extends AbstractUpdateCommand implements ClearCaseCommand {
   protected UpdateScmResult executeUpdateCommand(ScmProviderRepository repository, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing update command...");
      }

      Commandline cl = createCommandLine(fileSet);
      ClearCaseUpdateConsumer consumer = new ClearCaseUpdateConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
         }

         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var9) {
         throw new ScmException("Error while executing clearcase command.", var9);
      }

      return exitCode != 0 ? new UpdateScmResult(cl.toString(), "The cleartool command failed.", stderr.getOutput(), false) : new UpdateScmResult(cl.toString(), consumer.getUpdatedFiles());
   }

   protected ChangeLogCommand getChangeLogCommand() {
      ClearCaseChangeLogCommand changeLogCmd = new ClearCaseChangeLogCommand();
      changeLogCmd.setLogger(this.getLogger());
      return changeLogCmd;
   }

   public static Commandline createCommandLine(ScmFileSet scmFileSet) {
      Commandline command = new Commandline();
      File workingDirectory = scmFileSet.getBasedir();
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("update");
      command.createArg().setValue("-f");
      return command;
   }
}

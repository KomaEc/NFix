package org.apache.maven.scm.provider.vss.commands.status;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.vss.commands.VssCommandLineUtils;
import org.apache.maven.scm.provider.vss.commands.changelog.VssHistoryCommand;
import org.apache.maven.scm.provider.vss.repository.VssScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class VssStatusCommand extends AbstractStatusCommand {
   protected StatusScmResult executeStatusCommand(ScmProviderRepository repository, ScmFileSet fileSet) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing status command...");
      }

      VssScmProviderRepository repo = (VssScmProviderRepository)repository;
      Commandline cl = this.buildCmdLine(repo, fileSet);
      VssStatusConsumer consumer = new VssStatusConsumer(repo, this.getLogger(), fileSet);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
      }

      int exitCode = VssCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
      if (exitCode != 0) {
         String error = stderr.getOutput();
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("VSS returns error: [" + error + "] return code: [" + exitCode + "]");
         }

         return new StatusScmResult(cl.toString(), "The vss command failed.", error, false);
      } else {
         return new StatusScmResult(cl.toString(), consumer.getUpdatedFiles());
      }
   }

   public Commandline buildCmdLine(VssScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      Commandline command = new Commandline();
      command.setWorkingDirectory(fileSet.getBasedir().getAbsolutePath());

      try {
         command.addSystemEnvironment();
      } catch (Exception var5) {
         throw new ScmException("Can't add system environment.", var5);
      }

      command.addEnvironment("SSDIR", repo.getVssdir());
      String ssDir = VssCommandLineUtils.getSsDir();
      command.setExecutable(ssDir + "ss");
      command.createArg().setValue("Diff");
      command.createArg().setValue("$" + repo.getProject());
      if (repo.getUserPassword() != null) {
         command.createArg().setValue("-Y" + repo.getUserPassword());
      }

      command.createArg().setValue("-R");
      command.createArg().setValue("-I-");
      return command;
   }

   protected ChangeLogCommand getChangeLogCommand() {
      VssHistoryCommand command = new VssHistoryCommand();
      command.setLogger(this.getLogger());
      return command;
   }
}

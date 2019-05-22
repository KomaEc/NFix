package org.apache.maven.scm.provider.vss.commands.add;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AbstractAddCommand;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.vss.commands.VssCommandLineUtils;
import org.apache.maven.scm.provider.vss.repository.VssScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class VssAddCommand extends AbstractAddCommand {
   protected ScmResult executeAddCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message, boolean binary) throws ScmException {
      VssScmProviderRepository repo = (VssScmProviderRepository)repository;
      if (fileSet.getFileList().isEmpty()) {
         throw new ScmException("You must provide at least one file/directory to add");
      } else {
         Commandline cl = this.buildCmdLine(repo, fileSet);
         VssAddConsumer consumer = new VssAddConsumer(this.getLogger());
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("Executing: " + cl);
            this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
         }

         int exitCode = VssCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
         return (ScmResult)(exitCode != 0 ? new ChangeLogScmResult(cl.toString(), "The vss command failed.", stderr.getOutput(), false) : new AddScmResult(cl.toString(), consumer.getAddedFiles()));
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
      command.createArg().setValue("Add");
      VssCommandLineUtils.addFiles(command, fileSet);
      if (repo.getUserPassword() != null) {
         command.createArg().setValue("-Y" + repo.getUserPassword());
      }

      command.createArg().setValue("-I-");
      return command;
   }

   public Commandline buildSetCurrentProjectCmdLine(VssScmProviderRepository repo) throws ScmException {
      Commandline command = new Commandline();

      try {
         command.addSystemEnvironment();
      } catch (Exception var4) {
         throw new ScmException("Can't add system environment.", var4);
      }

      command.addEnvironment("SSDIR", repo.getVssdir());
      String ssDir = VssCommandLineUtils.getSsDir();
      command.setExecutable(ssDir + "ss");
      command.createArg().setValue("CP");
      command.createArg().setValue("$" + repo.getProject());
      if (repo.getUserPassword() != null) {
         command.createArg().setValue("-Y" + repo.getUserPassword());
      }

      command.createArg().setValue("-I-");
      return command;
   }
}

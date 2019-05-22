package org.apache.maven.scm.provider.svn.svnexe.command.update;

import java.io.File;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmTag;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.update.AbstractUpdateCommand;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.command.update.UpdateScmResultWithRevision;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.SvnTagBranchUtils;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.apache.maven.scm.provider.svn.svnexe.command.changelog.SvnChangeLogCommand;
import org.apache.maven.scm.provider.svn.util.SvnUtil;
import org.apache.maven.scm.providers.svn.settings.Settings;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnUpdateCommand extends AbstractUpdateCommand implements SvnCommand {
   protected UpdateScmResult executeUpdateCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      Commandline cl = createCommandLine((SvnScmProviderRepository)repo, fileSet.getBasedir(), version);
      SvnUpdateConsumer consumer = new SvnUpdateConsumer(this.getLogger(), fileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      int exitCode;
      try {
         exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      } catch (CommandLineException var9) {
         throw new ScmException("Error while executing command.", var9);
      }

      if (exitCode != 0) {
         return new UpdateScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false);
      } else {
         UpdateScmResultWithRevision result = new UpdateScmResultWithRevision(cl.toString(), consumer.getUpdatedFiles(), String.valueOf(consumer.getRevision()));
         result.setChanges(consumer.getChangeSets());
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("changeSets " + consumer.getChangeSets());
         }

         return result;
      }
   }

   public static Commandline createCommandLine(SvnScmProviderRepository repository, File workingDirectory, ScmVersion version) {
      Settings settings = SvnUtil.getSettings();
      String workingDir = workingDirectory.getAbsolutePath();
      if (settings.isUseCygwinPath()) {
         workingDir = settings.getCygwinMountPath() + "/" + workingDir;
         workingDir = StringUtils.replace(workingDir, ":", "");
         workingDir = StringUtils.replace(workingDir, "\\", "/");
      }

      if (version != null && StringUtils.isEmpty(version.getName())) {
         version = null;
      }

      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(workingDirectory, repository);
      if (version != null && !SvnTagBranchUtils.isRevisionSpecifier(version)) {
         if (version instanceof ScmBranch) {
            cl.createArg().setValue("switch");
            if (version instanceof ScmTag) {
               cl.createArg().setValue(SvnTagBranchUtils.resolveTagUrl(repository, (ScmTag)version));
            } else {
               cl.createArg().setValue(SvnTagBranchUtils.resolveBranchUrl(repository, (ScmBranch)version));
            }

            cl.createArg().setValue(workingDir);
         }
      } else {
         cl.createArg().setValue("update");
         if (version != null && StringUtils.isNotEmpty(version.getName())) {
            cl.createArg().setValue("-r");
            cl.createArg().setValue(version.getName());
         }

         cl.createArg().setValue(workingDir);
      }

      return cl;
   }

   protected ChangeLogCommand getChangeLogCommand() {
      SvnChangeLogCommand command = new SvnChangeLogCommand();
      command.setLogger(this.getLogger());
      return command;
   }
}

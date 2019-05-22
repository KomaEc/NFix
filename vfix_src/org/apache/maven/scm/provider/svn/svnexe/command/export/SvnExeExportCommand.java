package org.apache.maven.scm.provider.svn.svnexe.command.export;

import java.io.File;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmRevision;
import org.apache.maven.scm.ScmTag;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.export.AbstractExportCommand;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.command.export.ExportScmResultWithRevision;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.SvnCommandUtils;
import org.apache.maven.scm.provider.svn.SvnTagBranchUtils;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.apache.maven.scm.provider.svn.svnexe.command.update.SvnUpdateConsumer;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnExeExportCommand extends AbstractExportCommand implements SvnCommand {
   protected ExportScmResult executeExportCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version, String outputDirectory) throws ScmException {
      if (outputDirectory == null) {
         outputDirectory = fileSet.getBasedir().getAbsolutePath();
      }

      SvnScmProviderRepository repository = (SvnScmProviderRepository)repo;
      String url = repository.getUrl();
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         if (version instanceof ScmTag) {
            url = SvnTagBranchUtils.resolveTagUrl(repository, (ScmTag)version);
         } else if (version instanceof ScmBranch) {
            url = SvnTagBranchUtils.resolveBranchUrl(repository, (ScmBranch)version);
         }
      }

      url = SvnCommandUtils.fixUrl(url, repository.getUser());
      Commandline cl = createCommandLine((SvnScmProviderRepository)repo, fileSet.getBasedir(), version, url, outputDirectory);
      SvnUpdateConsumer consumer = new SvnUpdateConsumer(this.getLogger(), fileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
         if (cl.getWorkingDirectory() != null) {
            this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
         }
      }

      int exitCode;
      try {
         exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      } catch (CommandLineException var12) {
         throw new ScmException("Error while executing command.", var12);
      }

      return (ExportScmResult)(exitCode != 0 ? new ExportScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false) : new ExportScmResultWithRevision(cl.toString(), consumer.getUpdatedFiles(), String.valueOf(consumer.getRevision())));
   }

   public static Commandline createCommandLine(SvnScmProviderRepository repository, File workingDirectory, ScmVersion version, String url, String outputSirectory) {
      if (version != null && StringUtils.isEmpty(version.getName())) {
         version = null;
      }

      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(workingDirectory, repository);
      cl.createArg().setValue("export");
      if (version != null && StringUtils.isNotEmpty(version.getName()) && version instanceof ScmRevision) {
         cl.createArg().setValue("-r");
         cl.createArg().setValue(version.getName());
      }

      cl.createArg().setValue("--force");
      cl.createArg().setValue(url);
      if (StringUtils.isNotEmpty(outputSirectory)) {
         cl.createArg().setValue(outputSirectory);
      }

      return cl;
   }
}

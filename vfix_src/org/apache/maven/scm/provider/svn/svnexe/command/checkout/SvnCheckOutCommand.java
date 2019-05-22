package org.apache.maven.scm.provider.svn.svnexe.command.checkout;

import java.io.File;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmRevision;
import org.apache.maven.scm.ScmTag;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkout.AbstractCheckOutCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.SvnCommandUtils;
import org.apache.maven.scm.provider.svn.SvnTagBranchUtils;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnCheckOutCommand extends AbstractCheckOutCommand implements SvnCommand {
   protected CheckOutScmResult executeCheckOutCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version, boolean recursive) throws ScmException {
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
      Commandline cl = createCommandLine(repository, fileSet.getBasedir(), version, url, recursive);
      SvnCheckOutConsumer consumer = new SvnCheckOutConsumer(this.getLogger(), fileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      int exitCode;
      try {
         exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      } catch (CommandLineException var12) {
         throw new ScmException("Error while executing command.", var12);
      }

      return exitCode != 0 ? new CheckOutScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false) : new CheckOutScmResult(cl.toString(), Integer.toString(consumer.getRevision()), consumer.getCheckedOutFiles());
   }

   public static Commandline createCommandLine(SvnScmProviderRepository repository, File workingDirectory, ScmVersion version, String url) {
      return createCommandLine(repository, workingDirectory, version, url, true);
   }

   public static Commandline createCommandLine(SvnScmProviderRepository repository, File workingDirectory, ScmVersion version, String url, boolean recursive) {
      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(workingDirectory.getParentFile(), repository);
      cl.createArg().setValue("checkout");
      if (!recursive) {
         cl.createArg().setValue("-N");
      }

      if (version != null && StringUtils.isNotEmpty(version.getName()) && version instanceof ScmRevision) {
         cl.createArg().setValue("-r");
         cl.createArg().setValue(version.getName());
      }

      cl.createArg().setValue(url);
      cl.createArg().setValue(workingDirectory.getAbsolutePath());
      return cl;
   }
}

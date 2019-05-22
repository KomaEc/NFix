package org.apache.maven.scm.provider.svn.svnexe.command.status;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnStatusCommand extends AbstractStatusCommand implements SvnCommand {
   protected StatusScmResult executeStatusCommand(ScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      Commandline cl = createCommandLine((SvnScmProviderRepository)repo, fileSet);
      SvnStatusConsumer consumer = new SvnStatusConsumer(this.getLogger(), fileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      int exitCode;
      try {
         exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      } catch (CommandLineException var8) {
         throw new ScmException("Error while executing command.", var8);
      }

      return exitCode != 0 ? new StatusScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false) : new StatusScmResult(cl.toString(), consumer.getChangedFiles());
   }

   public static Commandline createCommandLine(SvnScmProviderRepository repository, ScmFileSet fileSet) {
      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(fileSet.getBasedir(), repository);
      cl.createArg().setValue("status");
      return cl;
   }
}

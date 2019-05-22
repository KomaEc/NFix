package org.apache.maven.scm.provider.svn.svnexe.command.diff;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.diff.AbstractDiffCommand;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.command.diff.SvnDiffConsumer;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnDiffCommand extends AbstractDiffCommand implements SvnCommand {
   protected DiffScmResult executeDiffCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion) throws ScmException {
      Commandline cl = createCommandLine((SvnScmProviderRepository)repo, fileSet.getBasedir(), startVersion, endVersion);
      SvnDiffConsumer consumer = new SvnDiffConsumer(this.getLogger(), fileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      int exitCode;
      try {
         exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      } catch (CommandLineException var10) {
         throw new ScmException("Error while executing command.", var10);
      }

      return exitCode != 0 ? new DiffScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false) : new DiffScmResult(cl.toString(), consumer.getChangedFiles(), consumer.getDifferences(), consumer.getPatch());
   }

   public static Commandline createCommandLine(SvnScmProviderRepository repository, File workingDirectory, ScmVersion startVersion, ScmVersion endVersion) {
      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(workingDirectory, repository);
      cl.createArg().setValue("diff");
      if (startVersion != null && StringUtils.isNotEmpty(startVersion.getName())) {
         cl.createArg().setValue("-r");
         if (endVersion != null && StringUtils.isNotEmpty(endVersion.getName())) {
            cl.createArg().setValue(startVersion.getName() + ":" + endVersion.getName());
         } else {
            cl.createArg().setValue(startVersion.getName());
         }
      }

      return cl;
   }
}

package org.apache.maven.scm.provider.starteam.command.status;

import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.starteam.command.StarteamCommand;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class StarteamStatusCommand extends AbstractStatusCommand implements StarteamCommand {
   protected StatusScmResult executeStatusCommand(ScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Working directory: " + fileSet.getBasedir().getAbsolutePath());
      }

      if (fileSet.getFileList().size() != 0) {
         throw new ScmException("This provider doesn't support checking status of a subsets of a directory");
      } else {
         StarteamScmProviderRepository repository = (StarteamScmProviderRepository)repo;
         StarteamStatusConsumer consumer = new StarteamStatusConsumer(this.getLogger(), fileSet.getBasedir());
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         Commandline cl = createCommandLine(repository, fileSet);
         int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
         return exitCode != 0 ? new StatusScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false) : new StatusScmResult(cl.toString(), consumer.getChangedFiles());
      }
   }

   public static Commandline createCommandLine(StarteamScmProviderRepository repo, ScmFileSet workingDirectory) {
      return StarteamCommandLineUtils.createStarteamCommandLine("hist", (List)null, workingDirectory, repo);
   }
}

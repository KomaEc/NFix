package org.apache.maven.scm.provider.git.gitexe.command.status;

import java.net.URI;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitStatusCommand extends AbstractStatusCommand implements GitCommand {
   protected StatusScmResult executeStatusCommand(ScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      Commandline clRevparse = createRevparseShowToplevelCommand(fileSet);
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      URI relativeRepositoryPath = null;
      int exitCode = GitCommandLineUtils.execute(clRevparse, stdout, stderr, this.getLogger());
      if (exitCode != 0) {
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("Could not resolve toplevel");
         }
      } else {
         relativeRepositoryPath = GitStatusConsumer.resolveURI(stdout.getOutput().trim(), fileSet.getBasedir().toURI());
      }

      Commandline cl = createCommandLine((GitScmProviderRepository)repo, fileSet);
      GitStatusConsumer consumer = new GitStatusConsumer(this.getLogger(), fileSet.getBasedir(), relativeRepositoryPath);
      stderr = new CommandLineUtils.StringStreamConsumer();
      exitCode = GitCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      if (exitCode != 0 && this.getLogger().isInfoEnabled()) {
         this.getLogger().info("nothing added to commit but untracked files present (use \"git add\" to track)");
      }

      return new StatusScmResult(cl.toString(), consumer.getChangedFiles());
   }

   public static Commandline createCommandLine(GitScmProviderRepository repository, ScmFileSet fileSet) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(fileSet.getBasedir(), "status");
      cl.addArguments(new String[]{"--porcelain", "."});
      return cl;
   }

   public static Commandline createRevparseShowToplevelCommand(ScmFileSet fileSet) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(fileSet.getBasedir(), "rev-parse");
      cl.addArguments(new String[]{"--show-toplevel"});
      return cl;
   }
}

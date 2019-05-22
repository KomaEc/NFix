package org.apache.maven.scm.provider.git.gitexe.command.list;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.list.AbstractListCommand;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitListCommand extends AbstractListCommand implements GitCommand {
   protected ListScmResult executeListCommand(ScmProviderRepository repo, ScmFileSet fileSet, boolean recursive, ScmVersion scmVersion) throws ScmException {
      GitScmProviderRepository repository = (GitScmProviderRepository)repo;
      if ("file".equals(repository.getFetchInfo().getProtocol()) && repository.getFetchInfo().getPath().indexOf(fileSet.getBasedir().getPath()) >= 0) {
         throw new ScmException("remote repository must not be the working directory");
      } else {
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         GitListConsumer consumer = new GitListConsumer(this.getLogger(), fileSet.getBasedir().getParentFile(), ScmFileStatus.CHECKED_IN);
         Commandline cl = createCommandLine(repository, fileSet.getBasedir());
         int exitCode = GitCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
         return exitCode != 0 ? new ListScmResult(cl.toString(), "The git-ls-files command failed.", stderr.getOutput(), false) : new ListScmResult(cl.toString(), consumer.getListedFiles());
      }
   }

   public static Commandline createCommandLine(GitScmProviderRepository repository, File workingDirectory) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "ls-files");
      return cl;
   }
}

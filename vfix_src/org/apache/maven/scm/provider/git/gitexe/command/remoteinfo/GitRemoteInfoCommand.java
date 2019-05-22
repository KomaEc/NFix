package org.apache.maven.scm.provider.git.gitexe.command.remoteinfo;

import java.io.File;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.remoteinfo.AbstractRemoteInfoCommand;
import org.apache.maven.scm.command.remoteinfo.RemoteInfoScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitRemoteInfoCommand extends AbstractRemoteInfoCommand implements GitCommand {
   public RemoteInfoScmResult executeRemoteInfoCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      GitScmProviderRepository gitRepository = (GitScmProviderRepository)repository;
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      Commandline clLsRemote = createCommandLine(gitRepository);
      GitRemoteInfoConsumer consumer = new GitRemoteInfoConsumer(this.getLogger(), clLsRemote.toString());
      int exitCode = GitCommandLineUtils.execute(clLsRemote, (StreamConsumer)consumer, stderr, this.getLogger());
      if (exitCode != 0) {
         throw new ScmException("unbale to execute ls-remote on " + gitRepository.getFetchUrl());
      } else {
         return consumer.getRemoteInfoScmResult();
      }
   }

   public static Commandline createCommandLine(GitScmProviderRepository repository) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine((File)null, "ls-remote");
      cl.setWorkingDirectory(System.getProperty("java.io.tmpdir"));
      String remoteUrl = repository.getPushUrl();
      cl.createArg().setValue(remoteUrl);
      return cl;
   }
}

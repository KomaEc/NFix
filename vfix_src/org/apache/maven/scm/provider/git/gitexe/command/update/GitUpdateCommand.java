package org.apache.maven.scm.provider.git.gitexe.command.update;

import java.io.File;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.update.AbstractUpdateCommand;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.command.update.UpdateScmResultWithRevision;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.apache.maven.scm.provider.git.gitexe.command.changelog.GitChangeLogCommand;
import org.apache.maven.scm.provider.git.gitexe.command.diff.GitDiffCommand;
import org.apache.maven.scm.provider.git.gitexe.command.diff.GitDiffRawConsumer;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitUpdateCommand extends AbstractUpdateCommand implements GitCommand {
   protected UpdateScmResult executeUpdateCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion scmVersion) throws ScmException {
      GitScmProviderRepository repository = (GitScmProviderRepository)repo;
      if ("file".equals(repository.getFetchInfo().getProtocol()) && repository.getFetchInfo().getPath().indexOf(fileSet.getBasedir().getPath()) >= 0) {
         throw new ScmException("remote repository must not be the working directory");
      } else {
         CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         Commandline clRev = createLatestRevisionCommandLine(repository, fileSet.getBasedir(), scmVersion);
         GitLatestRevisionCommandConsumer consumerRev = new GitLatestRevisionCommandConsumer(this.getLogger());
         int exitCode = GitCommandLineUtils.execute(clRev, (StreamConsumer)consumerRev, stderr, this.getLogger());
         if (exitCode != 0) {
            return new UpdateScmResult(clRev.toString(), "The git-log command failed.", stderr.getOutput(), false);
         } else {
            String origSha1 = consumerRev.getLatestRevision();
            Commandline cl = createCommandLine(repository, fileSet.getBasedir(), scmVersion);
            exitCode = GitCommandLineUtils.execute(cl, stdout, stderr, this.getLogger());
            if (exitCode != 0) {
               return new UpdateScmResult(cl.toString(), "The git-pull command failed.", stderr.getOutput(), false);
            } else {
               GitDiffRawConsumer diffRawConsumer = new GitDiffRawConsumer(this.getLogger());
               Commandline clDiffRaw = GitDiffCommand.createDiffRawCommandLine(fileSet.getBasedir(), origSha1);
               exitCode = GitCommandLineUtils.execute(clDiffRaw, (StreamConsumer)diffRawConsumer, stderr, this.getLogger());
               if (exitCode != 0) {
                  return new UpdateScmResult(clDiffRaw.toString(), "The git-diff --raw command failed.", stderr.getOutput(), false);
               } else {
                  consumerRev = new GitLatestRevisionCommandConsumer(this.getLogger());
                  exitCode = GitCommandLineUtils.execute(clRev, (StreamConsumer)consumerRev, stderr, this.getLogger());
                  if (exitCode != 0) {
                     return new UpdateScmResult(clRev.toString(), "The git-log command failed.", stderr.getOutput(), false);
                  } else {
                     String latestRevision = consumerRev.getLatestRevision();
                     return new UpdateScmResultWithRevision(cl.toString(), diffRawConsumer.getChangedFiles(), latestRevision);
                  }
               }
            }
         }
      }
   }

   protected ChangeLogCommand getChangeLogCommand() {
      GitChangeLogCommand changelogCmd = new GitChangeLogCommand();
      changelogCmd.setLogger(this.getLogger());
      return changelogCmd;
   }

   public static Commandline createCommandLine(GitScmProviderRepository repository, File workingDirectory, ScmVersion scmVersion) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "pull");
      cl.createArg().setLine(repository.getFetchUrl());
      if (scmVersion instanceof ScmBranch) {
         cl.createArg().setLine(scmVersion.getName());
      } else {
         cl.createArg().setLine("master");
      }

      return cl;
   }

   public static Commandline createLatestRevisionCommandLine(GitScmProviderRepository repository, File workingDirectory, ScmVersion scmVersion) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "log");
      cl.createArg().setValue("-n1");
      cl.createArg().setValue("--date-order");
      if (scmVersion != null && scmVersion instanceof ScmBranch && scmVersion.getName() != null && scmVersion.getName().length() > 0) {
         cl.createArg().setValue(scmVersion.getName());
      } else {
         cl.createArg().setValue("master");
      }

      return cl;
   }
}

package org.apache.maven.scm.provider.git.gitexe.command.branch;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.branch.AbstractBranchCommand;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.apache.maven.scm.provider.git.gitexe.command.list.GitListCommand;
import org.apache.maven.scm.provider.git.gitexe.command.list.GitListConsumer;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitBranchCommand extends AbstractBranchCommand implements GitCommand {
   public ScmResult executeBranchCommand(ScmProviderRepository repo, ScmFileSet fileSet, String branch, String message) throws ScmException {
      if (branch != null && !StringUtils.isEmpty(branch.trim())) {
         if (!fileSet.getFileList().isEmpty()) {
            throw new ScmException("This provider doesn't support branching subsets of a directory");
         } else {
            GitScmProviderRepository repository = (GitScmProviderRepository)repo;
            Commandline cl = createCommandLine(repository, fileSet.getBasedir(), branch);
            CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
            CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
            int exitCode = GitCommandLineUtils.execute(cl, stdout, stderr, this.getLogger());
            if (exitCode != 0) {
               return new BranchScmResult(cl.toString(), "The git-branch command failed.", stderr.getOutput(), false);
            } else {
               if (repo.isPushChanges()) {
                  Commandline clPush = createPushCommandLine(repository, fileSet, branch);
                  exitCode = GitCommandLineUtils.execute(clPush, stdout, stderr, this.getLogger());
                  if (exitCode != 0) {
                     return new BranchScmResult(clPush.toString(), "The git-push command failed.", stderr.getOutput(), false);
                  }
               }

               GitListConsumer listConsumer = new GitListConsumer(this.getLogger(), fileSet.getBasedir(), ScmFileStatus.TAGGED);
               Commandline clList = GitListCommand.createCommandLine(repository, fileSet.getBasedir());
               exitCode = GitCommandLineUtils.execute(clList, (StreamConsumer)listConsumer, stderr, this.getLogger());
               return exitCode != 0 ? new BranchScmResult(clList.toString(), "The git-ls-files command failed.", stderr.getOutput(), false) : new BranchScmResult(cl.toString(), listConsumer.getListedFiles());
            }
         }
      } else {
         throw new ScmException("branch name must be specified");
      }
   }

   public static Commandline createCommandLine(GitScmProviderRepository repository, File workingDirectory, String branch) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "branch");
      cl.createArg().setValue(branch);
      return cl;
   }

   public static Commandline createPushCommandLine(GitScmProviderRepository repository, ScmFileSet fileSet, String branch) throws ScmException {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(fileSet.getBasedir(), "push");
      cl.createArg().setValue(repository.getPushUrl());
      cl.createArg().setValue("refs/heads/" + branch);
      return cl;
   }

   public static String getCurrentBranch(ScmLogger logger, GitScmProviderRepository repository, ScmFileSet fileSet) throws ScmException {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(fileSet.getBasedir(), "symbolic-ref");
      cl.createArg().setValue("HEAD");
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      GitCurrentBranchConsumer cbConsumer = new GitCurrentBranchConsumer(logger);
      int exitCode = GitCommandLineUtils.execute(cl, (StreamConsumer)cbConsumer, stderr, logger);
      if (exitCode != 0) {
         throw new ScmException("Detecting the current branch failed: " + stderr.getOutput());
      } else {
         return cbConsumer.getBranchName();
      }
   }
}

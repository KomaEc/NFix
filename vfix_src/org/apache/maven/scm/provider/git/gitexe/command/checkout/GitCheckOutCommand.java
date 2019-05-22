package org.apache.maven.scm.provider.git.gitexe.command.checkout;

import java.io.File;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmTag;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkout.AbstractCheckOutCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.remoteinfo.RemoteInfoScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.apache.maven.scm.provider.git.gitexe.command.list.GitListCommand;
import org.apache.maven.scm.provider.git.gitexe.command.list.GitListConsumer;
import org.apache.maven.scm.provider.git.gitexe.command.remoteinfo.GitRemoteInfoCommand;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitCheckOutCommand extends AbstractCheckOutCommand implements GitCommand {
   protected CheckOutScmResult executeCheckOutCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version, boolean recursive) throws ScmException {
      GitScmProviderRepository repository = (GitScmProviderRepository)repo;
      if ("file".equals(repository.getFetchInfo().getProtocol()) && repository.getFetchInfo().getPath().indexOf(fileSet.getBasedir().getPath()) >= 0) {
         throw new ScmException("remote repository must not be the working directory");
      } else {
         CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         String lastCommandLine = "git-nothing-to-do";
         int exitCode;
         if (!fileSet.getBasedir().exists() || !(new File(fileSet.getBasedir(), ".git")).exists()) {
            if (fileSet.getBasedir().exists()) {
               fileSet.getBasedir().delete();
            }

            Commandline clClone = this.createCloneCommand(repository, fileSet.getBasedir(), version);
            exitCode = GitCommandLineUtils.execute(clClone, stdout, stderr, this.getLogger());
            if (exitCode != 0) {
               return new CheckOutScmResult(clClone.toString(), "The git-clone command failed.", stderr.getOutput(), false);
            }

            lastCommandLine = clClone.toString();
         }

         GitRemoteInfoCommand gitRemoteInfoCommand = new GitRemoteInfoCommand();
         gitRemoteInfoCommand.setLogger(this.getLogger());
         RemoteInfoScmResult result = gitRemoteInfoCommand.executeRemoteInfoCommand(repository, (ScmFileSet)null, (CommandParameters)null);
         Commandline clCheckout;
         if (fileSet.getBasedir().exists() && (new File(fileSet.getBasedir(), ".git")).exists() && result.getBranches().size() > 0) {
            Commandline clPull = this.createPullCommand(repository, fileSet.getBasedir(), version);
            exitCode = GitCommandLineUtils.execute(clPull, stdout, stderr, this.getLogger());
            if (exitCode != 0) {
               return new CheckOutScmResult(clPull.toString(), "The git-pull command failed.", stderr.getOutput(), false);
            }

            lastCommandLine = clPull.toString();
            clCheckout = createCommandLine(repository, fileSet.getBasedir(), version);
            exitCode = GitCommandLineUtils.execute(clCheckout, stdout, stderr, this.getLogger());
            if (exitCode != 0) {
               return new CheckOutScmResult(clCheckout.toString(), "The git-checkout command failed.", stderr.getOutput(), false);
            }

            lastCommandLine = clCheckout.toString();
         }

         GitListConsumer listConsumer = new GitListConsumer(this.getLogger(), fileSet.getBasedir(), ScmFileStatus.CHECKED_IN);
         clCheckout = GitListCommand.createCommandLine(repository, fileSet.getBasedir());
         exitCode = GitCommandLineUtils.execute(clCheckout, (StreamConsumer)listConsumer, stderr, this.getLogger());
         return exitCode != 0 ? new CheckOutScmResult(clCheckout.toString(), "The git-ls-files command failed.", stderr.getOutput(), false) : new CheckOutScmResult(lastCommandLine, listConsumer.getListedFiles());
      }
   }

   public static Commandline createCommandLine(GitScmProviderRepository repository, File workingDirectory, ScmVersion version) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "checkout");
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         cl.createArg().setValue(version.getName());
      }

      return cl;
   }

   private Commandline createCloneCommand(GitScmProviderRepository repository, File workingDirectory, ScmVersion version) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory.getParentFile(), "clone");
      if (version != null && version instanceof ScmBranch) {
         cl.createArg().setValue("--branch");
         cl.createArg().setValue(version.getName());
      }

      cl.createArg().setValue(repository.getFetchUrl());
      cl.createArg().setFile(workingDirectory);
      return cl;
   }

   private Commandline createPullCommand(GitScmProviderRepository repository, File workingDirectory, ScmVersion version) {
      Commandline cl;
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         if (version instanceof ScmTag) {
            cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "fetch");
            cl.createArg().setValue(repository.getFetchUrl());
         } else {
            cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "pull");
            cl.createArg().setValue(repository.getFetchUrl());
            cl.createArg().setValue(version.getName() + ":" + version.getName());
         }
      } else {
         cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "pull");
         cl.createArg().setValue(repository.getFetchUrl());
         cl.createArg().setValue("master");
      }

      return cl;
   }
}

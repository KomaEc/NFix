package org.apache.maven.scm.provider.git.gitexe.command.checkin;

import com.gzoltar.shaded.org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkin.AbstractCheckInCommand;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.apache.maven.scm.provider.git.gitexe.command.add.GitAddCommand;
import org.apache.maven.scm.provider.git.gitexe.command.branch.GitBranchCommand;
import org.apache.maven.scm.provider.git.gitexe.command.status.GitStatusCommand;
import org.apache.maven.scm.provider.git.gitexe.command.status.GitStatusConsumer;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.apache.maven.scm.provider.git.util.GitUtil;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitCheckInCommand extends AbstractCheckInCommand implements GitCommand {
   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message, ScmVersion version) throws ScmException {
      GitScmProviderRepository repository = (GitScmProviderRepository)repo;
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
      File messageFile = FileUtils.createTempFile("maven-scm-", ".commit", (File)null);

      try {
         FileUtils.fileWrite(messageFile.getAbsolutePath(), message);
      } catch (IOException var32) {
         return new CheckInScmResult((String)null, "Error while making a temporary file for the commit message: " + var32.getMessage(), (String)null, false);
      }

      try {
         Commandline clAdd;
         int exitCode;
         if (!fileSet.getFileList().isEmpty()) {
            clAdd = GitAddCommand.createCommandLine(fileSet.getBasedir(), fileSet.getFileList());
            exitCode = GitCommandLineUtils.execute(clAdd, stdout, stderr, this.getLogger());
            if (exitCode != 0) {
               CheckInScmResult var34 = new CheckInScmResult(clAdd.toString(), "The git-add command failed.", stderr.getOutput(), false);
               return var34;
            }
         }

         clAdd = GitStatusCommand.createRevparseShowToplevelCommand(fileSet);
         stdout = new CommandLineUtils.StringStreamConsumer();
         stderr = new CommandLineUtils.StringStreamConsumer();
         URI relativeRepositoryPath = null;
         exitCode = GitCommandLineUtils.execute(clAdd, stdout, stderr, this.getLogger());
         if (exitCode != 0) {
            if (this.getLogger().isInfoEnabled()) {
               this.getLogger().info("Could not resolve toplevel");
            }
         } else {
            relativeRepositoryPath = GitStatusConsumer.resolveURI(stdout.getOutput().trim(), fileSet.getBasedir().toURI());
         }

         Commandline clStatus = GitStatusCommand.createCommandLine(repository, fileSet);
         GitStatusConsumer statusConsumer = new GitStatusConsumer(this.getLogger(), fileSet.getBasedir(), relativeRepositoryPath);
         exitCode = GitCommandLineUtils.execute(clStatus, (StreamConsumer)statusConsumer, stderr, this.getLogger());
         if (exitCode != 0 && this.getLogger().isInfoEnabled()) {
            this.getLogger().info("nothing added to commit but untracked files present (use \"git add\" to track)");
         }

         if (statusConsumer.getChangedFiles().isEmpty()) {
            CheckInScmResult var35 = new CheckInScmResult((String)null, statusConsumer.getChangedFiles());
            return var35;
         } else {
            Commandline clCommit = createCommitCommandLine(repository, fileSet, messageFile);
            exitCode = GitCommandLineUtils.execute(clCommit, stdout, stderr, this.getLogger());
            if (exitCode != 0) {
               CheckInScmResult var37 = new CheckInScmResult(clCommit.toString(), "The git-commit command failed.", stderr.getOutput(), false);
               return var37;
            } else {
               CheckInScmResult var38;
               if (repo.isPushChanges()) {
                  Commandline cl = createPushCommandLine(this.getLogger(), repository, fileSet, version);
                  exitCode = GitCommandLineUtils.execute(cl, stdout, stderr, this.getLogger());
                  if (exitCode != 0) {
                     var38 = new CheckInScmResult(cl.toString(), "The git-push command failed.", stderr.getOutput(), false);
                     return var38;
                  }
               }

               List<ScmFile> checkedInFiles = new ArrayList(statusConsumer.getChangedFiles().size());
               Iterator i$ = statusConsumer.getChangedFiles().iterator();

               while(i$.hasNext()) {
                  ScmFile changedFile = (ScmFile)i$.next();
                  ScmFile scmfile = new ScmFile(changedFile.getPath(), ScmFileStatus.CHECKED_IN);
                  if (fileSet.getFileList().isEmpty()) {
                     checkedInFiles.add(scmfile);
                  } else {
                     Iterator i$ = fileSet.getFileList().iterator();

                     while(i$.hasNext()) {
                        File f = (File)i$.next();
                        if (FilenameUtils.separatorsToUnix(f.getPath()).equals(scmfile.getPath())) {
                           checkedInFiles.add(scmfile);
                        }
                     }
                  }
               }

               var38 = new CheckInScmResult(clCommit.toString(), checkedInFiles);
               return var38;
            }
         }
      } finally {
         try {
            FileUtils.forceDelete(messageFile);
         } catch (IOException var31) {
         }

      }
   }

   public static Commandline createPushCommandLine(ScmLogger logger, GitScmProviderRepository repository, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(fileSet.getBasedir(), "push");
      String branch = GitBranchCommand.getCurrentBranch(logger, repository, fileSet);
      if (branch != null && branch.length() != 0) {
         cl.createArg().setValue(repository.getPushUrl());
         cl.createArg().setValue("refs/heads/" + branch + ":" + "refs/heads/" + branch);
         return cl;
      } else {
         throw new ScmException("Could not detect the current branch. Don't know where I should push to!");
      }
   }

   public static Commandline createCommitCommandLine(GitScmProviderRepository repository, ScmFileSet fileSet, File messageFile) throws ScmException {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(fileSet.getBasedir(), "commit");
      cl.createArg().setValue("--verbose");
      cl.createArg().setValue("-F");
      cl.createArg().setValue(messageFile.getAbsolutePath());
      if (fileSet.getFileList().isEmpty()) {
         cl.createArg().setValue("-a");
      } else {
         GitCommandLineUtils.addTarget(cl, fileSet.getFileList());
      }

      if (GitUtil.getSettings().isCommitNoVerify()) {
         cl.createArg().setValue("--no-verify");
      }

      return cl;
   }
}

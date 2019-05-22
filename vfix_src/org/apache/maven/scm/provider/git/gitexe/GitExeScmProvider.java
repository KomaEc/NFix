package org.apache.maven.scm.provider.git.gitexe;

import java.io.File;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.info.InfoItem;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.provider.git.AbstractGitScmProvider;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.add.GitAddCommand;
import org.apache.maven.scm.provider.git.gitexe.command.blame.GitBlameCommand;
import org.apache.maven.scm.provider.git.gitexe.command.branch.GitBranchCommand;
import org.apache.maven.scm.provider.git.gitexe.command.changelog.GitChangeLogCommand;
import org.apache.maven.scm.provider.git.gitexe.command.checkin.GitCheckInCommand;
import org.apache.maven.scm.provider.git.gitexe.command.checkout.GitCheckOutCommand;
import org.apache.maven.scm.provider.git.gitexe.command.diff.GitDiffCommand;
import org.apache.maven.scm.provider.git.gitexe.command.info.GitInfoCommand;
import org.apache.maven.scm.provider.git.gitexe.command.list.GitListCommand;
import org.apache.maven.scm.provider.git.gitexe.command.remoteinfo.GitRemoteInfoCommand;
import org.apache.maven.scm.provider.git.gitexe.command.remove.GitRemoveCommand;
import org.apache.maven.scm.provider.git.gitexe.command.status.GitStatusCommand;
import org.apache.maven.scm.provider.git.gitexe.command.tag.GitTagCommand;
import org.apache.maven.scm.provider.git.gitexe.command.update.GitUpdateCommand;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;

public class GitExeScmProvider extends AbstractGitScmProvider {
   protected GitCommand getAddCommand() {
      return new GitAddCommand();
   }

   protected GitCommand getBranchCommand() {
      return new GitBranchCommand();
   }

   protected GitCommand getChangeLogCommand() {
      return new GitChangeLogCommand();
   }

   protected GitCommand getCheckInCommand() {
      return new GitCheckInCommand();
   }

   protected GitCommand getCheckOutCommand() {
      return new GitCheckOutCommand();
   }

   protected GitCommand getDiffCommand() {
      return new GitDiffCommand();
   }

   protected GitCommand getExportCommand() {
      return null;
   }

   protected GitCommand getRemoveCommand() {
      return new GitRemoveCommand();
   }

   protected GitCommand getStatusCommand() {
      return new GitStatusCommand();
   }

   protected GitCommand getTagCommand() {
      return new GitTagCommand();
   }

   protected GitCommand getUpdateCommand() {
      return new GitUpdateCommand();
   }

   protected GitCommand getListCommand() {
      return new GitListCommand();
   }

   public GitCommand getInfoCommand() {
      return new GitInfoCommand();
   }

   protected GitCommand getBlameCommand() {
      return new GitBlameCommand();
   }

   protected GitCommand getRemoteInfoCommand() {
      return new GitRemoteInfoCommand();
   }

   protected String getRepositoryURL(File path) throws ScmException {
      InfoScmResult result = this.info(new GitScmProviderRepository(path.getPath()), new ScmFileSet(path), (CommandParameters)null);
      if (result.getInfoItems().size() != 1) {
         throw new ScmRepositoryException("Cannot find URL: " + (result.getInfoItems().size() == 0 ? "no" : "multiple") + " items returned by the info command");
      } else {
         return ((InfoItem)result.getInfoItems().get(0)).getURL();
      }
   }
}

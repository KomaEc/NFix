package org.apache.maven.scm.provider.git;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.command.remoteinfo.RemoteInfoScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.scm.repository.UnknownRepositoryStructure;

public abstract class AbstractGitScmProvider extends AbstractScmProvider {
   public String getScmSpecificFilename() {
      return ".git";
   }

   public ScmProviderRepository makeProviderScmRepository(String scmSpecificUrl, char delimiter) throws ScmRepositoryException {
      try {
         AbstractGitScmProvider.ScmUrlParserResult result = this.parseScmUrl(scmSpecificUrl, delimiter);
         if (result.messages.size() > 0) {
            throw new ScmRepositoryException("The scm url " + scmSpecificUrl + " is invalid.", result.messages);
         } else {
            return result.repository;
         }
      } catch (ScmException var4) {
         throw new ScmRepositoryException("Error creating the scm repository", var4);
      }
   }

   public ScmProviderRepository makeProviderScmRepository(File path) throws ScmRepositoryException, UnknownRepositoryStructure {
      if (path == null) {
         throw new NullPointerException("Path argument is null");
      } else if (!path.isDirectory()) {
         throw new ScmRepositoryException(path.getAbsolutePath() + " isn't a valid directory.");
      } else if (!(new File(path, ".git")).exists()) {
         throw new ScmRepositoryException(path.getAbsolutePath() + " isn't a git checkout directory.");
      } else {
         try {
            return this.makeProviderScmRepository(this.getRepositoryURL(path), ':');
         } catch (ScmException var3) {
            throw new ScmRepositoryException("Error creating the scm repository", var3);
         }
      }
   }

   protected abstract String getRepositoryURL(File var1) throws ScmException;

   public List<String> validateScmUrl(String scmSpecificUrl, char delimiter) {
      Object messages = new ArrayList();

      try {
         this.makeProviderScmRepository(scmSpecificUrl, delimiter);
      } catch (ScmRepositoryException var5) {
         messages = var5.getValidationMessages();
      }

      return (List)messages;
   }

   public String getScmType() {
      return "git";
   }

   private AbstractGitScmProvider.ScmUrlParserResult parseScmUrl(String scmSpecificUrl, char delimiter) throws ScmException {
      AbstractGitScmProvider.ScmUrlParserResult result = new AbstractGitScmProvider.ScmUrlParserResult();
      result.repository = new GitScmProviderRepository(scmSpecificUrl);
      return result;
   }

   protected abstract GitCommand getAddCommand();

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (AddScmResult)this.executeCommand(this.getAddCommand(), repository, fileSet, parameters);
   }

   protected abstract GitCommand getBranchCommand();

   protected BranchScmResult branch(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (BranchScmResult)this.executeCommand(this.getBranchCommand(), repository, fileSet, parameters);
   }

   protected abstract GitCommand getChangeLogCommand();

   public ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (ChangeLogScmResult)this.executeCommand(this.getChangeLogCommand(), repository, fileSet, parameters);
   }

   protected abstract GitCommand getCheckInCommand();

   public CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (CheckInScmResult)this.executeCommand(this.getCheckInCommand(), repository, fileSet, parameters);
   }

   protected abstract GitCommand getCheckOutCommand();

   public CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (CheckOutScmResult)this.executeCommand(this.getCheckOutCommand(), repository, fileSet, parameters);
   }

   protected abstract GitCommand getDiffCommand();

   public DiffScmResult diff(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (DiffScmResult)this.executeCommand(this.getDiffCommand(), repository, fileSet, parameters);
   }

   protected abstract GitCommand getExportCommand();

   protected ExportScmResult export(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (ExportScmResult)this.executeCommand(this.getExportCommand(), repository, fileSet, parameters);
   }

   protected abstract GitCommand getRemoveCommand();

   public RemoveScmResult remove(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (RemoveScmResult)this.executeCommand(this.getRemoveCommand(), repository, fileSet, parameters);
   }

   protected abstract GitCommand getStatusCommand();

   public StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (StatusScmResult)this.executeCommand(this.getStatusCommand(), repository, fileSet, parameters);
   }

   protected abstract GitCommand getTagCommand();

   public TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (TagScmResult)this.executeCommand(this.getTagCommand(), repository, fileSet, parameters);
   }

   protected abstract GitCommand getUpdateCommand();

   public UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (UpdateScmResult)this.executeCommand(this.getUpdateCommand(), repository, fileSet, parameters);
   }

   protected ScmResult executeCommand(GitCommand command, ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      command.setLogger(this.getLogger());
      return command.execute(repository, fileSet, parameters);
   }

   protected abstract GitCommand getListCommand();

   public ListScmResult list(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      GitCommand cmd = this.getListCommand();
      return (ListScmResult)this.executeCommand(cmd, repository, fileSet, parameters);
   }

   protected abstract GitCommand getInfoCommand();

   public InfoScmResult info(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      GitCommand cmd = this.getInfoCommand();
      return (InfoScmResult)this.executeCommand(cmd, repository, fileSet, parameters);
   }

   protected BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      GitCommand cmd = this.getBlameCommand();
      return (BlameScmResult)this.executeCommand(cmd, repository, fileSet, parameters);
   }

   protected abstract GitCommand getBlameCommand();

   public RemoteInfoScmResult remoteInfo(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      GitCommand cmd = this.getRemoteInfoCommand();
      return (RemoteInfoScmResult)this.executeCommand(cmd, repository, fileSet, parameters);
   }

   protected abstract GitCommand getRemoteInfoCommand();

   private static class ScmUrlParserResult {
      private List<String> messages;
      private ScmProviderRepository repository;

      private ScmUrlParserResult() {
         this.messages = new ArrayList();
      }

      // $FF: synthetic method
      ScmUrlParserResult(Object x0) {
         this();
      }
   }
}

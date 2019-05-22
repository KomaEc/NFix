package org.apache.maven.scm.provider.hg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.hg.command.add.HgAddCommand;
import org.apache.maven.scm.provider.hg.command.blame.HgBlameCommand;
import org.apache.maven.scm.provider.hg.command.branch.HgBranchCommand;
import org.apache.maven.scm.provider.hg.command.changelog.HgChangeLogCommand;
import org.apache.maven.scm.provider.hg.command.checkin.HgCheckInCommand;
import org.apache.maven.scm.provider.hg.command.checkout.HgCheckOutCommand;
import org.apache.maven.scm.provider.hg.command.diff.HgDiffCommand;
import org.apache.maven.scm.provider.hg.command.info.HgInfoCommand;
import org.apache.maven.scm.provider.hg.command.inventory.HgListCommand;
import org.apache.maven.scm.provider.hg.command.remove.HgRemoveCommand;
import org.apache.maven.scm.provider.hg.command.status.HgStatusCommand;
import org.apache.maven.scm.provider.hg.command.tag.HgTagCommand;
import org.apache.maven.scm.provider.hg.command.update.HgUpdateCommand;
import org.apache.maven.scm.provider.hg.repository.HgScmProviderRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.scm.repository.UnknownRepositoryStructure;

public class HgScmProvider extends AbstractScmProvider {
   public String getScmSpecificFilename() {
      return ".hg";
   }

   public ScmProviderRepository makeProviderScmRepository(String scmSpecificUrl, char delimiter) throws ScmRepositoryException {
      HgScmProvider.HgUrlParserResult result = this.parseScmUrl(scmSpecificUrl);
      if (result.messages.size() > 0) {
         throw new ScmRepositoryException("The scm url is invalid.", result.messages);
      } else {
         return result.repository;
      }
   }

   private HgScmProvider.HgUrlParserResult parseScmUrl(String scmSpecificUrl) {
      HgScmProvider.HgUrlParserResult result = new HgScmProvider.HgUrlParserResult();
      String url = scmSpecificUrl;
      if (scmSpecificUrl.startsWith("file")) {
         if (!scmSpecificUrl.startsWith("file:///") && !scmSpecificUrl.startsWith("file://localhost/")) {
            result.messages.add("An hg 'file' url must be on the form 'file:///' or 'file://localhost/'.");
            return result;
         }
      } else if (scmSpecificUrl.startsWith("https")) {
         if (!scmSpecificUrl.startsWith("https://")) {
            result.messages.add("An hg 'http' url must be on the form 'https://'.");
            return result;
         }
      } else if (scmSpecificUrl.startsWith("http")) {
         if (!scmSpecificUrl.startsWith("http://")) {
            result.messages.add("An hg 'http' url must be on the form 'http://'.");
            return result;
         }
      } else {
         try {
            new File(url);
         } catch (Throwable var5) {
            result.messages.add("The filename provided is not valid");
            return result;
         }
      }

      result.repository = new HgScmProviderRepository(scmSpecificUrl);
      return result;
   }

   public ScmProviderRepository makeProviderScmRepository(File path) throws ScmRepositoryException, UnknownRepositoryStructure {
      if (path != null && path.isDirectory()) {
         File hgDir = new File(path, ".hg");
         if (!hgDir.exists()) {
            throw new ScmRepositoryException(path.getAbsolutePath() + " isn't a hg directory.");
         } else {
            return this.makeProviderScmRepository(path.getAbsolutePath(), ':');
         }
      } else {
         throw new ScmRepositoryException(path.getAbsolutePath() + " isn't a valid directory.");
      }
   }

   public List<String> validateScmUrl(String scmSpecificUrl, char delimiter) {
      HgScmProvider.HgUrlParserResult result = this.parseScmUrl(scmSpecificUrl);
      return result.messages;
   }

   public String getScmType() {
      return "hg";
   }

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgAddCommand command = new HgAddCommand();
      command.setLogger(this.getLogger());
      return (AddScmResult)command.execute(repository, fileSet, parameters);
   }

   public ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgChangeLogCommand command = new HgChangeLogCommand();
      command.setLogger(this.getLogger());
      return (ChangeLogScmResult)command.execute(repository, fileSet, parameters);
   }

   public CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgCheckInCommand command = new HgCheckInCommand();
      command.setLogger(this.getLogger());
      return (CheckInScmResult)command.execute(repository, fileSet, parameters);
   }

   public CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgCheckOutCommand command = new HgCheckOutCommand();
      command.setLogger(this.getLogger());
      return (CheckOutScmResult)command.execute(repository, fileSet, parameters);
   }

   public TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgTagCommand command = new HgTagCommand();
      command.setLogger(this.getLogger());
      return (TagScmResult)command.execute(repository, fileSet, parameters);
   }

   public DiffScmResult diff(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgDiffCommand command = new HgDiffCommand();
      command.setLogger(this.getLogger());
      return (DiffScmResult)command.execute(repository, fileSet, parameters);
   }

   public RemoveScmResult remove(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgRemoveCommand command = new HgRemoveCommand();
      command.setLogger(this.getLogger());
      return (RemoveScmResult)command.execute(repository, fileSet, parameters);
   }

   public StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgStatusCommand command = new HgStatusCommand();
      command.setLogger(this.getLogger());
      return (StatusScmResult)command.execute(repository, fileSet, parameters);
   }

   public UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgUpdateCommand command = new HgUpdateCommand();
      command.setLogger(this.getLogger());
      return (UpdateScmResult)command.execute(repository, fileSet, parameters);
   }

   protected BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgBlameCommand command = new HgBlameCommand();
      command.setLogger(this.getLogger());
      return (BlameScmResult)command.execute(repository, fileSet, parameters);
   }

   public BranchScmResult branch(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgBranchCommand command = new HgBranchCommand();
      command.setLogger(this.getLogger());
      return (BranchScmResult)command.execute(repository, fileSet, parameters);
   }

   protected ListScmResult list(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgListCommand hgListCommand = new HgListCommand();
      hgListCommand.setLogger(this.getLogger());
      return (ListScmResult)hgListCommand.executeCommand(repository, fileSet, parameters);
   }

   public InfoScmResult info(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      HgInfoCommand infoCommand = new HgInfoCommand();
      infoCommand.setLogger(this.getLogger());
      return (InfoScmResult)infoCommand.execute(repository, fileSet, parameters);
   }

   private static class HgUrlParserResult {
      private List<String> messages;
      private ScmProviderRepository repository;

      private HgUrlParserResult() {
         this.messages = new ArrayList();
      }

      // $FF: synthetic method
      HgUrlParserResult(Object x0) {
         this();
      }
   }
}

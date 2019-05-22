package org.apache.maven.scm.provider.bazaar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.bazaar.command.add.BazaarAddCommand;
import org.apache.maven.scm.provider.bazaar.command.blame.BazaarBlameCommand;
import org.apache.maven.scm.provider.bazaar.command.changelog.BazaarChangeLogCommand;
import org.apache.maven.scm.provider.bazaar.command.checkin.BazaarCheckInCommand;
import org.apache.maven.scm.provider.bazaar.command.checkout.BazaarCheckOutCommand;
import org.apache.maven.scm.provider.bazaar.command.diff.BazaarDiffCommand;
import org.apache.maven.scm.provider.bazaar.command.remove.BazaarRemoveCommand;
import org.apache.maven.scm.provider.bazaar.command.status.BazaarStatusCommand;
import org.apache.maven.scm.provider.bazaar.command.tag.BazaarTagCommand;
import org.apache.maven.scm.provider.bazaar.command.update.BazaarUpdateCommand;
import org.apache.maven.scm.provider.bazaar.repository.BazaarScmProviderRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.scm.repository.UnknownRepositoryStructure;

public class BazaarScmProvider extends AbstractScmProvider {
   public String getScmSpecificFilename() {
      return ".bzr";
   }

   public ScmProviderRepository makeProviderScmRepository(String scmSpecificUrl, char delimiter) throws ScmRepositoryException {
      return new BazaarScmProviderRepository(scmSpecificUrl);
   }

   public ScmProviderRepository makeProviderScmRepository(File path) throws ScmRepositoryException, UnknownRepositoryStructure {
      if (path != null && path.isDirectory()) {
         File bzrDir = new File(path, ".bzr");
         if (!bzrDir.exists()) {
            throw new ScmRepositoryException(path.getAbsolutePath() + " isn't a bazaar directory.");
         } else {
            return this.makeProviderScmRepository("file:///" + path.getAbsolutePath(), ':');
         }
      } else {
         throw new ScmRepositoryException(path.getAbsolutePath() + " isn't a valid directory.");
      }
   }

   public List<String> validateScmUrl(String scmSpecificUrl, char delimiter) {
      List<String> errorMessages = new ArrayList();
      String[] checkCmd = new String[]{"check", scmSpecificUrl};

      try {
         File tmpDir = new File(System.getProperty("java.io.tmpdir"));
         ScmResult result = BazaarUtils.execute(tmpDir, checkCmd);
         if (!result.isSuccess()) {
            errorMessages.add(result.getCommandOutput());
            errorMessages.add(result.getProviderMessage());
         }
      } catch (ScmException var7) {
         errorMessages.add(var7.getMessage());
      }

      return errorMessages;
   }

   public String getScmType() {
      return "bazaar";
   }

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      BazaarAddCommand command = new BazaarAddCommand();
      command.setLogger(this.getLogger());
      return (AddScmResult)command.execute(repository, fileSet, parameters);
   }

   public ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      BazaarChangeLogCommand command = new BazaarChangeLogCommand();
      command.setLogger(this.getLogger());
      return (ChangeLogScmResult)command.execute(repository, fileSet, parameters);
   }

   public CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      BazaarCheckInCommand command = new BazaarCheckInCommand();
      command.setLogger(this.getLogger());
      return (CheckInScmResult)command.execute(repository, fileSet, parameters);
   }

   public CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      BazaarCheckOutCommand command = new BazaarCheckOutCommand();
      command.setLogger(this.getLogger());
      return (CheckOutScmResult)command.execute(repository, fileSet, parameters);
   }

   public DiffScmResult diff(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      BazaarDiffCommand command = new BazaarDiffCommand();
      command.setLogger(this.getLogger());
      return (DiffScmResult)command.execute(repository, fileSet, parameters);
   }

   public RemoveScmResult remove(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      BazaarRemoveCommand command = new BazaarRemoveCommand();
      command.setLogger(this.getLogger());
      return (RemoveScmResult)command.execute(repository, fileSet, parameters);
   }

   public StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      BazaarStatusCommand command = new BazaarStatusCommand();
      command.setLogger(this.getLogger());
      return (StatusScmResult)command.execute(repository, fileSet, parameters);
   }

   public TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      BazaarTagCommand command = new BazaarTagCommand();
      command.setLogger(this.getLogger());
      return (TagScmResult)command.execute(repository, fileSet, parameters);
   }

   public UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      BazaarUpdateCommand command = new BazaarUpdateCommand();
      command.setLogger(this.getLogger());
      return (UpdateScmResult)command.execute(repository, fileSet, parameters);
   }

   protected BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      BazaarBlameCommand command = new BazaarBlameCommand();
      command.setLogger(this.getLogger());
      return (BlameScmResult)command.execute(repository, fileSet, parameters);
   }
}

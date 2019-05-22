package org.apache.maven.scm.provider.clearcase;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.command.blame.ClearCaseBlameCommand;
import org.apache.maven.scm.provider.clearcase.command.changelog.ClearCaseChangeLogCommand;
import org.apache.maven.scm.provider.clearcase.command.checkin.ClearCaseCheckInCommand;
import org.apache.maven.scm.provider.clearcase.command.checkout.ClearCaseCheckOutCommand;
import org.apache.maven.scm.provider.clearcase.command.edit.ClearCaseEditCommand;
import org.apache.maven.scm.provider.clearcase.command.status.ClearCaseStatusCommand;
import org.apache.maven.scm.provider.clearcase.command.tag.ClearCaseTagCommand;
import org.apache.maven.scm.provider.clearcase.command.update.ClearCaseUpdateCommand;
import org.apache.maven.scm.provider.clearcase.repository.ClearCaseScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.util.ClearCaseUtil;
import org.apache.maven.scm.providers.clearcase.settings.Settings;
import org.apache.maven.scm.repository.ScmRepositoryException;

public class ClearCaseScmProvider extends AbstractScmProvider {
   private Settings settings;

   public ScmProviderRepository makeProviderScmRepository(String scmSpecificUrl, char delimiter) throws ScmRepositoryException {
      this.settings = ClearCaseUtil.getSettings();
      return new ClearCaseScmProviderRepository(this.getLogger(), scmSpecificUrl, this.settings);
   }

   public String getScmType() {
      return "clearcase";
   }

   public boolean requiresEditMode() {
      return true;
   }

   public ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      ClearCaseChangeLogCommand command = new ClearCaseChangeLogCommand();
      command.setLogger(this.getLogger());
      return (ChangeLogScmResult)command.execute(repository, fileSet, parameters);
   }

   public CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      ClearCaseCheckInCommand command = new ClearCaseCheckInCommand();
      command.setLogger(this.getLogger());
      return (CheckInScmResult)command.execute(repository, fileSet, parameters);
   }

   public CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      ClearCaseCheckOutCommand command = new ClearCaseCheckOutCommand();
      command.setLogger(this.getLogger());
      command.setSettings(this.settings);
      return (CheckOutScmResult)command.execute(repository, fileSet, parameters);
   }

   protected UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      ClearCaseUpdateCommand command = new ClearCaseUpdateCommand();
      command.setLogger(this.getLogger());
      return (UpdateScmResult)command.execute(repository, fileSet, parameters);
   }

   public TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      ClearCaseTagCommand command = new ClearCaseTagCommand();
      command.setLogger(this.getLogger());
      return (TagScmResult)command.execute(repository, fileSet, parameters);
   }

   protected StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      ClearCaseStatusCommand command = new ClearCaseStatusCommand();
      command.setLogger(this.getLogger());
      return (StatusScmResult)command.execute(repository, fileSet, parameters);
   }

   protected EditScmResult edit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      ClearCaseEditCommand command = new ClearCaseEditCommand();
      command.setLogger(this.getLogger());
      return (EditScmResult)command.execute(repository, fileSet, parameters);
   }

   protected BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      ClearCaseBlameCommand command = new ClearCaseBlameCommand();
      command.setLogger(this.getLogger());
      return (BlameScmResult)command.execute(repository, fileSet, parameters);
   }
}

package org.apache.maven.scm.provider.synergy;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.add.SynergyAddCommand;
import org.apache.maven.scm.provider.synergy.command.changelog.SynergyChangeLogCommand;
import org.apache.maven.scm.provider.synergy.command.checkin.SynergyCheckInCommand;
import org.apache.maven.scm.provider.synergy.command.checkout.SynergyCheckOutCommand;
import org.apache.maven.scm.provider.synergy.command.edit.SynergyEditCommand;
import org.apache.maven.scm.provider.synergy.command.remove.SynergyRemoveCommand;
import org.apache.maven.scm.provider.synergy.command.status.SynergyStatusCommand;
import org.apache.maven.scm.provider.synergy.command.tag.SynergyTagCommand;
import org.apache.maven.scm.provider.synergy.command.update.SynergyUpdateCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;

public class SynergyScmProvider extends AbstractScmProvider {
   public ScmProviderRepository makeProviderScmRepository(String scmSpecificUrl, char delimiter) throws ScmRepositoryException {
      this.getLogger().debug("Creating SynergyScmProviderRepository...");
      return new SynergyScmProviderRepository(scmSpecificUrl);
   }

   public String getScmType() {
      return "synergy";
   }

   public boolean requiresEditMode() {
      return true;
   }

   public String getScmSpecificFilename() {
      return "_ccmwaid.inf";
   }

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SynergyAddCommand command = new SynergyAddCommand();
      command.setLogger(this.getLogger());
      return (AddScmResult)command.execute(repository, fileSet, parameters);
   }

   public RemoveScmResult remove(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SynergyRemoveCommand command = new SynergyRemoveCommand();
      command.setLogger(this.getLogger());
      return (RemoveScmResult)command.execute(repository, fileSet, parameters);
   }

   public ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SynergyChangeLogCommand command = new SynergyChangeLogCommand();
      command.setLogger(this.getLogger());
      return (ChangeLogScmResult)command.execute(repository, fileSet, parameters);
   }

   public CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SynergyCheckInCommand command = new SynergyCheckInCommand();
      command.setLogger(this.getLogger());
      return (CheckInScmResult)command.execute(repository, fileSet, parameters);
   }

   public CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SynergyCheckOutCommand command = new SynergyCheckOutCommand();
      command.setLogger(this.getLogger());
      return (CheckOutScmResult)command.execute(repository, fileSet, parameters);
   }

   public EditScmResult edit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SynergyEditCommand command = new SynergyEditCommand();
      command.setLogger(this.getLogger());
      return (EditScmResult)command.execute(repository, fileSet, parameters);
   }

   public UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SynergyUpdateCommand command = new SynergyUpdateCommand();
      command.setLogger(this.getLogger());
      return (UpdateScmResult)command.execute(repository, fileSet, parameters);
   }

   public TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SynergyTagCommand command = new SynergyTagCommand();
      command.setLogger(this.getLogger());
      return (TagScmResult)command.execute(repository, fileSet, parameters);
   }

   public StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SynergyStatusCommand command = new SynergyStatusCommand();
      command.setLogger(this.getLogger());
      return (StatusScmResult)command.execute(repository, fileSet, parameters);
   }
}

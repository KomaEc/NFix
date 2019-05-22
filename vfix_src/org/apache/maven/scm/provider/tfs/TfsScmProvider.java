package org.apache.maven.scm.provider.tfs;

import java.net.URI;
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
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.TfsAddCommand;
import org.apache.maven.scm.provider.tfs.command.TfsBranchCommand;
import org.apache.maven.scm.provider.tfs.command.TfsChangeLogCommand;
import org.apache.maven.scm.provider.tfs.command.TfsCheckInCommand;
import org.apache.maven.scm.provider.tfs.command.TfsCheckOutCommand;
import org.apache.maven.scm.provider.tfs.command.TfsEditCommand;
import org.apache.maven.scm.provider.tfs.command.TfsListCommand;
import org.apache.maven.scm.provider.tfs.command.TfsStatusCommand;
import org.apache.maven.scm.provider.tfs.command.TfsTagCommand;
import org.apache.maven.scm.provider.tfs.command.TfsUnEditCommand;
import org.apache.maven.scm.provider.tfs.command.TfsUpdateCommand;
import org.apache.maven.scm.provider.tfs.command.blame.TfsBlameCommand;
import org.apache.maven.scm.repository.ScmRepositoryException;

public class TfsScmProvider extends AbstractScmProvider {
   public static final String TFS_URL_FORMAT = "[[domain\\]username[;password]@]http[s]://server_name[:port][:isCheckinPoliciesEnabled]:workspace:$/TeamProject/Path/To/Project";

   public String getScmType() {
      return "tfs";
   }

   public boolean requiresEditMode() {
      return true;
   }

   public ScmProviderRepository makeProviderScmRepository(String scmUrl, char delimiter) throws ScmRepositoryException {
      int lastAtPos = scmUrl.lastIndexOf(64);
      this.getLogger().info("scmUrl - " + scmUrl);
      String tfsUrl = lastAtPos < 0 ? scmUrl : scmUrl.substring(lastAtPos + 1);
      String usernamePassword = lastAtPos < 0 ? null : scmUrl.substring(0, lastAtPos);
      int tfsPathPos = tfsUrl.lastIndexOf(delimiter + "$/");
      String serverPath = "$/";
      if (tfsPathPos > 0) {
         serverPath = tfsUrl.substring(tfsPathPos + 1);
         tfsUrl = tfsUrl.substring(0, tfsPathPos);
      }

      int workspacePos = tfsUrl.lastIndexOf(delimiter);
      String workspace = tfsUrl.substring(workspacePos + 1);
      tfsUrl = tfsUrl.substring(0, workspacePos);
      this.getLogger().info("workspace: " + workspace);
      int checkinPoliciesPos = tfsUrl.lastIndexOf(delimiter);
      String checkinPolicies = tfsUrl.substring(checkinPoliciesPos + 1);
      tfsUrl = tfsUrl.substring(0, checkinPoliciesPos);
      this.getLogger().info("checkinPolicies: " + checkinPolicies);

      String password;
      try {
         URI tfsUri = URI.create(tfsUrl);
         password = tfsUri.getScheme();
         this.getLogger().info("Scheme - " + password);
         if (password == null || !password.equalsIgnoreCase("http") && !password.equalsIgnoreCase("https")) {
            throw new ScmRepositoryException("TFS Url \"" + tfsUrl + "\" is not a valid URL. " + "The TFS Url syntax is " + "[[domain\\]username[;password]@]http[s]://server_name[:port][:isCheckinPoliciesEnabled]:workspace:$/TeamProject/Path/To/Project");
         }
      } catch (IllegalArgumentException var15) {
         throw new ScmRepositoryException("TFS Url \"" + tfsUrl + "\" is not a valid URL. The TFS Url syntax is " + "[[domain\\]username[;password]@]http[s]://server_name[:port][:isCheckinPoliciesEnabled]:workspace:$/TeamProject/Path/To/Project");
      }

      String username = null;
      password = null;
      if (usernamePassword != null) {
         int delimPos = usernamePassword.indexOf(59);
         username = delimPos < 0 ? usernamePassword : usernamePassword.substring(0, delimPos);
         password = delimPos < 0 ? null : usernamePassword.substring(delimPos + 1);
      }

      boolean useCheckinPolicies = Boolean.parseBoolean(checkinPolicies);
      return new TfsScmProviderRepository(tfsUrl, username, password, serverPath, workspace, useCheckinPolicies);
   }

   protected ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      TfsChangeLogCommand command = new TfsChangeLogCommand();
      command.setLogger(this.getLogger());
      return (ChangeLogScmResult)command.execute(repository, fileSet, parameters);
   }

   protected CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      TfsCheckOutCommand command = new TfsCheckOutCommand();
      command.setLogger(this.getLogger());
      return (CheckOutScmResult)command.execute(repository, fileSet, parameters);
   }

   protected EditScmResult edit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      TfsEditCommand command = new TfsEditCommand();
      command.setLogger(this.getLogger());
      return (EditScmResult)command.execute(repository, fileSet, parameters);
   }

   protected UnEditScmResult unedit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      TfsUnEditCommand command = new TfsUnEditCommand();
      command.setLogger(this.getLogger());
      return (UnEditScmResult)command.execute(repository, fileSet, parameters);
   }

   protected StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      TfsStatusCommand command = new TfsStatusCommand();
      command.setLogger(this.getLogger());
      return (StatusScmResult)command.execute(repository, fileSet, parameters);
   }

   protected UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      TfsUpdateCommand command = new TfsUpdateCommand();
      command.setLogger(this.getLogger());
      return (UpdateScmResult)command.execute(repository, fileSet, parameters);
   }

   protected CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      TfsCheckInCommand command = new TfsCheckInCommand();
      command.setLogger(this.getLogger());
      return (CheckInScmResult)command.execute(repository, fileSet, parameters);
   }

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      TfsAddCommand command = new TfsAddCommand();
      command.setLogger(this.getLogger());
      return (AddScmResult)command.execute(repository, fileSet, parameters);
   }

   protected TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      TfsTagCommand command = new TfsTagCommand();
      command.setLogger(this.getLogger());
      return (TagScmResult)command.execute(repository, fileSet, parameters);
   }

   protected BranchScmResult branch(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      TfsBranchCommand command = new TfsBranchCommand();
      command.setLogger(this.getLogger());
      return (BranchScmResult)command.execute(repository, fileSet, parameters);
   }

   protected ListScmResult list(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      TfsListCommand command = new TfsListCommand();
      command.setLogger(this.getLogger());
      return (ListScmResult)command.execute(repository, fileSet, parameters);
   }

   protected BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      TfsBlameCommand command = new TfsBlameCommand();
      command.setLogger(this.getLogger());
      return (BlameScmResult)command.execute(repository, fileSet, parameters);
   }

   protected DiffScmResult diff(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return super.diff(repository, fileSet, parameters);
   }

   protected ExportScmResult export(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return super.export(repository, fileSet, parameters);
   }
}

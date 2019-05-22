package org.apache.maven.scm.provider.integrity;

import org.apache.maven.scm.CommandParameter;
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
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.command.login.LoginScmResult;
import org.apache.maven.scm.command.mkdir.MkdirScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.command.add.IntegrityAddCommand;
import org.apache.maven.scm.provider.integrity.command.blame.IntegrityBlameCommand;
import org.apache.maven.scm.provider.integrity.command.branch.IntegrityBranchCommand;
import org.apache.maven.scm.provider.integrity.command.changelog.IntegrityChangeLogCommand;
import org.apache.maven.scm.provider.integrity.command.checkin.IntegrityCheckInCommand;
import org.apache.maven.scm.provider.integrity.command.checkout.IntegrityCheckOutCommand;
import org.apache.maven.scm.provider.integrity.command.diff.IntegrityDiffCommand;
import org.apache.maven.scm.provider.integrity.command.edit.IntegrityEditCommand;
import org.apache.maven.scm.provider.integrity.command.export.IntegrityExportCommand;
import org.apache.maven.scm.provider.integrity.command.fileinfo.IntegrityFileInfoCommand;
import org.apache.maven.scm.provider.integrity.command.list.IntegrityListCommand;
import org.apache.maven.scm.provider.integrity.command.lock.IntegrityLockCommand;
import org.apache.maven.scm.provider.integrity.command.login.IntegrityLoginCommand;
import org.apache.maven.scm.provider.integrity.command.mkdir.IntegrityMkdirCommand;
import org.apache.maven.scm.provider.integrity.command.remove.IntegrityRemoveCommand;
import org.apache.maven.scm.provider.integrity.command.status.IntegrityStatusCommand;
import org.apache.maven.scm.provider.integrity.command.tag.IntegrityTagCommand;
import org.apache.maven.scm.provider.integrity.command.unedit.IntegrityUnEditCommand;
import org.apache.maven.scm.provider.integrity.command.unlock.IntegrityUnlockCommand;
import org.apache.maven.scm.provider.integrity.command.update.IntegrityUpdateCommand;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.codehaus.plexus.util.StringUtils;

public class IntegrityScmProvider extends AbstractScmProvider {
   public static final String INTEGRITY_CM_URL = "[[user][/pass]@host[:port]]|configPath";

   public String getScmType() {
      return "integrity";
   }

   public ScmProviderRepository makeProviderScmRepository(String scmSpecificUrl, char delimiter) throws ScmRepositoryException {
      String hostName = "";
      int port = 0;
      String userName = "";
      String password = "";
      String configPath = "";
      String[] tokens = StringUtils.split(scmSpecificUrl, String.valueOf(delimiter));
      if (tokens.length >= 1 && tokens.length <= 2) {
         if (tokens[0].indexOf(64) >= 0) {
            String userPassStr = tokens[0].substring(0, tokens[0].indexOf(64));
            this.getLogger().debug("User/Password information supplied: " + userPassStr);
            String hostPortStr = tokens[0].substring(tokens[0].indexOf(64) + 1, tokens[0].length());
            this.getLogger().debug("Host/Port information supplied: " + hostPortStr);
            int hostPortDelimIndx;
            if (userPassStr.length() > 0) {
               hostPortDelimIndx = userPassStr.indexOf(47);
               if (hostPortDelimIndx > 0) {
                  userName = userPassStr.substring(0, userPassStr.indexOf(47));
                  if (userPassStr.length() > hostPortDelimIndx + 1) {
                     password = userPassStr.substring(userPassStr.indexOf(47) + 1, userPassStr.length());
                  }
               } else {
                  userName = userPassStr;
               }
            }

            if (hostPortStr.length() > 0) {
               hostPortDelimIndx = hostPortStr.indexOf(58);
               if (hostPortDelimIndx > 0) {
                  hostName = hostPortStr.substring(0, hostPortStr.indexOf(58));
                  if (hostPortStr.length() > hostPortDelimIndx + 1) {
                     port = Integer.parseInt(hostPortStr.substring(hostPortStr.indexOf(58) + 1, hostPortStr.length()));
                  }
               } else {
                  hostName = hostPortStr;
               }
            }
         }

         configPath = tokens[tokens.length - 1];
         return new IntegrityScmProviderRepository(hostName, port, userName, password, configPath, this.getLogger());
      } else {
         throw new ScmRepositoryException("Invalid SCM URL '" + scmSpecificUrl + "'.  Expecting a url using format: " + "[[user][/pass]@host[:port]]|configPath");
      }
   }

   protected LoginScmResult login(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityLoginCommand command = new IntegrityLoginCommand();
      command.setLogger(this.getLogger());
      return (LoginScmResult)command.execute(repository, fileSet, params);
   }

   protected ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      IntegrityChangeLogCommand command = new IntegrityChangeLogCommand();
      command.setLogger(this.getLogger());
      return (ChangeLogScmResult)command.execute(repository, fileSet, parameters);
   }

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityAddCommand command = new IntegrityAddCommand();
      command.setLogger(this.getLogger());
      return (AddScmResult)command.execute(repository, fileSet, params);
   }

   protected RemoveScmResult remove(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityRemoveCommand command = new IntegrityRemoveCommand();
      command.setLogger(this.getLogger());
      return (RemoveScmResult)command.execute(repository, fileSet, params);
   }

   protected CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityCheckInCommand command = new IntegrityCheckInCommand();
      command.setLogger(this.getLogger());
      return (CheckInScmResult)command.execute(repository, fileSet, params);
   }

   protected CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityCheckOutCommand command = new IntegrityCheckOutCommand();
      command.setLogger(this.getLogger());
      return (CheckOutScmResult)command.execute(repository, fileSet, params);
   }

   protected DiffScmResult diff(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityDiffCommand command = new IntegrityDiffCommand();
      command.setLogger(this.getLogger());
      return (DiffScmResult)command.execute(repository, fileSet, params);
   }

   protected EditScmResult edit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityEditCommand command = new IntegrityEditCommand();
      command.setLogger(this.getLogger());
      return (EditScmResult)command.execute(repository, fileSet, params);
   }

   protected StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityStatusCommand command = new IntegrityStatusCommand();
      command.setLogger(this.getLogger());
      return (StatusScmResult)command.execute(repository, fileSet, params);
   }

   protected TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityTagCommand command = new IntegrityTagCommand();
      command.setLogger(this.getLogger());
      return (TagScmResult)command.execute(repository, fileSet, params);
   }

   protected UnEditScmResult unedit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityUnEditCommand command = new IntegrityUnEditCommand();
      command.setLogger(this.getLogger());
      return (UnEditScmResult)command.execute(repository, fileSet, params);
   }

   protected UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityUpdateCommand command = new IntegrityUpdateCommand();
      command.setLogger(this.getLogger());
      return (UpdateScmResult)command.execute(repository, fileSet, params);
   }

   protected BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityBlameCommand command = new IntegrityBlameCommand();
      command.setLogger(this.getLogger());
      return (BlameScmResult)command.execute(repository, fileSet, params);
   }

   protected ListScmResult list(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityListCommand command = new IntegrityListCommand();
      command.setLogger(this.getLogger());
      return (ListScmResult)command.execute(repository, fileSet, params);
   }

   protected ExportScmResult export(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityExportCommand command = new IntegrityExportCommand();
      command.setLogger(this.getLogger());
      return (ExportScmResult)command.execute(repository, fileSet, params);
   }

   protected BranchScmResult branch(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityBranchCommand command = new IntegrityBranchCommand();
      command.setLogger(this.getLogger());
      return (BranchScmResult)command.execute(repository, fileSet, params);
   }

   protected MkdirScmResult mkdir(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityMkdirCommand command = new IntegrityMkdirCommand();
      command.setLogger(this.getLogger());
      return (MkdirScmResult)command.execute(repository, fileSet, params);
   }

   protected ScmResult fileinfo(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityFileInfoCommand command = new IntegrityFileInfoCommand();
      command.setLogger(this.getLogger());
      return command.execute(repository, fileSet, params);
   }

   protected ScmResult lock(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityLockCommand command = new IntegrityLockCommand();
      command.setLogger(this.getLogger());
      return command.execute(repository, fileSet, params);
   }

   protected ScmResult unlock(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      IntegrityUnlockCommand command = new IntegrityUnlockCommand(params.getString(CommandParameter.FILE));
      command.setLogger(this.getLogger());
      return command.execute(repository, fileSet, params);
   }
}

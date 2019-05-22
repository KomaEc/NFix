package org.apache.maven.scm.provider.vss;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.vss.commands.add.VssAddCommand;
import org.apache.maven.scm.provider.vss.commands.changelog.VssHistoryCommand;
import org.apache.maven.scm.provider.vss.commands.checkin.VssCheckInCommand;
import org.apache.maven.scm.provider.vss.commands.checkout.VssCheckOutCommand;
import org.apache.maven.scm.provider.vss.commands.edit.VssEditCommand;
import org.apache.maven.scm.provider.vss.commands.status.VssStatusCommand;
import org.apache.maven.scm.provider.vss.commands.tag.VssTagCommand;
import org.apache.maven.scm.provider.vss.commands.update.VssUpdateCommand;
import org.apache.maven.scm.provider.vss.repository.VssScmProviderRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.codehaus.plexus.util.StringUtils;

public class VssScmProvider extends AbstractScmProvider {
   public static final String VSS_URL_FORMAT = "[username[|password]@]vssdir|projectPath";

   public String getScmSpecificFilename() {
      return "vssver.scc";
   }

   public ScmProviderRepository makeProviderScmRepository(String scmSpecificUrl, char delimiter) throws ScmRepositoryException {
      String user = null;
      String password = null;
      int index = scmSpecificUrl.indexOf(64);
      String rest = scmSpecificUrl;
      if (index != -1) {
         String userAndPassword = scmSpecificUrl.substring(0, index);
         rest = scmSpecificUrl.substring(index + 1);
         index = userAndPassword.indexOf(delimiter);
         if (index != -1) {
            user = userAndPassword.substring(0, index);
            password = userAndPassword.substring(index + 1);
         } else {
            user = userAndPassword;
         }
      }

      String[] tokens = StringUtils.split(rest, String.valueOf(delimiter));
      if (tokens.length < 2) {
         throw new ScmRepositoryException("Invalid SCM URL: The url has to be on the form: [username[|password]@]vssdir|projectPath");
      } else {
         String vssDir = tokens[0];
         String project = tokens[1];
         return new VssScmProviderRepository(user, password, vssDir, project);
      }
   }

   public String getScmType() {
      return "vss";
   }

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      VssAddCommand command = new VssAddCommand();
      command.setLogger(this.getLogger());
      return (AddScmResult)command.execute(repository, fileSet, parameters);
   }

   public CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      VssCheckInCommand command = new VssCheckInCommand();
      command.setLogger(this.getLogger());
      return (CheckInScmResult)command.execute(repository, fileSet, parameters);
   }

   public CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      VssCheckOutCommand command = new VssCheckOutCommand();
      command.setLogger(this.getLogger());
      return (CheckOutScmResult)command.execute(repository, fileSet, parameters);
   }

   public ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      VssHistoryCommand command = new VssHistoryCommand();
      command.setLogger(this.getLogger());
      return (ChangeLogScmResult)command.execute(repository, fileSet, parameters);
   }

   public TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      VssTagCommand command = new VssTagCommand();
      command.setLogger(this.getLogger());
      return (TagScmResult)command.execute(repository, fileSet, parameters);
   }

   public UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      VssUpdateCommand command = new VssUpdateCommand();
      command.setLogger(this.getLogger());
      return (UpdateScmResult)command.execute(repository, fileSet, parameters);
   }

   public StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      VssStatusCommand command = new VssStatusCommand();
      command.setLogger(this.getLogger());
      return (StatusScmResult)command.execute(repository, fileSet, parameters);
   }

   public EditScmResult edit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      VssEditCommand command = new VssEditCommand();
      command.setLogger(this.getLogger());
      return (EditScmResult)command.execute(repository, fileSet, parameters);
   }
}

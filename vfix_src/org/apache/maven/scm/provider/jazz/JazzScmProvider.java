package org.apache.maven.scm.provider.jazz;

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
import org.apache.maven.scm.provider.jazz.command.add.JazzAddCommand;
import org.apache.maven.scm.provider.jazz.command.blame.JazzBlameCommand;
import org.apache.maven.scm.provider.jazz.command.branch.JazzBranchCommand;
import org.apache.maven.scm.provider.jazz.command.changelog.JazzChangeLogCommand;
import org.apache.maven.scm.provider.jazz.command.checkin.JazzCheckInCommand;
import org.apache.maven.scm.provider.jazz.command.checkout.JazzCheckOutCommand;
import org.apache.maven.scm.provider.jazz.command.diff.JazzDiffCommand;
import org.apache.maven.scm.provider.jazz.command.edit.JazzEditCommand;
import org.apache.maven.scm.provider.jazz.command.list.JazzListCommand;
import org.apache.maven.scm.provider.jazz.command.status.JazzStatusCommand;
import org.apache.maven.scm.provider.jazz.command.tag.JazzTagCommand;
import org.apache.maven.scm.provider.jazz.command.unedit.JazzUnEditCommand;
import org.apache.maven.scm.provider.jazz.command.update.JazzUpdateCommand;
import org.apache.maven.scm.provider.jazz.repository.JazzScmProviderRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;

public class JazzScmProvider extends AbstractScmProvider {
   public static final String JAZZ_URL_FORMAT = "scm:jazz:[username[;password]@]http[s]://server_name[:port]/contextRoot:repositoryWorkspace";

   public String getScmType() {
      return "jazz";
   }

   public ScmProviderRepository makeProviderScmRepository(String scmUrl, char delimiter) throws ScmRepositoryException {
      this.getLogger().debug("JazzScmProvider:makeProviderScmRepository");
      this.getLogger().debug("Provided scm url   - " + scmUrl);
      this.getLogger().debug("Provided delimiter - '" + delimiter + "'");
      String jazzUrlAndWorkspace = null;
      String usernameAndPassword = null;
      int lastAtPosition = scmUrl.lastIndexOf(64);
      if (lastAtPosition == -1) {
         jazzUrlAndWorkspace = scmUrl;
      } else {
         jazzUrlAndWorkspace = lastAtPosition < 0 ? scmUrl : scmUrl.substring(lastAtPosition + 1);
         usernameAndPassword = lastAtPosition < 0 ? null : scmUrl.substring(0, lastAtPosition);
      }

      String username = null;
      String password = null;
      int colonsCounted;
      if (usernameAndPassword != null) {
         colonsCounted = usernameAndPassword.indexOf(59);
         username = colonsCounted >= 0 ? usernameAndPassword.substring(0, colonsCounted) : usernameAndPassword;
         password = colonsCounted >= 0 ? usernameAndPassword.substring(colonsCounted + 1) : null;
      }

      colonsCounted = 0;
      int colonIndex = 0;

      while(colonIndex != -1) {
         colonIndex = jazzUrlAndWorkspace.indexOf(":", colonIndex + 1);
         if (colonIndex != -1) {
            ++colonsCounted;
         }
      }

      boolean havePort = colonsCounted == 3;
      int repositoryWorkspacePosition = jazzUrlAndWorkspace.lastIndexOf(delimiter);
      String repositoryWorkspace = jazzUrlAndWorkspace.substring(repositoryWorkspacePosition + 1);
      String jazzUrl = jazzUrlAndWorkspace.substring(0, repositoryWorkspacePosition);

      URI hostname;
      try {
         hostname = URI.create(jazzUrl);
         String scheme = hostname.getScheme();
         this.getLogger().debug("Scheme - " + scheme);
         if (scheme == null || !scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https")) {
            throw new ScmRepositoryException("Jazz Url \"" + jazzUrl + "\" is not a valid URL. The Jazz Url syntax is " + "scm:jazz:[username[;password]@]http[s]://server_name[:port]/contextRoot:repositoryWorkspace");
         }
      } catch (IllegalArgumentException var22) {
         throw new ScmRepositoryException("Jazz Url \"" + jazzUrl + "\" is not a valid URL. The Jazz Url syntax is " + "scm:jazz:[username[;password]@]http[s]://server_name[:port]/contextRoot:repositoryWorkspace");
      }

      hostname = null;
      int port = 0;
      int protocolIndex;
      int pathIndex;
      String hostname;
      if (havePort) {
         protocolIndex = jazzUrl.indexOf(":") + 3;
         pathIndex = jazzUrl.indexOf(":", protocolIndex + 1);
         hostname = jazzUrl.substring(protocolIndex, pathIndex);
         int pathIndex = jazzUrl.indexOf("/", pathIndex + 1);
         String portNo = jazzUrl.substring(pathIndex + 1, pathIndex);

         try {
            port = Integer.parseInt(portNo);
         } catch (NumberFormatException var21) {
            throw new ScmRepositoryException("Jazz Url \"" + jazzUrl + "\" is not a valid URL. The Jazz Url syntax is " + "scm:jazz:[username[;password]@]http[s]://server_name[:port]/contextRoot:repositoryWorkspace");
         }
      } else {
         protocolIndex = jazzUrl.indexOf(":") + 3;
         pathIndex = jazzUrl.indexOf("/", protocolIndex + 1);
         if (protocolIndex == -1 || pathIndex == -1) {
            throw new ScmRepositoryException("Jazz Url \"" + jazzUrl + "\" is not a valid URL. The Jazz Url syntax is " + "scm:jazz:[username[;password]@]http[s]://server_name[:port]/contextRoot:repositoryWorkspace");
         }

         hostname = jazzUrl.substring(protocolIndex, pathIndex);
      }

      this.getLogger().debug("Creating JazzScmProviderRepository with the following values:");
      this.getLogger().debug("jazzUrl             - " + jazzUrl);
      this.getLogger().debug("username            - " + username);
      this.getLogger().debug("password            - " + password);
      this.getLogger().debug("hostname            - " + hostname);
      this.getLogger().debug("port                - " + port);
      this.getLogger().debug("repositoryWorkspace - " + repositoryWorkspace);
      return new JazzScmProviderRepository(jazzUrl, username, password, hostname, port, repositoryWorkspace);
   }

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzAddCommand command = new JazzAddCommand();
      command.setLogger(this.getLogger());
      return (AddScmResult)command.execute(repository, fileSet, parameters);
   }

   protected BranchScmResult branch(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzBranchCommand command = new JazzBranchCommand();
      command.setLogger(this.getLogger());
      return (BranchScmResult)command.execute(repository, fileSet, parameters);
   }

   protected BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzBlameCommand command = new JazzBlameCommand();
      command.setLogger(this.getLogger());
      return (BlameScmResult)command.execute(repository, fileSet, parameters);
   }

   protected ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzStatusCommand statusCommand = new JazzStatusCommand();
      statusCommand.setLogger(this.getLogger());
      statusCommand.execute(repository, fileSet, parameters);
      JazzChangeLogCommand command = new JazzChangeLogCommand();
      command.setLogger(this.getLogger());
      return (ChangeLogScmResult)command.execute(repository, fileSet, parameters);
   }

   protected CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzCheckInCommand command = new JazzCheckInCommand();
      command.setLogger(this.getLogger());
      return (CheckInScmResult)command.execute(repository, fileSet, parameters);
   }

   protected CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzCheckOutCommand command = new JazzCheckOutCommand();
      command.setLogger(this.getLogger());
      return (CheckOutScmResult)command.execute(repository, fileSet, parameters);
   }

   protected DiffScmResult diff(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzDiffCommand command = new JazzDiffCommand();
      command.setLogger(this.getLogger());
      return (DiffScmResult)command.execute(repository, fileSet, parameters);
   }

   protected EditScmResult edit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzEditCommand command = new JazzEditCommand();
      command.setLogger(this.getLogger());
      return (EditScmResult)command.execute(repository, fileSet, parameters);
   }

   protected ExportScmResult export(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return super.export(repository, fileSet, parameters);
   }

   protected ListScmResult list(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzStatusCommand statusCommand = new JazzStatusCommand();
      statusCommand.setLogger(this.getLogger());
      statusCommand.execute(repository, fileSet, parameters);
      JazzListCommand command = new JazzListCommand();
      command.setLogger(this.getLogger());
      return (ListScmResult)command.execute(repository, fileSet, parameters);
   }

   protected StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzStatusCommand command = new JazzStatusCommand();
      command.setLogger(this.getLogger());
      return (StatusScmResult)command.execute(repository, fileSet, parameters);
   }

   protected TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzStatusCommand statusCommand = new JazzStatusCommand();
      statusCommand.setLogger(this.getLogger());
      statusCommand.execute(repository, fileSet, parameters);
      JazzTagCommand command = new JazzTagCommand();
      command.setLogger(this.getLogger());
      return (TagScmResult)command.execute(repository, fileSet, parameters);
   }

   protected UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzUpdateCommand command = new JazzUpdateCommand();
      command.setLogger(this.getLogger());
      return (UpdateScmResult)command.execute(repository, fileSet, parameters);
   }

   protected UnEditScmResult unedit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      JazzUnEditCommand command = new JazzUnEditCommand();
      command.setLogger(this.getLogger());
      return (UnEditScmResult)command.execute(repository, fileSet, parameters);
   }

   public String getScmSpecificFilename() {
      return ".jazz5";
   }
}

package org.apache.maven.scm.provider.accurev;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmRevision;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.command.login.LoginScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.accurev.cli.AccuRevCommandLine;
import org.apache.maven.scm.provider.accurev.command.add.AccuRevAddCommand;
import org.apache.maven.scm.provider.accurev.command.blame.AccuRevBlameCommand;
import org.apache.maven.scm.provider.accurev.command.changelog.AccuRevChangeLogCommand;
import org.apache.maven.scm.provider.accurev.command.checkin.AccuRevCheckInCommand;
import org.apache.maven.scm.provider.accurev.command.checkout.AccuRevCheckOutCommand;
import org.apache.maven.scm.provider.accurev.command.export.AccuRevExportCommand;
import org.apache.maven.scm.provider.accurev.command.login.AccuRevLoginCommand;
import org.apache.maven.scm.provider.accurev.command.remove.AccuRevRemoveCommand;
import org.apache.maven.scm.provider.accurev.command.status.AccuRevStatusCommand;
import org.apache.maven.scm.provider.accurev.command.tag.AccuRevTagCommand;
import org.apache.maven.scm.provider.accurev.command.update.AccuRevUpdateCommand;
import org.apache.maven.scm.provider.accurev.command.update.AccuRevUpdateScmResult;
import org.apache.maven.scm.provider.accurev.util.QuotedPropertyParser;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.scm.repository.UnknownRepositoryStructure;
import org.codehaus.plexus.util.StringUtils;

public class AccuRevScmProvider extends AbstractScmProvider {
   public static final String ACCUREV_EXECUTABLE_PROPERTY = "accurevExe";
   public static final String TAG_FORMAT_PROPERTY = "tagFormat";
   public static final String SYSTEM_PROPERTY_PREFIX = "maven.scm.accurev.";

   public String getScmType() {
      return "accurev";
   }

   public ScmProviderRepository makeProviderScmRepository(String scmSpecificUrl, char delimiter) throws ScmRepositoryException {
      List<String> validationMessages = new ArrayList();
      String[] tokens = StringUtils.split(scmSpecificUrl, Character.toString(delimiter));
      String basisStream = null;
      String projectPath = null;
      int port = 5050;
      String host = null;
      String user = null;
      String password = null;
      Map<String, String> properties = new HashMap();
      properties.put("tagFormat", "%s");
      properties.put("accurevExe", "accurev");
      this.fillSystemProperties(properties);

      int i;
      for(i = 0; i < tokens.length; ++i) {
         int at = tokens[i].indexOf(64);
         int slash = tokens[i].indexOf(47);
         slash = slash < 0 ? tokens[i].indexOf(92) : slash;
         int qMark = tokens[i].indexOf(63);
         if (qMark == 0) {
            QuotedPropertyParser.parse(tokens[i].substring(1), properties);
         } else {
            if (slash == 0) {
               projectPath = tokens[i].substring(1);
               break;
            }

            if ((slash > 0 || at >= 0) && host == null && user == null) {
               int len = tokens[i].length();
               if (at >= 0 && len > at) {
                  host = tokens[i].substring(at + 1);
               }

               if (slash > 0) {
                  user = tokens[i].substring(0, slash);
                  password = tokens[i].substring(slash + 1, at < 0 ? len : at);
               } else {
                  user = tokens[i].substring(0, at < 0 ? len : at);
               }
            } else if (host != null && tokens[i].matches("^[0-9]+$")) {
               port = Integer.parseInt(tokens[i]);
            } else {
               basisStream = tokens[i];
            }
         }
      }

      if (i < tokens.length) {
         validationMessages.add("Unknown tokens in URL " + scmSpecificUrl);
      }

      AccuRevScmProviderRepository repo = new AccuRevScmProviderRepository();
      repo.setLogger(this.getLogger());
      if (!StringUtils.isEmpty(user)) {
         repo.setUser(user);
      }

      if (!StringUtils.isEmpty(password)) {
         repo.setPassword(password);
      }

      if (!StringUtils.isEmpty(basisStream)) {
         repo.setStreamName(basisStream);
      }

      if (!StringUtils.isEmpty(projectPath)) {
         repo.setProjectPath(projectPath);
      }

      if (!StringUtils.isEmpty(host)) {
         repo.setHost(host);
      }

      repo.setPort(port);
      repo.setTagFormat((String)properties.get("tagFormat"));
      AccuRevCommandLine accuRev = new AccuRevCommandLine(host, port);
      accuRev.setLogger(this.getLogger());
      accuRev.setExecutable((String)properties.get("accurevExe"));
      repo.setAccuRev(accuRev);
      return repo;
   }

   private void fillSystemProperties(Map<String, String> properties) {
      Set<String> propertyKeys = properties.keySet();
      Iterator i$ = propertyKeys.iterator();

      while(i$.hasNext()) {
         String key = (String)i$.next();
         String systemPropertyKey = "maven.scm.accurev." + key;
         String systemProperty = System.getProperty(systemPropertyKey);
         if (systemProperty != null) {
            properties.put(key, systemProperty);
         }
      }

   }

   protected LoginScmResult login(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(repository.toString());
      }

      AccuRevLoginCommand command = new AccuRevLoginCommand(this.getLogger());
      return command.login(repository, fileSet, parameters);
   }

   protected CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      AccuRevScmProviderRepository accuRevRepo = (AccuRevScmProviderRepository)repository;
      if (!repository.isPersistCheckout() && accuRevRepo.shouldUseExportForNonPersistentCheckout()) {
         ExportScmResult result = this.export(repository, fileSet, parameters);
         return result.isSuccess() ? new CheckOutScmResult(result.getCommandLine(), result.getExportedFiles(), accuRevRepo.getExportRelativePath()) : new CheckOutScmResult(result.getCommandLine(), result.getProviderMessage(), result.getCommandOutput(), false);
      } else {
         AccuRevCheckOutCommand command = new AccuRevCheckOutCommand(this.getLogger());
         return command.checkout(repository, fileSet, parameters);
      }
   }

   protected CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      AccuRevCheckInCommand command = new AccuRevCheckInCommand(this.getLogger());
      return command.checkIn(repository, fileSet, parameters);
   }

   public ScmProviderRepository makeProviderScmRepository(File path) throws ScmRepositoryException, UnknownRepositoryStructure {
      return super.makeProviderScmRepository(path);
   }

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      AccuRevAddCommand command = new AccuRevAddCommand(this.getLogger());
      return command.add(repository, fileSet, parameters);
   }

   protected TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      AccuRevTagCommand command = new AccuRevTagCommand(this.getLogger());
      return command.tag(repository, fileSet, parameters);
   }

   protected StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      AccuRevStatusCommand command = new AccuRevStatusCommand(this.getLogger());
      return command.status(repository, fileSet, parameters);
   }

   protected UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      AccuRevScmProviderRepository accurevRepo = (AccuRevScmProviderRepository)repository;
      AccuRevUpdateCommand command = new AccuRevUpdateCommand(this.getLogger());
      UpdateScmResult result = command.update(repository, fileSet, parameters);
      if (result.isSuccess() && parameters.getBoolean(CommandParameter.RUN_CHANGELOG_WITH_UPDATE)) {
         AccuRevUpdateScmResult accuRevResult = (AccuRevUpdateScmResult)result;
         ScmRevision fromRevision = new ScmRevision(accuRevResult.getFromRevision());
         ScmRevision toRevision = new ScmRevision(accuRevResult.getToRevision());
         parameters.setScmVersion(CommandParameter.START_SCM_VERSION, fromRevision);
         parameters.setScmVersion(CommandParameter.END_SCM_VERSION, toRevision);
         AccuRevVersion startVersion = accurevRepo.getAccuRevVersion(fromRevision);
         AccuRevVersion endVersion = accurevRepo.getAccuRevVersion(toRevision);
         if (startVersion.getBasisStream().equals(endVersion.getBasisStream())) {
            ChangeLogScmResult changeLogResult = this.changelog(repository, fileSet, parameters);
            if (changeLogResult.isSuccess()) {
               result.setChanges(changeLogResult.getChangeLog().getChangeSets());
            } else {
               this.getLogger().warn("Changelog from " + fromRevision + " to " + toRevision + " failed");
            }
         } else {
            String comment = "Cross stream update result from " + startVersion + " to " + endVersion;
            String author = "";
            List<ScmFile> files = result.getUpdatedFiles();
            List<ChangeFile> changeFiles = new ArrayList(files.size());
            Iterator i$ = files.iterator();

            while(i$.hasNext()) {
               ScmFile scmFile = (ScmFile)i$.next();
               changeFiles.add(new ChangeFile(scmFile.getPath()));
            }

            ChangeSet dummyChangeSet = new ChangeSet(new Date(), comment, author, changeFiles);
            List<ChangeSet> changeSets = Collections.singletonList(dummyChangeSet);
            result.setChanges(changeSets);
         }
      }

      return result;
   }

   protected ExportScmResult export(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      AccuRevExportCommand command = new AccuRevExportCommand(this.getLogger());
      return command.export(repository, fileSet, parameters);
   }

   protected ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      AccuRevChangeLogCommand command = new AccuRevChangeLogCommand(this.getLogger());
      return command.changelog(repository, fileSet, parameters);
   }

   protected RemoveScmResult remove(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      AccuRevRemoveCommand command = new AccuRevRemoveCommand(this.getLogger());
      return command.remove(repository, fileSet, parameters);
   }

   protected BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      AccuRevBlameCommand blameCommand = new AccuRevBlameCommand(this.getLogger());
      return blameCommand.blame(repository, fileSet, parameters);
   }
}

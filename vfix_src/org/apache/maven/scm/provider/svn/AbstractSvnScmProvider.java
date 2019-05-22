package org.apache.maven.scm.provider.svn;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.apache.maven.scm.command.info.InfoItem;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.command.mkdir.MkdirScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.util.SvnUtil;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.scm.repository.UnknownRepositoryStructure;
import org.codehaus.plexus.util.StringUtils;

public abstract class AbstractSvnScmProvider extends AbstractScmProvider {
   private static final String CHECK_WORKING_DIRECTORY_URL = "scmCheckWorkingDirectoryUrl";

   public String getScmSpecificFilename() {
      return ".svn";
   }

   public ScmProviderRepository makeProviderScmRepository(String scmSpecificUrl, char delimiter) throws ScmRepositoryException {
      AbstractSvnScmProvider.ScmUrlParserResult result = this.parseScmUrl(scmSpecificUrl);
      if (this.checkWorkingDirectoryUrl()) {
         this.getLogger().debug("Checking svn info 'URL:' field matches current sources directory");

         try {
            InfoScmResult info = this.info(result.repository, new ScmFileSet(new File(".")), new CommandParameters());
            String url = this.findUrlInfoItem(info);
            if (url != null && !url.equals(scmSpecificUrl)) {
               result.messages.add("The scm url does not match the value returned by svn info");
            }
         } catch (ScmException var6) {
            throw new ScmRepositoryException("An error occurred while trying to svn info", var6);
         }
      }

      if (result.messages.size() > 0) {
         throw new ScmRepositoryException("The scm url is invalid.", result.messages);
      } else {
         return result.repository;
      }
   }

   private boolean checkWorkingDirectoryUrl() {
      return Boolean.getBoolean("scmCheckWorkingDirectoryUrl");
   }

   private String findUrlInfoItem(InfoScmResult infoScmResult) {
      Iterator i$ = infoScmResult.getInfoItems().iterator();

      InfoItem infoItem;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         infoItem = (InfoItem)i$.next();
      } while(infoItem.getURL() == null);

      this.getLogger().debug("URL found: " + infoItem.getURL());
      return infoItem.getURL();
   }

   public ScmProviderRepository makeProviderScmRepository(File path) throws ScmRepositoryException, UnknownRepositoryStructure {
      if (path == null) {
         throw new NullPointerException("Path argument is null");
      } else if (!path.isDirectory()) {
         throw new ScmRepositoryException(path.getAbsolutePath() + " isn't a valid directory.");
      } else if (!(new File(path, ".svn")).exists()) {
         throw new ScmRepositoryException(path.getAbsolutePath() + " isn't a svn checkout directory.");
      } else {
         try {
            return this.makeProviderScmRepository(this.getRepositoryURL(path), ':');
         } catch (ScmException var3) {
            throw new ScmRepositoryException("Error executing info command", var3);
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
      return "svn";
   }

   private AbstractSvnScmProvider.ScmUrlParserResult parseScmUrl(String scmSpecificUrl) {
      AbstractSvnScmProvider.ScmUrlParserResult result = new AbstractSvnScmProvider.ScmUrlParserResult();
      if (scmSpecificUrl.startsWith("file")) {
         if (!scmSpecificUrl.startsWith("file://")) {
            result.messages.add("A svn 'file' url must be on the form 'file://[hostname]/'.");
            return result;
         }
      } else if (scmSpecificUrl.startsWith("https")) {
         if (!scmSpecificUrl.startsWith("https://")) {
            result.messages.add("A svn 'http' url must be on the form 'https://'.");
            return result;
         }
      } else if (scmSpecificUrl.startsWith("http")) {
         if (!scmSpecificUrl.startsWith("http://")) {
            result.messages.add("A svn 'http' url must be on the form 'http://'.");
            return result;
         }
      } else if (scmSpecificUrl.startsWith("svn+")) {
         if (scmSpecificUrl.indexOf("://") < 0) {
            result.messages.add("A svn 'svn+xxx' url must be on the form 'svn+xxx://'.");
            return result;
         }

         String tunnel = scmSpecificUrl.substring("svn+".length(), scmSpecificUrl.indexOf("://"));
         if (!"ssh".equals(tunnel)) {
            SvnConfigFileReader reader = new SvnConfigFileReader();
            if (SvnUtil.getSettings().getConfigDirectory() != null) {
               reader.setConfigDirectory(new File(SvnUtil.getSettings().getConfigDirectory()));
            }

            if (StringUtils.isEmpty(reader.getProperty("tunnels", tunnel))) {
               result.messages.add("The tunnel '" + tunnel + "' isn't defined in your subversion configuration file.");
               return result;
            }
         }
      } else {
         if (!scmSpecificUrl.startsWith("svn")) {
            result.messages.add(scmSpecificUrl + " url isn't a valid svn URL.");
            return result;
         }

         if (!scmSpecificUrl.startsWith("svn://")) {
            result.messages.add("A svn 'svn' url must be on the form 'svn://'.");
            return result;
         }
      }

      result.repository = new SvnScmProviderRepository(scmSpecificUrl);
      return result;
   }

   protected abstract SvnCommand getAddCommand();

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (AddScmResult)this.executeCommand(this.getAddCommand(), repository, fileSet, parameters);
   }

   protected abstract SvnCommand getBranchCommand();

   protected BranchScmResult branch(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (BranchScmResult)this.executeCommand(this.getBranchCommand(), repository, fileSet, parameters);
   }

   protected abstract SvnCommand getChangeLogCommand();

   public ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (ChangeLogScmResult)this.executeCommand(this.getChangeLogCommand(), repository, fileSet, parameters);
   }

   protected abstract SvnCommand getCheckInCommand();

   public CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (CheckInScmResult)this.executeCommand(this.getCheckInCommand(), repository, fileSet, parameters);
   }

   protected abstract SvnCommand getCheckOutCommand();

   public CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (CheckOutScmResult)this.executeCommand(this.getCheckOutCommand(), repository, fileSet, parameters);
   }

   protected abstract SvnCommand getDiffCommand();

   public DiffScmResult diff(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (DiffScmResult)this.executeCommand(this.getDiffCommand(), repository, fileSet, parameters);
   }

   protected abstract SvnCommand getExportCommand();

   protected ExportScmResult export(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (ExportScmResult)this.executeCommand(this.getExportCommand(), repository, fileSet, parameters);
   }

   protected abstract SvnCommand getRemoveCommand();

   public RemoveScmResult remove(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (RemoveScmResult)this.executeCommand(this.getRemoveCommand(), repository, fileSet, parameters);
   }

   protected abstract SvnCommand getStatusCommand();

   public StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (StatusScmResult)this.executeCommand(this.getStatusCommand(), repository, fileSet, parameters);
   }

   protected abstract SvnCommand getTagCommand();

   public TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (TagScmResult)this.executeCommand(this.getTagCommand(), repository, fileSet, parameters);
   }

   protected abstract SvnCommand getUpdateCommand();

   public UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (UpdateScmResult)this.executeCommand(this.getUpdateCommand(), repository, fileSet, parameters);
   }

   protected ScmResult executeCommand(SvnCommand command, ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      command.setLogger(this.getLogger());
      return command.execute(repository, fileSet, parameters);
   }

   protected abstract SvnCommand getListCommand();

   public ListScmResult list(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SvnCommand cmd = this.getListCommand();
      return (ListScmResult)this.executeCommand(cmd, repository, fileSet, parameters);
   }

   protected abstract SvnCommand getInfoCommand();

   public InfoScmResult info(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SvnCommand cmd = this.getInfoCommand();
      return (InfoScmResult)this.executeCommand(cmd, repository, fileSet, parameters);
   }

   protected BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SvnCommand cmd = this.getBlameCommand();
      return (BlameScmResult)this.executeCommand(cmd, repository, fileSet, parameters);
   }

   protected abstract SvnCommand getBlameCommand();

   public MkdirScmResult mkdir(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      SvnCommand cmd = this.getMkdirCommand();
      return (MkdirScmResult)this.executeCommand(cmd, repository, fileSet, parameters);
   }

   protected abstract SvnCommand getMkdirCommand();

   public abstract boolean remoteUrlExist(ScmProviderRepository var1, CommandParameters var2) throws ScmException;

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

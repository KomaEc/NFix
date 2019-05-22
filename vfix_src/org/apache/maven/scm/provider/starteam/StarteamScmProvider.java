package org.apache.maven.scm.provider.starteam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.starteam.command.add.StarteamAddCommand;
import org.apache.maven.scm.provider.starteam.command.changelog.StarteamChangeLogCommand;
import org.apache.maven.scm.provider.starteam.command.checkin.StarteamCheckInCommand;
import org.apache.maven.scm.provider.starteam.command.checkout.StarteamCheckOutCommand;
import org.apache.maven.scm.provider.starteam.command.diff.StarteamDiffCommand;
import org.apache.maven.scm.provider.starteam.command.edit.StarteamEditCommand;
import org.apache.maven.scm.provider.starteam.command.remove.StarteamRemoveCommand;
import org.apache.maven.scm.provider.starteam.command.status.StarteamStatusCommand;
import org.apache.maven.scm.provider.starteam.command.tag.StarteamTagCommand;
import org.apache.maven.scm.provider.starteam.command.unedit.StarteamUnEditCommand;
import org.apache.maven.scm.provider.starteam.command.update.StarteamUpdateCommand;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.codehaus.plexus.util.StringUtils;

public class StarteamScmProvider extends AbstractScmProvider {
   public static final String STARTEAM_URL_FORMAT = "[username[:password]@]hostname:port:/projectName/[viewName/][folderHiearchy/]";

   public ScmProviderRepository makeProviderScmRepository(String scmSpecificUrl, char delimiter) throws ScmRepositoryException {
      String user = null;
      String password = null;
      int index = scmSpecificUrl.indexOf(64);
      String rest = scmSpecificUrl;
      if (index != -1) {
         String userAndPassword = scmSpecificUrl.substring(0, index);
         rest = scmSpecificUrl.substring(index + 1);
         index = userAndPassword.indexOf(58);
         if (index != -1) {
            user = userAndPassword.substring(0, index);
            password = userAndPassword.substring(index + 1);
         } else {
            user = userAndPassword;
         }
      }

      String[] tokens = StringUtils.split(rest, Character.toString(delimiter));
      String host;
      int port;
      String path;
      if (tokens.length == 3) {
         host = tokens[0];
         port = Integer.valueOf(tokens[1]);
         path = tokens[2];
      } else {
         if (tokens.length != 2) {
            throw new ScmRepositoryException("Invalid SCM URL: The url has to be on the form: [username[:password]@]hostname:port:/projectName/[viewName/][folderHiearchy/]");
         }

         if (this.getLogger().isWarnEnabled()) {
            this.getLogger().warn("Your scm URL use a deprecated format. The new format is :[username[:password]@]hostname:port:/projectName/[viewName/][folderHiearchy/]");
         }

         host = tokens[0];
         if (tokens[1].indexOf(47) == -1) {
            throw new ScmRepositoryException("Invalid SCM URL: The url has to be on the form: [username[:password]@]hostname:port:/projectName/[viewName/][folderHiearchy/]");
         }

         int at = tokens[1].indexOf(47);
         port = new Integer(tokens[1].substring(0, at));
         path = tokens[1].substring(at);
      }

      try {
         return new StarteamScmProviderRepository(user, password, host, port, path);
      } catch (Exception var12) {
         throw new ScmRepositoryException("Invalid SCM URL: The url has to be on the form: [username[:password]@]hostname:port:/projectName/[viewName/][folderHiearchy/]");
      }
   }

   public String getScmType() {
      return "starteam";
   }

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      fileSet = fixUpScmFileSetAbsoluteFilePath(fileSet);
      StarteamAddCommand command = new StarteamAddCommand();
      command.setLogger(this.getLogger());
      return (AddScmResult)command.execute(repository, fileSet, parameters);
   }

   public ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      fileSet = fixUpScmFileSetAbsoluteFilePath(fileSet);
      StarteamChangeLogCommand command = new StarteamChangeLogCommand();
      command.setLogger(this.getLogger());
      return (ChangeLogScmResult)command.execute(repository, fileSet, parameters);
   }

   public CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      fileSet = fixUpScmFileSetAbsoluteFilePath(fileSet);
      StarteamCheckInCommand command = new StarteamCheckInCommand();
      command.setLogger(this.getLogger());
      return (CheckInScmResult)command.execute(repository, fileSet, parameters);
   }

   public CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      fileSet = fixUpScmFileSetAbsoluteFilePath(fileSet);
      StarteamCheckOutCommand command = new StarteamCheckOutCommand();
      command.setLogger(this.getLogger());
      return (CheckOutScmResult)command.execute(repository, fileSet, parameters);
   }

   public DiffScmResult diff(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      fileSet = fixUpScmFileSetAbsoluteFilePath(fileSet);
      StarteamDiffCommand command = new StarteamDiffCommand();
      command.setLogger(this.getLogger());
      return (DiffScmResult)command.execute(repository, fileSet, parameters);
   }

   public StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      fileSet = fixUpScmFileSetAbsoluteFilePath(fileSet);
      StarteamStatusCommand command = new StarteamStatusCommand();
      command.setLogger(this.getLogger());
      return (StatusScmResult)command.execute(repository, fileSet, parameters);
   }

   public TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      fileSet = fixUpScmFileSetAbsoluteFilePath(fileSet);
      StarteamTagCommand command = new StarteamTagCommand();
      command.setLogger(this.getLogger());
      return (TagScmResult)command.execute(repository, fileSet, parameters);
   }

   public UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      fileSet = fixUpScmFileSetAbsoluteFilePath(fileSet);
      StarteamUpdateCommand command = new StarteamUpdateCommand();
      command.setLogger(this.getLogger());
      return (UpdateScmResult)command.execute(repository, fileSet, parameters);
   }

   protected EditScmResult edit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      fileSet = fixUpScmFileSetAbsoluteFilePath(fileSet);
      StarteamEditCommand command = new StarteamEditCommand();
      command.setLogger(this.getLogger());
      return (EditScmResult)command.execute(repository, fileSet, parameters);
   }

   protected UnEditScmResult unedit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      fileSet = fixUpScmFileSetAbsoluteFilePath(fileSet);
      StarteamUnEditCommand command = new StarteamUnEditCommand();
      command.setLogger(this.getLogger());
      return (UnEditScmResult)command.execute(repository, fileSet, parameters);
   }

   public RemoveScmResult remove(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      fileSet = fixUpScmFileSetAbsoluteFilePath(fileSet);
      StarteamRemoveCommand command = new StarteamRemoveCommand();
      command.setLogger(this.getLogger());
      return (RemoveScmResult)command.execute(repository, fileSet, parameters);
   }

   private static ScmFileSet fixUpScmFileSetAbsoluteFilePath(ScmFileSet currentFileSet) throws ScmException {
      ScmFileSet newFileSet = null;

      try {
         File basedir = getAbsoluteFilePath(currentFileSet.getBasedir());
         List<File> files = currentFileSet.getFileList();
         List<File> relPathFiles = new ArrayList(files.size());

         File file;
         for(Iterator i$ = files.iterator(); i$.hasNext(); relPathFiles.add(file)) {
            file = (File)i$.next();
            if (file.isAbsolute()) {
               file = new File(getRelativePath(basedir, file));
            }
         }

         newFileSet = new ScmFileSet(basedir, relPathFiles);
         return newFileSet;
      } catch (IOException var7) {
         throw new ScmException("Invalid file set.", var7);
      }
   }

   public static String getRelativePath(File basedir, File f) throws ScmException, IOException {
      File fileOrDir = getAbsoluteFilePath(f);
      if (!fileOrDir.getCanonicalPath().startsWith(basedir.getCanonicalPath())) {
         throw new ScmException(fileOrDir.getPath() + " was not contained in " + basedir.getPath());
      } else {
         return basedir.getCanonicalFile().equals(basedir.getAbsoluteFile()) ? fileOrDir.getPath().substring(basedir.getPath().length() + 1, fileOrDir.getPath().length()) : fileOrDir.getPath().substring(basedir.getCanonicalPath().length() + 1, fileOrDir.getPath().length());
      }
   }

   private static File getAbsoluteFilePath(File fileOrDir) throws IOException {
      String javaPathString = fileOrDir.getCanonicalPath().replace('\\', '/');
      if (javaPathString.endsWith("/")) {
         javaPathString = javaPathString.substring(0, javaPathString.length() - 1);
      }

      return new File(javaPathString);
   }
}

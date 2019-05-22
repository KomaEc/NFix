package org.apache.maven.scm.provider.cvslib;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.command.login.LoginScmResult;
import org.apache.maven.scm.command.mkdir.MkdirScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.scm.repository.UnknownRepositoryStructure;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

public abstract class AbstractCvsScmProvider extends AbstractScmProvider {
   public static final String TRANSPORT_EXT = "ext";
   public static final String TRANSPORT_LOCAL = "local";
   public static final String TRANSPORT_LSERVER = "lserver";
   public static final String TRANSPORT_PSERVER = "pserver";
   public static final String TRANSPORT_SSPI = "sspi";

   public String getScmSpecificFilename() {
      return "CVS";
   }

   public String sanitizeTagName(String arg0) {
      if (this.validateTagName(arg0)) {
         return arg0;
      } else if (!arg0.equals("HEAD") && !arg0.equals("BASE") && arg0.matches("[A-Za-z].*")) {
         return arg0.replaceAll("[^A-Za-z0-9_-]", "_");
      } else {
         throw new RuntimeException("Unable to sanitize tag " + arg0 + ": must begin with a letter" + "and not be HEAD or BASE");
      }
   }

   public boolean validateTagName(String arg0) {
      return arg0.matches("[A-Za-z][A-Za-z0-9_-]*") && !arg0.equals("HEAD") && !arg0.equals("BASE");
   }

   public ScmProviderRepository makeProviderScmRepository(String scmSpecificUrl, char delimiter) throws ScmRepositoryException {
      AbstractCvsScmProvider.ScmUrlParserResult result = this.parseScmUrl(scmSpecificUrl, delimiter);
      if (result.getMessages().size() > 0) {
         throw new ScmRepositoryException("The scm url is invalid.", result.getMessages());
      } else {
         return result.getRepository();
      }
   }

   public ScmProviderRepository makeProviderScmRepository(File path) throws ScmRepositoryException, UnknownRepositoryStructure {
      if (path != null && path.isDirectory()) {
         File cvsDirectory = new File(path, "CVS");
         if (!cvsDirectory.exists()) {
            throw new ScmRepositoryException(path.getAbsolutePath() + " isn't a cvs checkout directory.");
         } else {
            File cvsRootFile = new File(cvsDirectory, "Root");
            File moduleFile = new File(cvsDirectory, "Repository");

            String cvsRoot;
            try {
               cvsRoot = FileUtils.fileRead(cvsRootFile).trim().substring(1);
            } catch (IOException var9) {
               throw new ScmRepositoryException("Can't read " + cvsRootFile.getAbsolutePath());
            }

            String module;
            try {
               module = FileUtils.fileRead(moduleFile).trim();
            } catch (IOException var8) {
               throw new ScmRepositoryException("Can't read " + moduleFile.getAbsolutePath());
            }

            return this.makeProviderScmRepository(cvsRoot + ":" + module, ':');
         }
      } else {
         throw new ScmRepositoryException(path.getAbsolutePath() + " isn't a valid directory.");
      }
   }

   public List<String> validateScmUrl(String scmSpecificUrl, char delimiter) {
      AbstractCvsScmProvider.ScmUrlParserResult result = this.parseScmUrl(scmSpecificUrl, delimiter);
      return result.getMessages();
   }

   public String getScmType() {
      return "cvs";
   }

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (AddScmResult)this.executeCommand(this.getAddCommand(), repository, fileSet, parameters);
   }

   public BranchScmResult branch(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (BranchScmResult)this.executeCommand(this.getBranchCommand(), repository, fileSet, parameters);
   }

   protected BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (BlameScmResult)this.executeCommand(this.getBlameCommand(), repository, fileSet, parameters);
   }

   public ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (ChangeLogScmResult)this.executeCommand(this.getChangeLogCommand(), repository, fileSet, parameters);
   }

   public CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (CheckInScmResult)this.executeCommand(this.getCheckInCommand(), repository, fileSet, parameters);
   }

   public CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (CheckOutScmResult)this.executeCommand(this.getCheckOutCommand(), repository, fileSet, parameters);
   }

   public DiffScmResult diff(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (DiffScmResult)this.executeCommand(this.getDiffCommand(), repository, fileSet, parameters);
   }

   protected ExportScmResult export(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (ExportScmResult)this.executeCommand(this.getExportCommand(), repository, fileSet, parameters);
   }

   public LoginScmResult login(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (LoginScmResult)this.executeCommand(this.getLoginCommand(), repository, fileSet, parameters);
   }

   public RemoveScmResult remove(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (RemoveScmResult)this.executeCommand(this.getRemoveCommand(), repository, fileSet, parameters);
   }

   public StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (StatusScmResult)this.executeCommand(this.getStatusCommand(), repository, fileSet, parameters);
   }

   public TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (TagScmResult)this.executeCommand(this.getTagCommand(), repository, fileSet, parameters);
   }

   protected TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters, ScmTagParameters scmParameters) throws ScmException {
      return (TagScmResult)this.getTagCommand().execute(repository, fileSet, parameters);
   }

   public UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (UpdateScmResult)this.executeCommand(this.getUpdateCommand(), repository, fileSet, parameters);
   }

   protected ListScmResult list(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (ListScmResult)this.executeCommand(this.getListCommand(), repository, fileSet, parameters);
   }

   protected MkdirScmResult mkdir(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (MkdirScmResult)this.executeCommand(this.getMkdirCommand(), repository, fileSet, parameters);
   }

   public static String getRelativePath(File basedir, File f) throws ScmException, IOException {
      File fileOrDir = getAbsoluteFilePath(f);
      if (!fileOrDir.getPath().startsWith(basedir.getPath())) {
         throw new ScmException(fileOrDir.getPath() + " was not contained in " + basedir.getPath());
      } else {
         return fileOrDir.getPath().substring(basedir.getPath().length() + 1, fileOrDir.getPath().length());
      }
   }

   protected AbstractCvsScmProvider.ScmUrlParserResult parseScmUrl(String scmSpecificUrl, char delimiter) {
      AbstractCvsScmProvider.ScmUrlParserResult result = new AbstractCvsScmProvider.ScmUrlParserResult();
      String[] tokens = StringUtils.split(scmSpecificUrl, Character.toString(delimiter));
      if (tokens.length < 3) {
         result.getMessages().add("The connection string contains too few tokens.");
         return result;
      } else {
         String transport = tokens[0];
         String cvsroot;
         if (transport.equalsIgnoreCase("local")) {
            cvsroot = tokens[1];
         } else {
            if (!transport.equalsIgnoreCase("pserver") && !transport.equalsIgnoreCase("lserver") && !transport.equalsIgnoreCase("ext") && !transport.equalsIgnoreCase("sspi")) {
               result.getMessages().add("Unknown transport: " + transport);
               return result;
            }

            if (tokens.length != 4 && transport.equalsIgnoreCase("ext")) {
               result.getMessages().add("The connection string contains too few tokens.");
               return result;
            }

            if ((tokens.length < 4 || tokens.length > 6) && transport.equalsIgnoreCase("pserver")) {
               result.getMessages().add("The connection string contains too few tokens.");
               return result;
            }

            if (tokens.length < 4 || tokens.length > 5 && !transport.equalsIgnoreCase("pserver")) {
               result.getMessages().add("The connection string contains too few tokens.");
               return result;
            }

            if (tokens.length < 4 || tokens.length > 5 && transport.equalsIgnoreCase("sspi")) {
               result.getMessages().add("The connection string contains too few tokens.");
               return result;
            }

            if (transport.equalsIgnoreCase("lserver")) {
               cvsroot = tokens[1] + ":" + tokens[2];
            } else if (tokens.length == 4) {
               cvsroot = ":" + transport + ":" + tokens[1] + ":" + tokens[2];
            } else {
               cvsroot = ":" + transport + ":" + tokens[1] + ":" + tokens[2] + ":" + tokens[3];
            }
         }

         String user = null;
         String password = null;
         String host = null;
         String path = null;
         String module = null;
         int port = -1;
         String userHost;
         int index;
         if (transport.equalsIgnoreCase("pserver")) {
            port = 2401;
            if (tokens.length == 4) {
               userHost = tokens[1];
               index = userHost.indexOf(64);
               if (index == -1) {
                  host = userHost;
               } else {
                  user = userHost.substring(0, index);
                  host = userHost.substring(index + 1);
               }

               path = tokens[2];
               module = tokens[3];
            } else if (tokens.length == 6) {
               user = tokens[1];
               userHost = tokens[2];
               index = userHost.indexOf(64);
               if (index == -1) {
                  result.getMessages().add("The user_password_host part must be on the form: <username>:<password>@<hostname>.");
                  return result;
               }

               password = userHost.substring(0, index);
               host = userHost.substring(index + 1);
               port = Integer.valueOf(tokens[3]);
               path = tokens[4];
               module = tokens[5];
            } else {
               if (tokens[1].indexOf(64) > 0) {
                  userHost = tokens[1];
                  index = userHost.indexOf(64);
                  user = userHost.substring(0, index);
                  host = userHost.substring(index + 1);
                  port = new Integer(tokens[2]);
               } else if (tokens[2].indexOf(64) >= 0) {
                  user = tokens[1];
                  userHost = tokens[2];
                  index = userHost.indexOf(64);
                  password = userHost.substring(0, index);
                  host = userHost.substring(index + 1);
               } else {
                  try {
                     port = new Integer(tokens[2]);
                  } catch (Exception var17) {
                     result.getMessages().add("Your scm url is invalid.");
                     return result;
                  }

                  host = tokens[1];
               }

               path = tokens[3];
               module = tokens[4];
            }

            userHost = host;
            if (user != null) {
               userHost = user + "@" + host;
            }

            cvsroot = ":" + transport + ":" + userHost + ":";
            if (port != -1) {
               cvsroot = cvsroot + port;
            }

            cvsroot = cvsroot + path;
         } else if (transport.equalsIgnoreCase("sspi")) {
            userHost = tokens[1];
            index = userHost.indexOf(64);
            if (index == -1) {
               user = "";
               host = userHost;
            } else {
               user = userHost.substring(0, index);
               host = userHost.substring(index + 1);
            }

            if (tokens.length == 4) {
               path = tokens[2];
               module = tokens[3];
            } else {
               try {
                  port = new Integer(tokens[2]);
                  path = tokens[3];
                  module = tokens[4];
               } catch (Exception var16) {
                  result.getMessages().add("Your scm url is invalid, could not get port value.");
                  return result;
               }
            }

            cvsroot = ":" + transport + ":" + host + ":";
            if (port != -1) {
               cvsroot = cvsroot + port;
            }

            cvsroot = cvsroot + path;
         } else {
            if (!transport.equalsIgnoreCase("local")) {
               userHost = tokens[1];
               index = userHost.indexOf(64);
               if (index == -1) {
                  host = userHost;
               } else {
                  user = userHost.substring(0, index);
                  host = userHost.substring(index + 1);
               }
            }

            if (transport.equals("local")) {
               path = tokens[1];
               module = tokens[2];
               if (module != null && module.startsWith("/")) {
                  module = module.substring(1);
               }
            } else if (tokens.length == 4) {
               path = tokens[2];
               module = tokens[3];
            } else {
               port = new Integer(tokens[2]);
               path = tokens[3];
               module = tokens[4];
            }
         }

         if (port == -1) {
            result.setRepository(new CvsScmProviderRepository(cvsroot, transport, user, password, host, path, module));
         } else {
            result.setRepository(new CvsScmProviderRepository(cvsroot, transport, user, password, host, port, path, module));
         }

         return result;
      }
   }

   protected abstract Command getAddCommand();

   protected abstract Command getBranchCommand();

   protected abstract Command getBlameCommand();

   protected abstract Command getChangeLogCommand();

   protected abstract Command getCheckInCommand();

   protected abstract Command getCheckOutCommand();

   protected abstract Command getDiffCommand();

   protected abstract Command getExportCommand();

   protected abstract Command getListCommand();

   protected abstract Command getLoginCommand();

   protected abstract Command getRemoveCommand();

   protected abstract Command getStatusCommand();

   protected abstract Command getTagCommand();

   protected abstract Command getUpdateCommand();

   protected abstract Command getMkdirCommand();

   private ScmResult executeCommand(Command command, ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      fileSet = fixUpScmFileSetAbsoluteFilePath(fileSet);
      command.setLogger(this.getLogger());
      return command.execute(repository, fileSet, parameters);
   }

   private static ScmFileSet fixUpScmFileSetAbsoluteFilePath(ScmFileSet currentFileSet) throws ScmException {
      ScmFileSet newFileSet = null;

      try {
         File basedir = getAbsoluteFilePath(currentFileSet.getBasedir());
         List<File> fixedFiles = new ArrayList(currentFileSet.getFileList().size());
         Iterator i$ = currentFileSet.getFileList().iterator();

         while(i$.hasNext()) {
            File file = (File)i$.next();
            if (file.isAbsolute()) {
               fixedFiles.add(new File(getRelativePath(basedir, file)));
            } else {
               fixedFiles.add(file);
            }
         }

         newFileSet = new ScmFileSet(basedir, fixedFiles);
         return newFileSet;
      } catch (IOException var6) {
         throw new ScmException("Invalid file set.", var6);
      }
   }

   private static File getAbsoluteFilePath(File fileOrDir) throws IOException {
      String javaPathString = fileOrDir.getCanonicalPath().replace('\\', '/');
      if (javaPathString.endsWith("/")) {
         javaPathString = javaPathString.substring(0, javaPathString.length() - 1);
      }

      return new File(javaPathString);
   }

   public static class ScmUrlParserResult {
      private List<String> messages = new ArrayList();
      private ScmProviderRepository repository;

      public List<String> getMessages() {
         return this.messages;
      }

      public void setMessages(List<String> messages) {
         this.messages = messages;
      }

      public ScmProviderRepository getRepository() {
         return this.repository;
      }

      public void setRepository(ScmProviderRepository repository) {
         this.repository = repository;
      }

      public void resetMessages() {
         this.messages = new ArrayList();
      }
   }
}

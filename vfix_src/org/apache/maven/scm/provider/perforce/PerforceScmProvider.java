package org.apache.maven.scm.provider.perforce;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.command.login.LoginScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.AbstractScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.command.PerforceInfoCommand;
import org.apache.maven.scm.provider.perforce.command.PerforceWhereCommand;
import org.apache.maven.scm.provider.perforce.command.add.PerforceAddCommand;
import org.apache.maven.scm.provider.perforce.command.blame.PerforceBlameCommand;
import org.apache.maven.scm.provider.perforce.command.changelog.PerforceChangeLogCommand;
import org.apache.maven.scm.provider.perforce.command.checkin.PerforceCheckInCommand;
import org.apache.maven.scm.provider.perforce.command.checkout.PerforceCheckOutCommand;
import org.apache.maven.scm.provider.perforce.command.diff.PerforceDiffCommand;
import org.apache.maven.scm.provider.perforce.command.edit.PerforceEditCommand;
import org.apache.maven.scm.provider.perforce.command.login.PerforceLoginCommand;
import org.apache.maven.scm.provider.perforce.command.remove.PerforceRemoveCommand;
import org.apache.maven.scm.provider.perforce.command.status.PerforceStatusCommand;
import org.apache.maven.scm.provider.perforce.command.tag.PerforceTagCommand;
import org.apache.maven.scm.provider.perforce.command.unedit.PerforceUnEditCommand;
import org.apache.maven.scm.provider.perforce.command.update.PerforceUpdateCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceScmProvider extends AbstractScmProvider {
   private static final String[] PROTOCOLS = new String[]{"tcp", "tcp4", "tcp6", "tcp46", "tcp64", "ssl", "ssl4", "ssl6", "ssl46", "ssl64"};
   private static final String NEWLINE = "\r\n";
   public static final String DEFAULT_CLIENTSPEC_PROPERTY = "maven.scm.perforce.clientspec.name";
   private static Boolean live = null;

   public boolean requiresEditMode() {
      return true;
   }

   public ScmProviderRepository makeProviderScmRepository(String scmSpecificUrl, char delimiter) throws ScmRepositoryException {
      String protocol = null;
      int port = 0;
      String host = null;
      int i0 = scmSpecificUrl.indexOf(delimiter);
      if (i0 > 0) {
         protocol = scmSpecificUrl.substring(0, i0);
         HashSet<String> protocols = new HashSet(Arrays.asList(PROTOCOLS));
         if (protocols.contains(protocol)) {
            scmSpecificUrl = scmSpecificUrl.substring(i0 + 1);
         } else {
            protocol = null;
         }
      }

      int i1 = scmSpecificUrl.indexOf(delimiter);
      int i2 = scmSpecificUrl.indexOf(delimiter, i1 + 1);
      String path;
      String password;
      if (i1 > 0) {
         int lastDelimiter = scmSpecificUrl.lastIndexOf(delimiter);
         path = scmSpecificUrl.substring(lastDelimiter + 1);
         host = scmSpecificUrl.substring(0, i1);
         if (i2 >= 0) {
            try {
               password = scmSpecificUrl.substring(i1 + 1, lastDelimiter);
               port = Integer.parseInt(password);
            } catch (NumberFormatException var12) {
               throw new ScmRepositoryException("The port has to be a number.");
            }
         }
      } else {
         path = scmSpecificUrl;
      }

      String user = null;
      password = null;
      if (host != null && host.indexOf(64) > 1) {
         user = host.substring(0, host.indexOf(64));
         host = host.substring(host.indexOf(64) + 1);
      }

      if (path.indexOf(64) > 1) {
         if (host != null && this.getLogger().isWarnEnabled()) {
            this.getLogger().warn("Username as part of path is deprecated, the new format is scm:perforce:[username@]host:port:path_to_repository");
         }

         user = path.substring(0, path.indexOf(64));
         path = path.substring(path.indexOf(64) + 1);
      }

      return new PerforceScmProviderRepository(protocol, host, port, path, user, password);
   }

   public String getScmType() {
      return "perforce";
   }

   protected ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      PerforceChangeLogCommand command = new PerforceChangeLogCommand();
      command.setLogger(this.getLogger());
      return (ChangeLogScmResult)command.execute(repository, fileSet, parameters);
   }

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      PerforceAddCommand command = new PerforceAddCommand();
      command.setLogger(this.getLogger());
      return (AddScmResult)command.execute(repository, fileSet, params);
   }

   protected RemoveScmResult remove(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      PerforceRemoveCommand command = new PerforceRemoveCommand();
      command.setLogger(this.getLogger());
      return (RemoveScmResult)command.execute(repository, fileSet, params);
   }

   protected CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      PerforceCheckInCommand command = new PerforceCheckInCommand();
      command.setLogger(this.getLogger());
      return (CheckInScmResult)command.execute(repository, fileSet, params);
   }

   protected CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      PerforceCheckOutCommand command = new PerforceCheckOutCommand();
      command.setLogger(this.getLogger());
      return (CheckOutScmResult)command.execute(repository, fileSet, params);
   }

   protected DiffScmResult diff(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      PerforceDiffCommand command = new PerforceDiffCommand();
      command.setLogger(this.getLogger());
      return (DiffScmResult)command.execute(repository, fileSet, params);
   }

   protected EditScmResult edit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      PerforceEditCommand command = new PerforceEditCommand();
      command.setLogger(this.getLogger());
      return (EditScmResult)command.execute(repository, fileSet, params);
   }

   protected LoginScmResult login(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      PerforceLoginCommand command = new PerforceLoginCommand();
      command.setLogger(this.getLogger());
      return (LoginScmResult)command.execute(repository, fileSet, params);
   }

   protected StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      PerforceStatusCommand command = new PerforceStatusCommand();
      command.setLogger(this.getLogger());
      return (StatusScmResult)command.execute(repository, fileSet, params);
   }

   protected TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      PerforceTagCommand command = new PerforceTagCommand();
      command.setLogger(this.getLogger());
      return (TagScmResult)command.execute(repository, fileSet, params);
   }

   protected UnEditScmResult unedit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      PerforceUnEditCommand command = new PerforceUnEditCommand();
      command.setLogger(this.getLogger());
      return (UnEditScmResult)command.execute(repository, fileSet, params);
   }

   protected UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      PerforceUpdateCommand command = new PerforceUpdateCommand();
      command.setLogger(this.getLogger());
      return (UpdateScmResult)command.execute(repository, fileSet, params);
   }

   protected BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      PerforceBlameCommand command = new PerforceBlameCommand();
      command.setLogger(this.getLogger());
      return (BlameScmResult)command.execute(repository, fileSet, params);
   }

   public static Commandline createP4Command(PerforceScmProviderRepository repo, File workingDir) {
      Commandline command = new Commandline();
      command.setExecutable("p4");
      if (workingDir != null) {
         command.createArg().setValue("-d");
         command.createArg().setValue(workingDir.getAbsolutePath());
      }

      if (repo.getHost() != null) {
         command.createArg().setValue("-p");
         String value = "";
         if (!StringUtils.isBlank(repo.getProtocol())) {
            value = value + repo.getProtocol() + ":";
         }

         value = value + repo.getHost();
         if (repo.getPort() != 0) {
            value = value + ":" + Integer.toString(repo.getPort());
         }

         command.createArg().setValue(value);
      }

      if (StringUtils.isNotEmpty(repo.getUser())) {
         command.createArg().setValue("-u");
         command.createArg().setValue(repo.getUser());
      }

      if (StringUtils.isNotEmpty(repo.getPassword())) {
         command.createArg().setValue("-P");
         command.createArg().setValue(repo.getPassword());
      }

      return command;
   }

   public static String clean(String string) {
      if (string.indexOf(" -P ") == -1) {
         return string;
      } else {
         int idx = string.indexOf(" -P ") + 4;
         int end = string.indexOf(32, idx);
         return string.substring(0, idx) + StringUtils.repeat("*", end - idx) + string.substring(end);
      }
   }

   public static String getCanonicalRepoPath(String repoPath) {
      if (repoPath.endsWith("/...")) {
         return repoPath;
      } else {
         return repoPath.endsWith("/") ? repoPath + "..." : repoPath + "/...";
      }
   }

   public static String createClientspec(ScmLogger logger, PerforceScmProviderRepository repo, File workDir, String repoPath) {
      String clientspecName = getClientspecName(logger, repo, workDir);
      String userName = getUsername(logger, repo);

      String rootDir;
      try {
         rootDir = workDir.getCanonicalPath();
      } catch (IOException var8) {
         rootDir = workDir.getAbsolutePath();
      }

      StringBuilder buf = new StringBuilder();
      buf.append("Client: ").append(clientspecName).append("\r\n");
      buf.append("Root: ").append(rootDir).append("\r\n");
      buf.append("Owner: ").append(userName).append("\r\n");
      buf.append("View:").append("\r\n");
      buf.append("\t").append(getCanonicalRepoPath(repoPath));
      buf.append(" //").append(clientspecName).append("/...").append("\r\n");
      buf.append("Description:").append("\r\n");
      buf.append("\t").append("Created by maven-scm-provider-perforce").append("\r\n");
      return buf.toString();
   }

   public static String getClientspecName(ScmLogger logger, PerforceScmProviderRepository repo, File workDir) {
      String def = generateDefaultClientspecName(logger, repo, workDir);
      String l = System.getProperty("maven.scm.perforce.clientspec.name", def);
      return l != null && !"".equals(l.trim()) ? l : def;
   }

   private static String generateDefaultClientspecName(ScmLogger logger, PerforceScmProviderRepository repo, File workDir) {
      String username = getUsername(logger, repo);

      String hostname;
      String path;
      try {
         hostname = InetAddress.getLocalHost().getHostName();
         path = workDir.getCanonicalPath().replaceAll("[/ ~]", "-");
      } catch (UnknownHostException var7) {
         throw new RuntimeException(var7);
      } catch (IOException var8) {
         throw new RuntimeException(var8);
      }

      return username + "-" + hostname + "-MavenSCM-" + path;
   }

   private static String getUsername(ScmLogger logger, PerforceScmProviderRepository repo) {
      String username = PerforceInfoCommand.getInfo(logger, repo).getEntry("User name");
      if (username == null) {
         username = repo.getUser();
         if (username == null) {
            username = System.getProperty("user.name", "nouser");
         }
      }

      return username;
   }

   public static String getRepoPath(ScmLogger log, PerforceScmProviderRepository repo, File basedir) {
      PerforceWhereCommand where = new PerforceWhereCommand(log, repo);
      if (basedir.toString().replace('\\', '/').endsWith("/target/checkout")) {
         String dir = basedir.toString();
         basedir = new File(dir.substring(0, dir.length() - "/target/checkout".length()));
         log.debug("Fixing checkout URL: " + basedir);
      }

      File pom = new File(basedir, "pom.xml");
      String loc = repo.getPath();
      log.debug("SCM path in pom: " + loc);
      if (pom.exists()) {
         loc = where.getDepotLocation(pom);
         if (loc == null) {
            loc = repo.getPath();
            log.debug("cannot find depot => using " + loc);
         } else if (loc.endsWith("/pom.xml")) {
            loc = loc.substring(0, loc.length() - "/pom.xml".length());
            log.debug("Actual POM location: " + loc);
            if (!repo.getPath().equals(loc)) {
               log.info("The SCM location in your pom.xml (" + repo.getPath() + ") is not equal to the depot location (" + loc + ").  This happens frequently with branches.  " + "Ignoring the SCM location.");
            }
         }
      }

      return loc;
   }

   public static boolean isLive() {
      if (live == null) {
         if (!Boolean.getBoolean("maven.scm.testing")) {
            live = Boolean.TRUE;
         } else {
            try {
               Commandline command = new Commandline();
               command.setExecutable("p4");
               Process proc = command.execute();
               BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

               while(true) {
                  if (br.readLine() == null) {
                     int rc = proc.exitValue();
                     live = rc == 0 ? Boolean.TRUE : Boolean.FALSE;
                     break;
                  }
               }
            } catch (Exception var5) {
               var5.printStackTrace();
               live = Boolean.FALSE;
            }
         }
      }

      return live;
   }
}

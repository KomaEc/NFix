package org.apache.maven.scm.provider.starteam.command.checkin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkin.AbstractCheckInCommand;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.starteam.command.StarteamCommand;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class StarteamCheckInCommand extends AbstractCheckInCommand implements StarteamCommand {
   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message, ScmVersion version) throws ScmException {
      String issueType = System.getProperty("maven.scm.issue.type");
      String issueValue = System.getProperty("maven.scm.issue.value");
      String deprecatedIssue = System.getProperty("maven.scm.issue");
      if (deprecatedIssue != null && deprecatedIssue.trim().length() > 0) {
         issueType = "cr";
         issueValue = deprecatedIssue;
      }

      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Working directory: " + fileSet.getBasedir().getAbsolutePath());
      }

      StarteamScmProviderRepository repository = (StarteamScmProviderRepository)repo;
      StarteamCheckInConsumer consumer = new StarteamCheckInConsumer(this.getLogger(), fileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      List<File> checkInFiles = fileSet.getFileList();
      if (checkInFiles.isEmpty()) {
         Commandline cl = createCommandLine(repository, fileSet, message, version, issueType, issueValue);
         int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
         if (exitCode != 0) {
            return new CheckInScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false);
         }
      } else {
         for(int i = 0; i < checkInFiles.size(); ++i) {
            ScmFileSet checkInFile = new ScmFileSet(fileSet.getBasedir(), (File)checkInFiles.get(i));
            Commandline cl = createCommandLine(repository, checkInFile, message, version, issueType, issueValue);
            int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
            if (exitCode != 0) {
               return new CheckInScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false);
            }
         }
      }

      return new CheckInScmResult((String)null, consumer.getCheckedInFiles());
   }

   public static Commandline createCommandLine(StarteamScmProviderRepository repo, ScmFileSet fileSet, String message, ScmVersion version, String issueType, String issueValue) {
      List<String> args = new ArrayList();
      if (message != null && message.length() != 0) {
         args.add("-r");
         args.add(message);
      }

      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         args.add("-vl");
         args.add(version.getName());
      }

      if (issueType != null && issueType.trim().length() > 0) {
         args.add("-" + issueType.trim());
         if (issueValue != null && issueValue.trim().length() > 0) {
            args.add(issueValue.trim());
         }
      }

      boolean checkinDirectory = fileSet.getFileList().size() == 0;
      if (!checkinDirectory && fileSet.getFileList().size() != 0) {
         File subFile = (File)fileSet.getFileList().get(0);
         checkinDirectory = subFile.isDirectory();
      }

      if (checkinDirectory) {
         args.add("-f");
         args.add("NCI");
      }

      StarteamCommandLineUtils.addEOLOption(args);
      return StarteamCommandLineUtils.createStarteamCommandLine("ci", args, fileSet, repo);
   }
}

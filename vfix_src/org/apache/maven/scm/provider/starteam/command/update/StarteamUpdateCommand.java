package org.apache.maven.scm.provider.starteam.command.update;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.update.AbstractUpdateCommand;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.starteam.command.StarteamCommand;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.apache.maven.scm.provider.starteam.command.changelog.StarteamChangeLogCommand;
import org.apache.maven.scm.provider.starteam.command.checkout.StarteamCheckOutConsumer;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.DefaultConsumer;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class StarteamUpdateCommand extends AbstractUpdateCommand implements StarteamCommand {
   protected UpdateScmResult executeUpdateCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Working directory: " + fileSet.getBasedir().getAbsolutePath());
      }

      StarteamScmProviderRepository repository = (StarteamScmProviderRepository)repo;
      StarteamCheckOutConsumer consumer = new StarteamCheckOutConsumer(this.getLogger(), fileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      List<File> updateFiles = fileSet.getFileList();
      if (updateFiles.size() == 0) {
         Commandline cl = createCommandLine(repository, fileSet, version);
         int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
         if (exitCode != 0) {
            return new UpdateScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false);
         }

         String doDeleteLocal = System.getProperty("maven.scm.starteam.deleteLocal");
         if ("true".equalsIgnoreCase(doDeleteLocal)) {
            this.deleteLocal(repository, fileSet, version);
         }
      } else {
         for(int i = 0; i < updateFiles.size(); ++i) {
            File updateFile = (File)updateFiles.get(i);
            ScmFileSet scmFileSet = new ScmFileSet(fileSet.getBasedir(), updateFile);
            Commandline cl = createCommandLine(repository, scmFileSet, version);
            int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
            if (exitCode != 0) {
               return new UpdateScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false);
            }
         }
      }

      return new UpdateScmResult((String)null, consumer.getCheckedOutFiles());
   }

   public static Commandline createCommandLine(StarteamScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version) {
      List<String> args = new ArrayList();
      args.add("-merge");
      args.add("-neverprompt");
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         args.add("-vl");
         args.add(version.getName());
      }

      StarteamCommandLineUtils.addEOLOption(args);
      return StarteamCommandLineUtils.createStarteamCommandLine("co", args, fileSet, repo);
   }

   protected ChangeLogCommand getChangeLogCommand() {
      StarteamChangeLogCommand command = new StarteamChangeLogCommand();
      command.setLogger(this.getLogger());
      return command;
   }

   private void deleteLocal(StarteamScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      if (fileSet.getFileList().size() == 0) {
         Commandline cl = createDeleteLocalCommand(repo, fileSet, version);
         StreamConsumer consumer = new DefaultConsumer();
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
         if (exitCode != 0) {
            throw new ScmException("Error executing delete-local: " + stderr.toString());
         }
      }
   }

   public static Commandline createDeleteLocalCommand(StarteamScmProviderRepository repo, ScmFileSet dir, ScmVersion version) {
      List<String> args = new ArrayList();
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         args.add("-cfgl");
         args.add(version.getName());
      }

      args.add("-filter");
      args.add("N");
      return StarteamCommandLineUtils.createStarteamCommandLine("delete-local", args, dir, repo);
   }
}

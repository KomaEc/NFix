package org.apache.maven.scm.provider.starteam.command.unedit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.unedit.AbstractUnEditCommand;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.starteam.command.StarteamCommand;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class StarteamUnEditCommand extends AbstractUnEditCommand implements StarteamCommand {
   protected ScmResult executeUnEditCommand(ScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Working directory: " + fileSet.getBasedir().getAbsolutePath());
      }

      StarteamScmProviderRepository repository = (StarteamScmProviderRepository)repo;
      StarteamUnEditConsumer consumer = new StarteamUnEditConsumer(this.getLogger(), fileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      List<File> unlockFiles = fileSet.getFileList();
      if (unlockFiles.size() == 0) {
         Commandline cl = createCommandLine(repository, fileSet);
         int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
         if (exitCode != 0) {
            return new UnEditScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false);
         }
      } else {
         for(int i = 0; i < unlockFiles.size(); ++i) {
            ScmFileSet unlockFile = new ScmFileSet(fileSet.getBasedir(), (File)unlockFiles.get(i));
            Commandline cl = createCommandLine(repository, unlockFile);
            int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
            if (exitCode != 0) {
               return new UnEditScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false);
            }
         }
      }

      return new UnEditScmResult((String)null, consumer.getUnEditFiles());
   }

   public static Commandline createCommandLine(StarteamScmProviderRepository repo, ScmFileSet dirOrFile) {
      List<String> args = new ArrayList();
      args.add("-u");
      return StarteamCommandLineUtils.createStarteamCommandLine("lck", args, dirOrFile, repo);
   }
}

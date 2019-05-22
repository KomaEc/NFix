package org.apache.maven.scm.provider.starteam.command.remove;

import java.io.File;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.remove.AbstractRemoveCommand;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.starteam.command.StarteamCommand;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.apache.maven.scm.provider.starteam.command.checkin.StarteamCheckInConsumer;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class StarteamRemoveCommand extends AbstractRemoveCommand implements StarteamCommand {
   protected ScmResult executeRemoveCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message) throws ScmException {
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Working directory: " + fileSet.getBasedir().getAbsolutePath());
      }

      StarteamScmProviderRepository repository = (StarteamScmProviderRepository)repo;
      StarteamCheckInConsumer consumer = new StarteamCheckInConsumer(this.getLogger(), fileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      List<File> remvoveFiles = fileSet.getFileList();
      if (remvoveFiles.size() == 0) {
         Commandline cl = createCommandLine(repository, fileSet);
         int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
         if (exitCode != 0) {
            return new RemoveScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false);
         }
      } else {
         for(int i = 0; i < remvoveFiles.size(); ++i) {
            File fileToBeRemoved = (File)remvoveFiles.get(i);
            ScmFileSet scmFileSet = new ScmFileSet(fileSet.getBasedir(), fileToBeRemoved);
            Commandline cl = createCommandLine(repository, scmFileSet);
            int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
            if (exitCode != 0) {
               return new RemoveScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false);
            }
         }
      }

      return new RemoveScmResult((String)null, consumer.getCheckedInFiles());
   }

   public static Commandline createCommandLine(StarteamScmProviderRepository repo, ScmFileSet dirOrFile) {
      return StarteamCommandLineUtils.createStarteamCommandLine("remove", (List)null, dirOrFile, repo);
   }
}

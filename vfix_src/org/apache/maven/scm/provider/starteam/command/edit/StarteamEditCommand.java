package org.apache.maven.scm.provider.starteam.command.edit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.edit.AbstractEditCommand;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.starteam.command.StarteamCommand;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class StarteamEditCommand extends AbstractEditCommand implements StarteamCommand {
   protected ScmResult executeEditCommand(ScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Working directory: " + fileSet.getBasedir().getAbsolutePath());
      }

      StarteamScmProviderRepository repository = (StarteamScmProviderRepository)repo;
      StarteamEditConsumer consumer = new StarteamEditConsumer(this.getLogger(), fileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      List<File> editFiles = fileSet.getFileList();
      if (editFiles.size() == 0) {
         Commandline cl = createCommandLine(repository, fileSet);
         int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
         if (exitCode != 0) {
            return new EditScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false);
         }
      } else {
         for(int i = 0; i < editFiles.size(); ++i) {
            ScmFileSet editFile = new ScmFileSet(fileSet.getBasedir(), (File)editFiles.get(i));
            Commandline cl = createCommandLine(repository, editFile);
            int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
            if (exitCode != 0) {
               return new EditScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false);
            }
         }
      }

      return new EditScmResult((String)null, consumer.getEditedFiles());
   }

   public static Commandline createCommandLine(StarteamScmProviderRepository repo, ScmFileSet dirOrFile) {
      List<String> args = new ArrayList();
      args.add("-l");
      return StarteamCommandLineUtils.createStarteamCommandLine("lck", args, dirOrFile, repo);
   }
}

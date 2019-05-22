package org.apache.maven.scm.provider.starteam.command.add;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AbstractAddCommand;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.starteam.command.StarteamCommand;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class StarteamAddCommand extends AbstractAddCommand implements StarteamCommand {
   protected ScmResult executeAddCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message, boolean binary) throws ScmException {
      String issue = System.getProperty("maven.scm.issue");
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Working directory: " + fileSet.getBasedir().getAbsolutePath());
      }

      StarteamScmProviderRepository repository = (StarteamScmProviderRepository)repo;
      StarteamAddConsumer consumer = new StarteamAddConsumer(this.getLogger(), fileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      Iterator i$ = fileSet.getFileList().iterator();

      Commandline cl;
      int exitCode;
      do {
         if (!i$.hasNext()) {
            return new AddScmResult((String)null, consumer.getAddedFiles());
         }

         File fileToBeAdded = (File)i$.next();
         ScmFileSet scmFile = new ScmFileSet(fileSet.getBasedir(), fileToBeAdded);
         cl = createCommandLine(repository, scmFile, issue);
         exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
      } while(exitCode == 0);

      return new AddScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false);
   }

   static Commandline createCommandLine(StarteamScmProviderRepository repo, ScmFileSet scmFileSet, String issue) {
      List<String> args = new ArrayList();
      if (issue != null && issue.length() != 0) {
         args.add("-cr");
         args.add(issue);
      }

      StarteamCommandLineUtils.addEOLOption(args);
      return StarteamCommandLineUtils.createStarteamCommandLine("add", args, scmFileSet, repo);
   }
}

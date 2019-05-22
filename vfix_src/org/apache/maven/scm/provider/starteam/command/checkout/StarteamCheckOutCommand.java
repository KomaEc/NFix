package org.apache.maven.scm.provider.starteam.command.checkout;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkout.AbstractCheckOutCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.starteam.command.StarteamCommand;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class StarteamCheckOutCommand extends AbstractCheckOutCommand implements StarteamCommand {
   protected CheckOutScmResult executeCheckOutCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version, boolean recursive) throws ScmException {
      if (fileSet.getFileList().size() != 0) {
         throw new ScmException("This provider doesn't support checking out subsets of a directory");
      } else {
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("Working directory: " + fileSet.getBasedir().getAbsolutePath());
         }

         StarteamScmProviderRepository repository = (StarteamScmProviderRepository)repo;
         StarteamCheckOutConsumer consumer = new StarteamCheckOutConsumer(this.getLogger(), fileSet.getBasedir());
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         Commandline cl = createCommandLine(repository, fileSet, version);
         int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
         return exitCode != 0 ? new CheckOutScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false) : new CheckOutScmResult(cl.toString(), consumer.getCheckedOutFiles());
      }
   }

   public static Commandline createCommandLine(StarteamScmProviderRepository repo, ScmFileSet baseDir, ScmVersion version) {
      List<String> args = new ArrayList();
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         args.add("-vl");
         args.add(version.getName());
      }

      StarteamCommandLineUtils.addEOLOption(args);
      return StarteamCommandLineUtils.createStarteamCommandLine("co", args, baseDir, repo);
   }
}

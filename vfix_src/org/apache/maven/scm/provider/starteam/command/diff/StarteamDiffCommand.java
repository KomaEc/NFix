package org.apache.maven.scm.provider.starteam.command.diff;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.diff.AbstractDiffCommand;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.starteam.command.StarteamCommand;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class StarteamDiffCommand extends AbstractDiffCommand implements StarteamCommand {
   protected DiffScmResult executeDiffCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion) throws ScmException {
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Working directory: " + fileSet.getBasedir().getAbsolutePath());
      }

      if (fileSet.getFileList().isEmpty()) {
         throw new ScmException("This provider doesn't support diff command on a subsets of a directory");
      } else {
         StarteamScmProviderRepository repository = (StarteamScmProviderRepository)repo;
         StarteamDiffConsumer consumer = new StarteamDiffConsumer(this.getLogger(), fileSet.getBasedir());
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         Commandline cl = createCommandLine(repository, fileSet, startVersion, endVersion);
         int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
         return exitCode != 0 ? new DiffScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false) : new DiffScmResult(cl.toString(), consumer.getChangedFiles(), consumer.getDifferences(), consumer.getPatch());
      }
   }

   public static Commandline createCommandLine(StarteamScmProviderRepository repo, ScmFileSet workingDirectory, ScmVersion startLabel, ScmVersion endLabel) throws ScmException {
      List<String> args = new ArrayList();
      args.add("-filter");
      args.add("M");
      if (startLabel != null && StringUtils.isNotEmpty(startLabel.getName())) {
         args.add("-vl");
         args.add(startLabel.getName());
      }

      if (endLabel != null && StringUtils.isNotEmpty(endLabel.getName())) {
         args.add("-vl");
         args.add(endLabel.getName());
      }

      if (endLabel == null || startLabel != null && !StringUtils.isEmpty(startLabel.getName())) {
         StarteamCommandLineUtils.addEOLOption(args);
         return StarteamCommandLineUtils.createStarteamCommandLine("diff", args, workingDirectory, repo);
      } else {
         throw new ScmException("Missing start label.");
      }
   }
}

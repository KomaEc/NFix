package org.apache.maven.scm.provider.git.gitexe.command.diff;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.diff.AbstractDiffCommand;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.command.diff.GitDiffConsumer;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitDiffCommand extends AbstractDiffCommand implements GitCommand {
   protected DiffScmResult executeDiffCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion) throws ScmException {
      GitDiffConsumer consumer = new GitDiffConsumer(this.getLogger(), fileSet.getBasedir());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      Commandline clDiff2Index = createCommandLine(fileSet.getBasedir(), startVersion, endVersion, false);
      int exitCode = GitCommandLineUtils.execute(clDiff2Index, (StreamConsumer)consumer, stderr, this.getLogger());
      if (exitCode != 0) {
         return new DiffScmResult(clDiff2Index.toString(), "The git-diff command failed.", stderr.getOutput(), false);
      } else {
         Commandline clDiff2Head = createCommandLine(fileSet.getBasedir(), startVersion, endVersion, true);
         exitCode = GitCommandLineUtils.execute(clDiff2Head, (StreamConsumer)consumer, stderr, this.getLogger());
         return exitCode != 0 ? new DiffScmResult(clDiff2Head.toString(), "The git-diff command failed.", stderr.getOutput(), false) : new DiffScmResult(clDiff2Index.toString(), consumer.getChangedFiles(), consumer.getDifferences(), consumer.getPatch());
      }
   }

   public static Commandline createCommandLine(File workingDirectory, ScmVersion startVersion, ScmVersion endVersion, boolean cached) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "diff");
      if (cached) {
         cl.createArg().setValue("--cached");
      }

      if (startVersion != null && StringUtils.isNotEmpty(startVersion.getName())) {
         cl.createArg().setValue(startVersion.getName());
      }

      if (endVersion != null && StringUtils.isNotEmpty(endVersion.getName())) {
         cl.createArg().setValue(endVersion.getName());
      }

      return cl;
   }

   public static Commandline createDiffRawCommandLine(File workingDirectory, String sha1) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "diff");
      cl.createArg().setValue("--raw");
      cl.createArg().setValue(sha1);
      return cl;
   }
}

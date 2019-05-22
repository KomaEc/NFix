package org.apache.maven.scm.provider.git.gitexe.command.blame;

import java.io.File;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.blame.AbstractBlameCommand;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitBlameCommand extends AbstractBlameCommand implements GitCommand {
   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet workingDirectory, CommandParameters parameters) throws ScmException {
      String filename = parameters.getString(CommandParameter.FILE);
      Commandline cl = createCommandLine(workingDirectory.getBasedir(), filename, parameters.getBoolean(CommandParameter.IGNORE_WHITESPACE, false));
      GitBlameConsumer consumer = new GitBlameConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      int exitCode = GitCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      return exitCode != 0 ? new BlameScmResult(cl.toString(), "The git blame command failed.", stderr.getOutput(), false) : new BlameScmResult(cl.toString(), consumer.getLines());
   }

   public BlameScmResult executeBlameCommand(ScmProviderRepository repo, ScmFileSet workingDirectory, String filename) throws ScmException {
      CommandParameters commandParameters = new CommandParameters();
      commandParameters.setString(CommandParameter.FILE, filename);
      commandParameters.setString(CommandParameter.IGNORE_WHITESPACE, Boolean.FALSE.toString());
      return (BlameScmResult)this.execute(repo, workingDirectory, commandParameters);
   }

   protected static Commandline createCommandLine(File workingDirectory, String filename, boolean ignoreWhitespace) {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "blame");
      cl.createArg().setValue("--porcelain");
      cl.createArg().setValue(filename);
      if (ignoreWhitespace) {
         cl.createArg().setValue("-w");
      }

      return cl;
   }
}

package org.apache.maven.scm.provider.git.gitexe.command.info;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitInfoCommand extends AbstractCommand implements GitCommand {
   public static final int NO_REVISION_LENGTH = -1;

   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      GitInfoConsumer consumer = new GitInfoConsumer(this.getLogger(), fileSet);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      Commandline cli = createCommandLine(repository, fileSet, parameters);
      int exitCode = GitCommandLineUtils.execute(cli, (StreamConsumer)consumer, stderr, this.getLogger());
      return exitCode != 0 ? new InfoScmResult(cli.toString(), "The git rev-parse command failed.", stderr.getOutput(), false) : new InfoScmResult(cli.toString(), consumer.getInfoItems());
   }

   public static Commandline createCommandLine(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      Commandline cli = GitCommandLineUtils.getBaseGitCommandLine(fileSet.getBasedir(), "rev-parse");
      cli.createArg().setValue("--verify");
      int revLength = getRevisionLength(parameters);
      if (revLength > -1) {
         cli.createArg().setValue("--short=" + revLength);
      }

      cli.createArg().setValue("HEAD");
      return cli;
   }

   private static int getRevisionLength(CommandParameters parameters) throws ScmException {
      return parameters == null ? -1 : parameters.getInt(CommandParameter.SCM_SHORT_REVISION_LENGTH, -1);
   }
}

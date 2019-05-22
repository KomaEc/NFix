package org.apache.maven.scm.provider.starteam.command.changelog;

import java.util.Date;
import java.util.List;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.changelog.AbstractChangeLogCommand;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.starteam.command.StarteamCommand;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class StarteamChangeLogCommand extends AbstractChangeLogCommand implements StarteamCommand {
   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern) throws ScmException {
      if ((branch != null || StringUtils.isNotEmpty(branch == null ? null : branch.getName())) && this.getLogger().isWarnEnabled()) {
         this.getLogger().warn("This provider doesn't support changelog with on a given branch.");
      }

      StarteamScmProviderRepository repository = (StarteamScmProviderRepository)repo;
      Commandline cl = createCommandLine(repository, fileSet, startDate);
      StarteamChangeLogConsumer consumer = new StarteamChangeLogConsumer(fileSet.getBasedir(), this.getLogger(), startDate, endDate, datePattern);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var13) {
         throw new ScmException("Error while executing command.", var13);
      }

      return exitCode != 0 ? new ChangeLogScmResult(cl.toString(), "The 'stcmd' command failed.", stderr.getOutput(), false) : new ChangeLogScmResult(cl.toString(), new ChangeLogSet(consumer.getModifications(), startDate, endDate));
   }

   public static Commandline createCommandLine(StarteamScmProviderRepository repo, ScmFileSet workingDirectory, Date startDate) {
      return StarteamCommandLineUtils.createStarteamCommandLine("hist", (List)null, workingDirectory, repo);
   }
}

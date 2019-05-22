package org.apache.maven.scm.provider.git.gitexe.command.changelog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.AbstractChangeLogCommand;
import org.apache.maven.scm.command.changelog.ChangeLogScmRequest;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitChangeLogCommand extends AbstractChangeLogCommand implements GitCommand {
   private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z";

   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion, String datePattern) throws ScmException {
      return this.executeChangeLogCommand(repo, fileSet, (Date)null, (Date)null, (ScmBranch)null, datePattern, startVersion, endVersion);
   }

   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern) throws ScmException {
      return this.executeChangeLogCommand(repo, fileSet, startDate, endDate, branch, datePattern, (ScmVersion)null, (ScmVersion)null);
   }

   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern, ScmVersion startVersion, ScmVersion endVersion) throws ScmException {
      return this.executeChangeLogCommand(repo, fileSet, startDate, endDate, branch, datePattern, startVersion, endVersion, (Integer)null);
   }

   protected ChangeLogScmResult executeChangeLogCommand(ChangeLogScmRequest request) throws ScmException {
      ScmVersion startVersion = request.getStartRevision();
      ScmVersion endVersion = request.getEndRevision();
      ScmFileSet fileSet = request.getScmFileSet();
      String datePattern = request.getDatePattern();
      return this.executeChangeLogCommand(request.getScmRepository().getProviderRepository(), fileSet, request.getStartDate(), request.getEndDate(), request.getScmBranch(), datePattern, startVersion, endVersion, request.getLimit());
   }

   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern, ScmVersion startVersion, ScmVersion endVersion, Integer limit) throws ScmException {
      Commandline cl = createCommandLine((GitScmProviderRepository)repo, fileSet.getBasedir(), branch, startDate, endDate, startVersion, endVersion, limit);
      GitChangeLogConsumer consumer = new GitChangeLogConsumer(this.getLogger(), datePattern);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      int exitCode = GitCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      if (exitCode != 0) {
         return new ChangeLogScmResult(cl.toString(), "The git-log command failed.", stderr.getOutput(), false);
      } else {
         ChangeLogSet changeLogSet = new ChangeLogSet(consumer.getModifications(), startDate, endDate);
         changeLogSet.setStartVersion(startVersion);
         changeLogSet.setEndVersion(endVersion);
         return new ChangeLogScmResult(cl.toString(), changeLogSet);
      }
   }

   public static Commandline createCommandLine(GitScmProviderRepository repository, File workingDirectory, ScmBranch branch, Date startDate, Date endDate, ScmVersion startVersion, ScmVersion endVersion) {
      return createCommandLine(repository, workingDirectory, branch, startDate, endDate, startVersion, endVersion, (Integer)null);
   }

   static Commandline createCommandLine(GitScmProviderRepository repository, File workingDirectory, ScmBranch branch, Date startDate, Date endDate, ScmVersion startVersion, ScmVersion endVersion, Integer limit) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "whatchanged");
      if (startDate != null || endDate != null) {
         if (startDate != null) {
            cl.createArg().setValue("--since=" + StringUtils.escape(dateFormat.format(startDate)));
         }

         if (endDate != null) {
            cl.createArg().setValue("--until=" + StringUtils.escape(dateFormat.format(endDate)));
         }
      }

      cl.createArg().setValue("--date=iso");
      if (startVersion != null || endVersion != null) {
         StringBuilder versionRange = new StringBuilder();
         if (startVersion != null) {
            versionRange.append(StringUtils.escape(startVersion.getName()));
         }

         versionRange.append("..");
         if (endVersion != null) {
            versionRange.append(StringUtils.escape(endVersion.getName()));
         }

         cl.createArg().setValue(versionRange.toString());
      }

      if (limit != null && limit > 0) {
         cl.createArg().setValue("--max-count=" + limit);
      }

      if (branch != null && branch.getName() != null && branch.getName().length() > 0) {
         cl.createArg().setValue(branch.getName());
      }

      cl.createArg().setValue("--");
      cl.createArg().setFile(workingDirectory);
      return cl;
   }
}

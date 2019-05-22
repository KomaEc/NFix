package org.apache.maven.scm.provider.svn.svnexe.command.changelog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmTag;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.AbstractChangeLogCommand;
import org.apache.maven.scm.command.changelog.ChangeLogScmRequest;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.SvnTagBranchUtils;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnChangeLogCommand extends AbstractChangeLogCommand implements SvnCommand {
   private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z";

   /** @deprecated */
   @Deprecated
   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion, String datePattern) throws ScmException {
      return this.executeChangeLogCommand(repo, fileSet, (Date)null, (Date)null, (ScmBranch)null, datePattern, startVersion, endVersion, (Integer)null);
   }

   /** @deprecated */
   @Deprecated
   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern) throws ScmException {
      return this.executeChangeLogCommand(repo, fileSet, startDate, endDate, branch, datePattern, (ScmVersion)null, (ScmVersion)null, (Integer)null);
   }

   protected ChangeLogScmResult executeChangeLogCommand(ChangeLogScmRequest request) throws ScmException {
      ScmVersion startVersion = request.getStartRevision();
      ScmVersion endVersion = request.getEndRevision();
      ScmFileSet fileSet = request.getScmFileSet();
      String datePattern = request.getDatePattern();
      return this.executeChangeLogCommand(request.getScmRepository().getProviderRepository(), fileSet, request.getStartDate(), request.getEndDate(), request.getScmBranch(), datePattern, startVersion, endVersion, request.getLimit());
   }

   private ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern, ScmVersion startVersion, ScmVersion endVersion, Integer limit) throws ScmException {
      Commandline cl = createCommandLine((SvnScmProviderRepository)repo, fileSet.getBasedir(), branch, startDate, endDate, startVersion, endVersion, limit);
      SvnChangeLogConsumer consumer = new SvnChangeLogConsumer(this.getLogger(), datePattern);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      int exitCode;
      try {
         exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
      } catch (CommandLineException var15) {
         throw new ScmException("Error while executing svn command.", var15);
      }

      if (exitCode != 0) {
         return new ChangeLogScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false);
      } else {
         ChangeLogSet changeLogSet = new ChangeLogSet(consumer.getModifications(), startDate, endDate);
         changeLogSet.setStartVersion(startVersion);
         changeLogSet.setEndVersion(endVersion);
         return new ChangeLogScmResult(cl.toString(), changeLogSet);
      }
   }

   public static Commandline createCommandLine(SvnScmProviderRepository repository, File workingDirectory, ScmBranch branch, Date startDate, Date endDate, ScmVersion startVersion, ScmVersion endVersion) {
      return createCommandLine(repository, workingDirectory, branch, startDate, endDate, startVersion, endVersion, (Integer)null);
   }

   public static Commandline createCommandLine(SvnScmProviderRepository repository, File workingDirectory, ScmBranch branch, Date startDate, Date endDate, ScmVersion startVersion, ScmVersion endVersion, Integer limit) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(workingDirectory, repository);
      cl.createArg().setValue("log");
      cl.createArg().setValue("-v");
      if (limit != null && limit > 0) {
         cl.createArg().setValue("--limit");
         cl.createArg().setValue(Integer.toString(limit));
      }

      if (startDate != null) {
         cl.createArg().setValue("-r");
         if (endDate != null) {
            cl.createArg().setValue("{" + dateFormat.format(startDate) + "}" + ":" + "{" + dateFormat.format(endDate) + "}");
         } else {
            cl.createArg().setValue("{" + dateFormat.format(startDate) + "}:HEAD");
         }
      }

      if (startVersion != null) {
         cl.createArg().setValue("-r");
         if (endVersion != null) {
            if (startVersion.getName().equals(endVersion.getName())) {
               cl.createArg().setValue(startVersion.getName());
            } else {
               cl.createArg().setValue(startVersion.getName() + ":" + endVersion.getName());
            }
         } else {
            cl.createArg().setValue(startVersion.getName() + ":HEAD");
         }
      }

      if (branch != null && StringUtils.isNotEmpty(branch.getName())) {
         if (branch instanceof ScmTag) {
            cl.createArg().setValue(SvnTagBranchUtils.resolveTagUrl(repository, (ScmTag)branch));
         } else {
            cl.createArg().setValue(SvnTagBranchUtils.resolveBranchUrl(repository, branch));
         }
      }

      if (endVersion == null || !StringUtils.equals("BASE", endVersion.getName())) {
         cl.createArg().setValue(repository.getUrl());
      }

      return cl;
   }
}

package org.apache.maven.scm.provider.clearcase.command.changelog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.changelog.AbstractChangeLogCommand;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.command.ClearCaseCommand;
import org.apache.maven.scm.provider.clearcase.util.ClearCaseUtil;
import org.apache.maven.scm.providers.clearcase.settings.Settings;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class ClearCaseChangeLogCommand extends AbstractChangeLogCommand implements ClearCaseCommand {
   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repository, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing changelog command...");
      }

      Commandline cl = createCommandLine(fileSet.getBasedir(), branch, startDate);
      ClearCaseChangeLogConsumer consumer = new ClearCaseChangeLogConsumer(this.getLogger(), datePattern);
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
         }

         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var12) {
         throw new ScmException("Error while executing cvs command.", var12);
      }

      return exitCode != 0 ? new ChangeLogScmResult(cl.toString(), "The cleartool command failed.", stderr.getOutput(), false) : new ChangeLogScmResult(cl.toString(), new ChangeLogSet(consumer.getModifications(), startDate, endDate));
   }

   public static Commandline createCommandLine(File workingDirectory, ScmBranch branch, Date startDate) {
      Commandline command = new Commandline();
      command.setExecutable("cleartool");
      command.createArg().setValue("lshistory");
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      Settings settings = ClearCaseUtil.getSettings();
      String userFormat = StringUtils.isEmpty(settings.getChangelogUserFormat()) ? "" : settings.getChangelogUserFormat();
      StringBuilder format = new StringBuilder();
      format.append("NAME:%En\\n");
      format.append("DATE:%Nd\\n");
      format.append("COMM:%-12.12o - %o - %c - Activity: %[activity]p\\n");
      format.append("USER:%" + userFormat + "u\\n");
      format.append("REVI:%Ln\\n");
      command.createArg().setValue("-fmt");
      command.createArg().setValue(format.toString());
      command.createArg().setValue("-recurse");
      command.createArg().setValue("-nco");
      if (startDate != null) {
         SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
         String start = sdf.format(startDate);
         command.createArg().setValue("-since");
         command.createArg().setValue(start);
      }

      if (branch != null && StringUtils.isNotEmpty(branch.getName())) {
         command.createArg().setValue("-branch");
         command.createArg().setValue(branch.getName());
      }

      return command;
   }
}

package org.apache.maven.scm.provider.vss.commands.changelog;

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
import org.apache.maven.scm.provider.vss.commands.VssCommandLineUtils;
import org.apache.maven.scm.provider.vss.repository.VssScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class VssHistoryCommand extends AbstractChangeLogCommand {
   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repository, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern) throws ScmException {
      VssScmProviderRepository repo = (VssScmProviderRepository)repository;
      Commandline cl = this.buildCmdLine(repo, fileSet, startDate, endDate);
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      VssChangeLogConsumer consumer = new VssChangeLogConsumer(repo, datePattern, this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      int exitCode = VssCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
      return exitCode != 0 ? new ChangeLogScmResult(cl.toString(), "The vss command failed.", stderr.getOutput(), false) : new ChangeLogScmResult(cl.toString(), new ChangeLogSet(consumer.getModifications(), startDate, endDate));
   }

   public Commandline buildCmdLine(VssScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate) throws ScmException {
      Commandline command = new Commandline();
      command.setWorkingDirectory(fileSet.getBasedir().getAbsolutePath());

      try {
         command.addSystemEnvironment();
      } catch (Exception var9) {
         throw new ScmException("Can't add system environment.", var9);
      }

      command.addEnvironment("SSDIR", repo.getVssdir());
      String ssDir = VssCommandLineUtils.getSsDir();
      command.setExecutable(ssDir + "ss");
      command.createArg().setValue("History");
      command.createArg().setValue("$" + repo.getProject());
      if (repo.getUserPassword() != null) {
         command.createArg().setValue("-Y" + repo.getUserPassword());
      }

      command.createArg().setValue("-R");
      command.createArg().setValue("-I-");
      if (startDate != null) {
         if (endDate == null) {
            endDate = new Date();
         }

         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
         String dateRange = sdf.format(endDate) + "~" + sdf.format(startDate);
         command.createArg().setValue("-Vd" + dateRange);
      }

      return command;
   }
}

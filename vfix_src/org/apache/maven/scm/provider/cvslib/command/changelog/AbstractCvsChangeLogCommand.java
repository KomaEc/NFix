package org.apache.maven.scm.provider.cvslib.command.changelog;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.AbstractChangeLogCommand;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommand;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.util.CvsUtil;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractCvsChangeLogCommand extends AbstractChangeLogCommand implements CvsCommand {
   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion, String datePattern) throws ScmException {
      return this.executeChangeLogCommand(repo, fileSet, (Date)null, (Date)null, (ScmBranch)null, startVersion, endVersion, datePattern);
   }

   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern) throws ScmException {
      return this.executeChangeLogCommand(repo, fileSet, startDate, endDate, branch, (ScmVersion)null, (ScmVersion)null, datePattern);
   }

   private ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, ScmVersion startVersion, ScmVersion endVersion, String datePattern) throws ScmException {
      CvsScmProviderRepository repository = (CvsScmProviderRepository)repo;
      Commandline cl = CvsCommandUtils.getBaseCommand("log", repository, fileSet);
      if (startDate != null) {
         SimpleDateFormat outputDate = new SimpleDateFormat(this.getDateFormat());
         String dateRange;
         if (endDate == null) {
            dateRange = ">" + outputDate.format(startDate);
         } else {
            dateRange = outputDate.format(startDate) + "<" + outputDate.format(endDate);
         }

         cl.createArg().setValue("-d");
         this.addDateRangeParameter(cl, dateRange);
      }

      if (branch != null && StringUtils.isNotEmpty(branch.getName())) {
         cl.createArg().setValue("-r" + branch.getName());
      }

      if (startVersion != null || endVersion != null) {
         StringBuilder sb = new StringBuilder();
         sb.append("-r");
         if (startVersion != null && StringUtils.isNotEmpty(startVersion.getName())) {
            sb.append(startVersion.getName());
         }

         sb.append("::");
         if (endVersion != null && StringUtils.isNotEmpty(endVersion.getName())) {
            sb.append(endVersion.getName());
         }

         cl.createArg().setValue(sb.toString());
      }

      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      return this.executeCvsCommand(cl, startDate, endDate, startVersion, endVersion, datePattern);
   }

   protected abstract ChangeLogScmResult executeCvsCommand(Commandline var1, Date var2, Date var3, ScmVersion var4, ScmVersion var5, String var6) throws ScmException;

   protected String getDateFormat() {
      return CvsUtil.getSettings().getChangeLogCommandDateFormat();
   }

   protected void addDateRangeParameter(Commandline cl, String dateRange) {
      if (Os.isFamily("windows")) {
         cl.createArg().setValue("\"" + dateRange + "\"");
      } else {
         cl.createArg().setValue(dateRange);
      }

   }
}

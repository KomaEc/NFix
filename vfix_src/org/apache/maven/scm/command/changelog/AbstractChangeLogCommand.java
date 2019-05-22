package org.apache.maven.scm.command.changelog;

import java.util.Date;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractChangeLogCommand extends AbstractCommand implements ChangeLogCommand {
   /** @deprecated */
   @Deprecated
   protected abstract ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository var1, ScmFileSet var2, Date var3, Date var4, ScmBranch var5, String var6) throws ScmException;

   /** @deprecated */
   @Deprecated
   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repository, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion, String datePattern) throws ScmException {
      throw new ScmException("Unsupported method for this provider.");
   }

   public ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      Date startDate = parameters.getDate(CommandParameter.START_DATE, (Date)null);
      Date endDate = parameters.getDate(CommandParameter.END_DATE, (Date)null);
      int numDays = parameters.getInt(CommandParameter.NUM_DAYS, 0);
      Integer limit = parameters.getInt(CommandParameter.LIMIT, -1);
      if (limit < 1) {
         limit = null;
      }

      ScmBranch branch = (ScmBranch)parameters.getScmVersion(CommandParameter.BRANCH, (ScmVersion)null);
      ScmVersion startVersion = parameters.getScmVersion(CommandParameter.START_SCM_VERSION, (ScmVersion)null);
      ScmVersion endVersion = parameters.getScmVersion(CommandParameter.END_SCM_VERSION, (ScmVersion)null);
      String datePattern = parameters.getString(CommandParameter.CHANGELOG_DATE_PATTERN, (String)null);
      if (startVersion == null && endVersion == null) {
         if (numDays == 0 || startDate == null && endDate == null) {
            if (endDate != null && startDate == null) {
               throw new ScmException("The end date is set but the start date isn't.");
            } else {
               if (numDays > 0) {
                  int day = 86400000;
                  startDate = new Date(System.currentTimeMillis() - (long)numDays * (long)day);
                  endDate = new Date(System.currentTimeMillis() + (long)day);
               } else if (endDate == null) {
                  endDate = new Date();
               }

               return this.executeChangeLogCommand(repository, fileSet, startDate, endDate, branch, datePattern);
            }
         } else {
            throw new ScmException("Start or end date cannot be set if num days is set.");
         }
      } else {
         return this.executeChangeLogCommand(repository, fileSet, startVersion, endVersion, datePattern);
      }
   }

   protected ChangeLogScmResult executeChangeLogCommand(ChangeLogScmRequest request) throws ScmException {
      throw new ScmException("Unsupported method for this provider.");
   }
}

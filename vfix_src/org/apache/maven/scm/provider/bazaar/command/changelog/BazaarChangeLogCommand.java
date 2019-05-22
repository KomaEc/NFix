package org.apache.maven.scm.provider.bazaar.command.changelog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.changelog.AbstractChangeLogCommand;
import org.apache.maven.scm.command.changelog.ChangeLogScmRequest;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.bazaar.BazaarUtils;

public class BazaarChangeLogCommand extends AbstractChangeLogCommand implements Command {
   protected ChangeLogScmResult executeChangeLogCommand(ChangeLogScmRequest request) throws ScmException {
      ScmVersion startVersion = request.getStartRevision();
      ScmVersion endVersion = request.getEndRevision();
      ScmFileSet fileSet = request.getScmFileSet();
      String datePattern = request.getDatePattern();
      if (startVersion == null && endVersion == null) {
         return this.executeChangeLogCommand(fileSet, request.getStartDate(), request.getEndDate(), datePattern, request.getLimit());
      } else {
         ScmProviderRepository scmProviderRepository = request.getScmRepository().getProviderRepository();
         return this.executeChangeLogCommand(scmProviderRepository, fileSet, startVersion, endVersion, datePattern);
      }
   }

   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern) throws ScmException {
      return this.executeChangeLogCommand(fileSet, startDate, endDate, datePattern, (Integer)null);
   }

   private ChangeLogScmResult executeChangeLogCommand(ScmFileSet fileSet, Date startDate, Date endDate, String datePattern, Integer limit) throws ScmException {
      List<String> cmd = new ArrayList();
      cmd.addAll(Arrays.asList("log", "--verbose"));
      if (limit != null && limit > 0) {
         cmd.add("--limit");
         cmd.add(Integer.toString(limit));
      }

      BazaarChangeLogConsumer consumer = new BazaarChangeLogConsumer(this.getLogger(), datePattern);
      ScmResult result = BazaarUtils.execute(consumer, this.getLogger(), fileSet.getBasedir(), (String[])cmd.toArray(new String[cmd.size()]));
      List<ChangeSet> logEntries = consumer.getModifications();
      List<ChangeSet> inRangeAndValid = new ArrayList();
      startDate = startDate == null ? new Date(0L) : startDate;
      endDate = endDate == null ? new Date() : endDate;
      Iterator i$ = logEntries.iterator();

      while(i$.hasNext()) {
         ChangeSet change = (ChangeSet)i$.next();
         if (change.getFiles().size() > 0 && !change.getDate().before(startDate) && !change.getDate().after(endDate)) {
            inRangeAndValid.add(change);
         }
      }

      ChangeLogSet changeLogSet = new ChangeLogSet(inRangeAndValid, startDate, endDate);
      return new ChangeLogScmResult(changeLogSet, result);
   }
}

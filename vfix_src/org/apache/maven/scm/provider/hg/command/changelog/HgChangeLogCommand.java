package org.apache.maven.scm.provider.hg.command.changelog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.apache.maven.scm.provider.hg.HgUtils;

public class HgChangeLogCommand extends AbstractChangeLogCommand implements Command {
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

   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository scmProviderRepository, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern) throws ScmException {
      return this.executeChangeLogCommand((ScmFileSet)fileSet, (Date)startDate, (Date)endDate, (String)datePattern, (Integer)null);
   }

   private ChangeLogScmResult executeChangeLogCommand(ScmFileSet fileSet, Date startDate, Date endDate, String datePattern, Integer limit) throws ScmException {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      StringBuilder dateInterval = new StringBuilder();
      dateInterval.append(dateFormat.format(startDate == null ? new Date(86400000L) : startDate));
      dateInterval.append(" to ");
      dateInterval.append(dateFormat.format(endDate == null ? new Date() : endDate));
      List<String> cmd = new ArrayList();
      cmd.addAll(Arrays.asList("log", "--template", "changeset:   {rev}:{node|short}\nbranch:      {branch}\nuser:        {author}\ndate:        {date|isodatesec}\ntag:         {tags}\nfiles:       {files}\ndescription:\n{desc}\n", "--no-merges", "--date", dateInterval.toString()));
      if (limit != null && limit > 0) {
         cmd.add("--limit");
         cmd.add(Integer.toString(limit));
      }

      HgChangeLogConsumer consumer = new HgChangeLogConsumer(this.getLogger(), datePattern);
      ScmResult result = HgUtils.execute(consumer, this.getLogger(), fileSet.getBasedir(), (String[])cmd.toArray(new String[cmd.size()]));
      List<ChangeSet> logEntries = consumer.getModifications();
      ChangeLogSet changeLogSet = new ChangeLogSet(logEntries, startDate, endDate);
      return new ChangeLogScmResult(changeLogSet, result);
   }

   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repository, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion, String datePattern) throws ScmException {
      StringBuilder revisionInterval = new StringBuilder();
      if (startVersion != null) {
         revisionInterval.append(startVersion.getName());
      }

      revisionInterval.append(":");
      if (endVersion != null) {
         revisionInterval.append(endVersion.getName());
      }

      String[] cmd = new String[]{"log", "--template", "changeset:   {rev}:{node|short}\nbranch:      {branch}\nuser:        {author}\ndate:        {date|isodatesec}\ntag:         {tags}\nfiles:       {files}\ndescription:\n{desc}\n", "--no-merges", "-r", revisionInterval.toString()};
      HgChangeLogConsumer consumer = new HgChangeLogConsumer(this.getLogger(), datePattern);
      ScmResult result = HgUtils.execute(consumer, this.getLogger(), fileSet.getBasedir(), cmd);
      List<ChangeSet> logEntries = consumer.getModifications();
      Date startDate = null;
      Date endDate = null;
      if (!logEntries.isEmpty()) {
         startDate = ((ChangeSet)logEntries.get(0)).getDate();
         endDate = ((ChangeSet)logEntries.get(logEntries.size() - 1)).getDate();
      }

      ChangeLogSet changeLogSet = new ChangeLogSet(logEntries, startDate, endDate);
      changeLogSet.setStartVersion(startVersion);
      changeLogSet.setEndVersion(endVersion);
      return new ChangeLogScmResult(changeLogSet, result);
   }
}

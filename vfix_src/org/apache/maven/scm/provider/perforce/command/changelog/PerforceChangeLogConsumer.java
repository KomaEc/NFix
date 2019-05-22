package org.apache.maven.scm.provider.perforce.command.changelog;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class PerforceChangeLogConsumer extends AbstractConsumer {
   private static final String PERFORCE_TIMESTAMP_PATTERN = "yyyy/MM/dd HH:mm:ss";
   private List<ChangeSet> entries = new ArrayList();
   private static final int GET_REVISION = 1;
   private static final int GET_COMMENT_BEGIN = 2;
   private static final int GET_COMMENT = 3;
   private static final String COMMENT_DELIMITER = "";
   private static final String FILE_BEGIN_TOKEN = "//";
   private int status = 1;
   private ChangeSet currentChange;
   private String currentFile;
   private String repoPath;
   private Date startDate;
   private Date endDate;
   private String userDatePattern;
   private static final Pattern PATTERN = Pattern.compile("^\\.\\.\\. #(\\d+) change (\\d+) .* on (.*) by (.*)@");

   public PerforceChangeLogConsumer(String path, Date startDate, Date endDate, String userDatePattern, ScmLogger logger) {
      super(logger);
      this.startDate = startDate;
      this.endDate = endDate;
      this.userDatePattern = userDatePattern;
      this.repoPath = path;
   }

   public List<ChangeSet> getModifications() throws ScmException {
      Map<Date, ChangeSet> groupedEntries = new LinkedHashMap();

      for(int i = 0; i < this.entries.size(); ++i) {
         ChangeSet cs = (ChangeSet)this.entries.get(i);
         ChangeSet hit = (ChangeSet)groupedEntries.get(cs.getDate());
         if (hit != null) {
            if (cs.getFiles().size() != 1) {
               throw new ScmException("Merge of entries failed. Bad entry size: " + cs.getFiles().size());
            }

            hit.addFile((ChangeFile)cs.getFiles().get(0));
         } else {
            groupedEntries.put(cs.getDate(), cs);
         }
      }

      List<ChangeSet> result = new ArrayList();
      result.addAll(groupedEntries.values());
      return result;
   }

   public void consumeLine(String line) {
      switch(this.status) {
      case 1:
         this.processGetRevision(line);
         break;
      case 2:
         this.status = 3;
         break;
      case 3:
         this.processGetComment(line);
         break;
      default:
         throw new IllegalStateException("Unknown state: " + this.status);
      }

   }

   private void addEntry(ChangeSet entry, ChangeFile file) {
      if (this.startDate == null || !entry.getDate().before(this.startDate)) {
         if (this.endDate == null || !entry.getDate().after(this.endDate)) {
            entry.addFile(file);
            this.entries.add(entry);
         }
      }
   }

   private void processGetRevision(String line) {
      if (line.startsWith("//")) {
         this.currentFile = line.substring(this.repoPath.length() + 1);
      } else {
         Matcher matcher = PATTERN.matcher(line);
         if (matcher.find()) {
            this.currentChange = new ChangeSet();
            this.currentChange.setRevision(matcher.group(1));
            this.currentChange.setDate(this.parseDate(matcher.group(3), this.userDatePattern, "yyyy/MM/dd HH:mm:ss"));
            this.currentChange.setAuthor(matcher.group(4));
            this.status = 2;
         }
      }
   }

   private void processGetComment(String line) {
      if (line.equals("")) {
         this.addEntry(this.currentChange, new ChangeFile(this.currentFile, this.currentChange.getRevision()));
         this.status = 1;
      } else {
         this.currentChange.setComment(this.currentChange.getComment() + line + "\n");
      }

   }
}

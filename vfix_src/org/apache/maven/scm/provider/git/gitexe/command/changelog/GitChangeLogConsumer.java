package org.apache.maven.scm.provider.git.gitexe.command.changelog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class GitChangeLogConsumer extends AbstractConsumer {
   private static final String GIT_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss Z";
   private static final int STATUS_GET_HEADER = 1;
   private static final int STATUS_GET_AUTHOR = 2;
   private static final int STATUS_RAW_TREE = 21;
   private static final int STATUS_RAW_PARENT = 22;
   private static final int STATUS_RAW_AUTHOR = 23;
   private static final int STATUS_RAW_COMMITTER = 24;
   private static final int STATUS_GET_DATE = 3;
   private static final int STATUS_GET_FILE = 4;
   private static final int STATUS_GET_COMMENT = 5;
   private static final Pattern HEADER_PATTERN = Pattern.compile("^commit (.*)");
   private static final Pattern AUTHOR_PATTERN = Pattern.compile("^Author: (.*)");
   private static final Pattern RAW_TREE_PATTERN = Pattern.compile("^tree ([A-Fa-f0-9]+)");
   private static final Pattern RAW_PARENT_PATTERN = Pattern.compile("^parent ([A-Fa-f0-9]+)");
   private static final Pattern RAW_AUTHOR_PATTERN = Pattern.compile("^author (.+ <.+>) ([0-9]+) (.*)");
   private static final Pattern RAW_COMMITTER_PATTERN = Pattern.compile("^committer (.+ <.+>) ([0-9]+) (.*)");
   private static final Pattern DATE_PATTERN = Pattern.compile("^Date:\\s*(.*)");
   private static final Pattern FILE_PATTERN = Pattern.compile("^:\\d* \\d* [A-Fa-f0-9]*\\.* [A-Fa-f0-9]*\\.* ([A-Z])[0-9]*\\t([^\\t]*)(\\t(.*))?");
   private int status = 1;
   private List<ChangeSet> entries = new ArrayList();
   private ChangeSet currentChange;
   private String currentRevision;
   private StringBuilder currentComment;
   private String userDateFormat;

   public GitChangeLogConsumer(ScmLogger logger, String userDateFormat) {
      super(logger);
      this.userDateFormat = userDateFormat;
   }

   public List<ChangeSet> getModifications() {
      this.processGetFile("");
      return this.entries;
   }

   public void consumeLine(String line) {
      switch(this.status) {
      case 1:
         this.processGetHeader(line);
         break;
      case 2:
         this.processGetAuthor(line);
         break;
      case 3:
         this.processGetDate(line, (Locale)null);
         break;
      case 4:
         this.processGetFile(line);
         break;
      case 5:
         this.processGetComment(line);
         break;
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      default:
         throw new IllegalStateException("Unknown state: " + this.status);
      case 21:
         this.processGetRawTree(line);
         break;
      case 22:
         this.processGetRawParent(line);
         break;
      case 23:
         this.processGetRawAuthor(line);
         break;
      case 24:
         this.processGetRawCommitter(line);
      }

   }

   private void processGetHeader(String line) {
      Matcher matcher = HEADER_PATTERN.matcher(line);
      if (matcher.matches()) {
         this.currentRevision = matcher.group(1);
         this.currentChange = new ChangeSet();
         this.currentChange.setRevision(this.currentRevision);
         this.status = 2;
      }
   }

   private void processGetAuthor(String line) {
      if (RAW_TREE_PATTERN.matcher(line).matches()) {
         this.status = 21;
         this.processGetRawTree(line);
      } else {
         Matcher matcher = AUTHOR_PATTERN.matcher(line);
         if (matcher.matches()) {
            String author = matcher.group(1);
            this.currentChange.setAuthor(author);
            this.status = 3;
         }
      }
   }

   private void processGetRawTree(String line) {
      if (RAW_TREE_PATTERN.matcher(line).matches()) {
         this.status = 22;
      }
   }

   private void processGetRawParent(String line) {
      Matcher matcher = RAW_PARENT_PATTERN.matcher(line);
      if (!matcher.matches()) {
         this.status = 23;
         this.processGetRawAuthor(line);
      } else {
         String parentHash = matcher.group(1);
         this.addParentRevision(parentHash);
      }
   }

   private void addParentRevision(String hash) {
      if (this.currentChange.getParentRevision() == null) {
         this.currentChange.setParentRevision(hash);
      } else {
         this.currentChange.addMergedRevision(hash);
      }

   }

   private void processGetRawAuthor(String line) {
      Matcher matcher = RAW_AUTHOR_PATTERN.matcher(line);
      if (matcher.matches()) {
         String author = matcher.group(1);
         this.currentChange.setAuthor(author);
         String datestring = matcher.group(2);
         String tz = matcher.group(3);
         Calendar c = Calendar.getInstance(TimeZone.getTimeZone(tz));
         c.setTimeInMillis(Long.parseLong(datestring) * 1000L);
         this.currentChange.setDate(c.getTime());
         this.status = 24;
      }
   }

   private void processGetRawCommitter(String line) {
      if (RAW_COMMITTER_PATTERN.matcher(line).matches()) {
         this.status = 5;
      }
   }

   private void processGetDate(String line, Locale locale) {
      Matcher matcher = DATE_PATTERN.matcher(line);
      if (matcher.matches()) {
         String datestring = matcher.group(1);
         Date date = this.parseDate(datestring.trim(), this.userDateFormat, "yyyy-MM-dd HH:mm:ss Z", locale);
         this.currentChange.setDate(date);
         this.status = 5;
      }
   }

   private void processGetComment(String line) {
      if (line.length() < 4) {
         if (this.currentComment == null) {
            this.currentComment = new StringBuilder();
         } else {
            this.currentChange.setComment(this.currentComment.toString());
            this.status = 4;
         }
      } else {
         if (this.currentComment.length() > 0) {
            this.currentComment.append('\n');
         }

         this.currentComment.append(line.substring(4));
      }

   }

   private void processGetFile(String line) {
      if (line.length() == 0) {
         if (this.currentChange != null) {
            this.entries.add(this.currentChange);
         }

         this.resetChangeLog();
         this.status = 1;
      } else {
         Matcher matcher = FILE_PATTERN.matcher(line);
         if (!matcher.matches()) {
            return;
         }

         String actionChar = matcher.group(1);
         String name = matcher.group(2);
         String originalName = null;
         String originalRevision = null;
         ScmFileStatus action;
         if ("A".equals(actionChar)) {
            action = ScmFileStatus.ADDED;
         } else if ("M".equals(actionChar)) {
            action = ScmFileStatus.MODIFIED;
         } else if ("D".equals(actionChar)) {
            action = ScmFileStatus.DELETED;
         } else if ("R".equals(actionChar)) {
            action = ScmFileStatus.RENAMED;
            originalName = name;
            name = matcher.group(4);
            originalRevision = this.currentChange.getParentRevision();
         } else if ("C".equals(actionChar)) {
            action = ScmFileStatus.COPIED;
            originalName = name;
            name = matcher.group(4);
            originalRevision = this.currentChange.getParentRevision();
         } else {
            action = ScmFileStatus.UNKNOWN;
         }

         ChangeFile changeFile = new ChangeFile(name, this.currentRevision);
         changeFile.setAction(action);
         changeFile.setOriginalName(originalName);
         changeFile.setOriginalRevision(originalRevision);
         this.currentChange.addFile(changeFile);
      }

   }

   private void resetChangeLog() {
      this.currentComment = null;
      this.currentChange = null;
   }
}

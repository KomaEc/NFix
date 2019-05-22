package org.apache.maven.scm.provider.perforce.command.changelog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class PerforceDescribeConsumer extends AbstractConsumer {
   private List<ChangeSet> entries = new ArrayList();
   private static final int GET_REVISION = 1;
   private static final int GET_COMMENT_BEGIN = 2;
   private static final int GET_COMMENT = 3;
   private static final int GET_AFFECTED_FILES = 4;
   private static final int GET_FILES_BEGIN = 5;
   private static final int GET_FILE = 6;
   private int status = 1;
   private String currentRevision;
   private ChangeSet currentChange;
   private String currentFile;
   private String repoPath;
   private String userDatePattern;
   private static final Pattern REVISION_PATTERN = Pattern.compile("^Change (\\d+) by (.*)@[^ ]+ on (.*)");
   private static final String COMMENT_DELIMITER = "";
   private static final String CHANGELIST_DELIMITER = "";
   private static final Pattern FILE_PATTERN = Pattern.compile("^\\.\\.\\. (.*)#(\\d+) ");

   public PerforceDescribeConsumer(String repoPath, String userDatePattern, ScmLogger logger) {
      super(logger);
      this.repoPath = repoPath;
      this.userDatePattern = userDatePattern;
   }

   public List<ChangeSet> getModifications() throws ScmException {
      return this.entries;
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
      case 4:
         this.processGetAffectedFiles(line);
         break;
      case 5:
         this.status = 6;
         break;
      case 6:
         this.processGetFile(line);
         break;
      default:
         throw new IllegalStateException("Unknown state: " + this.status);
      }

   }

   private void addEntry(ChangeSet entry, ChangeFile file) {
      entry.addFile(file);
   }

   private void processGetFile(String line) {
      if (line.equals("")) {
         this.entries.add(0, this.currentChange);
         this.status = 1;
      } else {
         Matcher matcher = FILE_PATTERN.matcher(line);
         if (matcher.find()) {
            this.currentFile = matcher.group(1);
            if (this.currentFile.startsWith(this.repoPath)) {
               this.currentFile = this.currentFile.substring(this.repoPath.length() + 1);
               this.addEntry(this.currentChange, new ChangeFile(this.currentFile, matcher.group(2)));
            }

         }
      }
   }

   private void processGetRevision(String line) {
      Matcher matcher = REVISION_PATTERN.matcher(line);
      if (matcher.find()) {
         this.currentChange = new ChangeSet();
         this.currentRevision = matcher.group(1);
         this.currentChange.setAuthor(matcher.group(2));
         this.currentChange.setDate(matcher.group(3), this.userDatePattern);
         this.status = 2;
      }
   }

   private void processGetComment(String line) {
      if (line.equals("")) {
         this.status = 4;
      } else {
         this.currentChange.setComment(this.currentChange.getComment() + line.substring(1) + "\n");
      }

   }

   private void processGetAffectedFiles(String line) {
      if (line.equals("Affected files ...")) {
         this.status = 5;
      }
   }
}

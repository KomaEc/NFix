package org.apache.maven.scm.provider.clearcase.command.changelog;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class ClearCaseChangeLogConsumer extends AbstractConsumer {
   private static final String CLEARCASE_TIMESTAMP_PATTERN = "yyyyMMdd.HHmmss";
   private static final String NAME_TAG = "NAME:";
   private static final String USER_TAG = "USER:";
   private static final String DATE_TAG = "DATE:";
   private static final String COMMENT_TAG = "COMM:";
   private static final String REVISION_TAG = "REVI:";
   private List<ChangeSet> entries = new ArrayList();
   private static final int GET_FILE = 1;
   private static final int GET_DATE = 2;
   private static final int GET_COMMENT = 3;
   private static final int GET_REVISION = 4;
   private int status = 1;
   private ChangeSet currentChange = null;
   private ChangeFile currentFile = null;
   private String userDatePattern;

   public ClearCaseChangeLogConsumer(ScmLogger logger, String userDatePattern) {
      super(logger);
      this.userDatePattern = userDatePattern;
   }

   public List<ChangeSet> getModifications() {
      return this.entries;
   }

   public void consumeLine(String line) {
      switch(this.getStatus()) {
      case 1:
         this.processGetFile(line);
         break;
      case 2:
         this.processGetDate(line);
         break;
      case 3:
         this.processGetCommentAndUser(line);
         break;
      case 4:
         this.processGetRevision(line);
         break;
      default:
         if (this.getLogger().isWarnEnabled()) {
            this.getLogger().warn("Unknown state: " + this.status);
         }
      }

   }

   private void processGetFile(String line) {
      if (line.startsWith("NAME:")) {
         this.setCurrentChange(new ChangeSet());
         this.setCurrentFile(new ChangeFile(line.substring("NAME:".length(), line.length())));
         this.setStatus(2);
      }

   }

   private void processGetDate(String line) {
      if (line.startsWith("DATE:")) {
         this.getCurrentChange().setDate(this.parseDate(line.substring("DATE:".length()), this.userDatePattern, "yyyyMMdd.HHmmss"));
         this.setStatus(3);
      }

   }

   private void processGetCommentAndUser(String line) {
      if (line.startsWith("COMM:")) {
         String comm = line.substring("COMM:".length());
         this.getCurrentChange().setComment(this.getCurrentChange().getComment() + comm + "\n");
      } else if (line.startsWith("USER:")) {
         this.getCurrentChange().setAuthor(line.substring("USER:".length()));
         this.getCurrentChange().addFile(this.getCurrentFile());
         this.entries.add(this.getCurrentChange());
         this.setStatus(4);
      } else {
         this.getCurrentChange().setComment(this.getCurrentChange().getComment() + line + "\n");
      }

   }

   private void processGetRevision(String line) {
      if (line.startsWith("REVI:")) {
         this.getCurrentChange().setRevision(line.substring("REVI:".length()));
         this.setStatus(1);
      }

   }

   private ChangeFile getCurrentFile() {
      return this.currentFile;
   }

   private void setCurrentFile(ChangeFile currentFile) {
      this.currentFile = currentFile;
   }

   private ChangeSet getCurrentChange() {
      return this.currentChange;
   }

   private void setCurrentChange(ChangeSet currentChange) {
      this.currentChange = currentChange;
   }

   private int getStatus() {
      return this.status;
   }

   private void setStatus(int status) {
      this.status = status;
   }
}

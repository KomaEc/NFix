package org.apache.maven.scm.provider.hg.command.changelog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.hg.command.HgConsumer;

public class HgChangeLogConsumer extends HgConsumer {
   private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss Z";
   private static final String REVNO_TAG = "changeset:";
   private static final String TAG_TAG = "tag:";
   private static final String BRANCH_TAG = "branch:";
   private static final String AUTHOR_TAG = "user:";
   private static final String TIME_STAMP_TOKEN = "date:";
   private static final String MESSAGE_TOKEN = "description:";
   private static final String FILES_TOKEN = "files:";
   private List<ChangeSet> logEntries = new ArrayList();
   private ChangeSet currentChange;
   private String currentRevision;
   private String currentTag;
   private String currentBranch;
   private String userDatePattern;

   public HgChangeLogConsumer(ScmLogger logger, String userDatePattern) {
      super(logger);
      this.userDatePattern = userDatePattern;
   }

   public List<ChangeSet> getModifications() {
      return this.logEntries;
   }

   public void consumeLine(String line) {
      String trimmedLine = line.trim();
      this.doConsume((ScmFileStatus)null, trimmedLine);
   }

   public void doConsume(ScmFileStatus status, String line) {
      String tmpLine;
      if (line.startsWith("changeset:")) {
         this.currentChange = new ChangeSet();
         this.currentChange.setFiles(new ArrayList(0));
         this.logEntries.add(this.currentChange);
         tmpLine = line.substring("changeset:".length()).trim();
         this.currentRevision = tmpLine.substring(tmpLine.indexOf(58) + 1);
         this.currentChange.setRevision(this.currentRevision);
      } else if (line.startsWith("branch:")) {
         tmpLine = line.substring("branch:".length()).trim();
         this.currentBranch = tmpLine;
      } else if (line.startsWith("user:")) {
         tmpLine = line.substring("user:".length()).trim();
         this.currentChange.setAuthor(tmpLine);
      } else if (line.startsWith("date:")) {
         tmpLine = line.substring("date:".length()).trim();
         Date date = this.parseDate(tmpLine, this.userDatePattern, "yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH);
         this.currentChange.setDate(date);
      } else if (line.startsWith("tag:")) {
         tmpLine = line.substring("tag:".length()).trim();
         this.currentTag = tmpLine;
      } else if (line.startsWith("files:")) {
         tmpLine = line.substring("files:".length()).trim();
         String[] files = tmpLine.split(" ");

         for(int i = 0; i < files.length; ++i) {
            String file = files[i];
            ChangeFile changeFile = new ChangeFile(file, this.currentRevision);
            this.currentChange.addFile(changeFile);
         }
      } else if (line.startsWith("description:")) {
         this.currentChange.setComment("");
      } else {
         StringBuilder comment = new StringBuilder(this.currentChange.getComment());
         comment.append(line);
         comment.append('\n');
         this.currentChange.setComment(comment.toString());
      }

   }
}

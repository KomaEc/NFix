package org.apache.maven.scm.provider.starteam.command.changelog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.apache.maven.scm.util.AbstractConsumer;

public class StarteamChangeLogConsumer extends AbstractConsumer {
   private SimpleDateFormat localFormat = new SimpleDateFormat("", Locale.getDefault());
   private List<ChangeSet> entries = new ArrayList();
   private String workingDirectory;
   private String currentDir = "";
   private static final int GET_FILE = 1;
   private static final int GET_AUTHOR = 2;
   private static final int GET_COMMENT = 3;
   private static final int GET_REVISION = 4;
   private static final String DIR_MARKER = "(working dir: ";
   private static final String START_FILE = "History for: ";
   private static final String END_FILE = "=============================================================================";
   private static final String START_REVISION = "----------------------------";
   private static final String REVISION_TAG = "Branch Revision: ";
   private static final String AUTHOR_TAG = "Author: ";
   private static final String DATE_TAG = " Date: ";
   private int status = 1;
   private ChangeSet currentChange = null;
   private ChangeFile currentFile = null;
   private Date startDate;
   private Date endDate;
   private String userDateFormat;

   public StarteamChangeLogConsumer(File workingDirectory, ScmLogger logger, Date startDate, Date endDate, String userDateFormat) {
      super(logger);
      this.workingDirectory = workingDirectory.getPath().replace('\\', '/');
      this.startDate = startDate;
      this.endDate = endDate;
      this.userDateFormat = userDateFormat;
      if ("M/d/yy h:mm a".equals(this.localFormat.toLocalizedPattern())) {
         this.localFormat = new SimpleDateFormat("M/d/yy h:mm:ss a z");
      }

   }

   public List<ChangeSet> getModifications() {
      return this.entries;
   }

   public void consumeLine(String line) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(line);
      }

      int pos = false;
      int pos;
      if ((pos = line.indexOf("(working dir: ")) != -1) {
         this.processDirectory(line, pos);
      } else {
         switch(this.getStatus()) {
         case 1:
            this.processGetFile(line);
            break;
         case 2:
            this.processGetAuthor(line);
            break;
         case 3:
            this.processGetComment(line);
            break;
         case 4:
            this.processGetRevision(line);
            break;
         default:
            throw new IllegalStateException("Unknown state: " + this.status);
         }

      }
   }

   private void addEntry(ChangeSet entry, ChangeFile file) {
      if (entry.getAuthor() != null) {
         if (this.startDate == null || !entry.getDate().before(this.startDate)) {
            if (this.endDate == null || !entry.getDate().after(this.endDate)) {
               entry.addFile(file);
               this.entries.add(entry);
            }
         }
      }
   }

   private void processDirectory(String line, int pos) {
      String dirPath = line.substring(pos + "(working dir: ".length(), line.length() - 1).replace('\\', '/');

      try {
         this.currentDir = StarteamCommandLineUtils.getRelativeChildDirectory(this.workingDirectory, dirPath);
      } catch (IllegalStateException var6) {
         String error = "Working and checkout directories are not on the same tree";
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error(error);
            this.getLogger().error("Working directory: " + this.workingDirectory);
            this.getLogger().error("Checked out directory: " + dirPath);
         }

         throw new IllegalStateException(error);
      }
   }

   private void processGetFile(String line) {
      if (line.startsWith("History for: ")) {
         this.setCurrentChange(new ChangeSet());
         this.setCurrentFile(new ChangeFile(this.currentDir + "/" + line.substring("History for: ".length(), line.length())));
         this.setStatus(4);
      }

   }

   private void processGetRevision(String line) {
      int pos;
      if ((pos = line.indexOf("Branch Revision: ")) != -1) {
         this.getCurrentFile().setRevision(line.substring(pos + "Branch Revision: ".length()));
         this.setStatus(2);
      } else if (line.startsWith("=============================================================================")) {
         this.setStatus(1);
         this.addEntry(this.getCurrentChange(), this.getCurrentFile());
      }

   }

   private void processGetAuthor(String line) {
      if (line.startsWith("Author: ")) {
         int posDateTag = line.indexOf(" Date: ");
         String author = line.substring("Author: ".length(), posDateTag);
         this.getCurrentChange().setAuthor(author);
         String date = line.substring(posDateTag + " Date: ".length());
         Date dateObj = this.parseDate(date, this.userDateFormat, this.localFormat.toPattern());
         if (dateObj != null) {
            this.getCurrentChange().setDate(dateObj);
         } else {
            this.getCurrentChange().setDate(date, this.userDateFormat);
         }

         this.setStatus(3);
      }

   }

   private void processGetComment(String line) {
      if (line.startsWith("----------------------------")) {
         this.addEntry(this.getCurrentChange(), this.getCurrentFile());
         this.setCurrentChange(new ChangeSet());
         this.setCurrentFile(new ChangeFile(this.getCurrentFile().getName()));
         this.setStatus(4);
      } else if (line.startsWith("=============================================================================")) {
         this.addEntry(this.getCurrentChange(), this.getCurrentFile());
         this.setStatus(1);
      } else {
         this.getCurrentChange().setComment(this.getCurrentChange().getComment() + line + "\n");
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

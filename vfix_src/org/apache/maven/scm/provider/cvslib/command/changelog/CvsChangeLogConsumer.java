package org.apache.maven.scm.provider.cvslib.command.changelog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class CvsChangeLogConsumer extends AbstractConsumer {
   private List<ChangeSet> entries = new ArrayList();
   private static final int GET_FILE = 1;
   private static final int GET_DATE = 2;
   private static final int GET_COMMENT = 3;
   private static final int GET_REVISION = 4;
   private static final String START_FILE = "Working file: ";
   private static final String END_FILE = "=============================================================================";
   private static final String START_REVISION = "----------------------------";
   private static final String REVISION_TAG = "revision ";
   private static final String DATE_TAG = "date: ";
   private int status = 1;
   private ChangeSet currentChange = null;
   private ChangeFile currentFile = null;
   private String userDatePattern;

   public CvsChangeLogConsumer(ScmLogger logger, String userDatePattern) {
      super(logger);
      this.userDatePattern = userDatePattern;
   }

   public List<ChangeSet> getModifications() {
      Collections.sort(this.entries, new Comparator<ChangeSet>() {
         public int compare(ChangeSet set1, ChangeSet set2) {
            return set1.getDate().compareTo(set2.getDate());
         }
      });
      List<ChangeSet> fixedModifications = new ArrayList();
      ChangeSet currentEntry = null;
      Iterator entryIterator = this.entries.iterator();

      while(entryIterator.hasNext()) {
         ChangeSet entry = (ChangeSet)entryIterator.next();
         if (currentEntry == null) {
            currentEntry = entry;
         } else if (this.areEqual(currentEntry, entry)) {
            currentEntry.addFile((ChangeFile)entry.getFiles().get(0));
         } else {
            fixedModifications.add(currentEntry);
            currentEntry = entry;
         }
      }

      if (currentEntry != null) {
         fixedModifications.add(currentEntry);
      }

      return fixedModifications;
   }

   private boolean areEqual(ChangeSet set1, ChangeSet set2) {
      return set1.getAuthor().equals(set2.getAuthor()) && set1.getComment().equals(set2.getComment()) && set1.getDate().equals(set2.getDate());
   }

   public void consumeLine(String line) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(line);
      }

      try {
         switch(this.getStatus()) {
         case 1:
            this.processGetFile(line);
            break;
         case 2:
            this.processGetDate(line);
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
      } catch (Throwable var3) {
         if (this.getLogger().isWarnEnabled()) {
            this.getLogger().warn("Exception in the cvs changelog consumer.", var3);
         }
      }

   }

   private void addEntry(ChangeSet entry, ChangeFile file) {
      if (entry.getAuthor() != null) {
         entry.addFile(file);
         this.entries.add(entry);
      }
   }

   private void processGetFile(String line) {
      if (line.startsWith("Working file: ")) {
         this.setCurrentChange(new ChangeSet());
         this.setCurrentFile(new ChangeFile(line.substring("Working file: ".length(), line.length())));
         this.setStatus(4);
      }

   }

   private void processGetRevision(String line) {
      if (line.startsWith("revision ")) {
         this.getCurrentFile().setRevision(line.substring("revision ".length()));
         this.setStatus(2);
      } else if (line.startsWith("=============================================================================")) {
         this.setStatus(1);
         this.addEntry(this.getCurrentChange(), this.getCurrentFile());
      }

   }

   private void processGetDate(String line) {
      if (line.startsWith("date: ")) {
         StringTokenizer tokenizer = new StringTokenizer(line, ";");
         String datePart = tokenizer.nextToken().trim();
         String dateTime = datePart.substring("date: ".length());
         StringTokenizer dateTokenizer = new StringTokenizer(dateTime, " ");
         if (dateTokenizer.countTokens() == 2) {
            dateTime = dateTime + " UTC";
         }

         this.getCurrentChange().setDate(dateTime, this.userDatePattern);
         String authorPart = tokenizer.nextToken().trim();
         String author = authorPart.substring("author: ".length());
         this.getCurrentChange().setAuthor(author);
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

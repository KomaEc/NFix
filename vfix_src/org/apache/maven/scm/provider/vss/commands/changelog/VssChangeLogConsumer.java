package org.apache.maven.scm.provider.vss.commands.changelog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.vss.repository.VssScmProviderRepository;
import org.apache.maven.scm.util.AbstractConsumer;

public class VssChangeLogConsumer extends AbstractConsumer {
   private static final SimpleDateFormat ENTRY_KEY_TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");
   private static final int GET_FILE = 1;
   private static final int GET_FILE_PATH = 2;
   private static final int GET_AUTHOR = 3;
   private static final int GET_COMMENT = 4;
   private static final int GET_REVISION = 5;
   private static final int GET_UNKNOWN = 6;
   private static final String START_FILE = "*****  ";
   private static final String START_FILE_PATH = "$/";
   private static final String START_REVISION = "Version";
   private static final String START_AUTHOR = "User: ";
   private static final String START_COMMENT = "Comment: ";
   private Map<String, ChangeSet> entries = new TreeMap(Collections.reverseOrder());
   private ChangeFile currentFile;
   private ChangeSet currentChangeSet;
   private int lastStatus = 1;
   private VssScmProviderRepository repo;
   private String userDatePattern;

   public VssChangeLogConsumer(VssScmProviderRepository repo, String userDatePattern, ScmLogger logger) {
      super(logger);
      this.userDatePattern = userDatePattern;
      this.repo = repo;
   }

   public List<ChangeSet> getModifications() {
      return new ArrayList(this.entries.values());
   }

   public void consumeLine(String line) {
      switch(this.getLineStatus(line)) {
      case 1:
         this.processGetFile(line);
         break;
      case 2:
         this.processGetFilePath(line);
         break;
      case 3:
         this.processGetAuthor(line);
         break;
      case 4:
         this.processGetComment(line);
         break;
      case 5:
         this.processGetRevision(line);
      }

   }

   private void processGetComment(String line) {
      String[] commentLine = line.split(":");
      if (commentLine.length == 2) {
         this.currentChangeSet.setComment(commentLine[1]);
      } else {
         String comment = this.currentChangeSet.getComment();
         comment = comment + " " + line;
         this.currentChangeSet.setComment(comment);
      }

   }

   private void processGetAuthor(String line) {
      String[] result = line.split("\\s");
      Vector<String> vector = new Vector();

      for(int i = 0; i < result.length; ++i) {
         if (!result[i].equals("")) {
            vector.add(result[i]);
         }
      }

      this.currentChangeSet.setAuthor((String)vector.get(1));
      this.currentChangeSet.setDate(this.parseDate((String)vector.get(3) + " " + (String)vector.get(5), this.userDatePattern, "dd.MM.yy HH:mm"));
   }

   private void processGetFile(String line) {
      this.currentChangeSet = new ChangeSet();
      String[] fileLine = line.split(" ");
      this.currentFile = new ChangeFile(fileLine[2]);
   }

   private void processGetFilePath(String line) {
      if (this.currentFile != null) {
         String fileName = this.currentFile.getName();
         String path = line.substring(line.indexOf(36), line.length());
         String longPath = path.substring(this.repo.getProject().length() + 1, path.length()) + "/" + fileName;
         this.currentFile.setName(longPath);
         this.addEntry(this.currentChangeSet, this.currentFile);
      }

   }

   private void processGetRevision(String line) {
      String[] revisionLine = line.split(" ");
      this.currentFile.setRevision(revisionLine[1]);
   }

   private int getLineStatus(String line) {
      int argument = 6;
      if (line.startsWith("*****  ")) {
         argument = 1;
      } else if (line.startsWith("Version")) {
         argument = 5;
      } else if (line.startsWith("User: ")) {
         argument = 3;
      } else if (line.indexOf("$/") != -1) {
         argument = 2;
      } else if (line.startsWith("Comment: ")) {
         argument = 4;
      } else if (this.lastStatus == 4) {
         argument = this.lastStatus;
      }

      this.lastStatus = argument;
      return argument;
   }

   private void addEntry(ChangeSet entry, ChangeFile file) {
      if (entry.getAuthor() != null) {
         String key = ENTRY_KEY_TIMESTAMP_FORMAT.format(entry.getDate()) + entry.getAuthor() + entry.getComment();
         if (!this.entries.containsKey(key)) {
            entry.addFile(file);
            this.entries.put(key, entry);
         } else {
            ChangeSet existingEntry = (ChangeSet)this.entries.get(key);
            existingEntry.addFile(file);
         }

      }
   }
}

package org.apache.tools.ant.taskdefs.cvslib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.TimeZone;

class ChangeLogParser {
   private static final int GET_FILE = 1;
   private static final int GET_DATE = 2;
   private static final int GET_COMMENT = 3;
   private static final int GET_REVISION = 4;
   private static final int GET_PREVIOUS_REV = 5;
   private static final SimpleDateFormat INPUT_DATE;
   private static final SimpleDateFormat CVS1129_INPUT_DATE;
   private String file;
   private String date;
   private String author;
   private String comment;
   private String revision;
   private String previousRevision;
   private int status = 1;
   private final Hashtable entries = new Hashtable();

   public CVSEntry[] getEntrySetAsArray() {
      CVSEntry[] array = new CVSEntry[this.entries.size()];
      int i = 0;

      for(Enumeration e = this.entries.elements(); e.hasMoreElements(); array[i++] = (CVSEntry)e.nextElement()) {
      }

      return array;
   }

   public void stdout(String line) {
      switch(this.status) {
      case 1:
         this.reset();
         this.processFile(line);
         break;
      case 2:
         this.processDate(line);
         break;
      case 3:
         this.processComment(line);
         break;
      case 4:
         this.processRevision(line);
         break;
      case 5:
         this.processGetPreviousRevision(line);
      }

   }

   private void processComment(String line) {
      String lineSeparator = System.getProperty("line.separator");
      int end;
      if (line.equals("=============================================================================")) {
         end = this.comment.length() - lineSeparator.length();
         this.comment = this.comment.substring(0, end);
         this.saveEntry();
         this.status = 1;
      } else if (line.equals("----------------------------")) {
         end = this.comment.length() - lineSeparator.length();
         this.comment = this.comment.substring(0, end);
         this.status = 5;
      } else {
         this.comment = this.comment + line + lineSeparator;
      }

   }

   private void processFile(String line) {
      if (line.startsWith("Working file:")) {
         this.file = line.substring(14, line.length());
         this.status = 4;
      }

   }

   private void processRevision(String line) {
      if (line.startsWith("revision")) {
         this.revision = line.substring(9);
         this.status = 2;
      } else if (line.startsWith("======")) {
         this.status = 1;
      }

   }

   private void processDate(String line) {
      if (line.startsWith("date:")) {
         int endOfDateIndex = line.indexOf(59);
         this.date = line.substring("date: ".length(), endOfDateIndex);
         int startOfAuthorIndex = line.indexOf("author: ", endOfDateIndex + 1);
         int endOfAuthorIndex = line.indexOf(59, startOfAuthorIndex + 1);
         this.author = line.substring("author: ".length() + startOfAuthorIndex, endOfAuthorIndex);
         this.status = 3;
         this.comment = "";
      }

   }

   private void processGetPreviousRevision(String line) {
      if (!line.startsWith("revision ")) {
         throw new IllegalStateException("Unexpected line from CVS: " + line);
      } else {
         this.previousRevision = line.substring("revision ".length());
         this.saveEntry();
         this.revision = this.previousRevision;
         this.status = 2;
      }
   }

   private void saveEntry() {
      String entryKey = this.date + this.author + this.comment;
      CVSEntry entry;
      if (!this.entries.containsKey(entryKey)) {
         Date dateObject = this.parseDate(this.date);
         entry = new CVSEntry(dateObject, this.author, this.comment);
         this.entries.put(entryKey, entry);
      } else {
         entry = (CVSEntry)this.entries.get(entryKey);
      }

      entry.addFile(this.file, this.revision, this.previousRevision);
   }

   private Date parseDate(String date) {
      try {
         return INPUT_DATE.parse(date);
      } catch (ParseException var5) {
         try {
            return CVS1129_INPUT_DATE.parse(date);
         } catch (ParseException var4) {
            throw new IllegalStateException("Invalid date format: " + date);
         }
      }
   }

   public void reset() {
      this.file = null;
      this.date = null;
      this.author = null;
      this.comment = null;
      this.revision = null;
      this.previousRevision = null;
   }

   static {
      INPUT_DATE = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
      CVS1129_INPUT_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US);
      TimeZone utc = TimeZone.getTimeZone("UTC");
      INPUT_DATE.setTimeZone(utc);
      CVS1129_INPUT_DATE.setTimeZone(utc);
   }
}

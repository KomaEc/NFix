package org.netbeans.lib.cvsclient.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class Entry {
   public static final String DUMMY_TIMESTAMP = "dummy timestamp";
   public static final String DUMMY_TIMESTAMP_NEW_ENTRY = "dummy timestamp from new-entry";
   public static final String MERGE_TIMESTAMP = "Result of merge";
   private static final String TAG = "T";
   private static final String DATE = "D";
   private static SimpleDateFormat stickyDateFormatter;
   private static final String BINARY_FILE = "-kb";
   private static final String NO_USER_FILE = "";
   private static final String NEW_USER_FILE = "0";
   private static final String REMOVE_USER_FILE = "-";
   private static SimpleDateFormat lastModifiedDateFormatter;
   public static final char HAD_CONFLICTS = '+';
   public static final char TIMESTAMP_MATCHES_FILE = '=';
   public static final String HAD_CONFLICTS_AND_TIMESTAMP_MATCHES_FILE = "+=";
   private static final String DIRECTORY_PREFIX = "D/";
   private String name;
   private String revision;
   private String conflict;
   private Date lastModified;
   private String options;
   private String tag;
   private Date date;
   private boolean directory;

   private static SimpleDateFormat getStickyDateFormatter() {
      if (stickyDateFormatter == null) {
         stickyDateFormatter = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss");
      }

      return stickyDateFormatter;
   }

   public static SimpleDateFormat getLastModifiedDateFormatter() {
      if (lastModifiedDateFormatter == null) {
         lastModifiedDateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US);
         lastModifiedDateFormatter.setTimeZone(getTimeZone());
      }

      return lastModifiedDateFormatter;
   }

   public static TimeZone getTimeZone() {
      return TimeZone.getTimeZone("GMT");
   }

   public Entry(String var1) {
      this.init(var1);
   }

   public Entry() {
   }

   protected void init(String var1) {
      if (var1.startsWith("D/")) {
         this.directory = true;
         var1 = var1.substring(1);
      }

      int[] var2 = new int[5];

      try {
         var2[0] = 0;

         for(int var3 = 1; var3 < 5; ++var3) {
            var2[var3] = var1.indexOf(47, var2[var3 - 1] + 1);
         }

         if (var2[1] > 0) {
            this.name = var1.substring(var2[0] + 1, var2[1]);
            this.revision = var1.substring(var2[1] + 1, var2[2]);
            String var8;
            if (var2[3] - var2[2] > 1) {
               var8 = var1.substring(var2[2] + 1, var2[3]);
               this.setConflict(var8);
            }

            if (var2[4] - var2[3] > 1) {
               this.options = var1.substring(var2[3] + 1, var2[4]);
            }

            if (var2[4] != var1.length() - 1) {
               var8 = var1.substring(var2[4] + 1);
               if (var8.startsWith("T")) {
                  this.setTag(var8.substring(1));
               } else if (var8.startsWith("D")) {
                  try {
                     String var4 = var8.substring("D".length());
                     Date var5 = getStickyDateFormatter().parse(var4);
                     this.setDate(var5);
                  } catch (ParseException var6) {
                     System.err.println("We got another inconsistency in the library's date formatting.");
                  }
               }
            }
         }

      } catch (Exception var7) {
         System.err.println("Error parsing entry line: " + var7);
         var7.printStackTrace();
         throw new IllegalArgumentException("Invalid entry line: " + var1);
      }
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getRevision() {
      return this.revision;
   }

   public void setRevision(String var1) {
      this.revision = var1;
   }

   public Date getLastModified() {
      return this.lastModified;
   }

   public String getConflict() {
      return this.conflict;
   }

   public void setConflict(String var1) {
      this.conflict = var1;
      this.lastModified = null;
      if (var1 != null && !var1.equals("dummy timestamp") && !var1.equals("Result of merge") && !var1.equals("dummy timestamp from new-entry")) {
         String var2 = var1;
         int var3 = var1.indexOf(43);
         if (var3 >= 0) {
            int var4 = var1.indexOf(61);
            var3 = Math.max(var3, var4);
         }

         if (var3 >= 0) {
            var2 = var1.substring(var3 + 1);
         }

         if (var2.length() != 0) {
            try {
               this.lastModified = getLastModifiedDateFormatter().parse(var2);
            } catch (Exception var5) {
               this.lastModified = null;
            }

         }
      }
   }

   public String getOptions() {
      return this.options;
   }

   public void setOptions(String var1) {
      this.options = var1;
   }

   public String getStickyInformation() {
      return this.tag != null ? this.tag : this.getDateFormatted();
   }

   public String getTag() {
      return this.tag;
   }

   public void setTag(String var1) {
      this.tag = var1;
      this.date = null;
   }

   public Date getDate() {
      return this.date;
   }

   public String getDateFormatted() {
      if (this.getDate() == null) {
         return null;
      } else {
         SimpleDateFormat var1 = getStickyDateFormatter();
         String var2 = var1.format(this.getDate());
         return var2;
      }
   }

   public void setDate(Date var1) {
      this.date = var1;
      this.tag = null;
   }

   public boolean hasDate() {
      return this.date != null;
   }

   public boolean hasTag() {
      return this.tag != null;
   }

   public boolean isBinary() {
      return this.options != null && this.options.equals("-kb");
   }

   public boolean isNoUserFile() {
      return this.revision == null || this.revision.equals("");
   }

   public boolean isNewUserFile() {
      return this.revision != null && this.revision.startsWith("0");
   }

   public boolean isUserFileToBeRemoved() {
      return this.revision != null && this.revision.startsWith("-");
   }

   public boolean isValid() {
      return this.getName() != null && this.getName().length() > 0;
   }

   public boolean isDirectory() {
      return this.directory;
   }

   public void setDirectory(boolean var1) {
      this.directory = var1;
   }

   public boolean hadConflicts() {
      if (this.conflict != null) {
         return this.conflict.indexOf(43) >= 0;
      } else {
         return false;
      }
   }

   public boolean timestampMatchesFile() {
      return this.conflict.charAt(1) == '=';
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.directory) {
         var1.append("D/");
      } else {
         var1.append('/');
      }

      if (this.name != null) {
         var1.append(this.name);
         var1.append('/');
         if (this.revision != null) {
            var1.append(this.revision);
         }

         var1.append('/');
         if (this.conflict != null) {
            var1.append(this.conflict);
         }

         var1.append('/');
         if (this.options != null) {
            var1.append(this.options);
         }

         var1.append('/');
         if (this.tag != null && this.date == null) {
            if (!"HEAD".equals(this.tag)) {
               var1.append("T");
               var1.append(this.getTag());
            }
         } else if (this.tag == null && this.date != null) {
            String var2 = this.getDateFormatted();
            var1.append("D");
            var1.append(var2);
         }
      }

      return var1.toString();
   }
}

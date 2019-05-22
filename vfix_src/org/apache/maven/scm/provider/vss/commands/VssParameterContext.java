package org.apache.maven.scm.provider.vss.commands;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.provider.vss.repository.VssScmProviderRepository;

public class VssParameterContext {
   private String vssPath = null;
   private String autoResponse = System.getProperty("maven.scm.autoResponse");
   private String ssDir;
   private String vssLogin;
   private String comment;
   private String user;
   private String fromLabel;
   private String toLabel;
   private boolean quiet;
   private boolean recursive;
   private boolean writable;
   private String label;
   private String style;
   private String version;
   private String date;
   private String localPath;
   private String timestamp;
   private String writableFiles = null;
   private String fromDate = null;
   private String toDate = null;
   private int numDays = Integer.MIN_VALUE;
   private boolean getLocalCopy = true;
   private DateFormat dateFormat = DateFormat.getDateInstance(3);
   private String outputFileName;

   public static VssParameterContext getInstance(Object obj) {
      return new VssParameterContext((VssScmProviderRepository)obj);
   }

   public VssParameterContext(VssScmProviderRepository repo) {
      this.ssDir = repo.getVssdir();
      this.user = repo.getUser();
   }

   public String getGetLocalCopy() {
      return !this.getLocalCopy ? "-G-" : "";
   }

   private String calcDate(String startDate, int daysToAdd) throws ParseException {
      new Date();
      Calendar calendar = new GregorianCalendar();
      Date currentDate = this.dateFormat.parse(startDate);
      calendar.setTime(currentDate);
      calendar.add(5, daysToAdd);
      return this.dateFormat.format(calendar.getTime());
   }

   public String getFileTimeStamp() {
      return this.timestamp == null ? "" : this.timestamp;
   }

   public String getLocalpath() throws ScmException {
      String lclPath = "";
      if (this.localPath != null) {
         File dir = new File(this.localPath);
         if (!dir.exists()) {
            boolean done = dir.mkdirs();
            if (!done) {
               String msg = "Directory " + this.localPath + " creation was not " + "successful for an unknown reason";
               throw new ScmException(msg);
            }
         }

         lclPath = "-GL" + this.localPath;
      }

      return lclPath;
   }

   public String getLabel() {
      String shortLabel = "";
      if (this.label != null && this.label.length() > 0) {
         shortLabel = "-L" + this.getShortLabel();
      }

      return shortLabel;
   }

   public String getVersionDateLabel() {
      String versionDateLabel = "";
      if (this.version != null) {
         versionDateLabel = "-V" + this.version;
      } else if (this.date != null) {
         versionDateLabel = "-Vd" + this.date;
      } else {
         String shortLabel = this.getShortLabel();
         if (shortLabel != null && !shortLabel.equals("")) {
            versionDateLabel = "-VL" + shortLabel;
         }
      }

      return versionDateLabel;
   }

   public String getVersion() {
      return this.version != null ? "-V" + this.version : "";
   }

   private String getShortLabel() {
      String shortLabel;
      if (this.label != null && this.label.length() > 31) {
         shortLabel = this.label.substring(0, 30);
      } else {
         shortLabel = this.label;
      }

      return shortLabel;
   }

   public String getStyle() {
      return this.style != null ? this.style : "";
   }

   public String getRecursive() {
      return this.recursive ? "-R" : "";
   }

   public String getWritable() {
      return this.writable ? "-W" : "";
   }

   public String getQuiet() {
      return this.quiet ? "-O-" : "";
   }

   public String getVersionLabel() {
      if (this.fromLabel == null && this.toLabel == null) {
         return "";
      } else if (this.fromLabel != null && this.toLabel != null) {
         if (this.fromLabel.length() > 31) {
            this.fromLabel = this.fromLabel.substring(0, 30);
         }

         if (this.toLabel.length() > 31) {
            this.toLabel = this.toLabel.substring(0, 30);
         }

         return "-VL" + this.toLabel + "~L" + this.fromLabel;
      } else if (this.fromLabel != null) {
         if (this.fromLabel.length() > 31) {
            this.fromLabel = this.fromLabel.substring(0, 30);
         }

         return "-V~L" + this.fromLabel;
      } else {
         if (this.toLabel.length() > 31) {
            this.toLabel = this.toLabel.substring(0, 30);
         }

         return "-VL" + this.toLabel;
      }
   }

   public String getUser() {
      return this.user != null ? "-U" + this.user : "";
   }

   public String getComment() {
      return this.comment != null ? "-C" + this.comment : "-C-";
   }

   public String getLogin() {
      return this.vssLogin != null ? "-Y" + this.vssLogin : "";
   }

   public String getAutoresponse() {
      if (this.autoResponse == null) {
         return "-I-";
      } else if (this.autoResponse.equalsIgnoreCase("Y")) {
         return "-I-Y";
      } else {
         return this.autoResponse.equalsIgnoreCase("N") ? "-I-N" : "-I-";
      }
   }

   public String getSSCommand() {
      if (this.ssDir == null) {
         return "ss";
      } else {
         return this.ssDir.endsWith(File.separator) ? this.ssDir + "ss" : this.ssDir + File.separator + "ss";
      }
   }

   public String getVssPath() {
      return this.vssPath;
   }

   public String getVersionDate() throws ScmException {
      if (this.fromDate == null && this.toDate == null && this.numDays == Integer.MIN_VALUE) {
         return "";
      } else if (this.fromDate != null && this.toDate != null) {
         return "-Vd" + this.toDate + "~d" + this.fromDate;
      } else {
         String msg;
         if (this.toDate != null && this.numDays != Integer.MIN_VALUE) {
            try {
               return "-Vd" + this.toDate + "~d" + this.calcDate(this.toDate, this.numDays);
            } catch (ParseException var3) {
               msg = "Error parsing date: " + this.toDate;
               throw new ScmException(msg);
            }
         } else if (this.fromDate != null && this.numDays != Integer.MIN_VALUE) {
            try {
               return "-Vd" + this.calcDate(this.fromDate, this.numDays) + "~d" + this.fromDate;
            } catch (ParseException var4) {
               msg = "Error parsing date: " + this.fromDate;
               throw new ScmException(msg);
            }
         } else {
            return this.fromDate != null ? "-V~d" + this.fromDate : "-Vd" + this.toDate;
         }
      }
   }

   public String getOutput() {
      return this.outputFileName != null ? "-O" + this.outputFileName : "";
   }

   public String getWritableFiles() {
      return this.writableFiles == null ? "" : this.writableFiles;
   }
}

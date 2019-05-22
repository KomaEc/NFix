package org.netbeans.lib.cvsclient.command.status;

import java.io.File;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;
import org.netbeans.lib.cvsclient.file.FileStatus;

public class StatusBuilder implements Builder {
   private static final String UNKNOWN = ": nothing known about";
   private static final String EXAM_DIR = ": Examining";
   private static final String NOT_IN_REPOSITORY = "No revision control file";
   private static final String FILE = "File: ";
   private static final String STATUS = "Status:";
   private static final String NO_FILE_FILENAME = "no file";
   private static final String WORK_REV = "   Working revision:";
   private static final String REP_REV = "   Repository revision:";
   private static final String TAG = "   Sticky Tag:";
   private static final String DATE = "   Sticky Date:";
   private static final String OPTIONS = "   Sticky Options:";
   private static final String EXISTING_TAGS = "   Existing Tags:";
   private static final String EMPTY_BEFORE_TAGS = "   ";
   private static final String NO_TAGS = "   No Tags Exist";
   private static final String UNKNOWN_FILE = "? ";
   private StatusInformation statusInformation;
   private EventManager eventManager;
   private final StatusCommand statusCommand;
   private String relativeDirectory;
   private final String localPath;
   private boolean beginning;
   private boolean readingTags;
   private final File[] fileArray;

   public StatusBuilder(EventManager var1, StatusCommand var2) {
      this.eventManager = var1;
      this.statusCommand = var2;
      File[] var3 = var2.getFiles();
      if (var3 != null) {
         this.fileArray = new File[var3.length];
         System.arraycopy(var3, 0, this.fileArray, 0, var3.length);
      } else {
         this.fileArray = null;
      }

      this.localPath = var2.getLocalDirectory();
      this.beginning = true;
   }

   public void outputDone() {
      if (this.statusInformation != null) {
         this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.statusInformation));
         this.statusInformation = null;
         this.readingTags = false;
      }

   }

   public void parseLine(String var1, boolean var2) {
      if (this.readingTags) {
         if (var1.startsWith("   No Tags Exist")) {
            this.outputDone();
            return;
         }

         int var3 = var1.indexOf("\t(");
         if (var3 <= 0) {
            this.outputDone();
            return;
         }

         String var4 = var1.substring(0, var3).trim();
         String var5 = var1.substring(var3 + 2, var1.length() - 1);
         if (this.statusInformation == null) {
            this.statusInformation = new StatusInformation();
         }

         this.statusInformation.addExistingTag(var4, var5);
      }

      if (var1.startsWith("? ") && this.beginning) {
         File var6 = new File(this.localPath, var1.substring("? ".length()));
         this.statusInformation = new StatusInformation();
         this.statusInformation.setFile(var6);
         this.statusInformation.setStatusString(FileStatus.UNKNOWN.toString());
         this.outputDone();
      }

      if (var1.startsWith(": nothing known about")) {
         this.outputDone();
         this.beginning = false;
      } else if (var1.indexOf(": Examining") >= 0) {
         this.relativeDirectory = var1.substring(var1.indexOf(": Examining") + ": Examining".length()).trim();
         this.beginning = false;
      } else if (var1.startsWith("File: ")) {
         this.outputDone();
         this.statusInformation = new StatusInformation();
         this.processFileAndStatusLine(var1.substring("File: ".length()));
         this.beginning = false;
      } else if (var1.startsWith("   Working revision:")) {
         this.processWorkRev(var1.substring("   Working revision:".length()));
      } else if (var1.startsWith("   Repository revision:")) {
         this.processRepRev(var1.substring("   Repository revision:".length()));
      } else if (var1.startsWith("   Sticky Tag:")) {
         this.processTag(var1.substring("   Sticky Tag:".length()));
      } else if (var1.startsWith("   Sticky Date:")) {
         this.processDate(var1.substring("   Sticky Date:".length()));
      } else if (var1.startsWith("   Sticky Options:")) {
         this.processOptions(var1.substring("   Sticky Options:".length()));
         if (!this.statusCommand.isIncludeTags()) {
            this.outputDone();
         }
      } else if (var1.startsWith("   Existing Tags:")) {
         this.readingTags = true;
      }

   }

   private File createFile(String var1) {
      File var2 = null;
      if (this.relativeDirectory != null) {
         if (this.relativeDirectory.trim().equals(".")) {
            var2 = new File(this.localPath, var1);
         } else {
            var2 = new File(this.localPath, this.relativeDirectory + '/' + var1);
         }
      } else if (this.fileArray != null) {
         for(int var3 = 0; var3 < this.fileArray.length; ++var3) {
            File var4 = this.fileArray[var3];
            if (var4 != null && !var4.isDirectory()) {
               String var5 = var4.getName();
               if (var1.equals(var5)) {
                  this.fileArray[var3] = null;
                  var2 = var4;
                  break;
               }
            }
         }
      }

      if (var2 == null) {
         System.err.println("JAVACVS ERROR!! wrong algorithm for assigning path to single files(1)!!");
      }

      return var2;
   }

   private void processFileAndStatusLine(String var1) {
      int var2 = var1.lastIndexOf("Status:");
      String var3 = var1.substring(0, var2).trim();
      if (var3.startsWith("no file")) {
         var3 = var3.substring(8);
      }

      this.statusInformation.setFile(this.createFile(var3));
      String var4 = new String(var1.substring(var2 + 8).trim());
      this.statusInformation.setStatusString(var4);
   }

   private boolean assertNotNull() {
      if (this.statusInformation == null) {
         System.err.println("Bug: statusInformation must not be null!");
         return false;
      } else {
         return true;
      }
   }

   private void processWorkRev(String var1) {
      if (this.assertNotNull()) {
         this.statusInformation.setWorkingRevision(var1.trim().intern());
      }
   }

   private void processRepRev(String var1) {
      if (this.assertNotNull()) {
         var1 = var1.trim();
         if (var1.startsWith("No revision control file")) {
            this.statusInformation.setRepositoryRevision(var1.trim().intern());
         } else {
            int var2 = var1.indexOf(9);
            if (var2 > 0) {
               this.statusInformation.setRepositoryRevision(var1.substring(0, var2).trim().intern());
               this.statusInformation.setRepositoryFileName(new String(var1.substring(var2).trim()));
            } else {
               this.statusInformation.setRepositoryRevision("");
               this.statusInformation.setRepositoryFileName("");
            }

         }
      }
   }

   private void processTag(String var1) {
      if (this.assertNotNull()) {
         this.statusInformation.setStickyTag(var1.trim().intern());
      }
   }

   private void processDate(String var1) {
      if (this.assertNotNull()) {
         this.statusInformation.setStickyDate(var1.trim().intern());
      }
   }

   private void processOptions(String var1) {
      if (this.assertNotNull()) {
         this.statusInformation.setStickyOptions(var1.trim().intern());
      }
   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }
}

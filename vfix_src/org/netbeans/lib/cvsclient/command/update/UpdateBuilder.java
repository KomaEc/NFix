package org.netbeans.lib.cvsclient.command.update;

import java.io.File;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.DefaultFileInfoContainer;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class UpdateBuilder implements Builder {
   public static final String UNKNOWN = ": nothing known about";
   public static final String EXAM_DIR = ": Updating";
   public static final String TO_ADD = ": use `cvs add' to create an entry for";
   public static final String STATES = "U P A R M C ? ";
   public static final String WARNING = ": warning: ";
   public static final String SERVER = "server: ";
   public static final String PERTINENT = "is not (any longer) pertinent";
   public static final String CONFLICTS = "rcsmerge: warning: conflicts during merge";
   public static final String NOT_IN_REPOSITORY = "is no longer in the repository";
   private static final String MERGE_SAME = " already contains the differences between";
   private DefaultFileInfoContainer fileInfoContainer;
   private EventManager eventManager;
   private final String localPath;
   private String diagnostics;

   public UpdateBuilder(EventManager var1, String var2) {
      this.eventManager = var1;
      this.localPath = var2;
   }

   public void outputDone() {
      if (this.fileInfoContainer != null) {
         if (this.fileInfoContainer.getFile() == null) {
            System.err.println("#65387 CVS: firing invalid event while processing: " + this.diagnostics);
         }

         this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.fileInfoContainer));
         this.fileInfoContainer = null;
      }

   }

   public void parseLine(String var1, boolean var2) {
      this.diagnostics = var1;
      if (var1.indexOf(": nothing known about") >= 0) {
         this.processUnknownFile(var1, var1.indexOf(": nothing known about") + ": nothing known about".length());
      } else if (var1.indexOf(": use `cvs add' to create an entry for") >= 0) {
         this.processUnknownFile(var1, var1.indexOf(": use `cvs add' to create an entry for") + ": use `cvs add' to create an entry for".length());
      } else {
         if (var1.indexOf(": Updating") >= 0) {
            return;
         }

         if (var1.startsWith("rcsmerge: warning: conflicts during merge")) {
            if (this.fileInfoContainer != null) {
               this.fileInfoContainer.setType("C");
            }
         } else {
            String var3;
            if (var1.indexOf(": warning: ") >= 0) {
               if (var1.indexOf("is not (any longer) pertinent") > 0) {
                  var3 = var1.substring(var1.indexOf(": warning: ") + ": warning: ".length(), var1.indexOf("is not (any longer) pertinent")).trim();
                  this.processNotPertinent(var3);
               }

               return;
            }

            if (var1.indexOf(" already contains the differences between") >= 0) {
               this.ensureExistingFileInfoContainer();
               this.fileInfoContainer.setType("G");
               var3 = var1.substring(0, var1.indexOf(" already contains the differences between"));
               this.fileInfoContainer.setFile(this.createFile(var3));
               this.outputDone();
            } else {
               if (var1.indexOf("is no longer in the repository") > 0) {
                  var3 = var1.substring(var1.indexOf("server: ") + "server: ".length(), var1.indexOf("is no longer in the repository")).trim();
                  this.processNotPertinent(var3);
                  return;
               }

               if (var1.length() > 2) {
                  var3 = var1.substring(0, 2);
                  if ("U P A R M C ? ".indexOf(var3) >= 0) {
                     this.processFile(var1);
                     return;
                  }
               }
            }
         }
      }

   }

   private File createFile(String var1) {
      return new File(this.localPath, var1);
   }

   private void ensureExistingFileInfoContainer() {
      if (this.fileInfoContainer == null) {
         this.fileInfoContainer = new DefaultFileInfoContainer();
      }
   }

   private void processUnknownFile(String var1, int var2) {
      this.outputDone();
      this.fileInfoContainer = new DefaultFileInfoContainer();
      this.fileInfoContainer.setType("?");
      String var3 = var1.substring(var2).trim();
      this.fileInfoContainer.setFile(this.createFile(var3));
   }

   private void processFile(String var1) {
      String var2 = var1.substring(2).trim();
      if (var2.startsWith("no file")) {
         var2 = var2.substring(8);
      }

      if (var2.startsWith("./")) {
         var2 = var2.substring(2);
      }

      File var3 = this.createFile(var2);
      if (this.fileInfoContainer != null) {
         if (this.fileInfoContainer.getFile() == null) {
            this.fileInfoContainer.setFile(var3);
         }

         if (var3.equals(this.fileInfoContainer.getFile())) {
            if (!this.fileInfoContainer.getType().equals("?")) {
               this.outputDone();
               return;
            }

            this.fileInfoContainer = null;
         }
      }

      this.outputDone();
      this.ensureExistingFileInfoContainer();
      this.fileInfoContainer.setType(var1.substring(0, 1));
      this.fileInfoContainer.setFile(var3);
   }

   private void processLog(String var1) {
      this.ensureExistingFileInfoContainer();
   }

   private void processNotPertinent(String var1) {
      this.outputDone();
      File var2 = this.createFile(var1);
      this.ensureExistingFileInfoContainer();
      this.fileInfoContainer.setType("Y");
      this.fileInfoContainer.setFile(var2);
   }

   public void parseEnhancedMessage(String var1, Object var2) {
      if (var1.equals("Merged_Response_File_Path")) {
         this.ensureExistingFileInfoContainer();
         String var3 = var2.toString();
         File var4 = new File(var3);
         if (!var4.equals(this.fileInfoContainer.getFile())) {
            this.fileInfoContainer.setFile(var4);
            this.fileInfoContainer.setType("G");
         }

         this.outputDone();
      }

   }
}

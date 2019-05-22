package org.netbeans.lib.cvsclient.command.commit;

import java.io.File;
import java.io.IOException;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class CommitBuilder implements Builder {
   public static final String UNKNOWN = "commit: nothing known about `";
   public static final String EXAM_DIR = ": Examining";
   public static final String REMOVING = "Removing ";
   public static final String NEW_REVISION = "new revision:";
   public static final String INITIAL_REVISION = "initial revision:";
   public static final String DELETED_REVISION = "delete";
   public static final String DONE = "done";
   public static final String RCS_FILE = "RCS file: ";
   public static final String ADD = "commit: use `cvs add' to create an entry for ";
   public static final String COMMITTED = " <-- ";
   private CommitInformation commitInformation;
   private File fileDirectory;
   private final EventManager eventManager;
   private final String localPath;
   private final String repositoryRoot;
   private boolean isAdding;

   public CommitBuilder(EventManager var1, String var2, String var3) {
      this.eventManager = var1;
      this.localPath = var2;
      this.repositoryRoot = var3;
   }

   public void outputDone() {
      if (this.commitInformation != null) {
         this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.commitInformation));
         this.commitInformation = null;
      }

   }

   public void parseLine(String var1, boolean var2) {
      if (var1.indexOf("commit: nothing known about `") >= 0) {
         this.outputDone();
         this.processUnknownFile(var1.substring(var1.indexOf("commit: nothing known about `") + "commit: nothing known about `".length()).trim());
      } else if (var1.indexOf("commit: use `cvs add' to create an entry for ") > 0) {
         this.processToAddFile(var1.substring(var1.indexOf("commit: use `cvs add' to create an entry for ") + "commit: use `cvs add' to create an entry for ".length()).trim());
      } else {
         int var3;
         if ((var3 = var1.indexOf(" <-- ")) > 0) {
            this.outputDone();
            String var4 = var1.substring(var3 + " <-- ".length()).trim();
            File var6;
            if (this.fileDirectory == null) {
               String var5 = var1.substring(0, var3).trim();
               if (var5.startsWith(this.repositoryRoot)) {
                  var5 = var5.substring(this.repositoryRoot.length());
                  if (var5.startsWith("/")) {
                     var5 = var5.substring(1);
                  }
               }

               var3 = var5.lastIndexOf(47);
               if (var3 > 0) {
                  var5 = var5.substring(0, var3);
               }

               var6 = this.findFile(var4, var5);
            } else {
               var6 = new File(this.fileDirectory, var4);
            }

            this.processFile(var6);
            if (this.isAdding) {
               this.commitInformation.setType("Added");
               this.isAdding = false;
            } else {
               this.commitInformation.setType("Changed");
            }
         } else if (var1.startsWith("Removing ")) {
            this.outputDone();
            this.processFile(var1.substring("Removing ".length(), var1.length() - 1));
            this.commitInformation.setType("Removed");
         } else if (var1.indexOf(": Examining") >= 0) {
            this.fileDirectory = new File(this.localPath, var1.substring(var1.indexOf(": Examining") + ": Examining".length()).trim());
         } else if (var1.startsWith("RCS file: ")) {
            this.isAdding = true;
         } else if (var1.startsWith("done")) {
            this.outputDone();
         } else if (var1.startsWith("initial revision:")) {
            this.processRevision(var1.substring("initial revision:".length()));
            this.commitInformation.setType("Added");
         } else if (var1.startsWith("new revision:")) {
            this.processRevision(var1.substring("new revision:".length()));
         }
      }

   }

   private File createFile(String var1) {
      return new File(this.localPath, var1);
   }

   private void processUnknownFile(String var1) {
      this.commitInformation = new CommitInformation();
      this.commitInformation.setType("Unknown");
      int var2 = var1.indexOf(39);
      String var3 = var1.substring(0, var2).trim();
      this.commitInformation.setFile(this.createFile(var3));
      this.outputDone();
   }

   private void processToAddFile(String var1) {
      this.commitInformation = new CommitInformation();
      this.commitInformation.setType("To-be-added");
      String var2 = var1.trim();
      if (var2.endsWith(";")) {
         var2 = var2.substring(0, var2.length() - 2);
      }

      this.commitInformation.setFile(this.createFile(var2));
      this.outputDone();
   }

   private void processFile(String var1) {
      if (this.commitInformation == null) {
         this.commitInformation = new CommitInformation();
      }

      if (var1.startsWith("no file")) {
         var1 = var1.substring(8);
      }

      this.commitInformation.setFile(this.createFile(var1));
   }

   private void processFile(File var1) {
      if (this.commitInformation == null) {
         this.commitInformation = new CommitInformation();
      }

      this.commitInformation.setFile(var1);
   }

   private void processRevision(String var1) {
      int var2 = var1.indexOf(59);
      if (var2 >= 0) {
         var1 = var1.substring(0, var2);
      }

      var1 = var1.trim();
      if ("delete".equals(var1)) {
         this.commitInformation.setType("Removed");
      }

      this.commitInformation.setRevision(var1);
   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }

   private File findFile(String var1, String var2) {
      File var3 = new File(this.localPath);
      if (var2.endsWith("/Attic")) {
         var2 = var2.substring(0, var2.length() - 6);
      }

      File var4 = this.quickFindFile(var3, var1, var2);
      return var4 != null ? var4 : this.findFile(var3, var1, var2);
   }

   private File findFile(File var1, String var2, String var3) {
      if (this.isWorkForRepository(var1, var3)) {
         return new File(var1, var2);
      } else {
         File var4 = null;
         File[] var5 = var1.listFiles();
         if (var5 != null) {
            for(int var6 = 0; var6 < var5.length; ++var6) {
               if (var5[var6].isDirectory()) {
                  var4 = this.findFile(var5[var6], var2, var3);
                  if (var4 != null) {
                     break;
                  }
               }
            }
         }

         return var4;
      }
   }

   private File quickFindFile(File var1, String var2, String var3) {
      do {
         File var4 = new File(var1, var3);
         if (this.isWorkForRepository(var4, var3)) {
            return new File(var4, var2);
         }

         var1 = var1.getParentFile();
      } while(var1 != null);

      return null;
   }

   private boolean isWorkForRepository(File var1, String var2) {
      try {
         String var3 = this.eventManager.getClientServices().getRepositoryForDirectory(var1);
         String var4 = this.eventManager.getClientServices().getRepository();
         if (var3.startsWith(var4)) {
            var3 = var3.substring(var4.length() + 1);
         }

         return var2.equals(var3);
      } catch (IOException var5) {
         return false;
      }
   }
}

package org.netbeans.lib.cvsclient.command;

import java.io.File;
import java.io.IOException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class PipedFilesBuilder implements Builder, BinaryBuilder {
   private static final String ERR_START = "=======";
   private static final String ERR_CHECK = "Checking out ";
   private static final String ERR_RCS = "RCS:  ";
   private static final String ERR_VERS = "VERS: ";
   private static final String EXAM_DIR = ": Updating";
   private static final byte[] lineSeparator = System.getProperty("line.separator").getBytes();
   private PipedFileInformation fileInformation;
   private EventManager eventManager;
   private String fileDirectory;
   private BuildableCommand command;
   private TemporaryFileCreator tempFileCreator;

   public PipedFilesBuilder(EventManager var1, BuildableCommand var2, TemporaryFileCreator var3) {
      this.eventManager = var1;
      this.command = var2;
      this.tempFileCreator = var3;
   }

   public void outputDone() {
      if (this.fileInformation != null) {
         try {
            this.fileInformation.closeTempFile();
         } catch (IOException var2) {
         }

         this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.fileInformation));
         this.fileInformation = null;
      }
   }

   public void parseBytes(byte[] var1, int var2) {
      if (this.fileInformation == null) {
         try {
            this.fileInformation = new PipedFileInformation(File.createTempFile("checkout", (String)null));
         } catch (IOException var5) {
            var5.printStackTrace();
         }
      }

      try {
         this.fileInformation.addToTempFile(var1, var2);
      } catch (IOException var4) {
         this.outputDone();
      }

   }

   public void parseLine(String var1, boolean var2) {
      if (var2) {
         if (var1.indexOf(": Updating") >= 0) {
            this.fileDirectory = var1.substring(var1.indexOf(": Updating") + ": Updating".length()).trim();
         } else if (var1.startsWith("Checking out ")) {
            this.processFile(var1);
         } else {
            String var3;
            if (var1.startsWith("RCS:  ")) {
               if (this.fileInformation != null) {
                  var3 = var1.substring("RCS:  ".length()).trim();
                  this.fileInformation.setRepositoryFileName(var3);
               }
            } else if (var1.startsWith("VERS: ") && this.fileInformation != null) {
               var3 = var1.substring("RCS:  ".length()).trim();
               this.fileInformation.setRepositoryRevision(var3);
            }
         }
      } else {
         if (this.fileInformation == null) {
            try {
               this.fileInformation = new PipedFileInformation(File.createTempFile("checkout", (String)null));
            } catch (IOException var5) {
               var5.printStackTrace();
            }
         }

         if (this.fileInformation != null) {
            try {
               this.fileInformation.addToTempFile(var1.getBytes("ISO-8859-1"));
               this.fileInformation.addToTempFile(lineSeparator);
            } catch (IOException var4) {
               this.outputDone();
            }
         }
      }

   }

   private void processFile(String var1) {
      this.outputDone();
      String var2 = var1.substring("Checking out ".length());

      try {
         File var3 = this.tempFileCreator.createTempFile(var2);
         this.fileInformation = new PipedFileInformation(var3);
      } catch (IOException var4) {
         this.fileInformation = null;
         return;
      }

      this.fileInformation.setFile(this.createFile(var2));
   }

   private File createFile(String var1) {
      File var2 = new File(this.command.getLocalDirectory(), var1);
      return var2;
   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }
}

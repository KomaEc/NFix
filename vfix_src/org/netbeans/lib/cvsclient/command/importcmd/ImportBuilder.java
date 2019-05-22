package org.netbeans.lib.cvsclient.command.importcmd;

import java.io.File;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.DefaultFileInfoContainer;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class ImportBuilder implements Builder {
   private static final String NO_CONFLICTS = "No conflicts created by this import";
   private static final String FILE_INFOS = "NUCIL?";
   private final EventManager eventManager;
   private final String localPath;
   private final String module;
   private DefaultFileInfoContainer fileInfoContainer;

   public ImportBuilder(EventManager var1, ImportCommand var2) {
      this.eventManager = var1;
      this.localPath = var2.getLocalDirectory();
      this.module = var2.getModule();
   }

   public void outputDone() {
      if (this.fileInfoContainer != null) {
         FileInfoEvent var1 = new FileInfoEvent(this, this.fileInfoContainer);
         this.eventManager.fireCVSEvent(var1);
         this.fileInfoContainer = null;
      }
   }

   public void parseLine(String var1, boolean var2) {
      if (var1.length() > 2 && var1.charAt(1) == ' ') {
         String var3 = var1.substring(0, 1);
         if ("NUCIL?".indexOf(var3) >= 0) {
            String var4 = var1.substring(2).trim();
            this.processFile(var3, var4);
         } else {
            this.error(var1);
         }
      } else if (var1.startsWith("No conflicts created by this import")) {
         this.outputDone();
      }

   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }

   private void error(String var1) {
      System.err.println("Don't know anything about: " + var1);
   }

   private void processFile(String var1, String var2) {
      this.outputDone();
      var2 = var2.substring(this.module.length());
      File var3 = new File(this.localPath, var2);
      this.fileInfoContainer = new DefaultFileInfoContainer();
      this.fileInfoContainer.setType(var1);
      this.fileInfoContainer.setFile(var3);
      this.outputDone();
   }
}

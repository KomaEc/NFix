package org.netbeans.lib.cvsclient.command.export;

import java.io.File;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.DefaultFileInfoContainer;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class ExportBuilder implements Builder {
   private static final String FILE_INFOS = "MUARC?";
   private final EventManager eventManager;
   private final String localPath;
   private DefaultFileInfoContainer fileInfoContainer;

   public ExportBuilder(EventManager var1, ExportCommand var2) {
      this.eventManager = var1;
      this.localPath = var2.getLocalDirectory();
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
         if ("MUARC?".indexOf(var3) >= 0) {
            String var4 = var1.substring(2).trim();
            this.processFile(var3, var4);
         } else {
            this.error(var1);
         }
      }

   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }

   private void error(String var1) {
      System.err.println("Don't know anything about: " + var1);
   }

   private void processFile(String var1, String var2) {
      this.outputDone();
      File var3 = new File(this.localPath, var2);
      this.fileInfoContainer = new DefaultFileInfoContainer();
      this.fileInfoContainer.setType(var1);
      this.fileInfoContainer.setFile(var3);
      this.outputDone();
   }
}

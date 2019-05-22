package org.netbeans.lib.cvsclient.command.tag;

import java.io.File;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.DefaultFileInfoContainer;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class TagBuilder implements Builder {
   public static final String STATES = "T D ? ";
   public static final String CVS_SERVER = "server: ";
   public static final String EXAM_DIR = "server: ";
   private DefaultFileInfoContainer fileInfoContainer;
   private EventManager eventManager;
   private String localPath;

   public TagBuilder(EventManager var1, String var2) {
      this.eventManager = var1;
      this.localPath = var2;
   }

   public void outputDone() {
      if (this.fileInfoContainer != null) {
         this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.fileInfoContainer));
         this.fileInfoContainer = null;
      }

   }

   public void parseLine(String var1, boolean var2) {
      if (!var2) {
         if (var1.indexOf("server: ") < 0) {
            if (var1.length() < 3) {
               return;
            }

            String var3 = var1.substring(0, 2);
            if ("T D ? ".indexOf(var3) >= 0) {
               this.processFile(var1);
            }
         }

      }
   }

   private void processFile(String var1) {
      if (this.fileInfoContainer == null) {
         this.fileInfoContainer = new DefaultFileInfoContainer();
      }

      this.fileInfoContainer.setType(var1.substring(0, 1));
      String var2 = var1.substring(2).trim();
      if (var2.startsWith("no file")) {
         var2 = var2.substring(8);
      }

      this.fileInfoContainer.setFile(this.createFile(var2));
      this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.fileInfoContainer));
      this.fileInfoContainer = null;
   }

   private File createFile(String var1) {
      return new File(this.localPath, var1);
   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }
}

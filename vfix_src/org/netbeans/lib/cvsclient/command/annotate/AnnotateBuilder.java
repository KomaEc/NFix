package org.netbeans.lib.cvsclient.command.annotate;

import java.io.File;
import java.io.IOException;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class AnnotateBuilder implements Builder {
   private static final String UNKNOWN = ": nothing known about";
   private static final String ANNOTATING = "Annotations for ";
   private static final String STARS = "***************";
   private AnnotateInformation annotateInformation;
   private final EventManager eventManager;
   private final String localPath;
   private String relativeDirectory;
   private int lineNum;
   private File tempDir;

   public AnnotateBuilder(EventManager var1, BasicCommand var2) {
      this.eventManager = var1;
      this.localPath = var2.getLocalDirectory();
      this.tempDir = var2.getGlobalOptions().getTempDir();
   }

   public void outputDone() {
      if (this.annotateInformation != null) {
         try {
            this.annotateInformation.closeTempFile();
         } catch (IOException var2) {
         }

         this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.annotateInformation));
         this.annotateInformation = null;
      }
   }

   public void parseLine(String var1, boolean var2) {
      if (var2 && var1.startsWith("Annotations for ")) {
         this.outputDone();
         this.annotateInformation = new AnnotateInformation(this.tempDir);
         this.annotateInformation.setFile(this.createFile(var1.substring("Annotations for ".length())));
         this.lineNum = 0;
      } else if (!var2 || !var1.startsWith("***************")) {
         if (!var2) {
            this.processLines(var1);
         }

      }
   }

   private File createFile(String var1) {
      return new File(this.localPath, var1);
   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }

   private void processLines(String var1) {
      if (this.annotateInformation != null) {
         try {
            this.annotateInformation.addToTempFile(var1);
         } catch (IOException var3) {
         }
      }

   }

   public static AnnotateLine processLine(String var0) {
      int var1 = var0.indexOf(40);
      int var2 = var0.indexOf(41);
      AnnotateLine var3 = null;
      if (var1 > 0 && var2 > var1) {
         String var4 = var0.substring(0, var1).trim();
         String var5 = var0.substring(var1 + 1, var2);
         String var6 = var0.substring(var2 + 3);
         int var7 = var5.lastIndexOf(32);
         String var8 = var5;
         String var9 = var5;
         if (var7 > 0) {
            var8 = var5.substring(0, var7).trim();
            var9 = var5.substring(var7).trim();
         }

         var3 = new AnnotateLine();
         var3.setContent(var6);
         var3.setAuthor(var8);
         var3.setDateString(var9);
         var3.setRevision(var4);
      }

      return var3;
   }
}

package org.netbeans.lib.cvsclient.command.remove;

import java.io.File;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class RemoveBuilder implements Builder {
   private static final String UNKNOWN = ": nothing known about";
   private static final String WARNING = ": warning: ";
   private static final String SCHEDULING = ": scheduling `";
   private static final String USE_COMMIT = ": use 'cvs commit' ";
   private static final String DIRECTORY = ": Removing ";
   private static final String STILL_IN_WORKING = ": file `";
   private static final String REMOVE_FIRST = "first";
   private static final String UNKNOWN_FILE = "?";
   private RemoveInformation removeInformation;
   private String fileDirectory;
   private final EventManager eventManager;
   private final RemoveCommand removeCommand;

   public RemoveBuilder(EventManager var1, RemoveCommand var2) {
      this.eventManager = var1;
      this.removeCommand = var2;
   }

   public void outputDone() {
      if (this.removeInformation != null) {
         this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.removeInformation));
         this.removeInformation = null;
      }

   }

   public void parseLine(String var1, boolean var2) {
      int var3;
      String var4;
      if (var1.indexOf(": scheduling `") >= 0) {
         var3 = var1.indexOf(39);
         var4 = var1.substring(var1.indexOf(": scheduling `") + ": scheduling `".length(), var3).trim();
         this.addFile(var4);
         this.removeInformation.setRemoved(true);
         this.outputDone();
      }

      if (var1.startsWith("?")) {
         this.addFile(var1.substring("?".length()));
         this.removeInformation.setRemoved(false);
         this.outputDone();
      }

      if (var1.indexOf(": file `") >= 0) {
         var3 = var1.indexOf(39);
         var4 = var1.substring(var1.indexOf(": file `") + ": file `".length(), var3).trim();
         this.addFile(var4);
         this.removeInformation.setRemoved(false);
         this.outputDone();
      }

   }

   protected File createFile(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(this.removeCommand.getLocalDirectory());
      var2.append(File.separator);
      if (this.fileDirectory == null) {
         File var3 = this.removeCommand.getFileEndingWith(var1);
         if (var3 == null) {
            var2.append(var1);
         } else {
            var2 = new StringBuffer(var3.getAbsolutePath());
         }
      } else {
         var2.append(var1);
      }

      String var4 = var2.toString();
      var4 = var4.replace('/', File.separatorChar);
      return new File(var2.toString());
   }

   protected void addFile(String var1) {
      this.removeInformation = new RemoveInformation();
      this.removeInformation.setFile(this.createFile(var1));
   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }
}

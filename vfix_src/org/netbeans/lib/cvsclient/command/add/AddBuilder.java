package org.netbeans.lib.cvsclient.command.add;

import java.io.File;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class AddBuilder implements Builder {
   private static final String UNKNOWN = ": nothing known about";
   private static final String ADDED = " added to the repository";
   private static final String WARNING = ": warning: ";
   private static final String ALREADY_ENTERED = " has already been entered";
   private static final String SCHEDULING = ": scheduling file `";
   private static final String USE_COMMIT = ": use 'cvs commit' ";
   private static final String DIRECTORY = "Directory ";
   private static final String READDING = ": re-adding file ";
   private static final String RESURRECTED = ", resurrected";
   private static final String RESUR_VERSION = ", version ";
   private static final boolean DEBUG = false;
   private AddInformation addInformation;
   private EventManager eventManager;
   private String fileDirectory;
   private AddCommand addCommand;
   private boolean readingTags;

   public AddBuilder(EventManager var1, AddCommand var2) {
      this.eventManager = var1;
      this.addCommand = var2;
   }

   public void outputDone() {
      if (this.addInformation != null) {
         FileInfoEvent var1 = new FileInfoEvent(this, this.addInformation);
         this.eventManager.fireCVSEvent(var1);
         this.addInformation = null;
      }

   }

   public void parseLine(String var1, boolean var2) {
      String var3;
      if (var1.endsWith(" added to the repository")) {
         var3 = var1.substring("Directory ".length(), var1.indexOf(" added to the repository"));
         this.addDirectory(var3);
      } else if (var1.indexOf(": scheduling file `") >= 0) {
         var3 = var1.substring(var1.indexOf(": scheduling file `") + ": scheduling file `".length(), var1.indexOf(39)).trim();
         this.addFile(var3);
      } else if (var1.indexOf(": re-adding file ") >= 0) {
         var3 = var1.substring(var1.indexOf(": re-adding file ") + ": re-adding file ".length(), var1.indexOf(40)).trim();
         this.addFile(var3);
      } else if (var1.endsWith(", resurrected")) {
         var3 = var1.substring(0, var1.length() - ", resurrected".length());
         this.resurrectFile(var3);
      }

   }

   private File createFile(String var1) {
      File var2 = this.addCommand.getFileEndingWith(var1);
      if (var2 != null) {
         return var2;
      } else {
         String var3 = var1.replace('\\', '/');
         File[] var4 = this.addCommand.getFiles();
         int var5 = var3.length();
         File var6 = null;
         String[] var7 = new String[var4.length];

         int var8;
         for(var8 = 0; var8 < var4.length; ++var8) {
            var7[var8] = var4[var8].getAbsolutePath().replace('\\', '/');
         }

         var8 = var3.lastIndexOf(47);
         String var9 = null;
         if (var8 < 0) {
            var9 = var3;
         } else {
            var9 = var3.substring(var8 + 1);
         }

         while(var8 >= 0 || var9 != null) {
            boolean var10 = false;

            for(int var11 = 0; var11 < var7.length; ++var11) {
               if (var7[var11].endsWith(var9)) {
                  var6 = var4[var11];
                  var10 = true;
               }
            }

            var8 = var3.lastIndexOf(47, var8 - 1);
            if (var8 < 0 || !var10) {
               break;
            }

            var9 = var3.substring(var8 + 1);
         }

         return var6;
      }
   }

   private void addDirectory(String var1) {
      this.addInformation = new AddInformation();
      this.addInformation.setType("A");
      String var2 = var1.replace('\\', '/');
      this.addInformation.setFile(this.createFile(var2));
      this.outputDone();
   }

   private void addFile(String var1) {
      this.addInformation = new AddInformation();
      this.addInformation.setFile(this.createFile(var1));
      this.addInformation.setType("A");
      this.outputDone();
   }

   private void resurrectFile(String var1) {
      int var2 = var1.lastIndexOf(", version ");
      String var3 = var1.substring(var2 + ", version ".length()).trim();
      String var4 = var1.substring(0, var2).trim();
      int var5 = var4.lastIndexOf(32);
      String var6 = var4.substring(var5).trim();
      this.addInformation = new AddInformation();
      this.addInformation.setType("U");
      this.addInformation.setFile(this.createFile(var6));
      this.outputDone();
   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }
}

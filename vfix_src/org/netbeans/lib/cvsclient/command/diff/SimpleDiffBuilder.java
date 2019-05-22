package org.netbeans.lib.cvsclient.command.diff;

import java.io.File;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class SimpleDiffBuilder implements Builder {
   protected EventManager eventManager;
   protected DiffCommand diffCommand;
   protected DiffInformation diffInformation;
   protected String fileDirectory;
   protected boolean readingDiffs = false;
   private static final String UNKNOWN = ": I know nothing about";
   private static final String CANNOT_FIND = ": cannot find";
   private static final String UNKNOWN_TAG = ": tag";
   private static final String EXAM_DIR = ": Diffing";
   private static final String FILE = "Index: ";
   private static final String RCS_FILE = "RCS file: ";
   private static final String REVISION = "retrieving revision ";
   private static final String PARAMETERS = "diff ";
   private DiffInformation.DiffChange currentChange;

   public SimpleDiffBuilder(EventManager var1, DiffCommand var2) {
      this.eventManager = var1;
      this.diffCommand = var2;
   }

   public void outputDone() {
      if (this.diffInformation != null) {
         if (this.currentChange != null) {
            this.diffInformation.addChange(this.currentChange);
            this.currentChange = null;
         }

         this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.diffInformation));
         this.diffInformation = null;
         this.readingDiffs = false;
      }

   }

   public void parseLine(String var1, boolean var2) {
      if (this.readingDiffs) {
         if (!var1.startsWith("Index: ")) {
            this.processDifferences(var1);
            return;
         }

         this.outputDone();
      }

      if (var1.indexOf(": I know nothing about") >= 0) {
         this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.diffInformation));
         this.diffInformation = null;
      } else if (var1.indexOf(": Diffing") >= 0) {
         this.fileDirectory = var1.substring(var1.indexOf(": Diffing") + ": Diffing".length()).trim();
      } else if (var1.startsWith("Index: ")) {
         this.processFile(var1.substring("Index: ".length()));
      } else if (var1.startsWith("RCS file: ")) {
         this.processRCSfile(var1.substring("RCS file: ".length()));
      } else if (var1.startsWith("retrieving revision ")) {
         this.processRevision(var1.substring("retrieving revision ".length()));
      } else if (var1.startsWith("diff ")) {
         this.processParameters(var1.substring("diff ".length()));
         this.readingDiffs = true;
      }
   }

   protected void processFile(String var1) {
      this.outputDone();
      this.diffInformation = this.createDiffInformation();
      String var2 = var1.trim();
      if (var2.startsWith("no file")) {
         var2 = var2.substring(8);
      }

      this.diffInformation.setFile(new File(this.diffCommand.getLocalDirectory(), var2));
   }

   protected void processRCSfile(String var1) {
      if (this.diffInformation != null) {
         this.diffInformation.setRepositoryFileName(var1.trim());
      }
   }

   protected void processRevision(String var1) {
      if (this.diffInformation != null) {
         var1 = var1.trim();
         if (this.diffInformation.getLeftRevision() != null) {
            this.diffInformation.setRightRevision(var1);
         } else {
            this.diffInformation.setLeftRevision(var1);
         }

      }
   }

   protected void processParameters(String var1) {
      if (this.diffInformation != null) {
         this.diffInformation.setParameters(var1.trim());
      }
   }

   public DiffInformation createDiffInformation() {
      return new DiffInformation();
   }

   protected void assignType(DiffInformation.DiffChange var1, String var2) {
      int var3 = 0;
      int var4 = var2.indexOf(99);
      if (var4 > 0) {
         var1.setType(2);
         var3 = var4;
      } else {
         int var5 = var2.indexOf(97);
         if (var5 > 0) {
            var1.setType(0);
            var3 = var5;
         } else {
            int var6 = var2.indexOf(100);
            if (var6 > 0) {
               var1.setType(1);
               var3 = var6;
            }
         }
      }

      String var7 = var2.substring(0, var3);
      var1.setLeftRange(this.getMin(var7), this.getMax(var7));
      String var8 = var2.substring(var3 + 1);
      var1.setRightRange(this.getMin(var8), this.getMax(var8));
   }

   private int getMin(String var1) {
      String var2 = var1;
      int var3 = var1.indexOf(44);
      if (var3 > 0) {
         var2 = var1.substring(0, var3);
      }

      int var4;
      try {
         var4 = Integer.parseInt(var2);
      } catch (NumberFormatException var6) {
         var4 = 0;
      }

      return var4;
   }

   private int getMax(String var1) {
      String var2 = var1;
      int var3 = var1.indexOf(44);
      if (var3 > 0) {
         var2 = var1.substring(var3 + 1);
      }

      int var4;
      try {
         var4 = Integer.parseInt(var2);
      } catch (NumberFormatException var6) {
         var4 = 0;
      }

      return var4;
   }

   protected void processDifferences(String var1) {
      char var2 = var1.charAt(0);
      if (var2 >= '0' && var2 <= '9') {
         if (this.currentChange != null) {
            this.diffInformation.addChange(this.currentChange);
         }

         this.currentChange = this.diffInformation.createDiffChange();
         this.assignType(this.currentChange, var1);
      }

      if (var2 == '<') {
         this.currentChange.appendLeftLine(var1.substring(2));
      }

      if (var2 == '>') {
         this.currentChange.appendRightLine(var1.substring(2));
      }

   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }
}

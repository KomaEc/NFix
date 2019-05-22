package org.netbeans.lib.cvsclient.command.diff;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.netbeans.lib.cvsclient.command.FileInfoContainer;

public class DiffInformation extends FileInfoContainer {
   private File file;
   private String repositoryFileName;
   private String rightRevision;
   private String leftRevision;
   private String parameters;
   private final List changesList = new ArrayList();
   private Iterator iterator;

   public File getFile() {
      return this.file;
   }

   public void setFile(File var1) {
      this.file = var1;
   }

   public String getRepositoryFileName() {
      return this.repositoryFileName;
   }

   public void setRepositoryFileName(String var1) {
      this.repositoryFileName = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(30);
      var1.append("\nFile: " + (this.file != null ? this.file.getAbsolutePath() : "null"));
      var1.append("\nRCS file: " + this.repositoryFileName);
      var1.append("\nRevision: " + this.leftRevision);
      if (this.rightRevision != null) {
         var1.append("\nRevision: " + this.rightRevision);
      }

      var1.append("\nParameters: " + this.parameters);
      return var1.toString();
   }

   public String getRightRevision() {
      return this.rightRevision;
   }

   public void setRightRevision(String var1) {
      this.rightRevision = var1;
   }

   public String getLeftRevision() {
      return this.leftRevision;
   }

   public void setLeftRevision(String var1) {
      this.leftRevision = var1;
   }

   public String getParameters() {
      return this.parameters;
   }

   public void setParameters(String var1) {
      this.parameters = var1;
   }

   public DiffInformation.DiffChange createDiffChange() {
      return new DiffInformation.DiffChange();
   }

   public void addChange(DiffInformation.DiffChange var1) {
      this.changesList.add(var1);
   }

   public DiffInformation.DiffChange getFirstChange() {
      this.iterator = this.changesList.iterator();
      return this.getNextChange();
   }

   public DiffInformation.DiffChange getNextChange() {
      if (this.iterator == null) {
         return null;
      } else {
         return !this.iterator.hasNext() ? null : (DiffInformation.DiffChange)this.iterator.next();
      }
   }

   public class DiffChange {
      public static final int ADD = 0;
      public static final int DELETE = 1;
      public static final int CHANGE = 2;
      protected int type;
      private int leftBeginning = -1;
      private int leftEnd = -1;
      private final List leftDiff = new ArrayList();
      private int rightBeginning = -1;
      private int rightEnd = -1;
      private final List rightDiff = new ArrayList();

      public void setType(int var1) {
         this.type = var1;
      }

      public int getType() {
         return this.type;
      }

      public void setLeftRange(int var1, int var2) {
         this.leftBeginning = var1;
         this.leftEnd = var2;
      }

      public void setRightRange(int var1, int var2) {
         this.rightBeginning = var1;
         this.rightEnd = var2;
      }

      public int getMainBeginning() {
         return this.rightBeginning;
      }

      public int getRightMin() {
         return this.rightBeginning;
      }

      public int getRightMax() {
         return this.rightEnd;
      }

      public int getLeftMin() {
         return this.leftBeginning;
      }

      public int getLeftMax() {
         return this.leftEnd;
      }

      public boolean isInRange(int var1, boolean var2) {
         if (var2) {
            return var1 >= this.leftBeginning && var1 <= this.leftEnd;
         } else {
            return var1 >= this.rightBeginning && var1 <= this.rightEnd;
         }
      }

      public String getLine(int var1, boolean var2) {
         int var3;
         String var4;
         if (var2) {
            var3 = var1 - this.leftBeginning;
            if (var3 >= 0 && var3 < this.leftDiff.size()) {
               var4 = (String)this.leftDiff.get(var3);
               return var4;
            } else {
               return null;
            }
         } else {
            var3 = var1 - this.rightBeginning;
            if (var3 >= 0 && var3 < this.rightDiff.size()) {
               var4 = (String)this.rightDiff.get(var3);
               return var4;
            } else {
               return null;
            }
         }
      }

      public void appendLeftLine(String var1) {
         this.leftDiff.add(var1);
      }

      public void appendRightLine(String var1) {
         this.rightDiff.add(var1);
      }
   }
}

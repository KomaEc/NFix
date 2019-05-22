package org.netbeans.lib.cvsclient.command;

import java.io.File;

public class DefaultFileInfoContainer extends FileInfoContainer {
   public static final String PERTINENT_STATE = "Y";
   public static final String MERGED_FILE = "G";
   private File file;
   private String type;

   public File getFile() {
      return this.file;
   }

   public boolean isDirectory() {
      File var1 = this.getFile();
      return var1 == null ? false : var1.isDirectory();
   }

   public void setFile(File var1) {
      this.file = var1;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.type);
      var1.append("  ");
      if (this.isDirectory()) {
         var1.append("Directory ");
      }

      var1.append(this.file != null ? this.file.getAbsolutePath() : "null");
      return var1.toString();
   }
}

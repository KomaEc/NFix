package org.netbeans.lib.cvsclient.command.remove;

import java.io.File;
import org.netbeans.lib.cvsclient.command.FileInfoContainer;

public class RemoveInformation extends FileInfoContainer {
   private File file;
   private boolean removed;

   public File getFile() {
      return this.file;
   }

   public void setFile(File var1) {
      this.file = var1;
   }

   public void setRemoved(boolean var1) {
      this.removed = var1;
   }

   public boolean isRemoved() {
      return this.removed;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(30);
      var1.append("  ");
      var1.append(this.file != null ? this.file.getAbsolutePath() : "null");
      return var1.toString();
   }
}

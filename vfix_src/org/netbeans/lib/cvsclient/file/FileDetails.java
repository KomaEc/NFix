package org.netbeans.lib.cvsclient.file;

import java.io.File;

public class FileDetails {
   private File file;
   private boolean isBinary;

   public FileDetails(File var1, boolean var2) {
      this.file = var1;
      this.isBinary = var2;
   }

   public File getFile() {
      return this.file;
   }

   public boolean isBinary() {
      return this.isBinary;
   }
}

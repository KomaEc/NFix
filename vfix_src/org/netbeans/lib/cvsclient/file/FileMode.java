package org.netbeans.lib.cvsclient.file;

import java.io.File;

public class FileMode {
   private File file;

   public FileMode(File var1) {
      this.file = var1;
   }

   public String toString() {
      return "u=rw,g=r,o=r";
   }
}

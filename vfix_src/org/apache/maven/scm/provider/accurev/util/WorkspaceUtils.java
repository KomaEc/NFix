package org.apache.maven.scm.provider.accurev.util;

import java.io.File;
import java.io.IOException;

public class WorkspaceUtils {
   public static boolean isSameFile(File file1, String filename2) {
      return isSameFile(file1, filename2 == null ? null : new File(filename2));
   }

   public static boolean isSameFile(File file1, File file2) {
      if (file1 != file2 && (file1 != null || file2 != null)) {
         if (file1 != null && file2 != null) {
            try {
               file1 = file1.getCanonicalFile();
            } catch (IOException var4) {
            }

            try {
               file2 = file2.getCanonicalFile();
            } catch (IOException var3) {
            }

            return file1.equals(file2);
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   private WorkspaceUtils() {
   }
}

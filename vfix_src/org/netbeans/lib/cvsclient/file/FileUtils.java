package org.netbeans.lib.cvsclient.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
   private static FileReadOnlyHandler fileReadOnlyHandler;

   public static FileReadOnlyHandler getFileReadOnlyHandler() {
      return fileReadOnlyHandler;
   }

   public static void setFileReadOnlyHandler(FileReadOnlyHandler var0) {
      fileReadOnlyHandler = var0;
   }

   public static void setFileReadOnly(File var0, boolean var1) throws IOException {
      if (getFileReadOnlyHandler() != null) {
         getFileReadOnlyHandler().setFileReadOnly(var0, var1);
      }
   }

   public static void copyFile(File var0, File var1) throws IOException {
      if (var0 != null && var1 != null) {
         File var2 = var1.getParentFile();
         if (!var2.exists() && !var2.mkdirs()) {
            throw new IOException("Could not create directory '" + var2 + "'");
         } else {
            BufferedInputStream var3 = null;
            BufferedOutputStream var4 = null;

            try {
               var3 = new BufferedInputStream(new FileInputStream(var0));
               var4 = new BufferedOutputStream(new FileOutputStream(var1));

               try {
                  byte[] var5 = new byte['耀'];

                  for(int var6 = var3.read(var5); var6 > 0; var6 = var3.read(var5)) {
                     var4.write(var5, 0, var6);
                  }
               } catch (IOException var18) {
                  var1.delete();
                  throw var18;
               }
            } finally {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (IOException var17) {
                  }
               }

               if (var4 != null) {
                  try {
                     var4.close();
                  } catch (IOException var16) {
                  }
               }

            }

         }
      } else {
         throw new NullPointerException("sourceFile and targetFile must not be null");
      }
   }

   public static void renameFile(File var0, File var1) throws IOException {
      boolean var2 = var1.exists();
      int var3;
      if (var2) {
         for(var3 = 0; var3 < 3; ++var3) {
            if (var1.delete()) {
               var2 = false;
               break;
            }

            try {
               Thread.sleep(71L);
            } catch (InterruptedException var7) {
            }
         }
      }

      if (!var2) {
         for(var3 = 0; var3 < 3; ++var3) {
            if (var0.renameTo(var1)) {
               return;
            }

            try {
               Thread.sleep(71L);
            } catch (InterruptedException var6) {
            }
         }
      }

      copyFile(var0, var1);

      for(var3 = 0; var3 < 3; ++var3) {
         if (var0.delete()) {
            return;
         }

         try {
            Thread.sleep(71L);
         } catch (InterruptedException var5) {
         }
      }

      throw new IOException("Can not delete: " + var0.getAbsolutePath());
   }

   private FileUtils() {
   }
}

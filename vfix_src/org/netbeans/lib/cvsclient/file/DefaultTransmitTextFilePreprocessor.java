package org.netbeans.lib.cvsclient.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DefaultTransmitTextFilePreprocessor implements TransmitTextFilePreprocessor {
   private static final int CHUNK_SIZE = 32768;
   private File tempDir;

   public void setTempDir(File var1) {
      this.tempDir = var1;
   }

   public File getPreprocessedTextFile(File var1) throws IOException {
      File var2 = File.createTempFile("cvs", (String)null, this.tempDir);
      byte[] var3 = System.getProperty("line.separator").getBytes();
      boolean var4 = var3.length != 1 || var3[0] != 10;
      BufferedOutputStream var5 = null;
      BufferedInputStream var6 = null;

      File var26;
      try {
         var6 = new BufferedInputStream(new FileInputStream(var1));
         var5 = new BufferedOutputStream(new FileOutputStream(var2));
         byte[] var7 = new byte['耀'];
         byte[] var8 = new byte['耀'];

         for(int var9 = var6.read(var7); var9 > 0; var9 = var6.read(var7)) {
            if (!var4) {
               var5.write(var7, 0, var9);
            } else {
               int var10 = 0;
               int var11 = 0;

               while(true) {
                  while(var11 < var9) {
                     int var12 = findIndexOf(var7, var3, var11);
                     if (var12 >= var11 && var12 < var9) {
                        System.arraycopy(var7, var11, var8, var10, var12 - var11);
                        var10 += var12 - var11;
                        var11 = var12 + var3.length;
                        var8[var10++] = 10;
                     } else {
                        System.arraycopy(var7, var11, var8, var10, var9 - var11);
                        var10 += var9 - var11;
                        var11 = var9;
                     }
                  }

                  var5.write(var8, 0, var10);
                  break;
               }
            }
         }

         var26 = var2;
      } catch (IOException var24) {
         if (var2 != null) {
            this.cleanup(var2);
         }

         throw var24;
      } finally {
         if (var6 != null) {
            try {
               var6.close();
            } catch (IOException var23) {
            }
         }

         if (var5 != null) {
            try {
               var5.close();
            } catch (IOException var22) {
            }
         }

      }

      return var26;
   }

   private static int findIndexOf(byte[] var0, byte[] var1, int var2) {
      int var3 = 0;

      for(int var4 = var2; var4 < var0.length; ++var4) {
         if (var0[var4] == var1[var3]) {
            ++var3;
            if (var3 == var1.length) {
               return var4 - var3 + 1;
            }
         } else {
            var3 = 0;
         }
      }

      return -1;
   }

   public void cleanup(File var1) {
      if (var1 != null) {
         var1.delete();
      }

   }
}

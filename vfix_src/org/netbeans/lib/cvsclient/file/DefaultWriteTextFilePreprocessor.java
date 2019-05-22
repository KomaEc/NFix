package org.netbeans.lib.cvsclient.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DefaultWriteTextFilePreprocessor implements WriteTextFilePreprocessor {
   private static final int CHUNK_SIZE = 32768;

   public void copyTextFileToLocation(InputStream var1, File var2, OutputStreamProvider var3) throws IOException {
      BufferedInputStream var4 = null;
      BufferedOutputStream var5 = null;
      byte[] var6 = System.getProperty("line.separator").getBytes();

      try {
         var4 = new BufferedInputStream(var1);
         var5 = new BufferedOutputStream(var3.createOutputStream());
         byte[] var7 = new byte['耀'];

         for(int var8 = var4.read(var7); var8 > 0; var8 = var4.read(var7)) {
            for(int var9 = 0; var9 < var8; ++var9) {
               if (var7[var9] == 10) {
                  var5.write(var6);
               } else {
                  var5.write(var7[var9]);
               }
            }
         }
      } finally {
         if (var4 != null) {
            try {
               var4.close();
            } catch (IOException var19) {
            }
         }

         if (var5 != null) {
            try {
               var5.close();
            } catch (IOException var18) {
            }
         }

      }

   }
}

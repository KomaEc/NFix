package com.gzoltar.nnative.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class FileUtils {
   public static void copyCompletely(InputStream var0, OutputStream var1) throws IOException {
      if (var1 instanceof FileOutputStream && var0 instanceof FileInputStream) {
         FileChannel var4 = ((FileOutputStream)var1).getChannel();
         FileChannel var5;
         (var5 = ((FileInputStream)var0).getChannel()).transferTo(0L, 2147483647L, var4);
         var5.close();
         var4.close();
      } else {
         byte[] var2 = new byte[8192];

         int var3;
         while((var3 = var0.read(var2)) >= 0) {
            var1.write(var2, 0, var3);
         }

         var0.close();
         var1.close();
      }
   }
}

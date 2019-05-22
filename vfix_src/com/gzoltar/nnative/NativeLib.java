package com.gzoltar.nnative;

import com.gzoltar.nnative.utils.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NativeLib {
   public static String copyNativeLib(File var0, String var1) throws IOException {
      String var2 = System.getProperty("os.name").toLowerCase();
      String var3 = System.getProperty("os.arch");
      var3 = "native" + (var3.contains("64") ? "/x86_64" : "/x86");
      if (var2.contains("linux")) {
         var3 = var3 + "/linux/";
      } else if (var2.contains("mac")) {
         var3 = var3 + "/macosx/";
      } else if (var2.contains("windows")) {
         var3 = var3 + "/windows/";
      }

      (var0 = new File(var0 + System.getProperty("file.separator") + var1)).deleteOnExit();
      FileOutputStream var4 = new FileOutputStream(var0);
      FileUtils.copyCompletely(NativeLib.class.getClassLoader().getResourceAsStream(var3 + var1), var4);
      var4.close();
      return var0.getAbsolutePath();
   }
}

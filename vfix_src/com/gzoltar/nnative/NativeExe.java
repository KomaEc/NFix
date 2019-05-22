package com.gzoltar.nnative;

import com.gzoltar.nnative.utils.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class NativeExe {
   private String execPath;
   private String outputFile;

   protected NativeExe(String var1) throws IOException {
      String var2 = System.getProperty("os.name").toLowerCase();
      String var3 = System.getProperty("os.arch");
      String var4 = "";
      String var5 = var1;
      if (var2.contains("linux")) {
         if (var3.contains("64")) {
            var4 = "native/x86_64/linux/";
            var5 = var1 + ".linux.x86_64";
         } else {
            var4 = "native/x86/linux/";
            var5 = var1 + ".linux.x86";
         }
      } else if (var2.contains("mac")) {
         var4 = "native/x86_64/macosx/";
         var5 = var1 + ".macosx.x86_64";
      } else {
         var2.contains("windows");
      }

      File var8 = null;
      if (!var2.contains("linux") && !var2.contains("mac")) {
         var2.contains("windows");
      } else {
         var8 = File.createTempFile(var5, "");
      }

      var8.setExecutable(true);
      var8.deleteOnExit();
      FileOutputStream var7 = new FileOutputStream(var8);
      FileUtils.copyCompletely(this.getClass().getClassLoader().getResourceAsStream(var4 + var5), var7);
      var7.close();
      this.execPath = var8.getAbsolutePath();
      File var6;
      (var6 = File.createTempFile(var1, ".output")).deleteOnExit();
      this.outputFile = var6.getAbsolutePath();
   }

   public String getExecPath() {
      return this.execPath;
   }

   public String getOutputFile() {
      return this.outputFile;
   }
}

package com.gzoltar.nnative;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TrieLib {
   private String librariesDir = new String();

   public TrieLib() throws IOException {
      String var1 = System.getProperty("os.name").toLowerCase();
      File var2;
      (var2 = Files.createTempDirectory("TrieLibs").toFile()).deleteOnExit();
      this.librariesDir = var2.getAbsolutePath();
      if (var1.contains("linux")) {
         NativeLib.copyNativeLib(var2, "libTrieJNI.so");
      } else if (var1.contains("mac")) {
         NativeLib.copyNativeLib(var2, "libTrieJNI.jnilib");
      } else {
         if (var1.contains("windows")) {
            NativeLib.copyNativeLib(var2, "TrieJNI.dll");
            if (System.getProperty("os.arch").contains("64")) {
               NativeLib.copyNativeLib(var2, "msvcr110d.dll");
               return;
            }

            NativeLib.copyNativeLib(var2, "msvcr100.dll");
         }

      }
   }

   public String getLibrariesDir() {
      return this.librariesDir;
   }
}

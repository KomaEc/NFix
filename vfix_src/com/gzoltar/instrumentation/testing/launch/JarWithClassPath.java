package com.gzoltar.instrumentation.testing.launch;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.utils.SystemProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;

public class JarWithClassPath {
   private static final String JAR_PREFIX = "GZoltarJarWithClassPath";
   private static String jarPath = null;

   public static String extractClassPathFromJar(String var0) {
      File var17 = new File(var0);

      try {
         JarInputStream var18 = new JarInputStream(new FileInputStream(var17));
         Throwable var1 = null;
         boolean var12 = false;

         String var2;
         try {
            var12 = true;
            var2 = var18.getManifest().getMainAttributes().getValue(Name.CLASS_PATH);
            ArrayList var3 = new ArrayList();
            String[] var19;
            int var4 = (var19 = var2.split(" ")).length;
            int var5 = 0;

            while(true) {
               if (var5 >= var4) {
                  var2 = String.join(SystemProperties.PATH_SEPARATOR, var3);
                  var12 = false;
                  break;
               }

               String var6 = var19[var5];
               File var7;
               if (!(var7 = new File(var6.replace("%20", " "))).exists()) {
                  throw new IllegalStateException("There is not any '" + var6 + "'");
               }

               var3.add(var7.getAbsolutePath());
               ++var5;
            }
         } catch (Throwable var14) {
            var1 = var14;
            throw var14;
         } finally {
            if (var12) {
               if (var1 != null) {
                  try {
                     var18.close();
                  } catch (Throwable var13) {
                     var1.addSuppressed(var13);
                  }
               } else {
                  var18.close();
               }

            }
         }

         var18.close();
         return var2;
      } catch (IOException var16) {
         Logger.getInstance().err(var16.getMessage(), var16);
         return null;
      }
   }

   public static String createJarWithClassPath(String var0) {
      if (jarPath != null && (new File(jarPath)).exists()) {
         Logger.getInstance().debug("Reusing an existing jar file '" + jarPath + "'");
         return jarPath;
      } else {
         Logger.getInstance().debug("Creating a jar file for classpath '" + var0 + "'");
         StringBuffer var1 = new StringBuffer();
         String[] var6;
         int var2 = (var6 = var0.split(SystemProperties.PATH_SEPARATOR)).length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = var6[var3];
            if (!(var4 = (new File(var4)).getAbsolutePath()).toLowerCase().contains("evosuite") || !var4.toLowerCase().endsWith(".jar")) {
               var4.replace("\\", "/");
               var4.replace(" ", "%20");
               if (!var4.startsWith("/")) {
                  var4 = "/" + var4;
               }

               if (!var4.toLowerCase().endsWith(".jar") && !var4.endsWith("/")) {
                  var4 = var4 + "/";
               }

               var1.append(var4 + " ");
            }
         }

         try {
            File var7;
            (var7 = File.createTempFile("GZoltarJarWithClassPath", ".jar")).deleteOnExit();
            var0 = var7.getAbsolutePath();
            Manifest var8;
            (var8 = new Manifest()).getMainAttributes().put(Name.MANIFEST_VERSION, "1.0");
            var8.getMainAttributes().put(Name.CLASS_PATH, var1.toString());
            JarOutputStream var9;
            (var9 = new JarOutputStream(new FileOutputStream(var7), var8)).flush();
            var9.close();
            Logger.getInstance().debug("Created jar path at " + var0 + " with classpath: " + var1.toString());
         } catch (Exception var5) {
            Logger.getInstance().err(var5.getMessage(), var5);
            return null;
         }

         jarPath = var0;
         return var0;
      }
   }

   public static void resetJarPath() {
      jarPath = null;
   }
}

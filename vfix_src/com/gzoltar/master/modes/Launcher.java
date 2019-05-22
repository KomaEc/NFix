package com.gzoltar.master.modes;

import com.gzoltar.client.Properties;
import com.gzoltar.client.rmi.Message;
import com.gzoltar.client.rmi.Response;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.testing.launch.JarWithClassPath;
import com.gzoltar.instrumentation.testing.launch.RegistrySingleton;
import com.gzoltar.instrumentation.utils.SystemProperties;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.UUID;

public class Launcher {
   public static String prepareClassPath() {
      StringBuilder var0 = new StringBuilder("");
      if (Properties.PROJECT_CP != null) {
         var0.append(Properties.PROJECT_CP);
         var0.append(SystemProperties.PATH_SEPARATOR);
      } else {
         String[] var1;
         int var2;
         int var3;
         String var4;
         if (Properties.CLASSESDIR != null) {
            var2 = (var1 = Properties.CLASSESDIR.split(":")).length;

            for(var3 = 0; var3 < var2; ++var3) {
               var4 = var1[var3];
               var0.append(var4);
               var0.append(SystemProperties.PATH_SEPARATOR);
            }
         }

         if (Properties.TESTSDIR != null) {
            var2 = (var1 = Properties.TESTSDIR.split(":")).length;

            for(var3 = 0; var3 < var2; ++var3) {
               var4 = var1[var3];
               var0.append(var4);
               var0.append(SystemProperties.PATH_SEPARATOR);
            }
         }

         if (Properties.DEPS != null) {
            var2 = (var1 = Properties.DEPS).length;

            for(var3 = 0; var3 < var2; ++var3) {
               var4 = var1[var3];
               var0.append(var4);
               var0.append(SystemProperties.PATH_SEPARATOR);
            }
         }
      }

      return var0.toString();
   }

   public static Response launch(String var0, String var1, String var2) {
      String var3 = UUID.randomUUID().toString();
      RegistrySingleton.createSingleton();
      Response var4 = null;

      try {
         Message var5;
         (var5 = new Message()).setProperties(Properties.getAllParameters());
         var5.setAgentPath(var2);
         RegistrySingleton.register(var3, var5);
         ArrayList var6 = new ArrayList();
         if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            var6.add(SystemProperties.JAVA_HOME + ".exe");
         } else {
            var6.add(SystemProperties.JAVA_HOME);
         }

         assert var6.size() != 0;

         if (Properties.ENABLE_ASSERTIONS_FOR_GZOLTAR) {
            var6.add("-ea");
         }

         var6.add("-Xmx" + Properties.MAX_CLIENT_MEMORY + "M");
         var6.add("-XX:MaxPermSize=" + Properties.MAX_PERM_SIZE + "M");
         int var10;
         if (Properties.NATIVE_LIBRARIES != null) {
            StringBuilder var7 = new StringBuilder(".");
            String[] var8;
            int var9 = (var8 = Properties.NATIVE_LIBRARIES).length;

            for(var10 = 0; var10 < var9; ++var10) {
               String var11 = var8[var10];
               var7.append(SystemProperties.PATH_SEPARATOR);
               var7.append(var11);
            }

            var6.add("-Djavarary.path=" + var7.toString());
         }

         var6.add("-cp");
         String var18 = prepareClassPath() + (var1 != null ? var1 : "");
         var5.setClassPath(var18);
         String var19 = null;
         String[] var20;
         var10 = (var20 = var18.split(SystemProperties.PATH_SEPARATOR)).length;

         for(int var23 = 0; var23 < var10; ++var23) {
            if ((var1 = var20[var23]).toLowerCase().contains("evosuite") && var1.endsWith(".jar")) {
               var19 = var1;
               break;
            }
         }

         var6.add(JarWithClassPath.createJarWithClassPath(var18) + (var19 != null ? SystemProperties.PATH_SEPARATOR + var19 : ""));
         if (var2 != null) {
            var6.add("-javaagent:" + var2);
         }

         var6.add(var0);
         var6.add(Integer.toString(RegistrySingleton.getPort()));
         var6.add(var3);
         Logger.getInstance().debug("Commands: " + var6.toString());
         ProcessBuilder var21;
         (var21 = new ProcessBuilder(var6)).redirectErrorStream(true);
         Process var22;
         InputStream var24 = (var22 = var21.start()).getInputStream();
         BufferedInputStream var15 = new BufferedInputStream(var24);
         byte[] var13 = new byte[1024];
         Timer var17 = new Timer();
         if (Properties.TIMELIMIT != -1) {
            var17.schedule(new a(var22), (long)(Properties.TIMELIMIT * 1000));
         }

         Logger.getInstance().debug(">>> Begin subprocess output");

         int var16;
         while((var16 = var15.read(var13)) != -1) {
            Logger.getInstance().info(var13, 0, var16);
         }

         Logger.getInstance().debug("<<< End subprocess output");
         int var14 = var22.waitFor();
         var17.cancel();
         var22.getOutputStream().close();
         var22.getInputStream().close();
         var22.getErrorStream().close();
         var22.destroy();
         if (var14 == 0) {
            var4 = var5.getResponse();
         }
      } catch (Exception var12) {
         Logger.getInstance().err("", var12);
      }

      Logger.getInstance().info("* Closing connection with client");
      RegistrySingleton.unregister(var3);
      return var4;
   }
}

package com.gzoltar.instrumentation.testing.launch;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.utils.SystemProperties;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class LaunchTest {
   public static TestResponse launch(String var0, String var1, String var2, ExecutionParameters var3) {
      String var4 = UUID.randomUUID().toString();
      TestResponse var5 = null;

      try {
         TestMessage var6;
         (var6 = new TestMessage()).setTestClassName(var1);
         var6.setTestMethodName(var2);
         var6.setExecutionData(var3);
         RegistrySingleton.register(var4, var6);
         ArrayList var13 = new ArrayList();
         if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            var13.add(SystemProperties.JAVA_HOME + ".exe");
         } else {
            var13.add(SystemProperties.JAVA_HOME);
         }

         assert var13.size() != 0;

         var13.add("-ea");
         var13.add("-Xmx1024M");
         var13.add("-XX:MaxPermSize=256M");
         var13.add("-cp");
         var2 = null;
         String[] var7;
         int var8 = (var7 = var3.getClassPath().split(SystemProperties.PATH_SEPARATOR)).length;

         int var9;
         for(var9 = 0; var9 < var8; ++var9) {
            String var10;
            if ((var10 = var7[var9]).toLowerCase().contains("evosuite") && var10.toLowerCase().endsWith(".jar")) {
               var2 = var10;
               break;
            }
         }

         var13.add(JarWithClassPath.createJarWithClassPath(var3.getClassPath()) + SystemProperties.PATH_SEPARATOR + var3.getJavaAgentPath() + (var2 != null ? SystemProperties.PATH_SEPARATOR + var2 : ""));
         StringBuilder var16 = new StringBuilder();
         String[] var18;
         var9 = (var18 = var3.getTargetClasses()).length;

         for(int var21 = 0; var21 < var9; ++var21) {
            var2 = var18[var21];
            var16.append(var2 + SystemProperties.PATH_SEPARATOR);
         }

         var13.add("-javaagent:" + var3.getJavaAgentPath() + "=output=none,append=false,dumponexit=false," + (SystemProperties.OS_NAME.contains("windows") ? "destfile=nul" : "destfile=/dev/null,includes=" + var16));
         var13.add(var0);
         var13.add(Integer.toString(RegistrySingleton.getPort()));
         var13.add(var4);
         Logger.getInstance().debug("Commands: " + var13.toString());
         ProcessBuilder var19;
         (var19 = new ProcessBuilder(var13)).redirectErrorStream(true);
         final Process var20;
         InputStream var22 = (var20 = var19.start()).getInputStream();
         BufferedInputStream var15 = new BufferedInputStream(var22);
         byte[] var12 = new byte[1024];
         Timer var17 = new Timer();
         if (var3.getTestCaseTimeout() != -1) {
            var17.schedule(new TimerTask() {
               public final void run() {
                  var20.destroy();
                  Logger.getInstance().err("Process terminated - timeout");
               }
            }, (long)(var3.getTestCaseTimeout() * 1000));
         }

         Logger.getInstance().debug(">>> Begin subprocess output");

         int var14;
         while((var14 = var15.read(var12)) != -1) {
            Logger.getInstance().info(var12, 0, var14);
         }

         Logger.getInstance().debug("<<< End subprocess output");
         if (var20.waitFor() == 0) {
            Logger.getInstance().debug("Subprocess ended with success");
            var5 = var6.getResponse();
         }

         var17.cancel();
         var20.getOutputStream().close();
         var20.getInputStream().close();
         var20.getErrorStream().close();
         var20.destroy();
      } catch (Exception var11) {
         Logger.getInstance().err(var11.getMessage(), var11);
      }

      RegistrySingleton.unregister(var4);
      return var5;
   }

   static {
      RegistrySingleton.createSingleton();
   }
}

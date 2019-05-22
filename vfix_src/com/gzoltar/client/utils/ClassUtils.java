package com.gzoltar.client.utils;

import com.gzoltar.client.Properties;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.transformer.ClassTransformer;
import com.gzoltar.instrumentation.utils.SystemProperties;
import com.gzoltar.shaded.javassist.ClassPool;
import com.gzoltar.shaded.org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtils implements Serializable {
   private static final long serialVersionUID = 5057937678023837930L;
   private static boolean testClasses = false;
   private static ClassPool classPool = ClassPool.getDefault();

   public static Map<String, ClassType.Type> getTestClasses() {
      testClasses = true;
      return getClasses();
   }

   public static List<String> getNonTestClasses() {
      testClasses = false;
      return new ArrayList(getClasses().keySet());
   }

   private static Map<String, ClassType.Type> getClasses() {
      HashMap var0 = new HashMap();
      int var3;
      if (Properties.PROJECT_CP != null) {
         String[] var1;
         int var2 = (var1 = Properties.PROJECT_CP.split(SystemProperties.PATH_SEPARATOR)).length;

         for(var3 = 0; var3 < var2; ++var3) {
            String var4 = var1[var3];
            Logger.getInstance().debug("Analysing classpath: " + var4);
            processClassPath(var4, var0);
         }
      } else {
         String var5;
         if ((var5 = testClasses ? Properties.TESTSDIR : Properties.CLASSESDIR) != null) {
            String[] var6;
            var3 = (var6 = var5.split(":")).length;

            for(int var7 = 0; var7 < var3; ++var7) {
               var5 = var6[var7];
               Logger.getInstance().debug("Analysing directory: " + var5);
               processClassPath(var5, var0);
            }
         }
      }

      return var0;
   }

   private static void processClassPath(String var0, Map<String, ClassType.Type> var1) {
      try {
         File var2;
         if (!(var2 = new File(var0)).exists()) {
            Logger.getInstance().debug("File/Directory '" + var0 + "' does not exist!");
            return;
         }

         if (!var2.canRead()) {
            Logger.getInstance().debug("No permission to read file/directory '" + var0 + "'!");
            return;
         }

         if (var2.isDirectory()) {
            Iterator var8 = FileUtils.listFiles(var2, new String[]{"class", "jar"}, true).iterator();

            while(var8.hasNext()) {
               processClassPath(((File)var8.next()).getAbsolutePath(), var1);
            }
         } else {
            if (!var2.getAbsolutePath().endsWith(".jar") || !Properties.SEARCH_FOR_CLASSES_ON_JAR_FILES) {
               if (var2.getAbsolutePath().endsWith(".class")) {
                  FileInputStream var7 = new FileInputStream(var2);
                  ClassType var11;
                  if ((var11 = loadClass(classPool.makeClass((InputStream)var7).getName())) != null) {
                     var1.put(var11.getName(), var11.getType());
                  }

                  var7.close();
               }

               return;
            }

            JarFile var6;
            Enumeration var9 = (var6 = new JarFile(var2.getAbsolutePath())).entries();

            while(var9.hasMoreElements()) {
               JarEntry var3;
               String var4;
               if ((var4 = (var3 = (JarEntry)var9.nextElement()).getName()).endsWith(".class")) {
                  InputStream var10 = var6.getInputStream(var3);
                  ClassType var12;
                  if ((var12 = loadClass(classPool.makeClass(var10).getName())) != null) {
                     var1.put(var12.getName(), var12.getType());
                  }

                  var10.close();
               } else {
                  var4.endsWith(".jar");
               }
            }

            var6.close();
         }
      } catch (IOException var5) {
         Logger.getInstance().err("", var5);
      }

   }

   private static ClassType loadClass(String var0) {
      if (ClassTransformer.matchesIgnored(var0)) {
         return null;
      } else if (!Properties.INCLUDE_LOCAL_CLASSES && var0.contains("$")) {
         Logger.getInstance().debug("Excluding inner classe " + var0);
         return null;
      } else {
         if (testClasses) {
            if (!ClassTransformer.isItAllowed(Properties.TESTPACKAGES, Properties.TESTCLASSES, var0)) {
               return null;
            }
         } else if (!ClassTransformer.isItAllowed(Properties.TARGETPACKAGES, Properties.TARGETCLASSES, var0)) {
            return null;
         }

         try {
            Class var1 = Class.forName(var0, false, ClassLoader.getSystemClassLoader());
            if (!Properties.INCLUDE_DEPRECATED_CLASSES && var1.isAnnotationPresent(Deprecated.class)) {
               Logger.getInstance().debug("Excluding deprecated class " + var0);
               return null;
            }

            ClassType.Type var3 = ClassType.whatType(var1);
            if (testClasses && (var3 == ClassType.Type.JUNIT || var3 == ClassType.Type.TESTNG)) {
               return new ClassType(var0, var3);
            }

            if (!testClasses && var3 == ClassType.Type.CLASS) {
               return new ClassType(var0, var3);
            }
         } catch (ClassNotFoundException var2) {
            Logger.getInstance().err("", var2);
         }

         return null;
      }
   }
}

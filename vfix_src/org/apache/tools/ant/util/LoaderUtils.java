package org.apache.tools.ant.util;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.launch.Locator;

public class LoaderUtils {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$util$LoaderUtils;

   public static void setContextClassLoader(ClassLoader loader) {
      Thread currentThread = Thread.currentThread();
      currentThread.setContextClassLoader(loader);
   }

   public static ClassLoader getContextClassLoader() {
      Thread currentThread = Thread.currentThread();
      return currentThread.getContextClassLoader();
   }

   public static boolean isContextLoaderAvailable() {
      return true;
   }

   private static File normalizeSource(File source) {
      if (source != null) {
         try {
            source = FILE_UTILS.normalize(source.getAbsolutePath());
         } catch (BuildException var2) {
         }
      }

      return source;
   }

   public static File getClassSource(Class c) {
      return normalizeSource(Locator.getClassSource(c));
   }

   public static File getResourceSource(ClassLoader c, String resource) {
      if (c == null) {
         c = (class$org$apache$tools$ant$util$LoaderUtils == null ? (class$org$apache$tools$ant$util$LoaderUtils = class$("org.apache.tools.ant.util.LoaderUtils")) : class$org$apache$tools$ant$util$LoaderUtils).getClassLoader();
      }

      return normalizeSource(Locator.getResourceSource(c, resource));
   }

   public static String classNameToResource(String className) {
      return className.replace('.', '/') + ".class";
   }

   public static boolean classExists(ClassLoader loader, String className) {
      return loader.getResource(classNameToResource(className)) != null;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}

package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Java7Support {
   private static final boolean isJava7;
   private static Method isSymbolicLink;
   private static Method toPath;

   public static boolean isSymLink(File file) {
      try {
         Object path = toPath.invoke(file);
         return (Boolean)isSymbolicLink.invoke((Object)null, path);
      } catch (IllegalAccessException var2) {
         throw new RuntimeException(var2);
      } catch (InvocationTargetException var3) {
         throw new RuntimeException(var3);
      }
   }

   public static boolean isJava7() {
      return isJava7;
   }

   static {
      boolean isJava7x = true;

      try {
         Class<?> files = Thread.currentThread().getContextClassLoader().loadClass("java.nio.file.Files");
         Class<?> path = Thread.currentThread().getContextClassLoader().loadClass("java.nio.file.Path");
         isSymbolicLink = files.getMethod("isSymbolicLink", path);
         toPath = File.class.getMethod("toPath");
      } catch (ClassNotFoundException var3) {
         isJava7x = false;
      } catch (NoSuchMethodException var4) {
         isJava7x = false;
      }

      isJava7 = isJava7x;
   }
}

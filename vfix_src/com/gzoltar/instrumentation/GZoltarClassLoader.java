package com.gzoltar.instrumentation;

import com.gzoltar.instrumentation.transformer.ClassTransformer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GZoltarClassLoader extends URLClassLoader {
   public static String projectCP = null;
   public static String classesDir = null;
   public static String testsDir = null;
   public static String[] deps = null;
   public static String mutantDir = null;

   public GZoltarClassLoader(ClassLoader var1) {
      super(((URLClassLoader)var1).getURLs(), ClassLoader.getSystemClassLoader().getParent());
   }

   public URL getResource(String var1) {
      return super.getResource(var1);
   }

   public Enumeration<URL> getResources(String var1) throws IOException {
      return super.getResources(var1);
   }

   public InputStream getResourceAsStream(String var1) {
      return super.getResourceAsStream(var1);
   }

   protected Class<?> findClass(String var1) throws ClassNotFoundException {
      return super.findClass(var1);
   }

   public URL findResource(String var1) {
      return super.findResource(var1);
   }

   public Enumeration<URL> findResources(String var1) throws IOException {
      return super.findResources(var1);
   }

   protected Class<?> loadClass(String var1, boolean var2) throws ClassNotFoundException {
      return super.loadClass(var1, var2);
   }

   public Class<?> loadClass(String var1) {
      if (ClassTransformer.matchesIgnored(var1)) {
         try {
            return super.loadClass(var1);
         } catch (ClassNotFoundException var6) {
            return null;
         }
      } else {
         Class var2 = null;
         if (mutantDir != null && (var2 = this.processClassPath(mutantDir, var1)) != null) {
            return var2;
         } else {
            URL[] var3;
            int var4 = (var3 = this.getURLs()).length;

            for(int var5 = 0; var5 < var4; ++var5) {
               URL var8 = var3[var5];
               if ((var2 = this.processClassPath(var8.getPath(), var1)) != null) {
                  return var2;
               }
            }

            if (var2 == null) {
               try {
                  return super.loadClass(var1);
               } catch (ClassNotFoundException var7) {
               }
            }

            return null;
         }
      }
   }

   private Class<?> processClassPath(String var1, String var2) {
      byte[] var3 = null;

      try {
         File var4;
         if (!(var4 = new File(var1)).exists()) {
            Logger.getInstance().debug("  File/Directory does not exist!");
            return null;
         }

         if (!var4.canRead()) {
            Logger.getInstance().debug("  No permission to read File/Directory!");
            return null;
         }

         if (var4.isDirectory()) {
            File var8;
            if (!(var8 = new File(var1 + System.getProperty("file.separator") + var2.replace(".", System.getProperty("file.separator")) + ".class")).exists()) {
               return null;
            }

            Logger.getInstance().debug("loading class " + var2 + " from " + var8.getAbsolutePath());
            var3 = this.getByteArray(var8.length(), new FileInputStream(var8));
         } else if (var4.getAbsolutePath().endsWith(".jar")) {
            JarFile var9;
            Enumeration var10 = (var9 = new JarFile(var4.getAbsolutePath())).entries();

            while(var10.hasMoreElements()) {
               JarEntry var5;
               String var6;
               if ((var6 = (var5 = (JarEntry)var10.nextElement()).getName()).endsWith(".class")) {
                  if (var6.equals(var2.replace(".", System.getProperty("file.separator")) + ".class")) {
                     Logger.getInstance().debug("loading class " + var2 + " from " + var6 + " (jar)");
                     var3 = this.getByteArray(var5.getSize(), var9.getInputStream(var5));
                     break;
                  }
               } else {
                  var6.endsWith(".jar");
               }
            }

            var9.close();
         } else if (var4.getAbsolutePath().endsWith(".class")) {
            var3 = this.getByteArray(var4.length(), new FileInputStream(var4));
         }
      } catch (IOException var7) {
         Logger.getInstance().err("", var7);
      }

      if (var3 == null) {
         return null;
      } else {
         this.createPackageDefinition(var2);
         return this.defineClass(var2, var3, 0, var3.length);
      }
   }

   private void createPackageDefinition(String var1) {
      int var2;
      if ((var2 = var1.lastIndexOf(46)) != -1) {
         var1 = var1.substring(0, var2);
         if (this.getPackage(var1) == null) {
            this.definePackage(var1, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (URL)null);
         }
      }

   }

   private byte[] getByteArray(long var1, InputStream var3) throws IOException {
      if (var1 >= -2147483648L && var1 <= 2147483647L) {
         byte[] var5 = new byte[(int)var1];
         ByteArrayOutputStream var2 = new ByteArrayOutputStream(var5.length);

         int var4;
         while((var4 = var3.read(var5)) != -1) {
            var2.write(var5, 0, var4);
         }

         var3.close();
         var2.close();
         return var2.toByteArray();
      } else {
         throw new IllegalArgumentException(var1 + " cannot be cast to int without changing its value!");
      }
   }
}

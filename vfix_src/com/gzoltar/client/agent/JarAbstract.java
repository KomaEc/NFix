package com.gzoltar.client.agent;

import com.gzoltar.client.utils.ArrayUtils;
import com.gzoltar.instrumentation.Logger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;

public abstract class JarAbstract {
   private JarOutputStream target;
   private final String filePrefix;
   private final String preMainClass;
   private final String[] packages;

   protected JarAbstract(String var1, String var2, String[] var3) {
      this.filePrefix = var1;
      this.preMainClass = var2;
      this.packages = var3;
   }

   private void add(InputStream var1, JarEntry var2, JarOutputStream var3) throws IOException {
      BufferedInputStream var4 = null;
      boolean var6 = false;

      try {
         var6 = true;
         var3.putNextEntry(var2);
         var4 = new BufferedInputStream(var1);
         byte[] var8 = new byte[1024];

         while(true) {
            int var9;
            if ((var9 = var4.read(var8)) == -1) {
               var3.closeEntry();
               var6 = false;
               break;
            }

            var3.write(var8, 0, var9);
         }
      } finally {
         if (var6) {
            if (var4 != null) {
               var4.close();
            }

         }
      }

      var4.close();
   }

   protected void processDirectory(File var1, String var2) throws IOException {
      File[] var6 = var1.listFiles();

      for(int var3 = 0; var3 < var6.length; ++var3) {
         String var4 = var6[var3].getName();
         if (var6[var3].isDirectory()) {
            this.processDirectory(var6[var3], var2 + '/' + var4);
         } else if ((var4.endsWith(".class") || var4.endsWith(".linux.x86") || var4.endsWith(".linux.x86_64") || var4.endsWith(".macosx.x86_64")) && ArrayUtils.startsWith(this.packages, var4)) {
            var4 = var2 + '/' + var4;
            JarEntry var7;
            (var7 = new JarEntry(var4)).setTime(var6[var3].lastModified());
            FileInputStream var5 = new FileInputStream(var6[var3]);
            this.add(var5, var7, this.target);
            var5.close();
         }
      }

   }

   private File extractJarFileFromJar(JarFile var1, JarEntry var2) throws IOException {
      BufferedInputStream var5 = new BufferedInputStream(var1.getInputStream(var2));
      File var6;
      (var6 = File.createTempFile(UUID.randomUUID().toString(), ".jar")).deleteOnExit();
      String var3;
      if ((var3 = var6.getParent()) != null) {
         (new File(var3)).mkdirs();
      }

      BufferedOutputStream var7 = new BufferedOutputStream(new FileOutputStream(var6));

      int var4;
      while((var4 = var5.read()) != -1) {
         var7.write((byte)var4);
      }

      var5.close();
      var7.close();
      return var6;
   }

   protected void processJarFile(JarFile var1) throws IOException {
      Enumeration var2 = var1.entries();

      while(true) {
         while(var2.hasMoreElements()) {
            JarEntry var3;
            String var4;
            if (((var4 = (var3 = (JarEntry)var2.nextElement()).getName()).endsWith(".class") || var4.endsWith(".linux.x86") || var4.endsWith(".linux.x86_64") || var4.endsWith(".macosx.x86_64")) && ArrayUtils.startsWith(this.packages, var4)) {
               InputStream var6 = var1.getInputStream(var3);
               this.add(var6, var3, this.target);
               var6.close();
            } else {
               File var5;
               if (var4.endsWith(".jar") && (var5 = this.extractJarFileFromJar(var1, var3)) != null) {
                  this.processJarFile(new JarFile(var5.getAbsolutePath()));
               }
            }
         }

         var1.close();
         return;
      }
   }

   protected List<String> getGZoltarTargetClassesDirectory() {
      ArrayList var1;
      (var1 = new ArrayList()).add(System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes");
      var1.add(System.getProperty("user.dir") + File.separator + ".." + File.separator + "com.gzoltar.client" + File.separator + "target" + File.separator + "classes");
      var1.add(System.getProperty("user.dir") + File.separator + ".." + File.separator + "com.gzoltar.instrumentation" + File.separator + "target" + File.separator + "classes");
      var1.add(System.getProperty("user.dir") + File.separator + ".." + File.separator + "com.gzoltar.nnative" + File.separator + "target" + File.separator + "classes");
      return var1;
   }

   protected void createJar() {
      URL var1;
      if ((var1 = JarAbstract.class.getClassLoader().getResource("com/gzoltar")) == null) {
         Logger.getInstance().warn("Cannot find the 'com/gzoltar' resource");
      } else {
         try {
            String var2;
            if (!var1.toString().startsWith("jar:")) {
               Iterator var4 = this.getGZoltarTargetClassesDirectory().iterator();

               while(var4.hasNext()) {
                  var2 = (String)var4.next();
                  this.processDirectory(new File(var2), "");
               }

               return;
            }

            var2 = var1.getPath().replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
            this.processJarFile(new JarFile(var2));
         } catch (IOException var3) {
            Logger.getInstance().err("", var3);
         }

      }
   }

   public File extract() {
      try {
         File var1;
         (var1 = File.createTempFile(this.filePrefix, ".jar")).createNewFile();
         var1.deleteOnExit();
         Logger.getInstance().debug("Jar file path " + var1.getAbsolutePath());
         FileOutputStream var2 = new FileOutputStream(var1);
         if (this.preMainClass != null) {
            Manifest var3;
            (var3 = new Manifest()).getMainAttributes().put(Name.MANIFEST_VERSION, "1.0");
            var3.getMainAttributes().putValue("Premain-Class", this.preMainClass);
            this.target = new JarOutputStream(var2, var3);
         } else {
            this.target = new JarOutputStream(var2);
         }

         this.createJar();
         this.target.close();
         var2.close();
         return var1;
      } catch (IOException var4) {
         Logger.getInstance().err("", var4);
         return null;
      }
   }
}

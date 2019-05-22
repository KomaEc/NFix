package org.codehaus.classworlds.uberjar.boot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class InitialClassLoader extends SecureClassLoader {
   private Map index = new HashMap();
   private URL classworldsJarUrl;

   public InitialClassLoader() throws Exception {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      URL classUrl = this.getClass().getResource("InitialClassLoader.class");
      String urlText = classUrl.toExternalForm();
      int bangLoc = urlText.indexOf("!");
      System.setProperty("classworlds.lib", urlText.substring(0, bangLoc) + "!/WORLDS-INF/lib");
      this.classworldsJarUrl = new URL(urlText.substring(0, bangLoc) + "!/WORLDS-INF/classworlds.jar");
   }

   public synchronized Class findClass(String className) throws ClassNotFoundException {
      String classPath = className.replace('.', '/') + ".class";
      if (this.index.containsKey(classPath)) {
         return (Class)this.index.get(classPath);
      } else {
         try {
            JarInputStream in = new JarInputStream(this.classworldsJarUrl.openStream());

            try {
               JarEntry entry = null;

               do {
                  if ((entry = in.getNextJarEntry()) == null) {
                     throw new ClassNotFoundException(className);
                  }
               } while(!entry.getName().equals(classPath));

               ByteArrayOutputStream out = new ByteArrayOutputStream();

               try {
                  byte[] buffer = new byte[2048];
                  boolean var7 = false;

                  while(true) {
                     if (in.available() > 0) {
                        int read = in.read(buffer, 0, buffer.length);
                        if (read >= 0) {
                           out.write(buffer, 0, read);
                           continue;
                        }
                     }

                     buffer = out.toByteArray();
                     Class cls = this.defineClass(className, buffer, 0, buffer.length);
                     this.index.put(className, cls);
                     Class var9 = cls;
                     return var9;
                  }
               } finally {
                  out.close();
               }
            } finally {
               in.close();
            }
         } catch (IOException var20) {
            var20.printStackTrace();
            throw new ClassNotFoundException("io error reading stream for: " + className);
         }
      }
   }
}

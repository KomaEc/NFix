package org.codehaus.classworlds;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class UberJarRealmClassLoader extends RealmClassLoader {
   private Map classIndex = new HashMap();
   private List urls = new ArrayList();
   private Map jarIndexes = new HashMap();

   public UberJarRealmClassLoader(DefaultClassRealm realm) {
      super(realm);
   }

   public void addConstituent(URL constituent) {
      if ("jar".equals(constituent.getProtocol()) || constituent.toExternalForm().endsWith(".jar")) {
         this.buildIndexForJar(constituent);
      }

      this.urls.add(constituent);
      super.addConstituent(constituent);
   }

   private void buildIndexForJar(URL inUrl) {
      HashMap index = new HashMap();
      String urlText = null;
      if (inUrl.getProtocol().equals("jar")) {
         urlText = inUrl.toExternalForm();
      } else {
         urlText = "jar:" + inUrl.toExternalForm();
      }

      URL resourceUrl = null;

      try {
         JarInputStream in = new JarInputStream(inUrl.openStream());

         try {
            JarEntry entry = null;

            while((entry = in.getNextJarEntry()) != null) {
               String resourceName = entry.getName();
               resourceUrl = new URL(urlText + "!/" + resourceName);
               index.put(resourceName, resourceUrl);
            }
         } finally {
            in.close();
         }
      } catch (IOException var12) {
      }

      this.jarIndexes.put(inUrl, index);
   }

   public Class loadClassDirect(String className) throws ClassNotFoundException {
      String classPath = className.replace('.', '/') + ".class";
      if (this.classIndex.containsKey(classPath)) {
         return (Class)this.classIndex.get(classPath);
      } else {
         Iterator urlIter = this.urls.iterator();
         URL eachUrl = null;
         byte[] classBytes = null;

         while(classBytes == null && urlIter.hasNext()) {
            eachUrl = (URL)urlIter.next();
            if (!"jar".equals(eachUrl.getProtocol()) && !eachUrl.toExternalForm().endsWith(".jar")) {
               classBytes = this.findClassInDirectoryUrl(eachUrl, classPath);
            } else {
               classBytes = this.findClassInJarStream(eachUrl, classPath);
            }
         }

         if (classBytes == null) {
            return null;
         } else {
            Class cls = this.defineClass(className, classBytes, 0, classBytes.length);
            this.classIndex.put(classPath, cls);
            return cls;
         }
      }
   }

   public URL findResource(String name) {
      URL resourceUrl = null;
      Iterator urlIter = this.urls.iterator();
      URL eachUrl = null;

      do {
         if (!urlIter.hasNext()) {
            return null;
         }

         eachUrl = (URL)urlIter.next();
         if (!"jar".equals(eachUrl.getProtocol()) && !eachUrl.toExternalForm().endsWith(".jar")) {
            resourceUrl = this.findResourceInDirectoryUrl(eachUrl, name);
         } else {
            resourceUrl = this.findResourceInJarStream(eachUrl, name);
         }
      } while(resourceUrl == null);

      return resourceUrl;
   }

   public Enumeration findResourcesDirect(String name) {
      Vector list = new Vector();
      URL resourceUrl = null;
      Iterator urlIter = this.urls.iterator();
      URL eachUrl = null;

      while(urlIter.hasNext()) {
         eachUrl = (URL)urlIter.next();
         if (!"jar".equals(eachUrl.getProtocol()) && !eachUrl.toExternalForm().endsWith(".jar")) {
            resourceUrl = this.findResourceInDirectoryUrl(eachUrl, name);
         } else {
            resourceUrl = this.findResourceInJarStream(eachUrl, name);
         }

         if (resourceUrl != null) {
            list.add(resourceUrl);
         }
      }

      return list.elements();
   }

   protected URL findResourceInJarStream(URL inUrl, String path) {
      return (URL)((Map)this.jarIndexes.get(inUrl)).get(path);
   }

   protected URL findResourceInDirectoryUrl(URL inUrl, String path) {
      return null;
   }

   protected byte[] findClassInJarStream(URL inUrl, String path) {
      URL classUrl = (URL)((Map)this.jarIndexes.get(inUrl)).get(path);
      if (classUrl != null) {
         try {
            return this.readStream(classUrl.openStream());
         } catch (IOException var5) {
         }
      }

      return null;
   }

   protected byte[] findClassInDirectoryUrl(URL url, String path) {
      try {
         new URL(url, path);
      } catch (IOException var4) {
      }

      return null;
   }

   private byte[] readStream(InputStream in) throws IOException {
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      try {
         byte[] buffer = new byte[2048];
         boolean var4 = false;

         while(true) {
            if (in.available() > 0) {
               int read = in.read(buffer, 0, buffer.length);
               if (read >= 0) {
                  out.write(buffer, 0, read);
                  continue;
               }
            }

            byte[] var5 = out.toByteArray();
            return var5;
         }
      } finally {
         out.close();
      }
   }
}

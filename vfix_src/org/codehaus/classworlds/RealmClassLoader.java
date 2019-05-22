package org.codehaus.classworlds;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

class RealmClassLoader extends URLClassLoader {
   protected DefaultClassRealm realm;

   RealmClassLoader(DefaultClassRealm realm) {
      this(realm, (ClassLoader)null);
   }

   RealmClassLoader(DefaultClassRealm realm, ClassLoader classLoader) {
      super(new URL[0], classLoader);
      this.realm = realm;
   }

   DefaultClassRealm getRealm() {
      return this.realm;
   }

   void addConstituent(URL constituent) {
      String urlStr = constituent.toExternalForm();
      if (!urlStr.endsWith(".class")) {
         if (urlStr.startsWith("jar:") && urlStr.endsWith("!/")) {
            urlStr = urlStr.substring(4, urlStr.length() - 2);

            try {
               constituent = new URL(urlStr);
            } catch (MalformedURLException var8) {
               var8.printStackTrace();
            }
         }

         this.addURL(constituent);
      } else {
         try {
            byte[] b = this.getBytesToEndOfStream(new DataInputStream(constituent.openStream()));
            int start = urlStr.lastIndexOf("byteclass") + 10;
            int end = urlStr.lastIndexOf(".class");
            String className = urlStr.substring(start, end);
            super.defineClass(className, b, 0, b.length);
            this.addURL(constituent);
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

   }

   public byte[] getBytesToEndOfStream(DataInputStream in) throws IOException {
      int chunkSize = in.available() > 0 ? in.available() : 2048;
      byte[] buf = new byte[chunkSize];
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream(chunkSize);

      int count;
      while((count = in.read(buf)) != -1) {
         byteStream.write(buf, 0, count);
      }

      return byteStream.toByteArray();
   }

   Class loadClassDirect(String name) throws ClassNotFoundException {
      return super.loadClass(name, false);
   }

   protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
      return this.getRealm().loadClass(name);
   }

   public URL[] getURLs() {
      return super.getURLs();
   }

   public URL findResource(String name) {
      return super.findResource(name);
   }

   public URL getResource(String name) {
      return this.getRealm().getResource(name);
   }

   public URL getResourceDirect(String name) {
      return super.getResource(name);
   }

   public Enumeration findResources(String name) throws IOException {
      return this.getRealm().findResources(name);
   }

   public Enumeration findResourcesDirect(String name) throws IOException {
      return super.findResources(name);
   }
}

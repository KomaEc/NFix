package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.classpath.ClassPath;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class DefaultPITClassloader extends ClassLoader {
   private final ClassPath classPath;

   public DefaultPITClassloader(ClassPath cp, ClassLoader parent) {
      super(parent);
      this.classPath = cp;
   }

   protected Class<?> findClass(String name) throws ClassNotFoundException {
      try {
         byte[] b = this.classPath.getClassData(name);
         if (b == null) {
            throw new ClassNotFoundException(name);
         } else {
            this.definePackage(name);
            return this.defineClass(name, b);
         }
      } catch (IOException var3) {
         throw new ClassNotFoundException(name, var3);
      }
   }

   private void definePackage(String name) {
      int i = name.lastIndexOf(46);
      if (i != -1) {
         String pkgname = name.substring(0, i);
         if (this.getPackage(pkgname) == null) {
            this.definePackage(pkgname, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (URL)null);
         }
      }

   }

   protected Class<?> defineClass(String name, byte[] b) {
      return this.defineClass(name, b, 0, b.length);
   }

   protected URL findResource(String name) {
      return this.classPath.findResource(name);
   }

   protected Enumeration<URL> findResources(final String name) {
      return new Enumeration<URL>() {
         private URL element = DefaultPITClassloader.this.findResource(name);

         public boolean hasMoreElements() {
            return this.element != null;
         }

         public URL nextElement() {
            if (this.element != null) {
               URL next = this.element;
               this.element = null;
               return next;
            } else {
               throw new NoSuchElementException();
            }
         }
      };
   }
}

package org.jboss.util.loading;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class DelegatingClassLoader extends URLClassLoader {
   public static final URL[] EMPTY_URL_ARRAY = new URL[0];
   protected boolean standard = false;
   private ClassLoader parent = null;

   public DelegatingClassLoader(ClassLoader parent) {
      super(EMPTY_URL_ARRAY, parent);
      if (parent == null) {
         throw new IllegalArgumentException("No parent");
      } else {
         this.parent = parent;
      }
   }

   public DelegatingClassLoader(ClassLoader parent, URLStreamHandlerFactory factory) {
      super(EMPTY_URL_ARRAY, parent, factory);
      if (parent == null) {
         throw new IllegalArgumentException("No parent");
      } else {
         this.parent = parent;
      }
   }

   protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
      if (this.standard) {
         return super.loadClass(className, resolve);
      } else {
         Class clazz = null;

         try {
            clazz = this.parent.loadClass(className);
         } catch (ClassNotFoundException var5) {
            clazz = this.findLoadedClass(className);
            if (clazz == null) {
               throw var5;
            }
         }

         if (resolve) {
            this.resolveClass(clazz);
         }

         return clazz;
      }
   }
}

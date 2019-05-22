package org.codehaus.groovy.tools;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class RootLoader extends URLClassLoader {
   private Map<String, Class> customClasses;

   private RootLoader(ClassLoader parent) {
      this(new URL[0], parent);
   }

   public RootLoader(URL[] urls, ClassLoader parent) {
      super(urls, parent);
      this.customClasses = new HashMap();

      try {
         this.customClasses.put("org.w3c.dom.Node", super.loadClass("org.w3c.dom.Node", false));
      } catch (Exception var4) {
      }

   }

   private static ClassLoader chooseParent() {
      ClassLoader cl = RootLoader.class.getClassLoader();
      return cl != null ? cl : ClassLoader.getSystemClassLoader();
   }

   public RootLoader(LoaderConfiguration lc) {
      this(chooseParent());
      Thread.currentThread().setContextClassLoader(this);
      URL[] urls = lc.getClassPathUrls();
      URL[] arr$ = urls;
      int len$ = urls.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         URL url = arr$[i$];
         this.addURL(url);
      }

   }

   protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
      Class c = this.findLoadedClass(name);
      if (c != null) {
         return c;
      } else {
         c = (Class)this.customClasses.get(name);
         if (c != null) {
            return c;
         } else {
            try {
               c = this.oldFindClass(name);
            } catch (ClassNotFoundException var5) {
            }

            if (c == null) {
               c = super.loadClass(name, resolve);
            }

            if (resolve) {
               this.resolveClass(c);
            }

            return c;
         }
      }
   }

   public URL getResource(String name) {
      URL url = this.findResource(name);
      if (url == null) {
         url = super.getResource(name);
      }

      return url;
   }

   public void addURL(URL url) {
      super.addURL(url);
   }

   private Class oldFindClass(String name) throws ClassNotFoundException {
      return super.findClass(name);
   }

   protected Class findClass(String name) throws ClassNotFoundException {
      throw new ClassNotFoundException(name);
   }
}

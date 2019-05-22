package org.apache.maven.surefire.booter;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

public class IsolatedClassLoader extends URLClassLoader {
   private final ClassLoader parent = ClassLoader.getSystemClassLoader();
   private final Set<URL> urls = new HashSet();
   private final String roleName;
   private boolean childDelegation = true;
   private static final URL[] EMPTY_URL_ARRAY = new URL[0];

   public IsolatedClassLoader(ClassLoader parent, boolean childDelegation, String roleName) {
      super(EMPTY_URL_ARRAY, parent);
      this.childDelegation = childDelegation;
      this.roleName = roleName;
   }

   public void addURL(URL url) {
      if (!this.urls.contains(url)) {
         super.addURL(url);
         this.urls.add(url);
      }

   }

   public synchronized Class loadClass(String name) throws ClassNotFoundException {
      Class c;
      if (this.childDelegation) {
         c = this.findLoadedClass(name);
         ClassNotFoundException ex = null;
         if (c == null) {
            try {
               c = this.findClass(name);
            } catch (ClassNotFoundException var5) {
               ex = var5;
               if (this.parent != null) {
                  c = this.parent.loadClass(name);
               }
            }
         }

         if (c == null) {
            throw ex;
         }
      } else {
         c = super.loadClass(name);
      }

      return c;
   }

   public String toString() {
      return "IsolatedClassLoader{roleName='" + this.roleName + '\'' + '}';
   }
}

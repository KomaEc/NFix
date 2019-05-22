package org.jboss.util.loading;

public class MyClassLoaderClassLoaderSource implements ClassLoaderSource {
   public ClassLoader getClassLoader() {
      return this.getClass().getClassLoader();
   }
}

package org.jboss.util.loading;

import java.lang.ref.WeakReference;
import java.security.AccessController;

public class ConstructorTCCLClassLoaderSource implements ClassLoaderSource {
   private final WeakReference classLoaderRef;

   public ConstructorTCCLClassLoaderSource() {
      ContextClassLoader ccl = (ContextClassLoader)AccessController.doPrivileged(ContextClassLoader.INSTANTIATOR);
      ClassLoader cl = ccl.getContextClassLoader();
      if (cl != null) {
         this.classLoaderRef = new WeakReference(cl);
      } else {
         this.classLoaderRef = null;
      }

   }

   public ClassLoader getClassLoader() {
      return this.classLoaderRef == null ? null : (ClassLoader)this.classLoaderRef.get();
   }
}

package org.jboss.util.loading;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class ContextClassLoader {
   public static final RuntimePermission GETCLASSLOADER = new RuntimePermission("getClassLoader");
   public static final ContextClassLoader.NewInstance INSTANTIATOR = new ContextClassLoader.NewInstance();

   ContextClassLoader() {
      SecurityManager manager = System.getSecurityManager();
      if (manager != null) {
         manager.checkPermission(GETCLASSLOADER);
      }

   }

   public ClassLoader getContextClassLoader() {
      return this.getContextClassLoader(Thread.currentThread());
   }

   public ClassLoader getContextClassLoader(final Thread thread) {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return thread.getContextClassLoader();
         }
      });
   }

   private static class NewInstance implements PrivilegedAction {
      private NewInstance() {
      }

      public Object run() {
         return new ContextClassLoader();
      }

      // $FF: synthetic method
      NewInstance(Object x0) {
         this();
      }
   }
}

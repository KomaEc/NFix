package org.jboss.util.collection;

import java.security.AccessController;
import java.security.PrivilegedAction;

class SecurityActions {
   static ClassLoader getClassLoader(Class<?> clazz) {
      return System.getSecurityManager() == null ? SecurityActions.GetClassLoaderAction.NON_PRIVILEGED.getClassLoader(clazz) : SecurityActions.GetClassLoaderAction.PRIVILEGED.getClassLoader(clazz);
   }

   interface GetClassLoaderAction {
      SecurityActions.GetClassLoaderAction NON_PRIVILEGED = new SecurityActions.GetClassLoaderAction() {
         public ClassLoader getClassLoader(Class<?> clazz) {
            return clazz.getClassLoader();
         }
      };
      SecurityActions.GetClassLoaderAction PRIVILEGED = new SecurityActions.GetClassLoaderAction() {
         public ClassLoader getClassLoader(final Class<?> clazz) {
            return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
               public ClassLoader run() {
                  return clazz.getClassLoader();
               }
            });
         }
      };

      ClassLoader getClassLoader(Class<?> var1);
   }
}

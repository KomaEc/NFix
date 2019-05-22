package org.jboss.util.loading;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class ContextClassLoaderSwitcher extends ContextClassLoader {
   public static final RuntimePermission SETCONTEXTCLASSLOADER = new RuntimePermission("setContextClassLoader");
   public static final ContextClassLoaderSwitcher.NewInstance INSTANTIATOR = new ContextClassLoaderSwitcher.NewInstance();

   private ContextClassLoaderSwitcher() {
      SecurityManager manager = System.getSecurityManager();
      if (manager != null) {
         manager.checkPermission(SETCONTEXTCLASSLOADER);
      }

   }

   public void setContextClassLoader(ClassLoader cl) {
      this.setContextClassLoader(Thread.currentThread(), cl);
   }

   public void setContextClassLoader(final Thread thread, final ClassLoader cl) {
      AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            thread.setContextClassLoader(cl);
            return null;
         }
      });
   }

   public ContextClassLoaderSwitcher.SwitchContext getSwitchContext() {
      return new ContextClassLoaderSwitcher.SwitchContext();
   }

   public ContextClassLoaderSwitcher.SwitchContext getSwitchContext(ClassLoader cl) {
      return new ContextClassLoaderSwitcher.SwitchContext(cl);
   }

   /** @deprecated */
   public ContextClassLoaderSwitcher.SwitchContext getSwitchContext(Class clazz) {
      return new ContextClassLoaderSwitcher.SwitchContext(clazz.getClassLoader());
   }

   // $FF: synthetic method
   ContextClassLoaderSwitcher(Object x0) {
      this();
   }

   private static class NewInstance implements PrivilegedAction {
      private NewInstance() {
      }

      public Object run() {
         return new ContextClassLoaderSwitcher();
      }

      // $FF: synthetic method
      NewInstance(Object x0) {
         this();
      }
   }

   public class SwitchContext {
      private ClassLoader origCL;
      private ClassLoader currentCL;
      private Thread currentThread;

      private SwitchContext() {
         this.currentThread = Thread.currentThread();
         this.origCL = ContextClassLoaderSwitcher.this.getContextClassLoader(this.currentThread);
         this.currentCL = this.origCL;
      }

      private SwitchContext(ClassLoader cl) {
         this();
         this.setClassLoader(cl);
      }

      public Thread getThread() {
         return this.currentThread;
      }

      public ClassLoader getOriginalClassLoader() {
         return this.origCL;
      }

      public ClassLoader getCurrentClassLoader() {
         return this.currentCL;
      }

      public void setClassLoader(ClassLoader cl) {
         if (cl != null && cl != this.currentCL) {
            ContextClassLoaderSwitcher.this.setContextClassLoader(this.currentThread, cl);
            this.currentCL = cl;
         }

      }

      public void reset() {
         if (this.currentCL != null && this.currentCL != this.origCL) {
            ContextClassLoaderSwitcher.this.setContextClassLoader(this.currentThread, this.origCL);
         }

      }

      public void forceReset() {
         ContextClassLoaderSwitcher.this.setContextClassLoader(this.currentThread, this.origCL);
      }

      // $FF: synthetic method
      SwitchContext(Object x1) {
         this();
      }

      // $FF: synthetic method
      SwitchContext(ClassLoader x1, Object x2) {
         this((ClassLoader)x1);
      }
   }
}

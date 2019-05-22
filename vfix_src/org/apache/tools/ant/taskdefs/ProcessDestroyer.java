package org.apache.tools.ant.taskdefs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;

class ProcessDestroyer implements Runnable {
   private Vector processes = new Vector();
   private Method addShutdownHookMethod;
   private Method removeShutdownHookMethod;
   private ProcessDestroyer.ProcessDestroyerImpl destroyProcessThread = null;
   private boolean added = false;
   private boolean running = false;
   // $FF: synthetic field
   static Class class$java$lang$Thread;
   // $FF: synthetic field
   static Class class$java$lang$Runtime;
   // $FF: synthetic field
   static Class class$java$lang$IllegalStateException;

   public ProcessDestroyer() {
      try {
         Class[] paramTypes = new Class[]{class$java$lang$Thread == null ? (class$java$lang$Thread = class$("java.lang.Thread")) : class$java$lang$Thread};
         this.addShutdownHookMethod = (class$java$lang$Runtime == null ? (class$java$lang$Runtime = class$("java.lang.Runtime")) : class$java$lang$Runtime).getMethod("addShutdownHook", paramTypes);
         this.removeShutdownHookMethod = (class$java$lang$Runtime == null ? (class$java$lang$Runtime = class$("java.lang.Runtime")) : class$java$lang$Runtime).getMethod("removeShutdownHook", paramTypes);
      } catch (NoSuchMethodException var2) {
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private void addShutdownHook() {
      if (this.addShutdownHookMethod != null && !this.running) {
         this.destroyProcessThread = new ProcessDestroyer.ProcessDestroyerImpl();
         Object[] args = new Object[]{this.destroyProcessThread};

         try {
            this.addShutdownHookMethod.invoke(Runtime.getRuntime(), args);
            this.added = true;
         } catch (IllegalAccessException var4) {
            var4.printStackTrace();
         } catch (InvocationTargetException var5) {
            Throwable t = var5.getTargetException();
            if (t != null && t.getClass() == (class$java$lang$IllegalStateException == null ? (class$java$lang$IllegalStateException = class$("java.lang.IllegalStateException")) : class$java$lang$IllegalStateException)) {
               this.running = true;
            } else {
               var5.printStackTrace();
            }
         }
      }

   }

   private void removeShutdownHook() {
      if (this.removeShutdownHookMethod != null && this.added && !this.running) {
         Object[] args = new Object[]{this.destroyProcessThread};

         try {
            Boolean removed = (Boolean)this.removeShutdownHookMethod.invoke(Runtime.getRuntime(), args);
            if (!removed) {
               System.err.println("Could not remove shutdown hook");
            }
         } catch (IllegalAccessException var5) {
            var5.printStackTrace();
         } catch (InvocationTargetException var6) {
            Throwable t = var6.getTargetException();
            if (t != null && t.getClass() == (class$java$lang$IllegalStateException == null ? (class$java$lang$IllegalStateException = class$("java.lang.IllegalStateException")) : class$java$lang$IllegalStateException)) {
               this.running = true;
            } else {
               var6.printStackTrace();
            }
         }

         this.destroyProcessThread.setShouldDestroy(false);
         if (!this.destroyProcessThread.getThreadGroup().isDestroyed()) {
            this.destroyProcessThread.start();
         }

         try {
            this.destroyProcessThread.join(20000L);
         } catch (InterruptedException var4) {
         }

         this.destroyProcessThread = null;
         this.added = false;
      }

   }

   public boolean isAddedAsShutdownHook() {
      return this.added;
   }

   public boolean add(Process process) {
      synchronized(this.processes) {
         if (this.processes.size() == 0) {
            this.addShutdownHook();
         }

         this.processes.addElement(process);
         return this.processes.contains(process);
      }
   }

   public boolean remove(Process process) {
      synchronized(this.processes) {
         boolean processRemoved = this.processes.removeElement(process);
         if (processRemoved && this.processes.size() == 0) {
            this.removeShutdownHook();
         }

         return processRemoved;
      }
   }

   public void run() {
      synchronized(this.processes) {
         this.running = true;
         Enumeration e = this.processes.elements();

         while(e.hasMoreElements()) {
            ((Process)e.nextElement()).destroy();
         }

      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   private class ProcessDestroyerImpl extends Thread {
      private boolean shouldDestroy = true;

      public ProcessDestroyerImpl() {
         super("ProcessDestroyer Shutdown Hook");
      }

      public void run() {
         if (this.shouldDestroy) {
            ProcessDestroyer.this.run();
         }

      }

      public void setShouldDestroy(boolean shouldDestroy) {
         this.shouldDestroy = shouldDestroy;
      }
   }
}

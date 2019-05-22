package org.apache.commons.beanutils;

import java.util.Map;
import java.util.WeakHashMap;

public class ContextClassLoaderLocal {
   private Map valueByClassLoader = new WeakHashMap();
   private boolean globalValueInitialized = false;
   private Object globalValue;

   protected Object initialValue() {
      return null;
   }

   public synchronized Object get() {
      this.valueByClassLoader.isEmpty();

      try {
         ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
         if (contextClassLoader != null) {
            Object value = this.valueByClassLoader.get(contextClassLoader);
            if (value == null && !this.valueByClassLoader.containsKey(contextClassLoader)) {
               value = this.initialValue();
               this.valueByClassLoader.put(contextClassLoader, value);
            }

            return value;
         }
      } catch (SecurityException var3) {
      }

      if (!this.globalValueInitialized) {
         this.globalValue = this.initialValue();
         this.globalValueInitialized = true;
      }

      return this.globalValue;
   }

   public synchronized void set(Object value) {
      this.valueByClassLoader.isEmpty();

      try {
         ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
         if (contextClassLoader != null) {
            this.valueByClassLoader.put(contextClassLoader, value);
            return;
         }
      } catch (SecurityException var3) {
      }

      this.globalValue = value;
      this.globalValueInitialized = true;
   }

   public synchronized void unset() {
      try {
         ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
         this.unset(contextClassLoader);
      } catch (SecurityException var2) {
      }

   }

   public synchronized void unset(ClassLoader classLoader) {
      this.valueByClassLoader.remove(classLoader);
   }
}

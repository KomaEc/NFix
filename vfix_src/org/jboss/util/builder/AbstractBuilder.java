package org.jboss.util.builder;

import java.security.PrivilegedAction;

public class AbstractBuilder<T> implements PrivilegedAction<T> {
   private Class<T> factoryClass;
   private String defaultFactory;

   public AbstractBuilder(Class<T> factoryClass, String defaultFactory) {
      if (factoryClass == null) {
         throw new IllegalArgumentException("Null factory class");
      } else if (defaultFactory == null) {
         throw new IllegalArgumentException("Null default factory");
      } else {
         this.factoryClass = factoryClass;
         this.defaultFactory = defaultFactory;
      }
   }

   public T run() {
      Object object;
      try {
         String className = System.getProperty(this.factoryClass.getName(), this.defaultFactory);
         Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
         object = clazz.newInstance();
      } catch (Throwable var10) {
         throw new RuntimeException("Error constructing " + this.factoryClass.getName(), var10);
      }

      try {
         T retObj = this.factoryClass.cast(object);
         return retObj;
      } catch (ClassCastException var9) {
         String objClassName = object.getClass().getName();
         ClassLoader objLoader = object.getClass().getClassLoader();
         String factoryClassName = this.factoryClass.getName();
         ClassLoader factoryLoader = this.factoryClass.getClassLoader();
         String msg = "Cannot cast object '" + objClassName + "' to factory '" + factoryClassName + "'\n" + "  factoryLoader: " + factoryLoader + "\n" + "  objLoader: " + objLoader;
         throw new RuntimeException(msg);
      }
   }
}

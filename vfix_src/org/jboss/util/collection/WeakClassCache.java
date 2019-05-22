package org.jboss.util.collection;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class WeakClassCache<T> {
   protected final Map<ClassLoader, Map<String, WeakReference<T>>> cache = new WeakHashMap();

   public T get(Class<?> clazz) {
      if (clazz == null) {
         throw new IllegalArgumentException("Null class");
      } else {
         Map<String, WeakReference<T>> classLoaderCache = this.getClassLoaderCache(clazz.getClassLoader());
         WeakReference<T> weak = (WeakReference)classLoaderCache.get(clazz.getName());
         Object result;
         if (weak != null) {
            result = weak.get();
            if (result != null) {
               return result;
            }
         }

         result = this.instantiate(clazz);
         weak = new WeakReference(result);
         classLoaderCache.put(clazz.getName(), weak);
         this.generate(clazz, result);
         return result;
      }
   }

   public T get(String name, ClassLoader cl) throws ClassNotFoundException {
      if (name == null) {
         throw new IllegalArgumentException("Null name");
      } else if (cl == null) {
         throw new IllegalArgumentException("Null classloader");
      } else {
         Class<?> clazz = cl.loadClass(name);
         return this.get(clazz);
      }
   }

   protected abstract T instantiate(Class<?> var1);

   protected abstract void generate(Class<?> var1, T var2);

   protected Map<String, WeakReference<T>> getClassLoaderCache(ClassLoader cl) {
      synchronized(this.cache) {
         Map<String, WeakReference<T>> result = (Map)this.cache.get(cl);
         if (result == null) {
            result = CollectionsFactory.createConcurrentReaderMap();
            this.cache.put(cl, result);
         }

         return result;
      }
   }
}

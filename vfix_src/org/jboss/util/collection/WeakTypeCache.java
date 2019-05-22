package org.jboss.util.collection;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class WeakTypeCache<T> {
   private Map<ClassLoader, Map<String, T>> cache = new WeakHashMap();

   public T get(Type type) {
      if (type == null) {
         throw new IllegalArgumentException("Null type");
      } else if (type instanceof ParameterizedType) {
         return this.getParameterizedType((ParameterizedType)type);
      } else if (type instanceof Class) {
         return this.getClass((Class)type);
      } else if (type instanceof TypeVariable) {
         return this.getTypeVariable((TypeVariable)type);
      } else if (type instanceof GenericArrayType) {
         return this.getGenericArrayType((GenericArrayType)type);
      } else if (type instanceof WildcardType) {
         return this.getWildcardType((WildcardType)type);
      } else {
         throw new UnsupportedOperationException("Unknown type: " + type + " class=" + type.getClass());
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

   protected abstract T instantiate(ParameterizedType var1);

   protected abstract void generate(ParameterizedType var1, T var2);

   protected T getParameterizedType(ParameterizedType type) {
      T result = this.peek(type);
      if (result != null) {
         return result;
      } else {
         result = this.instantiate(type);
         this.put(type, result);
         this.generate(type, result);
         return result;
      }
   }

   protected T getWildcardType(WildcardType type) {
      return this.get(type.getUpperBounds()[0]);
   }

   protected <D extends GenericDeclaration> T getTypeVariable(TypeVariable<D> type) {
      return this.get(type.getBounds()[0]);
   }

   protected T getGenericArrayType(GenericArrayType type) {
      return this.get(Object[].class);
   }

   protected T peek(ParameterizedType type) {
      Class<?> rawType = (Class)type.getRawType();
      ClassLoader cl = SecurityActions.getClassLoader(rawType);
      Map<String, T> classLoaderCache = this.getClassLoaderCache(cl);
      synchronized(classLoaderCache) {
         return classLoaderCache.get(type.toString());
      }
   }

   protected void put(ParameterizedType type, T result) {
      Class<?> rawType = (Class)type.getRawType();
      ClassLoader cl = SecurityActions.getClassLoader(rawType);
      Map<String, T> classLoaderCache = this.getClassLoaderCache(cl);
      synchronized(classLoaderCache) {
         classLoaderCache.put(type.toString(), result);
      }
   }

   protected T getClass(Class<?> clazz) {
      T result = this.peek(clazz);
      if (result != null) {
         return result;
      } else {
         result = this.instantiate(clazz);
         this.put(clazz, result);
         this.generate(clazz, result);
         return result;
      }
   }

   protected T peek(Class<?> clazz) {
      ClassLoader cl = SecurityActions.getClassLoader(clazz);
      Map<String, T> classLoaderCache = this.getClassLoaderCache(cl);
      synchronized(classLoaderCache) {
         return classLoaderCache.get(clazz.getName());
      }
   }

   protected void put(Class<?> clazz, T result) {
      ClassLoader cl = SecurityActions.getClassLoader(clazz);
      Map<String, T> classLoaderCache = this.getClassLoaderCache(cl);
      synchronized(classLoaderCache) {
         classLoaderCache.put(clazz.getName(), result);
      }
   }

   protected Map<String, T> getClassLoaderCache(ClassLoader cl) {
      synchronized(this.cache) {
         Map<String, T> result = (Map)this.cache.get(cl);
         if (result == null) {
            result = new WeakValueHashMap();
            this.cache.put(cl, result);
         }

         return (Map)result;
      }
   }
}

package org.apache.velocity.util.introspection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.velocity.runtime.log.Log;

public class ClassMap {
   private static final boolean debugReflection = false;
   private final Log log;
   private final Class clazz;
   private final ClassMap.MethodCache methodCache;
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$java$lang$Byte;
   // $FF: synthetic field
   static Class class$java$lang$Character;
   // $FF: synthetic field
   static Class class$java$lang$Double;
   // $FF: synthetic field
   static Class class$java$lang$Float;
   // $FF: synthetic field
   static Class class$java$lang$Integer;
   // $FF: synthetic field
   static Class class$java$lang$Long;
   // $FF: synthetic field
   static Class class$java$lang$Short;

   public ClassMap(Class clazz, Log log) {
      this.clazz = clazz;
      this.log = log;
      this.methodCache = new ClassMap.MethodCache(log);
      this.populateMethodCache();
   }

   public Class getCachedClass() {
      return this.clazz;
   }

   public Method findMethod(String name, Object[] params) throws MethodMap.AmbiguousException {
      return this.methodCache.get(name, params);
   }

   private void populateMethodCache() {
      List classesToReflect = new ArrayList();

      for(Class classToReflect = this.getCachedClass(); classToReflect != null; classToReflect = classToReflect.getSuperclass()) {
         if (Modifier.isPublic(classToReflect.getModifiers())) {
            classesToReflect.add(classToReflect);
         }

         Class[] interfaces = classToReflect.getInterfaces();

         for(int i = 0; i < interfaces.length; ++i) {
            if (Modifier.isPublic(interfaces[i].getModifiers())) {
               classesToReflect.add(interfaces[i]);
            }
         }
      }

      Iterator it = classesToReflect.iterator();

      while(it.hasNext()) {
         Class classToReflect = (Class)it.next();

         try {
            Method[] methods = classToReflect.getMethods();

            for(int i = 0; i < methods.length; ++i) {
               int modifiers = methods[i].getModifiers();
               if (Modifier.isPublic(modifiers) && (classToReflect.isInterface() || !Modifier.isAbstract(modifiers))) {
                  this.methodCache.put(methods[i]);
               }
            }
         } catch (SecurityException var7) {
            if (this.log.isDebugEnabled()) {
               this.log.debug("While accessing methods of " + classToReflect + ": ", var7);
            }
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

   private static final class MethodCache {
      private static final ClassMap.MethodCache.CacheMiss CACHE_MISS = new ClassMap.MethodCache.CacheMiss();
      private static final Object OBJECT = new Object();
      private static final Map convertPrimitives = new HashMap();
      private final Log log;
      private final Map cache;
      private final MethodMap methodMap;

      private MethodCache(Log log) {
         this.cache = new HashMap();
         this.methodMap = new MethodMap();
         this.log = log;
      }

      public synchronized Method get(String name, Object[] params) throws MethodMap.AmbiguousException {
         String methodKey = this.makeMethodKey(name, params);
         Object cacheEntry = this.cache.get(methodKey);
         if (cacheEntry == CACHE_MISS) {
            return null;
         } else {
            if (cacheEntry == null) {
               try {
                  cacheEntry = this.methodMap.find(name, params);
               } catch (MethodMap.AmbiguousException var6) {
                  this.cache.put(methodKey, CACHE_MISS);
                  throw var6;
               }

               this.cache.put(methodKey, cacheEntry != null ? cacheEntry : CACHE_MISS);
            }

            return (Method)cacheEntry;
         }
      }

      public synchronized void put(Method method) {
         String methodKey = this.makeMethodKey(method);
         if (this.cache.get(methodKey) == null) {
            this.cache.put(methodKey, method);
            this.methodMap.add(method);
         }

      }

      private String makeMethodKey(Method method) {
         Class[] parameterTypes = method.getParameterTypes();
         StringBuffer methodKey = new StringBuffer(method.getName());

         for(int j = 0; j < parameterTypes.length; ++j) {
            if (parameterTypes[j].isPrimitive()) {
               methodKey.append((String)convertPrimitives.get(parameterTypes[j]));
            } else {
               methodKey.append(parameterTypes[j].getName());
            }
         }

         return methodKey.toString();
      }

      private String makeMethodKey(String method, Object[] params) {
         StringBuffer methodKey = (new StringBuffer()).append(method);

         for(int j = 0; j < params.length; ++j) {
            Object arg = params[j];
            if (arg == null) {
               arg = OBJECT;
            }

            methodKey.append(arg.getClass().getName());
         }

         return methodKey.toString();
      }

      // $FF: synthetic method
      MethodCache(Log x0, Object x1) {
         this(x0);
      }

      static {
         convertPrimitives.put(Boolean.TYPE, (ClassMap.class$java$lang$Boolean == null ? (ClassMap.class$java$lang$Boolean = ClassMap.class$("java.lang.Boolean")) : ClassMap.class$java$lang$Boolean).getName());
         convertPrimitives.put(Byte.TYPE, (ClassMap.class$java$lang$Byte == null ? (ClassMap.class$java$lang$Byte = ClassMap.class$("java.lang.Byte")) : ClassMap.class$java$lang$Byte).getName());
         convertPrimitives.put(Character.TYPE, (ClassMap.class$java$lang$Character == null ? (ClassMap.class$java$lang$Character = ClassMap.class$("java.lang.Character")) : ClassMap.class$java$lang$Character).getName());
         convertPrimitives.put(Double.TYPE, (ClassMap.class$java$lang$Double == null ? (ClassMap.class$java$lang$Double = ClassMap.class$("java.lang.Double")) : ClassMap.class$java$lang$Double).getName());
         convertPrimitives.put(Float.TYPE, (ClassMap.class$java$lang$Float == null ? (ClassMap.class$java$lang$Float = ClassMap.class$("java.lang.Float")) : ClassMap.class$java$lang$Float).getName());
         convertPrimitives.put(Integer.TYPE, (ClassMap.class$java$lang$Integer == null ? (ClassMap.class$java$lang$Integer = ClassMap.class$("java.lang.Integer")) : ClassMap.class$java$lang$Integer).getName());
         convertPrimitives.put(Long.TYPE, (ClassMap.class$java$lang$Long == null ? (ClassMap.class$java$lang$Long = ClassMap.class$("java.lang.Long")) : ClassMap.class$java$lang$Long).getName());
         convertPrimitives.put(Short.TYPE, (ClassMap.class$java$lang$Short == null ? (ClassMap.class$java$lang$Short = ClassMap.class$("java.lang.Short")) : ClassMap.class$java$lang$Short).getName());
      }

      private static final class CacheMiss {
         private CacheMiss() {
         }

         // $FF: synthetic method
         CacheMiss(Object x0) {
            this();
         }
      }
   }
}

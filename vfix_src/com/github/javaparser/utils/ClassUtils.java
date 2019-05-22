package com.github.javaparser.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ClassUtils {
   private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap();
   private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap;

   public static boolean isPrimitiveOrWrapper(final Class<?> type) {
      if (type == null) {
         return false;
      } else {
         return type.isPrimitive() || isPrimitiveWrapper(type);
      }
   }

   public static boolean isPrimitiveWrapper(final Class<?> type) {
      return wrapperPrimitiveMap.containsKey(type);
   }

   static {
      primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
      primitiveWrapperMap.put(Byte.TYPE, Byte.class);
      primitiveWrapperMap.put(Character.TYPE, Character.class);
      primitiveWrapperMap.put(Short.TYPE, Short.class);
      primitiveWrapperMap.put(Integer.TYPE, Integer.class);
      primitiveWrapperMap.put(Long.TYPE, Long.class);
      primitiveWrapperMap.put(Double.TYPE, Double.class);
      primitiveWrapperMap.put(Float.TYPE, Float.class);
      primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
      wrapperPrimitiveMap = new HashMap();
      Iterator var0 = primitiveWrapperMap.keySet().iterator();

      while(var0.hasNext()) {
         Class<?> primitiveClass = (Class)var0.next();
         Class<?> wrapperClass = (Class)primitiveWrapperMap.get(primitiveClass);
         if (!primitiveClass.equals(wrapperClass)) {
            wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
         }
      }

   }
}

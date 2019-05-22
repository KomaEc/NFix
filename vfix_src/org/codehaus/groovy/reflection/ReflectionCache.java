package org.codehaus.groovy.reflection;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.groovy.util.TripleKeyHashMap;

public class ReflectionCache {
   private static Map primitiveTypesMap = new HashMap();
   static TripleKeyHashMap mopNames;
   static final CachedClass STRING_CLASS;
   public static final CachedClass OBJECT_CLASS;
   public static final CachedClass OBJECT_ARRAY_CLASS;

   public static Class autoboxType(Class type) {
      Class res = (Class)primitiveTypesMap.get(type);
      return res == null ? type : res;
   }

   public static String getMOPMethodName(CachedClass declaringClass, String name, boolean useThis) {
      TripleKeyHashMap.Entry mopNameEntry = mopNames.getOrPut(declaringClass, name, useThis);
      if (mopNameEntry.value == null) {
         mopNameEntry.value = (useThis ? "this$" : "super$") + declaringClass.getSuperClassDistance() + "$" + name;
      }

      return (String)mopNameEntry.value;
   }

   public static boolean isArray(Class klazz) {
      return klazz.getName().charAt(0) == '[';
   }

   static void setAssignableFrom(Class klazz, Class aClass) {
   }

   public static boolean isAssignableFrom(Class klazz, Class aClass) {
      return klazz == aClass ? true : klazz.isAssignableFrom(aClass);
   }

   static boolean arrayContentsEq(Object[] a1, Object[] a2) {
      if (a1 != null) {
         if (a2 == null) {
            return a1.length == 0;
         } else if (a1.length != a2.length) {
            return false;
         } else {
            for(int i = 0; i < a1.length; ++i) {
               if (a1[i] != a2[i]) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return a2 == null || a2.length == 0;
      }
   }

   public static CachedClass getCachedClass(Class klazz) {
      return klazz == null ? null : ClassInfo.getClassInfo(klazz).getCachedClass();
   }

   static {
      primitiveTypesMap.put(Byte.TYPE, Byte.class);
      primitiveTypesMap.put(Boolean.TYPE, Boolean.class);
      primitiveTypesMap.put(Character.TYPE, Character.class);
      primitiveTypesMap.put(Double.TYPE, Double.class);
      primitiveTypesMap.put(Float.TYPE, Float.class);
      primitiveTypesMap.put(Integer.TYPE, Integer.class);
      primitiveTypesMap.put(Long.TYPE, Long.class);
      primitiveTypesMap.put(Short.TYPE, Short.class);
      mopNames = new TripleKeyHashMap();
      STRING_CLASS = getCachedClass(String.class);
      OBJECT_CLASS = getCachedClass(Object.class);
      OBJECT_ARRAY_CLASS = getCachedClass(Object[].class);
   }
}

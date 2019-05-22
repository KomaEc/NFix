package org.jf.dexlib2.analysis.reflection.util;

import com.google.common.collect.ImmutableBiMap;

public class ReflectionUtils {
   private static ImmutableBiMap<String, String> primitiveMap = ImmutableBiMap.builder().put("boolean", "Z").put("int", "I").put("long", "J").put("double", "D").put("void", "V").put("float", "F").put("char", "C").put("short", "S").put("byte", "B").build();

   public static String javaToDexName(String javaName) {
      if (javaName.charAt(0) == '[') {
         return javaName.replace('.', '/');
      } else {
         return primitiveMap.containsKey(javaName) ? (String)primitiveMap.get(javaName) : 'L' + javaName.replace('.', '/') + ';';
      }
   }

   public static String dexToJavaName(String dexName) {
      if (dexName.charAt(0) == '[') {
         return dexName.replace('/', '.');
      } else {
         return primitiveMap.inverse().containsKey(dexName) ? (String)primitiveMap.inverse().get(dexName) : dexName.replace('/', '.').substring(1, dexName.length() - 2);
      }
   }
}

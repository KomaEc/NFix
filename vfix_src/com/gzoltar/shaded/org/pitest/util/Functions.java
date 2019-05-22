package com.gzoltar.shaded.org.pitest.util;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;

public abstract class Functions {
   public static F<String, String> classNameToJVMClassName() {
      return new F<String, String>() {
         public String apply(String a) {
            return a.replace(".", "/");
         }
      };
   }

   public static F<String, String> jvmClassToClassName() {
      return new F<String, String>() {
         public String apply(String a) {
            return a.replace("/", ".");
         }
      };
   }

   public static F<Class<?>, String> classToName() {
      return new F<Class<?>, String>() {
         public String apply(Class<?> clazz) {
            return clazz.getName();
         }
      };
   }

   public static Predicate<String> startsWith(final String filter) {
      return new Predicate<String>() {
         public Boolean apply(String a) {
            return a.startsWith(filter);
         }
      };
   }

   public static <T extends Enum<T>> F<String, T> stringToEnum(final Class<T> clazz) {
      return new F<String, T>() {
         public T apply(String name) {
            return Enum.valueOf(clazz, name);
         }
      };
   }
}

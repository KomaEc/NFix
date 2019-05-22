package com.gzoltar.shaded.org.pitest.reflection;

import com.gzoltar.shaded.org.pitest.functional.FArray;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class Reflection {
   public static Method publicMethod(Class<?> clazz, Predicate<Method> p) {
      return (Method)publicMethods(clazz, p).iterator().next();
   }

   public static Set<Method> publicMethods(Class<?> clazz, Predicate<Method> p) {
      Set<Method> ms = new LinkedHashSet();
      FArray.filter(clazz.getMethods(), p, ms);
      return ms;
   }

   public static Set<Field> publicFields(Class<?> clazz) {
      Set<Field> fields = new LinkedHashSet();
      if (clazz != null) {
         fields.addAll(Arrays.asList(clazz.getFields()));
      }

      return fields;
   }

   public static Set<Method> allMethods(Class<?> c) {
      Set<Method> methods = new LinkedHashSet();
      if (c != null) {
         List<Method> locallyDeclaredMethods = Arrays.asList(c.getDeclaredMethods());
         methods.addAll(locallyDeclaredMethods);
         methods.addAll(allMethods(c.getSuperclass()));
      }

      return methods;
   }

   public static Method publicMethod(Class<? extends Object> clazz, final String name) {
      Predicate<Method> p = new Predicate<Method>() {
         public Boolean apply(Method a) {
            return a.getName().equals(name);
         }
      };
      return publicMethod(clazz, p);
   }
}

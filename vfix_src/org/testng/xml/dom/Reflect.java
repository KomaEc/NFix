package org.testng.xml.dom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.testng.collections.Lists;
import org.testng.internal.collections.Pair;

public class Reflect {
   public static List<Pair<Method, Wrapper>> findMethodsWithAnnotation(Class<?> c, Class<? extends Annotation> ac, Object bean) {
      List<Pair<Method, Wrapper>> result = Lists.newArrayList();
      Method[] arr$ = c.getMethods();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Method m = arr$[i$];
         Annotation a = m.getAnnotation(ac);
         if (a != null) {
            result.add(Pair.of(m, new Wrapper(a, bean)));
         }
      }

      return result;
   }

   public static Pair<Method, Wrapper> findSetterForTag(Class<?> c, String tagName, Object bean) {
      List<Class<? extends Annotation>> annotations = Arrays.asList(OnElement.class, OnElementList.class, Tag.class);
      Iterator i$ = annotations.iterator();

      while(i$.hasNext()) {
         Class<? extends Annotation> annotationClass = (Class)i$.next();
         List<Pair<Method, Wrapper>> methods = findMethodsWithAnnotation(c, annotationClass, bean);
         Iterator i$ = methods.iterator();

         while(i$.hasNext()) {
            Pair<Method, Wrapper> pair = (Pair)i$.next();
            if (((Wrapper)pair.second()).getTagName().equals(tagName)) {
               return pair;
            }
         }
      }

      String[] arr$ = new String[]{"add", "set"};
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String prefix = arr$[i$];
         Method[] arr$ = c.getDeclaredMethods();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method m = arr$[i$];
            String methodName = toCamelCase(tagName, prefix);
            if (m.getName().equals(methodName)) {
               return Pair.of(m, (Object)null);
            }
         }
      }

      return null;
   }

   private static String toCamelCase(String name, String prefix) {
      return prefix + toCapitalizedCamelCase(name);
   }

   public static String toCapitalizedCamelCase(String name) {
      StringBuilder result = new StringBuilder(name.substring(0, 1).toUpperCase());

      for(int i = 1; i < name.length(); ++i) {
         if (name.charAt(i) == '-') {
            result.append(Character.toUpperCase(name.charAt(i + 1)));
            ++i;
         } else {
            result.append(name.charAt(i));
         }
      }

      return result.toString();
   }
}

package org.codehaus.groovy.reflection;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtils {
   private static final Set<String> IGNORED_PACKAGES = new HashSet();
   private static final Method MAGIC_METHOD;

   public static boolean isCallingClassReflectionAvailable() {
      return MAGIC_METHOD != null;
   }

   public static Class getCallingClass() {
      return getCallingClass(1);
   }

   public static Class getCallingClass(int matchLevel) {
      return getCallingClass(matchLevel, Collections.EMPTY_SET);
   }

   public static Class getCallingClass(int matchLevel, Collection<String> extraIgnoredPackages) {
      if (MAGIC_METHOD == null) {
         return null;
      } else {
         int var2 = 0;

         try {
            Class c;
            Class sc;
            do {
               do {
                  do {
                     c = (Class)MAGIC_METHOD.invoke((Object)null, var2++);
                     if (c != null) {
                        sc = c.getSuperclass();
                     } else {
                        sc = null;
                     }
                  } while(classShouldBeIgnored(c, extraIgnoredPackages));
               } while(superClassShouldBeIgnored(sc));
            } while(c != null && matchLevel-- > 0);

            return c;
         } catch (Throwable var5) {
            return null;
         }
      }
   }

   private static boolean superClassShouldBeIgnored(Class sc) {
      return sc != null && sc.getPackage() != null && "org.codehaus.groovy.runtime.callsite".equals(sc.getPackage().getName());
   }

   private static boolean classShouldBeIgnored(Class c, Collection<String> extraIgnoredPackages) {
      return c != null && (c.isSynthetic() || c.getPackage() != null && (IGNORED_PACKAGES.contains(c.getPackage().getName()) || extraIgnoredPackages.contains(c.getPackage().getName())));
   }

   static {
      IGNORED_PACKAGES.add("groovy.lang");
      IGNORED_PACKAGES.add("org.codehaus.groovy.reflection");
      IGNORED_PACKAGES.add("org.codehaus.groovy.runtime.callsite");
      IGNORED_PACKAGES.add("org.codehaus.groovy.runtime.metaclass");
      IGNORED_PACKAGES.add("org.codehaus.groovy.runtime");
      IGNORED_PACKAGES.add("sun.reflect");

      Method meth;
      try {
         Class srr = Class.forName("sun.reflect.Reflection");
         meth = srr.getMethod("getCallerClass", Integer.TYPE);
      } catch (Throwable var2) {
         meth = null;
      }

      MAGIC_METHOD = meth;
   }
}

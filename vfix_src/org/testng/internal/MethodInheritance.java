package org.testng.internal;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.testng.ITestNGMethod;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

public class MethodInheritance {
   private static List<ITestNGMethod> findMethodListSuperClass(Map<Class, List<ITestNGMethod>> map, Class<? extends ITestNGMethod> methodClass) {
      Iterator i$ = map.entrySet().iterator();

      Entry entry;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         entry = (Entry)i$.next();
      } while(!((Class)entry.getKey()).isAssignableFrom(methodClass));

      return (List)entry.getValue();
   }

   private static Class findSubClass(Map<Class, List<ITestNGMethod>> map, Class<? extends ITestNGMethod> methodClass) {
      Iterator i$ = map.keySet().iterator();

      Class cls;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         cls = (Class)i$.next();
      } while(!methodClass.isAssignableFrom(cls));

      return cls;
   }

   public static void fixMethodInheritance(ITestNGMethod[] methods, boolean before) {
      Map<Class, List<ITestNGMethod>> map = Maps.newHashMap();
      ITestNGMethod[] arr$ = methods;
      int len$ = methods.length;

      int i;
      ITestNGMethod m1;
      for(i = 0; i < len$; ++i) {
         m1 = arr$[i];
         Class<? extends ITestNGMethod> methodClass = m1.getRealClass();
         List<ITestNGMethod> l = findMethodListSuperClass(map, methodClass);
         if (null != l) {
            l.add(m1);
         } else {
            Class subClass = findSubClass(map, methodClass);
            if (null != subClass) {
               l = (List)map.get(subClass);
               l.add(m1);
               map.remove(subClass);
               map.put(methodClass, l);
            } else {
               l = Lists.newArrayList();
               l.add(m1);
               map.put(methodClass, l);
            }
         }
      }

      Iterator i$ = map.values().iterator();

      while(true) {
         List l;
         do {
            if (!i$.hasNext()) {
               return;
            }

            l = (List)i$.next();
         } while(l.size() <= 1);

         sortMethodsByInheritance(l, before);

         for(i = 0; i < l.size() - 1; ++i) {
            m1 = (ITestNGMethod)l.get(i);

            for(int j = i + 1; j < l.size(); ++j) {
               ITestNGMethod m2 = (ITestNGMethod)l.get(j);
               if (!equalsEffectiveClass(m1, m2) && !dependencyExists(m1, m2, methods)) {
                  Utils.log("MethodInheritance", 4, m2 + " DEPENDS ON " + m1);
                  m2.addMethodDependedUpon(MethodHelper.calculateMethodCanonicalName(m1));
               }
            }
         }
      }
   }

   private static boolean dependencyExists(ITestNGMethod m1, ITestNGMethod m2, ITestNGMethod[] methods) {
      return internalDependencyExists(m1, m2, methods) || internalDependencyExists(m2, m1, methods);
   }

   private static boolean internalDependencyExists(ITestNGMethod m1, ITestNGMethod m2, ITestNGMethod[] methods) {
      ITestNGMethod[] methodsNamed = MethodHelper.findDependedUponMethods(m1, methods);
      ITestNGMethod[] arr$ = methodsNamed;
      int len$ = methodsNamed.length;

      int i$;
      for(i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod method = arr$[i$];
         if (method.equals(m2)) {
            return true;
         }
      }

      String[] arr$ = m1.getGroupsDependedUpon();
      len$ = arr$.length;

      for(i$ = 0; i$ < len$; ++i$) {
         String group = arr$[i$];
         ITestNGMethod[] methodsThatBelongToGroup = MethodGroupsHelper.findMethodsThatBelongToGroup(m1, methods, group);
         ITestNGMethod[] arr$ = methodsThatBelongToGroup;
         int len$ = methodsThatBelongToGroup.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ITestNGMethod method = arr$[i$];
            if (method.equals(m2)) {
               return true;
            }
         }
      }

      return false;
   }

   private static boolean equalsEffectiveClass(ITestNGMethod m1, ITestNGMethod m2) {
      try {
         Class c1 = m1.getRealClass();
         Class c2 = m2.getRealClass();
         return c1 == null ? c2 == null : c1.equals(c2);
      } catch (Exception var4) {
         return false;
      }
   }

   private static void sortMethodsByInheritance(List<ITestNGMethod> methods, boolean baseClassToChild) {
      Collections.sort(methods);
      if (!baseClassToChild) {
         Collections.reverse(methods);
      }

   }
}

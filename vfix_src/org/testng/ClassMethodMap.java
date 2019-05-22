package org.testng;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.internal.XmlMethodSelector;

public class ClassMethodMap {
   private Map<Object, List<ITestNGMethod>> m_classMap = Maps.newHashMap();
   private Map<ITestClass, Set<Object>> m_beforeClassMethods = Maps.newHashMap();
   private Map<ITestClass, Set<Object>> m_afterClassMethods = Maps.newHashMap();

   public ClassMethodMap(List<ITestNGMethod> methods, XmlMethodSelector xmlMethodSelector) {
      Iterator i$ = methods.iterator();

      while(true) {
         ITestNGMethod m;
         do {
            if (!i$.hasNext()) {
               return;
            }

            m = (ITestNGMethod)i$.next();
         } while(xmlMethodSelector != null && !xmlMethodSelector.includeMethod((IMethodSelectorContext)null, m, true));

         Object instance = m.getInstance();
         List<ITestNGMethod> l = (List)this.m_classMap.get(instance);
         if (l == null) {
            l = Lists.newArrayList();
            this.m_classMap.put(instance, l);
         }

         l.add(m);
      }
   }

   public synchronized boolean removeAndCheckIfLast(ITestNGMethod m, Object instance) {
      List<ITestNGMethod> l = (List)this.m_classMap.get(instance);
      if (l != null) {
         l.remove(m);
         Iterator i$ = l.iterator();

         ITestNGMethod tm;
         do {
            if (!i$.hasNext()) {
               return true;
            }

            tm = (ITestNGMethod)i$.next();
         } while(!tm.getEnabled() || !tm.getTestClass().equals(m.getTestClass()));

         return false;
      } else {
         throw new AssertionError("l should not be null");
      }
   }

   private Class<?> getMethodClass(ITestNGMethod m) {
      return m.getTestClass().getRealClass();
   }

   public Map<ITestClass, Set<Object>> getInvokedBeforeClassMethods() {
      return this.m_beforeClassMethods;
   }

   public Map<ITestClass, Set<Object>> getInvokedAfterClassMethods() {
      return this.m_afterClassMethods;
   }

   public void clear() {
      Iterator i$;
      Set instances;
      for(i$ = this.m_beforeClassMethods.values().iterator(); i$.hasNext(); instances = null) {
         instances = (Set)i$.next();
         instances.clear();
      }

      for(i$ = this.m_afterClassMethods.values().iterator(); i$.hasNext(); instances = null) {
         instances = (Set)i$.next();
         instances.clear();
      }

      this.m_beforeClassMethods.clear();
      this.m_afterClassMethods.clear();
   }
}

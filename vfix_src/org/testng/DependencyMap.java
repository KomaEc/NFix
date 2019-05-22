package org.testng;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.testng.collections.ListMultiMap;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

public class DependencyMap {
   private ListMultiMap<String, ITestNGMethod> m_dependencies = Maps.newListMultiMap();
   private ListMultiMap<String, ITestNGMethod> m_groups = Maps.newListMultiMap();

   public DependencyMap(ITestNGMethod[] methods) {
      ITestNGMethod[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod m = arr$[i$];
         this.m_dependencies.put(m.getRealClass().getName() + "." + m.getMethodName(), m);
         String[] arr$ = m.getGroups();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String g = arr$[i$];
            this.m_groups.put(g, m);
         }
      }

   }

   public List<ITestNGMethod> getMethodsThatBelongTo(String group, ITestNGMethod fromMethod) {
      Set<String> uniqueKeys = this.m_groups.keySet();
      List<ITestNGMethod> result = Lists.newArrayList();
      Iterator i$ = uniqueKeys.iterator();

      while(i$.hasNext()) {
         String k = (String)i$.next();
         if (Pattern.matches(group, k)) {
            List<ITestNGMethod> temp = (List)this.m_groups.get(k);
            if (temp != null) {
               result.addAll(this.m_groups.get(k));
            }
         }
      }

      if (result.isEmpty() && !fromMethod.ignoreMissingDependencies()) {
         throw new TestNGException("DependencyMap::Method \"" + fromMethod + "\" depends on nonexistent group \"" + group + "\"");
      } else {
         return result;
      }
   }

   public ITestNGMethod getMethodDependingOn(String methodName, ITestNGMethod fromMethod) {
      List<ITestNGMethod> l = (List)this.m_dependencies.get(methodName);
      if (l == null && fromMethod.ignoreMissingDependencies()) {
         return fromMethod;
      } else {
         Iterator i$ = l.iterator();

         ITestNGMethod m;
         do {
            if (!i$.hasNext()) {
               throw new TestNGException("Method \"" + fromMethod + "\" depends on nonexistent method \"" + methodName + "\"");
            }

            m = (ITestNGMethod)i$.next();
            if (!fromMethod.getRealClass().isAssignableFrom(m.getRealClass())) {
               return m;
            }
         } while(m.getInstance() != fromMethod.getInstance());

         return m;
      }
   }
}

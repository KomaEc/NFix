package org.testng;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

class InstanceOrderingMethodInterceptor implements IMethodInterceptor {
   public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
      return this.groupMethodsByInstance(methods);
   }

   private List<IMethodInstance> groupMethodsByInstance(List<IMethodInstance> methods) {
      List<Object> instanceList = Lists.newArrayList();
      Map<Object, List<IMethodInstance>> map = Maps.newHashMap();
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         IMethodInstance mi = (IMethodInstance)i$.next();
         Object[] methodInstances = mi.getInstances();
         Object[] arr$ = methodInstances;
         int len$ = methodInstances.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Object instance = arr$[i$];
            if (!instanceList.contains(instance)) {
               instanceList.add(instance);
            }

            List<IMethodInstance> l = (List)map.get(instance);
            if (l == null) {
               l = Lists.newArrayList();
               map.put(instance, l);
            }

            l.add(mi);
         }
      }

      List<IMethodInstance> result = Lists.newArrayList();
      Iterator i$ = instanceList.iterator();

      while(i$.hasNext()) {
         Object instance = i$.next();
         result.addAll((Collection)map.get(instance));
      }

      return result;
   }
}

package org.testng;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.testng.internal.MethodInstance;

class PreserveOrderMethodInterceptor implements IMethodInterceptor {
   private void p(List<IMethodInstance> methods, String s) {
      System.out.println("[PreserveOrderMethodInterceptor] " + s);
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         IMethodInstance mi = (IMethodInstance)i$.next();
         System.out.println("  " + mi.getMethod().getMethodName() + " index:" + mi.getMethod().getTestClass().getXmlClass().getIndex());
      }

   }

   public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
      Collections.sort(methods, MethodInstance.SORT_BY_INDEX);
      return methods;
   }
}

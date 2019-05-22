package org.testng.internal;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.testng.IMethodSelector;
import org.testng.IMethodSelectorContext;
import org.testng.ITestNGMethod;
import org.testng.collections.Lists;

public class RunInfo implements Serializable {
   private static final long serialVersionUID = -9085221672822562888L;
   private transient List<MethodSelectorDescriptor> m_methodSelectors = Lists.newArrayList();

   public void addMethodSelector(IMethodSelector selector, int priority) {
      Utils.log("RunInfo", 3, "Adding method selector: " + selector + " priority: " + priority);
      MethodSelectorDescriptor md = new MethodSelectorDescriptor(selector, priority);
      this.m_methodSelectors.add(md);
   }

   public boolean includeMethod(ITestNGMethod tm, boolean isTestMethod) {
      Collections.sort(this.m_methodSelectors);
      boolean foundNegative = false;
      IMethodSelectorContext context = new DefaultMethodSelectorContext();
      boolean result = false;
      Iterator i$ = this.m_methodSelectors.iterator();

      while(i$.hasNext()) {
         MethodSelectorDescriptor mds = (MethodSelectorDescriptor)i$.next();
         if (!foundNegative) {
            foundNegative = mds.getPriority() < 0;
         }

         if (foundNegative && mds.getPriority() >= 0) {
            break;
         }

         IMethodSelector md = mds.getMethodSelector();
         result = md.includeMethod(context, tm, isTestMethod);
         if (context.isStopped()) {
            return result;
         }
      }

      return result;
   }

   public static void ppp(String s) {
      System.out.println("[RunInfo] " + s);
   }

   public void setTestMethods(List<ITestNGMethod> testMethods) {
      Iterator i$ = this.m_methodSelectors.iterator();

      while(i$.hasNext()) {
         MethodSelectorDescriptor mds = (MethodSelectorDescriptor)i$.next();
         mds.setTestMethods(testMethods);
      }

   }
}

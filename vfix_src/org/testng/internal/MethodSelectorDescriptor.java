package org.testng.internal;

import java.util.List;
import org.testng.IMethodSelector;
import org.testng.ITestNGMethod;

public class MethodSelectorDescriptor implements Comparable<MethodSelectorDescriptor> {
   private IMethodSelector m_methodSelector;
   private int m_priority;

   public int getPriority() {
      return this.m_priority;
   }

   public IMethodSelector getMethodSelector() {
      return this.m_methodSelector;
   }

   public MethodSelectorDescriptor(IMethodSelector selector, int priority) {
      this.m_methodSelector = selector;
      this.m_priority = priority;
   }

   public int compareTo(MethodSelectorDescriptor other) {
      int result = 0;

      try {
         int p1 = this.getPriority();
         int p2 = other.getPriority();
         result = p1 - p2;
      } catch (Exception var5) {
      }

      return result;
   }

   public void setTestMethods(List<ITestNGMethod> testMethods) {
      this.m_methodSelector.setTestMethods(testMethods);
   }
}

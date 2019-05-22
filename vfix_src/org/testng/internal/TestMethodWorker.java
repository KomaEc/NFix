package org.testng.internal;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.ClassMethodMap;
import org.testng.IMethodInstance;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.collections.Lists;
import org.testng.internal.thread.ThreadUtil;
import org.testng.internal.thread.graph.IWorker;
import org.testng.xml.XmlSuite;

public class TestMethodWorker implements IWorker<ITestNGMethod> {
   private IMethodInstance[] m_methodInstances;
   private final IInvoker m_invoker;
   private final Map<String, String> m_parameters;
   private final XmlSuite m_suite;
   private List<ITestResult> m_testResults = Lists.newArrayList();
   private final ConfigurationGroupMethods m_groupMethods;
   private final ClassMethodMap m_classMethodMap;
   private final ITestContext m_testContext;

   public TestMethodWorker(IInvoker invoker, IMethodInstance[] testMethods, XmlSuite suite, Map<String, String> parameters, ConfigurationGroupMethods groupMethods, ClassMethodMap classMethodMap, ITestContext testContext) {
      this.m_invoker = invoker;
      this.m_methodInstances = testMethods;
      this.m_suite = suite;
      this.m_parameters = parameters;
      this.m_groupMethods = groupMethods;
      this.m_classMethodMap = classMethodMap;
      this.m_testContext = testContext;
   }

   public long getTimeOut() {
      long result = 0L;
      IMethodInstance[] arr$ = this.m_methodInstances;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         IMethodInstance mi = arr$[i$];
         ITestNGMethod tm = mi.getMethod();
         if (tm.getTimeOut() > result) {
            result = tm.getTimeOut();
         }
      }

      return result;
   }

   public String toString() {
      StringBuilder result = new StringBuilder("[Worker thread:" + Thread.currentThread().getId() + " priority:" + this.getPriority() + " ");
      IMethodInstance[] arr$ = this.m_methodInstances;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         IMethodInstance m = arr$[i$];
         result.append(m.getMethod()).append(" ");
      }

      result.append("]");
      return result.toString();
   }

   public void run() {
      IMethodInstance[] arr$ = this.m_methodInstances;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         IMethodInstance testMthdInst = arr$[i$];
         ITestNGMethod testMethod = testMthdInst.getMethod();
         ITestClass testClass = testMethod.getTestClass();
         this.invokeBeforeClassMethods(testClass, testMthdInst);

         try {
            this.invokeTestMethods(testMethod, testMthdInst.getInstance(), this.m_testContext);
         } finally {
            this.invokeAfterClassMethods(testClass, testMthdInst);
         }
      }

   }

   protected void invokeTestMethods(ITestNGMethod tm, Object instance, ITestContext testContext) {
      List<ITestResult> testResults = this.m_invoker.invokeTestMethods(tm, this.m_suite, this.m_parameters, this.m_groupMethods, instance, testContext);
      if (testResults != null) {
         this.m_testResults.addAll(testResults);
      }

   }

   protected void invokeBeforeClassMethods(ITestClass testClass, IMethodInstance mi) {
      if (null != this.m_classMethodMap && null != this.m_classMethodMap.getInvokedBeforeClassMethods()) {
         ITestNGMethod[] classMethods = testClass.getBeforeClassMethods();
         if (null != classMethods && classMethods.length != 0) {
            Map<ITestClass, Set<Object>> invokedBeforeClassMethods = this.m_classMethodMap.getInvokedBeforeClassMethods();
            synchronized(testClass) {
               Set<Object> instances = (Set)invokedBeforeClassMethods.get(testClass);
               if (null == instances) {
                  instances = new HashSet();
                  invokedBeforeClassMethods.put(testClass, instances);
               }

               Object[] arr$ = mi.getInstances();
               int len$ = arr$.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  Object instance = arr$[i$];
                  if (!((Set)instances).contains(instance)) {
                     ((Set)instances).add(instance);
                     this.m_invoker.invokeConfigurations(testClass, testClass.getBeforeClassMethods(), this.m_suite, this.m_parameters, (Object[])null, instance);
                  }
               }

            }
         }
      }
   }

   protected void invokeAfterClassMethods(ITestClass testClass, IMethodInstance mi) {
      if (null != this.m_classMethodMap && null != this.m_classMethodMap.getInvokedAfterClassMethods()) {
         ITestNGMethod[] afterClassMethods = testClass.getAfterClassMethods();
         if (null != afterClassMethods && afterClassMethods.length != 0) {
            List<Object> invokeInstances = Lists.newArrayList();
            ITestNGMethod tm = mi.getMethod();
            if (this.m_classMethodMap.removeAndCheckIfLast(tm, mi.getInstance())) {
               Map<ITestClass, Set<Object>> invokedAfterClassMethods = this.m_classMethodMap.getInvokedAfterClassMethods();
               Object inst;
               synchronized(invokedAfterClassMethods) {
                  inst = (Set)invokedAfterClassMethods.get(testClass);
                  if (null == inst) {
                     inst = new HashSet();
                     invokedAfterClassMethods.put(testClass, inst);
                  }

                  Object[] arr$ = mi.getInstances();
                  int len$ = arr$.length;

                  for(int i$ = 0; i$ < len$; ++i$) {
                     Object inst = arr$[i$];
                     if (!((Set)inst).contains(inst)) {
                        invokeInstances.add(inst);
                     }
                  }
               }

               Iterator i$ = invokeInstances.iterator();

               while(i$.hasNext()) {
                  inst = i$.next();
                  this.m_invoker.invokeConfigurations(testClass, afterClassMethods, this.m_suite, this.m_parameters, (Object[])null, inst);
               }
            }

         }
      }
   }

   protected int indexOf(ITestNGMethod tm, ITestNGMethod[] allTestMethods) {
      for(int i = 0; i < allTestMethods.length; ++i) {
         if (allTestMethods[i] == tm) {
            return i;
         }
      }

      return -1;
   }

   public List<ITestResult> getTestResults() {
      return this.m_testResults;
   }

   private void ppp(String s) {
      Utils.log("TestMethodWorker", 2, ThreadUtil.currentThreadInfo() + ":" + s);
   }

   public List<ITestNGMethod> getTasks() {
      List<ITestNGMethod> result = Lists.newArrayList();
      IMethodInstance[] arr$ = this.m_methodInstances;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         IMethodInstance m = arr$[i$];
         result.add(m.getMethod());
      }

      return result;
   }

   public int compareTo(IWorker<ITestNGMethod> other) {
      return this.getPriority() - other.getPriority();
   }

   public int getPriority() {
      return this.m_methodInstances.length > 0 ? this.m_methodInstances[0].getMethod().getPriority() : 0;
   }
}

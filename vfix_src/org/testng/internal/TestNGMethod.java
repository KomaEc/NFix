package org.testng.internal;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.annotations.AnnotationHelper;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlTest;

public class TestNGMethod extends BaseTestMethod implements Serializable {
   private static final long serialVersionUID = -1742868891986775307L;
   private int m_threadPoolSize;
   private int m_invocationCount;
   private int m_totalInvocationCount;
   private int m_successPercentage;
   public static final Comparator<ITestNGMethod> SORT_BY_CLASS = new Comparator<ITestNGMethod>() {
      public int compare(ITestNGMethod o1, ITestNGMethod o2) {
         String c1 = o1.getTestClass().getName();
         String c2 = o2.getTestClass().getName();
         return c1.compareTo(c2);
      }
   };

   public TestNGMethod(Method method, IAnnotationFinder finder, XmlTest xmlTest, Object instance) {
      this(method, finder, true, xmlTest, instance);
   }

   private TestNGMethod(Method method, IAnnotationFinder finder, boolean initialize, XmlTest xmlTest, Object instance) {
      super(method.getName(), method, finder, instance);
      this.m_threadPoolSize = 0;
      this.m_invocationCount = 1;
      this.m_totalInvocationCount = this.m_invocationCount;
      this.m_successPercentage = 100;
      if (initialize) {
         this.init(xmlTest);
      }

   }

   public int getInvocationCount() {
      return this.m_invocationCount;
   }

   public int getTotalInvocationCount() {
      return this.m_totalInvocationCount;
   }

   public int getSuccessPercentage() {
      return this.m_successPercentage;
   }

   public boolean isTest() {
      return true;
   }

   private void ppp(String s) {
      System.out.println("[TestNGMethod] " + s);
   }

   private void init(XmlTest xmlTest) {
      this.setXmlTest(xmlTest);
      this.setInvocationNumbers(xmlTest.getInvocationNumbers(this.m_method.getDeclaringClass().getName() + "." + this.m_method.getName()));
      ITestAnnotation testAnnotation = AnnotationHelper.findTest(this.getAnnotationFinder(), this.m_method.getMethod());
      if (testAnnotation == null) {
         testAnnotation = AnnotationHelper.findTest(this.getAnnotationFinder(), this.m_method.getDeclaringClass());
      }

      if (null != testAnnotation) {
         this.setTimeOut(testAnnotation.getTimeOut());
         this.m_successPercentage = testAnnotation.getSuccessPercentage();
         this.setInvocationCount(testAnnotation.getInvocationCount());
         this.m_totalInvocationCount = testAnnotation.getInvocationCount();
         this.setThreadPoolSize(testAnnotation.getThreadPoolSize());
         this.setAlwaysRun(testAnnotation.getAlwaysRun());
         this.setDescription(this.findDescription(testAnnotation, xmlTest));
         this.setEnabled(testAnnotation.getEnabled());
         this.setRetryAnalyzer(testAnnotation.getRetryAnalyzer());
         this.setSkipFailedInvocations(testAnnotation.skipFailedInvocations());
         this.setInvocationTimeOut(testAnnotation.invocationTimeOut());
         this.setIgnoreMissingDependencies(testAnnotation.ignoreMissingDependencies());
         this.setPriority(testAnnotation.getPriority());
      }

      this.initGroups(ITestAnnotation.class);
   }

   private String findDescription(ITestAnnotation testAnnotation, XmlTest xmlTest) {
      String result = testAnnotation.getDescription();
      if (result == null) {
         List<XmlClass> classes = xmlTest.getXmlClasses();
         Iterator i$ = classes.iterator();

         while(true) {
            XmlClass c;
            do {
               if (!i$.hasNext()) {
                  return result;
               }

               c = (XmlClass)i$.next();
            } while(!c.getName().equals(this.m_method.getMethod().getDeclaringClass().getName()));

            Iterator i$ = c.getIncludedMethods().iterator();

            while(i$.hasNext()) {
               XmlInclude include = (XmlInclude)i$.next();
               if (include.getName().equals(this.m_method.getName())) {
                  result = include.getDescription();
                  if (result != null) {
                     break;
                  }
               }
            }
         }
      } else {
         return result;
      }
   }

   public int getThreadPoolSize() {
      return this.m_threadPoolSize;
   }

   public void setThreadPoolSize(int threadPoolSize) {
      this.m_threadPoolSize = threadPoolSize;
   }

   public void setInvocationCount(int counter) {
      this.m_invocationCount = counter;
   }

   public BaseTestMethod clone() {
      TestNGMethod clone = new TestNGMethod(this.getMethod(), this.getAnnotationFinder(), false, this.getXmlTest(), this.getInstance());
      ITestClass tc = this.getTestClass();
      NoOpTestClass testClass = new NoOpTestClass(tc);
      testClass.setBeforeTestMethods(this.clone(tc.getBeforeTestMethods()));
      testClass.setAfterTestMethod(this.clone(tc.getAfterTestMethods()));
      clone.m_testClass = testClass;
      clone.setDate(this.getDate());
      clone.setGroups(this.getGroups());
      clone.setGroupsDependedUpon(this.getGroupsDependedUpon(), Collections.emptyList());
      clone.setMethodsDependedUpon(this.getMethodsDependedUpon());
      clone.setAlwaysRun(this.isAlwaysRun());
      clone.m_beforeGroups = this.getBeforeGroups();
      clone.m_afterGroups = this.getAfterGroups();
      clone.m_currentInvocationCount = this.m_currentInvocationCount;
      clone.setMissingGroup(this.getMissingGroup());
      clone.setThreadPoolSize(this.getThreadPoolSize());
      clone.setDescription(this.getDescription());
      clone.setEnabled(this.getEnabled());
      clone.setParameterInvocationCount(this.getParameterInvocationCount());
      clone.setInvocationCount(this.getInvocationCount());
      clone.m_totalInvocationCount = this.getTotalInvocationCount();
      clone.m_successPercentage = this.getSuccessPercentage();
      clone.setTimeOut(this.getTimeOut());
      clone.setRetryAnalyzer(this.getRetryAnalyzer());
      clone.setSkipFailedInvocations(this.skipFailedInvocations());
      clone.setInvocationNumbers(this.getInvocationNumbers());
      clone.setPriority(this.getPriority());
      return clone;
   }

   private ITestNGMethod[] clone(ITestNGMethod[] sources) {
      ITestNGMethod[] clones = new ITestNGMethod[sources.length];

      for(int i = 0; i < sources.length; ++i) {
         clones[i] = sources[i].clone();
      }

      return clones;
   }
}

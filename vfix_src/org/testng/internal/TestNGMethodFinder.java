package org.testng.internal;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.testng.ITestMethodFinder;
import org.testng.ITestNGMethod;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.collections.Lists;
import org.testng.internal.annotations.AnnotationHelper;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlTest;

public class TestNGMethodFinder implements ITestMethodFinder {
   private static final int BEFORE_SUITE = 1;
   private static final int AFTER_SUITE = 2;
   private static final int BEFORE_TEST = 3;
   private static final int AFTER_TEST = 4;
   private static final int BEFORE_CLASS = 5;
   private static final int AFTER_CLASS = 6;
   private static final int BEFORE_TEST_METHOD = 7;
   private static final int AFTER_TEST_METHOD = 8;
   private static final int BEFORE_GROUPS = 9;
   private static final int AFTER_GROUPS = 10;
   private RunInfo m_runInfo = null;
   private IAnnotationFinder m_annotationFinder = null;

   public TestNGMethodFinder(RunInfo runInfo, IAnnotationFinder annotationFinder) {
      this.m_runInfo = runInfo;
      this.m_annotationFinder = annotationFinder;
   }

   public ITestNGMethod[] getTestMethods(Class<?> clazz, XmlTest xmlTest) {
      return AnnotationHelper.findMethodsWithAnnotation(clazz, ITestAnnotation.class, this.m_annotationFinder, xmlTest);
   }

   public ITestNGMethod[] getBeforeClassMethods(Class cls) {
      return this.findConfiguration(cls, 5);
   }

   public ITestNGMethod[] getAfterClassMethods(Class cls) {
      return this.findConfiguration(cls, 6);
   }

   public ITestNGMethod[] getBeforeTestMethods(Class cls) {
      return this.findConfiguration(cls, 7);
   }

   public ITestNGMethod[] getAfterTestMethods(Class cls) {
      return this.findConfiguration(cls, 8);
   }

   public ITestNGMethod[] getBeforeSuiteMethods(Class cls) {
      return this.findConfiguration(cls, 1);
   }

   public ITestNGMethod[] getAfterSuiteMethods(Class cls) {
      return this.findConfiguration(cls, 2);
   }

   public ITestNGMethod[] getBeforeTestConfigurationMethods(Class clazz) {
      return this.findConfiguration(clazz, 3);
   }

   public ITestNGMethod[] getAfterTestConfigurationMethods(Class clazz) {
      return this.findConfiguration(clazz, 4);
   }

   public ITestNGMethod[] getBeforeGroupsConfigurationMethods(Class clazz) {
      return this.findConfiguration(clazz, 9);
   }

   public ITestNGMethod[] getAfterGroupsConfigurationMethods(Class clazz) {
      return this.findConfiguration(clazz, 10);
   }

   private ITestNGMethod[] findConfiguration(Class clazz, int configurationType) {
      List<ITestNGMethod> vResult = Lists.newArrayList();
      Set<Method> methods = ClassHelper.getAvailableMethods(clazz);
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         Method m = (Method)i$.next();
         IConfigurationAnnotation configuration = AnnotationHelper.findConfiguration(this.m_annotationFinder, m);
         if (null != configuration) {
            boolean create = false;
            boolean isBeforeSuite = false;
            boolean isAfterSuite = false;
            boolean isBeforeTest = false;
            boolean isAfterTest = false;
            boolean isBeforeClass = false;
            boolean isAfterClass = false;
            boolean isBeforeTestMethod = false;
            boolean isAfterTestMethod = false;
            String[] beforeGroups = null;
            String[] afterGroups = null;
            switch(configurationType) {
            case 1:
               create = configuration.getBeforeSuite();
               isBeforeSuite = true;
               break;
            case 2:
               create = configuration.getAfterSuite();
               isAfterSuite = true;
               break;
            case 3:
               create = configuration.getBeforeTest();
               isBeforeTest = true;
               break;
            case 4:
               create = configuration.getAfterTest();
               isAfterTest = true;
               break;
            case 5:
               create = configuration.getBeforeTestClass();
               isBeforeClass = true;
               break;
            case 6:
               create = configuration.getAfterTestClass();
               isAfterClass = true;
               break;
            case 7:
               create = configuration.getBeforeTestMethod();
               isBeforeTestMethod = true;
               break;
            case 8:
               create = configuration.getAfterTestMethod();
               isAfterTestMethod = true;
               break;
            case 9:
               beforeGroups = configuration.getBeforeGroups();
               create = beforeGroups.length > 0;
               isBeforeTestMethod = true;
               break;
            case 10:
               afterGroups = configuration.getAfterGroups();
               create = afterGroups.length > 0;
               isBeforeTestMethod = true;
            }

            if (create) {
               this.addConfigurationMethod(clazz, vResult, m, isBeforeSuite, isAfterSuite, isBeforeTest, isAfterTest, isBeforeClass, isAfterClass, isBeforeTestMethod, isAfterTestMethod, beforeGroups, afterGroups, (Object)null);
            }
         }
      }

      List<ITestNGMethod> excludedMethods = Lists.newArrayList();
      boolean unique = configurationType == 1 || configurationType == 2;
      ITestNGMethod[] tmResult = MethodHelper.collectAndOrderMethods(Lists.newArrayList((Collection)vResult), false, this.m_runInfo, this.m_annotationFinder, unique, excludedMethods);
      return tmResult;
   }

   private void addConfigurationMethod(Class<?> clazz, List<ITestNGMethod> results, Method method, boolean isBeforeSuite, boolean isAfterSuite, boolean isBeforeTest, boolean isAfterTest, boolean isBeforeClass, boolean isAfterClass, boolean isBeforeTestMethod, boolean isAfterTestMethod, String[] beforeGroups, String[] afterGroups, Object instance) {
      if (method.getDeclaringClass().isAssignableFrom(clazz)) {
         ITestNGMethod confMethod = new ConfigurationMethod(new ConstructorOrMethod(method), this.m_annotationFinder, isBeforeSuite, isAfterSuite, isBeforeTest, isAfterTest, isBeforeClass, isAfterClass, isBeforeTestMethod, isAfterTestMethod, beforeGroups, afterGroups, instance);
         results.add(confMethod);
      }

   }
}

package org.testng;

import java.lang.reflect.Method;
import java.util.List;
import org.testng.collections.Lists;
import org.testng.collections.Objects;
import org.testng.internal.ConfigurationMethod;
import org.testng.internal.NoOpTestClass;
import org.testng.internal.RunInfo;
import org.testng.internal.TestNGMethod;
import org.testng.internal.Utils;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

class TestClass extends NoOpTestClass implements ITestClass {
   private static final long serialVersionUID = -8077917128278361294L;
   private transient IAnnotationFinder m_annotationFinder = null;
   private transient ITestMethodFinder m_testMethodFinder = null;
   private IClass m_iClass = null;
   private RunInfo m_runInfo = null;
   private String m_testName;
   private XmlTest m_xmlTest;
   private XmlClass m_xmlClass;

   protected TestClass(IClass cls, ITestMethodFinder testMethodFinder, IAnnotationFinder annotationFinder, RunInfo runInfo, XmlTest xmlTest, XmlClass xmlClass) {
      this.init(cls, testMethodFinder, annotationFinder, runInfo, xmlTest, xmlClass);
   }

   public String getTestName() {
      return this.m_testName;
   }

   public XmlTest getXmlTest() {
      return this.m_xmlTest;
   }

   public XmlClass getXmlClass() {
      return this.m_xmlClass;
   }

   public IAnnotationFinder getAnnotationFinder() {
      return this.m_annotationFinder;
   }

   private void init(IClass cls, ITestMethodFinder testMethodFinder, IAnnotationFinder annotationFinder, RunInfo runInfo, XmlTest xmlTest, XmlClass xmlClass) {
      this.log(3, "Creating TestClass for " + cls);
      this.m_iClass = cls;
      this.m_testClass = cls.getRealClass();
      this.m_xmlTest = xmlTest;
      this.m_xmlClass = xmlClass;
      this.m_runInfo = runInfo;
      this.m_testMethodFinder = testMethodFinder;
      this.m_annotationFinder = annotationFinder;
      this.initTestClassesAndInstances();
      this.initMethods();
   }

   private void initTestClassesAndInstances() {
      Object[] instances = this.getInstances(false);
      Object[] arr$ = instances;
      int len$ = instances.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Object instance = arr$[i$];
         if (instance instanceof ITest) {
            this.m_testName = ((ITest)instance).getTestName();
            break;
         }
      }

   }

   public Object[] getInstances(boolean create) {
      return this.m_iClass.getInstances(create);
   }

   public long[] getInstanceHashCodes() {
      return this.m_iClass.getInstanceHashCodes();
   }

   public int getInstanceCount() {
      return this.m_iClass.getInstanceCount();
   }

   public void addInstance(Object instance) {
      this.m_iClass.addInstance(instance);
   }

   private void initMethods() {
      ITestNGMethod[] methods = this.m_testMethodFinder.getTestMethods(this.m_testClass, this.m_xmlTest);
      this.m_testMethods = this.createTestMethods(methods);
      Object[] arr$ = this.m_iClass.getInstances(false);
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Object instance = arr$[i$];
         this.m_beforeSuiteMethods = ConfigurationMethod.createSuiteConfigurationMethods(this.m_testMethodFinder.getBeforeSuiteMethods(this.m_testClass), this.m_annotationFinder, true, instance);
         this.m_afterSuiteMethods = ConfigurationMethod.createSuiteConfigurationMethods(this.m_testMethodFinder.getAfterSuiteMethods(this.m_testClass), this.m_annotationFinder, false, instance);
         this.m_beforeTestConfMethods = ConfigurationMethod.createTestConfigurationMethods(this.m_testMethodFinder.getBeforeTestConfigurationMethods(this.m_testClass), this.m_annotationFinder, true, instance);
         this.m_afterTestConfMethods = ConfigurationMethod.createTestConfigurationMethods(this.m_testMethodFinder.getAfterTestConfigurationMethods(this.m_testClass), this.m_annotationFinder, false, instance);
         this.m_beforeClassMethods = ConfigurationMethod.createClassConfigurationMethods(this.m_testMethodFinder.getBeforeClassMethods(this.m_testClass), this.m_annotationFinder, true, instance);
         this.m_afterClassMethods = ConfigurationMethod.createClassConfigurationMethods(this.m_testMethodFinder.getAfterClassMethods(this.m_testClass), this.m_annotationFinder, false, instance);
         this.m_beforeGroupsMethods = ConfigurationMethod.createBeforeConfigurationMethods(this.m_testMethodFinder.getBeforeGroupsConfigurationMethods(this.m_testClass), this.m_annotationFinder, true, instance);
         this.m_afterGroupsMethods = ConfigurationMethod.createAfterConfigurationMethods(this.m_testMethodFinder.getAfterGroupsConfigurationMethods(this.m_testClass), this.m_annotationFinder, false, instance);
         this.m_beforeTestMethods = ConfigurationMethod.createTestMethodConfigurationMethods(this.m_testMethodFinder.getBeforeTestMethods(this.m_testClass), this.m_annotationFinder, true, instance);
         this.m_afterTestMethods = ConfigurationMethod.createTestMethodConfigurationMethods(this.m_testMethodFinder.getAfterTestMethods(this.m_testClass), this.m_annotationFinder, false, instance);
      }

   }

   private ITestNGMethod[] createTestMethods(ITestNGMethod[] methods) {
      List<ITestNGMethod> vResult = Lists.newArrayList();
      ITestNGMethod[] result = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod tm = result[i$];
         Method m = tm.getMethod();
         if (m.getDeclaringClass().isAssignableFrom(this.m_testClass)) {
            Object[] arr$ = this.m_iClass.getInstances(false);
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Object o = arr$[i$];
               this.log(4, "Adding method " + tm + " on TestClass " + this.m_testClass);
               vResult.add(new TestNGMethod(m, this.m_annotationFinder, this.m_xmlTest, o));
            }
         } else {
            this.log(4, "Rejecting method " + tm + " for TestClass " + this.m_testClass);
         }
      }

      result = (ITestNGMethod[])vResult.toArray(new ITestNGMethod[vResult.size()]);
      return result;
   }

   private RunInfo getRunInfo() {
      return this.m_runInfo;
   }

   public ITestMethodFinder getTestMethodFinder() {
      return this.m_testMethodFinder;
   }

   private void log(int level, String s) {
      Utils.log("TestClass", level, s);
   }

   private static void ppp(String s) {
      System.out.println("[TestClass] " + s);
   }

   protected void dump() {
      System.out.println("===== Test class\n" + this.m_testClass.getName());
      ITestNGMethod[] arr$ = this.m_beforeClassMethods;
      int len$ = arr$.length;

      int i$;
      ITestNGMethod m;
      for(i$ = 0; i$ < len$; ++i$) {
         m = arr$[i$];
         System.out.println("  @BeforeClass " + m);
      }

      arr$ = this.m_beforeTestMethods;
      len$ = arr$.length;

      for(i$ = 0; i$ < len$; ++i$) {
         m = arr$[i$];
         System.out.println("  @BeforeMethod " + m);
      }

      arr$ = this.m_testMethods;
      len$ = arr$.length;

      for(i$ = 0; i$ < len$; ++i$) {
         m = arr$[i$];
         System.out.println("    @Test " + m);
      }

      arr$ = this.m_afterTestMethods;
      len$ = arr$.length;

      for(i$ = 0; i$ < len$; ++i$) {
         m = arr$[i$];
         System.out.println("  @AfterMethod " + m);
      }

      arr$ = this.m_afterClassMethods;
      len$ = arr$.length;

      for(i$ = 0; i$ < len$; ++i$) {
         m = arr$[i$];
         System.out.println("  @AfterClass " + m);
      }

      System.out.println("======");
   }

   public String toString() {
      return Objects.toStringHelper(this.getClass()).add("name", (Object)this.m_testClass).toString();
   }

   public IClass getIClass() {
      return this.m_iClass;
   }
}

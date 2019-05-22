package org.testng.internal;

import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

public class NoOpTestClass implements ITestClass {
   private static final long serialVersionUID = -4544061405329040593L;
   protected Class m_testClass = null;
   protected ITestNGMethod[] m_beforeClassMethods = null;
   protected ITestNGMethod[] m_beforeTestMethods = null;
   protected ITestNGMethod[] m_testMethods = null;
   protected ITestNGMethod[] m_afterClassMethods = null;
   protected ITestNGMethod[] m_afterTestMethods = null;
   protected ITestNGMethod[] m_beforeSuiteMethods = null;
   protected ITestNGMethod[] m_afterSuiteMethods = null;
   protected ITestNGMethod[] m_beforeTestConfMethods = null;
   protected ITestNGMethod[] m_afterTestConfMethods = null;
   protected ITestNGMethod[] m_beforeGroupsMethods = null;
   protected ITestNGMethod[] m_afterGroupsMethods = null;
   private transient Object[] m_instances;
   private long[] m_instanceHashes;
   private XmlTest m_xmlTest;
   private XmlClass m_xmlClass;

   protected NoOpTestClass() {
   }

   public NoOpTestClass(ITestClass testClass) {
      this.m_testClass = testClass.getRealClass();
      this.m_beforeSuiteMethods = testClass.getBeforeSuiteMethods();
      this.m_beforeTestConfMethods = testClass.getBeforeTestConfigurationMethods();
      this.m_beforeGroupsMethods = testClass.getBeforeGroupsMethods();
      this.m_beforeClassMethods = testClass.getBeforeClassMethods();
      this.m_beforeTestMethods = testClass.getBeforeTestMethods();
      this.m_afterSuiteMethods = testClass.getAfterSuiteMethods();
      this.m_afterTestConfMethods = testClass.getAfterTestConfigurationMethods();
      this.m_afterGroupsMethods = testClass.getAfterGroupsMethods();
      this.m_afterClassMethods = testClass.getAfterClassMethods();
      this.m_afterTestMethods = testClass.getAfterTestMethods();
      this.m_instances = testClass.getInstances(true);
      this.m_instanceHashes = testClass.getInstanceHashCodes();
      this.m_xmlTest = testClass.getXmlTest();
      this.m_xmlClass = testClass.getXmlClass();
   }

   public void setBeforeTestMethods(ITestNGMethod[] beforeTestMethods) {
      this.m_beforeTestMethods = beforeTestMethods;
   }

   public void setAfterTestMethod(ITestNGMethod[] afterTestMethods) {
      this.m_afterTestMethods = afterTestMethods;
   }

   public ITestNGMethod[] getAfterClassMethods() {
      return this.m_afterClassMethods;
   }

   public ITestNGMethod[] getAfterTestMethods() {
      return this.m_afterTestMethods;
   }

   public ITestNGMethod[] getBeforeClassMethods() {
      return this.m_beforeClassMethods;
   }

   public ITestNGMethod[] getBeforeTestMethods() {
      return this.m_beforeTestMethods;
   }

   public ITestNGMethod[] getTestMethods() {
      return this.m_testMethods;
   }

   public ITestNGMethod[] getBeforeSuiteMethods() {
      return this.m_beforeSuiteMethods;
   }

   public ITestNGMethod[] getAfterSuiteMethods() {
      return this.m_afterSuiteMethods;
   }

   public ITestNGMethod[] getBeforeTestConfigurationMethods() {
      return this.m_beforeTestConfMethods;
   }

   public ITestNGMethod[] getAfterTestConfigurationMethods() {
      return this.m_afterTestConfMethods;
   }

   public ITestNGMethod[] getBeforeGroupsMethods() {
      return this.m_beforeGroupsMethods;
   }

   public ITestNGMethod[] getAfterGroupsMethods() {
      return this.m_afterGroupsMethods;
   }

   public int getInstanceCount() {
      return this.m_instances.length;
   }

   public long[] getInstanceHashCodes() {
      return this.m_instanceHashes;
   }

   public Object[] getInstances(boolean reuse) {
      return this.m_instances;
   }

   public String getName() {
      return this.m_testClass.getName();
   }

   public Class getRealClass() {
      return this.m_testClass;
   }

   public void addInstance(Object instance) {
   }

   public void setTestClass(Class<?> declaringClass) {
      this.m_testClass = declaringClass;
   }

   public String getTestName() {
      return null;
   }

   public XmlTest getXmlTest() {
      return this.m_xmlTest;
   }

   public XmlClass getXmlClass() {
      return this.m_xmlClass;
   }
}

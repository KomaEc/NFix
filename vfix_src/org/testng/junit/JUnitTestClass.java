package org.testng.junit;

import java.util.List;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.collections.Lists;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

public abstract class JUnitTestClass implements ITestClass {
   private static final long serialVersionUID = 405598615794850925L;
   private List<ITestNGMethod> m_testMethods = Lists.newArrayList();
   private List<ITestNGMethod> m_beforeClass = Lists.newArrayList();
   private List<ITestNGMethod> m_afterClass = Lists.newArrayList();
   private List<ITestNGMethod> m_beforeTest = Lists.newArrayList();
   private List<ITestNGMethod> m_afterTest = Lists.newArrayList();
   private Class m_realClass;
   private Object[] m_instances;
   private long[] m_instanceHashes;
   private XmlTest m_xmlTest;
   private XmlClass m_xmlClass;
   private static final ITestNGMethod[] EMPTY_METHODARRAY = new ITestNGMethod[0];

   public JUnitTestClass(Class test) {
      this.m_realClass = test;
      this.m_instances = new Object[]{test};
      this.m_instanceHashes = new long[]{(long)test.hashCode()};
   }

   List<ITestNGMethod> getTestMethodList() {
      return this.m_testMethods;
   }

   public void addInstance(Object instance) {
      throw new IllegalStateException("addInstance is not supported for JUnit");
   }

   public String getName() {
      return this.m_realClass.getName();
   }

   public Class getRealClass() {
      return this.m_realClass;
   }

   public String getTestName() {
      return this.m_xmlTest.getName();
   }

   public XmlTest getXmlTest() {
      return this.m_xmlTest;
   }

   public XmlClass getXmlClass() {
      return this.m_xmlClass;
   }

   public int getInstanceCount() {
      return 1;
   }

   public long[] getInstanceHashCodes() {
      return this.m_instanceHashes;
   }

   public Object[] getInstances(boolean reuse) {
      return this.m_instances;
   }

   public ITestNGMethod[] getTestMethods() {
      return (ITestNGMethod[])this.m_testMethods.toArray(new ITestNGMethod[this.m_testMethods.size()]);
   }

   public ITestNGMethod[] getBeforeTestMethods() {
      return (ITestNGMethod[])this.m_beforeTest.toArray(new ITestNGMethod[this.m_beforeTest.size()]);
   }

   public ITestNGMethod[] getAfterTestMethods() {
      return (ITestNGMethod[])this.m_afterTest.toArray(new ITestNGMethod[this.m_afterTest.size()]);
   }

   public ITestNGMethod[] getBeforeClassMethods() {
      return (ITestNGMethod[])this.m_beforeClass.toArray(new ITestNGMethod[this.m_beforeClass.size()]);
   }

   public ITestNGMethod[] getAfterClassMethods() {
      return (ITestNGMethod[])this.m_afterClass.toArray(new ITestNGMethod[this.m_afterClass.size()]);
   }

   public ITestNGMethod[] getBeforeSuiteMethods() {
      return EMPTY_METHODARRAY;
   }

   public ITestNGMethod[] getAfterSuiteMethods() {
      return EMPTY_METHODARRAY;
   }

   public ITestNGMethod[] getBeforeGroupsMethods() {
      return EMPTY_METHODARRAY;
   }

   public ITestNGMethod[] getAfterGroupsMethods() {
      return EMPTY_METHODARRAY;
   }

   public ITestNGMethod[] getBeforeTestConfigurationMethods() {
      return EMPTY_METHODARRAY;
   }

   public ITestNGMethod[] getAfterTestConfigurationMethods() {
      return EMPTY_METHODARRAY;
   }
}

package org.testng.internal;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.testng.IClass;
import org.testng.IRetryAnalyzer;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.collections.Lists;
import org.testng.xml.XmlTest;

public class ClonedMethod implements ITestNGMethod {
   private static final long serialVersionUID = 1L;
   private ITestNGMethod m_method;
   private transient Method m_javaMethod;
   private String m_id;
   private int m_currentInvocationCount;
   private long m_date;
   private List<Integer> m_invocationNumbers = Lists.newArrayList();
   private List<Integer> m_failedInvocationNumbers = Lists.newArrayList();

   public ClonedMethod(ITestNGMethod method, Method javaMethod) {
      this.m_method = method;
      this.m_javaMethod = javaMethod;
   }

   public void addMethodDependedUpon(String methodName) {
   }

   public boolean canRunFromClass(IClass testClass) {
      return this.m_method.canRunFromClass(testClass);
   }

   public String[] getAfterGroups() {
      return this.m_method.getAfterGroups();
   }

   public String[] getBeforeGroups() {
      return this.m_method.getBeforeGroups();
   }

   public int getCurrentInvocationCount() {
      return this.m_currentInvocationCount;
   }

   public long getDate() {
      return this.m_method.getDate();
   }

   public String getDescription() {
      return "";
   }

   public void setDescription(String description) {
      this.m_method.setDescription(description);
   }

   public boolean getEnabled() {
      return true;
   }

   public String[] getGroups() {
      return this.m_method.getGroups();
   }

   public String[] getGroupsDependedUpon() {
      return new String[0];
   }

   public String getId() {
      return this.m_id;
   }

   public long[] getInstanceHashCodes() {
      return this.m_method.getInstanceHashCodes();
   }

   public Object[] getInstances() {
      return this.m_method.getInstances();
   }

   public Object getInstance() {
      return this.m_method.getInstance();
   }

   public int getInvocationCount() {
      return 1;
   }

   public int getTotalInvocationCount() {
      return 1;
   }

   public long getInvocationTimeOut() {
      return this.m_method.getInvocationTimeOut();
   }

   public Method getMethod() {
      return this.m_javaMethod;
   }

   public String getMethodName() {
      return this.m_javaMethod.getName();
   }

   public String[] getMethodsDependedUpon() {
      return new String[0];
   }

   public String getMissingGroup() {
      return null;
   }

   public int getParameterInvocationCount() {
      return 1;
   }

   public Class getRealClass() {
      return this.m_javaMethod.getClass();
   }

   public IRetryAnalyzer getRetryAnalyzer() {
      return this.m_method.getRetryAnalyzer();
   }

   public int getSuccessPercentage() {
      return 100;
   }

   public ITestClass getTestClass() {
      return this.m_method.getTestClass();
   }

   public int getThreadPoolSize() {
      return this.m_method.getThreadPoolSize();
   }

   public long getTimeOut() {
      return this.m_method.getTimeOut();
   }

   public void setTimeOut(long timeOut) {
      this.m_method.setTimeOut(timeOut);
   }

   public boolean ignoreMissingDependencies() {
      return false;
   }

   public void incrementCurrentInvocationCount() {
      ++this.m_currentInvocationCount;
   }

   public boolean isAfterClassConfiguration() {
      return false;
   }

   public boolean isAfterGroupsConfiguration() {
      return false;
   }

   public boolean isAfterMethodConfiguration() {
      return false;
   }

   public boolean isAfterSuiteConfiguration() {
      return false;
   }

   public boolean isAfterTestConfiguration() {
      return false;
   }

   public boolean isAlwaysRun() {
      return false;
   }

   public boolean isBeforeClassConfiguration() {
      return false;
   }

   public boolean isBeforeGroupsConfiguration() {
      return false;
   }

   public boolean isBeforeMethodConfiguration() {
      return false;
   }

   public boolean isBeforeSuiteConfiguration() {
      return false;
   }

   public boolean isBeforeTestConfiguration() {
      return false;
   }

   public boolean isTest() {
      return true;
   }

   public void setDate(long date) {
      this.m_date = date;
   }

   public void setId(String id) {
      this.m_id = id;
   }

   public void setIgnoreMissingDependencies(boolean ignore) {
   }

   public void setInvocationCount(int count) {
   }

   public void setMissingGroup(String group) {
   }

   public void setParameterInvocationCount(int n) {
   }

   public void setRetryAnalyzer(IRetryAnalyzer retryAnalyzer) {
   }

   public void setSkipFailedInvocations(boolean skip) {
   }

   public void setTestClass(ITestClass cls) {
   }

   public void setThreadPoolSize(int threadPoolSize) {
   }

   public boolean skipFailedInvocations() {
      return false;
   }

   public int compareTo(Object o) {
      int result = -2;
      Class<?> thisClass = this.getRealClass();
      Class<?> otherClass = ((ITestNGMethod)o).getRealClass();
      if (thisClass.isAssignableFrom(otherClass)) {
         result = -1;
      } else if (otherClass.isAssignableFrom(thisClass)) {
         result = 1;
      } else if (this.equals(o)) {
         result = 0;
      }

      return result;
   }

   public ClonedMethod clone() {
      return new ClonedMethod(this.m_method, this.m_javaMethod);
   }

   public String toString() {
      Method m = this.getMethod();
      String cls = m.getDeclaringClass().getName();
      StringBuffer result = new StringBuffer(cls + "." + m.getName() + "(");
      int i = 0;
      Class[] arr$ = m.getParameterTypes();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class<?> p = arr$[i$];
         if (i++ > 0) {
            result.append(", ");
         }

         result.append(p.getName());
      }

      result.append(")");
      return result.toString();
   }

   public List<Integer> getInvocationNumbers() {
      return this.m_invocationNumbers;
   }

   public void setInvocationNumbers(List<Integer> count) {
      this.m_invocationNumbers = count;
   }

   public List<Integer> getFailedInvocationNumbers() {
      return this.m_failedInvocationNumbers;
   }

   public void addFailedInvocationNumber(int number) {
      this.m_failedInvocationNumbers.add(number);
   }

   public int getPriority() {
      return this.m_method.getPriority();
   }

   public void setPriority(int priority) {
   }

   public XmlTest getXmlTest() {
      return this.m_method.getXmlTest();
   }

   public ConstructorOrMethod getConstructorOrMethod() {
      return null;
   }

   public Map<String, String> findMethodParameters(XmlTest test) {
      return Collections.emptyMap();
   }
}

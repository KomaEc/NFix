package org.testng;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.testng.internal.ConstructorOrMethod;
import org.testng.xml.XmlTest;

public interface ITestNGMethod extends Comparable, Serializable, Cloneable {
   Class getRealClass();

   ITestClass getTestClass();

   void setTestClass(ITestClass var1);

   /** @deprecated */
   @Deprecated
   Method getMethod();

   String getMethodName();

   /** @deprecated */
   @Deprecated
   Object[] getInstances();

   Object getInstance();

   long[] getInstanceHashCodes();

   String[] getGroups();

   String[] getGroupsDependedUpon();

   String getMissingGroup();

   void setMissingGroup(String var1);

   String[] getBeforeGroups();

   String[] getAfterGroups();

   String[] getMethodsDependedUpon();

   void addMethodDependedUpon(String var1);

   boolean isTest();

   boolean isBeforeMethodConfiguration();

   boolean isAfterMethodConfiguration();

   boolean isBeforeClassConfiguration();

   boolean isAfterClassConfiguration();

   boolean isBeforeSuiteConfiguration();

   boolean isAfterSuiteConfiguration();

   boolean isBeforeTestConfiguration();

   boolean isAfterTestConfiguration();

   boolean isBeforeGroupsConfiguration();

   boolean isAfterGroupsConfiguration();

   long getTimeOut();

   void setTimeOut(long var1);

   int getInvocationCount();

   void setInvocationCount(int var1);

   int getTotalInvocationCount();

   int getSuccessPercentage();

   String getId();

   void setId(String var1);

   long getDate();

   void setDate(long var1);

   boolean canRunFromClass(IClass var1);

   boolean isAlwaysRun();

   int getThreadPoolSize();

   void setThreadPoolSize(int var1);

   boolean getEnabled();

   String getDescription();

   void setDescription(String var1);

   void incrementCurrentInvocationCount();

   int getCurrentInvocationCount();

   void setParameterInvocationCount(int var1);

   int getParameterInvocationCount();

   ITestNGMethod clone();

   IRetryAnalyzer getRetryAnalyzer();

   void setRetryAnalyzer(IRetryAnalyzer var1);

   boolean skipFailedInvocations();

   void setSkipFailedInvocations(boolean var1);

   long getInvocationTimeOut();

   boolean ignoreMissingDependencies();

   void setIgnoreMissingDependencies(boolean var1);

   List<Integer> getInvocationNumbers();

   void setInvocationNumbers(List<Integer> var1);

   void addFailedInvocationNumber(int var1);

   List<Integer> getFailedInvocationNumbers();

   int getPriority();

   void setPriority(int var1);

   XmlTest getXmlTest();

   ConstructorOrMethod getConstructorOrMethod();

   Map<String, String> findMethodParameters(XmlTest var1);
}

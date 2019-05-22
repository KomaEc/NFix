package org.testng.annotations;

import org.testng.IRetryAnalyzer;
import org.testng.internal.annotations.IDataProvidable;

public interface ITestAnnotation extends ITestOrConfiguration, IDataProvidable {
   int getInvocationCount();

   void setInvocationCount(int var1);

   int getThreadPoolSize();

   void setThreadPoolSize(int var1);

   int getSuccessPercentage();

   void setSuccessPercentage(int var1);

   boolean getAlwaysRun();

   void setAlwaysRun(boolean var1);

   Class<?>[] getExpectedExceptions();

   void setExpectedExceptions(Class<?>[] var1);

   String getExpectedExceptionsMessageRegExp();

   void setExpectedExceptionsMessageRegExp(String var1);

   String getSuiteName();

   void setSuiteName(String var1);

   String getTestName();

   void setTestName(String var1);

   boolean getSequential();

   void setSequential(boolean var1);

   boolean getSingleThreaded();

   void setSingleThreaded(boolean var1);

   String getDataProvider();

   void setDataProvider(String var1);

   Class<?> getDataProviderClass();

   void setDataProviderClass(Class<?> var1);

   IRetryAnalyzer getRetryAnalyzer();

   void setRetryAnalyzer(Class<?> var1);

   boolean skipFailedInvocations();

   void setSkipFailedInvocations(boolean var1);

   long invocationTimeOut();

   void setInvocationTimeOut(long var1);

   boolean ignoreMissingDependencies();

   void setIgnoreMissingDependencies(boolean var1);

   int getPriority();

   void setPriority(int var1);
}

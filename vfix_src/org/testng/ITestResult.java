package org.testng;

public interface ITestResult extends IAttributes, Comparable<ITestResult> {
   int SUCCESS = 1;
   int FAILURE = 2;
   int SKIP = 3;
   int SUCCESS_PERCENTAGE_FAILURE = 4;
   int STARTED = 16;

   int getStatus();

   void setStatus(int var1);

   ITestNGMethod getMethod();

   Object[] getParameters();

   void setParameters(Object[] var1);

   IClass getTestClass();

   Throwable getThrowable();

   void setThrowable(Throwable var1);

   long getStartMillis();

   long getEndMillis();

   void setEndMillis(long var1);

   String getName();

   boolean isSuccess();

   String getHost();

   Object getInstance();

   String getTestName();

   String getInstanceName();

   ITestContext getTestContext();
}

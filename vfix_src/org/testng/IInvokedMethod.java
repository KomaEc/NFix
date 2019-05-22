package org.testng;

public interface IInvokedMethod {
   boolean isTestMethod();

   boolean isConfigurationMethod();

   ITestNGMethod getTestMethod();

   ITestResult getTestResult();

   long getDate();
}

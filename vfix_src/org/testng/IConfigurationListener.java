package org.testng;

public interface IConfigurationListener extends ITestNGListener {
   void onConfigurationSuccess(ITestResult var1);

   void onConfigurationFailure(ITestResult var1);

   void onConfigurationSkip(ITestResult var1);
}

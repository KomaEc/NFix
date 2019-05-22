package org.testng;

public interface ITestListener extends ITestNGListener {
   void onTestStart(ITestResult var1);

   void onTestSuccess(ITestResult var1);

   void onTestFailure(ITestResult var1);

   void onTestSkipped(ITestResult var1);

   void onTestFailedButWithinSuccessPercentage(ITestResult var1);

   void onStart(ITestContext var1);

   void onFinish(ITestContext var1);
}

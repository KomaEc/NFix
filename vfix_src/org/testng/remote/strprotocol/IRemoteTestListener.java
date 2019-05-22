package org.testng.remote.strprotocol;

public interface IRemoteTestListener {
   void onStart(TestMessage var1);

   void onFinish(TestMessage var1);

   void onTestStart(TestResultMessage var1);

   void onTestSuccess(TestResultMessage var1);

   void onTestFailure(TestResultMessage var1);

   void onTestSkipped(TestResultMessage var1);

   void onTestFailedButWithinSuccessPercentage(TestResultMessage var1);
}

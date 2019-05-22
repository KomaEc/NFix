package org.testng;

public interface ISuiteListener extends ITestNGListener {
   void onStart(ISuite var1);

   void onFinish(ISuite var1);
}

package org.testng;

public interface IHookCallBack {
   void runTestMethod(ITestResult var1);

   Object[] getParameters();
}

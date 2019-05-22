package org.testng;

public interface IInvokedMethodListener extends ITestNGListener {
   void beforeInvocation(IInvokedMethod var1, ITestResult var2);

   void afterInvocation(IInvokedMethod var1, ITestResult var2);
}

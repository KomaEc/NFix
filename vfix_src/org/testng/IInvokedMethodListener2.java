package org.testng;

public interface IInvokedMethodListener2 extends IInvokedMethodListener {
   void beforeInvocation(IInvokedMethod var1, ITestResult var2, ITestContext var3);

   void afterInvocation(IInvokedMethod var1, ITestResult var2, ITestContext var3);
}

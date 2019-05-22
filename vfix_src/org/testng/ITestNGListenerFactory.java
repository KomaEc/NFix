package org.testng;

public interface ITestNGListenerFactory {
   ITestNGListener createListener(Class<? extends ITestNGListener> var1);
}

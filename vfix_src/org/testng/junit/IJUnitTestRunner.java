package org.testng.junit;

import java.util.List;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGMethod;
import org.testng.internal.ITestResultNotifier;

public interface IJUnitTestRunner {
   void setInvokedMethodListeners(List<IInvokedMethodListener> var1);

   void setTestResultNotifier(ITestResultNotifier var1);

   void run(Class var1, String... var2);

   List<ITestNGMethod> getTestMethods();
}

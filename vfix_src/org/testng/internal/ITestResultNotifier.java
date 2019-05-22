package org.testng.internal;

import java.util.List;
import java.util.Set;
import org.testng.IConfigurationListener;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.xml.XmlTest;

public interface ITestResultNotifier {
   Set<ITestResult> getPassedTests(ITestNGMethod var1);

   Set<ITestResult> getFailedTests(ITestNGMethod var1);

   Set<ITestResult> getSkippedTests(ITestNGMethod var1);

   void addPassedTest(ITestNGMethod var1, ITestResult var2);

   void addSkippedTest(ITestNGMethod var1, ITestResult var2);

   void addFailedTest(ITestNGMethod var1, ITestResult var2);

   void addFailedButWithinSuccessPercentageTest(ITestNGMethod var1, ITestResult var2);

   void addInvokedMethod(InvokedMethod var1);

   XmlTest getTest();

   List<ITestListener> getTestListeners();

   List<IConfigurationListener> getConfigurationListeners();
}

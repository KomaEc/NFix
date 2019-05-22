package org.testng;

import java.util.List;
import org.testng.xml.XmlTest;

public interface ITestRunnerFactory {
   TestRunner newTestRunner(ISuite var1, XmlTest var2, List<IInvokedMethodListener> var3);
}

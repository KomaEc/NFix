package org.testng;

import java.util.List;
import org.testng.xml.XmlSuite;

public interface IReporter extends ITestNGListener {
   void generateReport(List<XmlSuite> var1, List<ISuite> var2, String var3);
}

package org.testng.internal;

import java.util.List;
import java.util.Map;
import org.testng.IClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

public interface IInvoker {
   void invokeConfigurations(IClass var1, ITestNGMethod[] var2, XmlSuite var3, Map<String, String> var4, Object[] var5, Object var6);

   List<ITestResult> invokeTestMethods(ITestNGMethod var1, XmlSuite var2, Map<String, String> var3, ConfigurationGroupMethods var4, Object var5, ITestContext var6);
}

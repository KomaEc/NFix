package org.testng.internal;

import java.util.HashMap;
import java.util.Map;
import org.testng.ClassMethodMap;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.xml.XmlSuite;

class SingleTestMethodWorker extends TestMethodWorker {
   private static final ConfigurationGroupMethods EMPTY_GROUP_METHODS = new ConfigurationGroupMethods(new ITestNGMethod[0], new HashMap(), new HashMap());

   public SingleTestMethodWorker(IInvoker invoker, MethodInstance testMethod, XmlSuite suite, Map<String, String> parameters, ITestContext testContext) {
      super(invoker, new MethodInstance[]{testMethod}, suite, parameters, EMPTY_GROUP_METHODS, (ClassMethodMap)null, testContext);
   }
}

package org.apache.commons.digester.plugins.strategies;

import java.lang.reflect.Method;
import java.util.Properties;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.plugins.PluginException;
import org.apache.commons.digester.plugins.RuleFinder;
import org.apache.commons.digester.plugins.RuleLoader;

public class FinderFromDfltMethod extends RuleFinder {
   public static String DFLT_METHOD_NAME = "addRules";
   private String methodName;

   public FinderFromDfltMethod() {
      this(DFLT_METHOD_NAME);
   }

   public FinderFromDfltMethod(String methodName) {
      this.methodName = methodName;
   }

   public RuleLoader findLoader(Digester d, Class pluginClass, Properties p) throws PluginException {
      Method rulesMethod = LoaderFromClass.locateMethod(pluginClass, this.methodName);
      return rulesMethod == null ? null : new LoaderFromClass(pluginClass, rulesMethod);
   }
}

package org.apache.commons.digester.plugins.strategies;

import java.util.Properties;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.plugins.PluginException;
import org.apache.commons.digester.plugins.RuleFinder;
import org.apache.commons.digester.plugins.RuleLoader;

public class FinderFromMethod extends RuleFinder {
   public static String DFLT_METHOD_ATTR = "method";
   private String methodAttr;

   public FinderFromMethod() {
      this(DFLT_METHOD_ATTR);
   }

   public FinderFromMethod(String methodAttr) {
      this.methodAttr = methodAttr;
   }

   public RuleLoader findLoader(Digester d, Class pluginClass, Properties p) throws PluginException {
      String methodName = p.getProperty(this.methodAttr);
      return methodName == null ? null : new LoaderFromClass(pluginClass, methodName);
   }
}

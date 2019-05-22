package org.apache.commons.digester.plugins.strategies;

import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.plugins.PluginException;
import org.apache.commons.digester.plugins.RuleFinder;
import org.apache.commons.digester.plugins.RuleLoader;

public class FinderFromDfltResource extends RuleFinder {
   public static String DFLT_RESOURCE_SUFFIX = "RuleInfo.xml";
   private String resourceSuffix;

   public FinderFromDfltResource() {
      this(DFLT_RESOURCE_SUFFIX);
   }

   public FinderFromDfltResource(String resourceSuffix) {
      this.resourceSuffix = resourceSuffix;
   }

   public RuleLoader findLoader(Digester d, Class pluginClass, Properties p) throws PluginException {
      String resourceName = pluginClass.getName().replace('.', '/') + this.resourceSuffix;
      InputStream is = pluginClass.getClassLoader().getResourceAsStream(resourceName);
      return is == null ? null : FinderFromResource.loadRules(d, pluginClass, is, resourceName);
   }
}

package org.apache.commons.digester.plugins.strategies;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.plugins.PluginException;
import org.apache.commons.digester.plugins.RuleFinder;
import org.apache.commons.digester.plugins.RuleLoader;

public class FinderFromResource extends RuleFinder {
   public static String DFLT_RESOURCE_ATTR = "resource";
   private String resourceAttr;

   public FinderFromResource() {
      this(DFLT_RESOURCE_ATTR);
   }

   public FinderFromResource(String resourceAttr) {
      this.resourceAttr = resourceAttr;
   }

   public RuleLoader findLoader(Digester d, Class pluginClass, Properties p) throws PluginException {
      String resourceName = p.getProperty(this.resourceAttr);
      if (resourceName == null) {
         return null;
      } else {
         InputStream is = pluginClass.getClassLoader().getResourceAsStream(resourceName);
         if (is == null) {
            throw new PluginException("Resource " + resourceName + " not found.");
         } else {
            return loadRules(d, pluginClass, is, resourceName);
         }
      }
   }

   public static RuleLoader loadRules(Digester d, Class pluginClass, InputStream is, String resourceName) throws PluginException {
      LoaderFromStream var5;
      try {
         RuleLoader loader = new LoaderFromStream(is);
         var5 = loader;
      } catch (Exception var14) {
         throw new PluginException("Unable to load xmlrules from resource [" + resourceName + "]", var14);
      } finally {
         try {
            is.close();
         } catch (IOException var13) {
            throw new PluginException("Unable to close stream for resource [" + resourceName + "]", var13);
         }
      }

      return var5;
   }
}

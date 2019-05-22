package org.apache.velocity.runtime.resource.loader;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.ClassUtils;
import org.apache.velocity.util.StringUtils;

public class ResourceLoaderFactory {
   public static ResourceLoader getLoader(RuntimeServices rs, String loaderClassName) throws Exception {
      ResourceLoader loader = null;

      try {
         loader = (ResourceLoader)ClassUtils.getNewInstance(loaderClassName);
         rs.getLog().debug("ResourceLoader instantiated: " + loader.getClass().getName());
         return loader;
      } catch (Exception var4) {
         rs.getLog().error("Problem instantiating the template loader.\nLook at your properties file and make sure the\nname of the template loader is correct. Here is the\nerror:", var4);
         throw new Exception("Problem initializing template loader: " + loaderClassName + "\nError is: " + StringUtils.stackTrace(var4));
      }
   }
}

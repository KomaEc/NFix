package org.codehaus.plexus.velocity;

import java.io.InputStream;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

public class ContextClassLoaderResourceLoader extends ResourceLoader {
   public void init(ExtendedProperties configuration) {
   }

   public synchronized InputStream getResourceStream(String name) throws ResourceNotFoundException {
      InputStream result = null;
      if (name != null && name.length() != 0) {
         try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            result = classLoader.getResourceAsStream(name);
            return result;
         } catch (Exception var4) {
            throw new ResourceNotFoundException(var4.getMessage());
         }
      } else {
         throw new ResourceNotFoundException("No template name provided");
      }
   }

   public boolean isSourceModified(Resource resource) {
      return false;
   }

   public long getLastModified(Resource resource) {
      return 0L;
   }
}

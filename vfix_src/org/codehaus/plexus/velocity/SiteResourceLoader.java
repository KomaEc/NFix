package org.codehaus.plexus.velocity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

public class SiteResourceLoader extends ResourceLoader {
   private static String resource;

   public static void setResource(String staticResource) {
      resource = staticResource;
   }

   public void init(ExtendedProperties p) {
   }

   public InputStream getResourceStream(String name) throws ResourceNotFoundException {
      if (resource != null) {
         try {
            return new FileInputStream(resource);
         } catch (FileNotFoundException var3) {
            throw new ResourceNotFoundException("Cannot find resource, make sure you set the right resource.");
         }
      } else {
         return null;
      }
   }

   public boolean isSourceModified(Resource resource) {
      return false;
   }

   public long getLastModified(Resource resource) {
      return 0L;
   }
}

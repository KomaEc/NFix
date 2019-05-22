package org.apache.velocity.runtime.resource.loader;

import java.io.InputStream;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.util.ClassUtils;
import org.apache.velocity.util.ExceptionUtils;

public class ClasspathResourceLoader extends ResourceLoader {
   // $FF: synthetic field
   static Class class$org$apache$velocity$exception$ResourceNotFoundException;

   public void init(ExtendedProperties configuration) {
      if (this.log.isTraceEnabled()) {
         this.log.trace("ClasspathResourceLoader : initialization complete.");
      }

   }

   public InputStream getResourceStream(String name) throws ResourceNotFoundException {
      InputStream result = null;
      if (StringUtils.isEmpty(name)) {
         throw new ResourceNotFoundException("No template name provided");
      } else {
         try {
            result = ClassUtils.getResourceAsStream(this.getClass(), name);
         } catch (Exception var4) {
            throw (ResourceNotFoundException)ExceptionUtils.createWithCause(class$org$apache$velocity$exception$ResourceNotFoundException == null ? (class$org$apache$velocity$exception$ResourceNotFoundException = class$("org.apache.velocity.exception.ResourceNotFoundException")) : class$org$apache$velocity$exception$ResourceNotFoundException, "problem with template: " + name, var4);
         }

         if (result == null) {
            String msg = "ClasspathResourceLoader Error: cannot find resource " + name;
            throw new ResourceNotFoundException(msg);
         } else {
            return result;
         }
      }
   }

   public boolean isSourceModified(Resource resource) {
      return false;
   }

   public long getLastModified(Resource resource) {
      return 0L;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}

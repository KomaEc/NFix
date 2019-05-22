package org.apache.velocity.runtime.resource.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;

public class URLResourceLoader extends ResourceLoader {
   private String[] roots = null;
   protected HashMap templateRoots = null;

   public void init(ExtendedProperties configuration) {
      this.log.trace("URLResourceLoader : initialization starting.");
      this.roots = configuration.getStringArray("root");
      if (this.log.isInfoEnabled()) {
         for(int i = 0; i < this.roots.length; ++i) {
            this.log.info("URLResourceLoader : adding root '" + this.roots[i] + "'");
         }
      }

      this.templateRoots = new HashMap();
      this.log.trace("URLResourceLoader : initialization complete.");
   }

   public synchronized InputStream getResourceStream(String name) throws ResourceNotFoundException {
      if (StringUtils.isEmpty(name)) {
         throw new ResourceNotFoundException("URLResourceLoader : No template name provided");
      } else {
         InputStream inputStream = null;
         Exception exception = null;

         for(int i = 0; i < this.roots.length; ++i) {
            try {
               URL u = new URL(this.roots[i] + name);
               inputStream = u.openStream();
               if (inputStream != null) {
                  if (this.log.isDebugEnabled()) {
                     this.log.debug("URLResourceLoader: Found '" + name + "' at '" + this.roots[i] + "'");
                  }

                  this.templateRoots.put(name, this.roots[i]);
                  break;
               }
            } catch (IOException var6) {
               if (this.log.isDebugEnabled()) {
                  this.log.debug("URLResourceLoader: Exception when looking for '" + name + "' at '" + this.roots[i] + "'", var6);
               }

               if (exception == null) {
                  exception = var6;
               }
            }
         }

         if (inputStream == null) {
            String msg;
            if (exception == null) {
               msg = "URLResourceLoader : Resource '" + name + "' not found.";
            } else {
               msg = exception.getMessage();
            }

            throw new ResourceNotFoundException(msg);
         } else {
            return inputStream;
         }
      }
   }

   public boolean isSourceModified(Resource resource) {
      long fileLastModified = this.getLastModified(resource);
      return fileLastModified == 0L || fileLastModified != resource.getLastModified();
   }

   public long getLastModified(Resource resource) {
      String name = resource.getName();
      String root = (String)this.templateRoots.get(name);

      try {
         URL u = new URL(root + name);
         URLConnection conn = u.openConnection();
         return conn.getLastModified();
      } catch (IOException var6) {
         this.log.warn("URLResourceLoader: '" + name + "' is no longer reachable at '" + root + "'", var6);
         return 0L;
      }
   }
}

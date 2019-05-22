package org.apache.velocity.runtime.resource.loader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.util.StringUtils;

public class JarResourceLoader extends ResourceLoader {
   private Map entryDirectory = new HashMap(559);
   private Map jarfiles = new HashMap(89);

   public void init(ExtendedProperties configuration) {
      this.log.trace("JarResourceLoader : initialization starting.");
      Vector paths = configuration.getVector("path");
      StringUtils.trimStrings(paths);
      if (paths == null || paths.size() == 0) {
         paths = configuration.getVector("resource.path");
         StringUtils.trimStrings(paths);
         if (paths != null && paths.size() > 0) {
            this.log.info("JarResourceLoader : you are using a deprecated configuration property for the JarResourceLoader -> '<name>.resource.loader.resource.path'. Please change to the conventional '<name>.resource.loader.path'.");
         }
      }

      if (paths != null) {
         this.log.debug("JarResourceLoader # of paths : " + paths.size());

         for(int i = 0; i < paths.size(); ++i) {
            this.loadJar((String)paths.get(i));
         }
      }

      this.log.trace("JarResourceLoader : initialization complete.");
   }

   private void loadJar(String path) {
      if (this.log.isDebugEnabled()) {
         this.log.debug("JarResourceLoader : trying to load \"" + path + "\"");
      }

      if (path == null) {
         this.log.error("JarResourceLoader : can not load JAR - JAR path is null");
      }

      if (!path.startsWith("jar:")) {
         this.log.error("JarResourceLoader : JAR path must start with jar: -> see java.net.JarURLConnection for information");
      }

      if (!path.endsWith("!/")) {
         path = path + "!/";
      }

      this.closeJar(path);
      JarHolder temp = new JarHolder(this.rsvc, path);
      this.addEntries(temp.getEntries());
      this.jarfiles.put(temp.getUrlPath(), temp);
   }

   private void closeJar(String path) {
      if (this.jarfiles.containsKey(path)) {
         JarHolder theJar = (JarHolder)this.jarfiles.get(path);
         theJar.close();
      }

   }

   private void addEntries(Hashtable entries) {
      this.entryDirectory.putAll(entries);
   }

   public InputStream getResourceStream(String source) throws ResourceNotFoundException {
      InputStream results = null;
      if (org.apache.commons.lang.StringUtils.isEmpty(source)) {
         throw new ResourceNotFoundException("Need to have a resource!");
      } else {
         String normalizedPath = StringUtils.normalizePath(source);
         String jarurl;
         if (normalizedPath != null && normalizedPath.length() != 0) {
            if (normalizedPath.startsWith("/")) {
               normalizedPath = normalizedPath.substring(1);
            }

            if (this.entryDirectory.containsKey(normalizedPath)) {
               jarurl = (String)this.entryDirectory.get(normalizedPath);
               if (this.jarfiles.containsKey(jarurl)) {
                  JarHolder holder = (JarHolder)this.jarfiles.get(jarurl);
                  results = holder.getResource(normalizedPath);
                  return results;
               }
            }

            throw new ResourceNotFoundException("JarResourceLoader Error: cannot find resource " + source);
         } else {
            jarurl = "JAR resource error : argument " + normalizedPath + " contains .. and may be trying to access " + "content outside of template root.  Rejected.";
            this.log.error("JarResourceLoader : " + jarurl);
            throw new ResourceNotFoundException(jarurl);
         }
      }
   }

   public boolean isSourceModified(Resource resource) {
      return true;
   }

   public long getLastModified(Resource resource) {
      return 0L;
   }
}

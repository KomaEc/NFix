package org.jboss.util.file;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** @deprecated */
public abstract class ArchiveBrowser {
   public static Map factoryFinder = new ConcurrentHashMap();

   public static Iterator getBrowser(URL url, ArchiveBrowser.Filter filter) {
      ArchiveBrowserFactory factory = (ArchiveBrowserFactory)factoryFinder.get(url.getProtocol());
      if (factory == null) {
         throw new RuntimeException("Archive browser cannot handle protocol: " + url);
      } else {
         return factory.create(url, filter);
      }
   }

   static {
      factoryFinder.put("file", new FileProtocolArchiveBrowserFactory());
      factoryFinder.put("jar", new JarProtocolArchiveBrowserFactory());
   }

   public interface Filter {
      boolean accept(String var1);
   }
}

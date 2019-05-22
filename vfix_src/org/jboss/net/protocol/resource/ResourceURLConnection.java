package org.jboss.net.protocol.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jboss.logging.Logger;
import org.jboss.net.protocol.DelegatingURLConnection;

public class ResourceURLConnection extends DelegatingURLConnection {
   private static final Logger log = Logger.getLogger(ResourceURLConnection.class);

   public ResourceURLConnection(URL url) throws MalformedURLException, IOException {
      super(url);
   }

   protected URL makeDelegateUrl(URL url) throws MalformedURLException, IOException {
      String name = url.getHost();
      String file = url.getFile();
      if (file != null && !file.equals("")) {
         name = name + file;
      }

      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      URL target = cl.getResource(name);
      if (target == null) {
         cl = ClassLoader.getSystemClassLoader();
         target = cl.getResource(name);
      }

      if (target == null) {
         throw new FileNotFoundException("Could not locate resource: " + name);
      } else {
         if (log.isTraceEnabled()) {
            log.trace("Target resource URL: " + target);

            try {
               log.trace("Target resource URL connection: " + target.openConnection());
            } catch (Exception var7) {
            }
         }

         return new URL(target.toExternalForm());
      }
   }
}

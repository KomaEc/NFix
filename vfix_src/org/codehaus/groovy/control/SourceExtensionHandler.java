package org.codehaus.groovy.control;

import groovy.lang.GroovyRuntimeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

public class SourceExtensionHandler {
   public static Set<String> getRegisteredExtensions(ClassLoader loader) {
      Set<String> extensions = new LinkedHashSet();
      extensions.add("groovy");
      URL service = null;

      try {
         Enumeration globalServices = loader.getResources("META-INF/services/org.codehaus.groovy.source.Extensions");

         while(globalServices.hasMoreElements()) {
            service = (URL)globalServices.nextElement();
            BufferedReader svcIn = new BufferedReader(new InputStreamReader(service.openStream()));

            for(String extension = svcIn.readLine(); extension != null; extension = svcIn.readLine()) {
               extension = extension.trim();
               if (!extension.startsWith("#") && extension.length() > 0) {
                  extensions.add(extension);
               }
            }
         }

         return extensions;
      } catch (IOException var6) {
         throw new GroovyRuntimeException("IO Exception attempting to load source extension registerers. Exception: " + var6.toString() + (service == null ? "" : service.toExternalForm()));
      }
   }
}

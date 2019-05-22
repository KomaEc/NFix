package org.codehaus.plexus.configuration.processor;

import java.util.Map;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public abstract class AbstractConfigurationResourceHandler implements ConfigurationResourceHandler {
   protected String getSource(Map parameters) throws ConfigurationProcessingException {
      String source = (String)parameters.get("source");
      if (source == null) {
         throw new ConfigurationProcessingException("The 'source' attribute for a configuration resource handler cannot be null.");
      } else {
         return source;
      }
   }

   // $FF: synthetic method
   public abstract PlexusConfiguration[] handleRequest(Map var1) throws ConfigurationResourceNotFoundException, ConfigurationProcessingException;

   // $FF: synthetic method
   public abstract String getId();
}

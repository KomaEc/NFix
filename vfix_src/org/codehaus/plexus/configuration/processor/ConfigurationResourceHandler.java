package org.codehaus.plexus.configuration.processor;

import java.util.Map;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public interface ConfigurationResourceHandler {
   String SOURCE = "source";

   String getId();

   PlexusConfiguration[] handleRequest(Map var1) throws ConfigurationResourceNotFoundException, ConfigurationProcessingException;
}

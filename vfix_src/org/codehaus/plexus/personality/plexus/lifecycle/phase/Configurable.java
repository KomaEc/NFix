package org.codehaus.plexus.personality.plexus.lifecycle.phase;

import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;

public interface Configurable {
   void configure(PlexusConfiguration var1) throws PlexusConfigurationException;
}

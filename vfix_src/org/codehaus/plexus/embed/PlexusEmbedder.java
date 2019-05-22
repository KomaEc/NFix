package org.codehaus.plexus.embed;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.configuration.PlexusConfigurationResourceException;
import org.codehaus.plexus.logging.LoggerManager;

public interface PlexusEmbedder {
   PlexusContainer getContainer();

   Object lookup(String var1) throws ComponentLookupException;

   Object lookup(String var1, String var2) throws ComponentLookupException;

   boolean hasComponent(String var1);

   boolean hasComponent(String var1, String var2);

   void release(Object var1) throws ComponentLifecycleException;

   void setClassWorld(ClassWorld var1);

   void setConfiguration(URL var1) throws IOException;

   void setConfiguration(Reader var1) throws IOException;

   void addContextValue(Object var1, Object var2);

   void setProperties(Properties var1);

   void setProperties(File var1);

   void start(ClassWorld var1) throws PlexusContainerException, PlexusConfigurationResourceException;

   void start() throws PlexusContainerException, PlexusConfigurationResourceException;

   void stop();

   void setLoggerManager(LoggerManager var1);
}

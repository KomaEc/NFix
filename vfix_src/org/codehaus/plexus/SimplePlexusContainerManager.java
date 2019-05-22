package org.codehaus.plexus;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;
import org.codehaus.plexus.configuration.PlexusConfigurationResourceException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Startable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.StartingException;

public class SimplePlexusContainerManager implements PlexusContainerManager, Contextualizable, Initializable, Startable {
   private PlexusContainer parentPlexus;
   private DefaultPlexusContainer myPlexus;
   private String plexusConfig;
   private Properties contextValues;

   public void contextualize(Context context) throws ContextException {
      this.parentPlexus = (PlexusContainer)context.get("plexus");
   }

   public void initialize() throws InitializationException {
      this.myPlexus = new DefaultPlexusContainer();
      this.myPlexus.setParentPlexusContainer(this.parentPlexus);
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      InputStream stream = loader.getResourceAsStream(this.plexusConfig);
      InputStreamReader r = new InputStreamReader(stream);

      try {
         this.myPlexus.setConfigurationResource(r);
      } catch (PlexusConfigurationResourceException var7) {
         throw new InitializationException("Unable to initialize container configuration", var7);
      }

      if (this.contextValues != null) {
         Iterator i = this.contextValues.keySet().iterator();

         while(i.hasNext()) {
            String name = (String)i.next();
            this.myPlexus.addContextValue(name, this.contextValues.getProperty(name));
         }
      }

      try {
         this.myPlexus.initialize();
      } catch (PlexusContainerException var6) {
         throw new InitializationException("Error initializing container", var6);
      }
   }

   public void start() throws StartingException {
      try {
         this.myPlexus.start();
      } catch (PlexusContainerException var2) {
         throw new StartingException("Error starting container", var2);
      }
   }

   public void stop() {
      this.myPlexus.dispose();
   }

   public PlexusContainer[] getManagedContainers() {
      return new PlexusContainer[]{this.myPlexus};
   }
}

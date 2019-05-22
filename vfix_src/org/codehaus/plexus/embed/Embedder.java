package org.codehaus.plexus.embed;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.configuration.PlexusConfigurationResourceException;
import org.codehaus.plexus.logging.LoggerManager;
import org.codehaus.plexus.util.PropertyUtils;

public class Embedder implements PlexusEmbedder {
   private Reader configurationReader;
   private Properties properties;
   private final DefaultPlexusContainer container = new DefaultPlexusContainer();
   private boolean embedderStarted = false;
   private boolean embedderStopped = false;

   public synchronized PlexusContainer getContainer() {
      if (!this.embedderStarted) {
         throw new IllegalStateException("Embedder must be started");
      } else {
         return this.container;
      }
   }

   public Object lookup(String role) throws ComponentLookupException {
      return this.getContainer().lookup(role);
   }

   public Object lookup(String role, String id) throws ComponentLookupException {
      return this.getContainer().lookup(role, id);
   }

   public boolean hasComponent(String role) {
      return this.getContainer().hasComponent(role);
   }

   public boolean hasComponent(String role, String id) {
      return this.getContainer().hasComponent(role, id);
   }

   public void release(Object service) throws ComponentLifecycleException {
      this.getContainer().release(service);
   }

   public synchronized void setClassWorld(ClassWorld classWorld) {
      this.container.setClassWorld(classWorld);
   }

   public synchronized void setConfiguration(URL configuration) throws IOException {
      if (!this.embedderStarted && !this.embedderStopped) {
         this.configurationReader = new InputStreamReader(configuration.openStream());
      } else {
         throw new IllegalStateException("Embedder has already been started");
      }
   }

   public synchronized void setConfiguration(Reader configuration) throws IOException {
      if (!this.embedderStarted && !this.embedderStopped) {
         this.configurationReader = configuration;
      } else {
         throw new IllegalStateException("Embedder has already been started");
      }
   }

   public synchronized void addContextValue(Object key, Object value) {
      if (!this.embedderStarted && !this.embedderStopped) {
         this.container.addContextValue(key, value);
      } else {
         throw new IllegalStateException("Embedder has already been started");
      }
   }

   public synchronized void setProperties(Properties properties) {
      this.properties = properties;
   }

   public synchronized void setProperties(File file) {
      this.properties = PropertyUtils.loadProperties(file);
   }

   public void setLoggerManager(LoggerManager loggerManager) {
      this.container.setLoggerManager(loggerManager);
   }

   protected synchronized void initializeContext() {
      Set keys = this.properties.keySet();
      Iterator iter = keys.iterator();

      while(iter.hasNext()) {
         String key = (String)iter.next();
         String value = this.properties.getProperty(key);
         this.container.addContextValue(key, value);
      }

   }

   public synchronized void start(ClassWorld classWorld) throws PlexusContainerException {
      this.container.setClassWorld(classWorld);
      this.start();
   }

   public synchronized void start() throws PlexusContainerException {
      if (this.embedderStarted) {
         throw new IllegalStateException("Embedder already started");
      } else if (this.embedderStopped) {
         throw new IllegalStateException("Embedder cannot be restarted");
      } else {
         if (this.configurationReader != null) {
            try {
               this.container.setConfigurationResource(this.configurationReader);
            } catch (PlexusConfigurationResourceException var2) {
               throw new PlexusContainerException("Error loading from configuration reader", var2);
            }
         }

         if (this.properties != null) {
            this.initializeContext();
         }

         this.container.initialize();
         this.embedderStarted = true;
         this.container.start();
      }
   }

   public synchronized void stop() {
      if (this.embedderStopped) {
         throw new IllegalStateException("Embedder already stopped");
      } else if (!this.embedderStarted) {
         throw new IllegalStateException("Embedder not started");
      } else {
         this.container.dispose();
         this.embedderStarted = false;
         this.embedderStopped = true;
      }
   }
}

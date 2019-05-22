package org.codehaus.plexus.configuration.processor;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.codehaus.plexus.util.StringUtils;

public class ConfigurationProcessor {
   protected Map handlers = new HashMap();

   public void addConfigurationResourceHandler(ConfigurationResourceHandler handler) {
      this.handlers.put(handler.getId(), handler);
   }

   public PlexusConfiguration process(PlexusConfiguration configuration, Map variables) throws ConfigurationResourceNotFoundException, ConfigurationProcessingException {
      XmlPlexusConfiguration processed = new XmlPlexusConfiguration("configuration");
      this.walk(configuration, processed, variables);
      return processed;
   }

   protected void walk(PlexusConfiguration source, PlexusConfiguration processed, Map variables) throws ConfigurationResourceNotFoundException, ConfigurationProcessingException {
      PlexusConfiguration[] children = source.getChildren();

      for(int i = 0; i < children.length; ++i) {
         PlexusConfiguration child = children[i];
         int count = child.getChildCount();
         if (count > 0) {
            XmlPlexusConfiguration processedChild = new XmlPlexusConfiguration(child.getName());
            this.copyAttributes(child, processedChild);
            processed.addChild(processedChild);
            this.walk(child, processedChild, variables);
         } else {
            String elementName = child.getName();
            if (this.handlers.containsKey(elementName)) {
               ConfigurationResourceHandler handler = (ConfigurationResourceHandler)this.handlers.get(elementName);
               PlexusConfiguration[] configurations = handler.handleRequest(this.createHandlerParameters(child, variables));

               for(int j = 0; j < configurations.length; ++j) {
                  processed.addChild(configurations[j]);
               }
            } else {
               processed.addChild(child);
            }
         }
      }

   }

   protected Map createHandlerParameters(PlexusConfiguration c, Map variables) {
      Map parameters = new HashMap();
      String[] parameterNames = c.getAttributeNames();

      for(int i = 0; i < parameterNames.length; ++i) {
         String key = parameterNames[i];
         String value = StringUtils.interpolate(c.getAttribute(key, (String)null), variables);
         parameters.put(key, value);
      }

      return parameters;
   }

   private void copyAttributes(PlexusConfiguration source, XmlPlexusConfiguration target) {
      String[] names = source.getAttributeNames();

      for(int i = 0; i < names.length; ++i) {
         target.setAttribute(names[i], source.getAttribute(names[i], (String)null));
      }

   }
}

package org.codehaus.plexus.component.discovery;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentSetDescriptor;
import org.codehaus.plexus.component.repository.io.PlexusTools;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;

public class DefaultComponentDiscoverer extends AbstractComponentDiscoverer {
   public String getComponentDescriptorLocation() {
      return "META-INF/plexus/components.xml";
   }

   public ComponentSetDescriptor createComponentDescriptors(Reader componentDescriptorReader, String source) throws PlexusConfigurationException {
      PlexusConfiguration componentDescriptorConfiguration = PlexusTools.buildConfiguration(source, componentDescriptorReader);
      ComponentSetDescriptor componentSetDescriptor = new ComponentSetDescriptor();
      List componentDescriptors = new ArrayList();
      PlexusConfiguration[] componentConfigurations = componentDescriptorConfiguration.getChild("components").getChildren("component");

      for(int i = 0; i < componentConfigurations.length; ++i) {
         PlexusConfiguration componentConfiguration = componentConfigurations[i];
         ComponentDescriptor componentDescriptor = null;

         try {
            componentDescriptor = PlexusTools.buildComponentDescriptor(componentConfiguration);
         } catch (PlexusConfigurationException var11) {
            throw new PlexusConfigurationException("Cannot process component descriptor: " + source, var11);
         }

         componentDescriptor.setComponentType("plexus");
         componentDescriptors.add(componentDescriptor);
      }

      componentSetDescriptor.setComponents(componentDescriptors);
      return componentSetDescriptor;
   }
}

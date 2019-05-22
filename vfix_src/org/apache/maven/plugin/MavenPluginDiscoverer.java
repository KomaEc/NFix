package org.apache.maven.plugin;

import java.io.Reader;
import org.apache.maven.plugin.descriptor.PluginDescriptorBuilder;
import org.codehaus.plexus.component.discovery.AbstractComponentDiscoverer;
import org.codehaus.plexus.component.repository.ComponentSetDescriptor;
import org.codehaus.plexus.configuration.PlexusConfigurationException;

public class MavenPluginDiscoverer extends AbstractComponentDiscoverer {
   private PluginDescriptorBuilder builder = new PluginDescriptorBuilder();

   public String getComponentDescriptorLocation() {
      return "META-INF/maven/plugin.xml";
   }

   public ComponentSetDescriptor createComponentDescriptors(Reader componentDescriptorConfiguration, String source) throws PlexusConfigurationException {
      return this.builder.build(componentDescriptorConfiguration, source);
   }
}

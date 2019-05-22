package org.codehaus.plexus.component.discovery;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentSetDescriptor;
import org.codehaus.plexus.component.repository.io.PlexusTools;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.configuration.PlexusConfigurationMerger;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextMapAdapter;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;

public class PlexusXmlComponentDiscoverer implements ComponentDiscoverer {
   private static final String PLEXUS_XML_RESOURCE = "META-INF/plexus/plexus.xml";
   private ComponentDiscovererManager manager;

   public void setManager(ComponentDiscovererManager manager) {
      this.manager = manager;
   }

   public List findComponents(Context context, ClassRealm classRealm) throws PlexusConfigurationException {
      PlexusConfiguration configuration = this.discoverConfiguration(context, classRealm);
      List componentSetDescriptors = new ArrayList();
      ComponentSetDescriptor componentSetDescriptor = this.createComponentDescriptors(configuration, classRealm);
      componentSetDescriptors.add(componentSetDescriptor);
      ComponentDiscoveryEvent event = new ComponentDiscoveryEvent(componentSetDescriptor);
      this.manager.fireComponentDiscoveryEvent(event);
      return componentSetDescriptors;
   }

   public PlexusConfiguration discoverConfiguration(Context context, ClassRealm classRealm) throws PlexusConfigurationException {
      PlexusConfiguration configuration = null;
      Enumeration resources = null;

      try {
         resources = classRealm.findResources("META-INF/plexus/plexus.xml");
      } catch (IOException var17) {
         throw new PlexusConfigurationException("Error retrieving configuration resources: META-INF/plexus/plexus.xml from class realm: " + classRealm.getId(), var17);
      }

      Enumeration e = resources;

      while(e.hasMoreElements()) {
         URL url = (URL)e.nextElement();
         InputStreamReader reader = null;

         try {
            reader = new InputStreamReader(url.openStream());
            ContextMapAdapter contextAdapter = new ContextMapAdapter(context);
            InterpolationFilterReader interpolationFilterReader = new InterpolationFilterReader(reader, contextAdapter);
            PlexusConfiguration discoveredConfig = PlexusTools.buildConfiguration(url.toExternalForm(), interpolationFilterReader);
            if (configuration == null) {
               configuration = discoveredConfig;
            } else {
               configuration = PlexusConfigurationMerger.merge(configuration, discoveredConfig);
            }
         } catch (IOException var15) {
            throw new PlexusConfigurationException("Error reading configuration from: " + url.toExternalForm(), var15);
         } finally {
            IOUtil.close((Reader)reader);
         }
      }

      return configuration;
   }

   private ComponentSetDescriptor createComponentDescriptors(PlexusConfiguration configuration, ClassRealm classRealm) throws PlexusConfigurationException {
      ComponentSetDescriptor componentSetDescriptor = new ComponentSetDescriptor();
      if (configuration != null) {
         List componentDescriptors = new ArrayList();
         PlexusConfiguration[] componentConfigurations = configuration.getChild("components").getChildren("component");

         for(int i = 0; i < componentConfigurations.length; ++i) {
            PlexusConfiguration componentConfiguration = componentConfigurations[i];
            ComponentDescriptor componentDescriptor = null;

            try {
               componentDescriptor = PlexusTools.buildComponentDescriptor(componentConfiguration);
            } catch (PlexusConfigurationException var10) {
               throw new PlexusConfigurationException("Cannot build component descriptor from resource found in:\n" + Arrays.asList(classRealm.getConstituents()), var10);
            }

            componentDescriptor.setComponentType("plexus");
            componentDescriptors.add(componentDescriptor);
         }

         componentSetDescriptor.setComponents(componentDescriptors);
      }

      return componentSetDescriptor;
   }
}

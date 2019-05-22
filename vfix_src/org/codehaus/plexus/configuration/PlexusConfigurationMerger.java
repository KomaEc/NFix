package org.codehaus.plexus.configuration;

import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;

public class PlexusConfigurationMerger {
   public static PlexusConfiguration merge(PlexusConfiguration user, PlexusConfiguration system) {
      XmlPlexusConfiguration mergedConfiguration = new XmlPlexusConfiguration("plexus");
      PlexusConfiguration loadOnStart = user.getChild("load-on-start");
      if (loadOnStart.getChildCount() != 0) {
         mergedConfiguration.addChild(loadOnStart);
      }

      PlexusConfiguration systemProperties = user.getChild("system-properties");
      if (systemProperties.getChildCount() != 0) {
         mergedConfiguration.addChild(systemProperties);
      }

      PlexusConfiguration[] configurationsDirectories = user.getChildren("configurations-directory");
      if (configurationsDirectories.length != 0) {
         for(int i = 0; i < configurationsDirectories.length; ++i) {
            mergedConfiguration.addChild(configurationsDirectories[i]);
         }
      }

      PlexusConfiguration logging = user.getChild("logging");
      if (logging.getChildCount() != 0) {
         mergedConfiguration.addChild(logging);
      } else {
         mergedConfiguration.addChild(system.getChild("logging"));
      }

      PlexusConfiguration componentRepository = user.getChild("component-repository");
      if (componentRepository.getChildCount() != 0) {
         mergedConfiguration.addChild(componentRepository);
      } else {
         mergedConfiguration.addChild(system.getChild("component-repository"));
      }

      copyResources(system, mergedConfiguration);
      copyResources(user, mergedConfiguration);
      mergedConfiguration.addChild(system.getChild("component-manager-manager"));
      PlexusConfiguration componentDiscovererManager = user.getChild("component-discoverer-manager");
      if (componentDiscovererManager.getChildCount() != 0) {
         mergedConfiguration.addChild(componentDiscovererManager);
         copyComponentDiscoverers(system.getChild("component-discoverer-manager"), componentDiscovererManager);
      } else {
         mergedConfiguration.addChild(system.getChild("component-discoverer-manager"));
      }

      PlexusConfiguration componentFactoryManager = user.getChild("component-factory-manager");
      if (componentFactoryManager.getChildCount() != 0) {
         mergedConfiguration.addChild(componentFactoryManager);
         copyComponentFactories(system.getChild("component-factory-manager"), componentFactoryManager);
      } else {
         mergedConfiguration.addChild(system.getChild("component-factory-manager"));
      }

      PlexusConfiguration lifecycleHandlerManager = user.getChild("lifecycle-handler-manager");
      if (lifecycleHandlerManager.getChildCount() != 0) {
         mergedConfiguration.addChild(lifecycleHandlerManager);
         copyLifecycles(system.getChild("lifecycle-handler-manager"), lifecycleHandlerManager);
      } else {
         mergedConfiguration.addChild(system.getChild("lifecycle-handler-manager"));
      }

      PlexusConfiguration componentComposerManager = user.getChild("component-composer-manager");
      if (componentComposerManager.getChildCount() != 0) {
         mergedConfiguration.addChild(componentComposerManager);
         copyComponentComposers(system.getChild("component-composer-manager"), componentComposerManager);
      } else {
         mergedConfiguration.addChild(system.getChild("component-composer-manager"));
      }

      PlexusConfiguration components = system.getChild("components");
      mergedConfiguration.addChild(components);
      copyComponents(user.getChild("components"), components);
      return mergedConfiguration;
   }

   private static void copyResources(PlexusConfiguration source, PlexusConfiguration destination) {
      PlexusConfiguration[] handlers = source.getChild("resources").getChildren();
      XmlPlexusConfiguration dest = (XmlPlexusConfiguration)destination.getChild("resources");

      for(int i = 0; i < handlers.length; ++i) {
         dest.addChild(handlers[i]);
      }

   }

   private static void copyComponentDiscoverers(PlexusConfiguration source, PlexusConfiguration destination) {
      PlexusConfiguration[] handlers = source.getChild("component-discoverers").getChildren("component-discoverer");
      XmlPlexusConfiguration dest = (XmlPlexusConfiguration)destination.getChild("component-discoverers");

      for(int i = 0; i < handlers.length; ++i) {
         dest.addChild(handlers[i]);
      }

   }

   private static void copyComponentFactories(PlexusConfiguration source, PlexusConfiguration destination) {
      PlexusConfiguration[] handlers = source.getChild("component-factories").getChildren("component-factory");
      XmlPlexusConfiguration dest = (XmlPlexusConfiguration)destination.getChild("component-factories");

      for(int i = 0; i < handlers.length; ++i) {
         dest.addChild(handlers[i]);
      }

   }

   private static void copyComponentComposers(PlexusConfiguration source, PlexusConfiguration destination) {
      PlexusConfiguration[] composers = source.getChild("component-composers").getChildren("component-composer");
      XmlPlexusConfiguration dest = (XmlPlexusConfiguration)destination.getChild("component-composers");

      for(int i = 0; i < composers.length; ++i) {
         dest.addChild(composers[i]);
      }

   }

   private static void copyLifecycles(PlexusConfiguration source, PlexusConfiguration destination) {
      PlexusConfiguration[] handlers = source.getChild("lifecycle-handlers").getChildren("lifecycle-handler");
      XmlPlexusConfiguration dest = (XmlPlexusConfiguration)destination.getChild("lifecycle-handlers");

      for(int i = 0; i < handlers.length; ++i) {
         dest.addChild(handlers[i]);
      }

   }

   private static void copyComponents(PlexusConfiguration source, PlexusConfiguration destination) {
      PlexusConfiguration[] components = source.getChildren("component");

      for(int i = 0; i < components.length; ++i) {
         destination.addChild(components[i]);
      }

   }
}

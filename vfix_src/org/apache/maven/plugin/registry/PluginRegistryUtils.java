package org.apache.maven.plugin.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class PluginRegistryUtils {
   private PluginRegistryUtils() {
   }

   public static void merge(PluginRegistry dominant, PluginRegistry recessive, String recessiveSourceLevel) {
      if (dominant != null && recessive != null) {
         RuntimeInfo dominantRtInfo = dominant.getRuntimeInfo();
         String dominantUpdateInterval = dominant.getUpdateInterval();
         String recessiveUpdateInterval;
         if (dominantUpdateInterval == null) {
            recessiveUpdateInterval = recessive.getUpdateInterval();
            if (recessiveUpdateInterval != null) {
               dominant.setUpdateInterval(recessiveUpdateInterval);
               dominantRtInfo.setUpdateIntervalSourceLevel(recessiveSourceLevel);
            }
         }

         recessiveUpdateInterval = dominant.getAutoUpdate();
         String recessiveAutoUpdate;
         if (recessiveUpdateInterval == null) {
            recessiveAutoUpdate = recessive.getAutoUpdate();
            if (recessiveAutoUpdate != null) {
               dominant.setAutoUpdate(recessiveAutoUpdate);
               dominantRtInfo.setAutoUpdateSourceLevel(recessiveSourceLevel);
            }
         }

         recessiveAutoUpdate = null;
         List recessivePlugins;
         if (recessive != null) {
            recessivePlugins = recessive.getPlugins();
         } else {
            recessivePlugins = Collections.EMPTY_LIST;
         }

         shallowMergePlugins(dominant, recessivePlugins, recessiveSourceLevel);
      }
   }

   public static void recursivelySetSourceLevel(PluginRegistry pluginRegistry, String sourceLevel) {
      if (pluginRegistry != null) {
         pluginRegistry.setSourceLevel(sourceLevel);
         Iterator it = pluginRegistry.getPlugins().iterator();

         while(it.hasNext()) {
            Plugin plugin = (Plugin)it.next();
            plugin.setSourceLevel(sourceLevel);
         }

      }
   }

   private static void shallowMergePlugins(PluginRegistry dominant, List recessive, String recessiveSourceLevel) {
      Map dominantByKey = dominant.getPluginsByKey();
      List dominantPlugins = dominant.getPlugins();
      Iterator it = recessive.iterator();

      while(it.hasNext()) {
         Plugin recessivePlugin = (Plugin)it.next();
         if (!dominantByKey.containsKey(recessivePlugin.getKey())) {
            recessivePlugin.setSourceLevel(recessiveSourceLevel);
            dominantPlugins.add(recessivePlugin);
         }
      }

      dominant.flushPluginsByKey();
   }

   public static PluginRegistry extractUserPluginRegistry(PluginRegistry pluginRegistry) {
      PluginRegistry userRegistry = null;
      if (pluginRegistry != null && !"global-level".equals(pluginRegistry.getSourceLevel())) {
         userRegistry = new PluginRegistry();
         RuntimeInfo rtInfo = new RuntimeInfo(userRegistry);
         userRegistry.setRuntimeInfo(rtInfo);
         RuntimeInfo oldRtInfo = pluginRegistry.getRuntimeInfo();
         if ("user-level".equals(oldRtInfo.getAutoUpdateSourceLevel())) {
            userRegistry.setAutoUpdate(pluginRegistry.getAutoUpdate());
         }

         if ("user-level".equals(oldRtInfo.getUpdateIntervalSourceLevel())) {
            userRegistry.setUpdateInterval(pluginRegistry.getUpdateInterval());
         }

         List plugins = new ArrayList();
         Iterator it = pluginRegistry.getPlugins().iterator();

         while(it.hasNext()) {
            Plugin plugin = (Plugin)it.next();
            if ("user-level".equals(plugin.getSourceLevel())) {
               plugins.add(plugin);
            }
         }

         userRegistry.setPlugins(plugins);
         rtInfo.setFile(pluginRegistry.getRuntimeInfo().getFile());
      }

      return userRegistry;
   }
}

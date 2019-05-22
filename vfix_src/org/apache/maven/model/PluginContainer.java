package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PluginContainer implements Serializable {
   private List<Plugin> plugins;
   Map pluginMap;

   public void addPlugin(Plugin plugin) {
      if (!(plugin instanceof Plugin)) {
         throw new ClassCastException("PluginContainer.addPlugins(plugin) parameter must be instanceof " + Plugin.class.getName());
      } else {
         this.getPlugins().add(plugin);
      }
   }

   public List<Plugin> getPlugins() {
      if (this.plugins == null) {
         this.plugins = new ArrayList();
      }

      return this.plugins;
   }

   public void removePlugin(Plugin plugin) {
      if (!(plugin instanceof Plugin)) {
         throw new ClassCastException("PluginContainer.removePlugins(plugin) parameter must be instanceof " + Plugin.class.getName());
      } else {
         this.getPlugins().remove(plugin);
      }
   }

   public void setPlugins(List<Plugin> plugins) {
      this.plugins = plugins;
   }

   public void flushPluginMap() {
      this.pluginMap = null;
   }

   public Map getPluginsAsMap() {
      if (this.pluginMap == null) {
         this.pluginMap = new LinkedHashMap();
         if (this.plugins != null) {
            Iterator it = this.plugins.iterator();

            while(it.hasNext()) {
               Plugin plugin = (Plugin)it.next();
               this.pluginMap.put(plugin.getKey(), plugin);
            }
         }
      }

      return this.pluginMap;
   }
}

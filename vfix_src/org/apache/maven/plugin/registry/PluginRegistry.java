package org.apache.maven.plugin.registry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PluginRegistry extends TrackableBase implements Serializable {
   private String updateInterval = "never";
   private String autoUpdate;
   private String checkLatest;
   private List<Plugin> plugins;
   private String modelEncoding = "UTF-8";
   private Map pluginsByKey;
   private RuntimeInfo runtimeInfo;

   public void addPlugin(Plugin plugin) {
      if (!(plugin instanceof Plugin)) {
         throw new ClassCastException("PluginRegistry.addPlugins(plugin) parameter must be instanceof " + Plugin.class.getName());
      } else {
         this.getPlugins().add(plugin);
      }
   }

   public String getAutoUpdate() {
      return this.autoUpdate;
   }

   public String getCheckLatest() {
      return this.checkLatest;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }

   public List<Plugin> getPlugins() {
      if (this.plugins == null) {
         this.plugins = new ArrayList();
      }

      return this.plugins;
   }

   public String getUpdateInterval() {
      return this.updateInterval;
   }

   public void removePlugin(Plugin plugin) {
      if (!(plugin instanceof Plugin)) {
         throw new ClassCastException("PluginRegistry.removePlugins(plugin) parameter must be instanceof " + Plugin.class.getName());
      } else {
         this.getPlugins().remove(plugin);
      }
   }

   public void setAutoUpdate(String autoUpdate) {
      this.autoUpdate = autoUpdate;
   }

   public void setCheckLatest(String checkLatest) {
      this.checkLatest = checkLatest;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public void setPlugins(List<Plugin> plugins) {
      this.plugins = plugins;
   }

   public void setUpdateInterval(String updateInterval) {
      this.updateInterval = updateInterval;
   }

   public Map getPluginsByKey() {
      if (this.pluginsByKey == null) {
         this.pluginsByKey = new HashMap();
         Iterator it = this.getPlugins().iterator();

         while(it.hasNext()) {
            Plugin plugin = (Plugin)it.next();
            this.pluginsByKey.put(plugin.getKey(), plugin);
         }
      }

      return this.pluginsByKey;
   }

   public void flushPluginsByKey() {
      this.pluginsByKey = null;
   }

   public void setRuntimeInfo(RuntimeInfo runtimeInfo) {
      this.runtimeInfo = runtimeInfo;
   }

   public RuntimeInfo getRuntimeInfo() {
      return this.runtimeInfo;
   }
}

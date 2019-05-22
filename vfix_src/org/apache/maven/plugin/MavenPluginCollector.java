package org.apache.maven.plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.codehaus.plexus.component.discovery.ComponentDiscoveryEvent;
import org.codehaus.plexus.component.discovery.ComponentDiscoveryListener;
import org.codehaus.plexus.component.repository.ComponentSetDescriptor;
import org.codehaus.plexus.logging.AbstractLogEnabled;

public class MavenPluginCollector extends AbstractLogEnabled implements ComponentDiscoveryListener {
   private Set pluginsInProcess = new HashSet();
   private Map pluginDescriptors = new HashMap();
   private Map pluginIdsByPrefix = new HashMap();

   public void componentDiscovered(ComponentDiscoveryEvent event) {
      ComponentSetDescriptor componentSetDescriptor = event.getComponentSetDescriptor();
      if (componentSetDescriptor instanceof PluginDescriptor) {
         PluginDescriptor pluginDescriptor = (PluginDescriptor)componentSetDescriptor;
         String key = Plugin.constructKey(pluginDescriptor.getGroupId(), pluginDescriptor.getArtifactId());
         if (!this.pluginsInProcess.contains(key)) {
            this.pluginsInProcess.add(key);
            this.pluginDescriptors.put(key, pluginDescriptor);
            if (!this.pluginIdsByPrefix.containsKey(pluginDescriptor.getGoalPrefix())) {
               this.pluginIdsByPrefix.put(pluginDescriptor.getGoalPrefix(), pluginDescriptor);
            }
         }
      }

   }

   public PluginDescriptor getPluginDescriptor(Plugin plugin) {
      return (PluginDescriptor)this.pluginDescriptors.get(plugin.getKey());
   }

   public boolean isPluginInstalled(Plugin plugin) {
      return this.pluginDescriptors.containsKey(plugin.getKey());
   }

   public PluginDescriptor getPluginDescriptorForPrefix(String prefix) {
      return (PluginDescriptor)this.pluginIdsByPrefix.get(prefix);
   }

   public void flushPluginDescriptor(Plugin plugin) {
      this.pluginsInProcess.remove(plugin.getKey());
      this.pluginDescriptors.remove(plugin.getKey());
      Iterator it = this.pluginIdsByPrefix.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         if (plugin.getKey().equals(entry.getValue())) {
            it.remove();
         }
      }

   }
}

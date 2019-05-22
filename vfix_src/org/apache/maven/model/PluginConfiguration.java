package org.apache.maven.model;

import java.io.Serializable;

public class PluginConfiguration extends PluginContainer implements Serializable {
   private PluginManagement pluginManagement;

   public PluginManagement getPluginManagement() {
      return this.pluginManagement;
   }

   public void setPluginManagement(PluginManagement pluginManagement) {
      this.pluginManagement = pluginManagement;
   }
}

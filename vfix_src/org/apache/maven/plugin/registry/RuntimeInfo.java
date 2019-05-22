package org.apache.maven.plugin.registry;

import java.io.File;

public class RuntimeInfo {
   private File file;
   private String autoUpdateSourceLevel;
   private String updateIntervalSourceLevel;
   private final PluginRegistry registry;

   public RuntimeInfo(PluginRegistry registry) {
      this.registry = registry;
   }

   public String getAutoUpdateSourceLevel() {
      return this.autoUpdateSourceLevel == null ? this.registry.getSourceLevel() : this.autoUpdateSourceLevel;
   }

   public void setAutoUpdateSourceLevel(String autoUpdateSourceLevel) {
      this.autoUpdateSourceLevel = autoUpdateSourceLevel;
   }

   public File getFile() {
      return this.file;
   }

   public void setFile(File file) {
      this.file = file;
   }

   public String getUpdateIntervalSourceLevel() {
      return this.updateIntervalSourceLevel == null ? this.registry.getSourceLevel() : this.updateIntervalSourceLevel;
   }

   public void setUpdateIntervalSourceLevel(String updateIntervalSourceLevel) {
      this.updateIntervalSourceLevel = updateIntervalSourceLevel;
   }
}

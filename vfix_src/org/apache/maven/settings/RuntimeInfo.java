package org.apache.maven.settings;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RuntimeInfo {
   private File file;
   private Boolean pluginUpdateForced;
   private Boolean applyToAllPluginUpdates;
   private Map activeProfileToSourceLevel = new HashMap();
   private String localRepositorySourceLevel = "user-level";
   private Map pluginGroupIdSourceLevels = new HashMap();
   private final Settings settings;

   public RuntimeInfo(Settings settings) {
      this.settings = settings;
   }

   public void setFile(File file) {
      this.file = file;
   }

   public File getFile() {
      return this.file;
   }

   public void setPluginUpdateOverride(Boolean pluginUpdateForced) {
      this.pluginUpdateForced = pluginUpdateForced;
   }

   public Boolean getPluginUpdateOverride() {
      return this.pluginUpdateForced;
   }

   public Boolean getApplyToAllPluginUpdates() {
      return this.applyToAllPluginUpdates;
   }

   public void setApplyToAllPluginUpdates(Boolean applyToAll) {
      this.applyToAllPluginUpdates = applyToAll;
   }

   public void setActiveProfileSourceLevel(String activeProfile, String sourceLevel) {
      this.activeProfileToSourceLevel.put(activeProfile, sourceLevel);
   }

   public String getSourceLevelForActiveProfile(String activeProfile) {
      String sourceLevel = (String)this.activeProfileToSourceLevel.get(activeProfile);
      return sourceLevel != null ? sourceLevel : this.settings.getSourceLevel();
   }

   public void setPluginGroupIdSourceLevel(String pluginGroupId, String sourceLevel) {
      this.pluginGroupIdSourceLevels.put(pluginGroupId, sourceLevel);
   }

   public String getSourceLevelForPluginGroupId(String pluginGroupId) {
      String sourceLevel = (String)this.pluginGroupIdSourceLevels.get(pluginGroupId);
      return sourceLevel != null ? sourceLevel : this.settings.getSourceLevel();
   }

   public void setLocalRepositorySourceLevel(String localRepoSourceLevel) {
      this.localRepositorySourceLevel = localRepoSourceLevel;
   }

   public String getLocalRepositorySourceLevel() {
      return this.localRepositorySourceLevel;
   }
}

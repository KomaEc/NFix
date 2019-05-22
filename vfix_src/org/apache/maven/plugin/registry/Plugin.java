package org.apache.maven.plugin.registry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Plugin extends TrackableBase implements Serializable {
   private String groupId;
   private String artifactId;
   private String lastChecked;
   private String useVersion;
   private List<String> rejectedVersions;
   public static final String LAST_CHECKED_DATE_FORMAT = "yyyy-MM-dd.HH:mm:ss Z";

   public void addRejectedVersion(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("Plugin.addRejectedVersions(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getRejectedVersions().add(string);
      }
   }

   public String getArtifactId() {
      return this.artifactId;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getLastChecked() {
      return this.lastChecked;
   }

   public List<String> getRejectedVersions() {
      if (this.rejectedVersions == null) {
         this.rejectedVersions = new ArrayList();
      }

      return this.rejectedVersions;
   }

   public String getUseVersion() {
      return this.useVersion;
   }

   public void removeRejectedVersion(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("Plugin.removeRejectedVersions(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getRejectedVersions().remove(string);
      }
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public void setLastChecked(String lastChecked) {
      this.lastChecked = lastChecked;
   }

   public void setRejectedVersions(List<String> rejectedVersions) {
      this.rejectedVersions = rejectedVersions;
   }

   public void setUseVersion(String useVersion) {
      this.useVersion = useVersion;
   }

   public String getKey() {
      return this.getGroupId() + ":" + this.getArtifactId();
   }
}

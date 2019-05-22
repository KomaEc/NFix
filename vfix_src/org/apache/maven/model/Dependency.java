package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dependency implements Serializable {
   private String groupId;
   private String artifactId;
   private String version;
   private String type = "jar";
   private String classifier;
   private String scope;
   private String systemPath;
   private List<Exclusion> exclusions;
   private boolean optional = false;

   public void addExclusion(Exclusion exclusion) {
      if (!(exclusion instanceof Exclusion)) {
         throw new ClassCastException("Dependency.addExclusions(exclusion) parameter must be instanceof " + Exclusion.class.getName());
      } else {
         this.getExclusions().add(exclusion);
      }
   }

   public String getArtifactId() {
      return this.artifactId;
   }

   public String getClassifier() {
      return this.classifier;
   }

   public List<Exclusion> getExclusions() {
      if (this.exclusions == null) {
         this.exclusions = new ArrayList();
      }

      return this.exclusions;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getScope() {
      return this.scope;
   }

   public String getSystemPath() {
      return this.systemPath;
   }

   public String getType() {
      return this.type;
   }

   public String getVersion() {
      return this.version;
   }

   public boolean isOptional() {
      return this.optional;
   }

   public void removeExclusion(Exclusion exclusion) {
      if (!(exclusion instanceof Exclusion)) {
         throw new ClassCastException("Dependency.removeExclusions(exclusion) parameter must be instanceof " + Exclusion.class.getName());
      } else {
         this.getExclusions().remove(exclusion);
      }
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public void setClassifier(String classifier) {
      this.classifier = classifier;
   }

   public void setExclusions(List<Exclusion> exclusions) {
      this.exclusions = exclusions;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public void setOptional(boolean optional) {
      this.optional = optional;
   }

   public void setScope(String scope) {
      this.scope = scope;
   }

   public void setSystemPath(String systemPath) {
      this.systemPath = systemPath;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String toString() {
      return "Dependency {groupId=" + this.groupId + ", artifactId=" + this.artifactId + ", version=" + this.version + ", type=" + this.type + "}";
   }

   public String getManagementKey() {
      return this.groupId + ":" + this.artifactId + ":" + this.type + (this.classifier != null ? ":" + this.classifier : "");
   }
}

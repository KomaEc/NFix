package org.apache.maven.model;

import java.io.Serializable;

public class Exclusion implements Serializable {
   private String artifactId;
   private String groupId;

   public String getArtifactId() {
      return this.artifactId;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }
}

package org.apache.maven.model;

import java.io.Serializable;

public class Relocation implements Serializable {
   private String groupId;
   private String artifactId;
   private String version;
   private String message;

   public String getArtifactId() {
      return this.artifactId;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getMessage() {
      return this.message;
   }

   public String getVersion() {
      return this.version;
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public void setVersion(String version) {
      this.version = version;
   }
}

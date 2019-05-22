package org.apache.maven.artifact.repository.metadata;

import java.io.Serializable;

public class Plugin implements Serializable {
   private String name;
   private String prefix;
   private String artifactId;

   public String getArtifactId() {
      return this.artifactId;
   }

   public String getName() {
      return this.name;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }
}

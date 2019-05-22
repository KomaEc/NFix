package org.apache.maven.artifact.metadata;

import org.apache.maven.artifact.Artifact;

public abstract class AbstractArtifactMetadata implements ArtifactMetadata {
   protected Artifact artifact;

   protected AbstractArtifactMetadata(Artifact artifact) {
      this.artifact = artifact;
   }

   public boolean storedInGroupDirectory() {
      return false;
   }

   public String getGroupId() {
      return this.artifact.getGroupId();
   }

   public String getArtifactId() {
      return this.artifact.getArtifactId();
   }

   public String extendedToString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("\nArtifact Metadata\n--------------------------");
      buffer.append("\nGroupId: ").append(this.getGroupId());
      buffer.append("\nArtifactId: ").append(this.getArtifactId());
      buffer.append("\nMetadata Type: ").append(this.getClass().getName());
      return buffer.toString();
   }
}

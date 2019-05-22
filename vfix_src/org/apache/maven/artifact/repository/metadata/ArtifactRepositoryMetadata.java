package org.apache.maven.artifact.repository.metadata;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;

public class ArtifactRepositoryMetadata extends AbstractRepositoryMetadata {
   private Artifact artifact;

   public ArtifactRepositoryMetadata(Artifact artifact) {
      this(artifact, (Versioning)null);
   }

   public ArtifactRepositoryMetadata(Artifact artifact, Versioning versioning) {
      super(createMetadata(artifact, versioning));
      this.artifact = artifact;
   }

   public boolean storedInGroupDirectory() {
      return false;
   }

   public boolean storedInArtifactVersionDirectory() {
      return false;
   }

   public String getGroupId() {
      return this.artifact.getGroupId();
   }

   public String getArtifactId() {
      return this.artifact.getArtifactId();
   }

   public String getBaseVersion() {
      return null;
   }

   public Object getKey() {
      return "artifact " + this.artifact.getGroupId() + ":" + this.artifact.getArtifactId();
   }

   public boolean isSnapshot() {
      return false;
   }

   public void setRepository(ArtifactRepository remoteRepository) {
      this.artifact.setRepository(remoteRepository);
   }
}

package org.apache.maven.artifact.repository.metadata;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;

public class SnapshotArtifactRepositoryMetadata extends AbstractRepositoryMetadata {
   private Artifact artifact;

   public SnapshotArtifactRepositoryMetadata(Artifact artifact) {
      super(createMetadata(artifact, (Versioning)null));
      this.artifact = artifact;
   }

   public SnapshotArtifactRepositoryMetadata(Artifact artifact, Snapshot snapshot) {
      super(createMetadata(artifact, createVersioning(snapshot)));
      this.artifact = artifact;
   }

   public boolean storedInGroupDirectory() {
      return false;
   }

   public boolean storedInArtifactVersionDirectory() {
      return true;
   }

   public String getGroupId() {
      return this.artifact.getGroupId();
   }

   public String getArtifactId() {
      return this.artifact.getArtifactId();
   }

   public String getBaseVersion() {
      return this.artifact.getBaseVersion();
   }

   public Object getKey() {
      return "snapshot " + this.artifact.getGroupId() + ":" + this.artifact.getArtifactId() + ":" + this.artifact.getBaseVersion();
   }

   public boolean isSnapshot() {
      return this.artifact.isSnapshot();
   }

   public void setRepository(ArtifactRepository remoteRepository) {
      this.artifact.setRepository(remoteRepository);
   }
}

package org.apache.maven.artifact.repository;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.wagon.repository.Repository;

public class DefaultArtifactRepository extends Repository implements ArtifactRepository {
   private final ArtifactRepositoryLayout layout;
   private ArtifactRepositoryPolicy snapshots;
   private ArtifactRepositoryPolicy releases;
   private boolean uniqueVersion;
   private boolean blacklisted;

   public DefaultArtifactRepository(String id, String url, ArtifactRepositoryLayout layout) {
      this(id, url, layout, (ArtifactRepositoryPolicy)null, (ArtifactRepositoryPolicy)null);
   }

   public DefaultArtifactRepository(String id, String url, ArtifactRepositoryLayout layout, boolean uniqueVersion) {
      super(id, url);
      this.layout = layout;
      this.uniqueVersion = uniqueVersion;
   }

   public DefaultArtifactRepository(String id, String url, ArtifactRepositoryLayout layout, ArtifactRepositoryPolicy snapshots, ArtifactRepositoryPolicy releases) {
      super(id, url);
      this.layout = layout;
      if (snapshots == null) {
         snapshots = new ArtifactRepositoryPolicy(true, "always", "ignore");
      }

      this.snapshots = snapshots;
      if (releases == null) {
         releases = new ArtifactRepositoryPolicy(true, "always", "ignore");
      }

      this.releases = releases;
   }

   public String pathOf(Artifact artifact) {
      return this.layout.pathOf(artifact);
   }

   public String pathOfRemoteRepositoryMetadata(ArtifactMetadata artifactMetadata) {
      return this.layout.pathOfRemoteRepositoryMetadata(artifactMetadata);
   }

   public String pathOfLocalRepositoryMetadata(ArtifactMetadata metadata, ArtifactRepository repository) {
      return this.layout.pathOfLocalRepositoryMetadata(metadata, repository);
   }

   public ArtifactRepositoryLayout getLayout() {
      return this.layout;
   }

   public ArtifactRepositoryPolicy getSnapshots() {
      return this.snapshots;
   }

   public ArtifactRepositoryPolicy getReleases() {
      return this.releases;
   }

   public String getKey() {
      return this.getId();
   }

   public boolean isUniqueVersion() {
      return this.uniqueVersion;
   }

   public boolean isBlacklisted() {
      return this.blacklisted;
   }

   public void setBlacklisted(boolean blacklisted) {
      this.blacklisted = blacklisted;
   }
}

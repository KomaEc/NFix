package org.apache.maven.artifact.repository;

import java.util.HashMap;
import java.util.Map;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;

public class DefaultArtifactRepositoryFactory implements ArtifactRepositoryFactory {
   private String globalUpdatePolicy;
   private String globalChecksumPolicy;
   private final Map artifactRepositories = new HashMap();

   public ArtifactRepository createDeploymentArtifactRepository(String id, String url, ArtifactRepositoryLayout repositoryLayout, boolean uniqueVersion) {
      return new DefaultArtifactRepository(id, url, repositoryLayout, uniqueVersion);
   }

   public ArtifactRepository createArtifactRepository(String id, String url, ArtifactRepositoryLayout repositoryLayout, ArtifactRepositoryPolicy snapshots, ArtifactRepositoryPolicy releases) {
      boolean blacklisted = false;
      if (this.artifactRepositories.containsKey(id)) {
         ArtifactRepository repository = (ArtifactRepository)this.artifactRepositories.get(id);
         if (repository.getUrl().equals(url)) {
            blacklisted = repository.isBlacklisted();
         }
      }

      if (snapshots == null) {
         snapshots = new ArtifactRepositoryPolicy();
      }

      if (releases == null) {
         releases = new ArtifactRepositoryPolicy();
      }

      if (this.globalUpdatePolicy != null) {
         snapshots.setUpdatePolicy(this.globalUpdatePolicy);
         releases.setUpdatePolicy(this.globalUpdatePolicy);
      }

      if (this.globalChecksumPolicy != null) {
         snapshots.setChecksumPolicy(this.globalChecksumPolicy);
         releases.setChecksumPolicy(this.globalChecksumPolicy);
      }

      DefaultArtifactRepository repository = new DefaultArtifactRepository(id, url, repositoryLayout, snapshots, releases);
      repository.setBlacklisted(blacklisted);
      this.artifactRepositories.put(id, repository);
      return repository;
   }

   public void setGlobalUpdatePolicy(String updatePolicy) {
      this.globalUpdatePolicy = updatePolicy;
   }

   public void setGlobalChecksumPolicy(String checksumPolicy) {
      this.globalChecksumPolicy = checksumPolicy;
   }
}

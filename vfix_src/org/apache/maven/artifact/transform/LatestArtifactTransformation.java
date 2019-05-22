package org.apache.maven.artifact.transform;

import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataResolutionException;
import org.apache.maven.artifact.repository.metadata.Versioning;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;

public class LatestArtifactTransformation extends AbstractVersionTransformation {
   public void transformForResolve(Artifact artifact, List<ArtifactRepository> remoteRepositories, ArtifactRepository localRepository) throws ArtifactResolutionException, ArtifactNotFoundException {
      if ("LATEST".equals(artifact.getVersion())) {
         try {
            String version = this.resolveVersion(artifact, localRepository, remoteRepositories);
            if ("LATEST".equals(version)) {
               throw new ArtifactNotFoundException("Unable to determine the latest version", artifact);
            }

            artifact.setBaseVersion(version);
            artifact.updateVersion(version, localRepository);
         } catch (RepositoryMetadataResolutionException var5) {
            throw new ArtifactResolutionException(var5.getMessage(), artifact, var5);
         }
      }

   }

   public void transformForInstall(Artifact artifact, ArtifactRepository localRepository) {
   }

   public void transformForDeployment(Artifact artifact, ArtifactRepository remoteRepository, ArtifactRepository localRepository) {
   }

   protected String constructVersion(Versioning versioning, String baseVersion) {
      return versioning.getLatest();
   }
}

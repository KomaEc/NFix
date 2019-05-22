package org.apache.maven.artifact.repository.metadata;

import java.util.List;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;

public interface RepositoryMetadataManager {
   void resolve(RepositoryMetadata var1, List var2, ArtifactRepository var3) throws RepositoryMetadataResolutionException;

   void resolveAlways(RepositoryMetadata var1, ArtifactRepository var2, ArtifactRepository var3) throws RepositoryMetadataResolutionException;

   void deploy(ArtifactMetadata var1, ArtifactRepository var2, ArtifactRepository var3) throws RepositoryMetadataDeploymentException;

   void install(ArtifactMetadata var1, ArtifactRepository var2) throws RepositoryMetadataInstallationException;
}

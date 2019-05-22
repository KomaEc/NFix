package org.apache.maven.artifact.repository;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;

public interface ArtifactRepository {
   String pathOf(Artifact var1);

   String pathOfRemoteRepositoryMetadata(ArtifactMetadata var1);

   String pathOfLocalRepositoryMetadata(ArtifactMetadata var1, ArtifactRepository var2);

   String getUrl();

   String getBasedir();

   String getProtocol();

   String getId();

   ArtifactRepositoryPolicy getSnapshots();

   ArtifactRepositoryPolicy getReleases();

   ArtifactRepositoryLayout getLayout();

   String getKey();

   boolean isUniqueVersion();

   void setBlacklisted(boolean var1);

   boolean isBlacklisted();
}

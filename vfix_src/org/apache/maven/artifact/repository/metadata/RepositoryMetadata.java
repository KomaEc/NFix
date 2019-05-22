package org.apache.maven.artifact.repository.metadata;

import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;

public interface RepositoryMetadata extends ArtifactMetadata {
   void setRepository(ArtifactRepository var1);

   Metadata getMetadata();

   void setMetadata(Metadata var1);

   boolean isSnapshot();
}

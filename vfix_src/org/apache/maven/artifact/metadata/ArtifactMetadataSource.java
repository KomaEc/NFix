package org.apache.maven.artifact.metadata;

import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;

public interface ArtifactMetadataSource {
   String ROLE = ArtifactMetadataSource.class.getName();

   ResolutionGroup retrieve(Artifact var1, ArtifactRepository var2, List var3) throws ArtifactMetadataRetrievalException;

   Artifact retrieveRelocatedArtifact(Artifact var1, ArtifactRepository var2, List var3) throws ArtifactMetadataRetrievalException;

   List retrieveAvailableVersions(Artifact var1, ArtifactRepository var2, List var3) throws ArtifactMetadataRetrievalException;
}

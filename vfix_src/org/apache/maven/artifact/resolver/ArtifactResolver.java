package org.apache.maven.artifact.resolver;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;

public interface ArtifactResolver {
   String ROLE = ArtifactResolver.class.getName();

   void resolve(Artifact var1, List var2, ArtifactRepository var3) throws ArtifactResolutionException, ArtifactNotFoundException;

   ArtifactResolutionResult resolveTransitively(Set var1, Artifact var2, List var3, ArtifactRepository var4, ArtifactMetadataSource var5) throws ArtifactResolutionException, ArtifactNotFoundException;

   ArtifactResolutionResult resolveTransitively(Set var1, Artifact var2, List var3, ArtifactRepository var4, ArtifactMetadataSource var5, List var6) throws ArtifactResolutionException, ArtifactNotFoundException;

   ArtifactResolutionResult resolveTransitively(Set var1, Artifact var2, ArtifactRepository var3, List var4, ArtifactMetadataSource var5, ArtifactFilter var6) throws ArtifactResolutionException, ArtifactNotFoundException;

   ArtifactResolutionResult resolveTransitively(Set var1, Artifact var2, Map var3, ArtifactRepository var4, List var5, ArtifactMetadataSource var6) throws ArtifactResolutionException, ArtifactNotFoundException;

   ArtifactResolutionResult resolveTransitively(Set var1, Artifact var2, Map var3, ArtifactRepository var4, List var5, ArtifactMetadataSource var6, ArtifactFilter var7) throws ArtifactResolutionException, ArtifactNotFoundException;

   ArtifactResolutionResult resolveTransitively(Set var1, Artifact var2, Map var3, ArtifactRepository var4, List var5, ArtifactMetadataSource var6, ArtifactFilter var7, List var8) throws ArtifactResolutionException, ArtifactNotFoundException;

   void resolveAlways(Artifact var1, List var2, ArtifactRepository var3) throws ArtifactResolutionException, ArtifactNotFoundException;
}

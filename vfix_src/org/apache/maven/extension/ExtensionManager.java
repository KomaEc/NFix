package org.apache.maven.extension;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.model.Extension;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusContainerException;

public interface ExtensionManager {
   void addExtension(Extension var1, MavenProject var2, ArtifactRepository var3) throws ArtifactResolutionException, PlexusContainerException, ArtifactNotFoundException;

   void registerWagons();
}

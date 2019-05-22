package org.apache.maven.artifact.transform;

import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.deployer.ArtifactDeploymentException;
import org.apache.maven.artifact.installer.ArtifactInstallationException;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;

public interface ArtifactTransformation {
   String ROLE = ArtifactTransformation.class.getName();

   void transformForResolve(Artifact var1, List<ArtifactRepository> var2, ArtifactRepository var3) throws ArtifactResolutionException, ArtifactNotFoundException;

   void transformForInstall(Artifact var1, ArtifactRepository var2) throws ArtifactInstallationException;

   void transformForDeployment(Artifact var1, ArtifactRepository var2, ArtifactRepository var3) throws ArtifactDeploymentException;
}

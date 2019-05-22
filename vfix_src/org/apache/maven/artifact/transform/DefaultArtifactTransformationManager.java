package org.apache.maven.artifact.transform;

import java.util.Iterator;
import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.deployer.ArtifactDeploymentException;
import org.apache.maven.artifact.installer.ArtifactInstallationException;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

public class DefaultArtifactTransformationManager implements ArtifactTransformationManager, Initializable {
   private List<ArtifactTransformation> artifactTransformations;

   public void initialize() throws InitializationException {
      ArtifactTransformation[] transforms = (ArtifactTransformation[])this.artifactTransformations.toArray(new ArtifactTransformation[0]);

      for(int x = 0; x < transforms.length; ++x) {
         if (transforms[x].getClass().getName().indexOf("Snapshot") != -1) {
            this.artifactTransformations.remove(transforms[x]);
            this.artifactTransformations.add(transforms[x]);
         }
      }

   }

   public void transformForResolve(Artifact artifact, List<ArtifactRepository> remoteRepositories, ArtifactRepository localRepository) throws ArtifactResolutionException, ArtifactNotFoundException {
      Iterator i$ = this.artifactTransformations.iterator();

      while(i$.hasNext()) {
         ArtifactTransformation transform = (ArtifactTransformation)i$.next();
         transform.transformForResolve(artifact, remoteRepositories, localRepository);
      }

   }

   public void transformForInstall(Artifact artifact, ArtifactRepository localRepository) throws ArtifactInstallationException {
      Iterator i$ = this.artifactTransformations.iterator();

      while(i$.hasNext()) {
         ArtifactTransformation transform = (ArtifactTransformation)i$.next();
         transform.transformForInstall(artifact, localRepository);
      }

   }

   public void transformForDeployment(Artifact artifact, ArtifactRepository remoteRepository, ArtifactRepository localRepository) throws ArtifactDeploymentException {
      Iterator i$ = this.artifactTransformations.iterator();

      while(i$.hasNext()) {
         ArtifactTransformation transform = (ArtifactTransformation)i$.next();
         transform.transformForDeployment(artifact, remoteRepository, localRepository);
      }

   }
}

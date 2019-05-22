package org.apache.maven.artifact.deployer;

import java.io.File;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;

public interface ArtifactDeployer {
   String ROLE = ArtifactDeployer.class.getName();

   /** @deprecated */
   void deploy(String var1, String var2, Artifact var3, ArtifactRepository var4, ArtifactRepository var5) throws ArtifactDeploymentException;

   void deploy(File var1, Artifact var2, ArtifactRepository var3, ArtifactRepository var4) throws ArtifactDeploymentException;
}

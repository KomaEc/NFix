package org.apache.maven.artifact.repository;

import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;

public interface ArtifactRepositoryFactory {
   String ROLE = ArtifactRepositoryFactory.class.getName();

   ArtifactRepository createDeploymentArtifactRepository(String var1, String var2, ArtifactRepositoryLayout var3, boolean var4);

   ArtifactRepository createArtifactRepository(String var1, String var2, ArtifactRepositoryLayout var3, ArtifactRepositoryPolicy var4, ArtifactRepositoryPolicy var5);

   void setGlobalUpdatePolicy(String var1);

   void setGlobalChecksumPolicy(String var1);
}

package org.apache.maven.artifact.installer;

import java.io.File;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;

public interface ArtifactInstaller {
   String ROLE = ArtifactInstaller.class.getName();

   /** @deprecated */
   void install(String var1, String var2, Artifact var3, ArtifactRepository var4) throws ArtifactInstallationException;

   void install(File var1, Artifact var2, ArtifactRepository var3) throws ArtifactInstallationException;
}

package org.apache.maven.artifact.resolver;

import org.apache.maven.artifact.Artifact;

/** @deprecated */
public interface ResolutionListenerForDepMgmt {
   void manageArtifactVersion(Artifact var1, Artifact var2);

   void manageArtifactScope(Artifact var1, Artifact var2);
}

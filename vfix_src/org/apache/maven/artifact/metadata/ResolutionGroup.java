package org.apache.maven.artifact.metadata;

import java.util.List;
import java.util.Set;
import org.apache.maven.artifact.Artifact;

public class ResolutionGroup {
   private final Set artifacts;
   private final List resolutionRepositories;
   private final Artifact pomArtifact;

   public ResolutionGroup(Artifact pomArtifact, Set artifacts, List resolutionRepositories) {
      this.pomArtifact = pomArtifact;
      this.artifacts = artifacts;
      this.resolutionRepositories = resolutionRepositories;
   }

   public Artifact getPomArtifact() {
      return this.pomArtifact;
   }

   public Set getArtifacts() {
      return this.artifacts;
   }

   public List getResolutionRepositories() {
      return this.resolutionRepositories;
   }
}

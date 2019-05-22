package org.apache.maven.artifact.resolver.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.maven.artifact.Artifact;

public class ExclusionSetFilter implements ArtifactFilter {
   private Set excludes;

   public ExclusionSetFilter(String[] excludes) {
      this.excludes = new HashSet(Arrays.asList(excludes));
   }

   public ExclusionSetFilter(Set excludes) {
      this.excludes = excludes;
   }

   public boolean include(Artifact artifact) {
      return !this.excludes.contains(artifact.getArtifactId());
   }
}

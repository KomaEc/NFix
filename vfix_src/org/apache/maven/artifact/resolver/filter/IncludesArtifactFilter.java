package org.apache.maven.artifact.resolver.filter;

import java.util.Iterator;
import java.util.List;
import org.apache.maven.artifact.Artifact;

public class IncludesArtifactFilter implements ArtifactFilter {
   private final List patterns;

   public IncludesArtifactFilter(List patterns) {
      this.patterns = patterns;
   }

   public boolean include(Artifact artifact) {
      String id = artifact.getGroupId() + ":" + artifact.getArtifactId();
      boolean matched = false;
      Iterator i = this.patterns.iterator();

      while(i.hasNext() & !matched) {
         if (id.equals(i.next())) {
            matched = true;
         }
      }

      return matched;
   }
}

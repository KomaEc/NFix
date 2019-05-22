package org.apache.maven.artifact.resolver.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.artifact.Artifact;

public class AndArtifactFilter implements ArtifactFilter {
   private final List filters = new ArrayList();

   public boolean include(Artifact artifact) {
      boolean include = true;
      Iterator i = this.filters.iterator();

      while(i.hasNext() && include) {
         ArtifactFilter filter = (ArtifactFilter)i.next();
         if (!filter.include(artifact)) {
            include = false;
         }
      }

      return include;
   }

   public void add(ArtifactFilter artifactFilter) {
      this.filters.add(artifactFilter);
   }
}

package org.apache.maven.artifact.resolver;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ArtifactResolutionResult {
   private Set resolutionNodes;
   private Set artifacts;

   public Set getArtifacts() {
      if (this.artifacts == null) {
         this.artifacts = new LinkedHashSet();
         Iterator it = this.resolutionNodes.iterator();

         while(it.hasNext()) {
            ResolutionNode node = (ResolutionNode)it.next();
            this.artifacts.add(node.getArtifact());
         }
      }

      return this.artifacts;
   }

   public Set getArtifactResolutionNodes() {
      return this.resolutionNodes;
   }

   public void setArtifactResolutionNodes(Set resolutionNodes) {
      this.resolutionNodes = resolutionNodes;
      this.artifacts = null;
   }

   public String toString() {
      return "Artifacts: " + this.artifacts + " Nodes: " + this.resolutionNodes;
   }
}

package org.apache.maven.artifact.resolver.filter;

import org.apache.maven.artifact.Artifact;

public class ScopeArtifactFilter implements ArtifactFilter {
   private final boolean compileScope;
   private final boolean runtimeScope;
   private final boolean testScope;
   private final boolean providedScope;
   private final boolean systemScope;

   public ScopeArtifactFilter(String scope) {
      if ("compile".equals(scope)) {
         this.systemScope = true;
         this.providedScope = true;
         this.compileScope = true;
         this.runtimeScope = false;
         this.testScope = false;
      } else if ("runtime".equals(scope)) {
         this.systemScope = false;
         this.providedScope = false;
         this.compileScope = true;
         this.runtimeScope = true;
         this.testScope = false;
      } else if ("test".equals(scope)) {
         this.systemScope = true;
         this.providedScope = true;
         this.compileScope = true;
         this.runtimeScope = true;
         this.testScope = true;
      } else {
         this.systemScope = false;
         this.providedScope = false;
         this.compileScope = false;
         this.runtimeScope = false;
         this.testScope = false;
      }

   }

   public boolean include(Artifact artifact) {
      if ("compile".equals(artifact.getScope())) {
         return this.compileScope;
      } else if ("runtime".equals(artifact.getScope())) {
         return this.runtimeScope;
      } else if ("test".equals(artifact.getScope())) {
         return this.testScope;
      } else if ("provided".equals(artifact.getScope())) {
         return this.providedScope;
      } else {
         return "system".equals(artifact.getScope()) ? this.systemScope : true;
      }
   }
}

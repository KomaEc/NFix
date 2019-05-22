package org.apache.maven.artifact.handler.manager;

import java.util.Map;
import java.util.Set;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;

public class DefaultArtifactHandlerManager implements ArtifactHandlerManager {
   private Map artifactHandlers;

   public ArtifactHandler getArtifactHandler(String type) {
      ArtifactHandler handler = (ArtifactHandler)this.artifactHandlers.get(type);
      if (handler == null) {
         handler = new DefaultArtifactHandler(type);
      }

      return (ArtifactHandler)handler;
   }

   public void addHandlers(Map handlers) {
      this.artifactHandlers.putAll(handlers);
   }

   public Set getHandlerTypes() {
      return this.artifactHandlers.keySet();
   }
}

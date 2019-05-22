package org.apache.maven.artifact.handler.manager;

import java.util.Map;
import org.apache.maven.artifact.handler.ArtifactHandler;

public interface ArtifactHandlerManager {
   String ROLE = ArtifactHandlerManager.class.getName();

   ArtifactHandler getArtifactHandler(String var1);

   void addHandlers(Map var1);
}

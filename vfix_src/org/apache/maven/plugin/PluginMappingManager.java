package org.apache.maven.plugin;

import java.util.List;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Plugin;

public interface PluginMappingManager {
   Plugin getByPrefix(String var1, List var2, List var3, ArtifactRepository var4);
}

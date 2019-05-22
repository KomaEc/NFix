package org.apache.maven.plugin.version;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.InvalidPluginException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;

public interface PluginVersionManager {
   String ROLE = PluginVersionManager.class.getName();

   String resolvePluginVersion(String var1, String var2, MavenProject var3, Settings var4, ArtifactRepository var5) throws PluginVersionResolutionException, InvalidPluginException, PluginVersionNotFoundException;

   String resolveReportPluginVersion(String var1, String var2, MavenProject var3, Settings var4, ArtifactRepository var5) throws PluginVersionResolutionException, InvalidPluginException, PluginVersionNotFoundException;
}

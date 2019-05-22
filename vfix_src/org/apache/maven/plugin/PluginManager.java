package org.apache.maven.plugin;

import java.util.Map;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.version.PluginVersionNotFoundException;
import org.apache.maven.plugin.version.PluginVersionResolutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;
import org.apache.maven.reporting.MavenReport;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

public interface PluginManager {
   String ROLE = PluginManager.class.getName();

   void executeMojo(MavenProject var1, MojoExecution var2, MavenSession var3) throws MojoExecutionException, ArtifactResolutionException, MojoFailureException, ArtifactNotFoundException, InvalidDependencyVersionException, PluginManagerException, PluginConfigurationException;

   MavenReport getReport(MavenProject var1, MojoExecution var2, MavenSession var3) throws ArtifactNotFoundException, PluginConfigurationException, PluginManagerException, ArtifactResolutionException;

   PluginDescriptor getPluginDescriptorForPrefix(String var1);

   Plugin getPluginDefinitionForPrefix(String var1, MavenSession var2, MavenProject var3);

   PluginDescriptor verifyPlugin(Plugin var1, MavenProject var2, Settings var3, ArtifactRepository var4) throws ArtifactResolutionException, PluginVersionResolutionException, ArtifactNotFoundException, InvalidVersionSpecificationException, InvalidPluginException, PluginManagerException, PluginNotFoundException, PluginVersionNotFoundException;

   PluginDescriptor verifyReportPlugin(ReportPlugin var1, MavenProject var2, MavenSession var3) throws PluginVersionResolutionException, ArtifactResolutionException, ArtifactNotFoundException, InvalidVersionSpecificationException, InvalidPluginException, PluginManagerException, PluginNotFoundException, PluginVersionNotFoundException;

   Object getPluginComponent(Plugin var1, String var2, String var3) throws PluginManagerException, ComponentLookupException;

   Map getPluginComponents(Plugin var1, String var2) throws ComponentLookupException, PluginManagerException;
}

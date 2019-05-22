package org.apache.maven.plugin;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.MavenArtifactFilterManager;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.metadata.ResolutionGroup;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.MultipleArtifactsNotFoundException;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.RuntimeInformation;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.monitor.event.EventDispatcher;
import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.Parameter;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.descriptor.PluginDescriptorBuilder;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.version.PluginVersionManager;
import org.apache.maven.plugin.version.PluginVersionNotFoundException;
import org.apache.maven.plugin.version.PluginVersionResolutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;
import org.apache.maven.project.artifact.MavenMetadataSource;
import org.apache.maven.project.path.PathTranslator;
import org.apache.maven.reporting.MavenReport;
import org.apache.maven.settings.Settings;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.NoSuchRealmException;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ComponentConfigurator;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class DefaultPluginManager extends AbstractLogEnabled implements PluginManager, Initializable, Contextualizable {
   protected PlexusContainer container;
   protected PluginDescriptorBuilder pluginDescriptorBuilder = new PluginDescriptorBuilder();
   protected ArtifactFilter artifactFilter;
   private Log mojoLogger;
   private Map resolvedCoreArtifactFiles = new HashMap();
   protected PathTranslator pathTranslator;
   protected MavenPluginCollector pluginCollector;
   protected PluginVersionManager pluginVersionManager;
   protected ArtifactFactory artifactFactory;
   protected ArtifactResolver artifactResolver;
   protected ArtifactMetadataSource artifactMetadataSource;
   protected RuntimeInformation runtimeInformation;
   protected MavenProjectBuilder mavenProjectBuilder;
   protected PluginMappingManager pluginMappingManager;

   public PluginDescriptor getPluginDescriptorForPrefix(String prefix) {
      return this.pluginCollector.getPluginDescriptorForPrefix(prefix);
   }

   public Plugin getPluginDefinitionForPrefix(String prefix, MavenSession session, MavenProject project) {
      return this.pluginMappingManager.getByPrefix(prefix, session.getSettings().getPluginGroups(), project.getPluginArtifactRepositories(), session.getLocalRepository());
   }

   public PluginDescriptor verifyPlugin(Plugin plugin, MavenProject project, Settings settings, ArtifactRepository localRepository) throws ArtifactResolutionException, PluginVersionResolutionException, ArtifactNotFoundException, InvalidVersionSpecificationException, InvalidPluginException, PluginManagerException, PluginNotFoundException, PluginVersionNotFoundException {
      if (plugin.getVersion() == null) {
         String version = this.pluginVersionManager.resolvePluginVersion(plugin.getGroupId(), plugin.getArtifactId(), project, settings, localRepository);
         plugin.setVersion(version);
      }

      return this.verifyVersionedPlugin(plugin, project, localRepository);
   }

   private PluginDescriptor verifyVersionedPlugin(Plugin plugin, MavenProject project, ArtifactRepository localRepository) throws PluginVersionResolutionException, ArtifactNotFoundException, ArtifactResolutionException, InvalidVersionSpecificationException, InvalidPluginException, PluginManagerException, PluginNotFoundException {
      try {
         VersionRange versionRange = VersionRange.createFromVersionSpec(plugin.getVersion());
         List remoteRepositories = new ArrayList();
         remoteRepositories.addAll(project.getPluginArtifactRepositories());
         remoteRepositories.addAll(project.getRemoteArtifactRepositories());
         this.checkRequiredMavenVersion(plugin, localRepository, remoteRepositories);
         Artifact pluginArtifact = this.artifactFactory.createPluginArtifact(plugin.getGroupId(), plugin.getArtifactId(), versionRange);
         pluginArtifact = project.replaceWithActiveArtifact(pluginArtifact);
         this.artifactResolver.resolve(pluginArtifact, project.getPluginArtifactRepositories(), localRepository);
         PlexusContainer pluginContainer = this.container.getChildContainer(plugin.getKey());
         File pluginFile = pluginArtifact.getFile();
         if (this.pluginCollector.isPluginInstalled(plugin) && pluginContainer != null) {
            if (pluginFile.lastModified() > pluginContainer.getCreationDate().getTime()) {
               this.getLogger().info("Reloading plugin container for: " + plugin.getKey() + ". The plugin artifact has changed.");
               pluginContainer.dispose();
               this.pluginCollector.flushPluginDescriptor(plugin);
               this.addPlugin(plugin, pluginArtifact, project, localRepository);
            }
         } else {
            this.addPlugin(plugin, pluginArtifact, project, localRepository);
         }

         project.addPlugin(plugin);
      } catch (ArtifactNotFoundException var9) {
         String groupId = plugin.getGroupId();
         String artifactId = plugin.getArtifactId();
         String version = plugin.getVersion();
         if (groupId != null && artifactId != null && version != null) {
            if (groupId.equals(var9.getGroupId()) && artifactId.equals(var9.getArtifactId()) && version.equals(var9.getVersion()) && "maven-plugin".equals(var9.getType())) {
               throw new PluginNotFoundException(var9);
            }

            throw var9;
         }

         throw new PluginNotFoundException(var9);
      }

      return this.pluginCollector.getPluginDescriptor(plugin);
   }

   private void checkRequiredMavenVersion(Plugin plugin, ArtifactRepository localRepository, List remoteRepositories) throws PluginVersionResolutionException, InvalidPluginException {
      try {
         Artifact artifact = this.artifactFactory.createProjectArtifact(plugin.getGroupId(), plugin.getArtifactId(), plugin.getVersion());
         MavenProject project = this.mavenProjectBuilder.buildFromRepository(artifact, remoteRepositories, localRepository, false);
         if (project.getPrerequisites() != null && project.getPrerequisites().getMaven() != null) {
            DefaultArtifactVersion requiredVersion = new DefaultArtifactVersion(project.getPrerequisites().getMaven());
            if (this.runtimeInformation.getApplicationVersion().compareTo(requiredVersion) < 0) {
               throw new PluginVersionResolutionException(plugin.getGroupId(), plugin.getArtifactId(), "Plugin requires Maven version " + requiredVersion);
            }
         }

      } catch (ProjectBuildingException var7) {
         throw new InvalidPluginException("Unable to build project for plugin '" + plugin.getKey() + "': " + var7.getMessage(), var7);
      }
   }

   protected void addPlugin(Plugin plugin, Artifact pluginArtifact, MavenProject project, ArtifactRepository localRepository) throws PluginManagerException, InvalidPluginException {
      PlexusContainer child;
      try {
         child = this.container.createChildContainer(plugin.getKey(), Collections.singletonList(pluginArtifact.getFile()), Collections.EMPTY_MAP, Collections.singletonList(this.pluginCollector));

         try {
            child.getContainerRealm().importFrom("plexus.core", "org.codehaus.plexus.util.xml.Xpp3Dom");
            child.getContainerRealm().importFrom("plexus.core", "org.codehaus.plexus.util.xml.pull");
            child.getContainerRealm().importFrom("plexus.core", "/default-report.xml");
         } catch (NoSuchRealmException var10) {
         }
      } catch (PlexusContainerException var11) {
         throw new PluginManagerException("Failed to create plugin container for plugin '" + plugin + "': " + var11.getMessage(), var11);
      }

      PluginDescriptor addedPlugin = this.pluginCollector.getPluginDescriptor(plugin);
      if (addedPlugin == null) {
         throw new IllegalStateException("The PluginDescriptor for the plugin " + plugin + " was not found.");
      } else {
         addedPlugin.setClassRealm(child.getContainerRealm());
         addedPlugin.setArtifacts(Collections.singletonList(pluginArtifact));

         try {
            Plugin projectPlugin = (Plugin)project.getBuild().getPluginsAsMap().get(plugin.getKey());
            if (projectPlugin == null) {
               projectPlugin = plugin;
            }

            Set artifacts = MavenMetadataSource.createArtifacts(this.artifactFactory, projectPlugin.getDependencies(), (String)null, (ArtifactFilter)null, project);
            addedPlugin.setIntroducedDependencyArtifacts(artifacts);
         } catch (InvalidDependencyVersionException var9) {
            throw new InvalidPluginException("Plugin '" + plugin + "' is invalid: " + var9.getMessage(), var9);
         }
      }
   }

   public void executeMojo(MavenProject project, MojoExecution mojoExecution, MavenSession session) throws ArtifactResolutionException, MojoExecutionException, MojoFailureException, ArtifactNotFoundException, InvalidDependencyVersionException, PluginManagerException, PluginConfigurationException {
      MojoDescriptor mojoDescriptor = mojoExecution.getMojoDescriptor();
      if (mojoDescriptor.isProjectRequired() && !session.isUsingPOMsFromFilesystem()) {
         throw new MojoExecutionException("Cannot execute mojo: " + mojoDescriptor.getGoal() + ". It requires a project with an existing pom.xml, but the build is not using one.");
      } else if (mojoDescriptor.isOnlineRequired() && session.getSettings().isOffline()) {
         throw new MojoExecutionException("Mojo: " + mojoDescriptor.getGoal() + " requires online mode for execution. Maven is currently offline.");
      } else {
         if (mojoDescriptor.isDependencyResolutionRequired() != null) {
            Object projects;
            if (mojoDescriptor.isAggregator()) {
               projects = session.getSortedProjects();
            } else {
               projects = Collections.singleton(project);
            }

            Iterator i = ((Collection)projects).iterator();

            while(i.hasNext()) {
               MavenProject p = (MavenProject)i.next();
               this.resolveTransitiveDependencies(session, this.artifactResolver, mojoDescriptor.isDependencyResolutionRequired(), this.artifactFactory, p, mojoDescriptor.isAggregator());
            }

            this.downloadDependencies(project, session, this.artifactResolver);
         }

         String goalName = mojoDescriptor.getFullGoalName();
         PluginDescriptor pluginDescriptor = mojoDescriptor.getPluginDescriptor();
         String goalId = mojoDescriptor.getGoal();
         String groupId = pluginDescriptor.getGroupId();
         String artifactId = pluginDescriptor.getArtifactId();
         String executionId = mojoExecution.getExecutionId();
         Xpp3Dom dom = project.getGoalConfiguration(groupId, artifactId, executionId, goalId);
         Xpp3Dom reportDom = project.getReportConfiguration(groupId, artifactId, executionId);
         dom = Xpp3Dom.mergeXpp3Dom(dom, reportDom);
         if (mojoExecution.getConfiguration() != null) {
            dom = Xpp3Dom.mergeXpp3Dom(dom, mojoExecution.getConfiguration());
         }

         Mojo plugin = this.getConfiguredMojo(session, dom, project, false, mojoExecution);
         String event = "mojo-execute";
         EventDispatcher dispatcher = session.getEventDispatcher();
         String goalExecId = goalName;
         if (mojoExecution.getExecutionId() != null) {
            goalExecId = goalName + " {execution: " + mojoExecution.getExecutionId() + "}";
         }

         dispatcher.dispatchStart(event, goalExecId);
         ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
         boolean var31 = false;

         try {
            var31 = true;
            Thread.currentThread().setContextClassLoader(mojoDescriptor.getPluginDescriptor().getClassRealm().getClassLoader());
            plugin.execute();
            dispatcher.dispatchEnd(event, goalExecId);
            var31 = false;
         } catch (MojoExecutionException var33) {
            session.getEventDispatcher().dispatchError(event, goalExecId, var33);
            throw var33;
         } catch (MojoFailureException var34) {
            session.getEventDispatcher().dispatchError(event, goalExecId, var34);
            throw var34;
         } catch (LinkageError var35) {
            if (this.getLogger().isFatalErrorEnabled()) {
               this.getLogger().fatalError(plugin.getClass().getName() + "#execute() caused a linkage error (" + var35.getClass().getName() + ") and may be out-of-date. Check the realms:");
               ClassRealm pluginRealm = mojoDescriptor.getPluginDescriptor().getClassRealm();
               StringBuffer sb = new StringBuffer();
               sb.append("Plugin realm = " + pluginRealm.getId()).append('\n');

               for(int i = 0; i < pluginRealm.getConstituents().length; ++i) {
                  sb.append("urls[" + i + "] = " + pluginRealm.getConstituents()[i]);
                  if (i != pluginRealm.getConstituents().length - 1) {
                     sb.append('\n');
                  }
               }

               this.getLogger().fatalError(sb.toString());
               ClassRealm containerRealm = this.container.getContainerRealm();
               sb = new StringBuffer();
               sb.append("Container realm = " + containerRealm.getId()).append('\n');

               for(int i = 0; i < containerRealm.getConstituents().length; ++i) {
                  sb.append("urls[" + i + "] = " + containerRealm.getConstituents()[i]);
                  if (i != containerRealm.getConstituents().length - 1) {
                     sb.append('\n');
                  }
               }

               this.getLogger().fatalError(sb.toString());
            }

            session.getEventDispatcher().dispatchError(event, goalExecId, var35);
            throw var35;
         } finally {
            if (var31) {
               Thread.currentThread().setContextClassLoader(oldClassLoader);

               try {
                  PlexusContainer pluginContainer = this.getPluginContainer(mojoDescriptor.getPluginDescriptor());
                  pluginContainer.release(plugin);
               } catch (ComponentLifecycleException var32) {
                  if (this.getLogger().isErrorEnabled()) {
                     this.getLogger().error("Error releasing plugin - ignoring.", var32);
                  }
               }

            }
         }

         Thread.currentThread().setContextClassLoader(oldClassLoader);

         try {
            PlexusContainer pluginContainer = this.getPluginContainer(mojoDescriptor.getPluginDescriptor());
            pluginContainer.release(plugin);
         } catch (ComponentLifecycleException var37) {
            if (this.getLogger().isErrorEnabled()) {
               this.getLogger().error("Error releasing plugin - ignoring.", var37);
            }
         }

      }
   }

   public MavenReport getReport(MavenProject project, MojoExecution mojoExecution, MavenSession session) throws ArtifactNotFoundException, PluginConfigurationException, PluginManagerException, ArtifactResolutionException {
      MojoDescriptor mojoDescriptor = mojoExecution.getMojoDescriptor();
      PluginDescriptor descriptor = mojoDescriptor.getPluginDescriptor();
      Xpp3Dom dom = project.getReportConfiguration(descriptor.getGroupId(), descriptor.getArtifactId(), mojoExecution.getExecutionId());
      if (mojoExecution.getConfiguration() != null) {
         dom = Xpp3Dom.mergeXpp3Dom(dom, mojoExecution.getConfiguration());
      }

      return (MavenReport)this.getConfiguredMojo(session, dom, project, true, mojoExecution);
   }

   public PluginDescriptor verifyReportPlugin(ReportPlugin reportPlugin, MavenProject project, MavenSession session) throws PluginVersionResolutionException, ArtifactResolutionException, ArtifactNotFoundException, InvalidVersionSpecificationException, InvalidPluginException, PluginManagerException, PluginNotFoundException, PluginVersionNotFoundException {
      String version = reportPlugin.getVersion();
      if (version == null) {
         version = this.pluginVersionManager.resolveReportPluginVersion(reportPlugin.getGroupId(), reportPlugin.getArtifactId(), project, session.getSettings(), session.getLocalRepository());
         reportPlugin.setVersion(version);
      }

      Plugin forLookup = new Plugin();
      forLookup.setGroupId(reportPlugin.getGroupId());
      forLookup.setArtifactId(reportPlugin.getArtifactId());
      forLookup.setVersion(version);
      return this.verifyVersionedPlugin(forLookup, project, session.getLocalRepository());
   }

   private PlexusContainer getPluginContainer(PluginDescriptor pluginDescriptor) throws PluginManagerException {
      String pluginKey = pluginDescriptor.getPluginLookupKey();
      PlexusContainer pluginContainer = this.container.getChildContainer(pluginKey);
      if (pluginContainer == null) {
         throw new PluginManagerException("Cannot find Plexus container for plugin: " + pluginKey);
      } else {
         return pluginContainer;
      }
   }

   private Mojo getConfiguredMojo(MavenSession session, Xpp3Dom dom, MavenProject project, boolean report, MojoExecution mojoExecution) throws PluginConfigurationException, ArtifactNotFoundException, PluginManagerException, ArtifactResolutionException {
      MojoDescriptor mojoDescriptor = mojoExecution.getMojoDescriptor();
      PluginDescriptor pluginDescriptor = mojoDescriptor.getPluginDescriptor();
      PlexusContainer pluginContainer = this.getPluginContainer(pluginDescriptor);
      this.ensurePluginContainerIsComplete(pluginDescriptor, pluginContainer, project, session);

      Mojo plugin;
      try {
         plugin = (Mojo)pluginContainer.lookup(Mojo.ROLE, mojoDescriptor.getRoleHint());
         if (report && !(plugin instanceof MavenReport)) {
            return null;
         }
      } catch (ComponentLookupException var14) {
         throw new PluginManagerException("Unable to find the mojo '" + mojoDescriptor.getRoleHint() + "' in the plugin '" + pluginDescriptor.getPluginLookupKey() + "'", var14);
      }

      if (plugin instanceof ContextEnabled) {
         Map pluginContext = session.getPluginContext(pluginDescriptor, project);
         ((ContextEnabled)plugin).setPluginContext(pluginContext);
      }

      plugin.setLog(this.mojoLogger);
      XmlPlexusConfiguration pomConfiguration;
      if (dom == null) {
         pomConfiguration = new XmlPlexusConfiguration("configuration");
      } else {
         pomConfiguration = new XmlPlexusConfiguration(dom);
      }

      this.validatePomConfiguration(mojoDescriptor, pomConfiguration);
      PlexusConfiguration mergedConfiguration = this.mergeMojoConfiguration(pomConfiguration, mojoDescriptor);
      ExpressionEvaluator expressionEvaluator = new PluginParameterExpressionEvaluator(session, mojoExecution, this.pathTranslator, this.getLogger(), project, session.getExecutionProperties());
      PlexusConfiguration extractedMojoConfiguration = this.extractMojoConfiguration(mergedConfiguration, mojoDescriptor);
      this.checkRequiredParameters(mojoDescriptor, extractedMojoConfiguration, expressionEvaluator);
      this.populatePluginFields(plugin, mojoDescriptor, extractedMojoConfiguration, pluginContainer, expressionEvaluator);
      return plugin;
   }

   private void ensurePluginContainerIsComplete(PluginDescriptor pluginDescriptor, PlexusContainer pluginContainer, MavenProject project, MavenSession session) throws ArtifactNotFoundException, PluginManagerException, ArtifactResolutionException {
      if (pluginDescriptor.getArtifacts() != null && pluginDescriptor.getArtifacts().size() == 1) {
         Artifact pluginArtifact = (Artifact)pluginDescriptor.getArtifacts().get(0);
         ArtifactRepository localRepository = session.getLocalRepository();

         ResolutionGroup resolutionGroup;
         try {
            resolutionGroup = this.artifactMetadataSource.retrieve(pluginArtifact, localRepository, project.getPluginArtifactRepositories());
         } catch (ArtifactMetadataRetrievalException var20) {
            throw new ArtifactResolutionException("Unable to download metadata from repository for plugin '" + pluginArtifact.getId() + "': " + var20.getMessage(), pluginArtifact, var20);
         }

         checkPlexusUtils(resolutionGroup, this.artifactFactory);
         Map dependencyMap = new LinkedHashMap();
         List all = new ArrayList();
         all.addAll(pluginDescriptor.getIntroducedDependencyArtifacts());
         all.addAll(resolutionGroup.getArtifacts());
         Iterator it = all.iterator();

         while(it.hasNext()) {
            Artifact artifact = (Artifact)it.next();
            String conflictId = artifact.getDependencyConflictId();
            if (!dependencyMap.containsKey(conflictId)) {
               dependencyMap.put(conflictId, artifact);
            }
         }

         Set dependencies = new LinkedHashSet(dependencyMap.values());
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Plugin dependencies for:\n\n" + pluginDescriptor.getId() + "\n\nare:\n\n" + StringUtils.join(dependencies.iterator(), "\n") + "\n\n");
         }

         List repositories = new ArrayList();
         repositories.addAll(resolutionGroup.getResolutionRepositories());
         repositories.addAll(project.getRemoteArtifactRepositories());
         Object pluginManagedDependencies = new HashMap();

         try {
            MavenProject pluginProject = this.mavenProjectBuilder.buildFromRepository(pluginArtifact, project.getRemoteArtifactRepositories(), localRepository);
            if (pluginProject != null) {
               pluginManagedDependencies = pluginProject.getManagedVersionMap();
            }
         } catch (ProjectBuildingException var19) {
         }

         ArtifactResolutionResult result = this.artifactResolver.resolveTransitively(dependencies, pluginArtifact, (Map)pluginManagedDependencies, localRepository, repositories, this.artifactMetadataSource, this.artifactFilter);
         Set resolved = result.getArtifacts();
         Iterator it = resolved.iterator();

         while(it.hasNext()) {
            Artifact artifact = (Artifact)it.next();
            if (!artifact.equals(pluginArtifact)) {
               artifact = project.replaceWithActiveArtifact(artifact);

               try {
                  pluginContainer.addJarResource(artifact.getFile());
               } catch (PlexusContainerException var18) {
                  throw new PluginManagerException("Error adding plugin dependency '" + artifact.getDependencyConflictId() + "' into plugin manager: " + var18.getMessage(), var18);
               }
            }
         }

         pluginDescriptor.setClassRealm(pluginContainer.getContainerRealm());
         List unresolved = new ArrayList(dependencies);
         unresolved.removeAll(resolved);
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(" The following artifacts were filtered out for plugin: " + pluginDescriptor.getId() + " because they're already in the core of Maven:\n\n" + StringUtils.join(unresolved.iterator(), "\n") + "\n\nThese will use the artifact files already in the core ClassRealm instead, to allow them to be included in PluginDescriptor.getArtifacts().\n\n");
         }

         this.resolveCoreArtifacts(unresolved, localRepository, resolutionGroup.getResolutionRepositories());
         List allResolved = new ArrayList(resolved.size() + unresolved.size());
         allResolved.addAll(resolved);
         allResolved.addAll(unresolved);
         pluginDescriptor.setArtifacts(allResolved);
      }

   }

   public static void checkPlexusUtils(ResolutionGroup resolutionGroup, ArtifactFactory artifactFactory) {
      VersionRange vr = null;

      try {
         vr = VersionRange.createFromVersionSpec("[1.1,)");
      } catch (InvalidVersionSpecificationException var6) {
      }

      boolean plexusUtilsPresent = false;
      Iterator i = resolutionGroup.getArtifacts().iterator();

      while(i.hasNext()) {
         Artifact a = (Artifact)i.next();
         if (a.getArtifactId().equals("plexus-utils") && vr.containsVersion(new DefaultArtifactVersion(a.getVersion()))) {
            plexusUtilsPresent = true;
            break;
         }
      }

      if (!plexusUtilsPresent) {
         resolutionGroup.getArtifacts().add(artifactFactory.createArtifact("org.codehaus.plexus", "plexus-utils", "1.1", "runtime", "jar"));
      }

   }

   private void resolveCoreArtifacts(List unresolved, ArtifactRepository localRepository, List resolutionRepositories) throws ArtifactResolutionException, ArtifactNotFoundException {
      Artifact artifact;
      File artifactFile;
      for(Iterator it = unresolved.iterator(); it.hasNext(); artifact.setFile(artifactFile)) {
         artifact = (Artifact)it.next();
         artifactFile = (File)this.resolvedCoreArtifactFiles.get(artifact.getId());
         if (artifactFile == null) {
            String resource = "/META-INF/maven/" + artifact.getGroupId() + "/" + artifact.getArtifactId() + "/pom.xml";
            URL resourceUrl = this.container.getContainerRealm().getResource(resource);
            if (resourceUrl == null) {
               this.artifactResolver.resolve(artifact, resolutionRepositories, localRepository);
               artifactFile = artifact.getFile();
            } else {
               String artifactPath = resourceUrl.getPath();
               if (artifactPath.startsWith("file:")) {
                  artifactPath = artifactPath.substring("file:".length());
               }

               artifactPath = artifactPath.substring(0, artifactPath.length() - resource.length());
               if (artifactPath.endsWith("/")) {
                  artifactPath = artifactPath.substring(0, artifactPath.length() - 1);
               }

               if (artifactPath.endsWith("!")) {
                  artifactPath = artifactPath.substring(0, artifactPath.length() - 1);
               }

               artifactFile = (new File(artifactPath)).getAbsoluteFile();
            }

            this.resolvedCoreArtifactFiles.put(artifact.getId(), artifactFile);
         }
      }

   }

   private PlexusConfiguration extractMojoConfiguration(PlexusConfiguration mergedConfiguration, MojoDescriptor mojoDescriptor) {
      Map parameterMap = mojoDescriptor.getParameterMap();
      PlexusConfiguration[] mergedChildren = mergedConfiguration.getChildren();
      XmlPlexusConfiguration extractedConfiguration = new XmlPlexusConfiguration("configuration");

      for(int i = 0; i < mergedChildren.length; ++i) {
         PlexusConfiguration child = mergedChildren[i];
         if (parameterMap.containsKey(child.getName())) {
            extractedConfiguration.addChild(copyConfiguration(child));
         } else {
            this.getLogger().debug("*** WARNING: Configuration '" + child.getName() + "' is not used in goal '" + mojoDescriptor.getFullGoalName() + "; this may indicate a typo... ***");
         }
      }

      return extractedConfiguration;
   }

   private void checkRequiredParameters(MojoDescriptor goal, PlexusConfiguration configuration, ExpressionEvaluator expressionEvaluator) throws PluginConfigurationException {
      List parameters = goal.getParameters();
      if (parameters != null) {
         List invalidParameters = new ArrayList();

         for(int i = 0; i < parameters.size(); ++i) {
            Parameter parameter = (Parameter)parameters.get(i);
            if (parameter.isRequired()) {
               String key = parameter.getName();
               Object fieldValue = null;
               String expression = null;
               PlexusConfiguration value = configuration.getChild(key, false);

               try {
                  if (value != null) {
                     expression = value.getValue((String)null);
                     fieldValue = expressionEvaluator.evaluate(expression);
                     if (fieldValue == null) {
                        fieldValue = value.getAttribute("default-value", (String)null);
                     }
                  }

                  if (fieldValue == null && StringUtils.isNotEmpty(parameter.getAlias())) {
                     value = configuration.getChild(parameter.getAlias(), false);
                     if (value != null) {
                        expression = value.getValue((String)null);
                        fieldValue = expressionEvaluator.evaluate(expression);
                        if (fieldValue == null) {
                           fieldValue = value.getAttribute("default-value", (String)null);
                        }
                     }
                  }
               } catch (ExpressionEvaluationException var13) {
                  throw new PluginConfigurationException(goal.getPluginDescriptor(), var13.getMessage(), var13);
               }

               if (fieldValue == null && (value == null || value.getChildCount() == 0)) {
                  parameter.setExpression(expression);
                  invalidParameters.add(parameter);
               }
            }
         }

         if (!invalidParameters.isEmpty()) {
            throw new PluginParameterException(goal, invalidParameters);
         }
      }
   }

   private void validatePomConfiguration(MojoDescriptor goal, PlexusConfiguration pomConfiguration) throws PluginConfigurationException {
      List parameters = goal.getParameters();
      if (parameters != null) {
         for(int i = 0; i < parameters.size(); ++i) {
            Parameter parameter = (Parameter)parameters.get(i);
            String key = parameter.getName();
            PlexusConfiguration value = pomConfiguration.getChild(key, false);
            if (value == null && StringUtils.isNotEmpty(parameter.getAlias())) {
               key = parameter.getAlias();
               value = pomConfiguration.getChild(key, false);
            }

            if (value != null) {
               if (!parameter.isEditable()) {
                  StringBuffer errorMessage = (new StringBuffer()).append("ERROR: Cannot override read-only parameter: ");
                  errorMessage.append(key);
                  errorMessage.append(" in goal: ").append(goal.getFullGoalName());
                  throw new PluginConfigurationException(goal.getPluginDescriptor(), errorMessage.toString());
               }

               String deprecated = parameter.getDeprecated();
               if (StringUtils.isNotEmpty(deprecated)) {
                  this.getLogger().warn("DEPRECATED [" + parameter.getName() + "]: " + deprecated);
               }
            }
         }

      }
   }

   private PlexusConfiguration mergeMojoConfiguration(XmlPlexusConfiguration fromPom, MojoDescriptor mojoDescriptor) {
      XmlPlexusConfiguration result = new XmlPlexusConfiguration(fromPom.getName());
      result.setValue(fromPom.getValue((String)null));
      if (mojoDescriptor.getParameters() != null) {
         PlexusConfiguration fromMojo = mojoDescriptor.getMojoConfiguration();
         Iterator it = mojoDescriptor.getParameters().iterator();

         while(it.hasNext()) {
            Parameter parameter = (Parameter)it.next();
            String paramName = parameter.getName();
            String alias = parameter.getAlias();
            String implementation = parameter.getImplementation();
            PlexusConfiguration pomConfig = fromPom.getChild(paramName);
            PlexusConfiguration aliased = null;
            if (alias != null) {
               aliased = fromPom.getChild(alias);
            }

            PlexusConfiguration mojoConfig = fromMojo.getChild(paramName, false);
            if (aliased != null) {
               if (pomConfig == null) {
                  pomConfig = new XmlPlexusConfiguration(paramName);
               }

               pomConfig = this.buildTopDownMergedConfiguration((PlexusConfiguration)pomConfig, aliased);
            }

            PlexusConfiguration toAdd = null;
            if (pomConfig != null) {
               PlexusConfiguration pomConfig = this.buildTopDownMergedConfiguration((PlexusConfiguration)pomConfig, mojoConfig);
               if (StringUtils.isNotEmpty(pomConfig.getValue((String)null)) || pomConfig.getChildCount() > 0) {
                  toAdd = pomConfig;
               }
            }

            if (toAdd == null && mojoConfig != null) {
               toAdd = copyConfiguration(mojoConfig);
            }

            if (toAdd != null) {
               if (implementation != null && ((PlexusConfiguration)toAdd).getAttribute("implementation", (String)null) == null) {
                  XmlPlexusConfiguration implementationConf = new XmlPlexusConfiguration(paramName);
                  implementationConf.setAttribute("implementation", parameter.getImplementation());
                  toAdd = this.buildTopDownMergedConfiguration((PlexusConfiguration)toAdd, implementationConf);
               }

               result.addChild((PlexusConfiguration)toAdd);
            }
         }
      }

      return result;
   }

   private XmlPlexusConfiguration buildTopDownMergedConfiguration(PlexusConfiguration dominant, PlexusConfiguration recessive) {
      XmlPlexusConfiguration result = new XmlPlexusConfiguration(dominant.getName());
      String value = dominant.getValue((String)null);
      if (StringUtils.isEmpty(value) && recessive != null) {
         value = recessive.getValue((String)null);
      }

      if (StringUtils.isNotEmpty(value)) {
         result.setValue(value);
      }

      String[] attributeNames = dominant.getAttributeNames();

      int i;
      String attributeValue;
      for(i = 0; i < attributeNames.length; ++i) {
         attributeValue = dominant.getAttribute(attributeNames[i], (String)null);
         result.setAttribute(attributeNames[i], attributeValue);
      }

      if (recessive != null) {
         attributeNames = recessive.getAttributeNames();

         for(i = 0; i < attributeNames.length; ++i) {
            attributeValue = recessive.getAttribute(attributeNames[i], (String)null);
            result.setAttribute(attributeNames[i], attributeValue);
         }
      }

      PlexusConfiguration[] children = dominant.getChildren();

      for(int i = 0; i < children.length; ++i) {
         PlexusConfiguration childDom = children[i];
         PlexusConfiguration childRec = recessive == null ? null : recessive.getChild(childDom.getName(), false);
         if (childRec != null) {
            result.addChild(this.buildTopDownMergedConfiguration(childDom, childRec));
         } else {
            result.addChild(copyConfiguration(childDom));
         }
      }

      return result;
   }

   public static PlexusConfiguration copyConfiguration(PlexusConfiguration src) {
      XmlPlexusConfiguration dom = new XmlPlexusConfiguration(src.getName());
      dom.setValue(src.getValue((String)null));
      String[] attributeNames = src.getAttributeNames();

      for(int i = 0; i < attributeNames.length; ++i) {
         String attributeName = attributeNames[i];
         dom.setAttribute(attributeName, src.getAttribute(attributeName, (String)null));
      }

      PlexusConfiguration[] children = src.getChildren();

      for(int i = 0; i < children.length; ++i) {
         dom.addChild(copyConfiguration(children[i]));
      }

      return dom;
   }

   private void populatePluginFields(Mojo plugin, MojoDescriptor mojoDescriptor, PlexusConfiguration configuration, PlexusContainer pluginContainer, ExpressionEvaluator expressionEvaluator) throws PluginConfigurationException {
      ComponentConfigurator configurator = null;

      try {
         String configuratorId = mojoDescriptor.getComponentConfigurator();
         if (StringUtils.isNotEmpty(configuratorId)) {
            configurator = (ComponentConfigurator)pluginContainer.lookup(ComponentConfigurator.ROLE, configuratorId);
         } else {
            configurator = (ComponentConfigurator)pluginContainer.lookup(ComponentConfigurator.ROLE);
         }

         ConfigurationListener listener = new DebugConfigurationListener(this.getLogger());
         this.getLogger().debug("Configuring mojo '" + mojoDescriptor.getId() + "' -->");
         configurator.configureComponent(plugin, configuration, expressionEvaluator, pluginContainer.getContainerRealm(), listener);
         this.getLogger().debug("-- end configuration --");
      } catch (ComponentConfigurationException var21) {
         throw new PluginConfigurationException(mojoDescriptor.getPluginDescriptor(), "Unable to parse the created DOM for plugin configuration", var21);
      } catch (ComponentLookupException var22) {
         throw new PluginConfigurationException(mojoDescriptor.getPluginDescriptor(), "Unable to retrieve component configurator for plugin configuration", var22);
      } catch (LinkageError var23) {
         if (this.getLogger().isFatalErrorEnabled()) {
            this.getLogger().fatalError(configurator.getClass().getName() + "#configureComponent(...) caused a linkage error (" + var23.getClass().getName() + ") and may be out-of-date. Check the realms:");
            ClassRealm pluginRealm = mojoDescriptor.getPluginDescriptor().getClassRealm();
            StringBuffer sb = new StringBuffer();
            sb.append("Plugin realm = " + pluginRealm.getId()).append('\n');

            for(int i = 0; i < pluginRealm.getConstituents().length; ++i) {
               sb.append("urls[" + i + "] = " + pluginRealm.getConstituents()[i]);
               if (i != pluginRealm.getConstituents().length - 1) {
                  sb.append('\n');
               }
            }

            this.getLogger().fatalError(sb.toString());
            ClassRealm containerRealm = this.container.getContainerRealm();
            sb = new StringBuffer();
            sb.append("Container realm = " + containerRealm.getId()).append('\n');

            for(int i = 0; i < containerRealm.getConstituents().length; ++i) {
               sb.append("urls[" + i + "] = " + containerRealm.getConstituents()[i]);
               if (i != containerRealm.getConstituents().length - 1) {
                  sb.append('\n');
               }
            }

            this.getLogger().fatalError(sb.toString());
         }

         throw new PluginConfigurationException(mojoDescriptor.getPluginDescriptor(), var23.getClass().getName() + ": " + var23.getMessage(), var23);
      } finally {
         if (configurator != null) {
            try {
               pluginContainer.release(configurator);
            } catch (ComponentLifecycleException var20) {
               this.getLogger().debug("Failed to release plugin container - ignoring.");
            }
         }

      }

   }

   public static String createPluginParameterRequiredMessage(MojoDescriptor mojo, Parameter parameter, String expression) {
      StringBuffer message = new StringBuffer();
      message.append("The '");
      message.append(parameter.getName());
      message.append("' parameter is required for the execution of the ");
      message.append(mojo.getFullGoalName());
      message.append(" mojo and cannot be null.");
      if (expression != null) {
         message.append(" The retrieval expression was: ").append(expression);
      }

      return message.toString();
   }

   public void contextualize(Context context) throws ContextException {
      this.container = (PlexusContainer)context.get("plexus");
      this.mojoLogger = new DefaultLog(this.container.getLoggerManager().getLoggerForComponent(Mojo.ROLE));
   }

   public void initialize() {
      this.artifactFilter = MavenArtifactFilterManager.createStandardFilter();
   }

   private void resolveTransitiveDependencies(MavenSession context, ArtifactResolver artifactResolver, String scope, ArtifactFactory artifactFactory, MavenProject project, boolean isAggregator) throws ArtifactResolutionException, ArtifactNotFoundException, InvalidDependencyVersionException {
      ArtifactFilter filter = new ScopeArtifactFilter(scope);
      Artifact artifact = artifactFactory.createBuildArtifact(project.getGroupId(), project.getArtifactId(), project.getVersion(), project.getPackaging());
      if (project.getDependencyArtifacts() == null) {
         project.setDependencyArtifacts(project.createArtifacts(artifactFactory, (String)null, (ArtifactFilter)null));
      }

      Object resolvedArtifacts;
      try {
         ArtifactResolutionResult result = artifactResolver.resolveTransitively(project.getDependencyArtifacts(), artifact, project.getManagedVersionMap(), context.getLocalRepository(), project.getRemoteArtifactRepositories(), this.artifactMetadataSource, filter);
         resolvedArtifacts = result.getArtifacts();
      } catch (MultipleArtifactsNotFoundException var11) {
         if (!isAggregator || !this.checkMissingArtifactsInReactor(context.getSortedProjects(), var11.getMissingArtifacts())) {
            throw var11;
         }

         resolvedArtifacts = new HashSet(var11.getResolvedArtifacts());
      }

      project.setArtifacts((Set)resolvedArtifacts);
   }

   private boolean checkMissingArtifactsInReactor(Collection projects, Collection missing) {
      Collection foundInReactor = new HashSet();
      Iterator iter = missing.iterator();

      while(true) {
         while(iter.hasNext()) {
            Artifact mArtifact = (Artifact)iter.next();
            Iterator pIter = projects.iterator();

            while(pIter.hasNext()) {
               MavenProject p = (MavenProject)pIter.next();
               if (p.getArtifactId().equals(mArtifact.getArtifactId()) && p.getGroupId().equals(mArtifact.getGroupId()) && p.getVersion().equals(mArtifact.getVersion())) {
                  this.getLogger().warn("The dependency: " + p.getId() + " can't be resolved but has been found in the reactor.\nThis dependency has been excluded from the plugin execution. You should rerun this mojo after executing mvn install.\n");
                  foundInReactor.add(p);
                  break;
               }
            }
         }

         return foundInReactor.size() == missing.size();
      }
   }

   private void downloadDependencies(MavenProject project, MavenSession context, ArtifactResolver artifactResolver) throws ArtifactResolutionException, ArtifactNotFoundException {
      ArtifactRepository localRepository = context.getLocalRepository();
      List remoteArtifactRepositories = project.getRemoteArtifactRepositories();
      Iterator it = project.getArtifacts().iterator();

      while(it.hasNext()) {
         Artifact artifact = (Artifact)it.next();
         artifactResolver.resolve(artifact, remoteArtifactRepositories, localRepository);
      }

   }

   public Object getPluginComponent(Plugin plugin, String role, String roleHint) throws PluginManagerException, ComponentLookupException {
      PluginDescriptor pluginDescriptor = this.pluginCollector.getPluginDescriptor(plugin);
      PlexusContainer pluginContainer = this.getPluginContainer(pluginDescriptor);
      return pluginContainer.lookup(role, roleHint);
   }

   public Map getPluginComponents(Plugin plugin, String role) throws ComponentLookupException, PluginManagerException {
      PluginDescriptor pluginDescriptor = this.pluginCollector.getPluginDescriptor(plugin);
      PlexusContainer pluginContainer = this.getPluginContainer(pluginDescriptor);
      return pluginContainer.lookupMap(role);
   }
}

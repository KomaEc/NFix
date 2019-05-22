package org.apache.maven.plugin.version;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.metadata.ResolutionGroup;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.execution.RuntimeInformation;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.plugin.InvalidPluginException;
import org.apache.maven.plugin.registry.MavenPluginRegistryBuilder;
import org.apache.maven.plugin.registry.Plugin;
import org.apache.maven.plugin.registry.PluginRegistry;
import org.apache.maven.plugin.registry.PluginRegistryUtils;
import org.apache.maven.plugin.registry.io.xpp3.PluginRegistryXpp3Writer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.settings.RuntimeInfo;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.components.interactivity.InputHandler;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.WriterFactory;
import org.codehaus.plexus.util.xml.XmlStreamWriter;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class DefaultPluginVersionManager extends AbstractLogEnabled implements PluginVersionManager {
   private MavenPluginRegistryBuilder mavenPluginRegistryBuilder;
   private ArtifactFactory artifactFactory;
   private InputHandler inputHandler;
   private ArtifactMetadataSource artifactMetadataSource;
   private PluginRegistry pluginRegistry;
   private MavenProjectBuilder mavenProjectBuilder;
   private RuntimeInformation runtimeInformation;
   private Map resolvedMetaVersions = new HashMap();

   public String resolvePluginVersion(String groupId, String artifactId, MavenProject project, Settings settings, ArtifactRepository localRepository) throws PluginVersionResolutionException, InvalidPluginException, PluginVersionNotFoundException {
      return this.resolvePluginVersion(groupId, artifactId, project, settings, localRepository, false);
   }

   public String resolveReportPluginVersion(String groupId, String artifactId, MavenProject project, Settings settings, ArtifactRepository localRepository) throws PluginVersionResolutionException, InvalidPluginException, PluginVersionNotFoundException {
      return this.resolvePluginVersion(groupId, artifactId, project, settings, localRepository, true);
   }

   private String resolvePluginVersion(String groupId, String artifactId, MavenProject project, Settings settings, ArtifactRepository localRepository, boolean resolveAsReportPlugin) throws PluginVersionResolutionException, InvalidPluginException, PluginVersionNotFoundException {
      String version = this.getVersionFromPluginConfig(groupId, artifactId, project, resolveAsReportPlugin);
      String updatedVersion;
      if (version == null && project.getProjectReferences() != null) {
         updatedVersion = ArtifactUtils.versionlessKey(groupId, artifactId);
         MavenProject ref = (MavenProject)project.getProjectReferences().get(updatedVersion);
         if (ref != null) {
            version = ref.getVersion();
         }
      }

      updatedVersion = null;
      boolean promptToPersist = false;
      RuntimeInfo settingsRTInfo = settings.getRuntimeInfo();
      Boolean pluginUpdateOverride = settingsRTInfo.getPluginUpdateOverride();
      boolean forcePersist;
      if (StringUtils.isEmpty(version) && settings.isUsePluginRegistry()) {
         version = this.resolveExistingFromPluginRegistry(groupId, artifactId);
         if (StringUtils.isNotEmpty(version) && (Boolean.TRUE.equals(pluginUpdateOverride) || !Boolean.FALSE.equals(pluginUpdateOverride) && this.shouldCheckForUpdates(groupId, artifactId))) {
            updatedVersion = this.resolveMetaVersion(groupId, artifactId, project, localRepository, "LATEST");
            if (StringUtils.isNotEmpty(updatedVersion) && !updatedVersion.equals(version)) {
               forcePersist = this.checkForRejectedStatus(groupId, artifactId, updatedVersion);
               promptToPersist = !forcePersist;
               if (forcePersist) {
                  updatedVersion = null;
               } else {
                  this.getLogger().info("Plugin '" + this.constructPluginKey(groupId, artifactId) + "' has updates.");
               }
            }
         }
      }

      forcePersist = false;
      if (StringUtils.isEmpty(version)) {
         version = this.resolveMetaVersion(groupId, artifactId, project, localRepository, "LATEST");
         if (version != null) {
            updatedVersion = version;
            forcePersist = true;
            promptToPersist = false;
         }
      }

      if (StringUtils.isEmpty(version)) {
         version = this.resolveMetaVersion(groupId, artifactId, project, localRepository, "RELEASE");
         if (version != null) {
            updatedVersion = version;
            forcePersist = true;
            promptToPersist = false;
         }
      }

      if (StringUtils.isEmpty(version) && project.getGroupId().equals(groupId) && project.getArtifactId().equals(artifactId)) {
         version = project.getVersion();
      }

      if (StringUtils.isEmpty(version)) {
         throw new PluginVersionNotFoundException(groupId, artifactId);
      } else {
         if (settings.isUsePluginRegistry()) {
            boolean inInteractiveMode = settings.isInteractiveMode();
            String s = this.getPluginRegistry(groupId, artifactId).getAutoUpdate();
            boolean autoUpdate = true;
            if (s != null) {
               autoUpdate = Boolean.valueOf(s);
            }

            boolean persistUpdate = forcePersist || promptToPersist && !Boolean.FALSE.equals(pluginUpdateOverride) && (inInteractiveMode || autoUpdate);
            Boolean applyToAll = settings.getRuntimeInfo().getApplyToAllPluginUpdates();
            promptToPersist = promptToPersist && pluginUpdateOverride == null && applyToAll == null && inInteractiveMode;
            if (promptToPersist) {
               persistUpdate = this.promptToPersistPluginUpdate(version, updatedVersion, groupId, artifactId, settings);
            }

            if (!Boolean.FALSE.equals(applyToAll) && persistUpdate) {
               this.updatePluginVersionInRegistry(groupId, artifactId, updatedVersion);
               version = updatedVersion;
            } else if (promptToPersist) {
               this.addNewVersionToRejectedListInExisting(groupId, artifactId, updatedVersion);
            }
         }

         return version;
      }
   }

   private boolean shouldCheckForUpdates(String groupId, String artifactId) throws PluginVersionResolutionException {
      PluginRegistry pluginRegistry = this.getPluginRegistry(groupId, artifactId);
      Plugin plugin = this.getPlugin(groupId, artifactId, pluginRegistry);
      if (plugin == null) {
         return true;
      } else {
         String lastChecked = plugin.getLastChecked();
         if (StringUtils.isEmpty(lastChecked)) {
            return true;
         } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss Z");

            try {
               Date lastCheckedDate = format.parse(lastChecked);
               return IntervalUtils.isExpired(pluginRegistry.getUpdateInterval(), lastCheckedDate);
            } catch (ParseException var8) {
               this.getLogger().warn("Last-checked date for plugin {" + this.constructPluginKey(groupId, artifactId) + "} is invalid. Checking for updates.");
               return true;
            }
         }
      }
   }

   private boolean checkForRejectedStatus(String groupId, String artifactId, String version) throws PluginVersionResolutionException {
      PluginRegistry pluginRegistry = this.getPluginRegistry(groupId, artifactId);
      Plugin plugin = this.getPlugin(groupId, artifactId, pluginRegistry);
      return plugin.getRejectedVersions().contains(version);
   }

   private boolean promptToPersistPluginUpdate(String version, String updatedVersion, String groupId, String artifactId, Settings settings) throws PluginVersionResolutionException {
      try {
         StringBuffer message = new StringBuffer();
         if (version != null && version.equals(updatedVersion)) {
            message.append("Unregistered plugin detected.\n\n");
         } else {
            message.append("New plugin version detected.\n\n");
         }

         message.append("Group ID: ").append(groupId).append("\n");
         message.append("Artifact ID: ").append(artifactId).append("\n");
         message.append("\n");
         if (version != null && !version.equals(updatedVersion)) {
            message.append("Registered Version: ").append(version).append("\n");
         }

         message.append("Detected plugin version: ").append(updatedVersion).append("\n");
         message.append("\n");
         message.append("Would you like to use this new version from now on? ( [Y]es, [n]o, [a]ll, n[o]ne ) ");
         this.getLogger().info(message.toString());
         String persistAnswer = this.inputHandler.readLine();
         boolean shouldPersist = true;
         if (!StringUtils.isEmpty(persistAnswer)) {
            persistAnswer = persistAnswer.toLowerCase();
            if (persistAnswer.startsWith("y")) {
               shouldPersist = true;
            } else if (persistAnswer.startsWith("a")) {
               shouldPersist = true;
               settings.getRuntimeInfo().setApplyToAllPluginUpdates(Boolean.TRUE);
            } else if (persistAnswer.indexOf("o") > -1) {
               settings.getRuntimeInfo().setApplyToAllPluginUpdates(Boolean.FALSE);
            } else if (persistAnswer.startsWith("n")) {
               shouldPersist = false;
            } else {
               shouldPersist = true;
            }
         }

         if (shouldPersist) {
            this.getLogger().info("Updating plugin version to " + updatedVersion);
         } else {
            this.getLogger().info("NOT updating plugin version to " + updatedVersion);
         }

         return shouldPersist;
      } catch (IOException var9) {
         throw new PluginVersionResolutionException(groupId, artifactId, "Can't read user input.", var9);
      }
   }

   private void addNewVersionToRejectedListInExisting(String groupId, String artifactId, String rejectedVersion) throws PluginVersionResolutionException {
      PluginRegistry pluginRegistry = this.getPluginRegistry(groupId, artifactId);
      Plugin plugin = this.getPlugin(groupId, artifactId, pluginRegistry);
      String pluginKey = this.constructPluginKey(groupId, artifactId);
      if (plugin != null && !"global-level".equals(plugin.getSourceLevel())) {
         plugin.addRejectedVersion(rejectedVersion);
         this.writeUserRegistry(groupId, artifactId, pluginRegistry);
         this.getLogger().warn("Plugin version: " + rejectedVersion + " added to your rejectedVersions list.\n" + "You will not be prompted for this version again.\n\nPlugin: " + pluginKey);
      } else {
         this.getLogger().warn("Cannot add rejectedVersion entry for: " + rejectedVersion + ".\n\nPlugin: " + pluginKey);
      }

   }

   private String resolveExistingFromPluginRegistry(String groupId, String artifactId) throws PluginVersionResolutionException {
      PluginRegistry pluginRegistry = this.getPluginRegistry(groupId, artifactId);
      Plugin plugin = this.getPlugin(groupId, artifactId, pluginRegistry);
      String version = null;
      if (plugin != null) {
         version = plugin.getUseVersion();
      }

      return version;
   }

   private Plugin getPlugin(String groupId, String artifactId, PluginRegistry pluginRegistry) {
      Object pluginsByKey;
      if (pluginRegistry != null) {
         pluginsByKey = pluginRegistry.getPluginsByKey();
      } else {
         pluginsByKey = new HashMap();
      }

      String pluginKey = this.constructPluginKey(groupId, artifactId);
      return (Plugin)((Map)pluginsByKey).get(pluginKey);
   }

   private String constructPluginKey(String groupId, String artifactId) {
      return groupId + ":" + artifactId;
   }

   private String getVersionFromPluginConfig(String groupId, String artifactId, MavenProject project, boolean resolveAsReportPlugin) {
      String version = null;
      Iterator it;
      if (resolveAsReportPlugin) {
         if (project.getReportPlugins() != null) {
            it = project.getReportPlugins().iterator();

            while(it.hasNext() && version == null) {
               ReportPlugin plugin = (ReportPlugin)it.next();
               if (groupId.equals(plugin.getGroupId()) && artifactId.equals(plugin.getArtifactId())) {
                  version = plugin.getVersion();
               }
            }
         }
      } else if (project.getBuildPlugins() != null) {
         it = project.getBuildPlugins().iterator();

         while(it.hasNext() && version == null) {
            org.apache.maven.model.Plugin plugin = (org.apache.maven.model.Plugin)it.next();
            if (groupId.equals(plugin.getGroupId()) && artifactId.equals(plugin.getArtifactId())) {
               version = plugin.getVersion();
            }
         }
      }

      return version;
   }

   private void updatePluginVersionInRegistry(String groupId, String artifactId, String version) throws PluginVersionResolutionException {
      PluginRegistry pluginRegistry = this.getPluginRegistry(groupId, artifactId);
      Plugin plugin = this.getPlugin(groupId, artifactId, pluginRegistry);
      if (plugin != null) {
         if ("global-level".equals(plugin.getSourceLevel())) {
            this.getLogger().warn("Cannot update registered version for plugin {" + groupId + ":" + artifactId + "}; it is specified in the global registry.");
         } else {
            plugin.setUseVersion(version);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss Z");
            plugin.setLastChecked(format.format(new Date()));
         }
      } else {
         plugin = new Plugin();
         plugin.setGroupId(groupId);
         plugin.setArtifactId(artifactId);
         plugin.setUseVersion(version);
         pluginRegistry.addPlugin(plugin);
         pluginRegistry.flushPluginsByKey();
      }

      this.writeUserRegistry(groupId, artifactId, pluginRegistry);
   }

   private void writeUserRegistry(String groupId, String artifactId, PluginRegistry pluginRegistry) {
      File pluginRegistryFile = pluginRegistry.getRuntimeInfo().getFile();
      PluginRegistry extractedUserRegistry = PluginRegistryUtils.extractUserPluginRegistry(pluginRegistry);
      if (extractedUserRegistry != null) {
         XmlStreamWriter fWriter = null;

         try {
            pluginRegistryFile.getParentFile().mkdirs();
            fWriter = WriterFactory.newXmlWriter(pluginRegistryFile);
            PluginRegistryXpp3Writer writer = new PluginRegistryXpp3Writer();
            writer.write(fWriter, extractedUserRegistry);
         } catch (IOException var11) {
            this.getLogger().warn("Cannot rewrite user-level plugin-registry.xml with new plugin version of plugin: '" + groupId + ":" + artifactId + "'.", var11);
         } finally {
            IOUtil.close((Writer)fWriter);
         }
      }

   }

   private PluginRegistry getPluginRegistry(String groupId, String artifactId) throws PluginVersionResolutionException {
      if (this.pluginRegistry == null) {
         try {
            this.pluginRegistry = this.mavenPluginRegistryBuilder.buildPluginRegistry();
         } catch (IOException var4) {
            throw new PluginVersionResolutionException(groupId, artifactId, "Error reading plugin registry: " + var4.getMessage(), var4);
         } catch (XmlPullParserException var5) {
            throw new PluginVersionResolutionException(groupId, artifactId, "Error parsing plugin registry: " + var5.getMessage(), var5);
         }

         if (this.pluginRegistry == null) {
            this.pluginRegistry = this.mavenPluginRegistryBuilder.createUserPluginRegistry();
         }
      }

      return this.pluginRegistry;
   }

   private String resolveMetaVersion(String groupId, String artifactId, MavenProject project, ArtifactRepository localRepository, String metaVersionId) throws PluginVersionResolutionException, InvalidPluginException {
      Artifact artifact = this.artifactFactory.createProjectArtifact(groupId, artifactId, metaVersionId);
      String key = artifact.getDependencyConflictId();
      if (this.resolvedMetaVersions.containsKey(key)) {
         return (String)this.resolvedMetaVersions.get(key);
      } else {
         String version = null;

         try {
            ResolutionGroup resolutionGroup = this.artifactMetadataSource.retrieve(artifact, localRepository, project.getPluginArtifactRepositories());
            artifact = resolutionGroup.getPomArtifact();
         } catch (ArtifactMetadataRetrievalException var19) {
            throw new PluginVersionResolutionException(groupId, artifactId, var19.getMessage(), var19);
         }

         String artifactVersion = artifact.getVersion();
         if (artifact.getFile() != null) {
            boolean pluginValid = false;

            while(!pluginValid && artifactVersion != null) {
               pluginValid = true;

               MavenProject pluginProject;
               try {
                  artifact = this.artifactFactory.createProjectArtifact(groupId, artifactId, artifactVersion);
                  pluginProject = this.mavenProjectBuilder.buildFromRepository(artifact, project.getPluginArtifactRepositories(), localRepository, false);
               } catch (ProjectBuildingException var18) {
                  throw new InvalidPluginException("Unable to build project information for plugin '" + ArtifactUtils.versionlessKey(groupId, artifactId) + "': " + var18.getMessage(), var18);
               }

               if (pluginProject.getPrerequisites() != null && pluginProject.getPrerequisites().getMaven() != null) {
                  DefaultArtifactVersion requiredVersion = new DefaultArtifactVersion(pluginProject.getPrerequisites().getMaven());
                  if (this.runtimeInformation.getApplicationVersion().compareTo(requiredVersion) < 0) {
                     this.getLogger().info("Ignoring available plugin update: " + artifactVersion + " as it requires Maven version " + requiredVersion);

                     VersionRange vr;
                     try {
                        vr = VersionRange.createFromVersionSpec("(," + artifactVersion + ")");
                     } catch (InvalidVersionSpecificationException var17) {
                        throw new PluginVersionResolutionException(groupId, artifactId, "Error getting available plugin versions: " + var17.getMessage(), var17);
                     }

                     this.getLogger().debug("Trying " + vr);

                     try {
                        List versions = this.artifactMetadataSource.retrieveAvailableVersions(artifact, localRepository, project.getPluginArtifactRepositories());
                        ArtifactVersion v = vr.matchVersion(versions);
                        artifactVersion = v != null ? v.toString() : null;
                     } catch (ArtifactMetadataRetrievalException var16) {
                        throw new PluginVersionResolutionException(groupId, artifactId, "Error getting available plugin versions: " + var16.getMessage(), var16);
                     }

                     if (artifactVersion != null) {
                        this.getLogger().debug("Found " + artifactVersion);
                        pluginValid = false;
                     }
                  }
               }
            }
         }

         if (!metaVersionId.equals(artifactVersion)) {
            version = artifactVersion;
            this.resolvedMetaVersions.put(key, artifactVersion);
         }

         return version;
      }
   }
}

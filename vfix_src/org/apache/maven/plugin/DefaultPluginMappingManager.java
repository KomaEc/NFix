package org.apache.maven.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.metadata.GroupRepositoryMetadata;
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadata;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataManager;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataResolutionException;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.logging.AbstractLogEnabled;

public class DefaultPluginMappingManager extends AbstractLogEnabled implements PluginMappingManager {
   protected RepositoryMetadataManager repositoryMetadataManager;
   private Map pluginDefinitionsByPrefix = new HashMap();

   public Plugin getByPrefix(String pluginPrefix, List groupIds, List pluginRepositories, ArtifactRepository localRepository) {
      if (!this.pluginDefinitionsByPrefix.containsKey(pluginPrefix)) {
         this.getLogger().info("Searching repository for plugin with prefix: '" + pluginPrefix + "'.");
         this.loadPluginMappings(groupIds, pluginRepositories, localRepository);
      }

      return (Plugin)this.pluginDefinitionsByPrefix.get(pluginPrefix);
   }

   private void loadPluginMappings(List groupIds, List pluginRepositories, ArtifactRepository localRepository) {
      List pluginGroupIds = new ArrayList(groupIds);
      if (!pluginGroupIds.contains("org.apache.maven.plugins")) {
         pluginGroupIds.add("org.apache.maven.plugins");
      }

      if (!pluginGroupIds.contains("org.codehaus.mojo")) {
         pluginGroupIds.add("org.codehaus.mojo");
      }

      Iterator it = pluginGroupIds.iterator();

      while(it.hasNext()) {
         String groupId = (String)it.next();
         this.getLogger().debug("Loading plugin prefixes from group: " + groupId);

         try {
            this.loadPluginMappings(groupId, pluginRepositories, localRepository);
         } catch (RepositoryMetadataResolutionException var8) {
            this.getLogger().warn("Cannot resolve plugin-mapping metadata for groupId: " + groupId + " - IGNORING.");
            this.getLogger().debug("Error resolving plugin-mapping metadata for groupId: " + groupId + ".", var8);
         }
      }

   }

   private void loadPluginMappings(String groupId, List pluginRepositories, ArtifactRepository localRepository) throws RepositoryMetadataResolutionException {
      RepositoryMetadata metadata = new GroupRepositoryMetadata(groupId);
      this.repositoryMetadataManager.resolve(metadata, pluginRepositories, localRepository);
      Metadata repoMetadata = metadata.getMetadata();
      if (repoMetadata != null) {
         Iterator pluginIterator = repoMetadata.getPlugins().iterator();

         while(pluginIterator.hasNext()) {
            org.apache.maven.artifact.repository.metadata.Plugin mapping = (org.apache.maven.artifact.repository.metadata.Plugin)pluginIterator.next();
            String prefix = mapping.getPrefix();
            if (!this.pluginDefinitionsByPrefix.containsKey(prefix)) {
               String artifactId = mapping.getArtifactId();
               Plugin plugin = new Plugin();
               plugin.setGroupId(metadata.getGroupId());
               plugin.setArtifactId(artifactId);
               this.pluginDefinitionsByPrefix.put(prefix, plugin);
            }
         }
      }

   }
}

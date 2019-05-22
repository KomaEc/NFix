package org.apache.maven.extension;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import org.apache.maven.MavenArtifactFilterManager;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.metadata.ResolutionGroup;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.model.Extension;
import org.apache.maven.plugin.DefaultPluginManager;
import org.apache.maven.project.MavenProject;
import org.apache.maven.wagon.Wagon;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.classworlds.DuplicateRealmException;
import org.codehaus.classworlds.NoSuchRealmException;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;

public class DefaultExtensionManager extends AbstractLogEnabled implements ExtensionManager, Contextualizable {
   private ArtifactResolver artifactResolver;
   private ArtifactFactory artifactFactory;
   private ArtifactMetadataSource artifactMetadataSource;
   private DefaultPlexusContainer container;
   private ArtifactFilter artifactFilter = MavenArtifactFilterManager.createStandardFilter();
   private WagonManager wagonManager;
   private PlexusContainer extensionContainer;
   private static final String CONTAINER_NAME = "extensions";

   public void addExtension(Extension extension, MavenProject project, ArtifactRepository localRepository) throws ArtifactResolutionException, PlexusContainerException, ArtifactNotFoundException {
      String extensionId = ArtifactUtils.versionlessKey(extension.getGroupId(), extension.getArtifactId());
      this.getLogger().debug("Initialising extension: " + extensionId);
      Artifact artifact = (Artifact)project.getExtensionArtifactMap().get(extensionId);
      if (artifact != null) {
         DefaultExtensionManager.ProjectArtifactExceptionFilter filter = new DefaultExtensionManager.ProjectArtifactExceptionFilter(this.artifactFilter, project.getArtifact());

         ResolutionGroup resolutionGroup;
         try {
            resolutionGroup = this.artifactMetadataSource.retrieve(artifact, localRepository, project.getRemoteArtifactRepositories());
         } catch (ArtifactMetadataRetrievalException var13) {
            throw new ArtifactResolutionException("Unable to download metadata from repository for plugin '" + artifact.getId() + "': " + var13.getMessage(), artifact, var13);
         }

         DefaultPluginManager.checkPlexusUtils(resolutionGroup, this.artifactFactory);
         Set dependencies = new HashSet(resolutionGroup.getArtifacts());
         dependencies.add(artifact);
         ArtifactResolutionResult result = this.artifactResolver.resolveTransitively(dependencies, project.getArtifact(), Collections.EMPTY_MAP, localRepository, project.getRemoteArtifactRepositories(), this.artifactMetadataSource, filter);
         Set artifacts = result.getArtifacts();
         Iterator i;
         Artifact a;
         if (this.extensionContainsLifeycle(artifact.getFile())) {
            i = artifacts.iterator();

            while(i.hasNext()) {
               a = (Artifact)i.next();
               if (this.artifactFilter.include(a)) {
                  this.getLogger().debug("Adding extension to core container: " + a.getFile());
                  this.container.addJarResource(a.getFile());
               }
            }
         } else if (artifacts.size() == 2) {
            i = artifacts.iterator();

            while(i.hasNext()) {
               a = (Artifact)i.next();
               if (!a.getArtifactId().equals("plexus-utils")) {
                  a = project.replaceWithActiveArtifact(a);
                  this.getLogger().debug("Adding extension to core container: " + a.getFile());
                  this.container.addJarResource(a.getFile());
               }
            }
         } else {
            if (this.extensionContainer == null) {
               this.extensionContainer = this.createContainer();
            }

            i = result.getArtifacts().iterator();

            while(i.hasNext()) {
               a = (Artifact)i.next();
               a = project.replaceWithActiveArtifact(a);
               this.getLogger().debug("Adding to extension classpath: " + a.getFile());
               this.extensionContainer.addJarResource(a.getFile());
            }

            if (this.getLogger().isDebugEnabled()) {
               this.extensionContainer.getContainerRealm().display();
            }
         }
      }

   }

   private PlexusContainer createContainer() throws PlexusContainerException {
      DefaultPlexusContainer child = new DefaultPlexusContainer();
      ClassWorld classWorld = this.container.getClassWorld();
      child.setClassWorld(classWorld);
      ClassRealm childRealm = null;
      String childRealmId = "plexus.core.child-container[extensions]";

      try {
         childRealm = classWorld.getRealm(childRealmId);
      } catch (NoSuchRealmException var8) {
         try {
            childRealm = classWorld.newRealm(childRealmId);
         } catch (DuplicateRealmException var7) {
            this.getLogger().error("An impossible error has occurred. After getRealm() failed, newRealm() produced duplication error on same id!", var7);
         }
      }

      childRealm.setParent(this.container.getContainerRealm());
      child.setCoreRealm(childRealm);
      child.setName("extensions");
      child.setLoggerManager(this.container.getLoggerManager());
      child.initialize();
      child.start();
      return child;
   }

   public void registerWagons() {
      if (this.extensionContainer != null) {
         try {
            Map wagons = this.extensionContainer.lookupMap(Wagon.ROLE);
            this.wagonManager.registerWagons(wagons.keySet(), this.extensionContainer);
         } catch (ComponentLookupException var2) {
         }
      }

   }

   public void contextualize(Context context) throws ContextException {
      this.container = (DefaultPlexusContainer)context.get("plexus");
   }

   private boolean extensionContainsLifeycle(File extension) {
      try {
         JarFile f = new JarFile(extension);
         InputStream is = f.getInputStream(f.getEntry("META-INF/plexus/components.xml"));
         if (is == null) {
            return false;
         }

         Xpp3Dom dom = Xpp3DomBuilder.build((Reader)(new InputStreamReader(is)));
         Xpp3Dom[] components = dom.getChild("components").getChildren("component");

         for(int i = 0; i < components.length; ++i) {
            if (components[i].getChild("role").getValue().equals("org.apache.maven.lifecycle.mapping.LifecycleMapping")) {
               return true;
            }
         }
      } catch (Exception var7) {
      }

      return false;
   }

   private static final class ProjectArtifactExceptionFilter implements ArtifactFilter {
      private ArtifactFilter passThroughFilter;
      private String projectDependencyConflictId;

      ProjectArtifactExceptionFilter(ArtifactFilter passThroughFilter, Artifact projectArtifact) {
         this.passThroughFilter = passThroughFilter;
         this.projectDependencyConflictId = projectArtifact.getDependencyConflictId();
      }

      public boolean include(Artifact artifact) {
         String depConflictId = artifact.getDependencyConflictId();
         return this.projectDependencyConflictId.equals(depConflictId) || this.passThroughFilter.include(artifact);
      }
   }
}

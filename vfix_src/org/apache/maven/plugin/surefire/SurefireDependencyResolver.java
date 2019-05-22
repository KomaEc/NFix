package org.apache.maven.plugin.surefire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ExcludesArtifactFilter;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.surefire.booter.Classpath;

public class SurefireDependencyResolver {
   private final ArtifactResolver artifactResolver;
   private final ArtifactFactory artifactFactory;
   private final Log log;
   private final ArtifactRepository localRepository;
   private final List<ArtifactRepository> remoteRepositories;
   private final ArtifactMetadataSource artifactMetadataSource;
   private final String pluginName;

   protected SurefireDependencyResolver(ArtifactResolver artifactResolver, ArtifactFactory artifactFactory, Log log, ArtifactRepository localRepository, List<ArtifactRepository> remoteRepositories, ArtifactMetadataSource artifactMetadataSource, String pluginName) {
      this.artifactResolver = artifactResolver;
      this.artifactFactory = artifactFactory;
      this.log = log;
      this.localRepository = localRepository;
      this.remoteRepositories = remoteRepositories;
      this.artifactMetadataSource = artifactMetadataSource;
      this.pluginName = pluginName;
   }

   public boolean isWithinVersionSpec(@Nullable Artifact artifact, @Nonnull String versionSpec) {
      if (artifact == null) {
         return false;
      } else {
         try {
            VersionRange range = VersionRange.createFromVersionSpec(versionSpec);

            try {
               return range.containsVersion(artifact.getSelectedVersion());
            } catch (NullPointerException var5) {
               return range.containsVersion(new DefaultArtifactVersion(artifact.getBaseVersion()));
            }
         } catch (InvalidVersionSpecificationException var6) {
            throw new RuntimeException("Bug in plugin. Please report with stacktrace");
         } catch (OverConstrainedVersionException var7) {
            throw new RuntimeException("Bug in plugin. Please report with stacktrace");
         }
      }
   }

   public ArtifactResolutionResult resolveArtifact(@Nullable Artifact filteredArtifact, Artifact providerArtifact) throws ArtifactResolutionException, ArtifactNotFoundException {
      ArtifactFilter filter = null;
      if (filteredArtifact != null) {
         filter = new ExcludesArtifactFilter(Collections.singletonList(filteredArtifact.getGroupId() + ":" + filteredArtifact.getArtifactId()));
      }

      Artifact originatingArtifact = this.artifactFactory.createBuildArtifact("dummy", "dummy", "1.0", "jar");
      return this.artifactResolver.resolveTransitively(Collections.singleton(providerArtifact), originatingArtifact, (ArtifactRepository)this.localRepository, (List)this.remoteRepositories, (ArtifactMetadataSource)this.artifactMetadataSource, (ArtifactFilter)filter);
   }

   public Classpath getProviderClasspath(String provider, String version, Artifact filteredArtifact) throws ArtifactNotFoundException, ArtifactResolutionException {
      Classpath classPath = ClasspathCache.getCachedClassPath(provider);
      if (classPath == null) {
         Artifact providerArtifact = this.artifactFactory.createDependencyArtifact("org.apache.maven.surefire", provider, VersionRange.createFromVersion(version), "jar", (String)null, "test");
         ArtifactResolutionResult result = this.resolveArtifact(filteredArtifact, providerArtifact);
         List<String> files = new ArrayList();
         Iterator i$ = result.getArtifacts().iterator();

         while(i$.hasNext()) {
            Object o = i$.next();
            Artifact artifact = (Artifact)o;
            this.log.debug((CharSequence)("Adding to " + this.pluginName + " test classpath: " + artifact.getFile().getAbsolutePath() + " Scope: " + artifact.getScope()));
            files.add(artifact.getFile().getAbsolutePath());
         }

         classPath = new Classpath(files);
         ClasspathCache.setCachedClasspath(provider, classPath);
      }

      return classPath;
   }

   public Classpath addProviderToClasspath(Map<String, Artifact> pluginArtifactMap, Artifact surefireArtifact) throws ArtifactResolutionException, ArtifactNotFoundException {
      List<String> files = new ArrayList();
      if (surefireArtifact != null) {
         ArtifactResolutionResult artifactResolutionResult = this.resolveArtifact((Artifact)null, surefireArtifact);
         Iterator i$ = pluginArtifactMap.values().iterator();

         while(i$.hasNext()) {
            Artifact artifact = (Artifact)i$.next();
            if (!artifactResolutionResult.getArtifacts().contains(artifact)) {
               files.add(artifact.getFile().getAbsolutePath());
            }
         }
      } else {
         Iterator i$ = pluginArtifactMap.values().iterator();

         while(i$.hasNext()) {
            Artifact artifact = (Artifact)i$.next();
            files.add(artifact.getFile().getPath());
         }
      }

      return new Classpath(files);
   }
}

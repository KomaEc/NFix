package org.apache.maven.project.artifact;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.metadata.ResolutionGroup;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.metadata.ArtifactRepositoryMetadata;
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataManager;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataResolutionException;
import org.apache.maven.artifact.resolver.filter.AndArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ExcludesArtifactFilter;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Relocation;
import org.apache.maven.project.DefaultProjectBuilderConfiguration;
import org.apache.maven.project.InvalidProjectModelException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuilderConfiguration;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.validation.ModelValidationResult;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.StringUtils;

public class MavenMetadataSource extends AbstractLogEnabled implements ArtifactMetadataSource {
   public static final String ROLE_HINT = "maven";
   private MavenProjectBuilder mavenProjectBuilder;
   private ArtifactFactory artifactFactory;
   private RepositoryMetadataManager repositoryMetadataManager;
   private MavenProject superProject;
   private Set warnedPoms = new HashSet();

   public Artifact retrieveRelocatedArtifact(Artifact artifact, ArtifactRepository localRepository, List remoteRepositories) throws ArtifactMetadataRetrievalException {
      if (artifact instanceof ActiveProjectArtifact) {
         return artifact;
      } else {
         MavenMetadataSource.ProjectRelocation rel = this.retrieveRelocatedProject(artifact, localRepository, remoteRepositories);
         if (rel == null) {
            return artifact;
         } else {
            MavenProject project = rel.project;
            if (project != null && !this.getRelocationKey(artifact).equals(this.getRelocationKey(project.getArtifact()))) {
               Artifact result = null;
               if (artifact.getClassifier() != null) {
                  result = this.artifactFactory.createArtifactWithClassifier(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getType(), artifact.getClassifier());
               } else {
                  result = this.artifactFactory.createArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getScope(), artifact.getType());
               }

               result.setResolved(artifact.isResolved());
               result.setFile(artifact.getFile());
               result.setScope(artifact.getScope());
               result.setArtifactHandler(artifact.getArtifactHandler());
               result.setDependencyFilter(artifact.getDependencyFilter());
               result.setDependencyTrail(artifact.getDependencyTrail());
               result.setOptional(artifact.isOptional());
               result.setRelease(artifact.isRelease());
               return result;
            } else {
               return artifact;
            }
         }
      }
   }

   private String getRelocationKey(Artifact artifact) {
      return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
   }

   private MavenMetadataSource.ProjectRelocation retrieveRelocatedProject(Artifact artifact, ArtifactRepository localRepository, List remoteRepositories) throws ArtifactMetadataRetrievalException {
      MavenProject project = null;
      boolean done = false;

      Artifact pomArtifact;
      do {
         pomArtifact = this.artifactFactory.createProjectArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getScope());
         if ("system".equals(artifact.getScope())) {
            done = true;
         } else {
            try {
               project = this.mavenProjectBuilder.buildFromRepository(pomArtifact, remoteRepositories, localRepository, true);
            } catch (InvalidProjectModelException var11) {
               String id = pomArtifact.getId();
               if (!this.warnedPoms.contains(id)) {
                  this.warnedPoms.add(pomArtifact.getId());
                  this.getLogger().warn("POM for '" + pomArtifact + "' is invalid.\n\nIts dependencies (if any) will NOT be available to the current build.");
                  if (this.getLogger().isDebugEnabled()) {
                     this.getLogger().debug("Reason: " + var11.getMessage());
                     ModelValidationResult validationResult = var11.getValidationResult();
                     if (validationResult != null) {
                        this.getLogger().debug("\nValidation Errors:");
                        Iterator i = validationResult.getMessages().iterator();

                        while(i.hasNext()) {
                           this.getLogger().debug(i.next().toString());
                        }

                        this.getLogger().debug("\n");
                     }
                  }
               }

               project = null;
            } catch (ProjectBuildingException var12) {
               throw new ArtifactMetadataRetrievalException("Unable to read the metadata file for artifact '" + artifact.getDependencyConflictId() + "': " + var12.getMessage(), var12, artifact);
            }

            if (project != null) {
               Relocation relocation = null;
               DistributionManagement distMgmt = project.getDistributionManagement();
               if (distMgmt != null) {
                  relocation = distMgmt.getRelocation();
                  artifact.setDownloadUrl(distMgmt.getDownloadUrl());
                  pomArtifact.setDownloadUrl(distMgmt.getDownloadUrl());
               }

               if (relocation != null) {
                  if (relocation.getGroupId() != null) {
                     artifact.setGroupId(relocation.getGroupId());
                     project.setGroupId(relocation.getGroupId());
                  }

                  if (relocation.getArtifactId() != null) {
                     artifact.setArtifactId(relocation.getArtifactId());
                     project.setArtifactId(relocation.getArtifactId());
                  }

                  if (relocation.getVersion() != null) {
                     artifact.setVersionRange(VersionRange.createFromVersion(relocation.getVersion()));
                     project.setVersion(relocation.getVersion());
                  }

                  if (artifact.getDependencyFilter() != null && !artifact.getDependencyFilter().include(artifact)) {
                     return null;
                  }

                  List available = artifact.getAvailableVersions();
                  if (available != null && !available.isEmpty()) {
                     artifact.setAvailableVersions(this.retrieveAvailableVersions(artifact, localRepository, remoteRepositories));
                  }

                  String message = "\n  This artifact has been relocated to " + artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion() + ".\n";
                  if (relocation.getMessage() != null) {
                     message = message + "  " + relocation.getMessage() + "\n";
                  }

                  if (artifact.getDependencyTrail() != null && artifact.getDependencyTrail().size() == 1) {
                     this.getLogger().warn("While downloading " + pomArtifact.getGroupId() + ":" + pomArtifact.getArtifactId() + ":" + pomArtifact.getVersion() + message + "\n");
                  } else {
                     this.getLogger().debug("While downloading " + pomArtifact.getGroupId() + ":" + pomArtifact.getArtifactId() + ":" + pomArtifact.getVersion() + message + "\n");
                  }
               } else {
                  done = true;
               }
            } else {
               done = true;
            }
         }
      } while(!done);

      MavenMetadataSource.ProjectRelocation rel = new MavenMetadataSource.ProjectRelocation();
      rel.project = project;
      rel.pomArtifact = pomArtifact;
      return rel;
   }

   public ResolutionGroup retrieve(Artifact artifact, ArtifactRepository localRepository, List remoteRepositories) throws ArtifactMetadataRetrievalException {
      MavenMetadataSource.ProjectRelocation rel = this.retrieveRelocatedProject(artifact, localRepository, remoteRepositories);
      if (rel == null) {
         return null;
      } else {
         MavenProject project = rel.project;
         Artifact pomArtifact = rel.pomArtifact;
         if (artifact.getDownloadUrl() == null && pomArtifact != null) {
            artifact.setDownloadUrl(pomArtifact.getDownloadUrl());
         }

         ResolutionGroup result;
         if (project == null) {
            result = new ResolutionGroup(pomArtifact, Collections.EMPTY_SET, Collections.EMPTY_LIST);
         } else {
            Set artifacts = Collections.EMPTY_SET;
            if (!artifact.getArtifactHandler().isIncludesDependencies()) {
               try {
                  artifacts = project.createArtifacts(this.artifactFactory, artifact.getScope(), artifact.getDependencyFilter());
               } catch (InvalidDependencyVersionException var10) {
                  throw new ArtifactMetadataRetrievalException("Error in metadata for artifact '" + artifact.getDependencyConflictId() + "': " + var10.getMessage(), var10);
               }
            }

            List repositories = this.aggregateRepositoryLists(remoteRepositories, project.getRemoteArtifactRepositories());
            result = new ResolutionGroup(pomArtifact, artifacts, repositories);
         }

         return result;
      }
   }

   private List aggregateRepositoryLists(List remoteRepositories, List remoteArtifactRepositories) throws ArtifactMetadataRetrievalException {
      if (this.superProject == null) {
         try {
            this.superProject = this.mavenProjectBuilder.buildStandaloneSuperProject((ProjectBuilderConfiguration)(new DefaultProjectBuilderConfiguration()));
         } catch (ProjectBuildingException var8) {
            throw new ArtifactMetadataRetrievalException("Unable to parse the Maven built-in model: " + var8.getMessage(), var8);
         }
      }

      List repositories = new ArrayList();
      repositories.addAll(remoteRepositories);
      Iterator it = this.superProject.getRemoteArtifactRepositories().iterator();

      ArtifactRepository repository;
      while(it.hasNext()) {
         repository = (ArtifactRepository)it.next();
         Iterator aggregatedIterator = repositories.iterator();

         while(aggregatedIterator.hasNext()) {
            ArtifactRepository repo = (ArtifactRepository)aggregatedIterator.next();
            if (repo.getId().equals(repository.getId()) && repo.getUrl().equals(repository.getUrl())) {
               aggregatedIterator.remove();
            }
         }
      }

      it = remoteArtifactRepositories.iterator();

      while(it.hasNext()) {
         repository = (ArtifactRepository)it.next();
         if (!repositories.contains(repository)) {
            repositories.add(repository);
         }
      }

      return repositories;
   }

   public static Set createArtifacts(ArtifactFactory artifactFactory, List dependencies, String inheritedScope, ArtifactFilter dependencyFilter, MavenProject project) throws InvalidDependencyVersionException {
      Set projectArtifacts = new LinkedHashSet(dependencies.size());
      Iterator i = dependencies.iterator();

      while(true) {
         Dependency d;
         Artifact artifact;
         Object artifactFilter;
         do {
            do {
               if (!i.hasNext()) {
                  return projectArtifacts;
               }

               d = (Dependency)i.next();
               String scope = d.getScope();
               if (StringUtils.isEmpty(scope)) {
                  scope = "compile";
                  d.setScope(scope);
               }

               VersionRange versionRange;
               try {
                  versionRange = VersionRange.createFromVersionSpec(d.getVersion());
               } catch (InvalidVersionSpecificationException var15) {
                  throw new InvalidDependencyVersionException("Unable to parse version '" + d.getVersion() + "' for dependency '" + d.getManagementKey() + "': " + var15.getMessage(), var15);
               }

               artifact = artifactFactory.createDependencyArtifact(d.getGroupId(), d.getArtifactId(), versionRange, d.getType(), d.getClassifier(), scope, inheritedScope, d.isOptional());
               if ("system".equals(scope)) {
                  artifact.setFile(new File(d.getSystemPath()));
               }

               artifactFilter = dependencyFilter;
            } while(artifact == null);
         } while(dependencyFilter != null && !dependencyFilter.include(artifact));

         if (d.getExclusions() != null && !d.getExclusions().isEmpty()) {
            List exclusions = new ArrayList();
            Iterator j = d.getExclusions().iterator();

            while(j.hasNext()) {
               Exclusion e = (Exclusion)j.next();
               exclusions.add(e.getGroupId() + ":" + e.getArtifactId());
            }

            ArtifactFilter newFilter = new ExcludesArtifactFilter(exclusions);
            if (dependencyFilter != null) {
               AndArtifactFilter filter = new AndArtifactFilter();
               filter.add(dependencyFilter);
               filter.add(newFilter);
               artifactFilter = filter;
            } else {
               artifactFilter = newFilter;
            }
         }

         artifact.setDependencyFilter((ArtifactFilter)artifactFilter);
         if (project != null) {
            artifact = project.replaceWithActiveArtifact(artifact);
         }

         projectArtifacts.add(artifact);
      }
   }

   public List retrieveAvailableVersions(Artifact artifact, ArtifactRepository localRepository, List remoteRepositories) throws ArtifactMetadataRetrievalException {
      ArtifactRepositoryMetadata metadata = new ArtifactRepositoryMetadata(artifact);

      try {
         this.repositoryMetadataManager.resolve(metadata, remoteRepositories, localRepository);
      } catch (RepositoryMetadataResolutionException var10) {
         throw new ArtifactMetadataRetrievalException(var10.getMessage(), var10);
      }

      Metadata repoMetadata = metadata.getMetadata();
      Object versions;
      if (repoMetadata != null && repoMetadata.getVersioning() != null) {
         List metadataVersions = repoMetadata.getVersioning().getVersions();
         versions = new ArrayList(metadataVersions.size());
         Iterator i = metadataVersions.iterator();

         while(i.hasNext()) {
            String version = (String)i.next();
            ((List)versions).add(new DefaultArtifactVersion(version));
         }
      } else {
         versions = Collections.EMPTY_LIST;
      }

      return (List)versions;
   }

   private static final class ProjectRelocation {
      private MavenProject project;
      private Artifact pomArtifact;

      private ProjectRelocation() {
      }

      // $FF: synthetic method
      ProjectRelocation(Object x0) {
         this();
      }
   }
}

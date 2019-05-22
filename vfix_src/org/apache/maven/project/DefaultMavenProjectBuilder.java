package org.apache.maven.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactStatus;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.InvalidRepositoryException;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ExcludesArtifactFilter;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.ManagedVersionMap;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.Profile;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.Repository;
import org.apache.maven.model.Resource;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.profiles.DefaultProfileManager;
import org.apache.maven.profiles.MavenProfilesBuilder;
import org.apache.maven.profiles.ProfileManager;
import org.apache.maven.profiles.ProfilesConversionUtils;
import org.apache.maven.profiles.ProfilesRoot;
import org.apache.maven.profiles.activation.ProfileActivationException;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;
import org.apache.maven.project.artifact.ProjectArtifactFactory;
import org.apache.maven.project.inheritance.ModelInheritanceAssembler;
import org.apache.maven.project.injection.ModelDefaultsInjector;
import org.apache.maven.project.injection.ProfileInjector;
import org.apache.maven.project.interpolation.ModelInterpolationException;
import org.apache.maven.project.interpolation.ModelInterpolator;
import org.apache.maven.project.path.PathTranslator;
import org.apache.maven.project.validation.ModelValidationResult;
import org.apache.maven.project.validation.ModelValidator;
import org.apache.maven.wagon.events.TransferListener;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class DefaultMavenProjectBuilder extends AbstractLogEnabled implements MavenProjectBuilder, Initializable, Contextualizable {
   private PlexusContainer container;
   protected MavenProfilesBuilder profilesBuilder;
   protected ArtifactResolver artifactResolver;
   protected ArtifactMetadataSource artifactMetadataSource;
   private ProjectArtifactFactory artifactFactory;
   private ModelInheritanceAssembler modelInheritanceAssembler;
   private ProfileInjector profileInjector;
   private ModelValidator validator;
   private Map rawProjectCache = new HashMap();
   private Map processedProjectCache = new HashMap();
   private MavenXpp3Reader modelReader;
   private PathTranslator pathTranslator;
   private ModelDefaultsInjector modelDefaultsInjector;
   private ModelInterpolator modelInterpolator;
   private ArtifactRepositoryFactory artifactRepositoryFactory;
   private WagonManager wagonManager;
   public static final String MAVEN_MODEL_VERSION = "4.0.0";

   public void initialize() {
      this.modelReader = new MavenXpp3Reader();
   }

   public MavenProject build(File pom, ProjectBuilderConfiguration config) throws ProjectBuildingException {
      return this.buildFromSourceFileInternal(pom, config, true);
   }

   public MavenProject build(File pom, ProjectBuilderConfiguration config, boolean checkDistributionManagementStatus) throws ProjectBuildingException {
      return this.buildFromSourceFileInternal(pom, config, checkDistributionManagementStatus);
   }

   public MavenProject build(File projectDescriptor, ArtifactRepository localRepository, ProfileManager profileManager) throws ProjectBuildingException {
      ProjectBuilderConfiguration config = (new DefaultProjectBuilderConfiguration()).setLocalRepository(localRepository).setGlobalProfileManager(profileManager);
      return this.buildFromSourceFileInternal(projectDescriptor, config, true);
   }

   public MavenProject build(File projectDescriptor, ArtifactRepository localRepository, ProfileManager profileManager, boolean checkDistributionManagementStatus) throws ProjectBuildingException {
      ProjectBuilderConfiguration config = (new DefaultProjectBuilderConfiguration()).setLocalRepository(localRepository).setGlobalProfileManager(profileManager);
      return this.buildFromSourceFileInternal(projectDescriptor, config, checkDistributionManagementStatus);
   }

   public MavenProject buildFromRepository(Artifact artifact, List remoteArtifactRepositories, ArtifactRepository localRepository, boolean allowStubModel) throws ProjectBuildingException {
      String cacheKey = createCacheKey(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
      MavenProject project = (MavenProject)this.processedProjectCache.get(cacheKey);
      if (project != null) {
         return project;
      } else {
         Model model = this.findModelFromRepository(artifact, remoteArtifactRepositories, localRepository, allowStubModel);
         ProjectBuilderConfiguration config = (new DefaultProjectBuilderConfiguration()).setLocalRepository(localRepository);
         return this.buildInternal("Artifact [" + artifact + "]", model, config, remoteArtifactRepositories, (File)null, false);
      }
   }

   public MavenProject buildFromRepository(Artifact artifact, List remoteArtifactRepositories, ArtifactRepository localRepository) throws ProjectBuildingException {
      return this.buildFromRepository(artifact, remoteArtifactRepositories, localRepository, true);
   }

   public MavenProject buildStandaloneSuperProject(ArtifactRepository localRepository) throws ProjectBuildingException {
      ProfileManager profileManager = new DefaultProfileManager(this.container);
      return this.buildStandaloneSuperProject((new DefaultProjectBuilderConfiguration()).setLocalRepository(localRepository).setGlobalProfileManager(profileManager));
   }

   public MavenProject buildStandaloneSuperProject(ArtifactRepository localRepository, ProfileManager profileManager) throws ProjectBuildingException {
      return this.buildStandaloneSuperProject((new DefaultProjectBuilderConfiguration()).setLocalRepository(localRepository).setGlobalProfileManager(profileManager));
   }

   public MavenProject buildStandaloneSuperProject(ProjectBuilderConfiguration config) throws ProjectBuildingException {
      Model superModel = this.getSuperModel();
      superModel.setGroupId("org.apache.maven");
      superModel.setArtifactId("super-pom");
      superModel.setVersion("2.0");
      ProfileManager profileManager = config.getGlobalProfileManager();
      if (profileManager == null) {
         profileManager = new DefaultProfileManager(this.container);
      }

      ((ProfileManager)profileManager).addProfiles(superModel.getProfiles());
      String projectId = this.safeVersionlessKey("org.apache.maven", "super-pom");
      List activeProfiles = this.injectActiveProfiles((ProfileManager)profileManager, superModel);
      MavenProject project = new MavenProject(superModel);
      project.setManagedVersionMap(this.createManagedVersionMap(projectId, superModel.getDependencyManagement(), (MavenProject)null));
      project.setActiveProfiles(activeProfiles);
      project.setOriginalModel(superModel);

      try {
         project = this.processProjectLogic("<Super-POM>", project, config, (File)null, (List)null, true, true);
         project.setExecutionRoot(true);
         return project;
      } catch (ModelInterpolationException var8) {
         throw new ProjectBuildingException(projectId, var8.getMessage(), var8);
      } catch (InvalidRepositoryException var9) {
         throw new ProjectBuildingException(projectId, var9.getMessage(), var9);
      }
   }

   public MavenProject buildWithDependencies(File projectDescriptor, ArtifactRepository localRepository, ProfileManager profileManager) throws ProjectBuildingException, ArtifactResolutionException, ArtifactNotFoundException {
      return this.buildWithDependencies(projectDescriptor, localRepository, profileManager, (TransferListener)null);
   }

   public MavenProject buildWithDependencies(File projectDescriptor, ArtifactRepository localRepository, ProfileManager profileManager, TransferListener transferListener) throws ProjectBuildingException, ArtifactResolutionException, ArtifactNotFoundException {
      MavenProject project = this.build(projectDescriptor, localRepository, profileManager, false);
      Artifact projectArtifact = project.getArtifact();
      String projectId = this.safeVersionlessKey(project.getGroupId(), project.getArtifactId());
      Map managedVersions = project.getManagedVersionMap();
      this.ensureMetadataSourceIsInitialized();

      try {
         project.setDependencyArtifacts(project.createArtifacts(this.artifactFactory, (String)null, (ArtifactFilter)null));
      } catch (InvalidDependencyVersionException var10) {
         throw new ProjectBuildingException(projectId, "Unable to build project due to an invalid dependency version: " + var10.getMessage(), var10);
      }

      if (transferListener != null) {
         this.wagonManager.setDownloadMonitor(transferListener);
      }

      ArtifactResolutionResult result = this.artifactResolver.resolveTransitively(project.getDependencyArtifacts(), projectArtifact, managedVersions, localRepository, project.getRemoteArtifactRepositories(), this.artifactMetadataSource);
      project.setArtifacts(result.getArtifacts());
      return project;
   }

   private void ensureMetadataSourceIsInitialized() throws ProjectBuildingException {
      if (this.artifactMetadataSource == null) {
         try {
            this.artifactMetadataSource = (ArtifactMetadataSource)this.container.lookup(ArtifactMetadataSource.ROLE);
         } catch (ComponentLookupException var2) {
            throw new ProjectBuildingException("all", "Cannot lookup metadata source for building the project.", var2);
         }
      }

   }

   private Map createManagedVersionMap(String projectId, DependencyManagement dependencyManagement, MavenProject parent) throws ProjectBuildingException {
      Map map = null;
      List deps;
      if (dependencyManagement != null && (deps = dependencyManagement.getDependencies()) != null && deps.size() > 0) {
         map = new ManagedVersionMap((Map)map);
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Adding managed dependencies for " + projectId);
         }

         Iterator i = dependencyManagement.getDependencies().iterator();

         while(i.hasNext()) {
            Dependency d = (Dependency)i.next();

            try {
               VersionRange versionRange = VersionRange.createFromVersionSpec(d.getVersion());
               Artifact artifact = this.artifactFactory.createDependencyArtifact(d.getGroupId(), d.getArtifactId(), versionRange, d.getType(), d.getClassifier(), d.getScope(), d.isOptional());
               if (this.getLogger().isDebugEnabled()) {
                  this.getLogger().debug("  " + artifact);
               }

               if (null != d.getExclusions() && !d.getExclusions().isEmpty()) {
                  List exclusions = new ArrayList();
                  Iterator exclItr = d.getExclusions().iterator();

                  while(exclItr.hasNext()) {
                     Exclusion e = (Exclusion)exclItr.next();
                     exclusions.add(e.getGroupId() + ":" + e.getArtifactId());
                  }

                  ExcludesArtifactFilter eaf = new ExcludesArtifactFilter(exclusions);
                  artifact.setDependencyFilter(eaf);
               } else {
                  artifact.setDependencyFilter((ArtifactFilter)null);
               }

               ((Map)map).put(d.getManagementKey(), artifact);
            } catch (InvalidVersionSpecificationException var13) {
               throw new ProjectBuildingException(projectId, "Unable to parse version '" + d.getVersion() + "' for dependency '" + d.getManagementKey() + "': " + var13.getMessage(), var13);
            }
         }
      } else if (map == null) {
         map = Collections.EMPTY_MAP;
      }

      return (Map)map;
   }

   private MavenProject buildFromSourceFileInternal(File projectDescriptor, ProjectBuilderConfiguration config, boolean checkDistributionManagementStatus) throws ProjectBuildingException {
      Model model = this.readModel("unknown", projectDescriptor, true);
      MavenProject project = this.buildInternal(projectDescriptor.getAbsolutePath(), model, config, this.buildArtifactRepositories(this.getSuperModel()), projectDescriptor, true);
      if (checkDistributionManagementStatus && project.getDistributionManagement() != null && project.getDistributionManagement().getStatus() != null) {
         String projectId = this.safeVersionlessKey(project.getGroupId(), project.getArtifactId());
         throw new ProjectBuildingException(projectId, "Invalid project file: distribution status must not be specified for a project outside of the repository");
      } else {
         return project;
      }
   }

   private Model findModelFromRepository(Artifact artifact, List remoteArtifactRepositories, ArtifactRepository localRepository, boolean allowStubModel) throws ProjectBuildingException {
      String projectId = this.safeVersionlessKey(artifact.getGroupId(), artifact.getArtifactId());
      this.normalizeToArtifactRepositories(remoteArtifactRepositories, projectId);
      Artifact projectArtifact;
      if ("pom".equals(artifact.getType())) {
         projectArtifact = artifact;
      } else {
         this.getLogger().debug("Attempting to build MavenProject instance for Artifact (" + artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion() + ") of type: " + artifact.getType() + "; constructing POM artifact instead.");
         projectArtifact = this.artifactFactory.createProjectArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getScope());
      }

      Model model;
      try {
         this.artifactResolver.resolve(projectArtifact, remoteArtifactRepositories, localRepository);
         File file = projectArtifact.getFile();
         model = this.readModel(projectId, file, false);
         String downloadUrl = null;
         ArtifactStatus status = ArtifactStatus.NONE;
         DistributionManagement distributionManagement = model.getDistributionManagement();
         if (distributionManagement != null) {
            downloadUrl = distributionManagement.getDownloadUrl();
            status = ArtifactStatus.valueOf(distributionManagement.getStatus());
         }

         this.checkStatusAndUpdate(projectArtifact, status, file, remoteArtifactRepositories, localRepository);
         if (downloadUrl != null) {
            projectArtifact.setDownloadUrl(downloadUrl);
         } else {
            projectArtifact.setDownloadUrl(model.getUrl());
         }
      } catch (ArtifactResolutionException var12) {
         throw new ProjectBuildingException(projectId, "Error getting POM for '" + projectId + "' from the repository: " + var12.getMessage(), var12);
      } catch (ArtifactNotFoundException var13) {
         if (!allowStubModel) {
            throw new ProjectBuildingException(projectId, "POM '" + projectId + "' not found in repository: " + var13.getMessage(), var13);
         }

         this.getLogger().debug("Artifact not found - using stub model: " + var13.getMessage());
         model = this.createStubModel(projectArtifact);
      }

      return model;
   }

   private List normalizeToArtifactRepositories(List remoteArtifactRepositories, String projectId) throws ProjectBuildingException {
      List normalized = new ArrayList(remoteArtifactRepositories.size());
      boolean normalizationNeeded = false;
      Iterator it = remoteArtifactRepositories.iterator();

      while(it.hasNext()) {
         Object item = it.next();
         if (item instanceof ArtifactRepository) {
            normalized.add(item);
         } else {
            if (!(item instanceof Repository)) {
               throw new ProjectBuildingException(projectId, "Error building artifact repository from non-repository information item: " + item);
            }

            Repository repo = (Repository)item;

            try {
               Object item = ProjectUtils.buildArtifactRepository(repo, this.artifactRepositoryFactory, this.container);
               normalized.add(item);
               normalizationNeeded = true;
            } catch (InvalidRepositoryException var9) {
               throw new ProjectBuildingException(projectId, "Error building artifact repository for id: " + repo.getId(), var9);
            }
         }
      }

      if (normalizationNeeded) {
         return normalized;
      } else {
         return remoteArtifactRepositories;
      }
   }

   private void checkStatusAndUpdate(Artifact projectArtifact, ArtifactStatus status, File file, List remoteArtifactRepositories, ArtifactRepository localRepository) throws ArtifactNotFoundException {
      if (!projectArtifact.isSnapshot() && status.compareTo(ArtifactStatus.DEPLOYED) < 0) {
         ArtifactRepositoryPolicy policy = new ArtifactRepositoryPolicy();
         policy.setUpdatePolicy("never");
         if (policy.checkOutOfDate(new Date(file.lastModified()))) {
            this.getLogger().info(projectArtifact.getArtifactId() + ": updating metadata due to status of '" + status + "'");

            try {
               projectArtifact.setResolved(false);
               this.artifactResolver.resolveAlways(projectArtifact, remoteArtifactRepositories, localRepository);
            } catch (ArtifactResolutionException var8) {
               this.getLogger().warn("Error updating POM - using existing version");
               this.getLogger().debug("Cause", var8);
            } catch (ArtifactNotFoundException var9) {
               this.getLogger().warn("Error updating POM - not found. Removing local copy.");
               this.getLogger().debug("Cause", var9);
               file.delete();
               throw var9;
            }
         }
      }

   }

   private Model createStubModel(Artifact projectArtifact) {
      this.getLogger().debug("Using defaults for missing POM " + projectArtifact);
      Model model = new Model();
      model.setModelVersion("4.0.0");
      model.setArtifactId(projectArtifact.getArtifactId());
      model.setGroupId(projectArtifact.getGroupId());
      model.setVersion(projectArtifact.getVersion());
      model.setPackaging(projectArtifact.getType());
      model.setDistributionManagement(new DistributionManagement());
      model.getDistributionManagement().setStatus(ArtifactStatus.GENERATED.toString());
      return model;
   }

   private MavenProject buildInternal(String pomLocation, Model model, ProjectBuilderConfiguration config, List parentSearchRepositories, File projectDescriptor, boolean strict) throws ProjectBuildingException {
      File projectDir = null;
      if (projectDescriptor != null) {
         projectDir = projectDescriptor.getAbsoluteFile().getParentFile();
      }

      Model superModel = this.getSuperModel();
      ProfileManager externalProfileManager = config.getGlobalProfileManager();
      DefaultProfileManager superProjectProfileManager;
      if (externalProfileManager != null) {
         superProjectProfileManager = new DefaultProfileManager(this.container, externalProfileManager.getRequestProperties());
      } else {
         superProjectProfileManager = new DefaultProfileManager(this.container);
      }

      superProjectProfileManager.addProfiles(superModel.getProfiles());
      List activeProfiles = this.injectActiveProfiles(superProjectProfileManager, superModel);
      MavenProject superProject = new MavenProject(superModel);
      superProject.setActiveProfiles(activeProfiles);
      LinkedList lineage = new LinkedList();
      Set aggregatedRemoteWagonRepositories = new LinkedHashSet();
      String projectId = this.safeVersionlessKey(model.getGroupId(), model.getArtifactId());

      List activeExternalProfiles;
      try {
         if (externalProfileManager != null) {
            activeExternalProfiles = externalProfileManager.getActiveProfiles();
         } else {
            activeExternalProfiles = Collections.EMPTY_LIST;
         }
      } catch (ProfileActivationException var30) {
         throw new ProjectBuildingException(projectId, "Failed to calculate active external profiles.", var30);
      }

      Iterator i = activeExternalProfiles.iterator();

      while(i.hasNext()) {
         Profile externalProfile = (Profile)i.next();

         ArtifactRepository artifactRepo;
         for(Iterator repoIterator = externalProfile.getRepositories().iterator(); repoIterator.hasNext(); aggregatedRemoteWagonRepositories.add(artifactRepo)) {
            Repository mavenRepo = (Repository)repoIterator.next();
            artifactRepo = null;

            try {
               artifactRepo = ProjectUtils.buildArtifactRepository(mavenRepo, this.artifactRepositoryFactory, this.container);
            } catch (InvalidRepositoryException var29) {
               throw new ProjectBuildingException(projectId, var29.getMessage(), var29);
            }
         }
      }

      i = null;

      MavenProject project;
      try {
         project = this.assembleLineage(model, lineage, config, projectDescriptor, parentSearchRepositories, aggregatedRemoteWagonRepositories, strict);
      } catch (InvalidRepositoryException var28) {
         throw new ProjectBuildingException(projectId, var28.getMessage(), var28);
      }

      MavenProject previousProject = superProject;
      Model previous = superProject.getModel();

      MavenProject currentProject;
      for(Iterator i = lineage.iterator(); i.hasNext(); previousProject = currentProject) {
         currentProject = (MavenProject)i.next();
         Model current = currentProject.getModel();
         String pathAdjustment = null;

         try {
            pathAdjustment = previousProject.getModulePathAdjustment(currentProject);
         } catch (IOException var27) {
            this.getLogger().debug("Cannot determine whether " + currentProject.getId() + " is a module of " + previousProject.getId() + ". Reason: " + var27.getMessage(), var27);
         }

         this.modelInheritanceAssembler.assembleModelInheritance(current, previous, pathAdjustment);
         previous = current;
      }

      List repositories = new ArrayList(aggregatedRemoteWagonRepositories);
      List superRepositories = this.buildArtifactRepositories(superModel);
      Iterator i = superRepositories.iterator();

      while(i.hasNext()) {
         ArtifactRepository repository = (ArtifactRepository)i.next();
         if (!repositories.contains(repository)) {
            repositories.add(repository);
         }
      }

      ModelUtils.mergeDuplicatePluginDefinitions(project.getModel().getBuild());

      try {
         project = this.processProjectLogic(pomLocation, project, config, projectDir, repositories, strict, false);
      } catch (ModelInterpolationException var25) {
         throw new InvalidProjectModelException(projectId, pomLocation, var25.getMessage(), var25);
      } catch (InvalidRepositoryException var26) {
         throw new InvalidProjectModelException(projectId, pomLocation, var26.getMessage(), var26);
      }

      this.processedProjectCache.put(createCacheKey(project.getGroupId(), project.getArtifactId(), project.getVersion()), project);
      if (projectDescriptor != null) {
         Build build = project.getBuild();
         project.addCompileSourceRoot(build.getSourceDirectory());
         project.addScriptSourceRoot(build.getScriptSourceDirectory());
         project.addTestCompileSourceRoot(build.getTestSourceDirectory());
         project.setFile(projectDescriptor);
      }

      project.setManagedVersionMap(this.createManagedVersionMap(projectId, project.getDependencyManagement(), project.getParent()));
      return project;
   }

   private String safeVersionlessKey(String groupId, String artifactId) {
      String gid = groupId;
      if (StringUtils.isEmpty(groupId)) {
         gid = "unknown";
      }

      String aid = artifactId;
      if (StringUtils.isEmpty(artifactId)) {
         aid = "unknown";
      }

      return ArtifactUtils.versionlessKey(gid, aid);
   }

   private List buildArtifactRepositories(Model model) throws ProjectBuildingException {
      try {
         return ProjectUtils.buildArtifactRepositories(model.getRepositories(), this.artifactRepositoryFactory, this.container);
      } catch (InvalidRepositoryException var4) {
         String projectId = this.safeVersionlessKey(model.getGroupId(), model.getArtifactId());
         throw new ProjectBuildingException(projectId, var4.getMessage(), var4);
      }
   }

   private MavenProject processProjectLogic(String pomLocation, MavenProject project, ProjectBuilderConfiguration config, File projectDir, List remoteRepositories, boolean strict, boolean isSuperPom) throws ProjectBuildingException, ModelInterpolationException, InvalidRepositoryException {
      Model model = project.getModel();
      List activeProfiles = project.getActiveProfiles();
      if (activeProfiles == null) {
         activeProfiles = new ArrayList();
      }

      ProfileManager profileMgr = config == null ? null : config.getGlobalProfileManager();
      List injectedProfiles = this.injectActiveProfiles(profileMgr, model);
      ((List)activeProfiles).addAll(injectedProfiles);
      Build dynamicBuild = model.getBuild();
      model.setBuild(ModelUtils.cloneBuild(dynamicBuild));
      model = this.modelInterpolator.interpolate(model, projectDir, config, this.getLogger().isDebugEnabled());
      this.mergeDeterministicBuildElements(model.getBuild(), dynamicBuild);
      model.setBuild(dynamicBuild);
      if (!isSuperPom) {
         this.mergeManagedDependencies(model, config.getLocalRepository(), remoteRepositories);
      }

      this.modelDefaultsInjector.injectDefaults(model);
      MavenProject parentProject = project.getParent();
      Model originalModel = project.getOriginalModel();
      project = new MavenProject(model, this.getLogger());
      project.setOriginalModel(originalModel);
      project.setActiveProfiles((List)activeProfiles);
      Artifact projectArtifact = this.artifactFactory.create(project);
      project.setArtifact(projectArtifact);
      project.setProjectBuilderConfiguration(config);
      project.setPluginArtifactRepositories(ProjectUtils.buildArtifactRepositories(model.getPluginRepositories(), this.artifactRepositoryFactory, this.container));
      DistributionManagement dm = model.getDistributionManagement();
      if (dm != null) {
         ArtifactRepository repo = ProjectUtils.buildDeploymentArtifactRepository(dm.getRepository(), this.artifactRepositoryFactory, this.container);
         project.setReleaseArtifactRepository(repo);
         if (dm.getSnapshotRepository() != null) {
            repo = ProjectUtils.buildDeploymentArtifactRepository(dm.getSnapshotRepository(), this.artifactRepositoryFactory, this.container);
            project.setSnapshotArtifactRepository(repo);
         }
      }

      if (parentProject != null) {
         String cacheKey = createCacheKey(parentProject.getGroupId(), parentProject.getArtifactId(), parentProject.getVersion());
         MavenProject processedParent = (MavenProject)this.processedProjectCache.get(cacheKey);
         Artifact parentArtifact;
         if (processedParent != null) {
            project.setParent(processedParent);
            parentArtifact = processedParent.getArtifact();
         } else {
            project.setParent(parentProject);
            parentArtifact = this.artifactFactory.createParentArtifact(parentProject.getGroupId(), parentProject.getArtifactId(), parentProject.getVersion());
         }

         project.setParentArtifact(parentArtifact);
      }

      ModelValidationResult validationResult = this.validator.validate(model);
      String projectId = this.safeVersionlessKey(model.getGroupId(), model.getArtifactId());
      if (validationResult.getMessageCount() > 0) {
         throw new InvalidProjectModelException(projectId, pomLocation, "Failed to validate POM", validationResult);
      } else {
         project.setRemoteArtifactRepositories(ProjectUtils.buildArtifactRepositories(model.getRepositories(), this.artifactRepositoryFactory, this.container));
         project.setPluginArtifacts(this.createPluginArtifacts(projectId, project.getBuildPlugins()));
         project.setReportArtifacts(this.createReportArtifacts(projectId, project.getReportPlugins()));
         project.setExtensionArtifacts(this.createExtensionArtifacts(projectId, project.getBuildExtensions()));
         return project;
      }
   }

   private void mergeDeterministicBuildElements(Build interpolatedBuild, Build dynamicBuild) {
      this.mergeDeterministicPluginElements(interpolatedBuild.getPlugins(), dynamicBuild.getPlugins());
      PluginManagement dPluginMgmt = dynamicBuild.getPluginManagement();
      PluginManagement iPluginMgmt = interpolatedBuild.getPluginManagement();
      if (dPluginMgmt != null) {
         this.mergeDeterministicPluginElements(iPluginMgmt.getPlugins(), dPluginMgmt.getPlugins());
      }

      if (dynamicBuild.getExtensions() != null) {
         dynamicBuild.setExtensions(interpolatedBuild.getExtensions());
      }

   }

   private void mergeDeterministicPluginElements(List iPlugins, List dPlugins) {
      if (dPlugins != null) {
         for(int i = 0; i < dPlugins.size(); ++i) {
            Plugin dPlugin = (Plugin)dPlugins.get(i);
            Plugin iPlugin = (Plugin)iPlugins.get(i);
            dPlugin.setGroupId(iPlugin.getGroupId());
            dPlugin.setArtifactId(iPlugin.getArtifactId());
            dPlugin.setVersion(iPlugin.getVersion());
            dPlugin.setDependencies(iPlugin.getDependencies());
            List dExecutions = dPlugin.getExecutions();
            if (dExecutions != null) {
               List iExecutions = iPlugin.getExecutions();

               for(int j = 0; j < dExecutions.size(); ++j) {
                  PluginExecution dExec = (PluginExecution)dExecutions.get(j);
                  PluginExecution iExec = (PluginExecution)iExecutions.get(j);
                  dExec.setId(iExec.getId());
               }
            }
         }
      }

   }

   private MavenProject assembleLineage(Model model, LinkedList lineage, ProjectBuilderConfiguration config, File projectDescriptor, List parentSearchRepositories, Set aggregatedRemoteWagonRepositories, boolean strict) throws ProjectBuildingException, InvalidRepositoryException {
      Model originalModel = ModelUtils.cloneModel(model);
      File projectDir = null;
      if (projectDescriptor != null) {
         projectDir = projectDescriptor.getAbsoluteFile().getParentFile();
      }

      ProfileManager externalProfileManager = config.getGlobalProfileManager();
      DefaultProfileManager profileManager;
      if (externalProfileManager != null) {
         profileManager = new DefaultProfileManager(this.container, externalProfileManager.getRequestProperties());
      } else {
         profileManager = new DefaultProfileManager(this.container);
      }

      if (externalProfileManager != null) {
         profileManager.explicitlyActivate(externalProfileManager.getExplicitlyActivatedIds());
         profileManager.explicitlyDeactivate(externalProfileManager.getExplicitlyDeactivatedIds());
      }

      List activeProfiles;
      try {
         profileManager.addProfiles(model.getProfiles());
         this.loadProjectExternalProfiles(profileManager, projectDir);
         activeProfiles = this.injectActiveProfiles(profileManager, model);
      } catch (ProfileActivationException var25) {
         String projectId = this.safeVersionlessKey(model.getGroupId(), model.getArtifactId());
         throw new ProjectBuildingException(projectId, "Failed to activate local (project-level) build profiles: " + var25.getMessage(), var25);
      }

      if (!model.getRepositories().isEmpty()) {
         List respositories = this.buildArtifactRepositories(model);
         Iterator it = respositories.iterator();

         while(it.hasNext()) {
            ArtifactRepository repository = (ArtifactRepository)it.next();
            if (!aggregatedRemoteWagonRepositories.contains(repository)) {
               aggregatedRemoteWagonRepositories.add(repository);
            }
         }
      }

      MavenProject project = new MavenProject(model, this.getLogger());
      project.setFile(projectDescriptor);
      project.setActiveProfiles(activeProfiles);
      project.setOriginalModel(originalModel);
      lineage.addFirst(project);
      Parent parentModel = model.getParent();
      String projectId = this.safeVersionlessKey(model.getGroupId(), model.getArtifactId());
      if (parentModel != null) {
         if (StringUtils.isEmpty(parentModel.getGroupId())) {
            throw new ProjectBuildingException(projectId, "Missing groupId element from parent element");
         }

         if (StringUtils.isEmpty(parentModel.getArtifactId())) {
            throw new ProjectBuildingException(projectId, "Missing artifactId element from parent element");
         }

         if (parentModel.getGroupId().equals(model.getGroupId()) && parentModel.getArtifactId().equals(model.getArtifactId())) {
            throw new ProjectBuildingException(projectId, "Parent element is a duplicate of the current project ");
         }

         if (StringUtils.isEmpty(parentModel.getVersion())) {
            throw new ProjectBuildingException(projectId, "Missing version element from parent element");
         }

         File parentDescriptor = null;
         model = null;
         String parentKey = createCacheKey(parentModel.getGroupId(), parentModel.getArtifactId(), parentModel.getVersion());
         MavenProject parentProject = (MavenProject)this.rawProjectCache.get(parentKey);
         if (parentProject != null) {
            model = ModelUtils.cloneModel(parentProject.getOriginalModel());
            parentDescriptor = parentProject.getFile();
         }

         String parentRelativePath = parentModel.getRelativePath();
         if (model == null && projectDir != null && StringUtils.isNotEmpty(parentRelativePath)) {
            parentDescriptor = new File(projectDir, parentRelativePath);
            if (this.getLogger().isDebugEnabled()) {
               this.getLogger().debug("Searching for parent-POM: " + parentModel.getId() + " of project: " + project.getId() + " in relative path: " + parentRelativePath);
            }

            if (parentDescriptor.isDirectory()) {
               if (this.getLogger().isDebugEnabled()) {
                  this.getLogger().debug("Path specified in <relativePath/> (" + parentRelativePath + ") is a directory. Searching for 'pom.xml' within this directory.");
               }

               parentDescriptor = new File(parentDescriptor, "pom.xml");
               if (!parentDescriptor.exists() && this.getLogger().isDebugEnabled()) {
                  this.getLogger().debug("Parent-POM: " + parentModel.getId() + " for project: " + project.getId() + " cannot be loaded from relative path: " + parentDescriptor + "; path does not exist.");
               }
            }

            if (parentDescriptor != null) {
               try {
                  parentDescriptor = parentDescriptor.getCanonicalFile();
               } catch (IOException var24) {
                  this.getLogger().debug("Failed to canonicalize potential parent POM: '" + parentDescriptor + "'", var24);
                  parentDescriptor = null;
               }
            }

            if (parentDescriptor != null && parentDescriptor.exists()) {
               Model candidateParent = this.readModel(projectId, parentDescriptor, strict);
               String candidateParentGroupId = candidateParent.getGroupId();
               if (candidateParentGroupId == null && candidateParent.getParent() != null) {
                  candidateParentGroupId = candidateParent.getParent().getGroupId();
               }

               String candidateParentVersion = candidateParent.getVersion();
               if (candidateParentVersion == null && candidateParent.getParent() != null) {
                  candidateParentVersion = candidateParent.getParent().getVersion();
               }

               if (parentModel.getGroupId().equals(candidateParentGroupId) && parentModel.getArtifactId().equals(candidateParent.getArtifactId()) && parentModel.getVersion().equals(candidateParentVersion)) {
                  model = candidateParent;
                  this.getLogger().debug("Using parent-POM from the project hierarchy at: '" + parentModel.getRelativePath() + "' for project: " + project.getId());
               } else {
                  this.getLogger().debug("Invalid parent-POM referenced by relative path '" + parentModel.getRelativePath() + "' in parent specification in " + project.getId() + ":" + "\n  Specified: " + parentModel.getId() + "\n  Found:     " + candidateParent.getId());
               }
            } else if (this.getLogger().isDebugEnabled()) {
               this.getLogger().debug("Parent-POM: " + parentModel.getId() + " not found in relative path: " + parentRelativePath);
            }
         }

         Artifact parentArtifact = null;
         if (model == null) {
            parentDescriptor = null;
            List remoteRepositories = new ArrayList(aggregatedRemoteWagonRepositories);
            remoteRepositories.addAll(parentSearchRepositories);
            if (this.getLogger().isDebugEnabled()) {
               this.getLogger().debug("Retrieving parent-POM: " + parentModel.getId() + " for project: " + project.getId() + " from the repository.");
            }

            parentArtifact = this.artifactFactory.createParentArtifact(parentModel.getGroupId(), parentModel.getArtifactId(), parentModel.getVersion());

            try {
               model = this.findModelFromRepository(parentArtifact, remoteRepositories, config.getLocalRepository(), false);
            } catch (ProjectBuildingException var23) {
               throw new ProjectBuildingException(project.getId(), "Cannot find parent: " + var23.getProjectId() + " for project: " + project.getId(), var23);
            }
         }

         if (model != null && !"pom".equals(model.getPackaging())) {
            throw new ProjectBuildingException(projectId, "Parent: " + model.getId() + " of project: " + projectId + " has wrong packaging: " + model.getPackaging() + ". Must be 'pom'.");
         }

         MavenProject parent = this.assembleLineage(model, lineage, config, parentDescriptor, parentSearchRepositories, aggregatedRemoteWagonRepositories, strict);
         project.setParent(parent);
         project.setParentArtifact(parentArtifact);
      }

      this.rawProjectCache.put(createCacheKey(project.getGroupId(), project.getArtifactId(), project.getVersion()), new MavenProject(project));
      return project;
   }

   private void mergeManagedDependencies(Model model, ArtifactRepository localRepository, List parentSearchRepositories) throws ProjectBuildingException {
      DependencyManagement modelDepMgmt = model.getDependencyManagement();
      if (modelDepMgmt != null) {
         Map depsMap = new TreeMap();
         Iterator iter = modelDepMgmt.getDependencies().iterator();
         boolean doInclude = false;

         while(iter.hasNext()) {
            Dependency dep = (Dependency)iter.next();
            depsMap.put(dep.getManagementKey(), dep);
            if (dep.getType().equals("pom") && "import".equals(dep.getScope())) {
               doInclude = true;
            }
         }

         Map newDeps = new TreeMap(depsMap);
         iter = modelDepMgmt.getDependencies().iterator();
         if (doInclude) {
            while(true) {
               Dependency dep;
               DependencyManagement depMgmt;
               do {
                  do {
                     do {
                        if (!iter.hasNext()) {
                           List deps = new ArrayList(newDeps.values());
                           modelDepMgmt.setDependencies(deps);
                           return;
                        }

                        dep = (Dependency)iter.next();
                     } while(!dep.getType().equals("pom"));
                  } while(!"import".equals(dep.getScope()));

                  Artifact artifact = this.artifactFactory.createProjectArtifact(dep.getGroupId(), dep.getArtifactId(), dep.getVersion(), dep.getScope());
                  MavenProject project = this.buildFromRepository(artifact, parentSearchRepositories, localRepository, false);
                  depMgmt = project.getDependencyManagement();
               } while(depMgmt == null);

               if (this.getLogger().isDebugEnabled()) {
                  this.getLogger().debug("Importing managed dependencies for " + dep.toString());
               }

               Iterator it = depMgmt.getDependencies().iterator();

               while(it.hasNext()) {
                  Dependency includedDep = (Dependency)it.next();
                  String key = includedDep.getManagementKey();
                  if (!newDeps.containsKey(key)) {
                     newDeps.put(includedDep.getManagementKey(), includedDep);
                  }
               }

               newDeps.remove(dep.getManagementKey());
            }
         }
      }

   }

   private List injectActiveProfiles(ProfileManager profileManager, Model model) throws ProjectBuildingException {
      List activeProfiles;
      if (profileManager != null) {
         try {
            activeProfiles = profileManager.getActiveProfiles();
         } catch (ProfileActivationException var6) {
            String projectId = this.safeVersionlessKey(model.getGroupId(), model.getArtifactId());
            throw new ProjectBuildingException(projectId, var6.getMessage(), var6);
         }

         Iterator it = activeProfiles.iterator();

         while(it.hasNext()) {
            Profile profile = (Profile)it.next();
            this.profileInjector.inject(profile, model);
         }
      } else {
         activeProfiles = Collections.EMPTY_LIST;
      }

      return activeProfiles;
   }

   private void loadProjectExternalProfiles(ProfileManager profileManager, File projectDir) throws ProfileActivationException {
      if (projectDir != null) {
         try {
            ProfilesRoot root = this.profilesBuilder.buildProfiles(projectDir);
            if (root != null) {
               List active = root.getActiveProfiles();
               if (active != null && !active.isEmpty()) {
                  profileManager.explicitlyActivate(root.getActiveProfiles());
               }

               Iterator it = root.getProfiles().iterator();

               while(it.hasNext()) {
                  org.apache.maven.profiles.Profile rawProfile = (org.apache.maven.profiles.Profile)it.next();
                  Profile converted = ProfilesConversionUtils.convertFromProfileXmlProfile(rawProfile);
                  profileManager.addProfile(converted);
               }
            }
         } catch (IOException var8) {
            throw new ProfileActivationException("Cannot read profiles.xml resource from directory: " + projectDir, var8);
         } catch (XmlPullParserException var9) {
            throw new ProfileActivationException("Cannot parse profiles.xml resource from directory: " + projectDir, var9);
         }
      }

   }

   private Model readModel(String projectId, File file, boolean strict) throws ProjectBuildingException {
      XmlStreamReader reader = null;

      Model var5;
      try {
         reader = ReaderFactory.newXmlReader(file);
         var5 = this.readModel(projectId, file.getAbsolutePath(), reader, strict);
      } catch (FileNotFoundException var10) {
         throw new ProjectBuildingException(projectId, "Could not find the model file '" + file.getAbsolutePath() + "'.", var10);
      } catch (IOException var11) {
         throw new ProjectBuildingException(projectId, "Failed to build model from file '" + file.getAbsolutePath() + "'.\nError: '" + var11.getLocalizedMessage() + "'", var11);
      } finally {
         IOUtil.close((Reader)reader);
      }

      return var5;
   }

   private Model readModel(String projectId, String pomLocation, Reader reader, boolean strict) throws IOException, InvalidProjectModelException {
      String modelSource = IOUtil.toString(reader);
      if (modelSource.indexOf("<modelVersion>4.0.0") < 0) {
         throw new InvalidProjectModelException(projectId, pomLocation, "Not a v4.0.0 POM.");
      } else {
         StringReader sReader = new StringReader(modelSource);

         try {
            return this.modelReader.read((Reader)sReader, strict);
         } catch (XmlPullParserException var8) {
            throw new InvalidProjectModelException(projectId, pomLocation, "Parse error reading POM. Reason: " + var8.getMessage(), var8);
         }
      }
   }

   private Model readModel(String projectId, URL url, boolean strict) throws ProjectBuildingException {
      XmlStreamReader reader = null;

      Model var5;
      try {
         reader = ReaderFactory.newXmlReader(url.openStream());
         var5 = this.readModel(projectId, url.toExternalForm(), reader, strict);
      } catch (IOException var9) {
         throw new ProjectBuildingException(projectId, "Failed build model from URL '" + url.toExternalForm() + "'\nError: '" + var9.getLocalizedMessage() + "'", var9);
      } finally {
         IOUtil.close((Reader)reader);
      }

      return var5;
   }

   private static String createCacheKey(String groupId, String artifactId, String version) {
      return groupId + ":" + artifactId + ":" + version;
   }

   protected Set createPluginArtifacts(String projectId, List plugins) throws ProjectBuildingException {
      Set pluginArtifacts = new LinkedHashSet();
      Iterator i = plugins.iterator();

      while(i.hasNext()) {
         Plugin p = (Plugin)i.next();
         String version;
         if (StringUtils.isEmpty(p.getVersion())) {
            version = "RELEASE";
         } else {
            version = p.getVersion();
         }

         Artifact artifact;
         try {
            artifact = this.artifactFactory.createPluginArtifact(p.getGroupId(), p.getArtifactId(), VersionRange.createFromVersionSpec(version));
         } catch (InvalidVersionSpecificationException var9) {
            throw new ProjectBuildingException(projectId, "Unable to parse version '" + version + "' for plugin '" + ArtifactUtils.versionlessKey(p.getGroupId(), p.getArtifactId()) + "': " + var9.getMessage(), var9);
         }

         if (artifact != null) {
            pluginArtifacts.add(artifact);
         }
      }

      return pluginArtifacts;
   }

   protected Set createReportArtifacts(String projectId, List reports) throws ProjectBuildingException {
      Set pluginArtifacts = new LinkedHashSet();
      if (reports != null) {
         Iterator i = reports.iterator();

         while(i.hasNext()) {
            ReportPlugin p = (ReportPlugin)i.next();
            String version;
            if (StringUtils.isEmpty(p.getVersion())) {
               version = "RELEASE";
            } else {
               version = p.getVersion();
            }

            Artifact artifact;
            try {
               artifact = this.artifactFactory.createPluginArtifact(p.getGroupId(), p.getArtifactId(), VersionRange.createFromVersionSpec(version));
            } catch (InvalidVersionSpecificationException var9) {
               throw new ProjectBuildingException(projectId, "Unable to parse version '" + version + "' for report '" + ArtifactUtils.versionlessKey(p.getGroupId(), p.getArtifactId()) + "': " + var9.getMessage(), var9);
            }

            if (artifact != null) {
               pluginArtifacts.add(artifact);
            }
         }
      }

      return pluginArtifacts;
   }

   protected Set createExtensionArtifacts(String projectId, List extensions) throws ProjectBuildingException {
      Set extensionArtifacts = new LinkedHashSet();
      if (extensions != null) {
         Iterator i = extensions.iterator();

         while(i.hasNext()) {
            Extension ext = (Extension)i.next();
            String version;
            if (StringUtils.isEmpty(ext.getVersion())) {
               version = "RELEASE";
            } else {
               version = ext.getVersion();
            }

            Artifact artifact;
            try {
               VersionRange versionRange = VersionRange.createFromVersionSpec(version);
               artifact = this.artifactFactory.createExtensionArtifact(ext.getGroupId(), ext.getArtifactId(), versionRange);
            } catch (InvalidVersionSpecificationException var9) {
               throw new ProjectBuildingException(projectId, "Unable to parse version '" + version + "' for extension '" + ArtifactUtils.versionlessKey(ext.getGroupId(), ext.getArtifactId()) + "': " + var9.getMessage(), var9);
            }

            if (artifact != null) {
               extensionArtifacts.add(artifact);
            }
         }
      }

      return extensionArtifacts;
   }

   private Model getSuperModel() throws ProjectBuildingException {
      URL url = DefaultMavenProjectBuilder.class.getResource("pom-4.0.0.xml");
      String projectId = this.safeVersionlessKey("org.apache.maven", "super-pom");
      return this.readModel(projectId, url, true);
   }

   public void contextualize(Context context) throws ContextException {
      this.container = (PlexusContainer)context.get("plexus");
   }

   public void calculateConcreteState(MavenProject project, ProjectBuilderConfiguration config) throws ModelInterpolationException {
      this.calculateConcreteStateInternal(project, config, true, new HashSet());
   }

   public void calculateConcreteState(MavenProject project, ProjectBuilderConfiguration config, boolean processProjectReferences) throws ModelInterpolationException {
      this.calculateConcreteStateInternal(project, config, processProjectReferences, processProjectReferences ? new HashSet() : null);
   }

   private void calculateConcreteStateInternal(MavenProject project, ProjectBuilderConfiguration config, boolean processProjectReferences, Set processedProjects) throws ModelInterpolationException {
      if (processProjectReferences) {
         processedProjects.add(project.getId());
      }

      this.restoreDynamicStateInternal(project, config, processProjectReferences, processProjectReferences ? new HashSet(processedProjects) : null);
      if (!project.isConcrete()) {
         if (project.getParent() != null) {
            this.calculateConcreteStateInternal(project.getParent(), config, processProjectReferences, processedProjects);
         }

         Build build = project.getBuild();
         if (build != null) {
            this.initResourceMergeIds(build.getResources());
            this.initResourceMergeIds(build.getTestResources());
         }

         File basedir = project.getBasedir();
         Model model = ModelUtils.cloneModel(project.getModel());
         model = this.modelInterpolator.interpolate(model, project.getBasedir(), config, this.getLogger().isDebugEnabled());
         List originalInterpolatedCompileSourceRoots = this.interpolateListOfStrings(project.getCompileSourceRoots(), model, project.getBasedir(), config, this.getLogger().isDebugEnabled());
         project.preserveCompileSourceRoots(originalInterpolatedCompileSourceRoots);
         project.setCompileSourceRoots(originalInterpolatedCompileSourceRoots == null ? null : this.translateListOfPaths(originalInterpolatedCompileSourceRoots, basedir));
         List originalInterpolatedTestCompileSourceRoots = this.interpolateListOfStrings(project.getTestCompileSourceRoots(), model, project.getBasedir(), config, this.getLogger().isDebugEnabled());
         project.preserveTestCompileSourceRoots(originalInterpolatedTestCompileSourceRoots);
         project.setTestCompileSourceRoots(originalInterpolatedTestCompileSourceRoots == null ? null : this.translateListOfPaths(originalInterpolatedTestCompileSourceRoots, basedir));
         List originalInterpolatedScriptSourceRoots = this.interpolateListOfStrings(project.getScriptSourceRoots(), model, project.getBasedir(), config, this.getLogger().isDebugEnabled());
         project.preserveScriptSourceRoots(originalInterpolatedScriptSourceRoots);
         project.setScriptSourceRoots(originalInterpolatedScriptSourceRoots);
         if (basedir != null) {
            this.pathTranslator.alignToBaseDirectory(model, basedir);
         }

         project.preserveBuild(ModelUtils.cloneBuild(model.getBuild()));
         project.preserveProperties();
         project.preserveBasedir();
         project.setBuild(model.getBuild());
         if (project.getExecutionProject() != null) {
            this.calculateConcreteStateInternal(project.getExecutionProject(), config, processProjectReferences, processedProjects);
         }

         project.setConcrete(true);
      }

      if (processProjectReferences) {
         this.calculateConcreteProjectReferences(project, config, processedProjects);
      }

   }

   private void initResourceMergeIds(List resources) {
      if (resources != null) {
         Iterator it = resources.iterator();

         while(it.hasNext()) {
            Resource resource = (Resource)it.next();
            resource.initMergeId();
         }
      }

   }

   private void calculateConcreteProjectReferences(MavenProject project, ProjectBuilderConfiguration config, Set processedProjects) throws ModelInterpolationException {
      Map projectRefs = project.getProjectReferences();
      if (projectRefs != null) {
         Iterator it = projectRefs.values().iterator();

         while(it.hasNext()) {
            MavenProject reference = (MavenProject)it.next();
            if (!processedProjects.contains(reference.getId())) {
               this.calculateConcreteStateInternal(reference, config, true, processedProjects);
            }
         }
      }

   }

   private List translateListOfPaths(List paths, File basedir) {
      if (paths == null) {
         return null;
      } else if (basedir == null) {
         return paths;
      } else {
         List result = new ArrayList(paths.size());
         Iterator it = paths.iterator();

         while(it.hasNext()) {
            String path = (String)it.next();
            String aligned = this.pathTranslator.alignToBaseDirectory(path, basedir);
            result.add(aligned);
         }

         return result;
      }
   }

   public void restoreDynamicState(MavenProject project, ProjectBuilderConfiguration config) throws ModelInterpolationException {
      this.restoreDynamicStateInternal(project, config, true, new HashSet());
   }

   public void restoreDynamicState(MavenProject project, ProjectBuilderConfiguration config, boolean processProjectReferences) throws ModelInterpolationException {
      this.restoreDynamicStateInternal(project, config, processProjectReferences, processProjectReferences ? new HashSet() : null);
   }

   private void restoreDynamicStateInternal(MavenProject project, ProjectBuilderConfiguration config, boolean processProjectReferences, Set processedProjects) throws ModelInterpolationException {
      if (processProjectReferences) {
         processedProjects.add(project.getId());
      }

      if (project.isConcrete() && this.projectWasChanged(project)) {
         if (project.getParent() != null) {
            this.restoreDynamicStateInternal(project.getParent(), config, processProjectReferences, processedProjects);
         }

         this.restoreBuildRoots(project, config, this.getLogger().isDebugEnabled());
         this.restoreModelBuildSection(project, config, this.getLogger().isDebugEnabled());
         if (project.getExecutionProject() != null) {
            this.restoreDynamicStateInternal(project.getExecutionProject(), config, processProjectReferences, processedProjects);
         }

         project.setConcrete(false);
      }

      if (processProjectReferences) {
         this.restoreDynamicProjectReferences(project, config, processedProjects);
      }

   }

   private boolean projectWasChanged(MavenProject project) {
      if (!this.objectEquals(project.getBasedir(), project.getPreservedBasedir())) {
         return true;
      } else if (!this.objectEquals(project.getProperties(), project.getPreservedProperties())) {
         return true;
      } else {
         Build oBuild = project.getOriginalInterpolatedBuild();
         Build build = project.getBuild();
         if (!this.objectEquals(oBuild.getDirectory(), build.getDirectory())) {
            return true;
         } else if (!this.objectEquals(oBuild.getOutputDirectory(), build.getOutputDirectory())) {
            return true;
         } else if (!this.objectEquals(oBuild.getSourceDirectory(), build.getSourceDirectory())) {
            return true;
         } else if (!this.objectEquals(oBuild.getTestSourceDirectory(), build.getTestSourceDirectory())) {
            return true;
         } else {
            return !this.objectEquals(oBuild.getScriptSourceDirectory(), build.getScriptSourceDirectory());
         }
      }
   }

   private boolean objectEquals(Object obj1, Object obj2) {
      return obj1 == null ? obj2 == null : obj2 != null && (obj1 == obj2 || obj1.equals(obj2));
   }

   private void propagateNewPlugins(MavenProject project) {
      Build changedBuild = project.getBuild();
      Build dynamicBuild = project.getDynamicBuild();
      if (changedBuild != null && dynamicBuild != null) {
         List changedPlugins = changedBuild.getPlugins();
         List dynamicPlugins = dynamicBuild.getPlugins();
         if (changedPlugins != null && dynamicPlugins != null && changedPlugins.size() != dynamicPlugins.size()) {
            changedPlugins.removeAll(dynamicPlugins);
            if (!changedPlugins.isEmpty()) {
               Iterator it = changedPlugins.iterator();

               while(it.hasNext()) {
                  Plugin plugin = (Plugin)it.next();
                  dynamicBuild.addPlugin(plugin);
               }
            }
         }

         dynamicBuild.flushPluginMap();
      }
   }

   private void restoreDynamicProjectReferences(MavenProject project, ProjectBuilderConfiguration config, Set processedProjects) throws ModelInterpolationException {
      Map projectRefs = project.getProjectReferences();
      if (projectRefs != null) {
         Iterator it = projectRefs.values().iterator();

         while(it.hasNext()) {
            MavenProject projectRef = (MavenProject)it.next();
            if (!processedProjects.contains(projectRef.getId())) {
               this.restoreDynamicStateInternal(projectRef, config, true, processedProjects);
            }
         }
      }

   }

   private void restoreBuildRoots(MavenProject project, ProjectBuilderConfiguration config, boolean debugMessages) throws ModelInterpolationException {
      project.setCompileSourceRoots(this.restoreListOfStrings(project.getDynamicCompileSourceRoots(), project.getOriginalInterpolatedCompileSourceRoots(), project.getCompileSourceRoots(), project, config, debugMessages));
      project.setTestCompileSourceRoots(this.restoreListOfStrings(project.getDynamicTestCompileSourceRoots(), project.getOriginalInterpolatedTestCompileSourceRoots(), project.getTestCompileSourceRoots(), project, config, debugMessages));
      project.setScriptSourceRoots(this.restoreListOfStrings(project.getDynamicScriptSourceRoots(), project.getOriginalInterpolatedScriptSourceRoots(), project.getScriptSourceRoots(), project, config, debugMessages));
      project.clearRestorableRoots();
   }

   private void restoreModelBuildSection(MavenProject project, ProjectBuilderConfiguration config, boolean debugMessages) throws ModelInterpolationException {
      Build changedBuild = project.getBuild();
      Build dynamicBuild = project.getDynamicBuild();
      Build originalInterpolatedBuild = project.getOriginalInterpolatedBuild();
      dynamicBuild.setResources(this.restoreResources(dynamicBuild.getResources(), originalInterpolatedBuild.getResources(), changedBuild.getResources(), project, config, debugMessages));
      dynamicBuild.setTestResources(this.restoreResources(dynamicBuild.getTestResources(), originalInterpolatedBuild.getTestResources(), changedBuild.getTestResources(), project, config, debugMessages));
      dynamicBuild.setFilters(this.restoreListOfStrings(dynamicBuild.getFilters(), originalInterpolatedBuild.getFilters(), changedBuild.getFilters(), project, config, debugMessages));
      dynamicBuild.setFinalName(this.restoreString(dynamicBuild.getFinalName(), originalInterpolatedBuild.getFinalName(), changedBuild.getFinalName(), project, config, debugMessages));
      dynamicBuild.setDefaultGoal(this.restoreString(dynamicBuild.getDefaultGoal(), originalInterpolatedBuild.getDefaultGoal(), changedBuild.getDefaultGoal(), project, config, debugMessages));
      dynamicBuild.setSourceDirectory(this.restoreString(dynamicBuild.getSourceDirectory(), originalInterpolatedBuild.getSourceDirectory(), changedBuild.getSourceDirectory(), project, config, debugMessages));
      dynamicBuild.setTestSourceDirectory(this.restoreString(dynamicBuild.getTestSourceDirectory(), originalInterpolatedBuild.getTestSourceDirectory(), changedBuild.getTestSourceDirectory(), project, config, debugMessages));
      dynamicBuild.setScriptSourceDirectory(this.restoreString(dynamicBuild.getScriptSourceDirectory(), originalInterpolatedBuild.getScriptSourceDirectory(), changedBuild.getScriptSourceDirectory(), project, config, debugMessages));
      dynamicBuild.setOutputDirectory(this.restoreString(dynamicBuild.getOutputDirectory(), originalInterpolatedBuild.getOutputDirectory(), changedBuild.getOutputDirectory(), project, config, debugMessages));
      dynamicBuild.setTestOutputDirectory(this.restoreString(dynamicBuild.getTestOutputDirectory(), originalInterpolatedBuild.getTestOutputDirectory(), changedBuild.getTestOutputDirectory(), project, config, debugMessages));
      dynamicBuild.setDirectory(this.restoreString(dynamicBuild.getDirectory(), originalInterpolatedBuild.getDirectory(), changedBuild.getDirectory(), project, config, debugMessages));
      this.propagateNewPlugins(project);
      project.setBuild(dynamicBuild);
      project.clearRestorableBuild();
   }

   private List interpolateListOfStrings(List originalStrings, Model model, File projectDir, ProjectBuilderConfiguration config, boolean debugMessages) throws ModelInterpolationException {
      if (originalStrings == null) {
         return null;
      } else {
         List result = new ArrayList();
         Iterator it = originalStrings.iterator();

         while(it.hasNext()) {
            String original = (String)it.next();
            String interpolated = this.modelInterpolator.interpolate(original, model, projectDir, config, debugMessages);
            result.add(interpolated);
         }

         return result;
      }
   }

   private String restoreString(String originalString, String originalInterpolatedString, String changedString, MavenProject project, ProjectBuilderConfiguration config, boolean debugMessages) throws ModelInterpolationException {
      if (originalString == null) {
         return changedString;
      } else if (changedString == null) {
         return originalString;
      } else {
         Model model = project.getModel();
         String relativeChangedString;
         if (project.getBasedir() != null) {
            relativeChangedString = this.pathTranslator.unalignFromBaseDirectory(changedString, project.getBasedir());
         } else {
            relativeChangedString = changedString;
         }

         String interpolatedOriginal = this.modelInterpolator.interpolate(originalString, model, project.getBasedir(), config, debugMessages);
         interpolatedOriginal = this.pathTranslator.unalignFromBaseDirectory(interpolatedOriginal, project.getBasedir());
         String interpolatedOriginal2 = this.modelInterpolator.interpolate(originalInterpolatedString, model, project.getBasedir(), config, debugMessages);
         interpolatedOriginal2 = this.pathTranslator.alignToBaseDirectory(interpolatedOriginal2, project.getBasedir());
         String interpolatedChanged = this.modelInterpolator.interpolate(changedString, model, project.getBasedir(), config, debugMessages);
         interpolatedChanged = this.pathTranslator.alignToBaseDirectory(interpolatedChanged, project.getBasedir());
         String relativeInterpolatedChanged = this.modelInterpolator.interpolate(relativeChangedString, model, project.getBasedir(), config, debugMessages);
         if (!interpolatedOriginal.equals(interpolatedChanged) && !interpolatedOriginal2.equals(interpolatedChanged)) {
            return !interpolatedOriginal.equals(relativeInterpolatedChanged) && !interpolatedOriginal2.equals(relativeInterpolatedChanged) ? relativeChangedString : originalString;
         } else {
            return originalString;
         }
      }
   }

   private List restoreListOfStrings(List originalStrings, List originalInterpolatedStrings, List changedStrings, MavenProject project, ProjectBuilderConfiguration config, boolean debugMessages) throws ModelInterpolationException {
      if (originalStrings == null) {
         return changedStrings;
      } else if (changedStrings == null) {
         return originalStrings;
      } else {
         List result = new ArrayList();
         Map orig = new HashMap();

         for(int idx = 0; idx < originalStrings.size(); ++idx) {
            String[] permutations = new String[]{this.pathTranslator.alignToBaseDirectory((String)originalInterpolatedStrings.get(idx), project.getBasedir()), (String)originalStrings.get(idx)};
            orig.put(permutations[0], permutations);
         }

         Iterator it = changedStrings.iterator();

         while(it.hasNext()) {
            String changedString = (String)it.next();
            String relativeChangedString;
            if (project.getBasedir() != null) {
               relativeChangedString = this.pathTranslator.unalignFromBaseDirectory(changedString, project.getBasedir());
            } else {
               relativeChangedString = changedString;
            }

            String interpolated = this.modelInterpolator.interpolate(changedString, project.getModel(), project.getBasedir(), config, debugMessages);
            interpolated = this.pathTranslator.alignToBaseDirectory(interpolated, project.getBasedir());
            String relativeInterpolated = this.modelInterpolator.interpolate(relativeChangedString, project.getModel(), project.getBasedir(), config, debugMessages);
            String[] original = (String[])((String[])orig.get(interpolated));
            if (original == null) {
               original = (String[])((String[])orig.get(relativeInterpolated));
            }

            if (original == null) {
               result.add(relativeChangedString);
            } else {
               result.add(original[1]);
            }
         }

         return result;
      }
   }

   private List restoreResources(List originalResources, List originalInterpolatedResources, List changedResources, MavenProject project, ProjectBuilderConfiguration config, boolean debugMessages) throws ModelInterpolationException {
      if (originalResources != null && changedResources != null) {
         List result = new ArrayList();
         Map originalResourcesByMergeId = new HashMap();

         for(int idx = 0; idx < originalResources.size(); ++idx) {
            Resource[] permutations = new Resource[]{(Resource)originalInterpolatedResources.get(idx), (Resource)originalResources.get(idx)};
            originalResourcesByMergeId.put(permutations[0].getMergeId(), permutations);
         }

         Iterator it = changedResources.iterator();

         while(true) {
            while(it.hasNext()) {
               Resource resource = (Resource)it.next();
               String mergeId = resource.getMergeId();
               if (mergeId != null && originalResourcesByMergeId.containsKey(mergeId)) {
                  Resource originalInterpolatedResource = ((Resource[])((Resource[])originalResourcesByMergeId.get(mergeId)))[0];
                  Resource originalResource = ((Resource[])((Resource[])originalResourcesByMergeId.get(mergeId)))[1];
                  String dir = this.modelInterpolator.interpolate(resource.getDirectory(), project.getModel(), project.getBasedir(), config, this.getLogger().isDebugEnabled());
                  String oDir = originalInterpolatedResource.getDirectory();
                  if (!dir.equals(oDir)) {
                     originalResource.setDirectory(this.pathTranslator.unalignFromBaseDirectory(dir, project.getBasedir()));
                  }

                  if (resource.getTargetPath() != null) {
                     String target = this.modelInterpolator.interpolate(resource.getTargetPath(), project.getModel(), project.getBasedir(), config, this.getLogger().isDebugEnabled());
                     String oTarget = originalInterpolatedResource.getTargetPath();
                     if (!target.equals(oTarget)) {
                        originalResource.setTargetPath(this.pathTranslator.unalignFromBaseDirectory(target, project.getBasedir()));
                     }
                  }

                  originalResource.setFiltering(resource.isFiltering());
                  originalResource.setExcludes(this.collectRestoredListOfPatterns(resource.getExcludes(), originalResource.getExcludes(), originalInterpolatedResource.getExcludes()));
                  originalResource.setIncludes(this.collectRestoredListOfPatterns(resource.getIncludes(), originalResource.getIncludes(), originalInterpolatedResource.getIncludes()));
                  result.add(originalResource);
               } else {
                  result.add(resource);
               }
            }

            return result;
         }
      } else {
         return originalResources;
      }
   }

   private List collectRestoredListOfPatterns(List patterns, List originalPatterns, List originalInterpolatedPatterns) {
      LinkedHashSet collectedPatterns = new LinkedHashSet();
      collectedPatterns.addAll(originalPatterns);
      Iterator it = patterns.iterator();

      while(it.hasNext()) {
         String pattern = (String)it.next();
         if (!originalInterpolatedPatterns.contains(pattern)) {
            collectedPatterns.add(pattern);
         }
      }

      return (List)(collectedPatterns.isEmpty() ? Collections.EMPTY_LIST : new ArrayList(collectedPatterns));
   }
}

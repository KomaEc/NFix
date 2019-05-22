package org.apache.maven.project;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ManagedVersionMap;
import org.apache.maven.model.Build;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Developer;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.License;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.Model;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.Prerequisites;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.ReportSet;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Resource;
import org.apache.maven.model.Scm;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.project.artifact.ActiveProjectArtifact;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;
import org.apache.maven.project.artifact.MavenMetadataSource;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class MavenProject implements Cloneable {
   public static final String EMPTY_PROJECT_GROUP_ID = "unknown";
   public static final String EMPTY_PROJECT_ARTIFACT_ID = "empty-project";
   public static final String EMPTY_PROJECT_VERSION = "0";
   private Model model;
   private MavenProject parent;
   private File file;
   private Set artifacts;
   private Artifact parentArtifact;
   private Set pluginArtifacts;
   private List remoteArtifactRepositories;
   private List collectedProjects;
   private List attachedArtifacts;
   private MavenProject executionProject;
   private List compileSourceRoots;
   private List testCompileSourceRoots;
   private List scriptSourceRoots;
   private List pluginArtifactRepositories;
   private ArtifactRepository releaseArtifactRepository;
   private ArtifactRepository snapshotArtifactRepository;
   private List activeProfiles;
   private Set dependencyArtifacts;
   private Artifact artifact;
   private Map artifactMap;
   private Model originalModel;
   private Map pluginArtifactMap;
   private Set reportArtifacts;
   private Map reportArtifactMap;
   private Set extensionArtifacts;
   private Map extensionArtifactMap;
   private Map managedVersionMap;
   private Map projectReferences;
   private boolean executionRoot;
   private Map moduleAdjustments;
   private File basedir;
   private Logger logger;
   private ProjectBuilderConfiguration projectBuilderConfiguration;
   private Build dynamicBuild;
   private Build originalInterpolatedBuild;
   private List dynamicCompileSourceRoots;
   private List originalInterpolatedCompileSourceRoots;
   private List dynamicTestCompileSourceRoots;
   private List originalInterpolatedTestCompileSourceRoots;
   private List dynamicScriptSourceRoots;
   private List originalInterpolatedScriptSourceRoots;
   private boolean isConcrete;
   private Properties preservedProperties;
   private File preservedBasedir;

   public MavenProject() {
      this.collectedProjects = Collections.EMPTY_LIST;
      this.compileSourceRoots = new ArrayList();
      this.testCompileSourceRoots = new ArrayList();
      this.scriptSourceRoots = new ArrayList();
      this.activeProfiles = new ArrayList();
      this.projectReferences = new HashMap();
      this.isConcrete = false;
      Model model = new Model();
      model.setGroupId("unknown");
      model.setArtifactId("empty-project");
      model.setVersion("0");
      this.setModel(model);
   }

   public MavenProject(Model model) {
      this.collectedProjects = Collections.EMPTY_LIST;
      this.compileSourceRoots = new ArrayList();
      this.testCompileSourceRoots = new ArrayList();
      this.scriptSourceRoots = new ArrayList();
      this.activeProfiles = new ArrayList();
      this.projectReferences = new HashMap();
      this.isConcrete = false;
      this.setModel(model);
   }

   public MavenProject(Model model, Logger logger) {
      this.collectedProjects = Collections.EMPTY_LIST;
      this.compileSourceRoots = new ArrayList();
      this.testCompileSourceRoots = new ArrayList();
      this.scriptSourceRoots = new ArrayList();
      this.activeProfiles = new ArrayList();
      this.projectReferences = new HashMap();
      this.isConcrete = false;
      this.setModel(model);
      this.setLogger(logger);
   }

   /** @deprecated */
   public MavenProject(MavenProject project) {
      this.collectedProjects = Collections.EMPTY_LIST;
      this.compileSourceRoots = new ArrayList();
      this.testCompileSourceRoots = new ArrayList();
      this.scriptSourceRoots = new ArrayList();
      this.activeProfiles = new ArrayList();
      this.projectReferences = new HashMap();
      this.isConcrete = false;
      this.deepCopy(project);
   }

   private final void deepCopy(MavenProject project) {
      this.setFile(project.getFile());
      if (project.getDependencyArtifacts() != null) {
         this.setDependencyArtifacts(Collections.unmodifiableSet(project.getDependencyArtifacts()));
      }

      if (project.getArtifacts() != null) {
         this.setArtifacts(Collections.unmodifiableSet(project.getArtifacts()));
      }

      if (project.getPluginArtifacts() != null) {
         this.setPluginArtifacts(Collections.unmodifiableSet(project.getPluginArtifacts()));
      }

      if (project.getReportArtifacts() != null) {
         this.setReportArtifacts(Collections.unmodifiableSet(project.getReportArtifacts()));
      }

      if (project.getExtensionArtifacts() != null) {
         this.setExtensionArtifacts(Collections.unmodifiableSet(project.getExtensionArtifacts()));
      }

      this.setParentArtifact(project.getParentArtifact());
      if (project.getRemoteArtifactRepositories() != null) {
         this.setRemoteArtifactRepositories(Collections.unmodifiableList(project.getRemoteArtifactRepositories()));
      }

      if (project.getPluginArtifactRepositories() != null) {
         this.setPluginArtifactRepositories(Collections.unmodifiableList(project.getPluginArtifactRepositories()));
      }

      if (project.getCollectedProjects() != null) {
         this.setCollectedProjects(Collections.unmodifiableList(project.getCollectedProjects()));
      }

      if (project.getActiveProfiles() != null) {
         this.setActiveProfiles(Collections.unmodifiableList(project.getActiveProfiles()));
      }

      if (project.getAttachedArtifacts() != null) {
         this.setAttachedArtifacts(new ArrayList(project.getAttachedArtifacts()));
      }

      if (project.getCompileSourceRoots() != null) {
         this.setCompileSourceRoots(new ArrayList(project.getCompileSourceRoots()));
      }

      if (project.getTestCompileSourceRoots() != null) {
         this.setTestCompileSourceRoots(new ArrayList(project.getTestCompileSourceRoots()));
      }

      if (project.getScriptSourceRoots() != null) {
         this.setScriptSourceRoots(new ArrayList(project.getScriptSourceRoots()));
      }

      this.setModel(ModelUtils.cloneModel(project.getModel()));
      if (project.getOriginalModel() != null) {
         this.setOriginalModel(ModelUtils.cloneModel(project.getOriginalModel()));
      }

      this.setExecutionRoot(project.isExecutionRoot());
      if (project.getArtifact() != null) {
         this.setArtifact(ArtifactUtils.copyArtifact(project.getArtifact()));
      }

      if (project.getManagedVersionMap() != null) {
         this.setManagedVersionMap(new ManagedVersionMap(project.getManagedVersionMap()));
      }

      if (project.getReleaseArtifactRepository() != null) {
         this.setReleaseArtifactRepository(project.getReleaseArtifactRepository());
      }

      if (project.getSnapshotArtifactRepository() != null) {
         this.setSnapshotArtifactRepository(project.getSnapshotArtifactRepository());
      }

      if (project.isConcrete()) {
         this.setDynamicBuild(ModelUtils.cloneBuild(project.getDynamicBuild()));
         this.setOriginalInterpolatedBuild(ModelUtils.cloneBuild(project.getOriginalInterpolatedBuild()));
         List dynamicRoots = project.getDynamicCompileSourceRoots();
         if (dynamicRoots != null) {
            this.setDynamicCompileSourceRoots(new ArrayList(dynamicRoots));
            this.setOriginalInterpolatedCompileSourceRoots(new ArrayList(project.getOriginalInterpolatedCompileSourceRoots()));
         }

         dynamicRoots = project.getDynamicTestCompileSourceRoots();
         if (dynamicRoots != null) {
            this.setDynamicTestCompileSourceRoots(new ArrayList(dynamicRoots));
            this.setOriginalInterpolatedTestCompileSourceRoots(new ArrayList(project.getOriginalInterpolatedTestCompileSourceRoots()));
         }

         dynamicRoots = project.getDynamicScriptSourceRoots();
         if (dynamicRoots != null) {
            this.setDynamicScriptSourceRoots(new ArrayList(dynamicRoots));
            this.setOriginalInterpolatedScriptSourceRoots(new ArrayList(project.getOriginalInterpolatedScriptSourceRoots()));
         }
      }

      this.preservedProperties = project.preservedProperties;
      this.preservedBasedir = project.preservedBasedir;
      this.setConcrete(project.isConcrete());
   }

   public String getModulePathAdjustment(MavenProject moduleProject) throws IOException {
      String module = moduleProject.getArtifactId();
      File moduleFile = moduleProject.getFile();
      if (moduleFile != null) {
         File moduleDir = moduleFile.getCanonicalFile().getParentFile();
         module = moduleDir.getName();
      }

      if (this.moduleAdjustments == null) {
         this.moduleAdjustments = new HashMap();
         List modules = this.getModules();
         String moduleName;
         String adjustment;
         if (modules != null) {
            for(Iterator it = modules.iterator(); it.hasNext(); this.moduleAdjustments.put(moduleName, adjustment)) {
               String modulePath = (String)it.next();
               moduleName = modulePath;
               if (modulePath.endsWith("/") || modulePath.endsWith("\\")) {
                  moduleName = modulePath.substring(0, modulePath.length() - 1);
               }

               int lastSlash = moduleName.lastIndexOf(47);
               if (lastSlash < 0) {
                  lastSlash = moduleName.lastIndexOf(92);
               }

               adjustment = null;
               if (lastSlash > -1) {
                  moduleName = moduleName.substring(lastSlash + 1);
                  adjustment = modulePath.substring(0, lastSlash);
               }
            }
         }
      }

      return (String)this.moduleAdjustments.get(module);
   }

   public Artifact getArtifact() {
      return this.artifact;
   }

   public void setArtifact(Artifact artifact) {
      this.artifact = artifact;
   }

   public Model getModel() {
      return this.model;
   }

   public MavenProject getParent() {
      return this.parent;
   }

   public void setParent(MavenProject parent) {
      this.parent = parent;
   }

   public void setRemoteArtifactRepositories(List remoteArtifactRepositories) {
      this.remoteArtifactRepositories = remoteArtifactRepositories;
   }

   public List getRemoteArtifactRepositories() {
      return this.remoteArtifactRepositories;
   }

   public boolean hasParent() {
      return this.getParent() != null;
   }

   public File getFile() {
      return this.file;
   }

   public void setFile(File file) {
      if (file != null) {
         if (this.basedir == null) {
            this.basedir = file.getParentFile();
         }

         this.file = file;
      }
   }

   public void setBasedir(File basedir) {
      this.basedir = basedir;
   }

   public File getBasedir() {
      return this.basedir;
   }

   public void setDependencies(List dependencies) {
      this.getModel().setDependencies(dependencies);
   }

   public List getDependencies() {
      return this.getModel().getDependencies();
   }

   public DependencyManagement getDependencyManagement() {
      return this.getModel().getDependencyManagement();
   }

   public void addCompileSourceRoot(String path) {
      if (path != null) {
         path = path.trim();
         if (path.length() != 0 && !this.getCompileSourceRoots().contains(path)) {
            this.getCompileSourceRoots().add(path);
         }
      }

   }

   public void addScriptSourceRoot(String path) {
      if (path != null) {
         path = path.trim();
         if (path.length() != 0 && !this.getScriptSourceRoots().contains(path)) {
            this.getScriptSourceRoots().add(path);
         }
      }

   }

   public void addTestCompileSourceRoot(String path) {
      if (path != null) {
         path = path.trim();
         if (path.length() != 0 && !this.getTestCompileSourceRoots().contains(path)) {
            this.getTestCompileSourceRoots().add(path);
         }
      }

   }

   public List getCompileSourceRoots() {
      return this.compileSourceRoots;
   }

   public List getScriptSourceRoots() {
      return this.scriptSourceRoots;
   }

   public List getTestCompileSourceRoots() {
      return this.testCompileSourceRoots;
   }

   public List getCompileClasspathElements() throws DependencyResolutionRequiredException {
      List list = new ArrayList(this.getArtifacts().size());
      list.add(this.getBuild().getOutputDirectory());
      Iterator i = this.getArtifacts().iterator();

      while(true) {
         Artifact a;
         do {
            do {
               if (!i.hasNext()) {
                  return list;
               }

               a = (Artifact)i.next();
            } while(!a.getArtifactHandler().isAddedToClasspath());
         } while(!"compile".equals(a.getScope()) && !"provided".equals(a.getScope()) && !"system".equals(a.getScope()));

         this.addArtifactPath(a, list);
      }
   }

   public List getCompileArtifacts() {
      List list = new ArrayList(this.getArtifacts().size());
      Iterator i = this.getArtifacts().iterator();

      while(true) {
         Artifact a;
         do {
            do {
               if (!i.hasNext()) {
                  return list;
               }

               a = (Artifact)i.next();
            } while(!a.getArtifactHandler().isAddedToClasspath());
         } while(!"compile".equals(a.getScope()) && !"provided".equals(a.getScope()) && !"system".equals(a.getScope()));

         list.add(a);
      }
   }

   public List getCompileDependencies() {
      Set artifacts = this.getArtifacts();
      if (artifacts != null && !artifacts.isEmpty()) {
         List list = new ArrayList(artifacts.size());
         Iterator i = this.getArtifacts().iterator();

         while(true) {
            Artifact a;
            do {
               if (!i.hasNext()) {
                  return list;
               }

               a = (Artifact)i.next();
            } while(!"compile".equals(a.getScope()) && !"provided".equals(a.getScope()) && !"system".equals(a.getScope()));

            Dependency dependency = new Dependency();
            dependency.setArtifactId(a.getArtifactId());
            dependency.setGroupId(a.getGroupId());
            dependency.setVersion(a.getVersion());
            dependency.setScope(a.getScope());
            dependency.setType(a.getType());
            dependency.setClassifier(a.getClassifier());
            list.add(dependency);
         }
      } else {
         return Collections.EMPTY_LIST;
      }
   }

   public List getTestClasspathElements() throws DependencyResolutionRequiredException {
      List list = new ArrayList(this.getArtifacts().size() + 1);
      list.add(this.getBuild().getTestOutputDirectory());
      list.add(this.getBuild().getOutputDirectory());
      Iterator i = this.getArtifacts().iterator();

      while(i.hasNext()) {
         Artifact a = (Artifact)i.next();
         if (a.getArtifactHandler().isAddedToClasspath()) {
            File file = a.getFile();
            if (file == null) {
               throw new DependencyResolutionRequiredException(a);
            }

            list.add(file.getPath());
         }
      }

      return list;
   }

   public List getTestArtifacts() {
      List list = new ArrayList(this.getArtifacts().size());
      Iterator i = this.getArtifacts().iterator();

      while(i.hasNext()) {
         Artifact a = (Artifact)i.next();
         if (a.getArtifactHandler().isAddedToClasspath()) {
            list.add(a);
         }
      }

      return list;
   }

   public List getTestDependencies() {
      Set artifacts = this.getArtifacts();
      if (artifacts != null && !artifacts.isEmpty()) {
         List list = new ArrayList(artifacts.size());
         Iterator i = this.getArtifacts().iterator();

         while(i.hasNext()) {
            Artifact a = (Artifact)i.next();
            Dependency dependency = new Dependency();
            dependency.setArtifactId(a.getArtifactId());
            dependency.setGroupId(a.getGroupId());
            dependency.setVersion(a.getVersion());
            dependency.setScope(a.getScope());
            dependency.setType(a.getType());
            dependency.setClassifier(a.getClassifier());
            list.add(dependency);
         }

         return list;
      } else {
         return Collections.EMPTY_LIST;
      }
   }

   public List getRuntimeClasspathElements() throws DependencyResolutionRequiredException {
      List list = new ArrayList(this.getArtifacts().size() + 1);
      list.add(this.getBuild().getOutputDirectory());
      Iterator i = this.getArtifacts().iterator();

      while(true) {
         Artifact a;
         do {
            do {
               if (!i.hasNext()) {
                  return list;
               }

               a = (Artifact)i.next();
            } while(!a.getArtifactHandler().isAddedToClasspath());
         } while(!"compile".equals(a.getScope()) && !"runtime".equals(a.getScope()));

         File file = a.getFile();
         if (file == null) {
            throw new DependencyResolutionRequiredException(a);
         }

         list.add(file.getPath());
      }
   }

   public List getRuntimeArtifacts() {
      List list = new ArrayList(this.getArtifacts().size());
      Iterator i = this.getArtifacts().iterator();

      while(true) {
         Artifact a;
         do {
            do {
               if (!i.hasNext()) {
                  return list;
               }

               a = (Artifact)i.next();
            } while(!a.getArtifactHandler().isAddedToClasspath());
         } while(!"compile".equals(a.getScope()) && !"runtime".equals(a.getScope()));

         list.add(a);
      }
   }

   public List getRuntimeDependencies() {
      Set artifacts = this.getArtifacts();
      if (artifacts != null && !artifacts.isEmpty()) {
         List list = new ArrayList(artifacts.size());
         Iterator i = artifacts.iterator();

         while(true) {
            Artifact a;
            do {
               if (!i.hasNext()) {
                  return list;
               }

               a = (Artifact)i.next();
            } while(!"compile".equals(a.getScope()) && !"runtime".equals(a.getScope()));

            Dependency dependency = new Dependency();
            dependency.setArtifactId(a.getArtifactId());
            dependency.setGroupId(a.getGroupId());
            dependency.setVersion(a.getVersion());
            dependency.setScope(a.getScope());
            dependency.setType(a.getType());
            dependency.setClassifier(a.getClassifier());
            list.add(dependency);
         }
      } else {
         return Collections.EMPTY_LIST;
      }
   }

   public List getSystemClasspathElements() throws DependencyResolutionRequiredException {
      List list = new ArrayList(this.getArtifacts().size());
      list.add(this.getBuild().getOutputDirectory());
      Iterator i = this.getArtifacts().iterator();

      while(i.hasNext()) {
         Artifact a = (Artifact)i.next();
         if (a.getArtifactHandler().isAddedToClasspath() && "system".equals(a.getScope())) {
            this.addArtifactPath(a, list);
         }
      }

      return list;
   }

   public List getSystemArtifacts() {
      List list = new ArrayList(this.getArtifacts().size());
      Iterator i = this.getArtifacts().iterator();

      while(i.hasNext()) {
         Artifact a = (Artifact)i.next();
         if (a.getArtifactHandler().isAddedToClasspath() && "system".equals(a.getScope())) {
            list.add(a);
         }
      }

      return list;
   }

   public List getSystemDependencies() {
      Set artifacts = this.getArtifacts();
      if (artifacts != null && !artifacts.isEmpty()) {
         List list = new ArrayList(artifacts.size());
         Iterator i = this.getArtifacts().iterator();

         while(i.hasNext()) {
            Artifact a = (Artifact)i.next();
            if ("system".equals(a.getScope())) {
               Dependency dependency = new Dependency();
               dependency.setArtifactId(a.getArtifactId());
               dependency.setGroupId(a.getGroupId());
               dependency.setVersion(a.getVersion());
               dependency.setScope(a.getScope());
               dependency.setType(a.getType());
               dependency.setClassifier(a.getClassifier());
               list.add(dependency);
            }
         }

         return list;
      } else {
         return Collections.EMPTY_LIST;
      }
   }

   public void setModelVersion(String pomVersion) {
      this.getModel().setModelVersion(pomVersion);
   }

   public String getModelVersion() {
      return this.getModel().getModelVersion();
   }

   public String getId() {
      return this.getModel().getId();
   }

   public void setGroupId(String groupId) {
      this.getModel().setGroupId(groupId);
   }

   public String getGroupId() {
      String groupId = this.getModel().getGroupId();
      if (groupId == null && this.getModel().getParent() != null) {
         groupId = this.getModel().getParent().getGroupId();
      }

      return groupId;
   }

   public void setArtifactId(String artifactId) {
      this.getModel().setArtifactId(artifactId);
   }

   public String getArtifactId() {
      return this.getModel().getArtifactId();
   }

   public void setName(String name) {
      this.getModel().setName(name);
   }

   public String getName() {
      return this.getModel().getName() != null ? this.getModel().getName() : "Unnamed - " + this.getId();
   }

   public void setVersion(String version) {
      this.getModel().setVersion(version);
   }

   public String getVersion() {
      String version = this.getModel().getVersion();
      if (version == null && this.getModel().getParent() != null) {
         version = this.getModel().getParent().getVersion();
      }

      return version;
   }

   public String getPackaging() {
      return this.getModel().getPackaging();
   }

   public void setPackaging(String packaging) {
      this.getModel().setPackaging(packaging);
   }

   public void setInceptionYear(String inceptionYear) {
      this.getModel().setInceptionYear(inceptionYear);
   }

   public String getInceptionYear() {
      return this.getModel().getInceptionYear();
   }

   public void setUrl(String url) {
      this.getModel().setUrl(url);
   }

   public String getUrl() {
      return this.getModel().getUrl();
   }

   public Prerequisites getPrerequisites() {
      return this.getModel().getPrerequisites();
   }

   public void setIssueManagement(IssueManagement issueManagement) {
      this.getModel().setIssueManagement(issueManagement);
   }

   public CiManagement getCiManagement() {
      return this.getModel().getCiManagement();
   }

   public void setCiManagement(CiManagement ciManagement) {
      this.getModel().setCiManagement(ciManagement);
   }

   public IssueManagement getIssueManagement() {
      return this.getModel().getIssueManagement();
   }

   public void setDistributionManagement(DistributionManagement distributionManagement) {
      this.getModel().setDistributionManagement(distributionManagement);
   }

   public DistributionManagement getDistributionManagement() {
      return this.getModel().getDistributionManagement();
   }

   public void setDescription(String description) {
      this.getModel().setDescription(description);
   }

   public String getDescription() {
      return this.getModel().getDescription();
   }

   public void setOrganization(Organization organization) {
      this.getModel().setOrganization(organization);
   }

   public Organization getOrganization() {
      return this.getModel().getOrganization();
   }

   public void setScm(Scm scm) {
      this.getModel().setScm(scm);
   }

   public Scm getScm() {
      return this.getModel().getScm();
   }

   public void setMailingLists(List mailingLists) {
      this.getModel().setMailingLists(mailingLists);
   }

   public List getMailingLists() {
      return this.getModel().getMailingLists();
   }

   public void addMailingList(MailingList mailingList) {
      this.getModel().addMailingList(mailingList);
   }

   public void setDevelopers(List developers) {
      this.getModel().setDevelopers(developers);
   }

   public List getDevelopers() {
      return this.getModel().getDevelopers();
   }

   public void addDeveloper(Developer developer) {
      this.getModel().addDeveloper(developer);
   }

   public void setContributors(List contributors) {
      this.getModel().setContributors(contributors);
   }

   public List getContributors() {
      return this.getModel().getContributors();
   }

   public void addContributor(Contributor contributor) {
      this.getModel().addContributor(contributor);
   }

   public void setBuild(Build build) {
      this.getModel().setBuild(build);
   }

   public Build getBuild() {
      return this.getModelBuild();
   }

   public List getResources() {
      return this.getBuild().getResources();
   }

   public List getTestResources() {
      return this.getBuild().getTestResources();
   }

   public void addResource(Resource resource) {
      this.getBuild().addResource(resource);
   }

   public void addTestResource(Resource testResource) {
      this.getBuild().addTestResource(testResource);
   }

   public void setReporting(Reporting reporting) {
      this.getModel().setReporting(reporting);
   }

   public Reporting getReporting() {
      return this.getModel().getReporting();
   }

   public void setLicenses(List licenses) {
      this.getModel().setLicenses(licenses);
   }

   public List getLicenses() {
      return this.getModel().getLicenses();
   }

   public void addLicense(License license) {
      this.getModel().addLicense(license);
   }

   public void setArtifacts(Set artifacts) {
      this.artifacts = artifacts;
      this.artifactMap = null;
   }

   public Set getArtifacts() {
      return this.artifacts == null ? Collections.EMPTY_SET : this.artifacts;
   }

   public Map getArtifactMap() {
      if (this.artifactMap == null) {
         this.artifactMap = ArtifactUtils.artifactMapByVersionlessId(this.getArtifacts());
      }

      return this.artifactMap;
   }

   public void setPluginArtifacts(Set pluginArtifacts) {
      this.pluginArtifacts = pluginArtifacts;
      this.pluginArtifactMap = null;
   }

   public Set getPluginArtifacts() {
      return this.pluginArtifacts;
   }

   public Map getPluginArtifactMap() {
      if (this.pluginArtifactMap == null) {
         this.pluginArtifactMap = ArtifactUtils.artifactMapByVersionlessId(this.getPluginArtifacts());
      }

      return this.pluginArtifactMap;
   }

   public void setReportArtifacts(Set reportArtifacts) {
      this.reportArtifacts = reportArtifacts;
      this.reportArtifactMap = null;
   }

   public Set getReportArtifacts() {
      return this.reportArtifacts;
   }

   public Map getReportArtifactMap() {
      if (this.reportArtifactMap == null) {
         this.reportArtifactMap = ArtifactUtils.artifactMapByVersionlessId(this.getReportArtifacts());
      }

      return this.reportArtifactMap;
   }

   public void setExtensionArtifacts(Set extensionArtifacts) {
      this.extensionArtifacts = extensionArtifacts;
      this.extensionArtifactMap = null;
   }

   public Set getExtensionArtifacts() {
      return this.extensionArtifacts;
   }

   public Map getExtensionArtifactMap() {
      if (this.extensionArtifactMap == null) {
         this.extensionArtifactMap = ArtifactUtils.artifactMapByVersionlessId(this.getExtensionArtifacts());
      }

      return this.extensionArtifactMap;
   }

   public void setParentArtifact(Artifact parentArtifact) {
      this.parentArtifact = parentArtifact;
   }

   public Artifact getParentArtifact() {
      return this.parentArtifact;
   }

   public List getRepositories() {
      return this.getModel().getRepositories();
   }

   public List getReportPlugins() {
      return this.getModel().getReporting() == null ? null : this.getModel().getReporting().getPlugins();
   }

   public List getBuildPlugins() {
      return this.getModel().getBuild() == null ? null : this.getModel().getBuild().getPlugins();
   }

   public List getModules() {
      return this.getModel().getModules();
   }

   public PluginManagement getPluginManagement() {
      PluginManagement pluginMgmt = null;
      Build build = this.getModel().getBuild();
      if (build != null) {
         pluginMgmt = build.getPluginManagement();
      }

      return pluginMgmt;
   }

   private Build getModelBuild() {
      Build build = this.getModel().getBuild();
      if (build == null) {
         build = new Build();
         this.getModel().setBuild(build);
      }

      return build;
   }

   public void addPlugin(Plugin plugin) {
      Build build = this.getModelBuild();
      if (!build.getPluginsAsMap().containsKey(plugin.getKey())) {
         this.injectPluginManagementInfo(plugin);
         build.addPlugin(plugin);
         build.flushPluginMap();
      }

   }

   public void injectPluginManagementInfo(Plugin plugin) {
      PluginManagement pm = this.getModelBuild().getPluginManagement();
      if (pm != null) {
         Map pmByKey = pm.getPluginsAsMap();
         String pluginKey = plugin.getKey();
         if (pmByKey != null && pmByKey.containsKey(pluginKey)) {
            Plugin pmPlugin = (Plugin)pmByKey.get(pluginKey);
            ModelUtils.mergePluginDefinitions(plugin, pmPlugin, false);
         }
      }

   }

   public List getCollectedProjects() {
      return this.collectedProjects;
   }

   public void setCollectedProjects(List collectedProjects) {
      this.collectedProjects = collectedProjects;
   }

   public void setPluginArtifactRepositories(List pluginArtifactRepositories) {
      this.pluginArtifactRepositories = pluginArtifactRepositories;
   }

   public List getPluginArtifactRepositories() {
      return this.pluginArtifactRepositories;
   }

   public ArtifactRepository getDistributionManagementArtifactRepository() {
      return this.getArtifact().isSnapshot() && this.getSnapshotArtifactRepository() != null ? this.getSnapshotArtifactRepository() : this.getReleaseArtifactRepository();
   }

   public List getPluginRepositories() {
      return this.getModel().getPluginRepositories();
   }

   public void setActiveProfiles(List activeProfiles) {
      this.activeProfiles.addAll(activeProfiles);
   }

   public List getActiveProfiles() {
      return this.activeProfiles;
   }

   public void addAttachedArtifact(Artifact artifact) {
      this.getAttachedArtifacts().add(artifact);
   }

   public List getAttachedArtifacts() {
      if (this.attachedArtifacts == null) {
         this.attachedArtifacts = new ArrayList();
      }

      return this.attachedArtifacts;
   }

   public Xpp3Dom getGoalConfiguration(String pluginGroupId, String pluginArtifactId, String executionId, String goalId) {
      Xpp3Dom dom = null;
      if (this.getBuildPlugins() != null) {
         Iterator iterator = this.getBuildPlugins().iterator();

         while(iterator.hasNext()) {
            Plugin plugin = (Plugin)iterator.next();
            if (pluginGroupId.equals(plugin.getGroupId()) && pluginArtifactId.equals(plugin.getArtifactId())) {
               dom = (Xpp3Dom)plugin.getConfiguration();
               if (executionId != null) {
                  PluginExecution execution = (PluginExecution)plugin.getExecutionsAsMap().get(executionId);
                  if (execution != null) {
                     Xpp3Dom executionConfiguration = (Xpp3Dom)execution.getConfiguration();
                     if (executionConfiguration != null) {
                        Xpp3Dom newDom = new Xpp3Dom(executionConfiguration);
                        dom = Xpp3Dom.mergeXpp3Dom(newDom, dom);
                     }
                  }
               }
               break;
            }
         }
      }

      if (dom != null) {
         dom = new Xpp3Dom(dom);
      }

      return dom;
   }

   public Xpp3Dom getReportConfiguration(String pluginGroupId, String pluginArtifactId, String reportSetId) {
      Xpp3Dom dom = null;
      if (this.getReportPlugins() != null) {
         Iterator iterator = this.getReportPlugins().iterator();

         while(iterator.hasNext()) {
            ReportPlugin plugin = (ReportPlugin)iterator.next();
            if (pluginGroupId.equals(plugin.getGroupId()) && pluginArtifactId.equals(plugin.getArtifactId())) {
               dom = (Xpp3Dom)plugin.getConfiguration();
               if (reportSetId != null) {
                  ReportSet reportSet = (ReportSet)plugin.getReportSetsAsMap().get(reportSetId);
                  if (reportSet != null) {
                     Xpp3Dom executionConfiguration = (Xpp3Dom)reportSet.getConfiguration();
                     if (executionConfiguration != null) {
                        Xpp3Dom newDom = new Xpp3Dom(executionConfiguration);
                        dom = Xpp3Dom.mergeXpp3Dom(newDom, dom);
                     }
                  }
               }
               break;
            }
         }
      }

      if (dom != null) {
         dom = new Xpp3Dom(dom);
      }

      return dom;
   }

   public MavenProject getExecutionProject() {
      return this.executionProject;
   }

   public void setExecutionProject(MavenProject executionProject) {
      this.executionProject = executionProject;
   }

   public void writeModel(Writer writer) throws IOException {
      MavenXpp3Writer pomWriter = new MavenXpp3Writer();
      pomWriter.write(writer, this.getModel());
   }

   public void writeOriginalModel(Writer writer) throws IOException {
      MavenXpp3Writer pomWriter = new MavenXpp3Writer();
      pomWriter.write(writer, this.getOriginalModel());
   }

   public Set getDependencyArtifacts() {
      return this.dependencyArtifacts;
   }

   public void setDependencyArtifacts(Set dependencyArtifacts) {
      this.dependencyArtifacts = dependencyArtifacts;
   }

   public void setReleaseArtifactRepository(ArtifactRepository releaseArtifactRepository) {
      this.releaseArtifactRepository = releaseArtifactRepository;
   }

   public void setSnapshotArtifactRepository(ArtifactRepository snapshotArtifactRepository) {
      this.snapshotArtifactRepository = snapshotArtifactRepository;
   }

   public void setOriginalModel(Model originalModel) {
      this.originalModel = originalModel;
   }

   public Model getOriginalModel() {
      return this.originalModel;
   }

   public void setManagedVersionMap(Map map) {
      this.managedVersionMap = map;
   }

   public Map getManagedVersionMap() {
      return this.managedVersionMap;
   }

   public boolean equals(Object other) {
      if (other == this) {
         return true;
      } else if (!(other instanceof MavenProject)) {
         return false;
      } else {
         MavenProject otherProject = (MavenProject)other;
         return this.getId().equals(otherProject.getId());
      }
   }

   public int hashCode() {
      return this.getId().hashCode();
   }

   public List getBuildExtensions() {
      Build build = this.getBuild();
      return build != null && build.getExtensions() != null ? build.getExtensions() : Collections.EMPTY_LIST;
   }

   public Set createArtifacts(ArtifactFactory artifactFactory, String inheritedScope, ArtifactFilter dependencyFilter) throws InvalidDependencyVersionException {
      return MavenMetadataSource.createArtifacts(artifactFactory, this.getDependencies(), inheritedScope, dependencyFilter, this);
   }

   public void addProjectReference(MavenProject project) {
      this.projectReferences.put(getProjectReferenceId(project.getGroupId(), project.getArtifactId(), project.getVersion()), project);
   }

   public static String getProjectReferenceId(String groupId, String artifactId, String version) {
      return groupId + ":" + artifactId + ":" + version;
   }

   /** @deprecated */
   public void attachArtifact(String type, String classifier, File file) {
   }

   public Properties getProperties() {
      return this.getModel().getProperties();
   }

   public List getFilters() {
      return this.getBuild().getFilters();
   }

   public Map getProjectReferences() {
      return this.projectReferences;
   }

   public boolean isExecutionRoot() {
      return this.executionRoot;
   }

   public void setExecutionRoot(boolean executionRoot) {
      this.executionRoot = executionRoot;
   }

   public String getDefaultGoal() {
      return this.getBuild() != null ? this.getBuild().getDefaultGoal() : null;
   }

   protected void setModel(Model model) {
      this.model = model;
   }

   protected void setAttachedArtifacts(List attachedArtifacts) {
      this.attachedArtifacts = attachedArtifacts;
   }

   protected void setCompileSourceRoots(List compileSourceRoots) {
      this.compileSourceRoots = compileSourceRoots;
   }

   protected void setTestCompileSourceRoots(List testCompileSourceRoots) {
      this.testCompileSourceRoots = testCompileSourceRoots;
   }

   protected void setScriptSourceRoots(List scriptSourceRoots) {
      this.scriptSourceRoots = scriptSourceRoots;
   }

   protected ArtifactRepository getReleaseArtifactRepository() {
      return this.releaseArtifactRepository;
   }

   protected ArtifactRepository getSnapshotArtifactRepository() {
      return this.snapshotArtifactRepository;
   }

   public void resolveActiveArtifacts() {
      Set depArtifacts = this.getDependencyArtifacts();
      if (depArtifacts != null) {
         Set updated = new LinkedHashSet(depArtifacts.size());
         int updatedCount = 0;

         Artifact replaced;
         for(Iterator it = depArtifacts.iterator(); it.hasNext(); updated.add(replaced)) {
            Artifact depArtifact = (Artifact)it.next();
            replaced = this.replaceWithActiveArtifact(depArtifact);
            if (depArtifact != replaced) {
               ++updatedCount;
            }
         }

         if (updatedCount > 0) {
            this.setDependencyArtifacts(updated);
         }

      }
   }

   public Artifact replaceWithActiveArtifact(Artifact pluginArtifact) {
      if (this.getProjectReferences() != null && !this.getProjectReferences().isEmpty()) {
         String refId = getProjectReferenceId(pluginArtifact.getGroupId(), pluginArtifact.getArtifactId(), pluginArtifact.getVersion());
         MavenProject ref = (MavenProject)this.getProjectReferences().get(refId);
         if (ref != null) {
            if (ref.getArtifact() != null && ref.getArtifact().getDependencyConflictId().equals(pluginArtifact.getDependencyConflictId())) {
               if (ref.getArtifact().getFile() != null && ref.getArtifact().getFile().exists()) {
                  Artifact resultArtifact = new ActiveProjectArtifact(ref, pluginArtifact);
                  return resultArtifact;
               }

               this.logMissingSiblingProjectArtifact(pluginArtifact);
            }

            Artifact attached = this.findMatchingArtifact(ref.getAttachedArtifacts(), pluginArtifact);
            if (attached != null) {
               if (attached.getFile() != null && attached.getFile().exists()) {
                  Artifact resultArtifact = ArtifactUtils.copyArtifact(attached);
                  resultArtifact.setScope(pluginArtifact.getScope());
                  return resultArtifact;
               }

               this.logMissingSiblingProjectArtifact(pluginArtifact);
            }
         }
      }

      return pluginArtifact;
   }

   private Artifact findMatchingArtifact(List artifacts, Artifact requestedArtifact) {
      if (artifacts != null && !artifacts.isEmpty()) {
         String requestedId = requestedArtifact.getDependencyConflictId();
         Iterator it = artifacts.iterator();

         Artifact artifact;
         while(it.hasNext()) {
            artifact = (Artifact)it.next();
            if (requestedId.equals(artifact.getDependencyConflictId())) {
               return artifact;
            }
         }

         requestedId = this.getRepositoryConflictId(requestedArtifact);
         it = artifacts.iterator();

         while(it.hasNext()) {
            artifact = (Artifact)it.next();
            if (requestedId.equals(this.getRepositoryConflictId(artifact))) {
               return artifact;
            }
         }
      }

      return null;
   }

   private String getRepositoryConflictId(Artifact artifact) {
      StringBuffer buffer = new StringBuffer(128);
      buffer.append(artifact.getGroupId());
      buffer.append(':').append(artifact.getArtifactId());
      if (artifact.getArtifactHandler() != null) {
         buffer.append(':').append(artifact.getArtifactHandler().getExtension());
      } else {
         buffer.append(':').append(artifact.getType());
      }

      if (artifact.hasClassifier()) {
         buffer.append(':').append(artifact.getClassifier());
      }

      return buffer.toString();
   }

   private void logMissingSiblingProjectArtifact(Artifact artifact) {
      if (this.logger != null && this.logger.isDebugEnabled()) {
         if (this.logger.isDebugEnabled()) {
            StringBuffer message = new StringBuffer();
            message.append("WARNING: A dependency of the current project (or of one the plugins used in its build) was found in the reactor, ");
            message.append("\nbut had not been built at the time it was requested. It will be resolved from the repository instead.");
            message.append("\n\nCurrent Project: ").append(this.getName());
            message.append("\nRequested Dependency: ").append(artifact.getId());
            message.append("\n\nNOTE: You may need to run this build to the 'compile' lifecycle phase, or farther, in order to build the dependency artifact.");
            message.append("\n");
            this.logger.debug(message.toString());
         } else {
            this.logger.warn("Requested project artifact: " + artifact.getId() + " is not available at this time. Resolving externally.");
         }

      }
   }

   private void addArtifactPath(Artifact a, List list) throws DependencyResolutionRequiredException {
      File file = a.getFile();
      if (file == null) {
         throw new DependencyResolutionRequiredException(a);
      } else {
         list.add(file.getPath());
      }
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(30);
      sb.append("MavenProject: ");
      sb.append(this.getGroupId());
      sb.append(":");
      sb.append(this.getArtifactId());
      sb.append(":");
      sb.append(this.getVersion());
      sb.append(" @ ");

      try {
         sb.append(this.getFile().getPath());
      } catch (NullPointerException var3) {
      }

      return sb.toString();
   }

   public Object clone() throws CloneNotSupportedException {
      MavenProject clone = (MavenProject)super.clone();
      clone.deepCopy(this);
      return clone;
   }

   public boolean isConcrete() {
      return this.isConcrete;
   }

   public void setConcrete(boolean concrete) {
      this.isConcrete = concrete;
   }

   public Build getDynamicBuild() {
      return this.dynamicBuild;
   }

   public Build getOriginalInterpolatedBuild() {
      return this.originalInterpolatedBuild;
   }

   public List getDynamicCompileSourceRoots() {
      return this.dynamicCompileSourceRoots;
   }

   public List getOriginalInterpolatedCompileSourceRoots() {
      return this.originalInterpolatedCompileSourceRoots;
   }

   public List getDynamicTestCompileSourceRoots() {
      return this.dynamicTestCompileSourceRoots;
   }

   public List getOriginalInterpolatedTestCompileSourceRoots() {
      return this.originalInterpolatedTestCompileSourceRoots;
   }

   public List getDynamicScriptSourceRoots() {
      return this.dynamicScriptSourceRoots;
   }

   public List getOriginalInterpolatedScriptSourceRoots() {
      return this.originalInterpolatedScriptSourceRoots;
   }

   public void clearRestorableRoots() {
      this.dynamicCompileSourceRoots = null;
      this.dynamicTestCompileSourceRoots = null;
      this.dynamicScriptSourceRoots = null;
      this.originalInterpolatedCompileSourceRoots = null;
      this.originalInterpolatedScriptSourceRoots = null;
      this.originalInterpolatedTestCompileSourceRoots = null;
   }

   public void clearRestorableBuild() {
      this.dynamicBuild = null;
      this.originalInterpolatedBuild = null;
   }

   public void preserveCompileSourceRoots(List originalInterpolatedCompileSourceRoots) {
      this.dynamicCompileSourceRoots = this.getCompileSourceRoots();
      this.originalInterpolatedCompileSourceRoots = originalInterpolatedCompileSourceRoots;
   }

   public void preserveTestCompileSourceRoots(List originalInterpolatedTestCompileSourceRoots) {
      this.dynamicTestCompileSourceRoots = this.getTestCompileSourceRoots();
      this.originalInterpolatedTestCompileSourceRoots = originalInterpolatedTestCompileSourceRoots;
   }

   public void preserveScriptSourceRoots(List originalInterpolatedScriptSourceRoots) {
      this.dynamicScriptSourceRoots = this.getScriptSourceRoots();
      this.originalInterpolatedScriptSourceRoots = originalInterpolatedScriptSourceRoots;
   }

   public void preserveBuild(Build originalInterpolatedBuild) {
      this.dynamicBuild = this.getBuild();
      this.originalInterpolatedBuild = originalInterpolatedBuild;
      this.originalInterpolatedBuild.setPluginManagement((PluginManagement)null);
      this.originalInterpolatedBuild.setPlugins((List)null);
   }

   protected void setDynamicBuild(Build dynamicBuild) {
      this.dynamicBuild = dynamicBuild;
   }

   protected void setOriginalInterpolatedBuild(Build originalInterpolatedBuild) {
      this.originalInterpolatedBuild = originalInterpolatedBuild;
   }

   protected void setDynamicCompileSourceRoots(List dynamicCompileSourceRoots) {
      this.dynamicCompileSourceRoots = dynamicCompileSourceRoots;
   }

   protected void setOriginalInterpolatedCompileSourceRoots(List originalInterpolatedCompileSourceRoots) {
      this.originalInterpolatedCompileSourceRoots = originalInterpolatedCompileSourceRoots;
   }

   protected void setDynamicTestCompileSourceRoots(List dynamicTestCompileSourceRoots) {
      this.dynamicTestCompileSourceRoots = dynamicTestCompileSourceRoots;
   }

   protected void setOriginalInterpolatedTestCompileSourceRoots(List originalInterpolatedTestCompileSourceRoots) {
      this.originalInterpolatedTestCompileSourceRoots = originalInterpolatedTestCompileSourceRoots;
   }

   protected void setDynamicScriptSourceRoots(List dynamicScriptSourceRoots) {
      this.dynamicScriptSourceRoots = dynamicScriptSourceRoots;
   }

   protected void setOriginalInterpolatedScriptSourceRoots(List originalInterpolatedScriptSourceRoots) {
      this.originalInterpolatedScriptSourceRoots = originalInterpolatedScriptSourceRoots;
   }

   public Properties getPreservedProperties() {
      return this.preservedProperties;
   }

   public void preserveProperties() {
      Properties p = this.getProperties();
      if (p != null) {
         this.preservedProperties = new Properties();
         Enumeration e = p.propertyNames();

         while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            this.preservedProperties.setProperty(key, p.getProperty(key));
         }
      }

   }

   public File getPreservedBasedir() {
      return this.preservedBasedir;
   }

   public void preserveBasedir() {
      this.preservedBasedir = this.getBasedir();
   }

   public void setLogger(Logger logger) {
      this.logger = logger;
   }

   public ProjectBuilderConfiguration getProjectBuilderConfiguration() {
      return this.projectBuilderConfiguration;
   }

   public void setProjectBuilderConfiguration(ProjectBuilderConfiguration projectBuilderConfiguration) {
      this.projectBuilderConfiguration = projectBuilderConfiguration;
   }
}

package org.apache.maven;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.execution.BuildFailure;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ReactorManager;
import org.apache.maven.execution.RuntimeInformation;
import org.apache.maven.lifecycle.LifecycleExecutionException;
import org.apache.maven.lifecycle.LifecycleExecutor;
import org.apache.maven.monitor.event.DefaultEventDispatcher;
import org.apache.maven.monitor.event.EventDispatcher;
import org.apache.maven.profiles.ProfileManager;
import org.apache.maven.profiles.activation.ProfileActivationException;
import org.apache.maven.project.DefaultProjectBuilderConfiguration;
import org.apache.maven.project.DuplicateProjectException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuilderConfiguration;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.reactor.MavenExecutionException;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.usability.SystemWarnings;
import org.apache.maven.usability.diagnostics.ErrorDiagnostics;
import org.apache.maven.wagon.repository.RepositoryPermissions;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.dag.CycleDetectedException;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class DefaultMaven extends AbstractLogEnabled implements Maven, Contextualizable {
   protected MavenProjectBuilder projectBuilder;
   protected LifecycleExecutor lifecycleExecutor;
   protected PlexusContainer container;
   protected ErrorDiagnostics errorDiagnostics;
   protected RuntimeInformation runtimeInformation;
   private static final long MB = 1048576L;
   private static final int MS_PER_SEC = 1000;
   private static final int SEC_PER_MIN = 60;

   public void execute(MavenExecutionRequest request) throws MavenExecutionException {
      EventDispatcher dispatcher = request.getEventDispatcher();
      String event = "reactor-execute";
      dispatcher.dispatchStart(event, request.getBaseDirectory());

      ReactorManager rm;
      try {
         rm = this.doExecute(request, dispatcher);
      } catch (LifecycleExecutionException var6) {
         dispatcher.dispatchError(event, request.getBaseDirectory(), var6);
         this.logError(var6, request.isShowErrors());
         this.stats(request.getStartTime());
         this.line();
         throw new MavenExecutionException(var6.getMessage(), var6);
      } catch (BuildFailureException var7) {
         dispatcher.dispatchError(event, request.getBaseDirectory(), var7);
         this.logFailure(var7, request.isShowErrors());
         this.stats(request.getStartTime());
         this.line();
         throw new MavenExecutionException(var7.getMessage(), var7);
      } catch (Throwable var8) {
         dispatcher.dispatchError(event, request.getBaseDirectory(), var8);
         this.logFatal(var8);
         this.stats(request.getStartTime());
         this.line();
         throw new MavenExecutionException("Error executing project within the reactor", var8);
      }

      this.logReactorSummary(rm);
      if (rm.hasBuildFailures()) {
         this.logErrors(rm, request.isShowErrors());
         if (!"fail-never".equals(rm.getFailureBehavior())) {
            dispatcher.dispatchError(event, request.getBaseDirectory(), (Throwable)null);
            this.getLogger().info("BUILD ERRORS");
            this.line();
            this.stats(request.getStartTime());
            this.line();
            throw new MavenExecutionException("Some builds failed");
         }

         this.getLogger().info(" + Ignoring failures");
      }

      this.logSuccess(rm);
      this.stats(request.getStartTime());
      this.line();
      dispatcher.dispatchEnd(event, request.getBaseDirectory());
   }

   private void logErrors(ReactorManager rm, boolean showErrors) {
      Iterator it = rm.getSortedProjects().iterator();

      while(it.hasNext()) {
         MavenProject project = (MavenProject)it.next();
         if (rm.hasBuildFailure(project)) {
            BuildFailure buildFailure = rm.getBuildFailure(project);
            this.getLogger().info("Error for project: " + project.getName() + " (during " + buildFailure.getTask() + ")");
            this.line();
            this.logDiagnostics(buildFailure.getCause());
            this.logTrace(buildFailure.getCause(), showErrors);
         }
      }

      if (!showErrors) {
         this.getLogger().info("For more information, run Maven with the -e switch");
         this.line();
      }

   }

   private ReactorManager doExecute(MavenExecutionRequest request, EventDispatcher dispatcher) throws MavenExecutionException, BuildFailureException, LifecycleExecutionException {
      if (request.getSettings().isOffline()) {
         this.getLogger().info(SystemWarnings.getOfflineWarning());
         WagonManager wagonManager = null;

         try {
            wagonManager = (WagonManager)this.container.lookup(WagonManager.ROLE);
            wagonManager.setOnline(false);
         } catch (ComponentLookupException var24) {
            throw new MavenExecutionException("Cannot retrieve WagonManager in order to set offline mode.", var24);
         } finally {
            try {
               this.container.release(wagonManager);
            } catch (ComponentLifecycleException var18) {
               this.getLogger().warn("Cannot release WagonManager.", var18);
            }

         }
      }

      try {
         this.resolveParameters(request.getSettings());
      } catch (ComponentLookupException var21) {
         throw new MavenExecutionException("Unable to configure Maven for execution", var21);
      } catch (ComponentLifecycleException var22) {
         throw new MavenExecutionException("Unable to configure Maven for execution", var22);
      } catch (SettingsConfigurationException var23) {
         throw new MavenExecutionException("Unable to configure Maven for execution", var23);
      }

      ProfileManager globalProfileManager = request.getGlobalProfileManager();
      globalProfileManager.loadSettingsProfiles(request.getSettings());
      this.getLogger().info("Scanning for projects...");
      boolean foundProjects = true;
      List projects = this.getProjects(request);
      if (projects.isEmpty()) {
         projects.add(this.getSuperProject(request));
         foundProjects = false;
      }

      ReactorManager rm;
      try {
         rm = new ReactorManager(projects);
         String requestFailureBehavior = request.getFailureBehavior();
         if (requestFailureBehavior != null) {
            rm.setFailureBehavior(requestFailureBehavior);
         }
      } catch (CycleDetectedException var19) {
         throw new BuildFailureException("The projects in the reactor contain a cyclic reference: " + var19.getMessage(), var19);
      } catch (DuplicateProjectException var20) {
         throw new BuildFailureException(var20.getMessage(), var20);
      }

      if (rm.hasMultipleProjects()) {
         this.getLogger().info("Reactor build order: ");
         Iterator i = rm.getSortedProjects().iterator();

         while(i.hasNext()) {
            MavenProject project = (MavenProject)i.next();
            this.getLogger().info("  " + project.getName());
         }
      }

      MavenSession session = this.createSession(request, rm);
      session.setUsingPOMsFromFilesystem(foundProjects);
      this.lifecycleExecutor.execute(session, rm, dispatcher);
      return rm;
   }

   private MavenProject getSuperProject(MavenExecutionRequest request) throws MavenExecutionException {
      try {
         MavenProject superProject = this.projectBuilder.buildStandaloneSuperProject(request.getLocalRepository(), request.getGlobalProfileManager());
         return superProject;
      } catch (ProjectBuildingException var4) {
         throw new MavenExecutionException(var4.getMessage(), var4);
      }
   }

   private List getProjects(MavenExecutionRequest request) throws MavenExecutionException, BuildFailureException {
      try {
         List files = this.getProjectFiles(request);
         List projects = this.collectProjects(files, request, !request.isReactorActive());
         return projects;
      } catch (IOException var4) {
         throw new MavenExecutionException("Error processing projects for the reactor: " + var4.getMessage(), var4);
      } catch (ArtifactResolutionException var5) {
         throw new MavenExecutionException(var5.getMessage(), var5);
      } catch (ProjectBuildingException var6) {
         throw new MavenExecutionException(var6.getMessage(), var6);
      } catch (ProfileActivationException var7) {
         throw new MavenExecutionException(var7.getMessage(), var7);
      }
   }

   private void logReactorSummaryLine(String name, String status) {
      this.logReactorSummaryLine(name, status, -1L);
   }

   private void logReactorSummaryLine(String name, String status, long time) {
      StringBuffer messageBuffer = new StringBuffer();
      messageBuffer.append(name);
      int dotCount = 54;
      int dotCount = dotCount - name.length();
      messageBuffer.append(" ");

      for(int i = 0; i < dotCount; ++i) {
         messageBuffer.append('.');
      }

      messageBuffer.append(" ");
      messageBuffer.append(status);
      if (time >= 0L) {
         messageBuffer.append(" [");
         messageBuffer.append(getFormattedTime(time));
         messageBuffer.append("]");
      }

      this.getLogger().info(messageBuffer.toString());
   }

   private static String getFormattedTime(long time) {
      String pattern = "s.SSS's'";
      if (time / 60000L > 0L) {
         pattern = "m:s" + pattern;
         if (time / 3600000L > 0L) {
            pattern = "H:m" + pattern;
         }
      }

      DateFormat fmt = new SimpleDateFormat(pattern);
      fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
      return fmt.format(new Date(time));
   }

   private List collectProjects(List files, MavenExecutionRequest request, boolean isRoot) throws ArtifactResolutionException, ProjectBuildingException, ProfileActivationException, MavenExecutionException, BuildFailureException {
      List projects = new ArrayList(files.size());

      MavenProject project;
      for(Iterator iterator = files.iterator(); iterator.hasNext(); projects.add(project)) {
         File file = (File)iterator.next();
         boolean usingReleasePom = false;
         if ("release-pom.xml".equals(file.getName())) {
            this.getLogger().info("NOTE: Using release-pom: " + file + " in reactor build.");
            usingReleasePom = true;
         }

         project = this.getProject(file, request);
         if (isRoot) {
            project.setExecutionRoot(true);
         }

         if (project.getPrerequisites() != null && project.getPrerequisites().getMaven() != null) {
            DefaultArtifactVersion version = new DefaultArtifactVersion(project.getPrerequisites().getMaven());
            if (this.runtimeInformation.getApplicationVersion().compareTo(version) < 0) {
               throw new BuildFailureException("Unable to build project '" + project.getFile() + "; it requires Maven version " + version.toString());
            }
         }

         if (project.getModules() != null && !project.getModules().isEmpty() && request.isRecursive()) {
            project.setPackaging("pom");
            File basedir = file.getParentFile();
            List moduleFiles = new ArrayList(project.getModules().size());
            Iterator i = project.getModules().iterator();

            while(i.hasNext()) {
               String name = (String)i.next();
               if (StringUtils.isEmpty(StringUtils.trim(name))) {
                  this.getLogger().warn("Empty module detected. Please check you don't have any empty module definitions in your POM.");
               } else {
                  File moduleFile = new File(basedir, name);
                  if (moduleFile.exists() && moduleFile.isDirectory()) {
                     if (usingReleasePom) {
                        moduleFile = new File(basedir, name + "/" + "release-pom.xml");
                     } else {
                        moduleFile = new File(basedir, name + "/" + "pom.xml");
                     }
                  }

                  if (Os.isFamily("windows")) {
                     try {
                        moduleFile = moduleFile.getCanonicalFile();
                     } catch (IOException var15) {
                        throw new MavenExecutionException("Unable to canonicalize file name " + moduleFile, var15);
                     }
                  } else {
                     moduleFile = new File(moduleFile.toURI().normalize());
                  }

                  moduleFiles.add(moduleFile);
               }
            }

            List collectedProjects = this.collectProjects(moduleFiles, request, false);
            projects.addAll(collectedProjects);
            project.setCollectedProjects(collectedProjects);
         }
      }

      return projects;
   }

   /** @deprecated */
   public MavenProject getProject(File pom, ArtifactRepository localRepository, Settings settings, Properties userProperties, ProfileManager globalProfileManager) throws ProjectBuildingException, ArtifactResolutionException, ProfileActivationException {
      MavenExecutionRequest request = new DefaultMavenExecutionRequest(localRepository, settings, new DefaultEventDispatcher(), Collections.EMPTY_LIST, pom.getParentFile().getAbsolutePath(), globalProfileManager, globalProfileManager.getRequestProperties(), new Properties(), false);
      return this.getProject(pom, request);
   }

   public MavenProject getProject(File pom, MavenExecutionRequest request) throws ProjectBuildingException, ArtifactResolutionException, ProfileActivationException {
      if (pom.exists() && pom.length() == 0L) {
         throw new ProjectBuildingException("unknown", "The file " + pom.getAbsolutePath() + " you specified has zero length.");
      } else {
         ProjectBuilderConfiguration config = new DefaultProjectBuilderConfiguration();
         config.setLocalRepository(request.getLocalRepository()).setGlobalProfileManager(request.getGlobalProfileManager()).setUserProperties(request.getUserProperties());
         return this.projectBuilder.build(pom, config);
      }
   }

   protected MavenSession createSession(MavenExecutionRequest request, ReactorManager rpm) {
      return new MavenSession(this.container, request.getSettings(), request.getLocalRepository(), request.getEventDispatcher(), rpm, request.getGoals(), request.getBaseDirectory(), request.getExecutionProperties(), request.getStartTime());
   }

   private void resolveParameters(Settings settings) throws ComponentLookupException, ComponentLifecycleException, SettingsConfigurationException {
      WagonManager wagonManager = (WagonManager)this.container.lookup(WagonManager.ROLE);

      try {
         Proxy proxy = settings.getActiveProxy();
         if (proxy != null) {
            if (proxy.getHost() == null) {
               throw new SettingsConfigurationException("Proxy in settings.xml has no host");
            }

            wagonManager.addProxy(proxy.getProtocol(), proxy.getHost(), proxy.getPort(), proxy.getUsername(), proxy.getPassword(), proxy.getNonProxyHosts());
         }

         Iterator i = settings.getServers().iterator();

         while(i.hasNext()) {
            Server server = (Server)i.next();
            wagonManager.addAuthenticationInfo(server.getId(), server.getUsername(), server.getPassword(), server.getPrivateKey(), server.getPassphrase());
            wagonManager.addPermissionInfo(server.getId(), server.getFilePermissions(), server.getDirectoryPermissions());
            if (server.getConfiguration() != null) {
               wagonManager.addConfiguration(server.getId(), (Xpp3Dom)server.getConfiguration());
            }
         }

         RepositoryPermissions defaultPermissions = new RepositoryPermissions();
         defaultPermissions.setDirectoryMode("775");
         defaultPermissions.setFileMode("664");
         wagonManager.setDefaultRepositoryPermissions(defaultPermissions);
         Iterator i = settings.getMirrors().iterator();

         while(i.hasNext()) {
            Mirror mirror = (Mirror)i.next();
            wagonManager.addMirror(mirror.getId(), mirror.getMirrorOf(), mirror.getUrl());
         }
      } finally {
         this.container.release(wagonManager);
      }

   }

   public void contextualize(Context context) throws ContextException {
      this.container = (PlexusContainer)context.get("plexus");
   }

   protected void logFatal(Throwable error) {
      this.line();
      this.getLogger().error("FATAL ERROR");
      this.line();
      this.logDiagnostics(error);
      this.logTrace(error, true);
   }

   protected void logError(Exception e, boolean showErrors) {
      this.line();
      this.getLogger().error("BUILD ERROR");
      this.line();
      this.logDiagnostics(e);
      this.logTrace(e, showErrors);
      if (!showErrors) {
         this.getLogger().info("For more information, run Maven with the -e switch");
         this.line();
      }

   }

   protected void logFailure(BuildFailureException e, boolean showErrors) {
      this.line();
      this.getLogger().error("BUILD FAILURE");
      this.line();
      this.logDiagnostics(e);
      this.logTrace(e, showErrors);
      if (!showErrors) {
         this.getLogger().info("For more information, run Maven with the -e switch");
         this.line();
      }

   }

   private void logTrace(Throwable t, boolean showErrors) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Trace", t);
         this.line();
      } else if (showErrors) {
         this.getLogger().info("Trace", t);
         this.line();
      }

   }

   private void logDiagnostics(Throwable t) {
      String message = null;
      if (this.errorDiagnostics != null) {
         message = this.errorDiagnostics.diagnose(t);
      }

      if (message == null) {
         message = t.getMessage();
      }

      this.getLogger().info(message);
      this.line();
   }

   protected void logSuccess(ReactorManager rm) {
      this.line();
      this.getLogger().info("BUILD SUCCESSFUL");
      this.line();
   }

   private void logReactorSummary(ReactorManager rm) {
      if (rm.hasMultipleProjects() && rm.executedMultipleProjects()) {
         this.getLogger().info("");
         this.getLogger().info("");
         this.line();
         this.getLogger().info("Reactor Summary:");
         this.line();
         Iterator it = rm.getSortedProjects().iterator();

         while(it.hasNext()) {
            MavenProject project = (MavenProject)it.next();
            if (rm.hasBuildFailure(project)) {
               this.logReactorSummaryLine(project.getName(), "FAILED", rm.getBuildFailure(project).getTime());
            } else if (rm.isBlackListed(project)) {
               this.logReactorSummaryLine(project.getName(), "SKIPPED (dependency build failed or was skipped)");
            } else if (rm.hasBuildSuccess(project)) {
               this.logReactorSummaryLine(project.getName(), "SUCCESS", rm.getBuildSuccess(project).getTime());
            } else {
               this.logReactorSummaryLine(project.getName(), "NOT BUILT");
            }
         }

         this.line();
      }

   }

   protected void stats(Date start) {
      Date finish = new Date();
      long time = finish.getTime() - start.getTime();
      this.getLogger().info("Total time: " + formatTime(time));
      this.getLogger().info("Finished at: " + finish);
      System.gc();
      Runtime r = Runtime.getRuntime();
      this.getLogger().info("Final Memory: " + (r.totalMemory() - r.freeMemory()) / 1048576L + "M/" + r.totalMemory() / 1048576L + "M");
   }

   protected void line() {
      this.getLogger().info("------------------------------------------------------------------------");
   }

   protected static String formatTime(long ms) {
      long secs = ms / 1000L;
      long min = secs / 60L;
      secs %= 60L;
      String msg = "";
      if (min > 1L) {
         msg = min + " minutes ";
      } else if (min == 1L) {
         msg = "1 minute ";
      }

      if (secs > 1L) {
         msg = msg + secs + " seconds";
      } else if (secs == 1L) {
         msg = msg + "1 second";
      } else if (min == 0L) {
         msg = msg + "< 1 second";
      }

      return msg;
   }

   private List getProjectFiles(MavenExecutionRequest request) throws IOException {
      List files = Collections.EMPTY_LIST;
      File userDir = new File(System.getProperty("user.dir"));
      if (request.isReactorActive()) {
         String includes = System.getProperty("maven.reactor.includes", "**/pom.xml,**/release-pom.xml");
         String excludes = System.getProperty("maven.reactor.excludes", "pom.xml,release-pom.xml");
         files = FileUtils.getFiles(userDir, includes, excludes);
         this.filterOneProjectFilePerDirectory(files);
         Collections.sort(files);
      } else {
         File projectFile;
         if (request.getPomFile() != null) {
            projectFile = (new File(request.getPomFile())).getAbsoluteFile();
            if (projectFile.exists()) {
               files = Collections.singletonList(projectFile);
            }
         } else {
            projectFile = new File(userDir, "release-pom.xml");
            if (!projectFile.exists()) {
               projectFile = new File(userDir, "pom.xml");
            }

            if (projectFile.exists()) {
               files = Collections.singletonList(projectFile);
            }
         }
      }

      return files;
   }

   private void filterOneProjectFilePerDirectory(List files) {
      List releaseDirs = new ArrayList();
      Iterator it = files.iterator();

      File projectFile;
      while(it.hasNext()) {
         projectFile = (File)it.next();
         if ("release-pom.xml".equals(projectFile.getName())) {
            releaseDirs.add(projectFile.getParentFile());
         }
      }

      it = files.iterator();

      while(it.hasNext()) {
         projectFile = (File)it.next();
         if (!"release-pom.xml".equals(projectFile.getName()) && releaseDirs.contains(projectFile.getParentFile())) {
            it.remove();
         }
      }

   }
}

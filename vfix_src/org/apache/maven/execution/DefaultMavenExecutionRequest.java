package org.apache.maven.execution;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.monitor.event.EventDispatcher;
import org.apache.maven.monitor.event.EventMonitor;
import org.apache.maven.profiles.ProfileManager;
import org.apache.maven.settings.Settings;

public class DefaultMavenExecutionRequest implements MavenExecutionRequest {
   private final ArtifactRepository localRepository;
   private final List goals;
   protected MavenSession session;
   private final EventDispatcher eventDispatcher;
   private final Settings settings;
   private final String baseDirectory;
   private boolean recursive = true;
   private boolean reactorActive;
   private String pomFilename;
   private String failureBehavior;
   private final ProfileManager globalProfileManager;
   private final Properties executionProperties;
   private final Properties userProperties;
   private final Date startTime;
   private final boolean showErrors;

   public DefaultMavenExecutionRequest(ArtifactRepository localRepository, Settings settings, EventDispatcher eventDispatcher, List goals, String baseDirectory, ProfileManager globalProfileManager, Properties executionProperties, Properties userProperties, boolean showErrors) {
      this.localRepository = localRepository;
      this.settings = settings;
      this.goals = goals;
      this.eventDispatcher = eventDispatcher;
      this.baseDirectory = baseDirectory;
      this.globalProfileManager = globalProfileManager;
      this.executionProperties = executionProperties;
      this.userProperties = userProperties;
      this.startTime = new Date();
      this.showErrors = showErrors;
   }

   public Settings getSettings() {
      return this.settings;
   }

   public String getBaseDirectory() {
      return this.baseDirectory;
   }

   public boolean isRecursive() {
      return this.recursive;
   }

   public void setRecursive(boolean recursive) {
      this.recursive = false;
   }

   public ArtifactRepository getLocalRepository() {
      return this.localRepository;
   }

   public List getGoals() {
      return this.goals;
   }

   public Properties getExecutionProperties() {
      return this.executionProperties;
   }

   public MavenSession getSession() {
      return this.session;
   }

   public void setSession(MavenSession session) {
      this.session = session;
   }

   public void addEventMonitor(EventMonitor monitor) {
      this.eventDispatcher.addEventMonitor(monitor);
   }

   public EventDispatcher getEventDispatcher() {
      return this.eventDispatcher;
   }

   public void setReactorActive(boolean reactorActive) {
      this.reactorActive = reactorActive;
   }

   public boolean isReactorActive() {
      return this.reactorActive;
   }

   public void setPomFile(String pomFilename) {
      this.pomFilename = pomFilename;
   }

   public String getPomFile() {
      return this.pomFilename;
   }

   public void setFailureBehavior(String failureBehavior) {
      this.failureBehavior = failureBehavior;
   }

   public String getFailureBehavior() {
      return this.failureBehavior;
   }

   public ProfileManager getGlobalProfileManager() {
      return this.globalProfileManager;
   }

   public Date getStartTime() {
      return this.startTime;
   }

   public boolean isShowErrors() {
      return this.showErrors;
   }

   public Properties getUserProperties() {
      return this.userProperties;
   }
}

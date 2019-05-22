package org.apache.maven.execution;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.monitor.event.EventDispatcher;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

public class MavenSession {
   private PlexusContainer container;
   private ArtifactRepository localRepository;
   private List goals;
   private EventDispatcher eventDispatcher;
   private final Settings settings;
   private ReactorManager reactorManager;
   private final String executionRootDir;
   private boolean usingPOMsFromFilesystem = true;
   private final Properties executionProperties;
   private final Date startTime;
   private MavenProject currentProject;

   public MavenSession(PlexusContainer container, Settings settings, ArtifactRepository localRepository, EventDispatcher eventDispatcher, ReactorManager reactorManager, List goals, String executionRootDir, Properties executionProperties, Date startTime) {
      this.container = container;
      this.settings = settings;
      this.localRepository = localRepository;
      this.eventDispatcher = eventDispatcher;
      this.reactorManager = reactorManager;
      this.goals = goals;
      this.executionRootDir = executionRootDir;
      this.executionProperties = executionProperties;
      this.startTime = startTime;
   }

   public Map getPluginContext(PluginDescriptor pluginDescriptor, MavenProject project) {
      return this.reactorManager.getPluginContext(pluginDescriptor, project);
   }

   public PlexusContainer getContainer() {
      return this.container;
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

   public Object lookup(String role) throws ComponentLookupException {
      return this.container.lookup(role);
   }

   public Object lookup(String role, String roleHint) throws ComponentLookupException {
      return this.container.lookup(role, roleHint);
   }

   public List lookupList(String role) throws ComponentLookupException {
      return this.container.lookupList(role);
   }

   public Map lookupMap(String role) throws ComponentLookupException {
      return this.container.lookupMap(role);
   }

   public EventDispatcher getEventDispatcher() {
      return this.eventDispatcher;
   }

   public Settings getSettings() {
      return this.settings;
   }

   public List getSortedProjects() {
      return this.reactorManager.getSortedProjects();
   }

   public String getExecutionRootDirectory() {
      return this.executionRootDir;
   }

   public void setUsingPOMsFromFilesystem(boolean usingPOMsFromFilesystem) {
      this.usingPOMsFromFilesystem = usingPOMsFromFilesystem;
   }

   public boolean isUsingPOMsFromFilesystem() {
      return this.usingPOMsFromFilesystem;
   }

   public Date getStartTime() {
      return this.startTime;
   }

   public void setCurrentProject(MavenProject currentProject) {
      this.currentProject = currentProject;
   }

   public MavenProject getCurrentProject() {
      return this.currentProject;
   }
}

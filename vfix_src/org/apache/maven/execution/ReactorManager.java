package org.apache.maven.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.DuplicateProjectException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectSorter;
import org.codehaus.plexus.util.dag.CycleDetectedException;

public class ReactorManager {
   public static final String FAIL_FAST = "fail-fast";
   public static final String FAIL_AT_END = "fail-at-end";
   public static final String FAIL_NEVER = "fail-never";
   private List blackList = new ArrayList();
   private Map buildFailuresByProject = new HashMap();
   private Map pluginContextsByProjectAndPluginKey = new HashMap();
   private String failureBehavior = "fail-fast";
   private final ProjectSorter sorter;
   private Map buildSuccessesByProject = new HashMap();

   public ReactorManager(List projects) throws CycleDetectedException, DuplicateProjectException {
      this.sorter = new ProjectSorter(projects);
   }

   public Map getPluginContext(PluginDescriptor plugin, MavenProject project) {
      Map pluginContextsByKey = (Map)this.pluginContextsByProjectAndPluginKey.get(project.getId());
      if (pluginContextsByKey == null) {
         pluginContextsByKey = new HashMap();
         this.pluginContextsByProjectAndPluginKey.put(project.getId(), pluginContextsByKey);
      }

      Map pluginContext = (Map)((Map)pluginContextsByKey).get(plugin.getPluginLookupKey());
      if (pluginContext == null) {
         pluginContext = new HashMap();
         ((Map)pluginContextsByKey).put(plugin.getPluginLookupKey(), pluginContext);
      }

      return (Map)pluginContext;
   }

   public void setFailureBehavior(String failureBehavior) {
      if (!"fail-fast".equals(failureBehavior) && !"fail-at-end".equals(failureBehavior) && !"fail-never".equals(failureBehavior)) {
         throw new IllegalArgumentException("Invalid failure behavior (must be one of: 'fail-fast', 'fail-at-end', 'fail-never').");
      } else {
         this.failureBehavior = failureBehavior;
      }
   }

   public String getFailureBehavior() {
      return this.failureBehavior;
   }

   public void blackList(MavenProject project) {
      this.blackList(getProjectKey(project));
   }

   private void blackList(String id) {
      if (!this.blackList.contains(id)) {
         this.blackList.add(id);
         List dependents = this.sorter.getDependents(id);
         if (dependents != null && !dependents.isEmpty()) {
            Iterator it = dependents.iterator();

            while(it.hasNext()) {
               String dependentId = (String)it.next();
               if (!this.buildSuccessesByProject.containsKey(dependentId) && !this.buildFailuresByProject.containsKey(dependentId)) {
                  this.blackList(dependentId);
               }
            }
         }
      }

   }

   public boolean isBlackListed(MavenProject project) {
      return this.blackList.contains(getProjectKey(project));
   }

   private static String getProjectKey(MavenProject project) {
      return ArtifactUtils.versionlessKey(project.getGroupId(), project.getArtifactId());
   }

   public void registerBuildFailure(MavenProject project, Exception error, String task, long time) {
      this.buildFailuresByProject.put(getProjectKey(project), new BuildFailure(error, task, time));
   }

   public boolean hasBuildFailures() {
      return !this.buildFailuresByProject.isEmpty();
   }

   public boolean hasBuildFailure(MavenProject project) {
      return this.buildFailuresByProject.containsKey(getProjectKey(project));
   }

   public boolean hasMultipleProjects() {
      return this.sorter.hasMultipleProjects();
   }

   public List getSortedProjects() {
      return this.sorter.getSortedProjects();
   }

   public MavenProject getTopLevelProject() {
      return this.sorter.getTopLevelProject();
   }

   public boolean hasBuildSuccess(MavenProject project) {
      return this.buildSuccessesByProject.containsKey(getProjectKey(project));
   }

   public void registerBuildSuccess(MavenProject project, long time) {
      this.buildSuccessesByProject.put(getProjectKey(project), new BuildSuccess(project, time));
   }

   public BuildFailure getBuildFailure(MavenProject project) {
      return (BuildFailure)this.buildFailuresByProject.get(getProjectKey(project));
   }

   public BuildSuccess getBuildSuccess(MavenProject project) {
      return (BuildSuccess)this.buildSuccessesByProject.get(getProjectKey(project));
   }

   public boolean executedMultipleProjects() {
      return this.buildFailuresByProject.size() + this.buildSuccessesByProject.size() > 1;
   }
}

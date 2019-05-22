package org.apache.maven.project;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.ReportPlugin;
import org.codehaus.plexus.util.dag.CycleDetectedException;
import org.codehaus.plexus.util.dag.DAG;
import org.codehaus.plexus.util.dag.TopologicalSorter;
import org.codehaus.plexus.util.dag.Vertex;

public class ProjectSorter {
   private final DAG dag;
   private final Map projectMap;
   private final List<MavenProject> sortedProjects;
   private MavenProject topLevelProject;

   public ProjectSorter(List projects) throws CycleDetectedException, DuplicateProjectException, MissingProjectException {
      this(projects, (List)null, (String)null, false, false);
   }

   public ProjectSorter(List projects, List selectedProjectNames, String resumeFrom, boolean make, boolean makeDependents) throws CycleDetectedException, DuplicateProjectException, MissingProjectException {
      this.dag = new DAG();
      this.projectMap = new HashMap();
      Iterator i = projects.iterator();

      MavenProject project;
      String id;
      while(i.hasNext()) {
         project = (MavenProject)i.next();
         id = ArtifactUtils.versionlessKey(project.getGroupId(), project.getArtifactId());
         if (this.dag.getVertex(id) != null) {
            throw new DuplicateProjectException("Project '" + id + "' is duplicated in the reactor");
         }

         this.dag.addVertex(id);
         this.projectMap.put(id, project);
      }

      i = projects.iterator();

      while(i.hasNext()) {
         project = (MavenProject)i.next();
         id = ArtifactUtils.versionlessKey(project.getGroupId(), project.getArtifactId());
         Iterator j = project.getDependencies().iterator();

         while(j.hasNext()) {
            Dependency dependency = (Dependency)j.next();
            String dependencyId = ArtifactUtils.versionlessKey(dependency.getGroupId(), dependency.getArtifactId());
            if (this.dag.getVertex(dependencyId) != null) {
               project.addProjectReference((MavenProject)this.projectMap.get(dependencyId));
               this.dag.addEdge(id, dependencyId);
            }
         }

         MavenProject parent = project.getParent();
         if (parent != null) {
            String parentId = ArtifactUtils.versionlessKey(parent.getGroupId(), parent.getArtifactId());
            if (this.dag.getVertex(parentId) != null) {
               if (this.dag.hasEdge(parentId, id)) {
                  this.dag.removeEdge(parentId, id);
               }

               this.dag.addEdge(id, parentId);
            }
         }

         List buildPlugins = project.getBuildPlugins();
         if (buildPlugins != null) {
            Iterator j = buildPlugins.iterator();

            label111:
            while(true) {
               Plugin plugin;
               String pluginId;
               do {
                  if (!j.hasNext()) {
                     break label111;
                  }

                  plugin = (Plugin)j.next();
                  pluginId = ArtifactUtils.versionlessKey(plugin.getGroupId(), plugin.getArtifactId());
                  if (this.dag.getVertex(pluginId) != null && !pluginId.equals(id)) {
                     this.addEdgeWithParentCheck(this.projectMap, pluginId, project, id);
                  }
               } while(pluginId.equals(id));

               Iterator k = plugin.getDependencies().iterator();

               while(k.hasNext()) {
                  Dependency dependency = (Dependency)k.next();
                  String dependencyId = ArtifactUtils.versionlessKey(dependency.getGroupId(), dependency.getArtifactId());
                  if (this.dag.getVertex(dependencyId) != null && !id.equals(dependencyId)) {
                     project.addProjectReference((MavenProject)this.projectMap.get(dependencyId));
                     this.addEdgeWithParentCheck(this.projectMap, dependencyId, project, id);
                  }
               }
            }
         }

         List reportPlugins = project.getReportPlugins();
         Iterator j;
         String extensionId;
         if (reportPlugins != null) {
            j = reportPlugins.iterator();

            while(j.hasNext()) {
               ReportPlugin plugin = (ReportPlugin)j.next();
               extensionId = ArtifactUtils.versionlessKey(plugin.getGroupId(), plugin.getArtifactId());
               if (this.dag.getVertex(extensionId) != null && !extensionId.equals(id)) {
                  this.addEdgeWithParentCheck(this.projectMap, extensionId, project, id);
               }
            }
         }

         j = project.getBuildExtensions().iterator();

         while(j.hasNext()) {
            Extension extension = (Extension)j.next();
            extensionId = ArtifactUtils.versionlessKey(extension.getGroupId(), extension.getArtifactId());
            if (this.dag.getVertex(extensionId) != null) {
               this.addEdgeWithParentCheck(this.projectMap, extensionId, project, id);
            }
         }
      }

      List sortedProjects = new ArrayList();
      Iterator i = TopologicalSorter.sort(this.dag).iterator();

      while(i.hasNext()) {
         id = (String)i.next();
         sortedProjects.add(this.projectMap.get(id));
      }

      i = sortedProjects.iterator();

      while(i.hasNext() && this.topLevelProject == null) {
         MavenProject project = (MavenProject)i.next();
         if (project.isExecutionRoot()) {
            this.topLevelProject = project;
         }
      }

      List sortedProjects = applyMakeFilter(sortedProjects, this.dag, this.projectMap, this.topLevelProject, selectedProjectNames, make, makeDependents);
      resumeFrom(resumeFrom, sortedProjects, this.projectMap, this.topLevelProject);
      this.sortedProjects = Collections.unmodifiableList(sortedProjects);
   }

   private static List applyMakeFilter(List sortedProjects, DAG dag, Map projectMap, MavenProject topLevelProject, List selectedProjectNames, boolean make, boolean makeDependents) throws MissingProjectException {
      if (selectedProjectNames == null) {
         return sortedProjects;
      } else {
         MavenProject[] selectedProjects = new MavenProject[selectedProjectNames.size()];

         for(int i = 0; i < selectedProjects.length; ++i) {
            selectedProjects[i] = findProject((String)selectedProjectNames.get(i), projectMap, topLevelProject);
         }

         Set projectsToMake = new HashSet(Arrays.asList(selectedProjects));

         MavenProject project;
         for(int i = 0; i < selectedProjects.length; ++i) {
            project = selectedProjects[i];
            String id = ArtifactUtils.versionlessKey(project.getGroupId(), project.getArtifactId());
            Vertex v = dag.getVertex(id);
            if (make) {
               gatherDescendents(v, projectMap, projectsToMake, new HashSet());
            }

            if (makeDependents) {
               gatherAncestors(v, projectMap, projectsToMake, new HashSet());
            }
         }

         Iterator i = sortedProjects.iterator();

         while(i.hasNext()) {
            project = (MavenProject)i.next();
            if (!projectsToMake.contains(project)) {
               i.remove();
            }
         }

         return sortedProjects;
      }
   }

   private static void resumeFrom(String resumeFrom, List sortedProjects, Map projectMap, MavenProject topLevelProject) throws MissingProjectException {
      if (resumeFrom != null) {
         MavenProject resumeFromProject = findProject(resumeFrom, projectMap, topLevelProject);
         Iterator i = sortedProjects.iterator();

         while(i.hasNext()) {
            MavenProject project = (MavenProject)i.next();
            if (resumeFromProject.equals(project)) {
               break;
            }

            i.remove();
         }

         if (sortedProjects.isEmpty()) {
            throw new MissingProjectException("Couldn't resume, project was not scheduled to run: " + resumeFrom);
         }
      }
   }

   private static MavenProject findProject(String projectName, Map projectMap, MavenProject topLevelProject) throws MissingProjectException {
      MavenProject project = (MavenProject)projectMap.get(projectName);
      if (project != null) {
         return project;
      } else {
         File baseDir;
         if (topLevelProject == null) {
            baseDir = new File(System.getProperty("user.dir"));
         } else {
            baseDir = topLevelProject.getBasedir();
         }

         File projectDir = new File(baseDir, projectName);
         if (!projectDir.exists()) {
            throw new MissingProjectException("Couldn't find specified project dir: " + projectDir.getAbsolutePath());
         } else if (!projectDir.isDirectory()) {
            throw new MissingProjectException("Couldn't find specified project dir (not a directory): " + projectDir.getAbsolutePath());
         } else {
            Iterator i = projectMap.values().iterator();

            do {
               if (!i.hasNext()) {
                  throw new MissingProjectException("Couldn't find specified project in module list: " + projectDir.getAbsolutePath());
               }

               project = (MavenProject)i.next();
            } while(!projectDir.equals(project.getFile().getParentFile()));

            return project;
         }
      }
   }

   private static void gatherDescendents(Vertex v, Map projectMap, Set out, Set visited) {
      if (!visited.contains(v)) {
         visited.add(v);
         out.add(projectMap.get(v.getLabel()));
         Iterator i = v.getChildren().iterator();

         while(i.hasNext()) {
            Vertex child = (Vertex)i.next();
            gatherDescendents(child, projectMap, out, visited);
         }

      }
   }

   private static void gatherAncestors(Vertex v, Map projectMap, Set out, Set visited) {
      if (!visited.contains(v)) {
         visited.add(v);
         out.add(projectMap.get(v.getLabel()));
         Iterator i = v.getParents().iterator();

         while(i.hasNext()) {
            Vertex parent = (Vertex)i.next();
            gatherAncestors(parent, projectMap, out, visited);
         }

      }
   }

   private void addEdgeWithParentCheck(Map projectMap, String projectRefId, MavenProject project, String id) throws CycleDetectedException {
      MavenProject extProject = (MavenProject)projectMap.get(projectRefId);
      if (extProject != null) {
         project.addProjectReference(extProject);
         MavenProject extParent = extProject.getParent();
         if (extParent != null) {
            String parentId = ArtifactUtils.versionlessKey(extParent.getGroupId(), extParent.getArtifactId());
            if (!this.dag.hasEdge(projectRefId, id) || !parentId.equals(id)) {
               this.dag.addEdge(id, projectRefId);
            }
         }

      }
   }

   public MavenProject getTopLevelProject() {
      return this.topLevelProject;
   }

   public List<MavenProject> getSortedProjects() {
      return this.sortedProjects;
   }

   public boolean hasMultipleProjects() {
      return this.sortedProjects.size() > 1;
   }

   public List getDependents(String id) {
      return this.dag.getParentLabels(id);
   }

   public DAG getDAG() {
      return this.dag;
   }

   public Map getProjectMap() {
      return this.projectMap;
   }
}

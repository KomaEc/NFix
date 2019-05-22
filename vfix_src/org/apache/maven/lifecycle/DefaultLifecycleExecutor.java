package org.apache.maven.lifecycle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import org.apache.maven.BuildFailureException;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ReactorManager;
import org.apache.maven.extension.ExtensionManager;
import org.apache.maven.lifecycle.mapping.LifecycleMapping;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.ReportSet;
import org.apache.maven.monitor.event.EventDispatcher;
import org.apache.maven.plugin.InvalidPluginException;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginConfigurationException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.plugin.PluginManagerException;
import org.apache.maven.plugin.PluginNotFoundException;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.lifecycle.Execution;
import org.apache.maven.plugin.lifecycle.Phase;
import org.apache.maven.plugin.version.PluginVersionNotFoundException;
import org.apache.maven.plugin.version.PluginVersionResolutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;
import org.apache.maven.reporting.MavenReport;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class DefaultLifecycleExecutor extends AbstractLogEnabled implements LifecycleExecutor {
   private PluginManager pluginManager;
   private ExtensionManager extensionManager;
   private List lifecycles;
   private ArtifactHandlerManager artifactHandlerManager;
   private List defaultReports;
   private Map phaseToLifecycleMap;

   public void execute(MavenSession session, ReactorManager rm, EventDispatcher dispatcher) throws BuildFailureException, LifecycleExecutionException {
      MavenProject rootProject = rm.getTopLevelProject();
      List goals = session.getGoals();
      if (goals.isEmpty() && rootProject != null) {
         String goal = rootProject.getDefaultGoal();
         if (goal != null) {
            goals = Collections.singletonList(goal);
         }
      }

      if (goals.isEmpty()) {
         throw new BuildFailureException("\n\nYou must specify at least one goal. Try 'mvn install' to build or 'mvn -?' for options \nSee http://maven.apache.org for more information.\n\n");
      } else {
         List taskSegments = this.segmentTaskListByAggregationNeeds(goals, session, rootProject);
         this.findExtensions(session);
         this.executeTaskSegments(taskSegments, rm, session, rootProject, dispatcher);
      }
   }

   private void findExtensions(MavenSession session) throws LifecycleExecutionException {
      Iterator i = session.getSortedProjects().iterator();

      while(i.hasNext()) {
         MavenProject project = (MavenProject)i.next();
         Iterator j = project.getBuildExtensions().iterator();

         while(j.hasNext()) {
            Extension extension = (Extension)j.next();

            try {
               this.extensionManager.addExtension(extension, project, session.getLocalRepository());
            } catch (PlexusContainerException var7) {
               throw new LifecycleExecutionException("Unable to initialise extensions", var7);
            } catch (ArtifactResolutionException var8) {
               throw new LifecycleExecutionException(var8.getMessage(), var8);
            } catch (ArtifactNotFoundException var9) {
               throw new LifecycleExecutionException(var9.getMessage(), var9);
            }
         }

         this.extensionManager.registerWagons();

         try {
            Map handlers = this.findArtifactTypeHandlers(project, session.getSettings(), session.getLocalRepository());
            this.artifactHandlerManager.addHandlers(handlers);
         } catch (PluginNotFoundException var10) {
            throw new LifecycleExecutionException(var10.getMessage(), var10);
         }
      }

   }

   private void executeTaskSegments(List taskSegments, ReactorManager rm, MavenSession session, MavenProject rootProject, EventDispatcher dispatcher) throws LifecycleExecutionException, BuildFailureException {
      Iterator it = taskSegments.iterator();

      while(true) {
         label187:
         while(it.hasNext()) {
            DefaultLifecycleExecutor.TaskSegment segment = (DefaultLifecycleExecutor.TaskSegment)it.next();
            String event;
            if (segment.aggregate()) {
               if (!rm.isBlackListed(rootProject)) {
                  this.line();
                  this.getLogger().info("Building " + rootProject.getName());
                  this.getLogger().info("  " + segment);
                  this.line();
                  String event = "project-execute";
                  long buildStartTime = System.currentTimeMillis();
                  event = rootProject.getId() + " ( " + segment + " )";
                  dispatcher.dispatchStart(event, event);

                  try {
                     session.setCurrentProject(rootProject);
                     Iterator goalIterator = segment.getTasks().iterator();

                     while(true) {
                        if (!goalIterator.hasNext()) {
                           rm.registerBuildSuccess(rootProject, System.currentTimeMillis() - buildStartTime);
                           break;
                        }

                        String task = (String)goalIterator.next();
                        this.executeGoalAndHandleFailures(task, session, rootProject, dispatcher, event, rm, buildStartTime, event);
                     }
                  } finally {
                     session.setCurrentProject((MavenProject)null);
                  }

                  dispatcher.dispatchEnd(event, event);
               } else {
                  this.line();
                  this.getLogger().info("SKIPPING " + rootProject.getName());
                  this.getLogger().info("  " + segment);
                  this.getLogger().info("This project has been banned from further executions due to previous failures.");
                  this.line();
               }
            } else {
               List sortedProjects = session.getSortedProjects();
               Iterator projectIterator = sortedProjects.iterator();

               while(true) {
                  while(true) {
                     if (!projectIterator.hasNext()) {
                        continue label187;
                     }

                     MavenProject currentProject = (MavenProject)projectIterator.next();
                     if (!rm.isBlackListed(currentProject)) {
                        this.line();
                        this.getLogger().info("Building " + currentProject.getName());
                        this.getLogger().info("  " + segment);
                        this.line();
                        event = "project-execute";
                        long buildStartTime = System.currentTimeMillis();
                        String target = currentProject.getId() + " ( " + segment + " )";
                        dispatcher.dispatchStart(event, target);

                        try {
                           session.setCurrentProject(currentProject);
                           Iterator goalIterator = segment.getTasks().iterator();

                           while(goalIterator.hasNext()) {
                              String task = (String)goalIterator.next();
                              this.executeGoalAndHandleFailures(task, session, currentProject, dispatcher, event, rm, buildStartTime, target);
                           }
                        } finally {
                           session.setCurrentProject((MavenProject)null);
                        }

                        rm.registerBuildSuccess(currentProject, System.currentTimeMillis() - buildStartTime);
                        dispatcher.dispatchEnd(event, target);
                     } else {
                        this.line();
                        this.getLogger().info("SKIPPING " + currentProject.getName());
                        this.getLogger().info("  " + segment);
                        this.getLogger().info("This project has been banned from further executions due to previous failures.");
                        this.line();
                     }
                  }
               }
            }
         }

         return;
      }
   }

   private void executeGoalAndHandleFailures(String task, MavenSession session, MavenProject project, EventDispatcher dispatcher, String event, ReactorManager rm, long buildStartTime, String target) throws BuildFailureException, LifecycleExecutionException {
      try {
         this.executeGoal(task, session, project);
      } catch (LifecycleExecutionException var11) {
         dispatcher.dispatchError(event, target, var11);
         if (this.handleExecutionFailure(rm, project, var11, task, buildStartTime)) {
            throw var11;
         }
      } catch (BuildFailureException var12) {
         dispatcher.dispatchError(event, target, var12);
         if (this.handleExecutionFailure(rm, project, var12, task, buildStartTime)) {
            throw var12;
         }
      }

   }

   private boolean handleExecutionFailure(ReactorManager rm, MavenProject project, Exception e, String task, long buildStartTime) {
      rm.registerBuildFailure(project, e, task, System.currentTimeMillis() - buildStartTime);
      if ("fail-fast".equals(rm.getFailureBehavior())) {
         return true;
      } else {
         if ("fail-at-end".equals(rm.getFailureBehavior())) {
            rm.blackList(project);
         }

         return false;
      }
   }

   private List segmentTaskListByAggregationNeeds(List tasks, MavenSession session, MavenProject project) throws LifecycleExecutionException, BuildFailureException {
      List segments = new ArrayList();
      DefaultLifecycleExecutor.TaskSegment currentSegment;
      Iterator it;
      if (project != null) {
         currentSegment = null;
         it = tasks.iterator();

         while(true) {
            while(it.hasNext()) {
               String task = (String)it.next();
               if (this.getPhaseToLifecycleMap().containsKey(task)) {
                  if (currentSegment != null && currentSegment.aggregate()) {
                     segments.add(currentSegment);
                     currentSegment = null;
                  }

                  if (currentSegment == null) {
                     currentSegment = new DefaultLifecycleExecutor.TaskSegment();
                  }

                  currentSegment.add(task);
               } else {
                  MojoDescriptor mojo = null;

                  try {
                     mojo = this.getMojoDescriptor(task, session, project, task, true, false);
                  } catch (PluginNotFoundException var10) {
                     this.getLogger().info("Cannot find mojo descriptor for: '" + task + "' - Treating as non-aggregator.");
                     this.getLogger().debug("", var10);
                  }

                  if (mojo == null || !mojo.isAggregator() && mojo.isProjectRequired()) {
                     if (currentSegment != null && currentSegment.aggregate()) {
                        segments.add(currentSegment);
                        currentSegment = null;
                     }

                     if (currentSegment == null) {
                        currentSegment = new DefaultLifecycleExecutor.TaskSegment();
                     }

                     currentSegment.add(task);
                  } else {
                     if (currentSegment != null && !currentSegment.aggregate()) {
                        segments.add(currentSegment);
                        currentSegment = null;
                     }

                     if (currentSegment == null) {
                        currentSegment = new DefaultLifecycleExecutor.TaskSegment(true);
                     }

                     currentSegment.add(task);
                  }
               }
            }

            segments.add(currentSegment);
            break;
         }
      } else {
         currentSegment = new DefaultLifecycleExecutor.TaskSegment(false);
         it = tasks.iterator();

         while(it.hasNext()) {
            currentSegment.add((String)it.next());
         }

         segments.add(currentSegment);
      }

      return segments;
   }

   private void executeGoal(String task, MavenSession session, MavenProject project) throws LifecycleExecutionException, BuildFailureException {
      try {
         Stack forkEntryPoints = new Stack();
         if (this.getPhaseToLifecycleMap().containsKey(task)) {
            Lifecycle lifecycle = this.getLifecycleForPhase(task);
            Map lifecycleMappings = this.constructLifecycleMappings(session, task, project, lifecycle);
            this.executeGoalWithLifecycle(task, forkEntryPoints, session, lifecycleMappings, project, lifecycle);
         } else {
            this.executeStandaloneGoal(task, forkEntryPoints, session, project);
         }

      } catch (PluginNotFoundException var7) {
         throw new BuildFailureException("A required plugin was not found: " + var7.getMessage(), var7);
      }
   }

   private void executeGoalWithLifecycle(String task, Stack forkEntryPoints, MavenSession session, Map lifecycleMappings, MavenProject project, Lifecycle lifecycle) throws LifecycleExecutionException, BuildFailureException, PluginNotFoundException {
      List goals = this.processGoalChain(task, lifecycleMappings, lifecycle);
      if (!goals.isEmpty()) {
         this.executeGoals(goals, forkEntryPoints, session, project);
      } else {
         this.getLogger().info("No goals needed for project - skipping");
      }

   }

   private void executeStandaloneGoal(String task, Stack forkEntryPoints, MavenSession session, MavenProject project) throws LifecycleExecutionException, BuildFailureException, PluginNotFoundException {
      MojoDescriptor mojoDescriptor = this.getMojoDescriptor(task, session, project, task, true, false);
      this.executeGoals(Collections.singletonList(new MojoExecution(mojoDescriptor)), forkEntryPoints, session, project);
   }

   private void executeGoals(List goals, Stack forkEntryPoints, MavenSession session, MavenProject project) throws LifecycleExecutionException, BuildFailureException, PluginNotFoundException {
      Iterator i = goals.iterator();

      while(i.hasNext()) {
         MojoExecution mojoExecution = (MojoExecution)i.next();
         MojoDescriptor mojoDescriptor = mojoExecution.getMojoDescriptor();
         if (mojoDescriptor.getExecutePhase() != null || mojoDescriptor.getExecuteGoal() != null) {
            forkEntryPoints.push(mojoDescriptor);
            this.forkLifecycle(mojoDescriptor, forkEntryPoints, session, project);
            forkEntryPoints.pop();
         }

         if (mojoDescriptor.isRequiresReports()) {
            List reports = this.getReports(project, forkEntryPoints, mojoExecution, session);
            mojoExecution.setReports(reports);
            Iterator j = mojoExecution.getForkedExecutions().iterator();

            while(j.hasNext()) {
               MojoExecution forkedExecution = (MojoExecution)j.next();
               MojoDescriptor descriptor = forkedExecution.getMojoDescriptor();
               if (descriptor.getExecutePhase() != null) {
                  forkEntryPoints.push(descriptor);
                  this.forkLifecycle(descriptor, forkEntryPoints, session, project);
                  forkEntryPoints.pop();
               }
            }
         }

         try {
            this.pluginManager.executeMojo(project, mojoExecution, session);
         } catch (PluginManagerException var12) {
            throw new LifecycleExecutionException("Internal error in the plugin manager executing goal '" + mojoDescriptor.getId() + "': " + var12.getMessage(), var12);
         } catch (ArtifactNotFoundException var13) {
            throw new LifecycleExecutionException(var13.getMessage(), var13);
         } catch (InvalidDependencyVersionException var14) {
            throw new LifecycleExecutionException(var14.getMessage(), var14);
         } catch (ArtifactResolutionException var15) {
            throw new LifecycleExecutionException(var15.getMessage(), var15);
         } catch (MojoFailureException var16) {
            throw new BuildFailureException(var16.getMessage(), var16);
         } catch (MojoExecutionException var17) {
            throw new LifecycleExecutionException(var17.getMessage(), var17);
         } catch (PluginConfigurationException var18) {
            throw new LifecycleExecutionException(var18.getMessage(), var18);
         }
      }

   }

   private List getReports(MavenProject project, Stack forkEntryPoints, MojoExecution mojoExecution, MavenSession session) throws LifecycleExecutionException, PluginNotFoundException {
      List reportPlugins = project.getReportPlugins();
      if (project.getModel().getReports() != null) {
         this.getLogger().error("Plugin contains a <reports/> section: this is IGNORED - please use <reporting/> instead.");
      }

      if (project.getReporting() == null || !project.getReporting().isExcludeDefaults()) {
         if (reportPlugins == null) {
            reportPlugins = new ArrayList();
         } else {
            reportPlugins = new ArrayList((Collection)reportPlugins);
         }

         Iterator i = this.defaultReports.iterator();

         label76:
         while(true) {
            while(true) {
               if (!i.hasNext()) {
                  break label76;
               }

               String report = (String)i.next();
               StringTokenizer tok = new StringTokenizer(report, ":");
               if (tok.countTokens() != 2) {
                  this.getLogger().warn("Invalid default report ignored: '" + report + "' (must be groupId:artifactId)");
               } else {
                  String groupId = tok.nextToken();
                  String artifactId = tok.nextToken();
                  boolean found = false;
                  Iterator j = ((List)reportPlugins).iterator();

                  while(j.hasNext() && !found) {
                     ReportPlugin reportPlugin = (ReportPlugin)j.next();
                     if (reportPlugin.getGroupId().equals(groupId) && reportPlugin.getArtifactId().equals(artifactId)) {
                        found = true;
                     }
                  }

                  if (!found) {
                     ReportPlugin reportPlugin = new ReportPlugin();
                     reportPlugin.setGroupId(groupId);
                     reportPlugin.setArtifactId(artifactId);
                     ((List)reportPlugins).add(reportPlugin);
                  }
               }
            }
         }
      }

      List reports = new ArrayList();
      if (reportPlugins != null) {
         Iterator it = ((List)reportPlugins).iterator();

         while(true) {
            while(it.hasNext()) {
               ReportPlugin reportPlugin = (ReportPlugin)it.next();
               List reportSets = reportPlugin.getReportSets();
               if (reportSets != null && !reportSets.isEmpty()) {
                  Iterator j = reportSets.iterator();

                  while(j.hasNext()) {
                     ReportSet reportSet = (ReportSet)j.next();
                     reports.addAll(this.getReports(reportPlugin, forkEntryPoints, reportSet, project, session, mojoExecution));
                  }
               } else {
                  reports.addAll(this.getReports(reportPlugin, forkEntryPoints, (ReportSet)null, project, session, mojoExecution));
               }
            }

            return reports;
         }
      } else {
         return reports;
      }
   }

   private List getReports(ReportPlugin reportPlugin, Stack forkEntryPoints, ReportSet reportSet, MavenProject project, MavenSession session, MojoExecution mojoExecution) throws LifecycleExecutionException, PluginNotFoundException {
      PluginDescriptor pluginDescriptor = this.verifyReportPlugin(reportPlugin, project, session);
      List reports = new ArrayList();
      Iterator i = pluginDescriptor.getMojos().iterator();

      while(true) {
         while(i.hasNext()) {
            MojoDescriptor mojoDescriptor = (MojoDescriptor)i.next();
            if (forkEntryPoints.contains(mojoDescriptor)) {
               this.getLogger().debug("Omitting report: " + mojoDescriptor.getFullGoalName() + " from reports list. It initiated part of the fork currently executing.");
            } else if (reportSet == null || reportSet.getReports().contains(mojoDescriptor.getGoal())) {
               String id = null;
               if (reportSet != null) {
                  id = reportSet.getId();
               }

               MojoExecution reportExecution = new MojoExecution(mojoDescriptor, id);

               try {
                  MavenReport reportMojo = this.pluginManager.getReport(project, reportExecution, session);
                  if (reportMojo != null) {
                     reports.add(reportMojo);
                     mojoExecution.addMojoExecution(reportExecution);
                  }
               } catch (PluginManagerException var14) {
                  throw new LifecycleExecutionException("Error getting reports from the plugin '" + reportPlugin.getKey() + "': " + var14.getMessage(), var14);
               } catch (PluginConfigurationException var15) {
                  throw new LifecycleExecutionException("Error getting reports from the plugin '" + reportPlugin.getKey() + "'", var15);
               } catch (ArtifactNotFoundException var16) {
                  throw new LifecycleExecutionException(var16.getMessage(), var16);
               } catch (ArtifactResolutionException var17) {
                  throw new LifecycleExecutionException(var17.getMessage(), var17);
               }
            }
         }

         return reports;
      }
   }

   private void forkLifecycle(MojoDescriptor mojoDescriptor, Stack ancestorLifecycleForkers, MavenSession session, MavenProject project) throws LifecycleExecutionException, BuildFailureException, PluginNotFoundException {
      PluginDescriptor pluginDescriptor = mojoDescriptor.getPluginDescriptor();
      this.getLogger().info("Preparing " + pluginDescriptor.getGoalPrefix() + ":" + mojoDescriptor.getGoal());
      if (mojoDescriptor.isAggregator()) {
         Iterator i = session.getSortedProjects().iterator();

         while(i.hasNext()) {
            MavenProject reactorProject = (MavenProject)i.next();
            this.line();
            this.getLogger().info("Building " + reactorProject.getName());
            this.line();
            this.forkProjectLifecycle(mojoDescriptor, ancestorLifecycleForkers, session, reactorProject);
         }
      } else {
         this.forkProjectLifecycle(mojoDescriptor, ancestorLifecycleForkers, session, project);
      }

   }

   private void forkProjectLifecycle(MojoDescriptor mojoDescriptor, Stack forkEntryPoints, MavenSession session, MavenProject project) throws LifecycleExecutionException, BuildFailureException, PluginNotFoundException {
      forkEntryPoints.push(mojoDescriptor);
      PluginDescriptor pluginDescriptor = mojoDescriptor.getPluginDescriptor();
      String targetPhase = mojoDescriptor.getExecutePhase();
      Map lifecycleMappings = null;
      String executeLifecycle;
      if (targetPhase != null) {
         Lifecycle lifecycle = this.getLifecycleForPhase(targetPhase);
         lifecycleMappings = this.constructLifecycleMappings(session, targetPhase, project, lifecycle);
         executeLifecycle = mojoDescriptor.getExecuteLifecycle();
         if (executeLifecycle != null) {
            org.apache.maven.plugin.lifecycle.Lifecycle lifecycleOverlay;
            try {
               lifecycleOverlay = pluginDescriptor.getLifecycleMapping(executeLifecycle);
            } catch (IOException var23) {
               throw new LifecycleExecutionException("Unable to read lifecycle mapping file: " + var23.getMessage(), var23);
            } catch (XmlPullParserException var24) {
               throw new LifecycleExecutionException("Unable to parse lifecycle mapping file: " + var24.getMessage(), var24);
            }

            if (lifecycleOverlay == null) {
               throw new LifecycleExecutionException("Lifecycle '" + executeLifecycle + "' not found in plugin");
            }

            Iterator i = lifecycleOverlay.getPhases().iterator();

            label84:
            while(true) {
               Phase phase;
               Iterator j;
               Iterator k;
               do {
                  if (!i.hasNext()) {
                     break label84;
                  }

                  phase = (Phase)i.next();
                  j = phase.getExecutions().iterator();

                  while(j.hasNext()) {
                     Execution exec = (Execution)j.next();
                     k = exec.getGoals().iterator();

                     while(k.hasNext()) {
                        String goal = (String)k.next();
                        String lifecycleGoal;
                        PluginDescriptor lifecyclePluginDescriptor;
                        if (goal.indexOf(":") > 0) {
                           String[] s = StringUtils.split(goal, ":");
                           String groupId = s[0];
                           String artifactId = s[1];
                           lifecycleGoal = s[2];
                           Plugin plugin = new Plugin();
                           plugin.setGroupId(groupId);
                           plugin.setArtifactId(artifactId);
                           lifecyclePluginDescriptor = this.verifyPlugin(plugin, project, session.getSettings(), session.getLocalRepository());
                           if (lifecyclePluginDescriptor == null) {
                              throw new LifecycleExecutionException("Unable to find plugin " + groupId + ":" + artifactId);
                           }
                        } else {
                           lifecyclePluginDescriptor = pluginDescriptor;
                           lifecycleGoal = goal;
                        }

                        Xpp3Dom configuration = (Xpp3Dom)exec.getConfiguration();
                        if (phase.getConfiguration() != null) {
                           configuration = Xpp3Dom.mergeXpp3Dom(new Xpp3Dom((Xpp3Dom)phase.getConfiguration()), configuration);
                        }

                        MojoDescriptor desc = this.getMojoDescriptor(lifecyclePluginDescriptor, lifecycleGoal);
                        MojoExecution mojoExecution = new MojoExecution(desc, configuration);
                        this.addToLifecycleMappings(lifecycleMappings, phase.getId(), mojoExecution, session.getSettings());
                     }
                  }
               } while(phase.getConfiguration() == null);

               j = lifecycleMappings.values().iterator();

               while(j.hasNext()) {
                  List tasks = (List)j.next();
                  k = tasks.iterator();

                  while(k.hasNext()) {
                     MojoExecution exec = (MojoExecution)k.next();
                     Xpp3Dom configuration = Xpp3Dom.mergeXpp3Dom(new Xpp3Dom((Xpp3Dom)phase.getConfiguration()), exec.getConfiguration());
                     exec.setConfiguration(configuration);
                  }
               }
            }
         }

         this.removeFromLifecycle(forkEntryPoints, lifecycleMappings);
      }

      MavenProject executionProject = new MavenProject(project);
      if (targetPhase != null) {
         Lifecycle lifecycle = this.getLifecycleForPhase(targetPhase);
         this.executeGoalWithLifecycle(targetPhase, forkEntryPoints, session, lifecycleMappings, executionProject, lifecycle);
      } else {
         executeLifecycle = mojoDescriptor.getExecuteGoal();
         MojoDescriptor desc = this.getMojoDescriptor(pluginDescriptor, executeLifecycle);
         this.executeGoals(Collections.singletonList(new MojoExecution(desc)), forkEntryPoints, session, executionProject);
      }

      project.setExecutionProject(executionProject);
   }

   private Lifecycle getLifecycleForPhase(String phase) throws BuildFailureException, LifecycleExecutionException {
      Lifecycle lifecycle = (Lifecycle)this.getPhaseToLifecycleMap().get(phase);
      if (lifecycle == null) {
         throw new BuildFailureException("Unable to find lifecycle for phase '" + phase + "'");
      } else {
         return lifecycle;
      }
   }

   private MojoDescriptor getMojoDescriptor(PluginDescriptor pluginDescriptor, String goal) throws LifecycleExecutionException {
      MojoDescriptor desc = pluginDescriptor.getMojo(goal);
      if (desc == null) {
         String message = "Required goal '" + goal + "' not found in plugin '" + pluginDescriptor.getGoalPrefix() + "'";
         int index = goal.indexOf(58);
         if (index >= 0) {
            String prefix = goal.substring(index + 1);
            if (prefix.equals(pluginDescriptor.getGoalPrefix())) {
               message = message + " (goals should not be prefixed - try '" + prefix + "')";
            }
         }

         throw new LifecycleExecutionException(message);
      } else {
         return desc;
      }
   }

   private void removeFromLifecycle(Stack lifecycleForkers, Map lifecycleMappings) {
      Iterator lifecycleIterator = lifecycleMappings.values().iterator();

      while(lifecycleIterator.hasNext()) {
         List tasks = (List)lifecycleIterator.next();
         Iterator taskIterator = tasks.iterator();

         while(taskIterator.hasNext()) {
            MojoExecution execution = (MojoExecution)taskIterator.next();
            if (lifecycleForkers.contains(execution.getMojoDescriptor())) {
               taskIterator.remove();
               this.getLogger().warn("Removing: " + execution.getMojoDescriptor().getGoal() + " from forked lifecycle, to prevent recursive invocation.");
            }
         }
      }

   }

   private Map constructLifecycleMappings(MavenSession session, String selectedPhase, MavenProject project, Lifecycle lifecycle) throws LifecycleExecutionException, BuildFailureException, PluginNotFoundException {
      Map lifecycleMappings = this.bindLifecycleForPackaging(session, selectedPhase, project, lifecycle);
      Iterator i = project.getBuildPlugins().iterator();

      while(i.hasNext()) {
         Plugin plugin = (Plugin)i.next();
         this.bindPluginToLifecycle(plugin, session, lifecycleMappings, project);
      }

      return lifecycleMappings;
   }

   private Map bindLifecycleForPackaging(MavenSession session, String selectedPhase, MavenProject project, Lifecycle lifecycle) throws LifecycleExecutionException, BuildFailureException, PluginNotFoundException {
      Map mappings = this.findMappingsForLifecycle(session, project, lifecycle);
      List optionalMojos = this.findOptionalMojosForLifecycle(session, project, lifecycle);
      Map lifecycleMappings = new HashMap();
      Iterator i = lifecycle.getPhases().iterator();

      while(i.hasNext()) {
         String phase = (String)i.next();
         String phaseTasks = (String)mappings.get(phase);
         if (phaseTasks != null) {
            StringTokenizer tok = new StringTokenizer(phaseTasks, ",");

            while(tok.hasMoreTokens()) {
               String goal = tok.nextToken().trim();
               MojoDescriptor mojoDescriptor = this.getMojoDescriptor(goal, session, project, selectedPhase, false, optionalMojos.contains(goal));
               if (mojoDescriptor != null) {
                  if (mojoDescriptor.isDirectInvocationOnly()) {
                     throw new LifecycleExecutionException("Mojo: '" + goal + "' requires direct invocation. It cannot be used as part of lifecycle: '" + project.getPackaging() + "'.");
                  }

                  this.addToLifecycleMappings(lifecycleMappings, phase, new MojoExecution(mojoDescriptor), session.getSettings());
               }
            }
         }

         if (phase.equals(selectedPhase)) {
            break;
         }
      }

      return lifecycleMappings;
   }

   private Map findMappingsForLifecycle(MavenSession session, MavenProject project, Lifecycle lifecycle) throws LifecycleExecutionException, PluginNotFoundException {
      String packaging = project.getPackaging();
      Map mappings = null;
      LifecycleMapping m = (LifecycleMapping)this.findExtension(project, LifecycleMapping.ROLE, packaging, session.getSettings(), session.getLocalRepository());
      if (m != null) {
         mappings = m.getPhases(lifecycle.getId());
      }

      Map defaultMappings = lifecycle.getDefaultPhases();
      if (mappings == null) {
         try {
            m = (LifecycleMapping)session.lookup(LifecycleMapping.ROLE, packaging);
            mappings = m.getPhases(lifecycle.getId());
         } catch (ComponentLookupException var9) {
            if (defaultMappings == null) {
               throw new LifecycleExecutionException("Cannot find lifecycle mapping for packaging: '" + packaging + "'.", var9);
            }
         }
      }

      if (mappings == null) {
         if (defaultMappings == null) {
            throw new LifecycleExecutionException("Cannot find lifecycle mapping for packaging: '" + packaging + "', and there is no default");
         }

         mappings = defaultMappings;
      }

      return mappings;
   }

   private List findOptionalMojosForLifecycle(MavenSession session, MavenProject project, Lifecycle lifecycle) throws LifecycleExecutionException, PluginNotFoundException {
      String packaging = project.getPackaging();
      List optionalMojos = null;
      LifecycleMapping m = (LifecycleMapping)this.findExtension(project, LifecycleMapping.ROLE, packaging, session.getSettings(), session.getLocalRepository());
      if (m != null) {
         optionalMojos = m.getOptionalMojos(lifecycle.getId());
      }

      if (optionalMojos == null) {
         try {
            m = (LifecycleMapping)session.lookup(LifecycleMapping.ROLE, packaging);
            optionalMojos = m.getOptionalMojos(lifecycle.getId());
         } catch (ComponentLookupException var8) {
            this.getLogger().debug("Error looking up lifecycle mapping to retrieve optional mojos. Lifecycle ID: " + lifecycle.getId() + ". Error: " + var8.getMessage(), var8);
         }
      }

      if (optionalMojos == null) {
         optionalMojos = Collections.EMPTY_LIST;
      }

      return optionalMojos;
   }

   private Object findExtension(MavenProject project, String role, String roleHint, Settings settings, ArtifactRepository localRepository) throws LifecycleExecutionException, PluginNotFoundException {
      Object pluginComponent = null;
      Iterator i = project.getBuildPlugins().iterator();

      while(i.hasNext() && pluginComponent == null) {
         Plugin plugin = (Plugin)i.next();
         if (plugin.isExtensions()) {
            this.verifyPlugin(plugin, project, settings, localRepository);

            try {
               pluginComponent = this.pluginManager.getPluginComponent(plugin, role, roleHint);
            } catch (ComponentLookupException var10) {
               this.getLogger().debug("Unable to find the lifecycle component in the extension", var10);
            } catch (PluginManagerException var11) {
               throw new LifecycleExecutionException("Error getting extensions from the plugin '" + plugin.getKey() + "': " + var11.getMessage(), var11);
            }
         }
      }

      return pluginComponent;
   }

   private Map findArtifactTypeHandlers(MavenProject project, Settings settings, ArtifactRepository localRepository) throws LifecycleExecutionException, PluginNotFoundException {
      Map map = new HashMap();
      Iterator i = project.getBuildPlugins().iterator();

      while(true) {
         Plugin plugin;
         do {
            if (!i.hasNext()) {
               return map;
            }

            plugin = (Plugin)i.next();
         } while(!plugin.isExtensions());

         this.verifyPlugin(plugin, project, settings, localRepository);

         try {
            Map components = this.pluginManager.getPluginComponents(plugin, ArtifactHandler.ROLE);
            map.putAll(components);
         } catch (ComponentLookupException var9) {
            this.getLogger().debug("Unable to find the lifecycle component in the extension", var9);
         } catch (PluginManagerException var10) {
            throw new LifecycleExecutionException("Error looking up available components from plugin '" + plugin.getKey() + "': " + var10.getMessage(), var10);
         }

         Iterator j = map.values().iterator();

         while(j.hasNext()) {
            ArtifactHandler handler = (ArtifactHandler)j.next();
            if (project.getPackaging().equals(handler.getPackaging())) {
               project.getArtifact().setArtifactHandler(handler);
            }
         }
      }
   }

   private void bindPluginToLifecycle(Plugin plugin, MavenSession session, Map phaseMap, MavenProject project) throws LifecycleExecutionException, PluginNotFoundException {
      Settings settings = session.getSettings();
      PluginDescriptor pluginDescriptor = this.verifyPlugin(plugin, project, session.getSettings(), session.getLocalRepository());
      if (pluginDescriptor.getMojos() != null && !pluginDescriptor.getMojos().isEmpty() && (plugin.isInheritanceApplied() || pluginDescriptor.isInheritedByDefault())) {
         if (plugin.getGoals() != null) {
            this.getLogger().error("Plugin contains a <goals/> section: this is IGNORED - please use <executions/> instead.");
         }

         List executions = plugin.getExecutions();
         if (executions != null) {
            Iterator it = executions.iterator();

            while(it.hasNext()) {
               PluginExecution execution = (PluginExecution)it.next();
               this.bindExecutionToLifecycle(pluginDescriptor, phaseMap, execution, settings);
            }
         }
      }

   }

   private PluginDescriptor verifyPlugin(Plugin plugin, MavenProject project, Settings settings, ArtifactRepository localRepository) throws LifecycleExecutionException, PluginNotFoundException {
      try {
         PluginDescriptor pluginDescriptor = this.pluginManager.verifyPlugin(plugin, project, settings, localRepository);
         return pluginDescriptor;
      } catch (PluginManagerException var7) {
         throw new LifecycleExecutionException("Internal error in the plugin manager getting plugin '" + plugin.getKey() + "': " + var7.getMessage(), var7);
      } catch (PluginVersionResolutionException var8) {
         throw new LifecycleExecutionException(var8.getMessage(), var8);
      } catch (InvalidVersionSpecificationException var9) {
         throw new LifecycleExecutionException(var9.getMessage(), var9);
      } catch (InvalidPluginException var10) {
         throw new LifecycleExecutionException(var10.getMessage(), var10);
      } catch (ArtifactNotFoundException var11) {
         throw new LifecycleExecutionException(var11.getMessage(), var11);
      } catch (ArtifactResolutionException var12) {
         throw new LifecycleExecutionException(var12.getMessage(), var12);
      } catch (PluginVersionNotFoundException var13) {
         throw new LifecycleExecutionException(var13.getMessage(), var13);
      }
   }

   private PluginDescriptor verifyReportPlugin(ReportPlugin plugin, MavenProject project, MavenSession session) throws LifecycleExecutionException, PluginNotFoundException {
      try {
         PluginDescriptor pluginDescriptor = this.pluginManager.verifyReportPlugin(plugin, project, session);
         return pluginDescriptor;
      } catch (PluginManagerException var6) {
         throw new LifecycleExecutionException("Internal error in the plugin manager getting report '" + plugin.getKey() + "': " + var6.getMessage(), var6);
      } catch (PluginVersionResolutionException var7) {
         throw new LifecycleExecutionException(var7.getMessage(), var7);
      } catch (InvalidVersionSpecificationException var8) {
         throw new LifecycleExecutionException(var8.getMessage(), var8);
      } catch (InvalidPluginException var9) {
         throw new LifecycleExecutionException(var9.getMessage(), var9);
      } catch (ArtifactNotFoundException var10) {
         throw new LifecycleExecutionException(var10.getMessage(), var10);
      } catch (ArtifactResolutionException var11) {
         throw new LifecycleExecutionException(var11.getMessage(), var11);
      } catch (PluginVersionNotFoundException var12) {
         throw new LifecycleExecutionException(var12.getMessage(), var12);
      }
   }

   private void bindExecutionToLifecycle(PluginDescriptor pluginDescriptor, Map phaseMap, PluginExecution execution, Settings settings) throws LifecycleExecutionException {
      Iterator i = execution.getGoals().iterator();

      while(true) {
         String goal;
         MojoDescriptor mojoDescriptor;
         do {
            if (!i.hasNext()) {
               return;
            }

            goal = (String)i.next();
            mojoDescriptor = pluginDescriptor.getMojo(goal);
            if (mojoDescriptor == null) {
               throw new LifecycleExecutionException("'" + goal + "' was specified in an execution, but not found in the plugin");
            }
         } while(!execution.isInheritanceApplied() && !mojoDescriptor.isInheritedByDefault());

         MojoExecution mojoExecution = new MojoExecution(mojoDescriptor, execution.getId());
         String phase = execution.getPhase();
         if (phase == null) {
            phase = mojoDescriptor.getPhase();
         }

         if (phase != null) {
            if (mojoDescriptor.isDirectInvocationOnly()) {
               throw new LifecycleExecutionException("Mojo: '" + goal + "' requires direct invocation. It cannot be used as part of the lifecycle (it was included via the POM).");
            }

            this.addToLifecycleMappings(phaseMap, phase, mojoExecution, settings);
         }
      }
   }

   private void addToLifecycleMappings(Map lifecycleMappings, String phase, MojoExecution mojoExecution, Settings settings) {
      List goals = (List)lifecycleMappings.get(phase);
      if (goals == null) {
         goals = new ArrayList();
         lifecycleMappings.put(phase, goals);
      }

      MojoDescriptor mojoDescriptor = mojoExecution.getMojoDescriptor();
      if (settings.isOffline() && mojoDescriptor.isOnlineRequired()) {
         String goal = mojoDescriptor.getGoal();
         this.getLogger().warn(goal + " requires online mode, but maven is currently offline. Disabling " + goal + ".");
      } else {
         ((List)goals).add(mojoExecution);
      }

   }

   private List processGoalChain(String task, Map phaseMap, Lifecycle lifecycle) {
      List goals = new ArrayList();
      int index = lifecycle.getPhases().indexOf(task);

      for(int i = 0; i <= index; ++i) {
         String p = (String)lifecycle.getPhases().get(i);
         List phaseGoals = (List)phaseMap.get(p);
         if (phaseGoals != null) {
            goals.addAll(phaseGoals);
         }
      }

      return goals;
   }

   private MojoDescriptor getMojoDescriptor(String task, MavenSession session, MavenProject project, String invokedVia, boolean canUsePrefix, boolean isOptionalMojo) throws BuildFailureException, LifecycleExecutionException, PluginNotFoundException {
      PluginDescriptor pluginDescriptor = null;

      try {
         StringTokenizer tok = new StringTokenizer(task, ":");
         int numTokens = tok.countTokens();
         String prefix;
         String goal;
         Plugin plugin;
         if (numTokens == 2) {
            if (!canUsePrefix) {
               prefix = "Mapped-prefix lookup of mojos are only supported from direct invocation. Please use specification of the form groupId:artifactId[:version]:goal instead. (Offending mojo: '" + task + "', invoked via: '" + invokedVia + "')";
               throw new LifecycleExecutionException(prefix);
            }

            prefix = tok.nextToken();
            goal = tok.nextToken();
            pluginDescriptor = this.pluginManager.getPluginDescriptorForPrefix(prefix);
            if (pluginDescriptor == null) {
               plugin = this.pluginManager.getPluginDefinitionForPrefix(prefix, session, project);
            } else {
               plugin = new Plugin();
               plugin.setGroupId(pluginDescriptor.getGroupId());
               plugin.setArtifactId(pluginDescriptor.getArtifactId());
               plugin.setVersion(pluginDescriptor.getVersion());
            }

            if (plugin == null) {
               Iterator i = project.getBuildPlugins().iterator();

               while(i.hasNext()) {
                  Plugin buildPlugin = (Plugin)i.next();
                  PluginDescriptor desc = this.verifyPlugin(buildPlugin, project, session.getSettings(), session.getLocalRepository());
                  if (prefix.equals(desc.getGoalPrefix())) {
                     plugin = buildPlugin;
                  }
               }
            }

            if (plugin == null) {
               plugin = new Plugin();
               plugin.setGroupId(PluginDescriptor.getDefaultPluginGroupId());
               plugin.setArtifactId(PluginDescriptor.getDefaultPluginArtifactId(prefix));
            }
         } else {
            if (numTokens != 3 && numTokens != 4) {
               prefix = "Invalid task '" + task + "': you must specify a valid lifecycle phase, or" + " a goal in the format plugin:goal or pluginGroupId:pluginArtifactId:pluginVersion:goal";
               throw new BuildFailureException(prefix);
            }

            plugin = new Plugin();
            plugin.setGroupId(tok.nextToken());
            plugin.setArtifactId(tok.nextToken());
            if (numTokens == 4) {
               plugin.setVersion(tok.nextToken());
            }

            goal = tok.nextToken();
         }

         if (plugin.getVersion() == null) {
            Iterator i = project.getBuildPlugins().iterator();

            while(i.hasNext()) {
               Plugin buildPlugin = (Plugin)i.next();
               if (buildPlugin.getKey().equals(plugin.getKey())) {
                  plugin = buildPlugin;
                  break;
               }
            }

            project.injectPluginManagementInfo(plugin);
         }

         if (pluginDescriptor == null) {
            pluginDescriptor = this.verifyPlugin(plugin, project, session.getSettings(), session.getLocalRepository());
         }

         project.addPlugin(plugin);
         MojoDescriptor mojoDescriptor = pluginDescriptor.getMojo(goal);
         if (mojoDescriptor == null) {
            if (!isOptionalMojo) {
               throw new BuildFailureException("Required goal not found: " + task + " in " + pluginDescriptor.getId());
            }

            this.getLogger().info("Skipping missing optional mojo: " + task);
         }

         return mojoDescriptor;
      } catch (PluginNotFoundException var16) {
         if (isOptionalMojo) {
            this.getLogger().info("Skipping missing optional mojo: " + task);
            this.getLogger().debug("Mojo: " + task + " could not be found. Reason: " + var16.getMessage(), var16);
            return null;
         } else {
            throw var16;
         }
      }
   }

   protected void line() {
      this.getLogger().info("------------------------------------------------------------------------");
   }

   public Map getPhaseToLifecycleMap() throws LifecycleExecutionException {
      if (this.phaseToLifecycleMap == null) {
         this.phaseToLifecycleMap = new HashMap();
         Iterator i = this.lifecycles.iterator();

         while(i.hasNext()) {
            Lifecycle lifecycle = (Lifecycle)i.next();
            Iterator p = lifecycle.getPhases().iterator();

            while(p.hasNext()) {
               String phase = (String)p.next();
               if (this.phaseToLifecycleMap.containsKey(phase)) {
                  Lifecycle prevLifecycle = (Lifecycle)this.phaseToLifecycleMap.get(phase);
                  throw new LifecycleExecutionException("Phase '" + phase + "' is defined in more than one lifecycle: '" + lifecycle.getId() + "' and '" + prevLifecycle.getId() + "'");
               }

               this.phaseToLifecycleMap.put(phase, lifecycle);
            }
         }
      }

      return this.phaseToLifecycleMap;
   }

   private static class TaskSegment {
      private boolean aggregate;
      private List tasks = new ArrayList();

      TaskSegment() {
      }

      TaskSegment(boolean aggregate) {
         this.aggregate = aggregate;
      }

      public String toString() {
         StringBuffer message = new StringBuffer();
         message.append(" task-segment: [");
         Iterator it = this.tasks.iterator();

         while(it.hasNext()) {
            String task = (String)it.next();
            message.append(task);
            if (it.hasNext()) {
               message.append(", ");
            }
         }

         message.append("]");
         if (this.aggregate) {
            message.append(" (aggregator-style)");
         }

         return message.toString();
      }

      boolean aggregate() {
         return this.aggregate;
      }

      void add(String task) {
         this.tasks.add(task);
      }

      List getTasks() {
         return this.tasks;
      }
   }
}

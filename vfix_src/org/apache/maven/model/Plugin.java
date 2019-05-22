package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Plugin extends ConfigurationContainer implements Serializable {
   private String groupId = "org.apache.maven.plugins";
   private String artifactId;
   private String version;
   private boolean extensions = false;
   private List<PluginExecution> executions;
   private List<Dependency> dependencies;
   private Object goals;
   private Map executionMap = null;
   private String key;

   public void addDependency(Dependency dependency) {
      if (!(dependency instanceof Dependency)) {
         throw new ClassCastException("Plugin.addDependencies(dependency) parameter must be instanceof " + Dependency.class.getName());
      } else {
         this.getDependencies().add(dependency);
      }
   }

   public void addExecution(PluginExecution pluginExecution) {
      if (!(pluginExecution instanceof PluginExecution)) {
         throw new ClassCastException("Plugin.addExecutions(pluginExecution) parameter must be instanceof " + PluginExecution.class.getName());
      } else {
         this.getExecutions().add(pluginExecution);
      }
   }

   public String getArtifactId() {
      return this.artifactId;
   }

   public List<Dependency> getDependencies() {
      if (this.dependencies == null) {
         this.dependencies = new ArrayList();
      }

      return this.dependencies;
   }

   public List<PluginExecution> getExecutions() {
      if (this.executions == null) {
         this.executions = new ArrayList();
      }

      return this.executions;
   }

   public Object getGoals() {
      return this.goals;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getVersion() {
      return this.version;
   }

   public boolean isExtensions() {
      return this.extensions;
   }

   public void removeDependency(Dependency dependency) {
      if (!(dependency instanceof Dependency)) {
         throw new ClassCastException("Plugin.removeDependencies(dependency) parameter must be instanceof " + Dependency.class.getName());
      } else {
         this.getDependencies().remove(dependency);
      }
   }

   public void removeExecution(PluginExecution pluginExecution) {
      if (!(pluginExecution instanceof PluginExecution)) {
         throw new ClassCastException("Plugin.removeExecutions(pluginExecution) parameter must be instanceof " + PluginExecution.class.getName());
      } else {
         this.getExecutions().remove(pluginExecution);
      }
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public void setDependencies(List<Dependency> dependencies) {
      this.dependencies = dependencies;
   }

   public void setExecutions(List<PluginExecution> executions) {
      this.executions = executions;
   }

   public void setExtensions(boolean extensions) {
      this.extensions = extensions;
   }

   public void setGoals(Object goals) {
      this.goals = goals;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public void flushExecutionMap() {
      this.executionMap = null;
   }

   public Map getExecutionsAsMap() {
      if (this.executionMap == null) {
         this.executionMap = new LinkedHashMap();
         if (this.getExecutions() != null) {
            Iterator i = this.getExecutions().iterator();

            while(i.hasNext()) {
               PluginExecution exec = (PluginExecution)i.next();
               if (this.executionMap.containsKey(exec.getId())) {
                  throw new IllegalStateException("You cannot have two plugin executions with the same (or missing) <id/> elements.\nOffending execution\n\nId: '" + exec.getId() + "'\nPlugin:'" + this.getKey() + "'\n\n");
               }

               this.executionMap.put(exec.getId(), exec);
            }
         }
      }

      return this.executionMap;
   }

   public String getKey() {
      if (this.key == null) {
         this.key = constructKey(this.groupId, this.artifactId).intern();
      }

      return this.key;
   }

   public static String constructKey(String groupId, String artifactId) {
      return groupId + ":" + artifactId;
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (other instanceof Plugin) {
         Plugin otherPlugin = (Plugin)other;
         return this.getKey().equals(otherPlugin.getKey());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.getKey().hashCode();
   }

   public String toString() {
      return "Plugin [" + this.getKey() + "]";
   }
}

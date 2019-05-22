package org.apache.maven.project.overlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.maven.model.Build;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.Resource;

public class BuildOverlay extends Build {
   private final Build build;
   private List resources;
   private List testResources;

   public BuildOverlay(Build build) {
      if (build == null) {
         this.build = new Build();
         this.resources = new ArrayList();
         this.testResources = new ArrayList();
      } else {
         this.build = build;
         this.resources = new ArrayList(build.getResources());
         this.testResources = new ArrayList(build.getTestResources());
      }

   }

   public void addExtension(Extension extension) {
      this.build.addExtension(extension);
   }

   public void addPlugin(Plugin plugin) {
      this.build.addPlugin(plugin);
   }

   public void addResource(Resource resource) {
      this.resources.add(resource);
   }

   public void addTestResource(Resource resource) {
      this.testResources.add(resource);
   }

   public boolean equals(Object obj) {
      return this.build.equals(obj);
   }

   public void flushPluginMap() {
      this.build.flushPluginMap();
   }

   public String getDefaultGoal() {
      return this.build.getDefaultGoal();
   }

   public String getDirectory() {
      return this.build.getDirectory();
   }

   public List getExtensions() {
      return this.build.getExtensions();
   }

   public String getFinalName() {
      return this.build.getFinalName();
   }

   public String getOutputDirectory() {
      return this.build.getOutputDirectory();
   }

   public PluginManagement getPluginManagement() {
      return this.build.getPluginManagement();
   }

   public List getPlugins() {
      return this.build.getPlugins();
   }

   public Map getPluginsAsMap() {
      return this.build.getPluginsAsMap();
   }

   public List getResources() {
      return this.resources;
   }

   public String getScriptSourceDirectory() {
      return this.build.getScriptSourceDirectory();
   }

   public String getSourceDirectory() {
      return this.build.getSourceDirectory();
   }

   public String getTestOutputDirectory() {
      return this.build.getTestOutputDirectory();
   }

   public List getTestResources() {
      return this.testResources;
   }

   public String getTestSourceDirectory() {
      return this.build.getTestSourceDirectory();
   }

   public int hashCode() {
      return this.build.hashCode();
   }

   public void removeExtension(Extension extension) {
      this.build.removeExtension(extension);
   }

   public void removePlugin(Plugin plugin) {
      this.build.removePlugin(plugin);
   }

   public void removeResource(Resource resource) {
      this.resources.remove(resource);
   }

   public void removeTestResource(Resource resource) {
      this.testResources.remove(resource);
   }

   public void setDefaultGoal(String defaultGoal) {
      this.build.setDefaultGoal(defaultGoal);
   }

   public void setDirectory(String directory) {
      this.build.setDirectory(directory);
   }

   public void setExtensions(List extensions) {
      this.build.setExtensions(extensions);
   }

   public void setFinalName(String finalName) {
      this.build.setFinalName(finalName);
   }

   public void setOutputDirectory(String outputDirectory) {
      this.build.setOutputDirectory(outputDirectory);
   }

   public void setPluginManagement(PluginManagement pluginManagement) {
      this.build.setPluginManagement(pluginManagement);
   }

   public void setPlugins(List plugins) {
      this.build.setPlugins(plugins);
   }

   public void setResources(List resources) {
      this.resources = resources;
   }

   public void setScriptSourceDirectory(String scriptSourceDirectory) {
      this.build.setScriptSourceDirectory(scriptSourceDirectory);
   }

   public void setSourceDirectory(String sourceDirectory) {
      this.build.setSourceDirectory(sourceDirectory);
   }

   public void setTestOutputDirectory(String testOutputDirectory) {
      this.build.setTestOutputDirectory(testOutputDirectory);
   }

   public void setTestResources(List testResources) {
      this.testResources = testResources;
   }

   public void setTestSourceDirectory(String testSourceDirectory) {
      this.build.setTestSourceDirectory(testSourceDirectory);
   }

   public String toString() {
      return this.build.toString();
   }

   public void addFilter(String string) {
      this.build.addFilter(string);
   }

   public List getFilters() {
      return this.build.getFilters();
   }

   public void removeFilter(String string) {
      this.build.removeFilter(string);
   }

   public void setFilters(List filters) {
      this.build.setFilters(filters);
   }
}

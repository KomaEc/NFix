package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ModelBase implements Serializable {
   private DistributionManagement distributionManagement;
   private List<String> modules;
   private List<Repository> repositories;
   private List<Repository> pluginRepositories;
   private List<Dependency> dependencies;
   private Object reports;
   private Reporting reporting;
   private DependencyManagement dependencyManagement;
   private Properties properties;

   public void addDependency(Dependency dependency) {
      if (!(dependency instanceof Dependency)) {
         throw new ClassCastException("ModelBase.addDependencies(dependency) parameter must be instanceof " + Dependency.class.getName());
      } else {
         this.getDependencies().add(dependency);
      }
   }

   public void addModule(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("ModelBase.addModules(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getModules().add(string);
      }
   }

   public void addPluginRepository(Repository repository) {
      if (!(repository instanceof Repository)) {
         throw new ClassCastException("ModelBase.addPluginRepositories(repository) parameter must be instanceof " + Repository.class.getName());
      } else {
         this.getPluginRepositories().add(repository);
      }
   }

   public void addProperty(String key, String value) {
      this.getProperties().put(key, value);
   }

   public void addRepository(Repository repository) {
      if (!(repository instanceof Repository)) {
         throw new ClassCastException("ModelBase.addRepositories(repository) parameter must be instanceof " + Repository.class.getName());
      } else {
         this.getRepositories().add(repository);
      }
   }

   public List<Dependency> getDependencies() {
      if (this.dependencies == null) {
         this.dependencies = new ArrayList();
      }

      return this.dependencies;
   }

   public DependencyManagement getDependencyManagement() {
      return this.dependencyManagement;
   }

   public DistributionManagement getDistributionManagement() {
      return this.distributionManagement;
   }

   public List<String> getModules() {
      if (this.modules == null) {
         this.modules = new ArrayList();
      }

      return this.modules;
   }

   public List<Repository> getPluginRepositories() {
      if (this.pluginRepositories == null) {
         this.pluginRepositories = new ArrayList();
      }

      return this.pluginRepositories;
   }

   public Properties getProperties() {
      if (this.properties == null) {
         this.properties = new Properties();
      }

      return this.properties;
   }

   public Reporting getReporting() {
      return this.reporting;
   }

   public Object getReports() {
      return this.reports;
   }

   public List<Repository> getRepositories() {
      if (this.repositories == null) {
         this.repositories = new ArrayList();
      }

      return this.repositories;
   }

   public void removeDependency(Dependency dependency) {
      if (!(dependency instanceof Dependency)) {
         throw new ClassCastException("ModelBase.removeDependencies(dependency) parameter must be instanceof " + Dependency.class.getName());
      } else {
         this.getDependencies().remove(dependency);
      }
   }

   public void removeModule(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("ModelBase.removeModules(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getModules().remove(string);
      }
   }

   public void removePluginRepository(Repository repository) {
      if (!(repository instanceof Repository)) {
         throw new ClassCastException("ModelBase.removePluginRepositories(repository) parameter must be instanceof " + Repository.class.getName());
      } else {
         this.getPluginRepositories().remove(repository);
      }
   }

   public void removeRepository(Repository repository) {
      if (!(repository instanceof Repository)) {
         throw new ClassCastException("ModelBase.removeRepositories(repository) parameter must be instanceof " + Repository.class.getName());
      } else {
         this.getRepositories().remove(repository);
      }
   }

   public void setDependencies(List<Dependency> dependencies) {
      this.dependencies = dependencies;
   }

   public void setDependencyManagement(DependencyManagement dependencyManagement) {
      this.dependencyManagement = dependencyManagement;
   }

   public void setDistributionManagement(DistributionManagement distributionManagement) {
      this.distributionManagement = distributionManagement;
   }

   public void setModules(List<String> modules) {
      this.modules = modules;
   }

   public void setPluginRepositories(List<Repository> pluginRepositories) {
      this.pluginRepositories = pluginRepositories;
   }

   public void setProperties(Properties properties) {
      this.properties = properties;
   }

   public void setReporting(Reporting reporting) {
      this.reporting = reporting;
   }

   public void setReports(Object reports) {
      this.reports = reports;
   }

   public void setRepositories(List<Repository> repositories) {
      this.repositories = repositories;
   }
}

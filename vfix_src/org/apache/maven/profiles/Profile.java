package org.apache.maven.profiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Profile implements Serializable {
   private String id;
   private Activation activation;
   private Properties properties;
   private List<Repository> repositories;
   private List<Repository> pluginRepositories;

   public void addPluginRepository(Repository repository) {
      if (!(repository instanceof Repository)) {
         throw new ClassCastException("Profile.addPluginRepositories(repository) parameter must be instanceof " + Repository.class.getName());
      } else {
         this.getPluginRepositories().add(repository);
      }
   }

   public void addProperty(String key, String value) {
      this.getProperties().put(key, value);
   }

   public void addRepository(Repository repository) {
      if (!(repository instanceof Repository)) {
         throw new ClassCastException("Profile.addRepositories(repository) parameter must be instanceof " + Repository.class.getName());
      } else {
         this.getRepositories().add(repository);
      }
   }

   public Activation getActivation() {
      return this.activation;
   }

   public String getId() {
      return this.id;
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

   public List<Repository> getRepositories() {
      if (this.repositories == null) {
         this.repositories = new ArrayList();
      }

      return this.repositories;
   }

   public void removePluginRepository(Repository repository) {
      if (!(repository instanceof Repository)) {
         throw new ClassCastException("Profile.removePluginRepositories(repository) parameter must be instanceof " + Repository.class.getName());
      } else {
         this.getPluginRepositories().remove(repository);
      }
   }

   public void removeRepository(Repository repository) {
      if (!(repository instanceof Repository)) {
         throw new ClassCastException("Profile.removeRepositories(repository) parameter must be instanceof " + Repository.class.getName());
      } else {
         this.getRepositories().remove(repository);
      }
   }

   public void setActivation(Activation activation) {
      this.activation = activation;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setPluginRepositories(List<Repository> pluginRepositories) {
      this.pluginRepositories = pluginRepositories;
   }

   public void setProperties(Properties properties) {
      this.properties = properties;
   }

   public void setRepositories(List<Repository> repositories) {
      this.repositories = repositories;
   }
}

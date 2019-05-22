package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BuildBase extends PluginConfiguration implements Serializable {
   private String defaultGoal;
   private List<Resource> resources;
   private List<Resource> testResources;
   private String directory;
   private String finalName;
   private List<String> filters;

   public void addFilter(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("BuildBase.addFilters(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getFilters().add(string);
      }
   }

   public void addResource(Resource resource) {
      if (!(resource instanceof Resource)) {
         throw new ClassCastException("BuildBase.addResources(resource) parameter must be instanceof " + Resource.class.getName());
      } else {
         this.getResources().add(resource);
      }
   }

   public void addTestResource(Resource resource) {
      if (!(resource instanceof Resource)) {
         throw new ClassCastException("BuildBase.addTestResources(resource) parameter must be instanceof " + Resource.class.getName());
      } else {
         this.getTestResources().add(resource);
      }
   }

   public String getDefaultGoal() {
      return this.defaultGoal;
   }

   public String getDirectory() {
      return this.directory;
   }

   public List<String> getFilters() {
      if (this.filters == null) {
         this.filters = new ArrayList();
      }

      return this.filters;
   }

   public String getFinalName() {
      return this.finalName;
   }

   public List<Resource> getResources() {
      if (this.resources == null) {
         this.resources = new ArrayList();
      }

      return this.resources;
   }

   public List<Resource> getTestResources() {
      if (this.testResources == null) {
         this.testResources = new ArrayList();
      }

      return this.testResources;
   }

   public void removeFilter(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("BuildBase.removeFilters(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getFilters().remove(string);
      }
   }

   public void removeResource(Resource resource) {
      if (!(resource instanceof Resource)) {
         throw new ClassCastException("BuildBase.removeResources(resource) parameter must be instanceof " + Resource.class.getName());
      } else {
         this.getResources().remove(resource);
      }
   }

   public void removeTestResource(Resource resource) {
      if (!(resource instanceof Resource)) {
         throw new ClassCastException("BuildBase.removeTestResources(resource) parameter must be instanceof " + Resource.class.getName());
      } else {
         this.getTestResources().remove(resource);
      }
   }

   public void setDefaultGoal(String defaultGoal) {
      this.defaultGoal = defaultGoal;
   }

   public void setDirectory(String directory) {
      this.directory = directory;
   }

   public void setFilters(List<String> filters) {
      this.filters = filters;
   }

   public void setFinalName(String finalName) {
      this.finalName = finalName;
   }

   public void setResources(List<Resource> resources) {
      this.resources = resources;
   }

   public void setTestResources(List<Resource> testResources) {
      this.testResources = testResources;
   }
}

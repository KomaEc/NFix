package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DependencyManagement implements Serializable {
   private List<Dependency> dependencies;

   public void addDependency(Dependency dependency) {
      if (!(dependency instanceof Dependency)) {
         throw new ClassCastException("DependencyManagement.addDependencies(dependency) parameter must be instanceof " + Dependency.class.getName());
      } else {
         this.getDependencies().add(dependency);
      }
   }

   public List<Dependency> getDependencies() {
      if (this.dependencies == null) {
         this.dependencies = new ArrayList();
      }

      return this.dependencies;
   }

   public void removeDependency(Dependency dependency) {
      if (!(dependency instanceof Dependency)) {
         throw new ClassCastException("DependencyManagement.removeDependencies(dependency) parameter must be instanceof " + Dependency.class.getName());
      } else {
         this.getDependencies().remove(dependency);
      }
   }

   public void setDependencies(List<Dependency> dependencies) {
      this.dependencies = dependencies;
   }
}

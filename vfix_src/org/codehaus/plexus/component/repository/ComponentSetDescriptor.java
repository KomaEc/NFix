package org.codehaus.plexus.component.repository;

import java.util.ArrayList;
import java.util.List;

public class ComponentSetDescriptor {
   private List components;
   private List dependencies;
   private boolean isolatedRealm;
   private String id;

   public List getComponents() {
      return this.components;
   }

   public void addComponentDescriptor(ComponentDescriptor cd) {
      if (this.components == null) {
         this.components = new ArrayList();
      }

      this.components.add(cd);
   }

   public void setComponents(List components) {
      this.components = components;
   }

   public List getDependencies() {
      return this.dependencies;
   }

   public void addDependency(ComponentDependency cd) {
      if (this.dependencies == null) {
         this.dependencies = new ArrayList();
      }

      this.dependencies.add(cd);
   }

   public void setDependencies(List dependencies) {
      this.dependencies = dependencies;
   }

   public void setIsolatedRealm(boolean isolatedRealm) {
      this.isolatedRealm = isolatedRealm;
   }

   public boolean isIsolatedRealm() {
      return this.isolatedRealm;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }
}

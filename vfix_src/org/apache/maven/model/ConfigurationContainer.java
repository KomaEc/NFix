package org.apache.maven.model;

import java.io.Serializable;

public class ConfigurationContainer implements Serializable {
   private String inherited;
   private Object configuration;
   private boolean inheritanceApplied = true;

   public Object getConfiguration() {
      return this.configuration;
   }

   public String getInherited() {
      return this.inherited;
   }

   public void setConfiguration(Object configuration) {
      this.configuration = configuration;
   }

   public void setInherited(String inherited) {
      this.inherited = inherited;
   }

   public void unsetInheritanceApplied() {
      this.inheritanceApplied = false;
   }

   public boolean isInheritanceApplied() {
      return this.inheritanceApplied;
   }
}

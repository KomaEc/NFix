package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportSet implements Serializable {
   private String id = "default";
   private Object configuration;
   private String inherited;
   private List<String> reports;
   private boolean inheritanceApplied = true;

   public void addReport(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("ReportSet.addReports(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getReports().add(string);
      }
   }

   public Object getConfiguration() {
      return this.configuration;
   }

   public String getId() {
      return this.id;
   }

   public String getInherited() {
      return this.inherited;
   }

   public List<String> getReports() {
      if (this.reports == null) {
         this.reports = new ArrayList();
      }

      return this.reports;
   }

   public void removeReport(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("ReportSet.removeReports(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getReports().remove(string);
      }
   }

   public void setConfiguration(Object configuration) {
      this.configuration = configuration;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setInherited(String inherited) {
      this.inherited = inherited;
   }

   public void setReports(List<String> reports) {
      this.reports = reports;
   }

   public void unsetInheritanceApplied() {
      this.inheritanceApplied = false;
   }

   public boolean isInheritanceApplied() {
      return this.inheritanceApplied;
   }
}

package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportPlugin implements Serializable {
   private String groupId = "org.apache.maven.plugins";
   private String artifactId;
   private String version;
   private String inherited;
   private Object configuration;
   private List<ReportSet> reportSets;
   private Map reportSetMap = null;
   private boolean inheritanceApplied = true;

   public void addReportSet(ReportSet reportSet) {
      if (!(reportSet instanceof ReportSet)) {
         throw new ClassCastException("ReportPlugin.addReportSets(reportSet) parameter must be instanceof " + ReportSet.class.getName());
      } else {
         this.getReportSets().add(reportSet);
      }
   }

   public String getArtifactId() {
      return this.artifactId;
   }

   public Object getConfiguration() {
      return this.configuration;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getInherited() {
      return this.inherited;
   }

   public List<ReportSet> getReportSets() {
      if (this.reportSets == null) {
         this.reportSets = new ArrayList();
      }

      return this.reportSets;
   }

   public String getVersion() {
      return this.version;
   }

   public void removeReportSet(ReportSet reportSet) {
      if (!(reportSet instanceof ReportSet)) {
         throw new ClassCastException("ReportPlugin.removeReportSets(reportSet) parameter must be instanceof " + ReportSet.class.getName());
      } else {
         this.getReportSets().remove(reportSet);
      }
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public void setConfiguration(Object configuration) {
      this.configuration = configuration;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public void setInherited(String inherited) {
      this.inherited = inherited;
   }

   public void setReportSets(List<ReportSet> reportSets) {
      this.reportSets = reportSets;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public void flushReportSetMap() {
      this.reportSetMap = null;
   }

   public Map getReportSetsAsMap() {
      if (this.reportSetMap == null) {
         this.reportSetMap = new LinkedHashMap();
         if (this.getReportSets() != null) {
            Iterator i = this.getReportSets().iterator();

            while(i.hasNext()) {
               ReportSet reportSet = (ReportSet)i.next();
               this.reportSetMap.put(reportSet.getId(), reportSet);
            }
         }
      }

      return this.reportSetMap;
   }

   public String getKey() {
      return constructKey(this.groupId, this.artifactId);
   }

   public static String constructKey(String groupId, String artifactId) {
      return groupId + ":" + artifactId;
   }

   public void unsetInheritanceApplied() {
      this.inheritanceApplied = false;
   }

   public boolean isInheritanceApplied() {
      return this.inheritanceApplied;
   }

   public boolean equals(Object other) {
      if (other instanceof ReportPlugin) {
         ReportPlugin otherPlugin = (ReportPlugin)other;
         return this.getKey().equals(otherPlugin.getKey());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.getKey().hashCode();
   }

   public String toString() {
      return "ReportPlugin [" + this.getKey() + "]";
   }
}

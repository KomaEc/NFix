package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Reporting implements Serializable {
   private Boolean excludeDefaultsValue;
   private String outputDirectory;
   private List<ReportPlugin> plugins;
   Map reportPluginMap;

   public void addPlugin(ReportPlugin reportPlugin) {
      if (!(reportPlugin instanceof ReportPlugin)) {
         throw new ClassCastException("Reporting.addPlugins(reportPlugin) parameter must be instanceof " + ReportPlugin.class.getName());
      } else {
         this.getPlugins().add(reportPlugin);
      }
   }

   public String getOutputDirectory() {
      return this.outputDirectory;
   }

   public List<ReportPlugin> getPlugins() {
      if (this.plugins == null) {
         this.plugins = new ArrayList();
      }

      return this.plugins;
   }

   public Boolean isExcludeDefaultsValue() {
      return this.excludeDefaultsValue;
   }

   public void removePlugin(ReportPlugin reportPlugin) {
      if (!(reportPlugin instanceof ReportPlugin)) {
         throw new ClassCastException("Reporting.removePlugins(reportPlugin) parameter must be instanceof " + ReportPlugin.class.getName());
      } else {
         this.getPlugins().remove(reportPlugin);
      }
   }

   public void setExcludeDefaultsValue(Boolean excludeDefaultsValue) {
      this.excludeDefaultsValue = excludeDefaultsValue;
   }

   public void setOutputDirectory(String outputDirectory) {
      this.outputDirectory = outputDirectory;
   }

   public void setPlugins(List<ReportPlugin> plugins) {
      this.plugins = plugins;
   }

   public void flushReportPluginMap() {
      this.reportPluginMap = null;
   }

   public Map getReportPluginsAsMap() {
      if (this.reportPluginMap == null) {
         this.reportPluginMap = new LinkedHashMap();
         if (this.getPlugins() != null) {
            Iterator it = this.getPlugins().iterator();

            while(it.hasNext()) {
               ReportPlugin reportPlugin = (ReportPlugin)it.next();
               this.reportPluginMap.put(reportPlugin.getKey(), reportPlugin);
            }
         }
      }

      return this.reportPluginMap;
   }

   public boolean isExcludeDefaults() {
      return this.excludeDefaultsValue != null ? this.excludeDefaultsValue : false;
   }

   public void setExcludeDefaults(boolean excludeDefaults) {
      this.excludeDefaultsValue = excludeDefaults ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setExcludeDefaultsValue(String excludeDefaults) {
      this.excludeDefaultsValue = excludeDefaults != null ? Boolean.valueOf(excludeDefaults) : null;
   }
}

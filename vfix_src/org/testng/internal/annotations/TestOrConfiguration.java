package org.testng.internal.annotations;

import org.testng.annotations.ITestOrConfiguration;

public class TestOrConfiguration extends BaseAnnotation implements ITestOrConfiguration {
   private String[] m_parameters = new String[0];
   private String[] m_groups = new String[0];
   private boolean m_enabled = true;
   private String[] m_dependsOnGroups = new String[0];
   private String[] m_dependsOnMethods = new String[0];
   private String m_description = "";
   private int m_priority;
   private long m_timeOut = 0L;

   public String[] getGroups() {
      return this.m_groups;
   }

   public boolean getEnabled() {
      return this.m_enabled;
   }

   public void setDependsOnGroups(String[] dependsOnGroups) {
      this.m_dependsOnGroups = dependsOnGroups;
   }

   public void setDependsOnMethods(String[] dependsOnMethods) {
      this.m_dependsOnMethods = dependsOnMethods;
   }

   public void setGroups(String[] groups) {
      this.m_groups = groups;
   }

   public String getDescription() {
      return this.m_description;
   }

   public void setEnabled(boolean enabled) {
      this.m_enabled = enabled;
   }

   public String[] getDependsOnGroups() {
      return this.m_dependsOnGroups;
   }

   public String[] getDependsOnMethods() {
      return this.m_dependsOnMethods;
   }

   public String[] getParameters() {
      return this.m_parameters;
   }

   public void setParameters(String[] parameters) {
      this.m_parameters = parameters;
   }

   public void setDescription(String description) {
      this.m_description = description;
   }

   public int getPriority() {
      return this.m_priority;
   }

   public void setPriority(int priority) {
      this.m_priority = priority;
   }

   public void setTimeOut(long timeOut) {
      this.m_timeOut = timeOut;
   }

   public long getTimeOut() {
      return this.m_timeOut;
   }
}

package org.testng.internal.annotations;

public class BaseBeforeAfter extends TestOrConfiguration implements IBaseBeforeAfter {
   private String[] m_parameters = new String[0];
   private boolean m_alwaysRun = false;
   private boolean m_inheritGroups = true;
   private String[] m_beforeGroups = new String[0];
   private String[] m_afterGroups = new String[0];
   private String m_description;

   public String getDescription() {
      return this.m_description;
   }

   public void setDescription(String description) {
      this.m_description = description;
   }

   public void setAlwaysRun(boolean alwaysRun) {
      this.m_alwaysRun = alwaysRun;
   }

   public void setInheritGroups(boolean inheritGroups) {
      this.m_inheritGroups = inheritGroups;
   }

   public void setParameters(String[] parameters) {
      this.m_parameters = parameters;
   }

   public String[] getParameters() {
      return this.m_parameters;
   }

   public boolean getAlwaysRun() {
      return this.m_alwaysRun;
   }

   public boolean getInheritGroups() {
      return this.m_inheritGroups;
   }

   public String[] getAfterGroups() {
      return this.m_afterGroups;
   }

   public void setAfterGroups(String[] afterGroups) {
      this.m_afterGroups = afterGroups;
   }

   public String[] getBeforeGroups() {
      return this.m_beforeGroups;
   }

   public void setBeforeGroups(String[] beforeGroups) {
      this.m_beforeGroups = beforeGroups;
   }
}

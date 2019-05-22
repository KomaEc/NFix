package org.testng.internal.annotations;

import org.testng.annotations.IConfigurationAnnotation;

public class ConfigurationAnnotation extends TestOrConfiguration implements IConfigurationAnnotation, IBeforeSuite, IAfterSuite, IBeforeTest, IAfterTest, IBeforeGroups, IAfterGroups, IBeforeClass, IAfterClass, IBeforeMethod, IAfterMethod {
   private boolean m_beforeTestClass = false;
   private boolean m_afterTestClass = false;
   private boolean m_beforeTestMethod = false;
   private boolean m_afterTestMethod = false;
   private boolean m_beforeTest = false;
   private boolean m_afterTest = false;
   private boolean m_beforeSuite = false;
   private boolean m_afterSuite = false;
   private String[] m_parameters = new String[0];
   private boolean m_alwaysRun = false;
   private boolean m_inheritGroups = true;
   private String[] m_beforeGroups = new String[0];
   private String[] m_afterGroups = new String[0];
   private boolean m_isFakeConfiguration;
   private boolean m_firstTimeOnly = false;
   private boolean m_lastTimeOnly = false;

   public void setAfterSuite(boolean afterSuite) {
      this.m_afterSuite = afterSuite;
   }

   public void setAfterTest(boolean afterTest) {
      this.m_afterTest = afterTest;
   }

   public void setAfterTestClass(boolean afterTestClass) {
      this.m_afterTestClass = afterTestClass;
   }

   public void setAfterTestMethod(boolean afterTestMethod) {
      this.m_afterTestMethod = afterTestMethod;
   }

   public void setAlwaysRun(boolean alwaysRun) {
      this.m_alwaysRun = alwaysRun;
   }

   public void setBeforeSuite(boolean beforeSuite) {
      this.m_beforeSuite = beforeSuite;
   }

   public void setBeforeTest(boolean beforeTest) {
      this.m_beforeTest = beforeTest;
   }

   public void setBeforeTestClass(boolean beforeTestClass) {
      this.m_beforeTestClass = beforeTestClass;
   }

   public void setBeforeTestMethod(boolean beforeTestMethod) {
      this.m_beforeTestMethod = beforeTestMethod;
   }

   public void setInheritGroups(boolean inheritGroups) {
      this.m_inheritGroups = inheritGroups;
   }

   public void setParameters(String[] parameters) {
      this.m_parameters = parameters;
   }

   public boolean getBeforeTestClass() {
      return this.m_beforeTestClass;
   }

   public boolean getAfterTestClass() {
      return this.m_afterTestClass;
   }

   public boolean getBeforeTestMethod() {
      return this.m_beforeTestMethod;
   }

   public boolean getAfterTestMethod() {
      return this.m_afterTestMethod;
   }

   public boolean getBeforeSuite() {
      return this.m_beforeSuite;
   }

   public boolean getAfterSuite() {
      return this.m_afterSuite;
   }

   public boolean getBeforeTest() {
      return this.m_beforeTest;
   }

   public boolean getAfterTest() {
      return this.m_afterTest;
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

   public void setFakeConfiguration(boolean b) {
      this.m_isFakeConfiguration = b;
   }

   public boolean isFakeConfiguration() {
      return this.m_isFakeConfiguration;
   }

   public void setFirstTimeOnly(boolean f) {
      this.m_firstTimeOnly = f;
   }

   public boolean isFirstTimeOnly() {
      return this.m_firstTimeOnly;
   }

   public void setLastTimeOnly(boolean f) {
      this.m_lastTimeOnly = f;
   }

   public boolean isLastTimeOnly() {
      return this.m_lastTimeOnly;
   }
}

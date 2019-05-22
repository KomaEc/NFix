package org.testng.annotations;

public interface IConfigurationAnnotation extends ITestOrConfiguration {
   boolean getBeforeTestClass();

   boolean getAfterTestClass();

   boolean getBeforeTestMethod();

   boolean getAfterTestMethod();

   boolean getBeforeSuite();

   boolean getAfterSuite();

   boolean getBeforeTest();

   boolean getAfterTest();

   boolean getAlwaysRun();

   boolean getInheritGroups();

   String[] getBeforeGroups();

   String[] getAfterGroups();

   boolean isFakeConfiguration();
}

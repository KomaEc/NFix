package org.testng;

import org.testng.xml.XmlTest;

public interface ITestMethodFinder {
   ITestNGMethod[] getTestMethods(Class<?> var1, XmlTest var2);

   ITestNGMethod[] getBeforeTestMethods(Class<?> var1);

   ITestNGMethod[] getAfterTestMethods(Class<?> var1);

   ITestNGMethod[] getBeforeClassMethods(Class<?> var1);

   ITestNGMethod[] getAfterClassMethods(Class<?> var1);

   ITestNGMethod[] getBeforeSuiteMethods(Class<?> var1);

   ITestNGMethod[] getAfterSuiteMethods(Class<?> var1);

   ITestNGMethod[] getBeforeTestConfigurationMethods(Class<?> var1);

   ITestNGMethod[] getAfterTestConfigurationMethods(Class<?> var1);

   ITestNGMethod[] getBeforeGroupsConfigurationMethods(Class<?> var1);

   ITestNGMethod[] getAfterGroupsConfigurationMethods(Class<?> var1);
}

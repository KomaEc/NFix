package org.testng;

import java.io.Serializable;

public interface ITestClass extends IClass, Serializable {
   Object[] getInstances(boolean var1);

   long[] getInstanceHashCodes();

   int getInstanceCount();

   ITestNGMethod[] getTestMethods();

   ITestNGMethod[] getBeforeTestMethods();

   ITestNGMethod[] getAfterTestMethods();

   ITestNGMethod[] getBeforeClassMethods();

   ITestNGMethod[] getAfterClassMethods();

   ITestNGMethod[] getBeforeSuiteMethods();

   ITestNGMethod[] getAfterSuiteMethods();

   ITestNGMethod[] getBeforeTestConfigurationMethods();

   ITestNGMethod[] getAfterTestConfigurationMethods();

   ITestNGMethod[] getBeforeGroupsMethods();

   ITestNGMethod[] getAfterGroupsMethods();
}

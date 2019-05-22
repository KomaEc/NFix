package org.testng;

public interface ITestClassFinder {
   IClass[] findTestClasses();

   IClass getIClass(Class var1);
}

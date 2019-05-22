package org.testng.internal.annotations;

import org.testng.annotations.ITestOrConfiguration;

public interface IBaseBeforeAfter extends ITestOrConfiguration {
   boolean getEnabled();

   String[] getGroups();

   String[] getDependsOnGroups();

   String[] getDependsOnMethods();

   boolean getAlwaysRun();

   boolean getInheritGroups();

   String getDescription();
}

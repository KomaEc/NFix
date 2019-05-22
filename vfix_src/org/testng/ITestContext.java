package org.testng;

import com.google.inject.Injector;
import com.google.inject.Module;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.testng.xml.XmlTest;

public interface ITestContext extends IAttributes {
   String getName();

   Date getStartDate();

   Date getEndDate();

   IResultMap getPassedTests();

   IResultMap getSkippedTests();

   IResultMap getFailedButWithinSuccessPercentageTests();

   IResultMap getFailedTests();

   String[] getIncludedGroups();

   String[] getExcludedGroups();

   String getOutputDirectory();

   ISuite getSuite();

   ITestNGMethod[] getAllTestMethods();

   String getHost();

   Collection<ITestNGMethod> getExcludedMethods();

   IResultMap getPassedConfigurations();

   IResultMap getSkippedConfigurations();

   IResultMap getFailedConfigurations();

   XmlTest getCurrentXmlTest();

   List<Module> getGuiceModules(Class<? extends Module> var1);

   Injector getInjector(List<Module> var1);

   Injector getInjector(IClass var1);

   void addInjector(List<Module> var1, Injector var2);
}

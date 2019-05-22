package org.testng;

import com.google.inject.Injector;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlSuite;

public interface ISuite extends IAttributes {
   String getName();

   Map<String, ISuiteResult> getResults();

   IObjectFactory getObjectFactory();

   IObjectFactory2 getObjectFactory2();

   String getOutputDirectory();

   String getParallel();

   String getParentModule();

   String getGuiceStage();

   String getParameter(String var1);

   Map<String, Collection<ITestNGMethod>> getMethodsByGroups();

   /** @deprecated */
   @Deprecated
   Collection<ITestNGMethod> getInvokedMethods();

   List<IInvokedMethod> getAllInvokedMethods();

   Collection<ITestNGMethod> getExcludedMethods();

   void run();

   String getHost();

   SuiteRunState getSuiteState();

   IAnnotationFinder getAnnotationFinder();

   XmlSuite getXmlSuite();

   void addListener(ITestNGListener var1);

   Injector getParentInjector();

   void setParentInjector(Injector var1);

   List<ITestNGMethod> getAllMethods();
}

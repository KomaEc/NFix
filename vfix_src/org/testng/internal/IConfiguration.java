package org.testng.internal;

import java.util.List;
import org.testng.IConfigurable;
import org.testng.IConfigurationListener;
import org.testng.IExecutionListener;
import org.testng.IHookable;
import org.testng.ITestObjectFactory;
import org.testng.internal.annotations.IAnnotationFinder;

public interface IConfiguration {
   IAnnotationFinder getAnnotationFinder();

   void setAnnotationFinder(IAnnotationFinder var1);

   ITestObjectFactory getObjectFactory();

   void setObjectFactory(ITestObjectFactory var1);

   IHookable getHookable();

   void setHookable(IHookable var1);

   IConfigurable getConfigurable();

   void setConfigurable(IConfigurable var1);

   List<IExecutionListener> getExecutionListeners();

   void addExecutionListener(IExecutionListener var1);

   List<IConfigurationListener> getConfigurationListeners();

   void addConfigurationListener(IConfigurationListener var1);
}

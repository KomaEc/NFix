package org.testng.internal;

import java.util.Collection;
import java.util.List;
import org.testng.IConfigurable;
import org.testng.IConfigurationListener;
import org.testng.IExecutionListener;
import org.testng.IHookable;
import org.testng.ITestObjectFactory;
import org.testng.collections.Lists;
import org.testng.internal.annotations.DefaultAnnotationTransformer;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.annotations.JDK15AnnotationFinder;

public class Configuration implements IConfiguration {
   IAnnotationFinder m_annotationFinder;
   ITestObjectFactory m_objectFactory;
   IHookable m_hookable;
   IConfigurable m_configurable;
   List<IExecutionListener> m_executionListeners = Lists.newArrayList();
   private List<IConfigurationListener> m_configurationListeners = Lists.newArrayList();

   public Configuration() {
      this.init(new JDK15AnnotationFinder(new DefaultAnnotationTransformer()));
   }

   public Configuration(IAnnotationFinder finder) {
      this.init(finder);
   }

   private void init(IAnnotationFinder finder) {
      this.m_annotationFinder = finder;
   }

   public IAnnotationFinder getAnnotationFinder() {
      return this.m_annotationFinder;
   }

   public void setAnnotationFinder(IAnnotationFinder finder) {
      this.m_annotationFinder = finder;
   }

   public ITestObjectFactory getObjectFactory() {
      return this.m_objectFactory;
   }

   public void setObjectFactory(ITestObjectFactory factory) {
      this.m_objectFactory = factory;
   }

   public IHookable getHookable() {
      return this.m_hookable;
   }

   public void setHookable(IHookable h) {
      this.m_hookable = h;
   }

   public IConfigurable getConfigurable() {
      return this.m_configurable;
   }

   public void setConfigurable(IConfigurable c) {
      this.m_configurable = c;
   }

   public List<IExecutionListener> getExecutionListeners() {
      return this.m_executionListeners;
   }

   public void addExecutionListener(IExecutionListener l) {
      this.m_executionListeners.add(l);
   }

   public List<IConfigurationListener> getConfigurationListeners() {
      return Lists.newArrayList((Collection)this.m_configurationListeners);
   }

   public void addConfigurationListener(IConfigurationListener cl) {
      if (!this.m_configurationListeners.contains(cl)) {
         this.m_configurationListeners.add(cl);
      }

   }
}

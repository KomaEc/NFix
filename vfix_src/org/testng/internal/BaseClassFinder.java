package org.testng.internal;

import java.util.Map;
import org.testng.IClass;
import org.testng.ITestClassFinder;
import org.testng.ITestContext;
import org.testng.ITestObjectFactory;
import org.testng.collections.Maps;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

public abstract class BaseClassFinder implements ITestClassFinder {
   private Map<Class, IClass> m_classes = Maps.newLinkedHashMap();

   public IClass getIClass(Class cls) {
      return (IClass)this.m_classes.get(cls);
   }

   protected void putIClass(Class cls, IClass iClass) {
      if (!this.m_classes.containsKey(cls)) {
         this.m_classes.put(cls, iClass);
      }

   }

   private void ppp(String s) {
      System.out.println("[BaseClassFinder] " + s);
   }

   protected IClass findOrCreateIClass(ITestContext context, Class cls, XmlClass xmlClass, Object instance, XmlTest xmlTest, IAnnotationFinder annotationFinder, ITestObjectFactory objectFactory) {
      IClass result = (IClass)this.m_classes.get(cls);
      if (null == result) {
         result = new ClassImpl(context, cls, xmlClass, instance, this.m_classes, xmlTest, annotationFinder, objectFactory);
         this.m_classes.put(cls, result);
      }

      return (IClass)result;
   }

   protected Map getExistingClasses() {
      return this.m_classes;
   }

   protected boolean classExists(Class cls) {
      return this.m_classes.containsKey(cls);
   }

   public IClass[] findTestClasses() {
      return (IClass[])this.m_classes.values().toArray(new IClass[this.m_classes.size()]);
   }
}

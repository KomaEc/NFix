package org.testng.internal;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestNGException;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlTest;

public class FactoryMethod extends BaseTestMethod {
   private static final long serialVersionUID = -7329918821346197099L;
   private Object m_instance = null;
   private XmlTest m_xmlTest = null;
   private ITestContext m_testContext = null;

   public FactoryMethod(ConstructorOrMethod com, Object instance, XmlTest xmlTest, IAnnotationFinder annotationFinder, ITestContext testContext) {
      super(com.getName(), com, annotationFinder, instance);
      Class<?> declaringClass = com.getDeclaringClass();
      if (instance != null && !declaringClass.isAssignableFrom(instance.getClass())) {
         throw new TestNGException("Mismatch between instance/method classes:" + instance.getClass() + " " + declaringClass);
      } else {
         this.m_instance = instance;
         this.m_xmlTest = xmlTest;
         this.m_testContext = testContext;
         NoOpTestClass tc = new NoOpTestClass();
         tc.setTestClass(declaringClass);
         this.m_testClass = tc;
      }
   }

   private static void ppp(String s) {
      System.out.println("[FactoryMethod] " + s);
   }

   public Object[] invoke() {
      List<Object> result = Lists.newArrayList();
      Map<String, String> allParameterNames = Maps.newHashMap();
      Iterator parameterIterator = Parameters.handleParameters(this, allParameterNames, this.m_instance, new Parameters.MethodParameters(this.m_xmlTest.getAllParameters(), this.findMethodParameters(this.m_xmlTest), (Object[])null, (Method)null, this.m_testContext, (ITestResult)null), this.m_xmlTest.getSuite(), this.m_annotationFinder, (Object)null).parameters;

      try {
         while(parameterIterator.hasNext()) {
            Object[] parameters = (Object[])parameterIterator.next();
            ConstructorOrMethod com = this.getConstructorOrMethod();
            if (com.getMethod() != null) {
               Object[] testInstances = (Object[])((Object[])this.getMethod().invoke(this.m_instance, parameters));
               Object[] arr$ = testInstances;
               int len$ = testInstances.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  Object testInstance = arr$[i$];
                  result.add(testInstance);
               }
            } else {
               Object instance = com.getConstructor().newInstance(parameters);
               result.add(instance);
            }
         }
      } catch (Throwable var11) {
         ConstructorOrMethod com = this.getConstructorOrMethod();
         throw new TestNGException("The factory method " + com.getDeclaringClass() + "." + com.getName() + "() threw an exception", var11);
      }

      return result.toArray(new Object[result.size()]);
   }

   public ITestNGMethod clone() {
      throw new IllegalStateException("clone is not supported for FactoryMethod");
   }
}

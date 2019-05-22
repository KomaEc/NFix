package org.codehaus.groovy.vmplugin.v5;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyRuntimeException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.codehaus.groovy.runtime.InvokerHelper;

public class TestNgUtils {
   static Boolean realIsTestNgTest(Class scriptClass, GroovyClassLoader loader) {
      boolean isTest = false;

      try {
         try {
            Class testAnnotationClass = loader.loadClass("org.testng.annotations.Test");
            Annotation annotation = scriptClass.getAnnotation(testAnnotationClass);
            if (annotation != null) {
               isTest = true;
            } else {
               Method[] methods = scriptClass.getMethods();

               for(int i = 0; i < methods.length; ++i) {
                  Method method = methods[i];
                  annotation = method.getAnnotation(testAnnotationClass);
                  if (annotation != null) {
                     isTest = true;
                     break;
                  }
               }
            }
         } catch (ClassNotFoundException var8) {
         }
      } catch (Throwable var9) {
      }

      return isTest ? Boolean.TRUE : Boolean.FALSE;
   }

   static Object realRunTestNgTest(Class scriptClass, GroovyClassLoader loader) {
      try {
         Class testNGClass = loader.loadClass("org.testng.TestNG");
         Object testng = InvokerHelper.invokeConstructorOf((Class)testNGClass, new Object[0]);
         InvokerHelper.invokeMethod(testng, "setTestClasses", new Object[]{scriptClass});
         Class listenerClass = loader.loadClass("org.testng.TestListenerAdapter");
         Object listener = InvokerHelper.invokeConstructorOf((Class)listenerClass, new Object[0]);
         InvokerHelper.invokeMethod(testng, "addListener", new Object[]{listener});
         Object result = InvokerHelper.invokeMethod(testng, "run", new Object[0]);
         return result;
      } catch (ClassNotFoundException var7) {
         throw new GroovyRuntimeException("Error running TestNG test.", var7);
      }
   }
}

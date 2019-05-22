package org.codehaus.groovy.vmplugin.v5;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyRuntimeException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import org.codehaus.groovy.runtime.InvokerHelper;

public class JUnit4Utils {
   static Boolean realIsJUnit4Test(Class scriptClass, GroovyClassLoader loader) {
      boolean isTest = false;

      try {
         try {
            Class runWithAnnotationClass = loader.loadClass("org.junit.runner.RunWith");
            Annotation annotation = scriptClass.getAnnotation(runWithAnnotationClass);
            if (annotation != null) {
               isTest = true;
            } else {
               Class testAnnotationClass = loader.loadClass("org.junit.Test");
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
         } catch (ClassNotFoundException var9) {
         }
      } catch (Throwable var10) {
      }

      return isTest ? Boolean.TRUE : Boolean.FALSE;
   }

   static Object realRunJUnit4Test(Class scriptClass, GroovyClassLoader loader) {
      try {
         Class junitCoreClass = loader.loadClass("org.junit.runner.JUnitCore");
         Object result = InvokerHelper.invokeStaticMethod((Class)junitCoreClass, "runClasses", new Object[]{scriptClass});
         System.out.print("JUnit 4 Runner, Tests: " + InvokerHelper.getProperty(result, "runCount"));
         System.out.print(", Failures: " + InvokerHelper.getProperty(result, "failureCount"));
         System.out.println(", Time: " + InvokerHelper.getProperty(result, "runTime"));
         List failures = (List)InvokerHelper.getProperty(result, "failures");

         for(int i = 0; i < failures.size(); ++i) {
            Object f = failures.get(i);
            System.out.println("Test Failure: " + InvokerHelper.getProperty(f, "description"));
            System.out.println(InvokerHelper.getProperty(f, "trace"));
         }

         return result;
      } catch (ClassNotFoundException var7) {
         throw new GroovyRuntimeException("Error running JUnit 4 test.", var7);
      }
   }
}

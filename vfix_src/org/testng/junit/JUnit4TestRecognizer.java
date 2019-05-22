package org.testng.junit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.Test;
import org.junit.runner.RunWith;

public final class JUnit4TestRecognizer implements JUnitTestRecognizer {
   public boolean isTest(Class c) {
      Annotation[] arr$ = c.getAnnotations();
      int len$ = arr$.length;

      int len$;
      for(len$ = 0; len$ < len$; ++len$) {
         Annotation an = arr$[len$];
         if (RunWith.class.isAssignableFrom(an.annotationType())) {
            return true;
         }
      }

      boolean haveTest = false;
      Method[] arr$ = c.getMethods();
      len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Method m = arr$[i$];
         Annotation[] arr$ = m.getDeclaredAnnotations();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Annotation a = arr$[i$];
            if (Test.class.isAssignableFrom(a.annotationType())) {
               haveTest = true;
               break;
            }
         }
      }

      return haveTest;
   }
}

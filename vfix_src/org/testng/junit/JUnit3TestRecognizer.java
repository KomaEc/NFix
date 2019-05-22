package org.testng.junit;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import junit.framework.Test;

public class JUnit3TestRecognizer implements JUnitTestRecognizer {
   public boolean isTest(Class c) {
      if (Test.class.isAssignableFrom(c)) {
         boolean haveTest = false;
         Method[] arr$ = c.getMethods();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method m = arr$[i$];
            if (m.getName().startsWith("test")) {
               haveTest = true;
               break;
            }
         }

         if (haveTest) {
            return true;
         }
      }

      try {
         Method m = c.getDeclaredMethod("suite");
         return Modifier.isPublic(m.getModifiers()) && Modifier.isStatic(m.getModifiers()) ? m.getReturnType().isAssignableFrom(Test.class) : false;
      } catch (Throwable var7) {
         return false;
      }
   }
}

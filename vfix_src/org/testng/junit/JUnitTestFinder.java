package org.testng.junit;

import java.lang.reflect.Modifier;
import org.testng.internal.Utils;

public final class JUnitTestFinder {
   private static final String JUNIT3_TEST = "junit.framework.Test";
   private static final String JUNIT3_FINDER = "org.testng.junit.JUnit3TestRecognizer";
   private static final String JUNIT4_TEST = "org.junit.Test";
   private static final String JUNIT4_FINDER = "org.testng.junit.JUnit4TestRecognizer";
   private static final JUnitTestRecognizer junit3 = getJUnitTestRecognizer("junit.framework.Test", "org.testng.junit.JUnit3TestRecognizer");
   private static final JUnitTestRecognizer junit4 = getJUnitTestRecognizer("org.junit.Test", "org.testng.junit.JUnit4TestRecognizer");

   public static boolean isJUnitTest(Class c) {
      if (!haveJUnit()) {
         return false;
      } else if (Modifier.isPublic(c.getModifiers()) && !c.isInterface() && !c.isAnnotation() && !c.isEnum()) {
         return junit3 != null && junit3.isTest(c) || junit4 != null && junit4.isTest(c);
      } else {
         return false;
      }
   }

   private static boolean haveJUnit() {
      return junit3 != null || junit4 != null;
   }

   private static JUnitTestRecognizer getJUnitTestRecognizer(String test, String name) {
      try {
         Class.forName(test);
         Class<JUnitTestRecognizer> c = Class.forName(name);
         return (JUnitTestRecognizer)c.newInstance();
      } catch (Throwable var3) {
         return null;
      }
   }

   static {
      if (junit3 == null) {
         Utils.log("JUnitTestFinder", 2, "JUnit3 was not found on the classpath");
      }

      if (junit4 == null) {
         Utils.log("JUnitTestFinder", 2, "JUnit4 was not found on the classpath");
      }

   }
}

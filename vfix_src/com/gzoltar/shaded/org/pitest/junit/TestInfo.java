package com.gzoltar.shaded.org.pitest.junit;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

abstract class TestInfo {
   public static boolean isWithinATestClass(ClassInfo clazz) {
      Option<ClassInfo> outerClass = clazz.getOuterClass();
      return isATest(clazz) || outerClass.hasSome() && isATest((ClassInfo)outerClass.value());
   }

   private static boolean isATest(ClassInfo clazz) {
      return isJUnit3Test(clazz) || isJUnit4Test(clazz) || isATest(clazz.getSuperClass());
   }

   private static boolean isATest(Option<ClassInfo> clazz) {
      return clazz.hasSome() ? isATest((ClassInfo)clazz.value()) : false;
   }

   public static Predicate<ClassInfo> isATest() {
      return new Predicate<ClassInfo>() {
         public Boolean apply(ClassInfo clazz) {
            return TestInfo.isATest(clazz);
         }
      };
   }

   private static boolean isJUnit3Test(ClassInfo clazz) {
      return clazz.descendsFrom(TestCase.class) || clazz.descendsFrom(TestSuite.class);
   }

   private static boolean isJUnit4Test(ClassInfo clazz) {
      return clazz.hasAnnotation(RunWith.class) || clazz.hasAnnotation(Test.class);
   }
}

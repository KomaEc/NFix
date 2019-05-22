package com.gzoltar.shaded.org.pitest.testng;

import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.reflection.IsAnnotatedWith;
import com.gzoltar.shaded.org.pitest.reflection.Reflection;
import com.gzoltar.shaded.org.pitest.testapi.TestGroupConfig;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.testapi.TestUnitFinder;
import java.util.Collections;
import java.util.List;
import org.testng.annotations.Test;

public class TestNGTestUnitFinder implements TestUnitFinder {
   private final TestGroupConfig config;

   public TestNGTestUnitFinder(TestGroupConfig config) {
      this.config = config;
   }

   public List<TestUnit> findTestUnits(Class<?> clazz) {
      return !this.hasClassAnnotation(clazz) && !this.hasMethodAnnotation(clazz) ? Collections.emptyList() : Collections.singletonList(new TestNGTestUnit(clazz, this.config));
   }

   private boolean hasClassAnnotation(Class<?> clazz) {
      return clazz.getAnnotation(Test.class) != null;
   }

   private boolean hasMethodAnnotation(Class<?> clazz) {
      return FCollection.contains(Reflection.allMethods(clazz), IsAnnotatedWith.instance(Test.class));
   }
}

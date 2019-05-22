package com.gzoltar.shaded.org.pitest.junit;

import com.gzoltar.shaded.org.pitest.testapi.TestSuiteFinder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

public class JUnit4SuiteFinder implements TestSuiteFinder {
   public List<Class<?>> apply(Class<?> a) {
      Suite.SuiteClasses annotation = (Suite.SuiteClasses)a.getAnnotation(Suite.SuiteClasses.class);
      return annotation != null && this.hasSuitableRunnner(a) ? Arrays.asList(annotation.value()) : Collections.emptyList();
   }

   private boolean hasSuitableRunnner(Class<?> clazz) {
      RunWith runWith = (RunWith)clazz.getAnnotation(RunWith.class);
      return runWith != null ? runWith.value().equals(Suite.class) : false;
   }
}

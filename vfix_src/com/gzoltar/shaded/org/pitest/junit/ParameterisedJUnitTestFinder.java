package com.gzoltar.shaded.org.pitest.junit;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.junit.adapter.AdaptedJUnitTestUnit;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.testapi.TestUnitFinder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runners.Parameterized;

public class ParameterisedJUnitTestFinder implements TestUnitFinder {
   public List<TestUnit> findTestUnits(Class<?> clazz) {
      Runner runner = AdaptedJUnitTestUnit.createRunner(clazz);
      if (runner != null && !runner.getClass().isAssignableFrom(ErrorReportingRunner.class)) {
         return this.isParameterizedTest(runner) ? this.handleParameterizedTest(clazz, runner.getDescription()) : Collections.emptyList();
      } else {
         return Collections.emptyList();
      }
   }

   private List<TestUnit> handleParameterizedTest(Class<?> clazz, Description description) {
      List<TestUnit> result = new ArrayList();
      Iterator i$ = description.getChildren().iterator();

      while(i$.hasNext()) {
         Description each = (Description)i$.next();
         FCollection.mapTo(each.getChildren(), this.parameterizedToTestUnit(clazz), result);
      }

      return result;
   }

   private F<Description, TestUnit> parameterizedToTestUnit(final Class<?> clazz) {
      return new F<Description, TestUnit>() {
         public TestUnit apply(Description a) {
            return new AdaptedJUnitTestUnit(clazz, Option.some(new ParameterisedTestFilter(a.toString())));
         }
      };
   }

   private boolean isParameterizedTest(Runner runner) {
      return Parameterized.class.isAssignableFrom(runner.getClass());
   }
}

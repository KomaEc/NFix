package com.gzoltar.shaded.org.pitest.junit;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.junit.adapter.AdaptedJUnitTestUnit;
import com.gzoltar.shaded.org.pitest.testapi.TestSuiteFinder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.internal.runners.SuiteMethod;
import org.junit.runner.Description;
import org.junit.runner.Runner;

public class RunnerSuiteFinder implements TestSuiteFinder {
   public List<Class<?>> apply(Class<?> a) {
      Runner runner = AdaptedJUnitTestUnit.createRunner(a);
      List<Description> allChildren = new ArrayList();
      this.flattenChildren(allChildren, runner.getDescription());
      Set<Class<?>> classes = new LinkedHashSet(runner.getDescription().getChildren().size());
      List<Description> suites = FCollection.filter(allChildren, Prelude.or(isSuiteMethodRunner(runner), isSuite()));
      FCollection.flatMapTo(suites, descriptionToTestClass(), classes);
      classes.remove(a);
      return new ArrayList(classes);
   }

   private void flattenChildren(List<Description> allChildren, Description description) {
      Iterator i$ = description.getChildren().iterator();

      while(i$.hasNext()) {
         Description each = (Description)i$.next();
         allChildren.add(each);
         this.flattenChildren(allChildren, each);
      }

   }

   private static Predicate<Description> isSuiteMethodRunner(final Runner runner) {
      return new Predicate<Description>() {
         public Boolean apply(Description a) {
            return SuiteMethod.class.isAssignableFrom(runner.getClass());
         }
      };
   }

   private static F<Description, Option<Class<?>>> descriptionToTestClass() {
      return new F<Description, Option<Class<?>>>() {
         public Option<Class<?>> apply(Description a) {
            Class<?> clazz = a.getTestClass();
            return (Option)(clazz != null ? Option.some(clazz) : Option.none());
         }
      };
   }

   private static Predicate<Description> isSuite() {
      return new Predicate<Description>() {
         public Boolean apply(Description a) {
            return a.isSuite();
         }
      };
   }
}

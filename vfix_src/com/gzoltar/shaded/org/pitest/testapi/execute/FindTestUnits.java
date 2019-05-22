package com.gzoltar.shaded.org.pitest.testapi.execute;

import com.gzoltar.shaded.org.pitest.testapi.Configuration;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FindTestUnits {
   private final Configuration config;

   public FindTestUnits(Configuration config) {
      this.config = config;
   }

   public List<TestUnit> findTestUnitsForAllSuppliedClasses(Iterable<Class<?>> classes) {
      List<TestUnit> testUnits = new ArrayList();
      Iterator i$ = classes.iterator();

      while(i$.hasNext()) {
         Class<?> c = (Class)i$.next();
         Collection<TestUnit> testUnitsFromClass = this.getTestUnits(c);
         testUnits.addAll(testUnitsFromClass);
      }

      return testUnits;
   }

   private Collection<TestUnit> getTestUnits(Class<?> suiteClass) {
      List<TestUnit> tus = new ArrayList();
      Set<Class<?>> visitedClasses = new HashSet();
      this.findTestUnits(tus, visitedClasses, suiteClass);
      return tus;
   }

   private void findTestUnits(List<TestUnit> tus, Set<Class<?>> visitedClasses, Class<?> suiteClass) {
      visitedClasses.add(suiteClass);
      Collection<Class<?>> tcs = (Collection)this.config.testSuiteFinder().apply(suiteClass);
      Iterator i$ = tcs.iterator();

      while(i$.hasNext()) {
         Class<?> tc = (Class)i$.next();
         if (!visitedClasses.contains(tc)) {
            this.findTestUnits(tus, visitedClasses, tc);
         }
      }

      List<TestUnit> testsInThisClass = this.config.testUnitFinder().findTestUnits(suiteClass);
      if (!testsInThisClass.isEmpty()) {
         tus.addAll(testsInThisClass);
      }

   }
}

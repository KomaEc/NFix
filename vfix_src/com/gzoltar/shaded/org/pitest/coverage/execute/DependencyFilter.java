package com.gzoltar.shaded.org.pitest.coverage.execute;

import com.gzoltar.shaded.org.pitest.dependency.DependencyExtractor;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DependencyFilter {
   private final DependencyExtractor analyser;
   private final Predicate<String> filter;

   DependencyFilter(DependencyExtractor analyser, Predicate<String> filter) {
      this.analyser = analyser;
      this.filter = filter;
   }

   List<TestUnit> filterTestsByDependencyAnalysis(List<TestUnit> tus) {
      return (List)(this.analyser.getMaxDistance() < 0 ? tus : FCollection.filter(tus, this.isWithinReach()));
   }

   private F<TestUnit, Boolean> isWithinReach() {
      return new F<TestUnit, Boolean>() {
         private final Map<String, Boolean> cache = new HashMap();

         public Boolean apply(TestUnit testUnit) {
            String testClass = testUnit.getDescription().getFirstTestClass();

            try {
               if (this.cache.containsKey(testClass)) {
                  return (Boolean)this.cache.get(testClass);
               } else {
                  boolean inReach = !DependencyFilter.this.analyser.extractCallDependenciesForPackages(testClass, DependencyFilter.this.filter).isEmpty();
                  this.cache.put(testClass, inReach);
                  return inReach;
               }
            } catch (IOException var4) {
               throw Unchecked.translateCheckedException(var4);
            }
         }
      };
   }
}

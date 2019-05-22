package com.gzoltar.shaded.org.pitest.coverage.execute;

import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;

public class CoverageOptions {
   private final Predicate<String> filter;
   private final boolean verbose;
   private final Configuration pitConfig;
   private final int maxDependencyDistance;

   public CoverageOptions(Predicate<String> filter, Configuration pitConfig, boolean verbose, int maxDependencyDistance) {
      this.filter = filter;
      this.verbose = verbose;
      this.pitConfig = pitConfig;
      this.maxDependencyDistance = maxDependencyDistance;
   }

   public Predicate<String> getFilter() {
      return this.filter;
   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public Configuration getPitConfig() {
      return this.pitConfig;
   }

   public int getDependencyAnalysisMaxDistance() {
      return this.maxDependencyDistance;
   }
}

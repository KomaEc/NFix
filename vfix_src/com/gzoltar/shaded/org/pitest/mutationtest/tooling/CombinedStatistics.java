package com.gzoltar.shaded.org.pitest.mutationtest.tooling;

import com.gzoltar.shaded.org.pitest.coverage.CoverageSummary;
import com.gzoltar.shaded.org.pitest.mutationtest.statistics.MutationStatistics;

public class CombinedStatistics {
   private final MutationStatistics mutationStatistics;
   private final CoverageSummary coverageSummary;

   public CombinedStatistics(MutationStatistics mutationStatistics, CoverageSummary coverageSummary) {
      this.mutationStatistics = mutationStatistics;
      this.coverageSummary = coverageSummary;
   }

   public MutationStatistics getMutationStatistics() {
      return this.mutationStatistics;
   }

   public CoverageSummary getCoverageSummary() {
      return this.coverageSummary;
   }
}

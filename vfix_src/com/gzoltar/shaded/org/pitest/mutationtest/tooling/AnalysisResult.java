package com.gzoltar.shaded.org.pitest.mutationtest.tooling;

import com.gzoltar.shaded.org.pitest.functional.Option;

public final class AnalysisResult {
   private final Option<CombinedStatistics> statistics;
   private final Option<Exception> error;

   private AnalysisResult(CombinedStatistics statistics, Exception error) {
      this.statistics = Option.some(statistics);
      this.error = Option.some(error);
   }

   public static AnalysisResult success(CombinedStatistics statistics) {
      return new AnalysisResult(statistics, (Exception)null);
   }

   public static AnalysisResult fail(Exception error) {
      return new AnalysisResult((CombinedStatistics)null, error);
   }

   public Option<CombinedStatistics> getStatistics() {
      return this.statistics;
   }

   public Option<Exception> getError() {
      return this.error;
   }
}

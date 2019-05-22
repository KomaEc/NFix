package com.gzoltar.shaded.org.pitest.coverage;

public final class CoverageSummary {
   private final int numberOfLines;
   private final int numberOfCoveredLines;

   public CoverageSummary(int numberOfLines, int numberOfCoveredLines) {
      this.numberOfLines = numberOfLines;
      this.numberOfCoveredLines = numberOfCoveredLines;
   }

   public int getNumberOfLines() {
      return this.numberOfLines;
   }

   public int getNumberOfCoveredLines() {
      return this.numberOfCoveredLines;
   }

   public int getCoverage() {
      return this.numberOfLines == 0 ? 100 : Math.round(100.0F * (float)this.numberOfCoveredLines / (float)this.numberOfLines);
   }
}

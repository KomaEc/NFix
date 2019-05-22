package com.gzoltar.shaded.org.pitest.coverage;

import com.gzoltar.shaded.org.pitest.testapi.Description;
import java.util.Collection;

public class CoverageResult {
   private final Description testUnitDescription;
   private final int executionTime;
   private final Collection<BlockLocation> visitedBlocks;
   private final boolean greenSuite;

   public CoverageResult(Description testUnitDescription, int executionTime, boolean greenSuite, Collection<BlockLocation> visitedBlocks) {
      this.testUnitDescription = testUnitDescription;
      this.executionTime = executionTime;
      this.visitedBlocks = visitedBlocks;
      this.greenSuite = greenSuite;
   }

   public Description getTestUnitDescription() {
      return this.testUnitDescription;
   }

   public int getExecutionTime() {
      return this.executionTime;
   }

   public Collection<BlockLocation> getCoverage() {
      return this.visitedBlocks;
   }

   public boolean isGreenTest() {
      return this.greenSuite;
   }

   public int getNumberOfCoveredBlocks() {
      return this.visitedBlocks.size();
   }

   public String toString() {
      return "CoverageResult [testUnitDescription=" + this.testUnitDescription + ", executionTime=" + this.executionTime + ", coverage=" + this.visitedBlocks + ", greenSuite=" + this.greenSuite + "]";
   }
}

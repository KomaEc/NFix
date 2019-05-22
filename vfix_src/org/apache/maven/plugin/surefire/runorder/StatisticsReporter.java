package org.apache.maven.plugin.surefire.runorder;

import java.io.File;
import java.io.FileNotFoundException;
import org.apache.maven.surefire.report.ReportEntry;

public class StatisticsReporter {
   private final RunEntryStatisticsMap existing;
   private final RunEntryStatisticsMap newResults;
   private final File dataFile;

   public StatisticsReporter(File dataFile) {
      this.dataFile = dataFile;
      this.existing = RunEntryStatisticsMap.fromFile(this.dataFile);
      this.newResults = new RunEntryStatisticsMap();
   }

   public void testSetCompleted() {
      try {
         this.newResults.serialize(this.dataFile);
      } catch (FileNotFoundException var2) {
         throw new RuntimeException(var2);
      }
   }

   public void testSucceeded(ReportEntry report) {
      this.newResults.add(this.existing.createNextGeneration(report));
   }

   public void testSkipped(ReportEntry report) {
      this.newResults.add(this.existing.createNextGeneration(report));
   }

   public void testError(ReportEntry report) {
      this.newResults.add(this.existing.createNextGenerationFailure(report));
   }

   public void testFailed(ReportEntry report) {
      this.newResults.add(this.existing.createNextGenerationFailure(report));
   }
}

package org.apache.maven.plugin.surefire.report;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class TestSetStats {
   private final boolean trimStackTrace;
   private final boolean plainFormat;
   private long testSetStartAt;
   private long testStartAt;
   private int completedCount;
   private int errors;
   private int failures;
   private int skipped;
   private long lastStartAt;
   private long elapsedForTestSet;
   private final List<WrappedReportEntry> reportEntries = new ArrayList();
   private static final String TEST_SET_COMPLETED_PREFIX = "Tests run: ";
   private final NumberFormat numberFormat;
   private static final int MS_PER_SEC = 1000;

   public TestSetStats(boolean trimStackTrace, boolean plainFormat) {
      this.numberFormat = NumberFormat.getInstance(Locale.ENGLISH);
      this.trimStackTrace = trimStackTrace;
      this.plainFormat = plainFormat;
   }

   public int getElapsedSinceTestSetStart() {
      return (int)(System.currentTimeMillis() - this.testSetStartAt);
   }

   public int getElapsedSinceLastStart() {
      return (int)(System.currentTimeMillis() - this.lastStartAt);
   }

   public void testSetStart() {
      this.lastStartAt = this.testSetStartAt = System.currentTimeMillis();
   }

   public void testStart() {
      this.lastStartAt = this.testStartAt = System.currentTimeMillis();
   }

   private long finishTest(WrappedReportEntry reportEntry) {
      this.reportEntries.add(reportEntry);
      this.incrementCompletedCount();
      long testEndAt = System.currentTimeMillis();
      if (this.testStartAt == 0L) {
         this.testStartAt = testEndAt;
      }

      long elapsedForThis = reportEntry.getElapsed() != null ? (long)reportEntry.getElapsed() : testEndAt - this.testStartAt;
      this.elapsedForTestSet += elapsedForThis;
      return elapsedForThis;
   }

   public void testSucceeded(WrappedReportEntry reportEntry) {
      this.finishTest(reportEntry);
   }

   public void testError(WrappedReportEntry reportEntry) {
      ++this.errors;
      this.finishTest(reportEntry);
   }

   public void testFailure(WrappedReportEntry reportEntry) {
      ++this.failures;
      this.finishTest(reportEntry);
   }

   public void testSkipped(WrappedReportEntry reportEntry) {
      ++this.skipped;
      this.finishTest(reportEntry);
   }

   public void reset() {
      this.completedCount = 0;
      this.errors = 0;
      this.failures = 0;
      this.skipped = 0;
      this.elapsedForTestSet = 0L;
      Iterator i$ = this.reportEntries.iterator();

      while(i$.hasNext()) {
         WrappedReportEntry entry = (WrappedReportEntry)i$.next();
         entry.getStdout().free();
         entry.getStdErr().free();
      }

      this.reportEntries.clear();
   }

   public int getCompletedCount() {
      return this.completedCount;
   }

   public int getErrors() {
      return this.errors;
   }

   public int getFailures() {
      return this.failures;
   }

   public int getSkipped() {
      return this.skipped;
   }

   String elapsedTimeAsString(long runTime) {
      return this.numberFormat.format((double)runTime / 1000.0D);
   }

   public String getElapsedForTestSet() {
      return this.elapsedTimeAsString(this.elapsedForTestSet);
   }

   private void incrementCompletedCount() {
      ++this.completedCount;
   }

   public String getTestSetSummary(WrappedReportEntry reportEntry) {
      StringBuilder buf = new StringBuilder();
      buf.append("Tests run: ");
      buf.append(this.completedCount);
      buf.append(", Failures: ");
      buf.append(this.failures);
      buf.append(", Errors: ");
      buf.append(this.errors);
      buf.append(", Skipped: ");
      buf.append(this.skipped);
      buf.append(", Time elapsed: ");
      buf.append(reportEntry.elapsedTimeAsString());
      buf.append(" sec");
      if (this.failures > 0 || this.errors > 0) {
         buf.append(" <<< FAILURE!");
      }

      buf.append(" - in ");
      buf.append(reportEntry.getNameWithGroup());
      buf.append("\n");
      return buf.toString();
   }

   public List<String> getTestResults() {
      List<String> result = new ArrayList();
      Iterator i$ = this.reportEntries.iterator();

      while(true) {
         while(i$.hasNext()) {
            WrappedReportEntry testResult = (WrappedReportEntry)i$.next();
            if (testResult.isErrorOrFailure()) {
               result.add(testResult.getOutput(this.trimStackTrace));
            } else if (this.plainFormat && testResult.isSkipped()) {
               result.add(testResult.getName() + " skipped");
            } else if (this.plainFormat && testResult.isSucceeded()) {
               result.add(testResult.getElapsedTimeSummary());
            }
         }

         return result;
      }
   }

   public List<WrappedReportEntry> getReportEntries() {
      return this.reportEntries;
   }
}

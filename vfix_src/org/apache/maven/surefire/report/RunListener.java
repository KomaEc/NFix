package org.apache.maven.surefire.report;

public interface RunListener {
   void testSetStarting(ReportEntry var1);

   void testSetCompleted(ReportEntry var1);

   void testStarting(ReportEntry var1);

   void testSucceeded(ReportEntry var1);

   void testAssumptionFailure(ReportEntry var1);

   void testError(ReportEntry var1);

   void testFailed(ReportEntry var1);

   void testSkipped(ReportEntry var1);
}

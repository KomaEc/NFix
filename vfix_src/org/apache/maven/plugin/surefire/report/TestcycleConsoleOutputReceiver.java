package org.apache.maven.plugin.surefire.report;

import org.apache.maven.surefire.report.ConsoleOutputReceiver;
import org.apache.maven.surefire.report.ReportEntry;

public interface TestcycleConsoleOutputReceiver extends ConsoleOutputReceiver {
   void testSetStarting(ReportEntry var1);

   void testSetCompleted(ReportEntry var1);

   void close();
}

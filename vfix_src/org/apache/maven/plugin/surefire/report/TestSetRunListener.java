package org.apache.maven.plugin.surefire.report;

import java.io.IOException;
import java.util.List;
import org.apache.maven.plugin.surefire.runorder.StatisticsReporter;
import org.apache.maven.surefire.report.ConsoleLogger;
import org.apache.maven.surefire.report.ConsoleOutputReceiver;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.RunListener;
import org.apache.maven.surefire.report.RunStatistics;

public class TestSetRunListener implements RunListener, ConsoleOutputReceiver, ConsoleLogger {
   private final RunStatistics globalStatistics;
   private final TestSetStats detailsForThis;
   private Utf8RecodingDeferredFileOutputStream testStdOut = this.initDeferred("stdout");
   private Utf8RecodingDeferredFileOutputStream testStdErr = this.initDeferred("stderr");
   private final TestcycleConsoleOutputReceiver consoleOutputReceiver;
   private final boolean briefOrPlainFormat;
   private final StatelessXmlReporter simpleXMLReporter;
   private final ConsoleReporter consoleReporter;
   private final FileReporter fileReporter;
   private final StatisticsReporter statisticsReporter;

   private Utf8RecodingDeferredFileOutputStream initDeferred(String channel) {
      return new Utf8RecodingDeferredFileOutputStream(channel);
   }

   public TestSetRunListener(ConsoleReporter consoleReporter, FileReporter fileReporter, StatelessXmlReporter simpleXMLReporter, TestcycleConsoleOutputReceiver consoleOutputReceiver, StatisticsReporter statisticsReporter, RunStatistics globalStats, boolean trimStackTrace, boolean isPlainFormat, boolean briefOrPlainFormat) {
      this.consoleReporter = consoleReporter;
      this.fileReporter = fileReporter;
      this.statisticsReporter = statisticsReporter;
      this.simpleXMLReporter = simpleXMLReporter;
      this.consoleOutputReceiver = consoleOutputReceiver;
      this.briefOrPlainFormat = briefOrPlainFormat;
      this.detailsForThis = new TestSetStats(trimStackTrace, isPlainFormat);
      this.globalStatistics = globalStats;
   }

   public void info(String message) {
      if (this.consoleReporter != null) {
         this.consoleReporter.writeMessage(message);
      }

   }

   public void writeTestOutput(byte[] buf, int off, int len, boolean stdout) {
      try {
         if (stdout) {
            this.testStdOut.write(buf, off, len);
         } else {
            this.testStdErr.write(buf, off, len);
         }
      } catch (IOException var6) {
         throw new RuntimeException(var6);
      }

      this.consoleOutputReceiver.writeTestOutput(buf, off, len, stdout);
   }

   public void testSetStarting(ReportEntry report) {
      this.detailsForThis.testSetStart();
      if (this.consoleReporter != null) {
         this.consoleReporter.testSetStarting(report);
      }

      this.consoleOutputReceiver.testSetStarting(report);
   }

   public void clearCapture() {
      this.testStdOut = this.initDeferred("stdout");
      this.testStdErr = this.initDeferred("stderr");
   }

   public void testSetCompleted(ReportEntry report) {
      WrappedReportEntry wrap = this.wrapTestSet(report);
      List<String> testResults = this.briefOrPlainFormat ? this.detailsForThis.getTestResults() : null;
      if (this.fileReporter != null) {
         this.fileReporter.testSetCompleted(wrap, this.detailsForThis, testResults);
      }

      if (this.simpleXMLReporter != null) {
         this.simpleXMLReporter.testSetCompleted(wrap, this.detailsForThis);
      }

      if (this.statisticsReporter != null) {
         this.statisticsReporter.testSetCompleted();
      }

      if (this.consoleReporter != null) {
         this.consoleReporter.testSetCompleted(wrap, this.detailsForThis, testResults);
      }

      this.consoleOutputReceiver.testSetCompleted(wrap);
      if (this.consoleReporter != null) {
         this.consoleReporter.reset();
      }

      wrap.getStdout().free();
      wrap.getStdErr().free();
      this.globalStatistics.add(this.detailsForThis);
      this.detailsForThis.reset();
   }

   public void testStarting(ReportEntry report) {
      this.detailsForThis.testStart();
   }

   public void testSucceeded(ReportEntry reportEntry) {
      WrappedReportEntry wrapped = this.wrap(reportEntry, ReportEntryType.success);
      this.detailsForThis.testSucceeded(wrapped);
      if (this.statisticsReporter != null) {
         this.statisticsReporter.testSucceeded(reportEntry);
      }

      this.clearCapture();
   }

   public void testError(ReportEntry reportEntry) {
      WrappedReportEntry wrapped = this.wrap(reportEntry, ReportEntryType.error);
      this.detailsForThis.testError(wrapped);
      if (this.statisticsReporter != null) {
         this.statisticsReporter.testError(reportEntry);
      }

      this.globalStatistics.addErrorSource(reportEntry.getStackTraceWriter());
      this.clearCapture();
   }

   public void testFailed(ReportEntry reportEntry) {
      WrappedReportEntry wrapped = this.wrap(reportEntry, ReportEntryType.failure);
      this.detailsForThis.testFailure(wrapped);
      if (this.statisticsReporter != null) {
         this.statisticsReporter.testFailed(reportEntry);
      }

      this.globalStatistics.addFailureSource(reportEntry.getStackTraceWriter());
      this.clearCapture();
   }

   public void testSkipped(ReportEntry reportEntry) {
      WrappedReportEntry wrapped = this.wrap(reportEntry, ReportEntryType.skipped);
      this.detailsForThis.testSkipped(wrapped);
      if (this.statisticsReporter != null) {
         this.statisticsReporter.testSkipped(reportEntry);
      }

      this.clearCapture();
   }

   public void testAssumptionFailure(ReportEntry report) {
      this.testSkipped(report);
   }

   private WrappedReportEntry wrap(ReportEntry other, ReportEntryType reportEntryType) {
      int estimatedElapsed;
      if (reportEntryType != ReportEntryType.skipped) {
         if (other.getElapsed() != null) {
            estimatedElapsed = other.getElapsed();
         } else {
            estimatedElapsed = this.detailsForThis.getElapsedSinceLastStart();
         }
      } else {
         estimatedElapsed = 0;
      }

      return new WrappedReportEntry(other, reportEntryType, estimatedElapsed, this.testStdOut, this.testStdErr);
   }

   private WrappedReportEntry wrapTestSet(ReportEntry other) {
      return new WrappedReportEntry(other, (ReportEntryType)null, other.getElapsed() != null ? other.getElapsed() : this.detailsForThis.getElapsedSinceTestSetStart(), this.testStdOut, this.testStdErr);
   }

   public void close() {
      if (this.consoleOutputReceiver != null) {
         this.consoleOutputReceiver.close();
      }

   }
}

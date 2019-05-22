package org.apache.maven.plugin.surefire.booterclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.maven.surefire.report.ConsoleLogger;
import org.apache.maven.surefire.report.ConsoleOutputReceiver;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.RunListener;

public class MockReporter implements RunListener, ConsoleLogger, ConsoleOutputReceiver {
   private final List<String> events = new ArrayList();
   private final List<Object> data = new ArrayList();
   public static final String SET_STARTING = "SET_STARTED";
   public static final String SET_COMPLETED = "SET_COMPLETED";
   public static final String TEST_STARTING = "TEST_STARTED";
   public static final String TEST_SUCCEEDED = "TEST_COMPLETED";
   public static final String TEST_FAILED = "TEST_FAILED";
   public static final String TEST_ERROR = "TEST_ERROR";
   public static final String TEST_SKIPPED = "TEST_SKIPPED";
   public static final String TEST_ASSUMPTION_FAIL = "TEST_ASSUMPTION_SKIPPED";
   public static final String CONSOLE_OUTPUT = "CONSOLE_OUTPUT";
   public static final String STDOUT = "STDOUT";
   public static final String STDERR = "STDERR";
   private final AtomicInteger testSucceeded = new AtomicInteger();
   private final AtomicInteger testIgnored = new AtomicInteger();
   private final AtomicInteger testFailed = new AtomicInteger();

   public void testSetStarting(ReportEntry report) {
      this.events.add("SET_STARTED");
      this.data.add(report);
   }

   public void testSetCompleted(ReportEntry report) {
      this.events.add("SET_COMPLETED");
      this.data.add(report);
   }

   public void testStarting(ReportEntry report) {
      this.events.add("TEST_STARTED");
      this.data.add(report);
   }

   public void testSucceeded(ReportEntry report) {
      this.events.add("TEST_COMPLETED");
      this.testSucceeded.incrementAndGet();
      this.data.add(report);
   }

   public void testError(ReportEntry report) {
      this.events.add("TEST_ERROR");
      this.data.add(report);
      this.testFailed.incrementAndGet();
   }

   public void testFailed(ReportEntry report) {
      this.events.add("TEST_FAILED");
      this.data.add(report);
      this.testFailed.incrementAndGet();
   }

   public void testSkipped(ReportEntry report) {
      this.events.add("TEST_SKIPPED");
      this.data.add(report);
      this.testIgnored.incrementAndGet();
   }

   public List<String> getEvents() {
      return this.events;
   }

   public List getData() {
      return this.data;
   }

   public String getFirstEvent() {
      return (String)this.events.get(0);
   }

   public ReportEntry getFirstData() {
      return (ReportEntry)this.data.get(0);
   }

   public void testAssumptionFailure(ReportEntry report) {
      this.events.add("TEST_ASSUMPTION_SKIPPED");
      this.data.add(report);
      this.testIgnored.incrementAndGet();
   }

   public void info(String message) {
      this.events.add("CONSOLE_OUTPUT");
      this.data.add(message);
   }

   public void writeTestOutput(byte[] buf, int off, int len, boolean stdout) {
      this.events.add(stdout ? "STDOUT" : "STDERR");
      this.data.add(new String(buf, off, len));
   }
}

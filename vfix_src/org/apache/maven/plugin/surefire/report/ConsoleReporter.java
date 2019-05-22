package org.apache.maven.plugin.surefire.report;

import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.ReporterException;

public class ConsoleReporter {
   public static final String BRIEF = "brief";
   public static final String PLAIN = "plain";
   private static final String TEST_SET_STARTING_PREFIX = "Running ";
   private static final int BUFFER_SIZE = 4096;
   private final PrintWriter writer;

   public ConsoleReporter(PrintStream originalSystemOut) {
      OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(originalSystemOut, 4096));
      this.writer = new PrintWriter(out);
   }

   public void testSetStarting(ReportEntry report) throws ReporterException {
      this.writeMessage(getTestSetStartingMessage(report));
   }

   public void writeMessage(String message) {
      if (this.writer != null) {
         this.writer.print(message);
         this.writer.flush();
      }

   }

   public void writeLnMessage(String message) {
      if (this.writer != null) {
         this.writer.println(message);
         this.writer.flush();
      }

   }

   public void testSetCompleted(WrappedReportEntry report, TestSetStats testSetStats, List<String> testResults) throws ReporterException {
      this.writeMessage(testSetStats.getTestSetSummary(report));
      if (testResults != null) {
         Iterator i$ = testResults.iterator();

         while(i$.hasNext()) {
            String testResult = (String)i$.next();
            this.writeLnMessage(testResult);
         }
      }

   }

   public void reset() {
      if (this.writer != null) {
         this.writer.flush();
      }

   }

   static String getTestSetStartingMessage(ReportEntry report) {
      StringBuilder message = new StringBuilder();
      message.append("Running ");
      message.append(report.getNameWithGroup());
      message.append("\n");
      return message.toString();
   }
}

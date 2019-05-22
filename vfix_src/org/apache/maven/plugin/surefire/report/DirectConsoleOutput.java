package org.apache.maven.plugin.surefire.report;

import java.io.PrintStream;
import org.apache.maven.surefire.report.ReportEntry;

public class DirectConsoleOutput implements TestcycleConsoleOutputReceiver {
   private final PrintStream sout;
   private final PrintStream serr;

   public DirectConsoleOutput(PrintStream sout, PrintStream serr) {
      this.sout = sout;
      this.serr = serr;
   }

   public void writeTestOutput(byte[] buf, int off, int len, boolean stdout) {
      PrintStream stream = stdout ? this.sout : this.serr;
      stream.write(buf, off, len);
   }

   public void testSetStarting(ReportEntry reportEntry) {
   }

   public void testSetCompleted(ReportEntry report) {
   }

   public void close() {
   }
}

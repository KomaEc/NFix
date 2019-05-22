package org.apache.maven.surefire.booter;

import java.io.PrintStream;
import org.apache.maven.surefire.report.ReporterFactory;
import org.apache.maven.surefire.report.RunListener;
import org.apache.maven.surefire.suite.RunResult;

public class ForkingReporterFactory implements ReporterFactory {
   private final Boolean isTrimstackTrace;
   private final PrintStream originalSystemOut;
   private volatile int testSetChannelId = 1;

   public ForkingReporterFactory(Boolean trimstackTrace, PrintStream originalSystemOut) {
      this.isTrimstackTrace = trimstackTrace;
      this.originalSystemOut = originalSystemOut;
   }

   public synchronized RunListener createReporter() {
      return new ForkingRunListener(this.originalSystemOut, this.testSetChannelId++, this.isTrimstackTrace);
   }

   public RunResult close() {
      return new RunResult(17, 17, 17, 17);
   }
}

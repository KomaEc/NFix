package org.apache.maven.surefire.report;

import java.io.File;
import java.io.PrintStream;

public class ReporterConfiguration {
   private final File reportsDirectory;
   private final PrintStream originalSystemOut;
   private final Boolean trimStackTrace;

   public ReporterConfiguration(File reportsDirectory, Boolean trimStackTrace) {
      this.reportsDirectory = reportsDirectory;
      this.trimStackTrace = trimStackTrace;
      this.originalSystemOut = System.out;
   }

   public File getReportsDirectory() {
      return this.reportsDirectory;
   }

   public Boolean isTrimStackTrace() {
      return this.trimStackTrace;
   }

   public PrintStream getOriginalSystemOut() {
      return this.originalSystemOut;
   }
}

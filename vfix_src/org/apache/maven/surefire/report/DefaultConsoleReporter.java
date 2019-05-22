package org.apache.maven.surefire.report;

import java.io.PrintStream;

public class DefaultConsoleReporter implements ConsoleLogger {
   private final PrintStream systemOut;

   public DefaultConsoleReporter(PrintStream systemOut) {
      this.systemOut = systemOut;
   }

   public void info(String message) {
      this.systemOut.println(message);
   }
}

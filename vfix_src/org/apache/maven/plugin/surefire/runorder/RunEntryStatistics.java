package org.apache.maven.plugin.surefire.runorder;

import java.util.StringTokenizer;
import org.apache.maven.surefire.report.ReportEntry;

public class RunEntryStatistics {
   private final int runTime;
   private final int successfulBuilds;
   private final String testName;

   private RunEntryStatistics(int runTime, int successfulBuilds, String testName) {
      this.runTime = runTime;
      this.successfulBuilds = successfulBuilds;
      this.testName = testName;
   }

   public static RunEntryStatistics fromReportEntry(ReportEntry previous) {
      Integer elapsed = previous.getElapsed();
      return new RunEntryStatistics(elapsed != null ? elapsed : 0, 0, previous.getName());
   }

   public static RunEntryStatistics fromValues(int runTime, int successfulBuilds, Class clazz, String testName) {
      return new RunEntryStatistics(runTime, successfulBuilds, testName + "(" + clazz.getName() + ")");
   }

   public RunEntryStatistics nextGeneration(int runTime) {
      return new RunEntryStatistics(runTime, this.successfulBuilds + 1, this.testName);
   }

   public RunEntryStatistics nextGenerationFailure(int runTime) {
      return new RunEntryStatistics(runTime, 0, this.testName);
   }

   public String getTestName() {
      return this.testName;
   }

   public int getRunTime() {
      return this.runTime;
   }

   public int getSuccessfulBuilds() {
      return this.successfulBuilds;
   }

   public static RunEntryStatistics fromString(String line) {
      StringTokenizer tok = new StringTokenizer(line, ",");
      int successfulBuilds = Integer.parseInt(tok.nextToken());
      int runTime = Integer.parseInt(tok.nextToken());
      String className = tok.nextToken();
      return new RunEntryStatistics(runTime, successfulBuilds, className);
   }

   public String getAsString() {
      StringBuilder stringBuffer = new StringBuilder();
      stringBuffer.append(this.successfulBuilds);
      stringBuffer.append(",");
      stringBuffer.append(this.runTime);
      stringBuffer.append(",");
      stringBuffer.append(this.testName);
      return stringBuffer.toString();
   }
}

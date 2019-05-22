package org.apache.maven.surefire.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.apache.maven.plugin.surefire.report.TestSetStats;
import org.apache.maven.surefire.suite.RunResult;

public class RunStatistics {
   private final RunStatistics.Sources errorSources = new RunStatistics.Sources();
   private final RunStatistics.Sources failureSources = new RunStatistics.Sources();
   private int completedCount;
   private int errors;
   private int failures;
   private int skipped;

   public void addErrorSource(StackTraceWriter stackTraceWriter) {
      if (stackTraceWriter == null) {
         throw new IllegalArgumentException("Cant be null");
      } else {
         this.errorSources.addSource(stackTraceWriter);
      }
   }

   public void addFailureSource(StackTraceWriter stackTraceWriter) {
      if (stackTraceWriter == null) {
         throw new IllegalArgumentException("Cant be null");
      } else {
         this.failureSources.addSource(stackTraceWriter);
      }
   }

   public Collection<String> getErrorSources() {
      return this.errorSources.getListOfSources();
   }

   public Collection<String> getFailureSources() {
      return this.failureSources.getListOfSources();
   }

   public synchronized boolean hadFailures() {
      return this.failures > 0;
   }

   public synchronized boolean hadErrors() {
      return this.errors > 0;
   }

   public synchronized int getCompletedCount() {
      return this.completedCount;
   }

   public synchronized int getSkipped() {
      return this.skipped;
   }

   public synchronized void add(TestSetStats testSetStats) {
      this.completedCount += testSetStats.getCompletedCount();
      this.errors += testSetStats.getErrors();
      this.failures += testSetStats.getFailures();
      this.skipped += testSetStats.getSkipped();
   }

   public synchronized RunResult getRunResult() {
      return new RunResult(this.completedCount, this.errors, this.failures, this.skipped);
   }

   public synchronized String getSummary() {
      return "Tests run: " + this.completedCount + ", Failures: " + this.failures + ", Errors: " + this.errors + ", Skipped: " + this.skipped;
   }

   private static class Sources {
      private final Collection<String> listOfSources;

      private Sources() {
         this.listOfSources = new ArrayList();
      }

      void addSource(String source) {
         synchronized(this.listOfSources) {
            this.listOfSources.add(source);
         }
      }

      void addSource(StackTraceWriter stackTraceWriter) {
         this.addSource(stackTraceWriter.smartTrimmedStackTrace());
      }

      Collection<String> getListOfSources() {
         synchronized(this.listOfSources) {
            return Collections.unmodifiableCollection(this.listOfSources);
         }
      }

      // $FF: synthetic method
      Sources(Object x0) {
         this();
      }
   }
}

package org.apache.maven.plugin.surefire.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.plugin.surefire.StartupReportConfiguration;
import org.apache.maven.plugin.surefire.runorder.StatisticsReporter;
import org.apache.maven.surefire.report.DefaultDirectConsoleReporter;
import org.apache.maven.surefire.report.ReporterFactory;
import org.apache.maven.surefire.report.RunListener;
import org.apache.maven.surefire.report.RunStatistics;
import org.apache.maven.surefire.suite.RunResult;

public class DefaultReporterFactory implements ReporterFactory {
   private final RunStatistics globalStats = new RunStatistics();
   private final StartupReportConfiguration reportConfiguration;
   private final StatisticsReporter statisticsReporter;
   private final List<TestSetRunListener> listeners = Collections.synchronizedList(new ArrayList());

   public DefaultReporterFactory(StartupReportConfiguration reportConfiguration) {
      this.reportConfiguration = reportConfiguration;
      this.statisticsReporter = reportConfiguration.instantiateStatisticsReporter();
      this.runStarting();
   }

   public RunListener createReporter() {
      return this.createTestSetRunListener();
   }

   public RunListener createTestSetRunListener() {
      TestSetRunListener testSetRunListener = new TestSetRunListener(this.reportConfiguration.instantiateConsoleReporter(), this.reportConfiguration.instantiateFileReporter(), this.reportConfiguration.instantiateStatelessXmlReporter(), this.reportConfiguration.instantiateConsoleOutputFileReporter(), this.statisticsReporter, this.globalStats, this.reportConfiguration.isTrimStackTrace(), "plain".equals(this.reportConfiguration.getReportFormat()), this.reportConfiguration.isBriefOrPlainFormat());
      this.listeners.add(testSetRunListener);
      return testSetRunListener;
   }

   public RunResult close() {
      this.runCompleted();
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         TestSetRunListener listener = (TestSetRunListener)i$.next();
         listener.close();
      }

      return this.globalStats.getRunResult();
   }

   private DefaultDirectConsoleReporter createConsoleLogger() {
      return new DefaultDirectConsoleReporter(this.reportConfiguration.getOriginalSystemOut());
   }

   public void runStarting() {
      DefaultDirectConsoleReporter consoleReporter = this.createConsoleLogger();
      consoleReporter.info("");
      consoleReporter.info("-------------------------------------------------------");
      consoleReporter.info(" T E S T S");
      consoleReporter.info("-------------------------------------------------------");
   }

   private void runCompleted() {
      DefaultDirectConsoleReporter logger = this.createConsoleLogger();
      if (this.reportConfiguration.isPrintSummary()) {
         logger.info("");
         logger.info("Results :");
         logger.info("");
      }

      Iterator i$;
      String o;
      if (this.globalStats.hadFailures()) {
         logger.info("Failed tests: ");
         i$ = this.globalStats.getFailureSources().iterator();

         while(i$.hasNext()) {
            o = (String)i$.next();
            logger.info("  " + o);
         }

         logger.info("");
      }

      if (this.globalStats.hadErrors()) {
         logger.info("Tests in error: ");
         i$ = this.globalStats.getErrorSources().iterator();

         while(i$.hasNext()) {
            o = (String)i$.next();
            logger.info("  " + o);
         }

         logger.info("");
      }

      logger.info(this.globalStats.getSummary());
      logger.info("");
   }

   public RunStatistics getGlobalRunStatistics() {
      return this.globalStats;
   }

   public static DefaultReporterFactory defaultNoXml() {
      return new DefaultReporterFactory(StartupReportConfiguration.defaultNoXml());
   }
}

package org.apache.maven.plugin.surefire.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.ReporterException;

public class FileReporter {
   private final File reportsDirectory;
   private final boolean deleteOnStarting;
   private final String reportNameSuffix;

   public FileReporter(File reportsDirectory, String reportNameSuffix) {
      this.reportsDirectory = reportsDirectory;
      this.deleteOnStarting = false;
      this.reportNameSuffix = reportNameSuffix;
   }

   private PrintWriter testSetStarting(ReportEntry report) throws ReporterException {
      File reportFile = getReportFile(this.reportsDirectory, report.getName(), this.reportNameSuffix, ".txt");
      File reportDir = reportFile.getParentFile();
      reportDir.mkdirs();
      if (this.deleteOnStarting && reportFile.exists()) {
         reportFile.delete();
      }

      try {
         PrintWriter writer = new PrintWriter(new FileWriter(reportFile));
         writer.println("-------------------------------------------------------------------------------");
         writer.println("Test set: " + report.getName());
         writer.println("-------------------------------------------------------------------------------");
         return writer;
      } catch (IOException var5) {
         throw new ReporterException("Unable to create file for report: " + var5.getMessage(), var5);
      }
   }

   public static File getReportFile(File reportsDirectory, String reportEntryName, String reportNameSuffix, String fileExtension) {
      File reportFile;
      if (reportNameSuffix != null && reportNameSuffix.length() > 0) {
         reportFile = new File(reportsDirectory, FileReporterUtils.stripIllegalFilenameChars(reportEntryName + "-" + reportNameSuffix + fileExtension));
      } else {
         reportFile = new File(reportsDirectory, FileReporterUtils.stripIllegalFilenameChars(reportEntryName + fileExtension));
      }

      return reportFile;
   }

   public void testSetCompleted(WrappedReportEntry report, TestSetStats testSetStats, List<String> testResults) throws ReporterException {
      PrintWriter writer = this.testSetStarting(report);
      writer.print(testSetStats.getTestSetSummary(report));
      if (testResults != null) {
         Iterator i$ = testResults.iterator();

         while(i$.hasNext()) {
            String testResult = (String)i$.next();
            writer.println(testResult);
         }
      }

      writer.flush();
      writer.close();
   }
}

package org.apache.maven.plugin.surefire.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.ReporterException;

public class ConsoleOutputFileReporter implements TestcycleConsoleOutputReceiver {
   private final File reportsDirectory;
   private final String reportNameSuffix;
   private String reportEntryName;
   private FileOutputStream fileOutputStream;

   public ConsoleOutputFileReporter(File reportsDirectory, String reportNameSuffix) {
      this.reportsDirectory = reportsDirectory;
      this.reportNameSuffix = reportNameSuffix;
   }

   public void testSetStarting(ReportEntry reportEntry) {
      this.close();
      this.reportEntryName = reportEntry.getName();
   }

   public void testSetCompleted(ReportEntry report) throws ReporterException {
   }

   public void close() {
      if (this.fileOutputStream != null) {
         try {
            this.fileOutputStream.flush();
            this.fileOutputStream.close();
         } catch (IOException var2) {
         }

         this.fileOutputStream = null;
      }

   }

   public void writeTestOutput(byte[] buf, int off, int len, boolean stdout) {
      try {
         if (this.fileOutputStream == null) {
            if (!this.reportsDirectory.exists()) {
               this.reportsDirectory.mkdirs();
            }

            File file = FileReporter.getReportFile(this.reportsDirectory, this.reportEntryName, this.reportNameSuffix, "-output.txt");
            this.fileOutputStream = new FileOutputStream(file);
         }

         this.fileOutputStream.write(buf, off, len);
      } catch (IOException var6) {
         throw new RuntimeException(var6);
      }
   }
}

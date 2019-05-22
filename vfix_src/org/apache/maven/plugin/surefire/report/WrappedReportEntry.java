package org.apache.maven.plugin.surefire.report;

import java.text.NumberFormat;
import java.util.Locale;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.StackTraceWriter;

public class WrappedReportEntry implements ReportEntry {
   private final ReportEntry original;
   private final ReportEntryType reportEntryType;
   private final Integer elapsed;
   private final Utf8RecodingDeferredFileOutputStream stdout;
   private final Utf8RecodingDeferredFileOutputStream stdErr;
   private final NumberFormat numberFormat;
   private static final int MS_PER_SEC = 1000;
   static final String NL = System.getProperty("line.separator");

   public WrappedReportEntry(ReportEntry original, ReportEntryType reportEntryType, Integer estimatedElapsed, Utf8RecodingDeferredFileOutputStream stdout, Utf8RecodingDeferredFileOutputStream stdErr) {
      this.numberFormat = NumberFormat.getInstance(Locale.ENGLISH);
      this.original = original;
      this.reportEntryType = reportEntryType;
      this.elapsed = estimatedElapsed;
      this.stdout = stdout;
      this.stdErr = stdErr;
   }

   public Integer getElapsed() {
      return this.elapsed;
   }

   public ReportEntryType getReportEntryType() {
      return this.reportEntryType;
   }

   public Utf8RecodingDeferredFileOutputStream getStdout() {
      return this.stdout;
   }

   public Utf8RecodingDeferredFileOutputStream getStdErr() {
      return this.stdErr;
   }

   public String getSourceName() {
      return this.original.getSourceName();
   }

   public String getName() {
      return this.original.getName();
   }

   public String getGroup() {
      return this.original.getGroup();
   }

   public StackTraceWriter getStackTraceWriter() {
      return this.original.getStackTraceWriter();
   }

   public String getMessage() {
      return this.original.getMessage();
   }

   public String getStackTrace(boolean trimStackTrace) {
      StackTraceWriter writer = this.original.getStackTraceWriter();
      if (writer == null) {
         return null;
      } else {
         return trimStackTrace ? writer.writeTrimmedTraceToString() : writer.writeTraceToString();
      }
   }

   public String elapsedTimeAsString() {
      return this.elapsedTimeAsString((long)this.getElapsed());
   }

   String elapsedTimeAsString(long runTime) {
      return this.numberFormat.format((double)runTime / 1000.0D);
   }

   public String getReportName() {
      int i = this.getName().lastIndexOf("(");
      return i > 0 ? this.getName().substring(0, i) : this.getName();
   }

   public String getReportName(String suffix) {
      return suffix != null && suffix.length() > 0 ? this.getReportName() + "(" + suffix + ")" : this.getReportName();
   }

   public String getOutput(boolean trimStackTrace) {
      StringBuilder buf = new StringBuilder();
      buf.append(this.getElapsedTimeSummary());
      buf.append("  <<< ").append(this.getReportEntryType().toString().toUpperCase()).append("!").append(NL);
      buf.append(this.getStackTrace(trimStackTrace));
      return buf.toString();
   }

   public String getElapsedTimeSummary() {
      StringBuilder reportContent = new StringBuilder();
      reportContent.append(this.getName());
      reportContent.append("  Time elapsed: ");
      reportContent.append(this.elapsedTimeAsString());
      reportContent.append(" sec");
      return reportContent.toString();
   }

   public boolean isErrorOrFailure() {
      ReportEntryType thisType = this.getReportEntryType();
      return ReportEntryType.failure == thisType || ReportEntryType.error == thisType;
   }

   public boolean isSkipped() {
      return ReportEntryType.skipped == this.getReportEntryType();
   }

   public boolean isSucceeded() {
      return ReportEntryType.success == this.getReportEntryType();
   }

   public String getNameWithGroup() {
      return this.original.getNameWithGroup();
   }
}

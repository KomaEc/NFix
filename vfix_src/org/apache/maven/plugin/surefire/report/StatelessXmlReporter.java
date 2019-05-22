package org.apache.maven.plugin.surefire.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.ReporterException;
import org.apache.maven.surefire.report.SafeThrowable;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.IOUtil;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml.XMLWriter;

public class StatelessXmlReporter {
   private static final String ENCODING = "UTF-8";
   private static final Charset ENCODING_CS = Charset.forName("UTF-8");
   private final File reportsDirectory;
   private final String reportNameSuffix;
   private final boolean trimStackTrace;

   public StatelessXmlReporter(File reportsDirectory, String reportNameSuffix, boolean trimStackTrace) {
      this.reportsDirectory = reportsDirectory;
      this.reportNameSuffix = reportNameSuffix;
      this.trimStackTrace = trimStackTrace;
   }

   public void testSetCompleted(WrappedReportEntry testSetReportEntry, TestSetStats testSetStats) throws ReporterException {
      FileOutputStream outputStream = this.getOutputStream(testSetReportEntry);
      OutputStreamWriter fw = this.getWriter(outputStream);

      try {
         XMLWriter ppw = new org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml.PrettyPrintXMLWriter(fw);
         ppw.setEncoding("UTF-8");
         createTestSuiteElement(ppw, testSetReportEntry, testSetStats, this.reportNameSuffix);
         this.showProperties(ppw);
         Iterator i$ = testSetStats.getReportEntries().iterator();

         while(i$.hasNext()) {
            WrappedReportEntry entry = (WrappedReportEntry)i$.next();
            if (ReportEntryType.success.equals(entry.getReportEntryType())) {
               startTestElement(ppw, entry, this.reportNameSuffix);
               ppw.endElement();
            } else {
               this.getTestProblems(fw, ppw, entry, this.trimStackTrace, this.reportNameSuffix, outputStream);
            }
         }

         ppw.endElement();
      } finally {
         IOUtil.close((Writer)fw);
      }

   }

   private OutputStreamWriter getWriter(FileOutputStream fos) {
      return new OutputStreamWriter(fos, ENCODING_CS);
   }

   private FileOutputStream getOutputStream(WrappedReportEntry testSetReportEntry) {
      File reportFile = this.getReportFile(testSetReportEntry, this.reportsDirectory, this.reportNameSuffix);
      File reportDir = reportFile.getParentFile();
      reportDir.mkdirs();

      try {
         return new FileOutputStream(reportFile);
      } catch (Exception var5) {
         throw new ReporterException("When writing report", var5);
      }
   }

   private File getReportFile(ReportEntry report, File reportsDirectory, String reportNameSuffix) {
      File reportFile;
      if (reportNameSuffix != null && reportNameSuffix.length() > 0) {
         reportFile = new File(reportsDirectory, FileReporterUtils.stripIllegalFilenameChars("TEST-" + report.getName() + "-" + reportNameSuffix + ".xml"));
      } else {
         reportFile = new File(reportsDirectory, FileReporterUtils.stripIllegalFilenameChars("TEST-" + report.getName() + ".xml"));
      }

      return reportFile;
   }

   private static void startTestElement(XMLWriter ppw, WrappedReportEntry report, String reportNameSuffix) {
      ppw.startElement("testcase");
      ppw.addAttribute("name", report.getReportName());
      if (report.getGroup() != null) {
         ppw.addAttribute("group", report.getGroup());
      }

      if (report.getSourceName() != null) {
         if (reportNameSuffix != null && reportNameSuffix.length() > 0) {
            ppw.addAttribute("classname", report.getSourceName() + "(" + reportNameSuffix + ")");
         } else {
            ppw.addAttribute("classname", report.getSourceName());
         }
      }

      ppw.addAttribute("time", report.elapsedTimeAsString());
   }

   private static void createTestSuiteElement(XMLWriter ppw, WrappedReportEntry report, TestSetStats testSetStats, String reportNameSuffix1) {
      ppw.startElement("testsuite");
      ppw.addAttribute("name", report.getReportName(reportNameSuffix1));
      if (report.getGroup() != null) {
         ppw.addAttribute("group", report.getGroup());
      }

      ppw.addAttribute("time", testSetStats.getElapsedForTestSet());
      ppw.addAttribute("tests", String.valueOf(testSetStats.getCompletedCount()));
      ppw.addAttribute("errors", String.valueOf(testSetStats.getErrors()));
      ppw.addAttribute("skipped", String.valueOf(testSetStats.getSkipped()));
      ppw.addAttribute("failures", String.valueOf(testSetStats.getFailures()));
   }

   private void getTestProblems(OutputStreamWriter outputStreamWriter, XMLWriter ppw, WrappedReportEntry report, boolean trimStackTrace, String reportNameSuffix, FileOutputStream fw) {
      startTestElement(ppw, report, reportNameSuffix);
      ppw.startElement(report.getReportEntryType().name());
      String stackTrace = report.getStackTrace(trimStackTrace);
      if (report.getMessage() != null && report.getMessage().length() > 0) {
         ppw.addAttribute("message", extraEscape(report.getMessage(), true));
      }

      if (report.getStackTraceWriter() != null) {
         SafeThrowable t = report.getStackTraceWriter().getThrowable();
         if (t != null) {
            if (t.getMessage() != null) {
               ppw.addAttribute("type", stackTrace.contains(":") ? stackTrace.substring(0, stackTrace.indexOf(":")) : stackTrace);
            } else {
               ppw.addAttribute("type", (new StringTokenizer(stackTrace)).nextToken());
            }
         }
      }

      if (stackTrace != null) {
         ppw.writeText(extraEscape(stackTrace, false));
      }

      ppw.endElement();
      StatelessXmlReporter.EncodingOutputStream eos = new StatelessXmlReporter.EncodingOutputStream(fw);
      this.addOutputStreamElement(outputStreamWriter, fw, eos, ppw, report.getStdout(), "system-out");
      this.addOutputStreamElement(outputStreamWriter, fw, eos, ppw, report.getStdErr(), "system-err");
      ppw.endElement();
   }

   private void addOutputStreamElement(OutputStreamWriter outputStreamWriter, OutputStream fw, StatelessXmlReporter.EncodingOutputStream eos, XMLWriter xmlWriter, Utf8RecodingDeferredFileOutputStream utf8RecodingDeferredFileOutputStream, String name) {
      if (utf8RecodingDeferredFileOutputStream != null && utf8RecodingDeferredFileOutputStream.getByteCount() > 0L) {
         xmlWriter.startElement(name);

         try {
            xmlWriter.writeText("");
            outputStreamWriter.flush();
            utf8RecodingDeferredFileOutputStream.close();
            eos.getUnderlying().write(StatelessXmlReporter.ByteConstantsHolder.CDATA_START_BYTES);
            utf8RecodingDeferredFileOutputStream.writeTo(eos);
            eos.getUnderlying().write(StatelessXmlReporter.ByteConstantsHolder.CDATA_END_BYTES);
            eos.flush();
         } catch (IOException var8) {
            throw new ReporterException("When writing xml report stdout/stderr", var8);
         }

         xmlWriter.endElement();
      }

   }

   private void showProperties(XMLWriter xmlWriter) {
      xmlWriter.startElement("properties");
      Properties systemProperties = System.getProperties();
      if (systemProperties != null) {
         Enumeration propertyKeys = systemProperties.propertyNames();

         while(propertyKeys.hasMoreElements()) {
            String key = (String)propertyKeys.nextElement();
            String value = systemProperties.getProperty(key);
            if (value == null) {
               value = "null";
            }

            xmlWriter.startElement("property");
            xmlWriter.addAttribute("name", key);
            xmlWriter.addAttribute("value", extraEscape(value, true));
            xmlWriter.endElement();
         }
      }

      xmlWriter.endElement();
   }

   private static String extraEscape(String message, boolean attribute) {
      return !containsEscapesIllegalnXml10(message) ? message : escapeXml(message, attribute);
   }

   private static boolean containsEscapesIllegalnXml10(String message) {
      int size = message.length();

      for(int i = 0; i < size; ++i) {
         if (isIllegalEscape(message.charAt(i))) {
            return true;
         }
      }

      return false;
   }

   private static boolean isIllegalEscape(char c) {
      return isIllegalEscape((int)c);
   }

   private static boolean isIllegalEscape(int c) {
      return c >= 0 && c < 32 && c != 10 && c != 13 && c != 9;
   }

   private static String escapeXml(String text, boolean attribute) {
      StringBuilder sb = new StringBuilder(text.length() * 2);

      for(int i = 0; i < text.length(); ++i) {
         char c = text.charAt(i);
         if (isIllegalEscape(c)) {
            sb.append(attribute ? "&#" : "&amp#").append(c).append(';');
         } else {
            sb.append(c);
         }
      }

      return sb.toString();
   }

   private static class ByteConstantsHolder {
      private static final byte[] CDATA_START_BYTES;
      private static final byte[] CDATA_END_BYTES;
      private static final byte[] CDATA_ESCAPE_STRING_BYTES;
      private static final byte[] AMP_BYTES;

      static {
         try {
            CDATA_START_BYTES = "<![CDATA[".getBytes("UTF-8");
            CDATA_END_BYTES = "]]>".getBytes("UTF-8");
            CDATA_ESCAPE_STRING_BYTES = "]]><![CDATA[>".getBytes("UTF-8");
            AMP_BYTES = "&amp#".getBytes("UTF-8");
         } catch (UnsupportedEncodingException var1) {
            throw new RuntimeException(var1);
         }
      }
   }

   private static class EncodingOutputStream extends FilterOutputStream {
      private int c1;
      private int c2;

      public EncodingOutputStream(OutputStream out) {
         super(out);
      }

      public OutputStream getUnderlying() {
         return this.out;
      }

      private boolean isCdataEndBlock(int c) {
         return this.c1 == 93 && this.c2 == 93 && c == 62;
      }

      public void write(int b) throws IOException {
         if (this.isCdataEndBlock(b)) {
            this.out.write(StatelessXmlReporter.ByteConstantsHolder.CDATA_ESCAPE_STRING_BYTES);
         } else if (StatelessXmlReporter.isIllegalEscape(b)) {
            this.out.write(StatelessXmlReporter.ByteConstantsHolder.AMP_BYTES);
            this.out.write(String.valueOf(b).getBytes("UTF-8"));
            this.out.write(59);
         } else {
            this.out.write(b);
         }

         this.c1 = this.c2;
         this.c2 = b;
      }
   }
}

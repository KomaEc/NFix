package org.apache.maven.plugin.surefire.booterclient.output;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.maven.plugin.surefire.booterclient.lazytestprovider.TestProvidingInputStream;
import org.apache.maven.plugin.surefire.report.DefaultReporterFactory;
import org.apache.maven.surefire.report.CategorizedReportEntry;
import org.apache.maven.surefire.report.ConsoleLogger;
import org.apache.maven.surefire.report.ConsoleOutputReceiver;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.ReporterException;
import org.apache.maven.surefire.report.RunListener;
import org.apache.maven.surefire.report.StackTraceWriter;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.StreamConsumer;
import org.apache.maven.surefire.util.internal.StringUtils;

public class ForkClient implements StreamConsumer {
   private final DefaultReporterFactory defaultReporterFactory;
   private final TestProvidingInputStream testProvidingInputStream;
   private final Map<Integer, RunListener> testSetReporters;
   private final Properties testVmSystemProperties;
   private volatile boolean saidGoodBye;
   private volatile StackTraceWriter errorInFork;

   public ForkClient(DefaultReporterFactory defaultReporterFactory, Properties testVmSystemProperties) {
      this(defaultReporterFactory, testVmSystemProperties, (TestProvidingInputStream)null);
   }

   public ForkClient(DefaultReporterFactory defaultReporterFactory, Properties testVmSystemProperties, TestProvidingInputStream testProvidingInputStream) {
      this.testSetReporters = Collections.synchronizedMap(new HashMap());
      this.saidGoodBye = false;
      this.errorInFork = null;
      this.defaultReporterFactory = defaultReporterFactory;
      this.testVmSystemProperties = testVmSystemProperties;
      this.testProvidingInputStream = testProvidingInputStream;
   }

   public void consumeLine(String s) {
      try {
         if (s.length() == 0) {
            return;
         }

         byte operationId = (byte)s.charAt(0);
         int commma = s.indexOf(",", 3);
         if (commma < 0) {
            System.out.println(s);
            return;
         }

         Integer channelNumber = Integer.parseInt(s.substring(2, commma), 16);
         int rest = s.indexOf(",", commma);
         String remaining = s.substring(rest + 1);
         byte[] bytes;
         int len;
         switch(operationId) {
         case 49:
            this.getOrCreateReporter(channelNumber).testSetStarting(this.createReportEntry(remaining));
            break;
         case 50:
            this.getOrCreateReporter(channelNumber).testSetCompleted(this.createReportEntry(remaining));
            break;
         case 51:
            bytes = new byte[remaining.length()];
            len = StringUtils.unescapeBytes(bytes, remaining);
            this.getOrCreateConsoleOutputReceiver(channelNumber).writeTestOutput(bytes, 0, len, true);
            break;
         case 52:
            bytes = new byte[remaining.length()];
            len = StringUtils.unescapeBytes(bytes, remaining);
            this.getOrCreateConsoleOutputReceiver(channelNumber).writeTestOutput(bytes, 0, len, false);
            break;
         case 53:
            this.getOrCreateReporter(channelNumber).testStarting(this.createReportEntry(remaining));
            break;
         case 54:
            this.getOrCreateReporter(channelNumber).testSucceeded(this.createReportEntry(remaining));
            break;
         case 55:
            this.getOrCreateReporter(channelNumber).testError(this.createReportEntry(remaining));
            break;
         case 56:
            this.getOrCreateReporter(channelNumber).testFailed(this.createReportEntry(remaining));
            break;
         case 57:
            this.getOrCreateReporter(channelNumber).testSkipped(this.createReportEntry(remaining));
            break;
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 74:
         case 75:
         case 76:
         case 77:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 89:
         default:
            System.out.println(s);
            break;
         case 71:
            this.getOrCreateReporter(channelNumber).testAssumptionFailure(this.createReportEntry(remaining));
            break;
         case 72:
            this.getOrCreateConsoleLogger(channelNumber).info(this.createConsoleMessage(remaining));
            break;
         case 73:
            int keyEnd = remaining.indexOf(",");
            StringBuilder key = new StringBuilder();
            StringBuilder value = new StringBuilder();
            StringUtils.unescapeString(key, remaining.substring(0, keyEnd));
            StringUtils.unescapeString(value, remaining.substring(keyEnd + 1));
            synchronized(this.testVmSystemProperties) {
               this.testVmSystemProperties.put(key.toString(), value.toString());
               break;
            }
         case 78:
            if (null != this.testProvidingInputStream) {
               this.testProvidingInputStream.provideNewTest();
            }
            break;
         case 88:
            this.errorInFork = this.deserializeStackStraceWriter(new StringTokenizer(remaining, ","));
            break;
         case 90:
            this.saidGoodBye = true;
         }
      } catch (NumberFormatException var13) {
         System.out.println(s);
      } catch (ReporterException var14) {
         throw new RuntimeException(var14);
      }

   }

   public void consumeMultiLineContent(String s) throws IOException {
      BufferedReader stringReader = new BufferedReader(new StringReader(s));

      String s1;
      while((s1 = stringReader.readLine()) != null) {
         this.consumeLine(s1);
      }

   }

   private String createConsoleMessage(String remaining) {
      return this.unescape(remaining);
   }

   private ReportEntry createReportEntry(String untokenized) {
      StringTokenizer tokens = new StringTokenizer(untokenized, ",");

      try {
         String source = this.nullableCsv(tokens.nextToken());
         String name = this.nullableCsv(tokens.nextToken());
         String group = this.nullableCsv(tokens.nextToken());
         String message = this.nullableCsv(tokens.nextToken());
         String elapsedStr = tokens.nextToken();
         Integer elapsed = "null".equals(elapsedStr) ? null : Integer.decode(elapsedStr);
         StackTraceWriter stackTraceWriter = tokens.hasMoreTokens() ? this.deserializeStackStraceWriter(tokens) : null;
         return CategorizedReportEntry.reportEntry(source, name, group, stackTraceWriter, elapsed, message);
      } catch (RuntimeException var10) {
         throw new RuntimeException(untokenized, var10);
      }
   }

   private StackTraceWriter deserializeStackStraceWriter(StringTokenizer tokens) {
      String stackTraceMessage = this.nullableCsv(tokens.nextToken());
      String smartStackTrace = this.nullableCsv(tokens.nextToken());
      String stackTrace = tokens.hasMoreTokens() ? this.nullableCsv(tokens.nextToken()) : null;
      StackTraceWriter stackTraceWriter = stackTrace != null ? new DeserializedStacktraceWriter(stackTraceMessage, smartStackTrace, stackTrace) : null;
      return stackTraceWriter;
   }

   private String nullableCsv(String source) {
      return "null".equals(source) ? null : this.unescape(source);
   }

   private String unescape(String source) {
      StringBuilder stringBuffer = new StringBuilder(source.length());
      StringUtils.unescapeString(stringBuffer, source);
      return stringBuffer.toString();
   }

   public RunListener getReporter(Integer channelNumber) {
      return (RunListener)this.testSetReporters.get(channelNumber);
   }

   private RunListener getOrCreateReporter(Integer channelNumber) {
      RunListener reporter = (RunListener)this.testSetReporters.get(channelNumber);
      if (reporter == null) {
         reporter = this.defaultReporterFactory.createReporter();
         this.testSetReporters.put(channelNumber, reporter);
      }

      return reporter;
   }

   private ConsoleOutputReceiver getOrCreateConsoleOutputReceiver(Integer channelNumber) {
      return (ConsoleOutputReceiver)this.getOrCreateReporter(channelNumber);
   }

   private ConsoleLogger getOrCreateConsoleLogger(Integer channelNumber) {
      return (ConsoleLogger)this.getOrCreateReporter(channelNumber);
   }

   public void close(boolean hadTimeout) {
   }

   public boolean isSaidGoodBye() {
      return this.saidGoodBye;
   }

   public StackTraceWriter getErrorInFork() {
      return this.errorInFork;
   }

   public boolean isErrorInFork() {
      return this.errorInFork != null;
   }
}

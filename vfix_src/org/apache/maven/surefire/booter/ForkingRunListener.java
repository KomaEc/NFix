package org.apache.maven.surefire.booter;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.maven.surefire.report.ConsoleLogger;
import org.apache.maven.surefire.report.ConsoleOutputReceiver;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.RunListener;
import org.apache.maven.surefire.report.SafeThrowable;
import org.apache.maven.surefire.report.StackTraceWriter;
import org.apache.maven.surefire.util.internal.StringUtils;

public class ForkingRunListener implements RunListener, ConsoleLogger, ConsoleOutputReceiver {
   public static final byte BOOTERCODE_TESTSET_STARTING = 49;
   public static final byte BOOTERCODE_TESTSET_COMPLETED = 50;
   public static final byte BOOTERCODE_STDOUT = 51;
   public static final byte BOOTERCODE_STDERR = 52;
   public static final byte BOOTERCODE_TEST_STARTING = 53;
   public static final byte BOOTERCODE_TEST_SUCCEEDED = 54;
   public static final byte BOOTERCODE_TEST_ERROR = 55;
   public static final byte BOOTERCODE_TEST_FAILED = 56;
   public static final byte BOOTERCODE_TEST_SKIPPED = 57;
   public static final byte BOOTERCODE_TEST_ASSUMPTIONFAILURE = 71;
   public static final byte BOOTERCODE_CONSOLE = 72;
   public static final byte BOOTERCODE_SYSPROPS = 73;
   public static final byte BOOTERCODE_NEXT_TEST = 78;
   public static final byte BOOTERCODE_ERROR = 88;
   public static final byte BOOTERCODE_BYE = 90;
   private final PrintStream target;
   private final Integer testSetChannelId;
   private final boolean trimStackTraces;
   private final byte[] stdOutHeader;
   private final byte[] stdErrHeader;
   private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

   public ForkingRunListener(PrintStream target, int testSetChannelId, boolean trimStackTraces) {
      this.target = target;
      this.testSetChannelId = testSetChannelId;
      this.trimStackTraces = trimStackTraces;
      this.stdOutHeader = createHeader((byte)51, testSetChannelId);
      this.stdErrHeader = createHeader((byte)52, testSetChannelId);
      this.sendProps();
   }

   public void testSetStarting(ReportEntry report) {
      this.target.print(this.toString((byte)49, report, this.testSetChannelId));
   }

   public void testSetCompleted(ReportEntry report) {
      this.target.print(this.toString((byte)50, report, this.testSetChannelId));
   }

   public void testStarting(ReportEntry report) {
      this.target.print(this.toString((byte)53, report, this.testSetChannelId));
   }

   public void testSucceeded(ReportEntry report) {
      this.target.print(this.toString((byte)54, report, this.testSetChannelId));
   }

   public void testAssumptionFailure(ReportEntry report) {
      this.target.print(this.toString((byte)71, report, this.testSetChannelId));
   }

   public void testError(ReportEntry report) {
      this.target.print(this.toString((byte)55, report, this.testSetChannelId));
   }

   public void testFailed(ReportEntry report) {
      this.target.print(this.toString((byte)56, report, this.testSetChannelId));
   }

   public void testSkipped(ReportEntry report) {
      this.target.print(this.toString((byte)57, report, this.testSetChannelId));
   }

   void sendProps() {
      Properties systemProperties = System.getProperties();
      String key;
      String value;
      if (systemProperties != null) {
         for(Enumeration propertyKeys = systemProperties.propertyNames(); propertyKeys.hasMoreElements(); this.target.print(this.toPropertyString(key, value))) {
            key = (String)propertyKeys.nextElement();
            value = systemProperties.getProperty(key);
            if (value == null) {
               value = "null";
            }
         }
      }

   }

   public void writeTestOutput(byte[] buf, int off, int len, boolean stdout) {
      byte[] header = stdout ? this.stdOutHeader : this.stdErrHeader;
      byte[] content = new byte[buf.length * 3 + 1];
      int i = StringUtils.escapeBytesToPrintable(content, 0, buf, off, len);
      content[i++] = 10;
      synchronized(this.target) {
         this.target.write(header, 0, header.length);
         this.target.write(content, 0, i);
      }
   }

   public static byte[] createHeader(byte booterCode, int testSetChannel) {
      byte[] header = new byte[]{booterCode, 44, 0, 0, 0, 0, 44};
      int i = testSetChannel;
      int charPos = 6;
      int radix = 16;
      int mask = radix - 1;

      do {
         --charPos;
         header[charPos] = (byte)digits[i & mask];
         i >>>= 4;
      } while(i != 0);

      while(charPos > 2) {
         --charPos;
         header[charPos] = 48;
      }

      return header;
   }

   public void info(String message) {
      if (message != null) {
         StringBuilder sb = new StringBuilder(7 + message.length() * 5);
         this.append(sb, (byte)72);
         comma(sb);
         this.append(sb, Integer.toHexString(this.testSetChannelId));
         comma(sb);
         StringUtils.escapeToPrintable(sb, message);
         sb.append('\n');
         this.target.print(sb.toString());
      }
   }

   private String toPropertyString(String key, String value) {
      StringBuilder stringBuilder = new StringBuilder();
      this.append(stringBuilder, (byte)73);
      comma(stringBuilder);
      this.append(stringBuilder, Integer.toHexString(this.testSetChannelId));
      comma(stringBuilder);
      StringUtils.escapeToPrintable(stringBuilder, key);
      comma(stringBuilder);
      StringUtils.escapeToPrintable(stringBuilder, value);
      stringBuilder.append("\n");
      return stringBuilder.toString();
   }

   private String toString(byte operationCode, ReportEntry reportEntry, Integer testSetChannelId) {
      StringBuilder stringBuilder = new StringBuilder();
      this.append(stringBuilder, operationCode);
      comma(stringBuilder);
      this.append(stringBuilder, Integer.toHexString(testSetChannelId));
      comma(stringBuilder);
      nullableEncoding(stringBuilder, reportEntry.getSourceName());
      comma(stringBuilder);
      nullableEncoding(stringBuilder, reportEntry.getName());
      comma(stringBuilder);
      nullableEncoding(stringBuilder, reportEntry.getGroup());
      comma(stringBuilder);
      nullableEncoding(stringBuilder, reportEntry.getMessage());
      comma(stringBuilder);
      this.nullableEncoding(stringBuilder, reportEntry.getElapsed());
      this.encode(stringBuilder, reportEntry.getStackTraceWriter());
      stringBuilder.append("\n");
      return stringBuilder.toString();
   }

   private static void comma(StringBuilder stringBuilder) {
      stringBuilder.append(",");
   }

   private ForkingRunListener append(StringBuilder stringBuilder, String message) {
      stringBuilder.append(this.encode(message));
      return this;
   }

   private ForkingRunListener append(StringBuilder stringBuilder, byte b) {
      stringBuilder.append((char)b);
      return this;
   }

   private void nullableEncoding(StringBuilder stringBuilder, Integer source) {
      if (source == null) {
         stringBuilder.append("null");
      } else {
         stringBuilder.append(source.toString());
      }

   }

   private String encode(String source) {
      return source;
   }

   private static void nullableEncoding(StringBuilder stringBuilder, String source) {
      if (source != null && source.length() != 0) {
         StringUtils.escapeToPrintable(stringBuilder, source);
      } else {
         stringBuilder.append("null");
      }

   }

   private void encode(StringBuilder stringBuilder, StackTraceWriter stackTraceWriter) {
      encode(stringBuilder, stackTraceWriter, this.trimStackTraces);
   }

   public static void encode(StringBuilder stringBuilder, StackTraceWriter stackTraceWriter, boolean trimStackTraces) {
      if (stackTraceWriter != null) {
         comma(stringBuilder);
         SafeThrowable throwable = stackTraceWriter.getThrowable();
         if (throwable != null) {
            String message = throwable.getLocalizedMessage();
            nullableEncoding(stringBuilder, message);
         }

         comma(stringBuilder);
         nullableEncoding(stringBuilder, stackTraceWriter.smartTrimmedStackTrace());
         comma(stringBuilder);
         nullableEncoding(stringBuilder, trimStackTraces ? stackTraceWriter.writeTrimmedTraceToString() : stackTraceWriter.writeTraceToString());
      }

   }
}

package org.apache.maven.surefire.report;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.maven.surefire.util.internal.StringUtils;

public class LegacyPojoStackTraceWriter implements StackTraceWriter {
   private final Throwable t;
   private final String testClass;
   private final String testMethod;

   public LegacyPojoStackTraceWriter(String testClass, String testMethod, Throwable t) {
      this.testClass = testClass;
      this.testMethod = testMethod;
      this.t = t;
   }

   public String writeTraceToString() {
      StringWriter w = new StringWriter();
      if (this.t != null) {
         this.t.printStackTrace(new PrintWriter(w));
         w.flush();
      }

      return w.toString();
   }

   public String smartTrimmedStackTrace() {
      StringBuilder result = new StringBuilder();
      result.append(this.testClass);
      result.append("#");
      result.append(this.testMethod);
      SafeThrowable throwable = this.getThrowable();
      if (throwable.getTarget() instanceof AssertionError) {
         result.append(" ");
         result.append(getTruncatedMessage(throwable.getMessage(), 77 - result.length()));
      } else {
         Throwable target = throwable.getTarget();
         if (target != null) {
            result.append(" ");
            result.append(target.getClass().getSimpleName());
            result.append(getTruncatedMessage(throwable.getMessage(), 77 - result.length()));
         }
      }

      return result.toString();
   }

   private static String getTruncatedMessage(String msg, int i) {
      if (i < 0) {
         return "";
      } else if (msg == null) {
         return "";
      } else {
         String substring = msg.substring(0, Math.min(i, msg.length()));
         return i < msg.length() ? " " + substring + "..." : " " + substring;
      }
   }

   public String writeTrimmedTraceToString() {
      String text = this.writeTraceToString();
      String marker = "at " + this.testClass + "." + this.testMethod;
      String[] lines = StringUtils.split(text, "\n");
      int lastLine = lines.length - 1;
      int causedByLine = -1;

      for(int i = 1; i < lines.length; ++i) {
         String line = lines[i].trim();
         if (line.startsWith(marker)) {
            lastLine = i;
         } else if (line.startsWith("Caused by")) {
            causedByLine = i;
            break;
         }
      }

      StringBuilder trace = new StringBuilder();

      int i;
      for(i = 0; i <= lastLine; ++i) {
         trace.append(lines[i]);
         trace.append("\n");
      }

      if (causedByLine != -1) {
         for(i = causedByLine; i < lines.length; ++i) {
            trace.append(lines[i]);
            trace.append("\n");
         }
      }

      return trace.toString();
   }

   public SafeThrowable getThrowable() {
      return new SafeThrowable(this.t);
   }
}

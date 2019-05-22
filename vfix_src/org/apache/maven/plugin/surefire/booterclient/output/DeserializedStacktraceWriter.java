package org.apache.maven.plugin.surefire.booterclient.output;

import org.apache.maven.surefire.report.SafeThrowable;
import org.apache.maven.surefire.report.StackTraceWriter;

public class DeserializedStacktraceWriter implements StackTraceWriter {
   private final String message;
   private final String stackTrace;
   private final String smartTrimmed;

   public DeserializedStacktraceWriter(String message, String smartTrimmed, String stackTrace) {
      this.message = message;
      this.stackTrace = stackTrace;
      this.smartTrimmed = smartTrimmed;
   }

   public String smartTrimmedStackTrace() {
      return this.smartTrimmed;
   }

   public String writeTraceToString() {
      return this.stackTrace;
   }

   public String writeTrimmedTraceToString() {
      return this.stackTrace;
   }

   public SafeThrowable getThrowable() {
      return new SafeThrowable(new Throwable(this.message));
   }
}

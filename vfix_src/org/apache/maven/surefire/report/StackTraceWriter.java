package org.apache.maven.surefire.report;

public interface StackTraceWriter {
   String writeTraceToString();

   String writeTrimmedTraceToString();

   String smartTrimmedStackTrace();

   SafeThrowable getThrowable();
}

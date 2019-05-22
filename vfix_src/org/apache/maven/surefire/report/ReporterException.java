package org.apache.maven.surefire.report;

public class ReporterException extends RuntimeException {
   public ReporterException(String message, Exception nested) {
      super(message, nested);
   }
}

package org.apache.maven.surefire.booter;

public class SurefireExecutionException extends Exception {
   public SurefireExecutionException(String message, Throwable nested) {
      super(message, nested);
   }
}

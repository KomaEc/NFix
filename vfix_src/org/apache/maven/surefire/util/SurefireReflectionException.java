package org.apache.maven.surefire.util;

public class SurefireReflectionException extends RuntimeException {
   public SurefireReflectionException(Throwable cause) {
      super(cause == null ? "" : cause.toString(), cause);
   }
}

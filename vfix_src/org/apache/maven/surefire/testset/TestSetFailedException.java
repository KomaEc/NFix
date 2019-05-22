package org.apache.maven.surefire.testset;

public class TestSetFailedException extends Exception {
   public TestSetFailedException(String message) {
      super(message);
   }

   public TestSetFailedException(String message, Throwable cause) {
      super(message, cause);
   }

   public TestSetFailedException(Throwable cause) {
      super(cause == null ? "" : cause.toString(), cause);
   }
}

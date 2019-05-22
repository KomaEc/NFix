package org.apache.maven.scm.provider.accurev;

public class AccuRevException extends Exception {
   private static final long serialVersionUID = 1L;

   public AccuRevException() {
   }

   public AccuRevException(String message, Throwable cause) {
      super(message, cause);
   }

   public AccuRevException(String message) {
      super(message);
   }

   public AccuRevException(Throwable cause) {
      super(cause);
   }
}

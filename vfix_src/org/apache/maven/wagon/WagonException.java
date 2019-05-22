package org.apache.maven.wagon;

public abstract class WagonException extends Exception {
   private Throwable cause;

   public WagonException(String message, Throwable cause) {
      super(message);
      this.initCause(cause);
   }

   public WagonException(String message) {
      super(message);
   }

   public Throwable getCause() {
      return this.cause;
   }

   public Throwable initCause(Throwable cause) {
      this.cause = cause;
      return this;
   }
}

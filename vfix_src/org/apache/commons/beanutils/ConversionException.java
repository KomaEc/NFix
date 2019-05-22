package org.apache.commons.beanutils;

public class ConversionException extends RuntimeException {
   protected Throwable cause = null;

   public ConversionException(String message) {
      super(message);
   }

   public ConversionException(String message, Throwable cause) {
      super(message);
      this.cause = cause;
   }

   public ConversionException(Throwable cause) {
      super(cause.getMessage());
      this.cause = cause;
   }

   public Throwable getCause() {
      return this.cause;
   }
}

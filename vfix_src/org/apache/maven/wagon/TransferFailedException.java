package org.apache.maven.wagon;

public class TransferFailedException extends WagonException {
   public TransferFailedException(String message) {
      super(message);
   }

   public TransferFailedException(String message, Throwable cause) {
      super(message, cause);
   }
}

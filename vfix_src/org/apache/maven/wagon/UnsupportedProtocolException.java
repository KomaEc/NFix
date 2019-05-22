package org.apache.maven.wagon;

public class UnsupportedProtocolException extends WagonException {
   public UnsupportedProtocolException(String message) {
      super(message);
   }

   public UnsupportedProtocolException(String message, Throwable cause) {
      super(message, cause);
   }
}

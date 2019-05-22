package org.apache.maven.wagon;

public class ConnectionException extends WagonException {
   public ConnectionException(String message) {
      super(message);
   }

   public ConnectionException(String message, Throwable cause) {
      super(message, cause);
   }
}

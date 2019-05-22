package org.apache.maven.wagon;

public class ResourceDoesNotExistException extends WagonException {
   public ResourceDoesNotExistException(String message) {
      super(message);
   }

   public ResourceDoesNotExistException(String message, Throwable cause) {
      super(message, cause);
   }
}

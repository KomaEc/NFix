package org.codehaus.plexus.component.repository.exception;

public class ComponentProfileException extends Exception {
   public ComponentProfileException(String message) {
      super(message);
   }

   public ComponentProfileException(String message, Throwable cause) {
      super(message, cause);
   }
}

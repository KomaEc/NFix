package org.codehaus.plexus.component.repository.exception;

public class ComponentImplementationNotFoundException extends Exception {
   public ComponentImplementationNotFoundException(String message) {
      super(message);
   }

   public ComponentImplementationNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }
}

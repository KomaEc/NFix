package org.codehaus.plexus.component.repository.exception;

public class ComponentManagerImplementationNotFoundException extends Exception {
   public ComponentManagerImplementationNotFoundException(String message) {
      super(message);
   }

   public ComponentManagerImplementationNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }
}

package org.codehaus.plexus.component.repository.exception;

public class ComponentLookupException extends Exception {
   public ComponentLookupException(String message) {
      super(message);
   }

   public ComponentLookupException(String message, Throwable cause) {
      super(message, cause);
   }
}

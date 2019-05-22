package org.codehaus.plexus.component.repository.exception;

public class ComponentRepositoryException extends Exception {
   public ComponentRepositoryException(String message) {
      super(message);
   }

   public ComponentRepositoryException(String message, Throwable cause) {
      super(message, cause);
   }
}

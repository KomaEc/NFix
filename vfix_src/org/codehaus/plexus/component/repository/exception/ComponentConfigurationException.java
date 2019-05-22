package org.codehaus.plexus.component.repository.exception;

public class ComponentConfigurationException extends Exception {
   public ComponentConfigurationException(String message) {
      super(message);
   }

   public ComponentConfigurationException(String message, Throwable cause) {
      super(message, cause);
   }
}

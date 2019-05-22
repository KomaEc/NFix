package org.codehaus.plexus.component.repository.exception;

public class ComponentLifecycleException extends Exception {
   public ComponentLifecycleException(String message) {
      super(message);
   }

   public ComponentLifecycleException(String message, Throwable cause) {
      super(message, cause);
   }
}

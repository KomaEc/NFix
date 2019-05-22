package org.codehaus.plexus.component.repository.exception;

public class ComponentDescriptorUnmarshallingException extends Exception {
   public ComponentDescriptorUnmarshallingException(String message) {
      super(message);
   }

   public ComponentDescriptorUnmarshallingException(String message, Throwable cause) {
      super(message, cause);
   }
}

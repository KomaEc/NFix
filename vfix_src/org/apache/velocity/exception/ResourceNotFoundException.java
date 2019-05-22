package org.apache.velocity.exception;

public class ResourceNotFoundException extends VelocityException {
   private static final long serialVersionUID = -4287732191458420347L;

   public ResourceNotFoundException(String exceptionMessage) {
      super(exceptionMessage);
   }

   public ResourceNotFoundException(String exceptionMessage, Throwable t) {
      super(exceptionMessage, t);
   }

   public ResourceNotFoundException(Throwable t) {
      super(t);
   }
}

package org.codehaus.plexus.component.factory;

public class UndefinedComponentFactoryException extends Exception {
   public UndefinedComponentFactoryException(String message) {
      super(message);
   }

   public UndefinedComponentFactoryException(String message, Throwable cause) {
      super(message, cause);
   }

   public UndefinedComponentFactoryException(Throwable cause) {
      super(cause);
   }
}

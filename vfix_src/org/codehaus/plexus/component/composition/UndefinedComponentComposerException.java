package org.codehaus.plexus.component.composition;

public class UndefinedComponentComposerException extends Exception {
   public UndefinedComponentComposerException(String message) {
      super(message);
   }

   public UndefinedComponentComposerException(String message, Throwable cause) {
      super(message, cause);
   }

   public UndefinedComponentComposerException(Throwable cause) {
      super(cause);
   }
}

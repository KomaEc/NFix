package org.codehaus.plexus.component.composition;

public class CompositionException extends Exception {
   public CompositionException(String s) {
      super(s);
   }

   public CompositionException(String message, Throwable throwable) {
      super(message, throwable);
   }
}

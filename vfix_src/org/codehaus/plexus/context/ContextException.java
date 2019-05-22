package org.codehaus.plexus.context;

public class ContextException extends Exception {
   public ContextException(String message) {
      this(message, (Throwable)null);
   }

   public ContextException(String message, Throwable throwable) {
      super(message, throwable);
   }
}

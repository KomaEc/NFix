package org.codehaus.plexus.personality.plexus.lifecycle.phase;

public class InitializationException extends Exception {
   public InitializationException(String message) {
      super(message);
   }

   public InitializationException(String message, Throwable cause) {
      super(message, cause);
   }
}

package org.codehaus.plexus.personality.plexus.lifecycle.phase;

public class StoppingException extends Exception {
   public StoppingException(String message) {
      super(message);
   }

   public StoppingException(String message, Throwable cause) {
      super(message, cause);
   }
}

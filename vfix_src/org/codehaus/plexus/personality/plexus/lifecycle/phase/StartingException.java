package org.codehaus.plexus.personality.plexus.lifecycle.phase;

public class StartingException extends Exception {
   public StartingException(String message) {
      super(message);
   }

   public StartingException(String message, Throwable cause) {
      super(message, cause);
   }
}

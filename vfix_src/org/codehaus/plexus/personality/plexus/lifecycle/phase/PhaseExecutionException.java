package org.codehaus.plexus.personality.plexus.lifecycle.phase;

public class PhaseExecutionException extends Exception {
   public PhaseExecutionException(String message, Throwable throwable) {
      super(message, throwable);
   }
}

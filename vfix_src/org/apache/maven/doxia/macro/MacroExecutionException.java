package org.apache.maven.doxia.macro;

public class MacroExecutionException extends Exception {
   public MacroExecutionException(String message) {
      super(message);
   }

   public MacroExecutionException(String message, Throwable cause) {
      super(message, cause);
   }
}

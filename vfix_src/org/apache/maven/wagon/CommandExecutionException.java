package org.apache.maven.wagon;

public class CommandExecutionException extends WagonException {
   public CommandExecutionException(String message) {
      super(message);
   }

   public CommandExecutionException(String message, Throwable cause) {
      super(message, cause);
   }
}

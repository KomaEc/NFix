package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli;

public class CommandLineException extends Exception {
   public CommandLineException(String message) {
      super(message);
   }

   public CommandLineException(String message, Throwable cause) {
      super(message, cause);
   }
}

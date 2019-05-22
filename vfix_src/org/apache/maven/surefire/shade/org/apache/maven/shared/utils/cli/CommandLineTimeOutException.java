package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli;

public class CommandLineTimeOutException extends CommandLineException {
   public CommandLineTimeOutException(String message, Throwable cause) {
      super(message, cause);
   }
}

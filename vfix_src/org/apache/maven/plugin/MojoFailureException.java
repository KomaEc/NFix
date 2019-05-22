package org.apache.maven.plugin;

public class MojoFailureException extends AbstractMojoExecutionException {
   public MojoFailureException(Object source, String shortMessage, String longMessage) {
      super(shortMessage);
      this.source = source;
      this.longMessage = longMessage;
   }

   public MojoFailureException(String message) {
      super(message);
   }

   public MojoFailureException(String message, Throwable cause) {
      super(message, cause);
   }
}

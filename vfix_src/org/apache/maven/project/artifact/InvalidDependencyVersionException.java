package org.apache.maven.project.artifact;

public class InvalidDependencyVersionException extends Exception {
   public InvalidDependencyVersionException(String message, Exception cause) {
      super(message, cause);
   }
}

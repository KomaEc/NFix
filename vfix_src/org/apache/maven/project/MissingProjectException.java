package org.apache.maven.project;

public class MissingProjectException extends Exception {
   public MissingProjectException(String message) {
      super(message);
   }

   public MissingProjectException(String message, Exception e) {
      super(message, e);
   }
}

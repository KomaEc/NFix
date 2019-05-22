package org.apache.maven.scm;

public class ScmException extends Exception {
   static final long serialVersionUID = 5041965569154385323L;

   public ScmException(String message) {
      super(message);
   }

   public ScmException(String message, Throwable cause) {
      super(message, cause);
   }
}

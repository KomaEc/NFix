package org.codehaus.groovy;

public class GroovyBugError extends AssertionError {
   private String message;
   private final Exception exception;

   public GroovyBugError(String message) {
      this(message, (Exception)null);
   }

   public GroovyBugError(Exception exception) {
      this((String)null, exception);
   }

   public GroovyBugError(String msg, Exception exception) {
      this.exception = exception;
      this.message = msg;
   }

   public String toString() {
      return this.getMessage();
   }

   public String getMessage() {
      return this.message != null ? "BUG! " + this.message : "BUG! UNCAUGHT EXCEPTION: " + this.exception.getMessage();
   }

   public Throwable getCause() {
      return this.exception;
   }

   public String getBugText() {
      return this.message != null ? this.message : this.exception.getMessage();
   }

   public void setBugText(String msg) {
      this.message = msg;
   }
}

package org.codehaus.groovy;

public class GroovyException extends Exception implements GroovyExceptionInterface {
   private boolean fatal = true;

   public GroovyException() {
   }

   public GroovyException(String message) {
      super(message);
   }

   public GroovyException(String message, Throwable cause) {
      super(message, cause);
   }

   public GroovyException(boolean fatal) {
      this.fatal = fatal;
   }

   public GroovyException(String message, boolean fatal) {
      super(message);
      this.fatal = fatal;
   }

   public boolean isFatal() {
      return this.fatal;
   }

   public void setFatal(boolean fatal) {
      this.fatal = fatal;
   }
}

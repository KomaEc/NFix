package org.codehaus.groovy.control;

import org.codehaus.groovy.GroovyExceptionInterface;

public class ConfigurationException extends RuntimeException implements GroovyExceptionInterface {
   protected Exception cause;

   public ConfigurationException(Exception cause) {
      super(cause.getMessage());
      this.cause = cause;
   }

   public ConfigurationException(String message) {
      super(message);
   }

   public Throwable getCause() {
      return this.cause;
   }

   public boolean isFatal() {
      return true;
   }

   public void setFatal(boolean fatal) {
   }
}

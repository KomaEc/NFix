package org.codehaus.plexus.configuration.processor;

public class ConfigurationProcessingException extends Exception {
   public ConfigurationProcessingException(String message) {
      super(message);
   }

   public ConfigurationProcessingException(Throwable cause) {
      super(cause);
   }

   public ConfigurationProcessingException(String message, Throwable cause) {
      super(message, cause);
   }
}

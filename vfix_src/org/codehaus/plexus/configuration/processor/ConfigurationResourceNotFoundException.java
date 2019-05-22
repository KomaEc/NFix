package org.codehaus.plexus.configuration.processor;

public class ConfigurationResourceNotFoundException extends Exception {
   public ConfigurationResourceNotFoundException(String message) {
      super(message);
   }

   public ConfigurationResourceNotFoundException(Throwable cause) {
      super(cause);
   }

   public ConfigurationResourceNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }
}

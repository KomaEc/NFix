package org.codehaus.plexus.configuration;

public class PlexusConfigurationException extends Exception {
   public PlexusConfigurationException(String message) {
      this(message, (Throwable)null);
   }

   public PlexusConfigurationException(String message, Throwable throwable) {
      super(message, throwable);
   }
}

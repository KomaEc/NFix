package org.codehaus.plexus;

public class PlexusContainerException extends Exception {
   public PlexusContainerException(String message, Throwable throwable) {
      super(message, throwable);
   }

   public PlexusContainerException(String message) {
      super(message);
   }
}

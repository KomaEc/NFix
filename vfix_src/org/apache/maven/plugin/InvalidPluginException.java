package org.apache.maven.plugin;

public class InvalidPluginException extends Exception {
   public InvalidPluginException(String message, Exception e) {
      super(message, e);
   }
}

package org.apache.maven.plugin;

public class PluginManagerException extends Exception {
   public PluginManagerException(String message) {
      super(message);
   }

   public PluginManagerException(String message, Exception e) {
      super(message, e);
   }
}

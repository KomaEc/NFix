package org.apache.maven.plugin.descriptor;

public class DuplicateParameterException extends InvalidPluginDescriptorException {
   public DuplicateParameterException(String message) {
      super(message);
   }
}

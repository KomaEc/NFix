package org.codehaus.classworlds;

public class ConfigurationException extends Exception {
   public ConfigurationException(String msg) {
      super(msg);
   }

   public ConfigurationException(String msg, int lineNo, String line) {
      super(msg + " (" + lineNo + "): " + line);
   }
}

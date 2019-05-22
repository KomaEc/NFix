package org.apache.commons.digester.plugins;

public class PluginInvalidInputException extends PluginException {
   private Throwable cause;

   public PluginInvalidInputException(Throwable cause) {
      this(cause.getMessage());
      this.cause = cause;
   }

   public PluginInvalidInputException(String msg) {
      super(msg);
      this.cause = null;
   }

   public PluginInvalidInputException(String msg, Throwable cause) {
      this(msg);
      this.cause = cause;
   }
}

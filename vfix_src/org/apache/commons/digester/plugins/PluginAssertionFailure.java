package org.apache.commons.digester.plugins;

public class PluginAssertionFailure extends RuntimeException {
   private Throwable cause;

   public PluginAssertionFailure(Throwable cause) {
      this(cause.getMessage());
      this.cause = cause;
   }

   public PluginAssertionFailure(String msg) {
      super(msg);
      this.cause = null;
   }

   public PluginAssertionFailure(String msg, Throwable cause) {
      this(msg);
      this.cause = cause;
   }
}

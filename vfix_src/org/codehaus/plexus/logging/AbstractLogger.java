package org.codehaus.plexus.logging;

public abstract class AbstractLogger implements Logger {
   private int threshold;
   private String name;

   public AbstractLogger(int threshold, String name) {
      if (!this.isValidThreshold(threshold)) {
         throw new IllegalArgumentException("Threshold " + threshold + " is not valid");
      } else {
         this.threshold = threshold;
         this.name = name;
      }
   }

   public int getThreshold() {
      return this.threshold;
   }

   public void setThreshold(int threshold) {
      this.threshold = threshold;
   }

   public String getName() {
      return this.name;
   }

   public void debug(String message) {
      this.debug(message, (Throwable)null);
   }

   public boolean isDebugEnabled() {
      return this.threshold <= 0;
   }

   public void info(String message) {
      this.info(message, (Throwable)null);
   }

   public boolean isInfoEnabled() {
      return this.threshold <= 1;
   }

   public void warn(String message) {
      this.warn(message, (Throwable)null);
   }

   public boolean isWarnEnabled() {
      return this.threshold <= 2;
   }

   public void error(String message) {
      this.error(message, (Throwable)null);
   }

   public boolean isErrorEnabled() {
      return this.threshold <= 3;
   }

   public void fatalError(String message) {
      this.fatalError(message, (Throwable)null);
   }

   public boolean isFatalErrorEnabled() {
      return this.threshold <= 4;
   }

   protected boolean isValidThreshold(int threshold) {
      if (threshold == 0) {
         return true;
      } else if (threshold == 1) {
         return true;
      } else if (threshold == 2) {
         return true;
      } else if (threshold == 3) {
         return true;
      } else if (threshold == 4) {
         return true;
      } else {
         return threshold == 5;
      }
   }

   // $FF: synthetic method
   public abstract Logger getChildLogger(String var1);

   // $FF: synthetic method
   public abstract void fatalError(String var1, Throwable var2);

   // $FF: synthetic method
   public abstract void error(String var1, Throwable var2);

   // $FF: synthetic method
   public abstract void warn(String var1, Throwable var2);

   // $FF: synthetic method
   public abstract void info(String var1, Throwable var2);

   // $FF: synthetic method
   public abstract void debug(String var1, Throwable var2);
}

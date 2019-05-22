package org.apache.velocity.runtime.log;

public class Log {
   private LogChute chute;

   public Log() {
      this.setLogChute(new HoldingLogChute());
   }

   public Log(LogChute chute) {
      this.setLogChute(chute);
   }

   protected void setLogChute(LogChute chute) {
      if (chute == null) {
         throw new NullPointerException("The LogChute cannot be set to null!");
      } else {
         this.chute = chute;
      }
   }

   protected LogChute getLogChute() {
      return this.chute;
   }

   protected void log(int level, Object message) {
      this.getLogChute().log(level, String.valueOf(message));
   }

   protected void log(int level, Object message, Throwable t) {
      this.getLogChute().log(level, String.valueOf(message), t);
   }

   public boolean isTraceEnabled() {
      return this.getLogChute().isLevelEnabled(-1);
   }

   public void trace(Object message) {
      this.log(-1, message);
   }

   public void trace(Object message, Throwable t) {
      this.log(-1, message, t);
   }

   public boolean isDebugEnabled() {
      return this.getLogChute().isLevelEnabled(0);
   }

   public void debug(Object message) {
      this.log(0, message);
   }

   public void debug(Object message, Throwable t) {
      this.log(0, message, t);
   }

   public boolean isInfoEnabled() {
      return this.getLogChute().isLevelEnabled(1);
   }

   public void info(Object message) {
      this.log(1, message);
   }

   public void info(Object message, Throwable t) {
      this.log(1, message, t);
   }

   public boolean isWarnEnabled() {
      return this.getLogChute().isLevelEnabled(2);
   }

   public void warn(Object message) {
      this.log(2, message);
   }

   public void warn(Object message, Throwable t) {
      this.log(2, message, t);
   }

   public boolean isErrorEnabled() {
      return this.getLogChute().isLevelEnabled(3);
   }

   public void error(Object message) {
      this.log(3, message);
   }

   public void error(Object message, Throwable t) {
      this.log(3, message, t);
   }
}

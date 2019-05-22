package org.jboss.logging;

public class DynamicLogger extends Logger {
   private static final long serialVersionUID = -5963699806863917370L;
   public static final int LOG_LEVEL_NONE = 0;
   public static final int LOG_LEVEL_FATAL = 1;
   public static final int LOG_LEVEL_ERROR = 2;
   public static final int LOG_LEVEL_WARN = 3;
   public static final int LOG_LEVEL_INFO = 4;
   public static final int LOG_LEVEL_DEBUG = 5;
   public static final int LOG_LEVEL_TRACE = 6;
   public static final String[] LOG_LEVEL_STRINGS = new String[]{"NONE", "FATAL", "ERROR", "WARN", "INFO", "DEBUG", "TRACE"};
   private int logLevel = 5;

   public static DynamicLogger getDynamicLogger(String name) {
      return new DynamicLogger(name);
   }

   public static DynamicLogger getDynamicLogger(String name, String suffix) {
      return new DynamicLogger(name + "." + suffix);
   }

   public static DynamicLogger getDynamicLogger(Class<?> clazz) {
      return new DynamicLogger(clazz.getName());
   }

   public static DynamicLogger getDynamicLogger(Class<?> clazz, String suffix) {
      return new DynamicLogger(clazz.getName() + "." + suffix);
   }

   protected DynamicLogger(String name) {
      super(name);
   }

   public void setLogLevel(int logLevel) {
      if (logLevel >= 0 && logLevel <= 6) {
         this.logLevel = logLevel;
      }

   }

   public int getLogLevel() {
      return this.logLevel;
   }

   public void setLogLevelAsString(String logLevelString) {
      if (logLevelString != null) {
         logLevelString = logLevelString.toUpperCase().trim();

         for(int i = 0; i <= 6; ++i) {
            if (logLevelString.equals(LOG_LEVEL_STRINGS[i])) {
               this.logLevel = i;
               break;
            }
         }
      }

   }

   public String getLogLevelAsString() {
      return LOG_LEVEL_STRINGS[this.logLevel];
   }

   public void log(Object message) {
      switch(this.logLevel) {
      case 0:
      default:
         break;
      case 1:
         super.fatal(message);
         break;
      case 2:
         super.error(message);
         break;
      case 3:
         super.warn(message);
         break;
      case 4:
         super.info(message);
         break;
      case 5:
         super.debug(message);
         break;
      case 6:
         super.trace(message);
      }

   }

   public void log(Object message, Throwable t) {
      switch(this.logLevel) {
      case 0:
      default:
         break;
      case 1:
         super.fatal(message, t);
         break;
      case 2:
         super.error(message, t);
         break;
      case 3:
         super.warn(message, t);
         break;
      case 4:
         super.info(message, t);
         break;
      case 5:
         super.debug(message, t);
         break;
      case 6:
         super.trace(message, t);
      }

   }
}

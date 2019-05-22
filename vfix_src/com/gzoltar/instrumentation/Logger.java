package com.gzoltar.instrumentation;

public class Logger {
   private static Logger instance = null;
   private Logger.LogLevel logLevel;

   private Logger() {
      this.logLevel = Logger.LogLevel.INFO;
   }

   public static Logger getInstance() {
      if (instance == null) {
         instance = new Logger();
      }

      return instance;
   }

   public void setLogLevel(Logger.LogLevel var1) {
      this.logLevel = var1;
   }

   public Logger.LogLevel getLogLevel() {
      return this.logLevel;
   }

   public void info(String var1) {
      if (this.logLevel != Logger.LogLevel.NONE) {
         System.out.println(var1);
      }
   }

   public void info(byte[] var1, int var2, int var3) {
      if (this.logLevel != Logger.LogLevel.NONE) {
         System.out.write(var1, var2, var3);
      }
   }

   public void warn(String var1) {
      if (this.logLevel != Logger.LogLevel.NONE) {
         System.out.println("[WARN] " + var1);
      }
   }

   public void debug(String var1) {
      this.debug(var1, (Exception)null);
   }

   public void debug(String var1, Exception var2) {
      if (this.logLevel != Logger.LogLevel.NONE && this.logLevel == Logger.LogLevel.DEBUG) {
         System.out.println("[DEBUG] " + var1 + (var2 != null ? var2.getMessage() : ""));
      }
   }

   public void debug(byte[] var1, int var2, int var3) {
      if (this.logLevel != Logger.LogLevel.NONE && this.logLevel == Logger.LogLevel.DEBUG) {
         System.out.write(var1, var2, var3);
      }
   }

   public void err(String var1) {
      this.err(var1, (Throwable)null);
   }

   public void err(String var1, Throwable var2) {
      if (this.logLevel != Logger.LogLevel.NONE) {
         System.err.println("[ERROR] " + var1);
         if (var2 != null) {
            var2.printStackTrace();
         }

      }
   }

   public static enum LogLevel {
      NONE,
      INFO,
      DEBUG;
   }
}

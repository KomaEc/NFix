package com.mks.api.util;

import com.mks.api.response.APIException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Properties;

public class MKSLogger {
   private static final String API_CATEGORY_PREFIX = "IntegrityAPI.logging.";
   private Logger apiLogger;
   public static final String API = "API";
   public static final String ERROR = "ERROR";
   public static final String GENERAL = "GENERAL";
   public static final String WARNING = "WARNING";
   public static final String DEBUG = "DEBUG";
   public static final int HIGH = 0;
   public static final int MEDIUM = 5;
   public static final int LOW = 10;
   public static final int OFF = -1;
   public static final int MESSAGE = 1;
   public static final int EXCEPTION = 2;
   private static final String CREATE_LOGGER_ERROR_MSG = "Cannot create the logger instance.";
   private static final String CONFIG_LOGGER_ERROR_MSG = "Cannot configure the logger instance.";
   private static final String LOG_DEFAULT_MESSAGE_FMT = "{5} -- {2}({3}): {4}\n";
   private static final String LOG_DEFAULT_EXCEPTION_FMT = "{7} -- {2}({3}): {4}: {5}\n{6}";
   private Writer writer;

   public MKSLogger(String logFile) {
      try {
         if (logFile != null) {
            File tf = new File(logFile);
            if (tf.getParentFile() != null) {
               tf.getParentFile().mkdirs();
            }

            this.writer = new FileWriter(logFile, true);
         } else {
            this.writer = new PrintWriter(System.out, true);
         }
      } catch (IOException var3) {
         System.err.println("Cannot create the logger instance.");
         var3.printStackTrace(System.err);
         this.writer = new PrintWriter(System.out, true);
      }

   }

   public MKSLogger(File logFile) {
      try {
         if (logFile != null) {
            if (logFile.getParentFile() != null) {
               logFile.getParentFile().mkdirs();
            }

            this.writer = new FileWriter(logFile, true);
         } else {
            this.writer = new PrintWriter(System.out, true);
         }
      } catch (IOException var3) {
         System.err.println("Cannot create the logger instance.");
         var3.printStackTrace(System.err);
         this.writer = new PrintWriter(System.out, true);
      }

   }

   public void configure(Properties properties) {
      try {
         InternalAPILogListener ll = null;
         if (this.apiLogger == null) {
            this.apiLogger = new Logger(false);
            ll = new InternalAPILogListener(this.writer);
         } else {
            ll = this.apiLogger.getLogListeners().size() > 0 ? (InternalAPILogListener)this.apiLogger.getLogListeners().get(0) : null;
            if (ll == null) {
               ll = new InternalAPILogListener(this.writer);
            } else {
               ll.setWriter(this.writer);
            }
         }

         ll.setDefaultMessageFormat("{5} -- {2}({3}): {4}\n");
         ll.setDefaultExceptionFormat("{7} -- {2}({3}): {4}: {5}\n{6}");
         ll.configure(properties, "IntegrityAPI.logging.");
         this.apiLogger.addLogListener(ll);
         if (Logger.getApplicationLogger() == null) {
            Logger.setApplicationLogger(this.apiLogger);
         } else {
            Logger.getApplicationLogger().addLogListener(ll);
         }
      } catch (APIException var3) {
         System.err.println("Cannot configure the logger instance.");
         var3.printStackTrace(System.err);
      }

   }

   public void configure(String category, int type, int priority) {
      Properties properties = new Properties();
      String exCategory;
      if ((type & 1) != 0) {
         exCategory = "IntegrityAPI.logging.message.includeCategory." + category;
         properties.setProperty(exCategory, String.valueOf(priority));
      }

      if ((type & 2) != 0) {
         exCategory = "IntegrityAPI.logging.exception.includeCategory." + category;
         properties.setProperty(exCategory, String.valueOf(priority));
      }

      this.configure(properties);
   }

   public void exception(Class cls, String category, int priority, Throwable ex) {
      if (this.apiLogger != null) {
         Logger var10000 = this.apiLogger;
         Logger.exception(cls, category, priority, ex);
      }

   }

   public void exception(Object o, String category, int priority, Throwable ex) {
      if (this.apiLogger != null) {
         Logger var10000 = this.apiLogger;
         Logger.exception(o, category, priority, ex);
      }

   }

   public void exception(String category, int priority, Throwable ex) {
      if (this.apiLogger != null) {
         Logger var10000 = this.apiLogger;
         Logger.exception(category, priority, ex);
      }

   }

   public void exception(String category, Throwable ex) {
      if (this.apiLogger != null) {
         Logger var10000 = this.apiLogger;
         Logger.exception(category, ex);
      }

   }

   public void exception(Throwable ex) {
      if (this.apiLogger != null) {
         Logger var10000 = this.apiLogger;
         Logger.exception(ex);
      }

   }

   public void message(Class cls, String category, int priority, String msg) {
      if (this.apiLogger != null) {
         Logger var10000 = this.apiLogger;
         Logger.message(cls, category, priority, msg);
      }

   }

   public void message(Object o, String category, int priority, String msg) {
      if (this.apiLogger != null) {
         Logger var10000 = this.apiLogger;
         Logger.message(o, category, priority, msg);
      }

   }

   public void message(String category, int priority, String msg) {
      if (this.apiLogger != null) {
         Logger var10000 = this.apiLogger;
         Logger.message(category, priority, msg);
      }

   }

   public void message(String category, String msg) {
      if (this.apiLogger != null) {
         Logger var10000 = this.apiLogger;
         Logger.message(category, msg);
      }

   }

   public void message(String msg) {
      if (this.apiLogger != null) {
         Logger var10000 = this.apiLogger;
         Logger.message(msg);
      }

   }
}

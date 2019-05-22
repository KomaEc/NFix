package org.codehaus.plexus.logging.console;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.plexus.logging.AbstractLoggerManager;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.LoggerManager;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;

public class ConsoleLoggerManager extends AbstractLoggerManager implements LoggerManager, Initializable {
   private String threshold = "info";
   private int currentThreshold;
   private Map loggers;
   private int loggerCount;
   private boolean bootTimeLogger = false;

   public ConsoleLoggerManager() {
   }

   public ConsoleLoggerManager(String threshold) {
      this.threshold = threshold;
      this.bootTimeLogger = true;
      this.initialize();
   }

   public void initialize() {
      this.debug("Initializing ConsoleLoggerManager: " + this.hashCode() + ".");
      this.currentThreshold = this.parseThreshold(this.threshold);
      if (this.currentThreshold == -1) {
         this.debug("Could not parse the threshold level: '" + this.threshold + "', setting to debug.");
         this.currentThreshold = 0;
      }

      this.loggers = new HashMap();
   }

   public void setThreshold(int currentThreshold) {
      this.currentThreshold = currentThreshold;
   }

   public int getThreshold() {
      return this.currentThreshold;
   }

   public void setThreshold(String role, String roleHint, int threshold) {
      String name = this.toMapKey(role, roleHint);
      ConsoleLogger logger = (ConsoleLogger)this.loggers.get(name);
      if (logger == null) {
         this.debug("Trying to set the threshold of a unknown logger '" + name + "'.");
      } else {
         logger.setThreshold(threshold);
      }
   }

   public int getThreshold(String role, String roleHint) {
      String name = this.toMapKey(role, roleHint);
      ConsoleLogger logger = (ConsoleLogger)this.loggers.get(name);
      if (logger == null) {
         this.debug("Trying to get the threshold of a unknown logger '" + name + "'.");
         return 0;
      } else {
         return logger.getThreshold();
      }
   }

   public Logger getLoggerForComponent(String role, String roleHint) {
      String name = this.toMapKey(role, roleHint);
      Logger logger = (Logger)this.loggers.get(name);
      if (logger != null) {
         return logger;
      } else {
         this.debug("Creating logger '" + name + "' " + this.hashCode() + ".");
         Logger logger = new ConsoleLogger(this.getThreshold(), name);
         this.loggers.put(name, logger);
         return logger;
      }
   }

   public void returnComponentLogger(String role, String roleHint) {
      String name = this.toMapKey(role, roleHint);
      Object obj = this.loggers.remove(name);
      if (obj == null) {
         this.debug("There was no such logger '" + name + "' " + this.hashCode() + ".");
      } else {
         this.debug("Removed logger '" + name + "' " + this.hashCode() + ".");
      }

   }

   public int getActiveLoggerCount() {
      return this.loggers.size();
   }

   private int parseThreshold(String text) {
      text = text.trim().toLowerCase();
      if (text.equals("debug")) {
         return 0;
      } else if (text.equals("info")) {
         return 1;
      } else if (text.equals("warn")) {
         return 2;
      } else if (text.equals("error")) {
         return 3;
      } else {
         return text.equals("fatal") ? 4 : -1;
      }
   }

   private String decodeLogLevel(int logLevel) {
      switch(logLevel) {
      case 0:
         return "debug";
      case 1:
         return "info";
      case 2:
         return "warn";
      case 3:
         return "error";
      case 4:
         return "fatal";
      case 5:
         return "disabled";
      default:
         return "unknown";
      }
   }

   private void debug(String msg) {
   }
}

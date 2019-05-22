package org.codehaus.plexus.logging;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;

public abstract class BaseLoggerManager extends AbstractLoggerManager implements Initializable {
   private Map loggerCache = new HashMap();
   private String threshold = "info";
   private int currentThreshold;

   public void initialize() {
      this.currentThreshold = this.parseThreshold(this.threshold);
      if (this.currentThreshold == -1) {
         this.currentThreshold = 0;
      }

   }

   protected int parseThreshold(String text) {
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

   public void setThreshold(int currentThreshold) {
      this.currentThreshold = currentThreshold;
   }

   public int getThreshold() {
      return this.currentThreshold;
   }

   public void setThreshold(String role, String roleHint, int threshold) {
      String key = this.toMapKey(role, roleHint);
      AbstractLogger logger = (AbstractLogger)this.loggerCache.get(key);
      if (logger != null) {
         logger.setThreshold(threshold);
      }
   }

   public int getThreshold(String role, String roleHint) {
      String key = this.toMapKey(role, roleHint);
      AbstractLogger logger = (AbstractLogger)this.loggerCache.get(key);
      return logger == null ? 0 : logger.getThreshold();
   }

   public Logger getLoggerForComponent(String role, String roleHint) {
      String key = this.toMapKey(role, roleHint);
      Logger logger = (Logger)this.loggerCache.get(key);
      if (logger != null) {
         return logger;
      } else {
         logger = this.createLogger(key);
         this.loggerCache.put(key, logger);
         return logger;
      }
   }

   protected abstract Logger createLogger(String var1);

   public void returnComponentLogger(String role, String roleHint) {
      String key = this.toMapKey(role, roleHint);
      Object obj = this.loggerCache.remove(key);
      if (obj == null) {
         System.err.println("There was no such logger '" + key + "' " + this.hashCode() + ".");
      }

   }

   public int getActiveLoggerCount() {
      return this.loggerCache.size();
   }

   public String getThresholdAsString() {
      return this.threshold;
   }
}

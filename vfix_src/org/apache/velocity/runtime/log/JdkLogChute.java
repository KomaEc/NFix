package org.apache.velocity.runtime.log;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.velocity.runtime.RuntimeServices;

public class JdkLogChute implements LogChute {
   public static final String RUNTIME_LOG_JDK_LOGGER = "runtime.log.logsystem.jdk.logger";
   public static final String DEFAULT_LOG_NAME = "org.apache.velocity";
   protected Logger logger = null;

   public void init(RuntimeServices rs) {
      String name = (String)rs.getProperty("runtime.log.logsystem.jdk.logger");
      if (name == null) {
         name = "org.apache.velocity";
      }

      this.logger = Logger.getLogger(name);
      this.log(0, "JdkLogChute will use logger '" + name + '\'');
   }

   protected Level getJdkLevel(int level) {
      switch(level) {
      case -1:
         return Level.FINEST;
      case 0:
         return Level.FINE;
      case 1:
         return Level.INFO;
      case 2:
         return Level.WARNING;
      case 3:
         return Level.SEVERE;
      default:
         return Level.FINER;
      }
   }

   public void log(int level, String message) {
      this.log(level, message, (Throwable)null);
   }

   public void log(int level, String message, Throwable t) {
      Level jdkLevel = this.getJdkLevel(level);
      if (t == null) {
         this.logger.log(jdkLevel, message);
      } else {
         this.logger.log(jdkLevel, message, t);
      }

   }

   public boolean isLevelEnabled(int level) {
      Level jdkLevel = this.getJdkLevel(level);
      return this.logger.isLoggable(jdkLevel);
   }
}
